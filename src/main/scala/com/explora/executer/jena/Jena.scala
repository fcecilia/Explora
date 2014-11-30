package com.explora.executer.jena

import java.util.concurrent.Executors

import com.explora.executer.Executer
import com.explora.model.{Entity, Literal, RDFNode, Repository}
import com.explora.model.Repository.SPARQLResults
import scala.concurrent.{Future, ExecutionContext}
import scala.collection.immutable.Map

import com.hp.hpl.jena.query._

import scala.util.Try

/**
 * Created by fred on 29/11/2014.
 */
trait JenaContext {

  implicit val executer: Executer = JenaExecuter


}


object JenaExecuter extends Executer {

  lazy val newEc = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())


  def execute(req: String, rep: Repository)(implicit ex: ExecutionContext): Future[SPARQLResults] = {

    /**
     *
     * DON T USE DEFAULT ExecutionContext.Implicits.global WITH JENA
     *
     */

    implicit val ec = if (ex.eq(scala.concurrent.ExecutionContext.Implicits.global)) newEc else ex

    val query: Query = QueryFactory.create(req, Syntax.syntaxARQ)
    val qe: QueryExecution = QueryExecutionFactory.sparqlService(rep.url, query)
    val results = qe.execSelect


    import scala.collection.JavaConversions._
    val aBindingNames = results.getResultVars.toList



    def getter(selectQueryResult: ResultSet, accu: List[Map[String, RDFNode]]): List[Map[String, RDFNode]] = {

      if (selectQueryResult.hasNext) {

        val aBinding = selectQueryResult.next
        val accSub = aBindingNames.map { name =>

          val opt = Try(aBinding.get(name).asLiteral()).toOption
          val v: RDFNode = opt match {

            case None => Entity(aBinding.get(name).asResource().getURI)(rep)
            case Some(l) => Literal(l.getString, l.getLanguage)
          }

          name -> v
        }.toMap
        getter(selectQueryResult, accSub :: accu)
      } else {

        accu
      }
    }

    Future(SPARQLResults(getter(results, List.empty[Map[String, RDFNode]])))(ec)
  }

}

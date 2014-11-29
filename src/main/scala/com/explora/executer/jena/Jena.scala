package com.explora.executer.jena

import java.util

import com.explora.executer.Executer
import com.explora.model.Repository.SPARQLResults
import com.explora.model.{Entity, Literal, RDFNode, Repository}
import com.hp.hpl.jena.query._
import org.openrdf.query.TupleQueryResult

import scala.collection.immutable.Map
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

/**
 * Created by fred on 29/11/2014.
 */
trait JenaContext {

  implicit val executer: Executer = JenaExecuter


}


object JenaExecuter extends Executer {


  def execute(req: String, rep: Repository)(implicit ex: ExecutionContext): Future[SPARQLResults] = {
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

            case None => Entity(aBinding.get(name).asResource().getURI)(rep, ex)
            case Some(l) => Literal(l.getString, l.getLanguage)
          }

          v
          name -> v
        }.toMap
        getter(selectQueryResult, accSub :: accu)
      } else {

        accu
      }
    }

    val ter: List[Map[String, RDFNode]] = getter(results, List.empty[Map[String, RDFNode]])
    println(ter)
    Future(SPARQLResults(ter))

}

}

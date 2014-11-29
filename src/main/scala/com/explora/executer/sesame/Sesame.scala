package com.explora.executer.sesame

import com.explora.executer.Executer
import com.explora.model.{Entity, Literal, RDFNode, Repository}
import com.explora.model.Repository.SPARQLResults
import org.openrdf.model.impl.{URIImpl, LiteralImpl}
import org.openrdf.query.{TupleQueryResult, QueryLanguage}
import org.openrdf.repository.http.HTTPRepository

import scala.collection.immutable.Map
import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by fred on 29/11/2014.
 */
trait SesameContext {

  implicit val executer: Executer = SesameExecuter


}


object SesameExecuter extends Executer {


  def execute(req: String, rep: Repository)(implicit ex: ExecutionContext): Future[SPARQLResults] = {


    val repository = new HTTPRepository(rep.url)

    repository.initialize


    val connection = repository.getConnection

    val selectQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, req)

    val selectQueryResult = selectQuery.evaluate()

    import scala.collection.JavaConversions._
    val aBindingNames = selectQueryResult.getBindingNames.toList



    def getter(selectQueryResult: TupleQueryResult, accu: List[Map[String, RDFNode]]): List[Map[String, RDFNode]] = {

      if (selectQueryResult.hasNext) {

        val aBinding = selectQueryResult.next
        val accSub = aBindingNames.map { name =>

          val v = aBinding.getValue(name) match {
            case l: LiteralImpl => Literal(l.getLabel, l.getLanguage)
            case u: URIImpl => Entity(u.toString)(rep, ex)
          }

          name -> v
        }.toMap
        getter(selectQueryResult, accSub :: accu)
      } else {

        accu
      }
    }

    Future(SPARQLResults(getter(selectQueryResult, List.empty[Map[String, RDFNode]])))

  }

}

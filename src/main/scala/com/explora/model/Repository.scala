package com.explora.model

import java.util.concurrent.Executors

import com.explora.model.Repository.{SPARQLResult, SPARQLResults}
import org.openrdf.model.impl.{LiteralImpl, URIImpl}
import org.openrdf.query.{QueryLanguage, TupleQueryResult}
import org.openrdf.repository.http.HTTPRepository

import scala.collection.immutable.Map
import scala.concurrent.{ExecutionContext, Future}

object Repository {

  case class SPARQLResults(stream: List[Map[String, RDFNode]]) {

    def isEmpty = stream.isEmpty

    def apply(variable: String) = stream.map(map => map.get(variable)).filter(_ != None).map(t => t.get)

    def get(data: String) = stream.map(_(data))


  }

  type SPARQLResult = Map[String, RDFNode]
  type SPARQLResultStream = Stream[SPARQLResult]

}


case class Repository(url: String) {

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())


  def executeOne(req: String): Future[Option[SPARQLResult]] = execute(req).map(resul => if (resul.isEmpty) None else Some(resul.stream(0)))


  def execute(req: String): Future[SPARQLResults] = {


    val repository = new HTTPRepository(url)

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
            case u: URIImpl => Entity(u.toString)(this)

          }

          name -> v
        }.toMap
        getter(selectQueryResult,  accSub :: accu)
      } else {

        accu
      }
    }

    Future(SPARQLResults(getter(selectQueryResult, List.empty[Map[String, RDFNode]])))

  }


}



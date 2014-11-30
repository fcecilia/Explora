package com.explora.model

import com.explora.executer.Executer
import com.explora.model.Repository.{SPARQLResult, SPARQLResults}

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

  def executeOne(req: String)( implicit ex: Executer, ec: ExecutionContext): Future[Option[SPARQLResult]] = execute(req).map(resul => if (resul.isEmpty) None else Some(resul.stream(0)))


  def execute(req: String)(implicit ex: Executer, ec: ExecutionContext): Future[SPARQLResults] = {

    ex.execute(req, this)

  }


}



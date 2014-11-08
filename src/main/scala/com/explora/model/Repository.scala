package com.explora.model

import scala.xml._
import scala.concurrent.Future
import com.explora.helper.ExploraHelper
import scala.collection.immutable.Map
import com.explora.model.Repository.{SPARQLResults, SPARQLResult}


import scala.concurrent.ExecutionContext
import akka.actor.ActorSystem

import scala.util.Try
import scala.util.Failure
import scala.util.Success


import spray.client.pipelining._
import java.util.concurrent.Executors

object Repository {
  implicit val system = ActorSystem()

  case class SPARQLResults(stream: Stream[Map[String, RDFNode]]) {

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


  def makeQuery(req: String) = {
    val SPARQL = ExploraHelper.format(req)
    s"$url?query=$SPARQL&format=xml"

  }


  def responseXML(req: String, rep: String) = {


    Try(scala.xml.XML.loadString(rep)) match {

      case (Success(data)) => analyseXML(data)
      case (Failure(f)) => {
        f.printStackTrace()
        println(rep)
        Stream()
      }

    }

  }


  def analyseXML(xml: Elem): Stream[Map[String, RDFNode]] = {

    def extractUri(b: Node, name: String): Map[String, RDFNode] = {
      val uriMap = (b \ "uri").map {
        u => (name, Entity(u.text)(this))

      }
      uriMap.toMap[String, RDFNode]
    }

    def extractLiteral(b: Node, x: Node, name: String): Map[String, RDFNode] = {
      val uriMap = (b \ "literal").map {
        l =>

          x.attribute("xml:lang") match {
            case Some(t) => (name, Literal(l.text, t(0).toString()))
            case None => (name, Literal(l.text))
          }
      }
      uriMap.toMap[String, RDFNode]
    }

    val seq: NodeSeq = xml \ "results" \ "result"
    val results = seq.toStream.map {
      x =>

        val bind = (x \ "binding").toStream.map {
          b =>

            val name: String = b.attribute("name").map {
              t => t(0).toString()
            }.get

            extractUri(b, name) ++ extractLiteral(b, x, name)

        }
        bind.reduce((a: Map[String, RDFNode], b: Map[String, RDFNode]) => a ++ b)


    }


    results
  }


  def execute(req: String): Future[SPARQLResults] = {

    import Repository.system

    val pipeline = sendReceive ~> unmarshal[String]
    val future = pipeline(Get(makeQuery(req)))
    for (rep <- future) yield {
      val response = responseXML(req, rep)

      SPARQLResults(response)

    }

  }


}



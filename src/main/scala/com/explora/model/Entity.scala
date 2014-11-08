package com.explora.model

import scala.concurrent.Future
import scala.concurrent.future
import scala.concurrent.ExecutionContext.Implicits.global
import scala._
import scala.collection.immutable.Map

case class Entity(uri: String)(implicit repository: Repository) extends RDFNode {


  val value = uri

  def valueFrom(value: Entity): Future[Stream[RDFNode]] = valueFrom(value.uri)


  def valueFrom(value: String): Future[Stream[RDFNode]] = {

    val onto = value
    val request = s"""SELECT ?data WHERE { ?data <$onto> <$uri> } """
    /* val resultF = repository.execute(request)
     resultF.map(resul => for (listMap <- resul.stream) yield listMap.get("data").get)*/
    repository.execute(request).map(_("data"))

  }


  def valueOf(value: String): Future[Stream[RDFNode]] = valueOf(Entity(value))


  def valueOf(ontology: Entity): Future[Stream[RDFNode]] = {


    def request(en_uri: String, ont_uri: String) = s"""SELECT ?data WHERE { <$en_uri> <$ont_uri> ?data } """

    def explorationList(ens: Stream[Entity], ontos: Stream[Entity]) = {
      Future.sequence(for (en <- ens; onto <- ontos) yield exploration(en, onto))

    }


    def exploration(en: Entity, onto: Entity) = {


      val SPARQL = request(en.uri, onto.uri)
      val resultatF = repository.execute(SPARQL)
      for (resultat <- resultatF) yield
      {
        resultat.stream.flatMap(_.values.toList)
      }
    }





      exploration(this, ontology)

  }


  def validUri(entity: Entity) {
    uri.contains(repository.url.replace("sparql", ""))

  }

  lazy val ontologies: Future[List[Entity]] = {


    val request = s"""SELECT distinct ?onto WHERE { <$uri> ?onto ?data }"""
    val resultatF = repository.execute(request)
    val result = for (resultat <- resultatF) yield resultat.stream.flatMap(_.values.toList.map(f => f match {

      case p: Entity => p
    }))

    result.map(_.toList)
  }

  lazy val linkedEntity: Future[Stream[Entity]] = {

    val request = s"""SELECT distinct ?enti WHERE { <$uri> ?onto ?enti }"""
    val resultatF = repository.execute(request)
    val result = for (resultat <- resultatF) yield resultat.stream.flatMap(_.values.filter {
      case p: Entity => true
      case p: Literal => false
    } map {

      case p: Entity => p
    })
    result
  }







}






package com.explora

import com.explora.executer.jena.JenaContext
import com.explora.executer.sesame.SesameContext
import com.explora.model.Repository

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: fred
 * Date: 14/08/13
 * Time: 17:29
 */
object RequestBase1 extends App with JenaContext{

  val rep = Repository("http://fr.dbpedia.org/sparql")

  val req = "SELECT ?data WHERE { ?data  <http://dbpedia.org/ontology/wikiPageRedirects>  <http://fr.dbpedia.org/resource/Paris>} "

  val resultF = rep.execute(req)


  resultF.map { r =>
    println(r.stream.size)
    r.stream.foreach(p => println(p))
  }


}

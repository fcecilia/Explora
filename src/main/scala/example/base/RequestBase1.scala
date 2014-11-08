package example.base

import com.explora.model.Repository

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: fred
 * Date: 14/08/13
 * Time: 17:29
 */
object RequestBase1 extends App {

  val rep = Repository("http://fr.dbpedia.org/sparql")

  val req = "SELECT ?data WHERE { ?data  <http://dbpedia.org/ontology/wikiPageRedirects>  <http://fr.dbpedia.org/resource/Paris>} "

  val resultF = rep.execute(req)


  resultF.map { r =>
    r.stream.foreach(p => println(p))
  }


}

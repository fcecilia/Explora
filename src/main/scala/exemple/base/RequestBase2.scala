package exemple.base

import com.explora.model.Repository

import scala.concurrent.ExecutionContext.Implicits.global

object RequestBase2 extends App {

  val uri = "http://fr.dbpedia.org/resource/Tours"
  val req = s"""
        PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        PREFIX foaf: <http://xmlns.com/foaf/0.1/>
        SELECT ?label ?abstract ?thumbnail ?depiction ?wikipage
        WHERE {  <$uri> rdfs:label ?label .
           OPTIONAL {
            <$uri> dbpedia-owl:abstract ?abstract .
           }
            OPTIONAL {
           <$uri> dbpedia-owl:thumbnail ?thumbnail .
             <$uri> foaf:depiction ?depiction .
           }
            OPTIONAL {
            <$uri> foaf:isPrimaryTopicOf ?wikipage .
            }
          }"""

  val rep = Repository("http://fr.dbpedia.org/sparql")

  val result = rep.execute(req)


  result.map { r =>
    r.stream.foreach(p => println(p))
  }


}
package example.movies

import com.explora.ld.dbpedia.LiveDBPedia
import com.explora.model.{Entity, Literal, RDFNode}

import com.explora.pattern.QueryHelper._
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by fred on 08/11/2014.
 */
object MoviesReco extends App with LiveDBPedia {
  val smillis = System.currentTimeMillis()

  println(repository)
  val film = uri("The_Matrix")
  //val film = uri("Men_in_Black_(film)")
  //  val film = uri("The_Terminator")
  // val film = uri("Jurassic_Park_(film)")
  // val film = uri("Pretty_Woman")

  val request = s"""
select distinct ?movies ?Concept where {
  {
    <$film> <http://dbpedia.org/ontology/starring> ?Concept.
    ?movies <http://dbpedia.org/ontology/starring> ?Concept.
  } UNION {
    <$film> <http://dbpedia.org/ontology/director> ?Concept.
    ?movies <http://dbpedia.org/ontology/director> ?Concept.
  } UNION {
    <$film> <http://purl.org/dc/terms/subject> ?Concept.
     ?movies <http://purl.org/dc/terms/subject> ?Concept.
   }

} """

  request.execute.map { result =>
    val nodes: Stream[RDFNode] = result.get("movies")

    val movies: Stream[String] = nodes.map {
      case Literal(value, opt) => None
      case Entity(uri) => Some(uri)
    }.filter(m => m != None && m != Some(film.uri)).map(_.get)

    val by: List[(String, Int)] = movies.groupBy(a => a).map(m => (m._1, m._2.size)).toList.sortBy(-_._2).take(10)


    println(System.currentTimeMillis() - smillis)

    by.foreach(println)

  }

}
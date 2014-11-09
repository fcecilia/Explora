package example.movies

/**
 * Created by fred on 09/11/2014.
 */


import com.explora.ld.dbpedia.DBPedia
import com.explora.pattern.ExploraHelper._

import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by fred on 08/11/2014.
 */
object SimpleMovieReco extends App with DBPedia {
  val smillis = System.currentTimeMillis()

  println(repository)
  val film = DBEntity("The_Matrix")


  val nodesF = s"""
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

} """ executeAndGet "movies"

  nodesF.map { nodes =>

    val movies: Stream[String] = nodes.onlyEntities.filter(n => n != film).map(_.uri)

    val by: List[(String, Int)] = movies.groupBy(a => a).map(m => (m._1, m._2.size)).toList.sortBy(-_._2).take(10)

    println(System.currentTimeMillis() - smillis)

    by.foreach(println)

  }

}

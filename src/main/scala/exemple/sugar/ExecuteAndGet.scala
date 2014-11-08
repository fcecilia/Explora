package exemple.sugar

import com.explora.ld.dbpedia.DBPedia
import com.explora.pattern.QueryHelper._

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by fred on 24/03/2014.
 */
object ExecuteAndGet extends App with DBPedia {


  val resultF = "SELECT ?data WHERE { ?data  <http://dbpedia.org/ontology/wikiPageRedirects>  <http://dbpedia.org/resource/Paris>}" executeAndGet "data"


  resultF.map { result =>
    result.foreach(println)
  }


}

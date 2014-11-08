package example.sugar

import com.explora.ld.dbpedia.DBPediaFr
import com.explora.model.{Entity, Literal}
import com.explora.pattern.QueryHelper._

import scala.concurrent.ExecutionContext.Implicits.global

object ValueExample extends App with DBPediaFr {


  val National_Gallery = "http://fr.dbpedia.org/resource/National_Gallery".entity

  val wikiPageWikiLink: Entity = "http://dbpedia.org/ontology/wikiPageWikiLink".entity

  val valueOfF = National_Gallery valueOf wikiPageWikiLink

  valueOfF.map { tr =>

    println("====================valueOf====================")
    tr.foreach {
      case Literal(value, opt) => println(value)
      case Entity(uri) => println(uri)
    }
  }

  val pageRedirect: Entity = "http://dbpedia.org/ontology/wikiPageRedirects".entity

  val valueFrom = National_Gallery valueFrom pageRedirect

  valueFrom.map { tr =>
    println("====================valueFrom====================")

    tr.foreach {
      case Literal(value, opt) => println(value)
      case Entity(uri) => println(uri)
    }
  }

}
package exemple.sugar

import com.explora.ld.dbpedia.DBPediaFr
import com.explora.model.{Entity, Literal}
import com.explora.pattern.QueryHelper._

import scala.concurrent.ExecutionContext.Implicits.global

object ValueExample extends App with DBPediaFr{



  //Create Entity like that ...
  val National_Gallery = Entity("http://fr.dbpedia.org/resource/National_Gallery")

  //Or Like that
  val wikiPageRedirects = "http://dbpedia.org/ontology/wikiPageRedirects".entity

  val valueOfF = National_Gallery valueOf wikiPageRedirects

  valueOfF.map { tr =>

    println("====================valueOf====================")
    tr.foreach {
      case Literal(value, opt) => println(value)
      case Entity(uri) => println(uri)
    }
  }

  val valueFrom = National_Gallery valueFrom wikiPageRedirects

  valueFrom.map { tr =>
    println("====================valueFrom====================")

    tr.foreach {
      case Literal(value, opt) => println(value)
      case Entity(uri) => println(uri)
    }
  }

}
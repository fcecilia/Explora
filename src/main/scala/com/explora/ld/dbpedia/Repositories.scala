package com.explora.ld.dbpedia

import com.explora.model.Repository

/**
 * Created by fred on 15/03/2014.
 */
trait DBPedia {

  implicit val dbpediaorg = Repository("http://dbpedia.org/sparql")

}

trait DBPediaFr {

  implicit val frdbpediaorg = Repository("http://fr.dbpedia.org/sparql")

}

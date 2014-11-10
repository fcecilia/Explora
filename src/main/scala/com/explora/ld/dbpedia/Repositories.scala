package com.explora.ld.dbpedia

import com.explora.model.{Entity, Repository}

/**
 * Created by fred on 15/03/2014.
 */


trait DBTrait {

  def uri_base: String
  def uri_base_resource: String
  implicit lazy val repository = Repository(repo_uri)

  def repo_uri: String = uri_base + "/sparql"

  def uri(s: String) = uri_base_resource + "/resource/" + s

  def DBEntity(s: String) = Entity(uri(s))

  def wikiPage(url:String) = url.replace("http://dbpedia.org/resource/", "en.wikipedia.org/wiki/")
  def wikiPage(e:Entity) = e.uri.replace("http://dbpedia.org/resource/", "en.wikipedia.org/wiki/")


}

trait DBPedia extends DBTrait {

  val uri_base = "http://dbpedia.org"
  val uri_base_resource = "http://dbpedia.org"

}

trait DBPediaFr extends DBTrait {

  val uri_base = "http://fr.dbpedia.org"
  val uri_base_resource = "http://fr.dbpedia.org"

}

trait LiveDBPedia extends DBTrait {

  val uri_base = "http://live.dbpedia.org"
  val uri_base_resource = "http://dbpedia.org"


}

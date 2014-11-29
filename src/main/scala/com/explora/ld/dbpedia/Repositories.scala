package com.explora.ld.dbpedia

import com.explora.executer.Executer
import com.explora.model.{Entity, Repository}

import scala.concurrent.ExecutionContext

/**
 * Created by fred on 15/03/2014.
 */


trait DBTrait {


  def uri_base: String

  def uri_base_resource: String

  implicit def repository(implicit ex: Executer, ec: ExecutionContext) = Repository(repo_uri)(ex, ec)

  def repo_uri: String = uri_base + "/sparql"

  def uri(s: String) = uri_base_resource + "/resource/" + s

  def DBEntity(s: String)(implicit ex: Executer, ec: ExecutionContext) = {


    Entity(uri(s))(repository(ex, ec), ec)
  }

  def wikiPage(url: String) = url.replace("http://dbpedia.org/resource/", "en.wikipedia.org/wiki/")

  def wikiPage(e: Entity) = e.uri.replace("http://dbpedia.org/resource/", "en.wikipedia.org/wiki/")


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

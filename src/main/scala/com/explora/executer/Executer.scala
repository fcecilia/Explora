package com.explora.executer

import com.explora.model.Repository.SPARQLResults
import com.explora.model.{Entity, Literal, RDFNode, Repository}
import org.openrdf.model.impl.{LiteralImpl, URIImpl}
import org.openrdf.query.{QueryLanguage, TupleQueryResult}
import org.openrdf.repository.http.HTTPRepository

import scala.collection.immutable.Map
import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by fred on 29/11/2014.
 */
trait Executer {


  def execute(req: String, rep: Repository)(implicit ex: ExecutionContext): Future[SPARQLResults]

}



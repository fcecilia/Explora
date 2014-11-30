package com.explora.executer.empty

import com.explora.executer.Executer
import com.explora.model.Repository.SPARQLResults
import com.explora.model.{RDFNode, Repository}

import scala.collection.immutable.Map
import scala.concurrent.{ExecutionContext, Future}


/**
 * Created by fred on 29/11/2014.
 */
trait EmptyContext {

  implicit val executer: Executer = EmptyExecuter


}


object EmptyExecuter extends Executer {


  def execute(req: String, rep: Repository)(implicit ex: ExecutionContext): Future[SPARQLResults] = {


    /** *
      *
      * Why this code doesn't work with ExecutionContext.Implicits.global?
      * The application stop !
      *
      */
    Future(SPARQLResults(List[Map[String, RDFNode]]()))

  }

}

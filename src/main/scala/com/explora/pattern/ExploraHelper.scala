package com.explora.pattern

import com.explora.model.{Literal, RDFNode, Entity, Repository}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * Created by fred on 24/03/2014.
 */
object ExploraHelper {


  implicit class StringRequest(s: String) {

    def execute(implicit rep: Repository) = rep.execute(s)

    def executeAndGet (variable: String)(implicit rep: Repository) = {
      rep.execute(s).map(m =>
        m.get(variable)
      )
    }

  }


  implicit class String2Entity(s: String)(implicit rep: Repository) {


    def entity = Entity(s)

  }


  implicit class LiteralEntity(nodes: List[RDFNode])(implicit rep: Repository){

    def onlyEntities =

      nodes.map {
        case Literal(value, opt) => None
        case e:Entity => Some(e )
      }.filter(m => m != None).map(_.get)


    def onlyLiterals =

      nodes.map {
        case l:Literal => Some(l)
        case e:Entity => None
      }.filter(m => m != None).map(_.get)
    }


}

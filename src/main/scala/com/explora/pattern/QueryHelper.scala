package com.explora.pattern

import com.explora.model.{Entity, Repository}
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by fred on 24/03/2014.
 */
object QueryHelper {


  implicit class StringRequest(s: String) {

    def execute(implicit rep: Repository) = rep.execute(s)

    def executeAndGet (variable: String)(implicit rep: Repository) = {
      rep.execute(s).map(m =>m.get(variable))
    }

  }


  implicit class String2Entity(s: String)(implicit rep: Repository) extends Entity(s){


    def entity = Entity(s)

  }

}

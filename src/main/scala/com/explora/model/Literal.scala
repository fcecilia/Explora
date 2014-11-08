package com.explora.model

 case class Literal(value: String, lang: Option[String]) extends RDFNode {

  def this(value: String, lag_ : String) = this(value, Some(lag_))
  def this(value: String) = this(value, None)

}


object Literal {
	def apply(value: String, lag_ : String) =  new Literal(value, Some(lag_))
	def apply(value: String) = new Literal(value,None)
}
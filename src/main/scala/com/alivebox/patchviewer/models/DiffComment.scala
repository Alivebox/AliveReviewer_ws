package com.alivebox.patchviewer.models

import scala.collection.mutable.ArrayBuffer
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.Extraction

/**
 * Created with IntelliJ IDEA.
 * User: cleandro
* Date: 07/06/13
* Time: 03:06 PM
* To change this template use File | Settings | File Templates.
*/
case class DiffComment(var text:String = "",var author:String = "",var line:Integer = null,var id:Integer = null )

object DiffComment {

  private implicit val formats = net.liftweb.json.DefaultFormats

  implicit def toJson(argDiffComment: DiffComment): JValue =
    Extraction.decompose(argDiffComment);


}

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
case class DiffReviewer(var patch:Integer = null , var email:String = "",var id:Integer = null )

object DiffReviewer {

  private implicit val formats = net.liftweb.json.DefaultFormats

  implicit def toJson(argDiffReviewer: DiffReviewer): JValue =
    Extraction.decompose(argDiffReviewer);


}

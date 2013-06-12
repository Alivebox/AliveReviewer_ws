package com.alivebox.patchviewer.models

import scala.collection.mutable.ArrayBuffer
import net.liftweb.json.Extraction
import net.liftweb.json.JsonAST.JValue

/**
 * Created with IntelliJ IDEA.
 * User: cleandro
 * Date: 23/05/13
 * Time: 09:46 AM
 * To change this template use DiffFile | Settings | DiffFile Templates.
 */
case class Diff(var files:ArrayBuffer[DiffFile] = ArrayBuffer[DiffFile](), var id:Integer = null )

object Diff {

  private implicit val formats = net.liftweb.json.DefaultFormats

  implicit def toJson(argDiff: Diff): JValue =
    Extraction.decompose(argDiff);


}
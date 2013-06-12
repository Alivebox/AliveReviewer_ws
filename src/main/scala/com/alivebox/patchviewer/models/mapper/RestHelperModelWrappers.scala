package com.alivebox.patchviewer.models.mapper

import scala.collection.mutable.ArrayBuffer
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.Extraction

/**
 * This File Contains all the model wrapper used with the resthelper to provide json support out of the box , without doing the json parser manually
 *
 */


case class DiffFile(fileName:String,var hunks:ArrayBuffer[Hunk] = ArrayBuffer[Hunk]())
case class DiffReviewer(var patch:Integer = null , var email:String = "",var id:Integer = null )
case class Hunk(var hunkLines:ArrayBuffer[HunkLine] = ArrayBuffer[HunkLine]() )
case class HunkLine(var text:String = "",var fromLine:Integer = null,var toLine:Integer = null, var hunkType:String = "",var id:Integer = null)
case class Diff(var files:ArrayBuffer[DiffFile] = ArrayBuffer[DiffFile](), var id:Integer = null )

object Diff {

  private implicit val formats = net.liftweb.json.DefaultFormats

  implicit def toJson(argDiff: Diff): JValue =
    Extraction.decompose(argDiff)


}


case class DiffComment(var text:String = "",var author:String = "",var line:Integer = null,var id:Integer = null )

object DiffComment {

  private implicit val formats = net.liftweb.json.DefaultFormats

  implicit def toJson(argDiffComment: DiffComment): JValue =
    Extraction.decompose(argDiffComment)


}




object DiffReviewer {

  private implicit val formats = net.liftweb.json.DefaultFormats

  implicit def toJson(argDiffReviewer: DiffReviewer): JValue =
    Extraction.decompose(argDiffReviewer)
}




package com.alivebox.patchviewer.models


/**
 * Created with IntelliJ IDEA.
 * User: cleandro
 * Date: 23/05/13
 * Time: 10:27 AM
 * To change this template use DiffFile | Settings | DiffFile Templates.
 */
case class HunkLine(var text:String = "",var fromLine:Integer = null,var toLine:Integer = null, var hunkType:String = "",var id:Integer = null) {


}

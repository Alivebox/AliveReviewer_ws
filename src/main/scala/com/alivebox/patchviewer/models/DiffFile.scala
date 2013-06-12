package com.alivebox.patchviewer.models

import scala.collection.mutable.ArrayBuffer

/**
 * Created with IntelliJ IDEA.
 * User: cleandro
 * Date: 27/05/13
 * Time: 12:07 PM
 * To change this template use DiffFile | Settings | DiffFile Templates.
 */
case class DiffFile(fileName:String,var hunks:ArrayBuffer[Hunk] = ArrayBuffer[Hunk]()) {

}

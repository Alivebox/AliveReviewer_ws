package com.alivebox.patchviewer.models

/**
 * Created with IntelliJ IDEA.
 * User: cleandro
 * Date: 27/05/13
 * Time: 03:43 PM
 * To change this template use File | Settings | File Templates.
 */
class FromFilesToFilesLines {

    def this(fromFileLineStart:Int,fromFileLineEnd:Int,toFileLineStart:Int,toFileLineEnd:Int) { // An auxiliary constructor
      this() // Calls primary constructor
      this.fromFileLineStart = fromFileLineStart
      this.fromFileLineEnd = fromFileLineStart
      this.toFileLineStart = fromFileLineStart
      this.toFileLineEnd = fromFileLineStart
    }
    var fromFileLineStart = 0
    var fromFileLineEnd = 0
    var toFileLineStart = 0
    var toFileLineEnd = 0

}

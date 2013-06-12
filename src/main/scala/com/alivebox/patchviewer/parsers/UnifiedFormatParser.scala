package com.alivebox.patchviewer.parsers

import scala.collection.mutable.ArrayBuffer
import com.alivebox.patchviewer.models._
import com.alivebox.patchviewer.models.mapper.{HunkLine, DiffFile, Diff, Hunk}
import com.alivebox.patchviewer.models.util.FromFilesToFilesLines

/**
 * User: ljcp
 * Date: 23/05/13
 * Time: 09:47 AM
 * Parse for the UnifiedFormat defined in
 * http://www.gnu.org/software/diffutils/manual/html_node/Detailed-Unified.html#Detailed-Unified
 */
object UnifiedFormatParser{

  def parser(argFileLines:List[String]):Diff = {
      val tmpFileLines = fileLinesToFilesLines(argFileLines)
      val tmpDiff =  filesLinesToDiff(tmpFileLines)
      tmpDiff.files = tmpDiff.files.reverse
      tmpDiff
  }


  def filesLinesToDiff(argFilesLines:ArrayBuffer[ArrayBuffer[String]]): Diff = {
    val tmpDiff = new Diff

    for (tmpfileLines <- argFilesLines)
      tmpDiff.files += fileLinesToDiffFile(tmpfileLines)

    tmpDiff
  }

  def getIndexesData(argLine:String):FromFilesToFilesLines ={
    var tmpFromFilesToFilesLines = new FromFilesToFilesLines()
    if(!argLine.startsWith("@@")){
       return tmpFromFilesToFilesLines
    }

    var tmpData = argLine.split("\\W+")
    tmpFromFilesToFilesLines.fromFileLineStart = tmpData(1).toInt
    tmpFromFilesToFilesLines.fromFileLineEnd = tmpData(2).toInt
    tmpFromFilesToFilesLines.toFileLineStart = tmpData(3).toInt
    tmpFromFilesToFilesLines.toFileLineEnd = tmpData(4).toInt
    tmpFromFilesToFilesLines
  }

  def fileLinesToDiffFile(argFileLines:ArrayBuffer[String]):DiffFile = {
    var tmpFromFile = argFileLines(0)
    var tmpToFile = argFileLines(1)
    argFileLines -= tmpFromFile
    argFileLines -= tmpToFile
    var tmpDiffFile = new DiffFile(tmpToFile.split("\\s+")(1))
    tmpDiffFile = setHunks(tmpDiffFile,argFileLines)
    tmpDiffFile
  }


  def setHunks(argDiffFile:DiffFile,argFileLines:ArrayBuffer[String]):DiffFile = {
      var indxFrom,indxTo = 0
      var hunks = ArrayBuffer[Hunk]()
      var indexesData:FromFilesToFilesLines = null
      var tmpHunk:Hunk = null
      var tmpLine = ""
      var tmpHunkLine:HunkLine = null
      try {
      for (i <- 0 until argFileLines.length){
        tmpLine = argFileLines(i)
        if(tmpLine.startsWith("@@")){
          if(tmpHunk != null){
            hunks += tmpHunk
          }
          tmpHunk = new Hunk
          indexesData = getIndexesData(tmpLine)
          indxFrom = indexesData.fromFileLineStart
          indxTo = indexesData.toFileLineStart
        }else{
          tmpHunkLine = new HunkLine()
          val tmpTypeAndNewLine = getHunkTypeAndRemoveItFromLine(tmpLine)
          tmpHunkLine.text =  tmpTypeAndNewLine(1).replace("\t", "    ")
          tmpHunkLine.hunkType = tmpTypeAndNewLine(0)
          if(tmpHunkLine.hunkType == "added"){
            tmpHunkLine.fromLine = null
            tmpHunkLine.toLine = indxTo
            indxTo += 1
          }
          if(tmpHunkLine.hunkType == "removed"){
            tmpHunkLine.toLine = null
            tmpHunkLine.fromLine = indxFrom
            indxFrom += 1
          }

          if(tmpHunkLine.hunkType == "unchanged"){
            tmpHunkLine.toLine = indxTo
            tmpHunkLine.fromLine = indxFrom
            indxTo += 1
            indxFrom += 1
          }
          if(indxTo <= indexesData.toFileLineStart + indexesData.toFileLineEnd ){
            tmpHunk.hunkLines += tmpHunkLine
          }
        }

        if(i+1 == argFileLines.length){
          hunks += tmpHunk
        }

      }

      } catch {
        case ex: Exception =>{
          println("Setting Hunks Exception:" + ex.getStackTrace)
        }

      }

      argDiffFile.hunks = hunks
      argDiffFile
  }


  def getHunkTypeAndRemoveItFromLine(argLine:String):Array[String] = {
    val tmpChar = argLine.charAt(0)
    var tmpType = "unchanged"
    var tmpNewLine = argLine
    if(tmpChar == '+'){
      tmpType = "added"
      tmpNewLine = argLine.substring(1)
    }
    if(tmpChar == '-'){
      tmpType = "removed"
      tmpNewLine = argLine.substring(1)
    }
    Array(tmpType,tmpNewLine)
  }



  def fileLinesToFilesLines(argFileLines:List[String]):ArrayBuffer[ArrayBuffer[String]] = {
    val tmpIterator = argFileLines.iterator
    var tmpDiffFilesLines = new ArrayBuffer[ArrayBuffer[String]]()
    var foundIndx = 0
    var tmpDiffFileLine = new ArrayBuffer[String]()
    while(tmpIterator.hasNext){
      val tmpLine = tmpIterator.next()
      if(tmpLine.startsWith("---")){
        foundIndx = foundIndx + 1
        if(foundIndx > 1){
          foundIndx = 1
          tmpDiffFilesLines += tmpDiffFileLine
          tmpDiffFileLine = new ArrayBuffer[String]()
        }
      }

      if(foundIndx >= 1){
        tmpDiffFileLine += tmpLine
      }
    }
    tmpDiffFilesLines += tmpDiffFileLine
    tmpDiffFilesLines
  }


}
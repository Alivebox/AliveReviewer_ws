package com.alivebox.patchviewer.translators

import com.alivebox.patchviewer.models._
import com.alivebox.patchviewer.models.mapper._
import net.liftweb.common.Empty
import com.alivebox.patchviewer.models.HunkLine
import com.alivebox.patchviewer.models.Hunk
import com.alivebox.patchviewer.models.DiffFile
import net.liftweb.mapper.{By}
import net.liftweb.json.JsonAST.JValue

/**
 * Created with IntelliJ IDEA.
 * User: cleandro
 * Date: 07/06/13
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
object MapperTranslator {


  def diffToMapper(argDiff:Diff):DiffMapper = {
    var tmpDiffMapper = new DiffMapper
    tmpDiffMapper = tmpDiffMapper.saveMe()
    for (tmpFile <- argDiff.files){
      var tmpFileMapper = new DiffFileMapper
      tmpFileMapper.fileName(tmpFile.fileName)
      tmpFileMapper.diff(tmpDiffMapper)
      tmpFileMapper.save()

      for(tmpHunk <- tmpFile.hunks){
        var tmpHunkMapper = new HunkMapper
        tmpHunkMapper.diffFile(tmpFileMapper)
        tmpHunkMapper.save()

        for(tmpHunkLine <- tmpHunk.hunkLines){
          var tmpHunkLineMapper = new HunkLineMapper
          if(tmpHunkLine.fromLine == null){
            tmpHunkLineMapper.fromLine(-1)
          }else{
            tmpHunkLineMapper.fromLine(tmpHunkLine.fromLine)
          }
          tmpHunkLineMapper.hunk(tmpHunkMapper)
          tmpHunkLineMapper.hunkType(tmpHunkLine.hunkType)
          tmpHunkLineMapper.text(tmpHunkLine.text)
          if(tmpHunkLine.toLine == null){
            tmpHunkLineMapper.toLine(-1)
          }else{
            tmpHunkLineMapper.toLine(tmpHunkLine.toLine)
          }

          tmpHunkLineMapper = tmpHunkLineMapper.saveMe()
        }

      }
    }
    DiffMapper.findAll(By(DiffMapper.id,tmpDiffMapper.id))(0)
  }

  def MapperToDiff(argDiff:DiffMapper):Diff = {
    var tmpDiff = new Diff
    tmpDiff.id = argDiff.id.is.toInt
    for(tmpFile <- argDiff.files){
      var tmpNewFile = new DiffFile(tmpFile.fileName.is)
      for(tmpHunk <- tmpFile.hunks){
        var tmpNewHunk = new Hunk
        tmpNewFile.hunks += tmpNewHunk
        for(tmpHunkLine <- tmpHunk.hunkLines){
          var tmpNewLine = new HunkLine
          tmpNewLine.fromLine = if(tmpHunkLine.fromLine.is == -1) null else tmpHunkLine.fromLine.is
          tmpNewLine.toLine = if(tmpHunkLine.toLine.is == -1) null else tmpHunkLine.toLine.is
          tmpNewLine.hunkType = tmpHunkLine.hunkType
          tmpNewLine.id = tmpHunkLine.id.is.toInt
          tmpNewLine.text = tmpHunkLine.text
          tmpNewHunk.hunkLines += tmpNewLine
        }

      }
      tmpDiff.files += tmpNewFile
    }
     tmpDiff
  }

  def MapperToDiffComment(argDiffComment:DiffCommentMapper):DiffComment = {
    var tmpDiffComment = new DiffComment
    tmpDiffComment.author = argDiffComment.author.is
    tmpDiffComment.id = argDiffComment.id.is.toInt
    tmpDiffComment.line = argDiffComment.line.is.toInt
    tmpDiffComment.text = argDiffComment.text.is
    tmpDiffComment
  }

  def MapperToDiffReviewer(argDiffReviewer:DiffReviewerMapper):DiffReviewer = {
    var tmpDiffReviewer = new DiffReviewer
    tmpDiffReviewer.email = argDiffReviewer.email.is
    tmpDiffReviewer.id = argDiffReviewer.id.is.toInt
    tmpDiffReviewer.patch = argDiffReviewer.diff.is.toInt
    tmpDiffReviewer
  }

  /*def DiffCommentToMapper(argDiffComment:DiffComment):DiffCommentMapper = {
    var tmpDiffComment = new DiffCommentMapper
    tmpDiffComment.author()

  }            */
}

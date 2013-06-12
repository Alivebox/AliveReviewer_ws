package com.alivebox.patchviewer.restservices

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.{JsonResponse, FileParamHolder, LiftRules}
import scala.io.Source
import com.alivebox.patchviewer.parsers.UnifiedFormatParser
import com.alivebox.patchviewer.models.{DiffReviewer, DiffComment, Diff}
import net.liftweb.json.JsonAST.{JArray}
import com.alivebox.patchviewer.translators.MapperTranslator
import net.liftweb.util.BasicTypesHelpers.{AsInt, AsLong}
import com.alivebox.patchviewer.models.mapper._
import net.liftweb.mapper._
import net.liftweb.common.{Logger}
import net.liftweb.json.JsonParser
import net.liftweb.http

/**
 * User: ljcp
 * Date: 5/31/13
 * Time: 3:59 PM
 * Main Rest Service for handling the patch operations
 */
object  PatchService extends RestHelper with Logger{

  def init() : Unit = {
    LiftRules.statelessDispatch.append(PatchService)
  }

  def processPatch(argUploadedFiles:List[FileParamHolder]): Diff = {
    var tmpDiff:Diff = new Diff
    for (uploadedFile <- argUploadedFiles){
      tmpDiff = UnifiedFormatParser.parser(Source.fromInputStream(uploadedFile.fileStream).getLines().toList)
    }
    MapperTranslator.MapperToDiff(MapperTranslator.diffToMapper(tmpDiff))
  }

  serve {

    case Post("patchfile" :: _, req) => {
      JsonResponse(processPatch(req.uploadedFiles))
    }

    case Get("patchfile" :: AsLong(id) :: _ ,req ) =>  {
      var tmpResponse = DiffMapper.findAll(By(DiffMapper.id,id))
      JsonResponse(MapperTranslator.MapperToDiff(tmpResponse(0)))
    }

    case "comments" :: Nil JsonPost json -> _ => {
      var tmpDiffComment = json.extract[DiffComment]
      var tmpComment = new DiffCommentMapper
      tmpComment.author(tmpDiffComment.author)
      var tmpLine = HunkLineMapper.findAll(By(HunkLineMapper.id,tmpDiffComment.line.toLong))(0)
      tmpComment.line(tmpLine)
      tmpComment.text(tmpDiffComment.text)
      tmpComment = tmpComment.saveMe()
      var tmpCommentSaved = MapperTranslator.MapperToDiffComment(tmpComment)
      JsonResponse(tmpCommentSaved)
    }


    case JsonDelete("comments" :: AsInt(id) :: _ , req) => {
      try{
        var tmpComment =  DiffCommentMapper.findAll(By(DiffCommentMapper.id,id))(0)
        var deleted:Boolean = tmpComment.delete_!
        var jsonString = """{"result":""" + deleted.toString  + """}"""
        JsonResponse(JsonParser.parse(jsonString))

      } catch {
        case e: Exception => { var jsonString = """{"result":""" + false.toString  + """}"""; http.JsonResponse(JsonParser.parse(jsonString)) }
      }
    }

    case "comments" :: Nil JsonPut json -> _ => {
      var tmpDiffComment = json.extract[DiffComment]
      var tmpComment =  DiffCommentMapper.findAll(By(DiffCommentMapper.id,tmpDiffComment.id.intValue()))(0)
      tmpComment.text(tmpDiffComment.text)
      tmpComment = tmpComment.saveMe()
      var tmpCommentSaved = MapperTranslator.MapperToDiffComment(tmpComment)
      JsonResponse(tmpCommentSaved)
    }


    case Get("comments" :: AsLong(id) :: _ , req) =>{
      var ahh = DiffCommentMapper.findAllByPreparedStatement({superconn => {
        val preparedStatement = superconn.connection.prepareStatement("select diffcomment.id,diffcomment.text,diffcomment.author,diffcomment.line from diffcomment\njoin hunkline hl on diffcomment.line = hl.id\njoin hunk h on hl.hunk = h.id\njoin diffile df on h.difffile = df.id\njoin diff d on df.diff = d.id\nwhere d.id = ?")
        preparedStatement.setInt(1,id.toInt)
        preparedStatement
      }})
      var mysuperlist:List[DiffComment] = ahh map {MapperTranslator.MapperToDiffComment(_) }
      JArray(for{item <- mysuperlist} yield DiffComment.toJson(item))
    }

    case "reviewers" :: Nil JsonPost json -> _ => {
      var tmpDiffReviewer = json.extract[DiffReviewer]
      var tmpReviewer = new DiffReviewerMapper
      tmpReviewer.email(tmpDiffReviewer.email)
      var tmpDiff = DiffMapper.findAll(By(DiffMapper.id,tmpDiffReviewer.patch.toLong))(0)
      tmpReviewer.diff(tmpDiff)
      tmpReviewer = tmpReviewer.saveMe()
      var tmpReviewerSaved = MapperTranslator.MapperToDiffReviewer(tmpReviewer)
      JsonResponse(tmpReviewerSaved)
    }

    case JsonDelete("reviewers" :: AsInt(id) :: _ , req) => {
      try{
        var tmpReviewer =  DiffReviewerMapper.findAll(By(DiffReviewerMapper.id,id))(0)
        var deleted:Boolean = tmpReviewer.delete_!
        var jsonString = """{"result":""" + deleted.toString  + """}"""
        JsonResponse(JsonParser.parse(jsonString))

      } catch {
        case e: Exception => { var jsonString = """{"result":""" + false.toString  + """}"""; http.JsonResponse(JsonParser.parse(jsonString)) }
      }
    }

    case Get("reviewers" :: AsLong(id) :: _ , req) => {
      var ahh =  DiffReviewerMapper.findAll(By(DiffReviewerMapper.diff,id))
      var mysuperlist:List[DiffReviewer] = ahh map {MapperTranslator.MapperToDiffReviewer(_) }
      JArray(for{item <- mysuperlist} yield DiffReviewer.toJson(item))
  }


  }

}

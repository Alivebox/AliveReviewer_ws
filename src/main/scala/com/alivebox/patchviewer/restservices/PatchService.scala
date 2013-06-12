package com.alivebox.patchviewer.restservices

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.{JsonResponse, FileParamHolder, LiftRules}
import scala.io.Source
import com.alivebox.patchviewer.parsers.UnifiedFormatParser
import net.liftweb.json.JsonAST.JArray
import com.alivebox.patchviewer.translators.MapperTranslator
import net.liftweb.util.BasicTypesHelpers.{AsInt, AsLong}
import com.alivebox.patchviewer.models.mapper._
import net.liftweb.mapper._
import net.liftweb.common.Logger
import net.liftweb.json.JsonParser
import net.liftweb.http

/**
 * User: ljcp
 * Date: 5/31/13
 * Time: 3:59 PM
 * Main Rest Service for handling the patch operations
 */
object  PatchService extends RestHelper with Logger{

  def init(){
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
      val tmpDiff = DiffMapper.find(By(DiffMapper.id,id))
      JsonResponse(MapperTranslator.MapperToDiff(tmpDiff openOrThrowException "Patch File not found" ))
    }

    case "comments" :: Nil JsonPost json -> _ => {
      val tmpDiffComment = json.extract[DiffComment]
      var tmpDiffCommentMapper = new DiffCommentMapper
      tmpDiffCommentMapper.author(tmpDiffComment.author)
      var tmpHunkLineMapper = HunkLineMapper.find(By(HunkLineMapper.id,tmpDiffComment.line.toLong)) openOrThrowException "Line Not Found"
      tmpDiffCommentMapper.line(tmpHunkLineMapper)
      tmpDiffCommentMapper.text(tmpDiffComment.text)
      tmpDiffCommentMapper = tmpDiffCommentMapper.saveMe()
      var tmpCommentSaved = MapperTranslator.MapperToDiffComment(tmpDiffCommentMapper)
      JsonResponse(tmpCommentSaved)
    }


    case JsonDelete("comments" :: AsInt(id) :: _ , req) => {
      try{
        val tmpDiffCommentMapper =  DiffCommentMapper.find(By(DiffCommentMapper.id,id)) openOrThrowException  "Comment not found"
        tmpDiffCommentMapper.delete_!
        var jsonString = """{"result":""" + true.toString + """}"""
        JsonResponse(JsonParser.parse(jsonString))

      } catch {
        case e: Exception => { var jsonString = """{"result":""" + false.toString  + """}"""; http.JsonResponse(JsonParser.parse(jsonString)) }
      }
    }

    case "comments" :: Nil JsonPut json -> _ => {
      var tmpDiffComment = json.extract[DiffComment]
      var tmpDiffCommentMapper =  DiffCommentMapper.find(By(DiffCommentMapper.id,tmpDiffComment.id.intValue())) openOrThrowException "Comment not found"
      tmpDiffCommentMapper.text(tmpDiffComment.text)
      tmpDiffCommentMapper = tmpDiffCommentMapper.saveMe()
      var tmpCommentSaved = MapperTranslator.MapperToDiffComment(tmpDiffCommentMapper)
      JsonResponse(tmpCommentSaved)
    }


    case Get("comments" :: AsLong(id) :: _ , req) =>{
      val tmpMapperComments = DiffCommentMapper.findAllByPreparedStatement({superconn => {
        val preparedStatement = superconn.connection.prepareStatement("select diffcomment.id,diffcomment.text,diffcomment.author,diffcomment.line from diffcomment\njoin hunkline hl on diffcomment.line = hl.id\njoin hunk h on hl.hunk = h.id\njoin diffile df on h.difffile = df.id\njoin diff d on df.diff = d.id\nwhere d.id = ?")
        preparedStatement.setInt(1,id.toInt)
        preparedStatement
      }})
      val tmpComments:List[DiffComment] = tmpMapperComments map {MapperTranslator.MapperToDiffComment(_) }
      JArray(for{item <- tmpComments} yield DiffComment.toJson(item))
    }

    case "reviewers" :: Nil JsonPost json -> _ => {
      val tmpDiffReviewer = json.extract[DiffReviewer]
      var tmpDiffReviewerMapper = new DiffReviewerMapper
      tmpDiffReviewerMapper.email(tmpDiffReviewer.email)
      val tmpDiff = DiffMapper.find(By(DiffMapper.id,tmpDiffReviewer.patch.toLong)) openOrThrowException  "Patch Id not Found"
      tmpDiffReviewerMapper.diff(tmpDiff)
      tmpDiffReviewerMapper = tmpDiffReviewerMapper.saveMe()
      val tmpReviewerSaved = MapperTranslator.MapperToDiffReviewer(tmpDiffReviewerMapper)
      JsonResponse(tmpReviewerSaved)
    }

    case JsonDelete("reviewers" :: AsInt(id) :: _ , req) => {
      try{
        var tmpReviewer =  DiffReviewerMapper.find(By(DiffReviewerMapper.id,id)) openOrThrowException "Reviewer not found"
        var deleted:Boolean = tmpReviewer.delete_!
        var jsonString = """{"result":""" + deleted.toString  + """}"""
        JsonResponse(JsonParser.parse(jsonString))

      } catch {
        case e: Exception => { var jsonString = """{"result":""" + false.toString  + """}"""; http.JsonResponse(JsonParser.parse(jsonString)) }
      }
    }

    case Get("reviewers" :: AsLong(id) :: _ , req) => {
      var tmpReviewersMapper =  DiffReviewerMapper.findAll(By(DiffReviewerMapper.diff,id))
      var tmpReviewers:List[DiffReviewer] = tmpReviewersMapper map {MapperTranslator.MapperToDiffReviewer(_) }
      JArray(for{item <- tmpReviewers} yield DiffReviewer.toJson(item))
  }


  }

}

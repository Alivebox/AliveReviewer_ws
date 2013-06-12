package com.alivebox.patchviewer.models.mapper

import net.liftweb.mapper._

/**
 * Created with IntelliJ IDEA.
 * User: cleandro
 * Date: 07/06/13
 * Time: 03:01 PM
 * To change this template use File | Settings | File Templates.
 */
class DiffCommentMapper extends LongKeyedMapper[DiffCommentMapper] with IdPK {
  def getSingleton = DiffCommentMapper
  object text extends MappedString(this,500)
  object author extends MappedString(this,50)
  object line extends MappedLongForeignKey(this,HunkLineMapper)
  //object diff extends MappedLongForeignKey(this,DiffMapper)
}

object DiffCommentMapper extends DiffCommentMapper with KeyedMetaMapper[Long,DiffCommentMapper] {
  override def dbTableName = "diffcomment"
}
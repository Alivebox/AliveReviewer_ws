package com.alivebox.patchviewer.models.mapper;

import net.liftweb.mapper._
;

/**
 * Created with IntelliJ IDEA.
 * User: cleandro
 * Date: 12/06/13
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
*/
class DiffReviewerMapper extends LongKeyedMapper[DiffReviewerMapper] with IdPK {
  def getSingleton = DiffReviewerMapper
  object email extends MappedString(this,100)
  object diff extends MappedLongForeignKey(this,DiffMapper)
}

object DiffReviewerMapper extends DiffReviewerMapper with KeyedMetaMapper[Long,DiffReviewerMapper] {
  override def dbTableName = "diffreviewer"
}

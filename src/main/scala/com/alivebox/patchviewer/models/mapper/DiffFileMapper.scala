package com.alivebox.patchviewer.models.mapper

import net.liftweb.mapper._

/**
 * Created with IntelliJ IDEA.
 * User: ljcp
 * Date: 6/6/13
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
class DiffFileMapper extends LongKeyedMapper[DiffFileMapper] with IdPK {

  def getSingleton = DiffFileMapper
  object fileName extends MappedString(this,250)
  def hunks = HunkMapper.findAll(By(HunkMapper.diffFile,this.id))
  object diff extends MappedLongForeignKey(this,DiffMapper)
}

object DiffFileMapper extends DiffFileMapper with KeyedMetaMapper[Long,DiffFileMapper] {

  override def dbTableName = "diffile"

}
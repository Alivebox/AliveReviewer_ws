package com.alivebox.patchviewer.models.mapper

import net.liftweb.mapper.{KeyedMetaMapper, IdPK, LongKeyedMapper,MappedLongForeignKey,By}

/**
 * Created with IntelliJ IDEA.
 * User: ljcp
 * Date: 6/6/13
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
class HunkMapper extends LongKeyedMapper[HunkMapper] with IdPK {

  def getSingleton = HunkMapper
  def hunkLines = HunkLineMapper.findAll(By(HunkLineMapper.hunk,this.id))
  object diffFile extends MappedLongForeignKey(this,DiffFileMapper)
}

object HunkMapper extends HunkMapper with KeyedMetaMapper[Long,HunkMapper] {
  override def dbTableName = "hunk"

}

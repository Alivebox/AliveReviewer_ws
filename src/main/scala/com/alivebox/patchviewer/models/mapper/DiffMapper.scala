package com.alivebox.patchviewer.models.mapper

import net.liftweb.mapper.{KeyedMetaMapper, IdPK, LongKeyedMapper,By}
/**
 * Created with IntelliJ IDEA.
 * User: ljcp
 * Date: 6/6/13
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */



class DiffMapper extends LongKeyedMapper[DiffMapper] with IdPK {

  def getSingleton = DiffMapper
  def files = DiffFileMapper.findAll(By(DiffFileMapper.diff,this.id))
}

object DiffMapper extends DiffMapper with KeyedMetaMapper[Long,DiffMapper] {

  override def dbTableName = "diff"

}



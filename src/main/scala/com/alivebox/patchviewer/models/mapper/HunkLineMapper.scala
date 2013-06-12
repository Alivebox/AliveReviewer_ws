package com.alivebox.patchviewer.models.mapper
import net.liftweb.mapper._

/**
 * Created with IntelliJ IDEA.
 * User: ljcp
 * Date: 6/6/13
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
class HunkLineMapper extends LongKeyedMapper[HunkLineMapper] with IdPK {

  def getSingleton = HunkLineMapper
  //var text:String = "",var fromLine:Integer = null,var toLine:Integer = null, var hunkType:String = ""
  object text extends MappedString(this,250)
  object fromLine extends MappedInt(this)
  object toLine extends MappedInt(this)
  object hunkType extends MappedString(this,10)
  object hunk extends MappedLongForeignKey(this,HunkMapper)
}

object HunkLineMapper extends HunkLineMapper with KeyedMetaMapper[Long,HunkLineMapper] {

  override def dbTableName = "hunkline"

}
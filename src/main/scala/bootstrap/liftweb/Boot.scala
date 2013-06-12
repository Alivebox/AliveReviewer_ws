package bootstrap.liftweb

import com.alivebox.patchviewer.restservices.PatchService
import net.liftweb.mapper.{Schemifier, DB}
import net.liftweb.db.DefaultConnectionIdentifier
import com.alivebox.patchviewer.config.DBVendor
import com.alivebox.patchviewer.models.mapper._


/**
 * User: ljcp
 * Date: 5/31/13
 * Time: 3:30 PM
 * This is the main start point of the Application defined by the default listener in the web.xml
 */
class Boot {
  def boot {

    DB.defineConnectionManager(DefaultConnectionIdentifier,DBVendor)
    PatchService.init()
    //Creates the database structure if is not present
    Schemifier.schemify(true,Schemifier.infoF _,HunkLineMapper,HunkMapper,DiffMapper,DiffFileMapper,DiffCommentMapper,DiffReviewerMapper)
  }
}

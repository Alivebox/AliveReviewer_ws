package bootstrap.liftweb

/**
 * Created with IntelliJ IDEA.
 * User: ljcp
 * Date: 5/31/13
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb._
import com.alivebox.patchviewer.restservices.PatchService
import net.liftweb.mapper.{Schemifier, DB}
import net.liftweb.db.DefaultConnectionIdentifier
import com.alivebox.patchviewer.config.DBVendor
import net.liftweb.common.Logger
import com.alivebox.patchviewer.models.mapper._

class Boot {
  def boot {
    // where to search snippet
    //LiftRules.addToPackages("com.alivebox.patchviewer")
    val logger = Logger(classOf[Boot])
    DB.defineConnectionManager(DefaultConnectionIdentifier,DBVendor)
    PatchService.init()
    Schemifier.schemify(true,Schemifier.infoF _,HunkLineMapper,HunkMapper,DiffMapper,DiffFileMapper,DiffCommentMapper,DiffReviewerMapper)

    // Build SiteMap
    /*def sitemap(): SiteMap = SiteMap(
      Menu.i("Home") / "index"
    ) */

    // Use HTML5 for rendering
    /*LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))*/
  }
}

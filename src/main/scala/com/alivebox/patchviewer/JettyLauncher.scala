package com.alivebox.patchviewer

/**
 * Created with IntelliJ IDEA.
 * User: cleandro
 * Date: 22/05/13
 * Time: 12:05 PM
 * To change this template use DiffFile | Settings | DiffFile Templates.
 */
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler}
import org.eclipse.jetty.webapp.WebAppContext

object JettyLauncher { // this is my entry object as specified in sbt project definition
def main(args: Array[String]) {
  val port = if(System.getenv("PORT") != null) System.getenv("PORT").toInt else 8080

  val server = new Server(port)
  val context = new WebAppContext()
  context setContextPath "/"
  context.setResourceBase("src/main/webapp")
  var holder = context.addServlet(classOf[DefaultServlet], "/")
  /*holder.getRegistration.setMultipartConfig(
    MultipartConfig(
      maxFileSize = Some(100*1024*1024),
      fileSizeThreshold = Some(100*1024*1024)
    ).toMultipartConfigElement
  ) */
  server.setHandler(context)

  server.start
  server.join
}
}
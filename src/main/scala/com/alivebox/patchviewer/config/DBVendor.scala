package com.alivebox.patchviewer.config

import net.liftweb.db.{ConnectionIdentifier, ConnectionManager}
import net.liftweb.common.{Empty, Full, Box}
import java.sql.{DriverManager, Connection}
import net.liftweb.util.Props

/**
 * User: ljcp
 * Date: 6/4/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
object DBVendor extends ConnectionManager {
    // Force load the driver
    Class.forName("com.mysql.jdbc.Driver")

    def newConnection(name: ConnectionIdentifier) = {
      try {
          val dbUrl = Props.get("db.url") openOrThrowException "Database URL Not defined in property file"
          Full(DriverManager.getConnection(dbUrl))
          } catch {
          case e: Exception => e.printStackTrace; Empty
          }
    }

    def releaseConnection(c: Connection) {c.close}

}


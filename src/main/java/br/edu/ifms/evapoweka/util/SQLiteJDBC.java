/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifms.evapoweka.util;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 *
 * @author alisson
 * @link https://www.tutorialspoint.com/sqlite/sqlite_java.htm
 */
public class SQLiteJDBC {
    
    private static Connection con;
    
    public static Connection getConnection() {
        if (con == null) {
            init();
        }
        
        return con;
    }

    public static void init() {
        con = null;

        try {
            Class.forName("org.sqlite.JDBC");
            
            con = DriverManager.getConnection(String.format("jdbc:sqlite:%s/data.db", Config.PATH_DATA));
            con.setAutoCommit(false);
            
            System.out.println("Opened database successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
    }
}

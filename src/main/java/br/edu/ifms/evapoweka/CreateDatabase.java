/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.util.Config;
import br.edu.ifms.evapoweka.util.SQLiteJDBC;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author alisson
 */
public class CreateDatabase {

    public static void main(String[] args) {

        CreateDatabase c = new CreateDatabase();

    }

    public CreateDatabase() {

        try {
            this.createFilesTable();
            this.createFilesStationTable();
            this.createOutputTable();

            this.populateFilesTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void createFilesTable() throws SQLException {

        Connection con = SQLiteJDBC.getConnection();

        String sql;
        Statement stmt;

        sql = "DROP TABLE IF EXISTS file";
        stmt = con.createStatement();
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE file (\n"
                + "    idFile   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"
                + "    name STRING  NOT NULL\n"
                + ");";
        stmt = con.createStatement();
        stmt.executeUpdate(sql);

        con.commit();

    }

    private void populateFilesTable() throws SQLException {
        File folder = new File(Config.PATH_DATA);
        File[] listOfFiles = folder.listFiles();

        Connection con = SQLiteJDBC.getConnection();

        String selectSQL = "INSERT INTO file (name) VALUES (?)";

        PreparedStatement preparedStatement;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (!listOfFiles[i].isFile()) {
                continue;
            }

            String ext = FilenameUtils.getExtension(listOfFiles[i].getName());
            if (!ext.toUpperCase().equals("CSV")) {
                continue;
            }

            System.out.println("File " + listOfFiles[i].getName());

            preparedStatement = con.prepareStatement(selectSQL);
            preparedStatement.setString(1, FilenameUtils.removeExtension(listOfFiles[i].getName()));
            preparedStatement.executeUpdate();

        }

        con.commit();
    }

    private void createFilesStationTable() throws SQLException {
        Connection con = SQLiteJDBC.getConnection();

        String sql;
        Statement stmt;

        sql = "DROP TABLE IF EXISTS fileStation";
        stmt = con.createStatement();
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE fileStation (\n"
                + "    idFileStation   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"
                + "    idFile INTEGER REFERENCES file (idFile) NOT NULL,\n"
                + "    name STRING  NOT NULL\n"
                + ");";
        stmt = con.createStatement();
        stmt.executeUpdate(sql);

        con.commit();
    }

    private void createOutputTable() throws SQLException {
        Connection con = SQLiteJDBC.getConnection();

        String sql;
        Statement stmt;

        sql = "DROP TABLE IF EXISTS outputData";
        stmt = con.createStatement();
        stmt.executeUpdate(sql);
        
        sql = "CREATE TABLE outputData (\n"
                + "    idFile    INTEGER REFERENCES file (idFile),\n"
                + "    l1        INTEGER,\n"
                + "    l2        INTEGER,\n"
                + "    l3        INTEGER,\n"
                + "    verao     DOUBLE NOT NULL,\n"
                + "    outono    DOUBLE NOT NULL,\n"
                + "    inverno   DOUBLE NOT NULL,\n"
                + "    primavera DOUBLE NOT NULL,\n"
                + "    PRIMARY KEY (idFile, l1, l2, l3)\n"
                + ")";
        System.out.println(sql);
        stmt = con.createStatement();
        stmt.executeUpdate(sql);

        con.commit();
    }
}

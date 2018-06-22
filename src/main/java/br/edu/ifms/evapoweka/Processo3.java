package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.util.CsvSeparatorSeasonsByMonth;
import br.edu.ifms.evapoweka.util.Config;
import br.edu.ifms.evapoweka.util.SQLiteJDBC;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Processo3 {

    // private String PATH;
    // private String CSV;

    // private File csvFile;

    public Processo3() {
        // this.PATH = Config.PATH_DATA;

        Connection con = SQLiteJDBC.getConnection();

        String selectSQL = "SELECT idFile, name FROM file";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int idFile = rs.getInt("idFile");
                String name = rs.getString("name");
                String fileName = name + ".csv";
                
                String filePath = String.format("%s/%s", Config.PATH_DATA, fileName);
                System.out.println(filePath);
                subProcesso3(idFile, new File(filePath));
            }
            
            rs.close();
            preparedStatement.close();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void subProcesso3(int idFile, File csvFile) {

        // Separar o arquivo em estações (CsvSeparatorSeasons)
        CsvSeparatorSeasonsByMonth a = new CsvSeparatorSeasonsByMonth(csvFile);

        // Remover serie DATA para os arquivos gerados
        File[] csvStationFiles = {
            a.getCsvFileVerao(),
            a.getCsvFileOutono(),
            a.getCsvFileInverno(),
            a.getCsvFilePrimavera()
        };

        File[] arffFiles = new File[csvStationFiles.length];

        int i = 0;
        for (File csvStationFile : csvStationFiles) {
            CsvRemoveDataAttr b = new CsvRemoveDataAttr(csvStationFile);

            // Gerar arquivos ARFF (Csv2Arff)
            Csv2Arff c = new Csv2Arff(b.getNewFile());
            arffFiles[i] = c.getNewFile();

            i++;
        }

        // Calcular a melhor distribuição de neorônios (HiddenLayersVariation)
        HiddenLayersVariation d = new HiddenLayersVariation(idFile, arffFiles);

    }

    public static void main(String[] args) {

        Processo3 p = new Processo3();

    }

}

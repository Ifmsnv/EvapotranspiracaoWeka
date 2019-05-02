package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.instances.FullInstances;
import br.edu.ifms.evapoweka.util.Config;
import br.edu.ifms.evapoweka.util.MLPRun;
import br.edu.ifms.evapoweka.util.SQLiteJDBC;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

/*
 * Esta classe simula qual a melhor disposicao de camadas ocultas para o
 * algoritmo encontrar o melhor resultado.
 *
 * weka.classifiers.functions.MultilayerPerceptron -L 0.3 -M 0.2 -N 250 -V 0 -S
 * 1 -E 1 -H "3, 4, 5" -R
 *
 * @link https://findusages.com/repository/RPuSQfKEcWSdzPHvQtSWozab?file=src/gatech/cs7641/dgonzalez42/assignment3/NeuralNetworkRun.java&lines=90,144,102-102
 * @link https://www.programcreek.com/java-api-examples/index.php?api=weka.classifiers.functions.MultilayerPerceptron
 * @link http://www.cs.ubc.ca/labs/beta/Projects/autoweka/
 *
 * @author Alisson G. Chiquitto <alisson.chiquitto@ifms.edu.br>
 */
public class HiddenLayersVariation {

    private int idFile;
    private File[] arffFiles;

    public static void main(String[] args) {

        // new HiddenLayersVariation();
    }

    public HiddenLayersVariation(int idFile, File[] arffFiles) {

        this.idFile = idFile;
        this.arffFiles = arffFiles;

        run();

    }

    public HiddenLayersVariation(File output, File[] arffFiles) {

        this.arffFiles = arffFiles;

        run();

    }

    private void run() {

        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmm");
        try {
            test3HiddenLayersVariation();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void test3HiddenLayersVariation() throws SQLException {

        // String[] options = {"-L", "0.3", "-M", "0.2", "-N", "250", "-V", "0",
        //    "-S", "1", "-E", "1", "-H", "", "-R"};
        // try {
        //     MLPTest t = new MLPTest();
        // } catch (Exception ex) {
        //     ex.printStackTrace();
        //     return;
        // }
        // List<String> lines = new ArrayList<>();
        //Path file = Paths.get(this.output.getAbsolutePath());
        int l1, l2;
        int l1Start, l2Start;
        int min = 5;
        int max = 20;
        MLPRun mlp;

        String selectSQL = "Select\n"
                + "    MAX(l1) l1,\n"
                + "    MAX(l2) l2\n"
                + "From outputData\n"
                + "Where idFile = ?";

        // Continuar do ponto que parou
        Connection con = SQLiteJDBC.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
        preparedStatement.setInt(1, this.idFile);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            l1Start = Math.max(rs.getInt("l1"), min);
            l2Start = Math.max(rs.getInt("l2") + 1, min);
        } else {
            l1Start = min;
            l2Start = min;
        }
        rs.close();
        preparedStatement.close();

        if ((l1Start >= max) && (l2Start >= max)) {
            return;
        }

        List<Instances> instancesList = new ArrayList<>();

        for (File arffFile : this.arffFiles) {
            try {
                instancesList.add(FullInstances.factory(arffFile));
            } catch (Exception ex) {
                ex.printStackTrace();

                return;
            }
        }

        List<String> line;
        String tmp;

        String sql = "INSERT INTO outputData (\n"
                + "    idFile, l1, l2,\n"
                + "    verao, outono, inverno, primavera\n"
                + ")\n"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        preparedStatement = con.prepareStatement(sql);

        preparedStatement.setInt(1, this.idFile);

        for (l1 = l1Start; l1 <= max; l1++) {

            preparedStatement.setInt(2, l1);

            for (l2 = l2Start; l2 <= max; l2++) {

                preparedStatement.setInt(3, l2);

                line = new ArrayList<>();
                line.add(Integer.toString(l1));
                line.add(Integer.toString(l2));
                line.add(Integer.toString(l1 + l2));

                System.out.println(line);

                mlp = new MLPRun();
                mlp.mlp.setHiddenLayers(l1 + "," + l2);
                // mlp.printConfig();

                int col = 4;
                for (Instances instancesItem : instancesList) {

                    try {

                        Evaluation eval = mlp.evaluate(instancesItem, instancesItem);

                        Double correlationCoefficient = eval.correlationCoefficient() * 100;
                        //tmp = String.valueOf(correlationCoefficient);
                        // System.out.println(tmp);

                        line.add(String.valueOf(correlationCoefficient));
                        preparedStatement.setDouble(col++, correlationCoefficient);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                // tmp = String.join(",", line);
                // System.out.println(tmp);
                preparedStatement.executeUpdate();

                // writer.println(tmp);
                // writer.flush();
                con.commit();
            }

            l2Start = min;
        }

    }

}

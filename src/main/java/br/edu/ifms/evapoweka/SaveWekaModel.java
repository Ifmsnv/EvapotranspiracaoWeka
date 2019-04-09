package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.instances.FullInstances;
import br.edu.ifms.evapoweka.util.MLPRun;
import br.edu.ifms.evapoweka.util.SQLiteJDBC;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.output.prediction.AbstractOutput;
import weka.classifiers.evaluation.output.prediction.CSV;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 *
 * @author alisson
 * https://www.programcreek.com/java-api-examples/?api=weka.core.SerializationHelper
 */
public class SaveWekaModel {

    SaveWekaModel(int idFile, String estacao, File modelFile, File arffFile) throws SQLException, FileNotFoundException, Exception {
        String selectSQL = "Select l1, l2, l3 From outputData "
                + "Where (idFile = ?) Order By %s Desc Limit 1";
        selectSQL = String.format(selectSQL, estacao);

        // Continuar do ponto que parou
        Connection con = SQLiteJDBC.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
        preparedStatement.setInt(1, idFile);
        ResultSet rs = preparedStatement.executeQuery();

        if (!rs.next()) {
            return;
        }

        int l1 = rs.getInt("l1");
        int l2 = rs.getInt("l2");
        int l3 = rs.getInt("l3");

        MLPRun mlp = new MLPRun();
        mlp.mlp.setHiddenLayers(l1 + "," + l2 + "," + l3);

        Instances instances = FullInstances.factory(arffFile);

        AbstractOutput classificationOutput = generateClassificationOutput(instances);

        Evaluation eval = mlp.evaluate(instances, instances, classificationOutput);
        // Double correlationCoefficient = eval.correlationCoefficient() * 100;
        
        String summaryString = eval.toSummaryString("\nResults\n======\n", true);

        // BufferedWriter bwr = new BufferedWriter(new FileWriter(modelFile + ".predictions.csv"));
        // bwr.write(sbf.toString());
        // bwr.flush();
        // bwr.close();
        
        writeToFile(modelFile + ".predictions.csv", classificationOutput.getBuffer().toString());
        writeToFile(modelFile + ".summary.txt", summaryString);

        SerializationHelper.write(
                new FileOutputStream(modelFile),
                mlp.mlp);

    }
    
    private void writeToFile(String pFilename, String text) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(pFilename));
        out.write(text);
        out.flush();
        out.close();
    }

    private AbstractOutput generateClassificationOutput(Instances instances) {
        // StringBuffer sbf = new StringBuffer();
        // PlainText classificationOutput = new PlainText();
        CSV classificationOutput = new CSV();
        classificationOutput.setBuffer(new StringBuffer());
        classificationOutput.setHeader(instances);
        classificationOutput.printHeader();
        // classificationOutput.printClassifications(mlp, train);
        
        return classificationOutput;
    }

}

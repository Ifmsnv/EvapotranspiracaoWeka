package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.instances.FullInstances;
import br.edu.ifms.evapoweka.util.Config;
import br.edu.ifms.evapoweka.util.MLPRun;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

/**
 * Esta classe simula qual a melhor disposicao de camadas ocultas
 * para o algoritmo encontrar o melhor resultado.
 * 
 * weka.classifiers.functions.MultilayerPerceptron
 * -L 0.3 -M 0.2 -N 250 -V 0 -S 1 -E 1 -H "3, 4, 5" -R
 *
 * @link https://findusages.com/repository/RPuSQfKEcWSdzPHvQtSWozab?file=src/gatech/cs7641/dgonzalez42/assignment3/NeuralNetworkRun.java&lines=90,144,102-102
 * @link https://www.programcreek.com/java-api-examples/index.php?api=weka.classifiers.functions.MultilayerPerceptron
 * @link http://www.cs.ubc.ca/labs/beta/Projects/autoweka/
 *
 * @author Alisson G. Chiquitto <alisson.chiquitto@ifms.edu.br>
 */
public class HiddenLayersVariation {
    
    private static String arquivoSaida;

    public static void main(String[] args) {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmm");
        
        arquivoSaida = Config.PATH_DATA + "/hiddenLayers-"
                + dateFormat.format(new Date())
                + ".csv";
        
        // MLPTest t = new MLPTest();
        test3HiddenLayersVariation();

    }
    
    public static void test3HiddenLayersVariation() {
        
        // String[] options = {"-L", "0.3", "-M", "0.2", "-N", "250", "-V", "0",
        //    "-S", "1", "-E", "1", "-H", "", "-R"};
        
        // try {
        //     MLPTest t = new MLPTest();
        // } catch (Exception ex) {
        //     ex.printStackTrace();
        //     return;
        // }
        
        List<String> lines = new ArrayList<>();
        Path file = Paths.get(arquivoSaida);
        
        int l1, l2, l3;
        int max = 10;
        String tmp, line;
        MLPRun mlp;
        
        Instances data;
        try {
            data = FullInstances.factory();
        } catch (Exception ex) {
            ex.printStackTrace();
            
            return;
        }
        
        for (l1 = 2; l1 <= max; l1++) {
            for (l2 = 2; l2 <= max; l2++) {
                for (l3 = 2; l3 <= max; l3++) {
                    
                    try {
                        
                        tmp = l1 + "," + l2 + "," + l3;
                        line = tmp;
                        
                        System.out.println(tmp);
                        
                        mlp = new MLPRun();
                        mlp.mlp.setHiddenLayers(l1 + "," + l2 + "," + l3);
                        Evaluation eval = mlp.evaluate(data, data);
                        
                        tmp = String.valueOf(eval.correlationCoefficient() * 100);
                        System.out.println(tmp);
                        
                        line += "," + tmp;
                        lines.add(line);
                        
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
            
                }
            }
        }
        
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }

    

}

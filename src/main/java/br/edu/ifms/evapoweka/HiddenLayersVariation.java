package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.instances.FullInstances;
import br.edu.ifms.evapoweka.util.Config;
import br.edu.ifms.evapoweka.util.MLPRun;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

/**
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

    private File output;
    private File[] arffFiles;

    public static void main(String[] args) {

        // new HiddenLayersVariation();
    }

    public HiddenLayersVariation(File output, File[] arffFiles) {

        this.output = output;
        this.arffFiles = arffFiles;

        run();

    }

    private void run() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmm");

        test3HiddenLayersVariation();

    }

    public void test3HiddenLayersVariation() {

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
        int l1, l2, l3;
        int min = 5;
        int max = 20;
        MLPRun mlp;

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

        PrintWriter writer;
        try {
            writer = new PrintWriter(this.output.getAbsoluteFile(), "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        for (l1 = min; l1 <= max; l1++) {
            for (l2 = min; l2 <= max; l2++) {
                for (l3 = min; l3 <= max; l3++) {

                    line = new ArrayList<>();
                    line.add(Integer.toString(l1));
                    line.add(Integer.toString(l2));
                    line.add(Integer.toString(l3));
                    line.add(Integer.toString(l1 + l2 + l3));

                    System.out.println(line);

                    mlp = new MLPRun();
                    mlp.mlp.setHiddenLayers(l1 + "," + l2 + "," + l3);

                    for (Instances instancesItem : instancesList) {

                        try {

                            Evaluation eval = mlp.evaluate(instancesItem, instancesItem);

                            tmp = String.valueOf(eval.correlationCoefficient() * 100);
                            System.out.println(tmp);

                            line.add(tmp);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                    tmp = String.join(",", line);
                    writer.println(tmp);
                    writer.flush();

                    // lines.add(tmp);
                    // break;
                }

                // break;
            }

            // break;
        }

        writer.close();

        // try {
        //     Files.write(file, lines, Charset.forName("UTF-8"));
        // } catch (IOException ex) {
        //     ex.printStackTrace();
        // }
    }

}

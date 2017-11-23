package br.edu.ifms.evapoweka;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 * @link https://svn.cms.waikato.ac.nz/svn/weka/trunk/weka/src/main/java/weka/core/converters/
 * @link https://svn.cms.waikato.ac.nz/svn/weka/trunk/weka/src/main/java/weka/core/converters/AbstractFileLoader.java
 * @link https://svn.cms.waikato.ac.nz/svn/weka/trunk/weka/src/main/java/weka/core/converters/CSVLoader.java
 *
 * @author Alisson G. Chiquitto <chiquitto@gmail.com>
 */
public class Csv2Arff {

    public static void main(String[] args) {
        String path = "/home/alisson/work/evapoweka/data/dados-climaticos-regiao2/dados-climaticos-verao.csv";

        File f = new File(path);

        CSVLoader loader = new CSVLoader();
        try {
            loader.setSource(f);
            loader.setDateFormat("dd/MM/yyyy");
            loader.setDateAttributes("1");

            Instances structure = loader.getStructure();

            File file = new File(path + ".arff");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);

            writer.write(structure.toString() + "\n");

            Instance temp;
            do {
                temp = loader.getNextInstance(structure);
                if (temp != null) {
                    writer.write(temp.toString() + "\n");
                }
            } while (temp != null);

            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}

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

    private File newFile;

    public static void main(String[] args) {

        // new Csv2Arff("");
    }

    public Csv2Arff(File file) {

        run(file);

    }

    public File getNewFile() {
        return newFile;
    }

    private void run(File file) {

        CSVLoader loader = new CSVLoader();
        try {
            loader.setSource(file);
            // loader.setDateFormat("dd/MM/yyyy");
            // loader.setDateAttributes("1");
            loader.setNumericAttributes("first-last");

            Instances structure = loader.getStructure();

            this.newFile = new File(file.getAbsoluteFile() + ".arff");
            if (this.newFile.exists()) {
                this.newFile.delete();
            }
            this.newFile.createNewFile();
            FileWriter writer = new FileWriter(this.newFile);

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

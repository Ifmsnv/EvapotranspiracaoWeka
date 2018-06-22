package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.instances.FullInstances;
import br.edu.ifms.evapoweka.util.Config;
import br.edu.ifms.evapoweka.util.MLPRun;
import java.io.File;
import weka.core.Instances;

public class GenerateModel {

    public static void main(String[] args) {
        try {
            generate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void generate() throws Exception {
        
        Instances train = FullInstances.factory(new File(Config.PATH_DATA + "/amabai/verao.csv-FINAL.csv.arff"));

        MLPRun mlp = new MLPRun();
        mlp.printConfig();
        mlp.mlp.setHiddenLayers("10,17,6");
        mlp.mlp.buildClassifier(train);
        
        weka.core.SerializationHelper.write(Config.PATH_DATA + "/mlp.model", mlp.mlp);
    }
    
}

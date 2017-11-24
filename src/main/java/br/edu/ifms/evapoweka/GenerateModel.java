package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.instances.FullInstances;
import br.edu.ifms.evapoweka.util.Config;
import br.edu.ifms.evapoweka.util.MLPRun;
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
        
        Instances train = FullInstances.factory();

        MLPRun mlp = new MLPRun();
        mlp.mlp.setHiddenLayers("10,2,9");
        mlp.mlp.buildClassifier(train);
        
        weka.core.SerializationHelper.write(Config.PATH_DATA + "/mlp.model", mlp.mlp);
    }
    
}

package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.instances.FullInstances;
import br.edu.ifms.evapoweka.util.MLPRun;
import weka.core.Instances;

public class Test {

    public static void main(String[] args) {

        try {
            run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void run() throws Exception {
        Instances data = FullInstances.factory();

        MLPRun mlp = new MLPRun();
        mlp.mlp.setHiddenLayers("10,2,9");
        mlp.mlp.buildClassifier(data);
        
        int x = 100;
        System.out.println(data.get(x));
        double i = mlp.mlp.classifyInstance(data.get(x));
        
        System.out.println(i);
        
        //Evaluation eval = mlp.evaluate(data, data);
        //mlp.printConfig();
        //mlp.printResults(eval, 0);
        
    }

}

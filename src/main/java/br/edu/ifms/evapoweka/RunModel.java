package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.util.Config;
import java.util.ArrayList;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class RunModel {

    public static void main(String[] args) {
        
        try {
            run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }

    private static void run() throws Exception {
        
        MultilayerPerceptron mlp = (MultilayerPerceptron) weka.core.SerializationHelper.read(Config.PATH_DATA + "/mlp.model");
        
        ArrayList<Attribute> attributeList = new ArrayList<Attribute>(3);

        Attribute tmax = new Attribute("TMAX");
        Attribute tmin = new Attribute("TMIN");
        Attribute etp = new Attribute("ETP-Pmoith");

        attributeList.add(tmax);
        attributeList.add(tmin);
        attributeList.add(etp);
        // attributeList.add(new Attribute("@@class@@"));
        
        Instances data = new Instances("TestInstances",attributeList,0);

        Instance inst_co = new DenseInstance(data.numAttributes());
        data.add(inst_co);

        inst_co.setValue(tmax, 26.4);
        inst_co.setValue(tmin, 8.8);
        // inst_co.setValue(etp, 3);
        
        System.out.println(mlp.classifyInstance(inst_co));
        
        
    }
    
}

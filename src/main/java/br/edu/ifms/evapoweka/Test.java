package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.instances.FullInstances;
import br.edu.ifms.evapoweka.util.MLPRun;
import java.util.ArrayList;
import java.util.Enumeration;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * https://stackoverflow.com/questions/13029118/classifying-single-instance-in-weka
 * 
 * @author Alisson G. Chiquitto <chiquitto@gmail.com>
 */
public class Test {

    public static void main(String[] args) {

        try {
            run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void run() throws Exception {
        Instances train = FullInstances.factory();

        MLPRun mlp = new MLPRun();
        mlp.mlp.setHiddenLayers("10,2,9");
        mlp.mlp.buildClassifier(train);
        
//        Enumeration<Attribute> attrs = data.enumerateAttributes();
//        while (attrs.hasMoreElements()) {
//            Attribute nextElement = attrs.nextElement();
//            System.out.println(nextElement);
//        }
//        
//        System.out.println(data.classAttribute());
//        return;

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
        
        System.out.println(mlp.mlp.classifyInstance(inst_co));

        int x = 100;
        double i = mlp.mlp.classifyInstance(train.get(x));

        System.out.println(train.get(x));
        System.out.println(i);
        System.out.println("2.0377695563738625");

        //Evaluation eval = mlp.evaluate(data, data);
        //mlp.printConfig();
        //mlp.printResults(eval, 0);
    }

}

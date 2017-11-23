package br.edu.ifms.evapoweka.util;

import br.edu.ifms.evapoweka.instances.FullInstances;
import java.text.DecimalFormat;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

/**
 *
 * @author Alisson G. Chiquitto <chiquitto@gmail.com>
 */
public class MLPRun {

    // private static final DecimalFormat DF = new DecimalFormat("0.0000");

    public MultilayerPerceptron mlp;

    public MLPRun() {
        mlp = MultilayerPerceptronFactory.factory();
    }
    
    public MLPRun(String[] options) {
        mlp = MultilayerPerceptronFactory.factory(options);
    }

    public Evaluation evaluate(Instances train, Instances test) {
        try {
            mlp.buildClassifier(train);

            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(mlp, test);
            
            return eval;

            // printConfig();
            // printResults(eval, data.numAttributes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void printConfig() {
        
        // Print configuration
        
        System.out.println("*** Configuration ***");
        System.out.println("NominalToBinaryFilter: " + mlp.getNominalToBinaryFilter());
        System.out.println("NormalizeNumericClass: " + mlp.getNormalizeNumericClass());
        System.out.println("NormalizeAttributes: " + mlp.getNormalizeAttributes());
        System.out.println("Learning Rate: " + mlp.getLearningRate());
        System.out.println("Momentum Rate: " + mlp.getMomentum());
        System.out.println("Hidden Layers: " + mlp.getHiddenLayers());
        System.out.println("*********************\n");
        
    }

    public void printResults(Evaluation eval, int numInstances) {
        
        // Print results
        System.out.println(eval.toSummaryString("Results\n", false));
        return;
        
//        System.out.println("Test correct = " + eval.correct()
//                + " (" + DF.format(eval.correct() / numInstances * 100.0) + "%)");
//        System.out.println("Test incorrect = " + eval.incorrect()
//                + " (" + DF.format(eval.incorrect() / numInstances * 100.0) + "%)\n");
        
    }

}

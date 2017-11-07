package evapotranspiracaoweka;

import weka.classifiers.functions.MultilayerPerceptron;

/**
 *
 * @author Alisson G. Chiquitto <chiquitto@gmail.com>
 */
public class MultilayerPerceptronFactory {
    
    public static MultilayerPerceptron factory() {
        MultilayerPerceptron mlp = new MultilayerPerceptron();

        mlp.setAutoBuild(true);
        mlp.setBatchSize("100");
        mlp.setHiddenLayers("3,4,5");
        mlp.setLearningRate(0.3);
        mlp.setMomentum(0.2);
        mlp.setNominalToBinaryFilter(true);
        mlp.setNormalizeAttributes(true);
        mlp.setNormalizeNumericClass(true);
        mlp.setSeed(1);
        mlp.setTrainingTime(500);
        mlp.setValidationSetSize(0);
        mlp.setValidationThreshold(20);
        
        return mlp;
    }
    
    public static MultilayerPerceptron factory(String[] options) {
        MultilayerPerceptron mlp = new MultilayerPerceptron();

        try {
            mlp.setOptions(options);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return mlp;
    }
    
}

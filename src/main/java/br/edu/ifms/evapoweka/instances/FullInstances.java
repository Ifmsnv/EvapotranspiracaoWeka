package br.edu.ifms.evapoweka.instances;

import br.edu.ifms.evapoweka.Config;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author Alisson G. Chiquitto <chiquitto@gmail.com>
 */
public class FullInstances {

    public static Instances factory() throws Exception {
        ConverterUtils.DataSource source;

        source = new ConverterUtils.DataSource(Config.PATH
                + "/dados-climaticos/dados-climaticos-trainer.arff");
        
        Instances data = source.getDataSet();
        
        data.setClassIndex(data.numAttributes()-1);

        return data;
    }

}


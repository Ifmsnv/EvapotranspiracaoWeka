package br.edu.ifms.evapoweka.instances;

import br.edu.ifms.evapoweka.util.Config;
import java.io.File;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author Alisson G. Chiquitto <chiquitto@gmail.com>
 */
public class FullInstances {

    public static Instances factory(File file) throws Exception {
        ConverterUtils.DataSource source;
        
        //source = new ConverterUtils.DataSource(Config.PATH_DATA
        //        + "/dados-climaticos-regiao2/dados-climaticos-inverno.csv.arff");
        
        source = new ConverterUtils.DataSource(file.getAbsolutePath());
        
        Instances data = source.getDataSet();
        
        data.setClassIndex(data.numAttributes()-1);

        return data;
    }

}


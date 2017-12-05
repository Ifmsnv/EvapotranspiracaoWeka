package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.util.Config;
import java.io.File;

public class Processo2 {
    
    private final String PATH;
    private final String CSV;
    
    private final File csvFile;

    public Processo2() {
        
        this.PATH = Config.PATH_DATA + "/dados-climaticos-roncador-mes";
        this.CSV = this.PATH + "/RONCADOR-DF-ENVIADO-CHIQUITTO.csv";
        
        this.csvFile = new File(this.CSV);
        
        // Separar o arquivo em estações (CsvSeparatorSeasons)
        CsvSeparatorMonth a = new CsvSeparatorMonth(this.csvFile);
        
        // Remover serie DATA para os arquivos gerados
        File[] files = a.getCsvFile();
        
        for (File file : files) {
            CsvRemoveDataAttr b = new CsvRemoveDataAttr(file);
        
            // Gerar arquivos ARFF (Csv2Arff)
            Csv2Arff c = new Csv2Arff(b.getNewFile());
        
            // Calcular a melhor distribuição de neorônios (HiddenLayersVariation)
            HiddenLayersVariation d = new HiddenLayersVariation(c.getNewFile());
        }
        
    }
    
    public static void main(String[] args) {
        
        Processo2 p = new Processo2();
        
    }
    
}

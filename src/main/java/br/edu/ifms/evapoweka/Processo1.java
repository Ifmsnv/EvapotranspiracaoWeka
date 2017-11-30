package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.util.Config;
import java.io.File;

public class Processo1 {
    
    private final String PATH;
    private final String CSV;
    
    private final File csvFile;

    public Processo1() {
        
        this.PATH = Config.PATH_DATA + "/dados-climaticos-roncador";
        this.CSV = this.PATH + "/RONCADOR-DF-ENVIADO-CHIQUITTO.csv";
        
        this.csvFile = new File(this.CSV);
        
        // Separar o arquivo em estações (CsvSeparatorSeasons)
        CsvSeparatorSeasons a = new CsvSeparatorSeasons(this.csvFile);
        
        // Remover serie DATA para os arquivos gerados
        File[] files = {
            a.getCsvFileInverno(),
            a.getCsvFileOutono(),
            a.getCsvFilePrimavera(),
            a.getCsvFileVerao()
        };
        for (File file : files) {
            CsvRemoveDataAttr b1 = new CsvRemoveDataAttr(file);
        }
        
        // Gerar arquivos ARFF (Csv2Arff)
        // Calcular a melhor distribuição de neorônios (HiddenLayersVariation)
    }
    
    public static void main(String[] args) {
        
        Processo1 p = new Processo1();
        
    }
    
}

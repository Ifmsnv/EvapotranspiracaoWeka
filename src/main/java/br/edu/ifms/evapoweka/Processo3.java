package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.util.Config;
import br.edu.ifms.evapoweka.util.SQLiteJDBC;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Processo3 {

    private final String PATH;
    private final String CSV;

    private final File csvFile;

    public Processo3() {
        
        SQLiteJDBC.getInstance();
        
        System.exit(0);

        this.PATH = Config.PATH_DATA + "/dados-climaticos-roncador";
        this.CSV = this.PATH + "/RONCADOR-DF-ENVIADO-CHIQUITTO.csv";

        this.csvFile = new File(this.CSV);

        // Separar o arquivo em estações (CsvSeparatorSeasons)
        CsvSeparatorSeasons a = new CsvSeparatorSeasons(this.csvFile);

        // Remover serie DATA para os arquivos gerados
        File[] csvFiles = {
            a.getCsvFileVerao(),
            a.getCsvFileOutono(),
            a.getCsvFileInverno(),
            a.getCsvFilePrimavera()
        };

        File[] arffFiles = new File[csvFiles.length];

        int i = 0;
        for (File csvFile : csvFiles) {
            CsvRemoveDataAttr b = new CsvRemoveDataAttr(csvFile);

            // Gerar arquivos ARFF (Csv2Arff)
            Csv2Arff c = new Csv2Arff(b.getNewFile());
            arffFiles[i] = c.getNewFile();
            
            i++;
        }

        // Calcular a melhor distribuição de neorônios (HiddenLayersVariation)
        File output = new File(this.csvFile + "-3layers.csv");
        HiddenLayersVariation d = new HiddenLayersVariation(output, arffFiles);

    }

    public static void main(String[] args) {

        Processo3 p = new Processo3();

    }

}

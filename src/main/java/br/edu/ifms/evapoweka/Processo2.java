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
        File[] csvFiles = a.getCsvFile();

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

        Processo2 p = new Processo2();

    }

}

package br.edu.ifms.evapoweka;

import br.edu.ifms.evapoweka.util.Config;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 * Esta classe faz a separacao dos dados por estacoes do ano.
 * Os arquivos precisam ter os cabecalhos: Data, TMAX, TMIN, TMED, ETP-Pmoith
 *
 * @author Alisson G. Chiquitto <chiquitto@gmail.com>
 * @link https://www.programcreek.com/java-api-examples/index.php?api=org.apache.commons.csv.CSVPrinter
 */
public class CsvSeparatorSeasons {

    private String filePath;
    private Reader fileReader;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private CSVPrinter csvPrinterVerao;
    private CSVPrinter csvPrinterOutono;
    private CSVPrinter csvPrinterInverno;
    private CSVPrinter csvPrinterPrimavera;

    public static void main(String[] args) {

        CsvSeparatorSeasons x = new CsvSeparatorSeasons(
                Config.PATH_DATA + "/dados-climaticos-regiao2/dados-climaticos.csv"
        );

    }

    public CsvSeparatorSeasons(String filePath) {

        this.filePath = filePath;

        try {

            this.fileReader = new FileReader(this.filePath);

            initCsvPrinterVerao();
            initCsvPrinterOutono();
            initCsvPrinterInverno();
            initCsvPrinterPrimavera();

            parseLines();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } finally {
            try {
                this.fileReader.close();

                this.csvPrinterVerao.close();
                this.csvPrinterOutono.close();
                this.csvPrinterInverno.close();
                this.csvPrinterPrimavera.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Testa a data e retorna 1 (Verao) SE 21 de dezembro a 20 de março / 2
     * (Outono) SE 20 de março a 21 de junho / 3 (Inverno) - 21 de junho a 22 de
     * setembro / 4 (Primavera) SE 22 de setembro a 21 de dezembro
     *
     * @return int Numero indicando a estacao
     */
    private int getEstacao(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);

        // Verao
        switch (calendar.get(Calendar.MONTH)) {
            case 0:
            case 1:
                return 1;
            case 2:
                if ((calendar.get(Calendar.DAY_OF_MONTH) <= 20)) {
                    return 1;
                } else {
                    return 2;
                }
            case 3:
            case 4:
                return 2;
            case 5:
                if ((calendar.get(Calendar.DAY_OF_MONTH) <= 21)) {
                    return 2;
                } else {
                    return 3;
                }
            case 6:
            case 7:
                return 3;
            case 8:
                if ((calendar.get(Calendar.DAY_OF_MONTH) <= 22)) {
                    return 3;
                } else {
                    return 4;
                }
            case 9:
            case 10:
                return 4;
            case 11:
                if ((calendar.get(Calendar.DAY_OF_MONTH) <= 21)) {
                    return 4;
                } else {
                    return 1;
                }
        }

        return 0;
    }

    private CSVPrinter initCsvPrinter(String estacao)
            throws IOException {

        String fileName = this.filePath.replace(".csv", "-" + estacao + ".csv");

        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180);

        List<String> headers = new ArrayList<String>();

        headers.add("Data");
        headers.add("TMAX");
        headers.add("TMIN");
        // headers.add("TMED");
        headers.add("ETP-Pmoith");
        
        csvPrinter.printRecord(headers);

        return csvPrinter;

    }

    private void initCsvPrinterInverno() throws IOException {

        this.csvPrinterInverno = this.initCsvPrinter("inverno");

    }

    private void initCsvPrinterOutono() throws IOException {

        this.csvPrinterOutono = this.initCsvPrinter("outono");

    }

    private void initCsvPrinterPrimavera() throws IOException {

        this.csvPrinterPrimavera = this.initCsvPrinter("primavera");

    }

    private void initCsvPrinterVerao() throws IOException {

        this.csvPrinterVerao = this.initCsvPrinter("verao");

    }

    private Iterable<CSVRecord> obtainCsvIterable()
            throws FileNotFoundException, IOException {

        // Iterable<CSVRecord> records = CSVFormat.RFC4180
        //        .withHeader("Data","TMAX","TMIN","TMED","ETP-Pmoith").parse(in);
        return CSVFormat.RFC4180
                .withFirstRecordAsHeader()
                .parse(this.fileReader);

    }

    private void parseLines()
            throws IOException, ParseException {

        Iterable<CSVRecord> records = obtainCsvIterable();

        for (CSVRecord record : records) {
            String dataString = record.get("Data");
            // String tmax = record.get("TMAX");
            // String tmin = record.get("TMIN");
            // String tmed = record.get("TMED");
            // String etpPmoith = record.get("ETP-Pmoith");

            Date data = this.dateFormat.parse(dataString);
            int estacao = getEstacao(data);

            switch (estacao) {
                case 1:
                    csvPrinterVerao.printRecord(record);
                    break;
                case 2:
                    csvPrinterOutono.printRecord(record);
                    break;
                case 3:
                    csvPrinterInverno.printRecord(record);
                    break;
                case 4:
                    csvPrinterPrimavera.printRecord(record);
                    break;
            }
        }

    }

}

package br.edu.ifms.evapoweka;

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
 * Esta classe faz a separacao dos dados por meses do ano. Os arquivos precisam
 * ter os cabecalhos: Data, TMAX, TMIN, TMED, ETP-Pmoith
 *
 * @author Alisson G. Chiquitto <chiquitto@gmail.com>
 * @link https://www.programcreek.com/java-api-examples/index.php?api=org.apache.commons.csv.CSVPrinter
 */
public class CsvSeparatorMonth {

    private File file;
    private Reader fileReader;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private File[] csvFile;

    private CSVPrinter[] csvPrinter;

    public static void main(String[] args) {

        // CsvSeparatorSeasons x = new CsvSeparatorSeasons(
        //         Config.PATH_DATA + "/dados-climaticos-regiao2/dados-climaticos.csv"
        // );
    }

    public CsvSeparatorMonth(File file) {

        this.file = file;

        try {

            this.fileReader = new FileReader(this.file);

            initCsvPrinter();

            parseLines();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } finally {
            try {
                this.fileReader.close();

                for (int month = 0; month < 12; month++) {
                    this.csvPrinter[month].close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * 
     * @return int Numero indicando a estacao
     */
    private int calculateMonth(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        
        return calendar.get(Calendar.MONTH) + 1;
    }

    public File[] getCsvFile() {
        return csvFile;
    }

    private File initCsvFile(int month) {
        String newFileAbsolutePath = this.file
                .getAbsolutePath()
                .replace(".csv", "-" + (month + 1) + ".csv");

        return new File(newFileAbsolutePath);
    }

    private void initCsvPrinter()
            throws IOException {
        
        this.csvFile = new File[12];
        this.csvPrinter = new CSVPrinter[12];

        for (int month = 0; month < 12; month++) {
            this.csvFile[month] = this.initCsvFile(month);

            if (this.csvFile[month].exists()) {
                this.csvFile[month].delete();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(this.csvFile[month].getAbsoluteFile()));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180);

            List<String> headers = new ArrayList<String>();

            headers.add("DATA");
            headers.add("TMAX");
            headers.add("TMIN");
            headers.add("ETP");

            csvPrinter.printRecord(headers);
            
            this.csvPrinter[month] = csvPrinter;

        }

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
            String dataString = record.get("DATA");

            Date data = this.dateFormat.parse(dataString);
            int month = calculateMonth(data) - 1;
            
            this.csvPrinter[month].printRecord(record);
            this.csvPrinter[month].flush();
        }

    }

}

package br.edu.ifms.evapoweka;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/*
 * https://examples.javacodegeeks.com/core-java/apache/commons/csv-commons/writeread-csv-files-with-apache-commons-csv-example/
 * 
 * @author Alisson G. Chiquitto <chiquitto@gmail.com>
 */
public class CsvRemoveDataAttr {
    
    private File file;
    private File newFile;
    private Reader fileReader;
    private CSVPrinter csvPrinter;
    
    public static void main(String[] args) {
        
        // File file = new File("/home/alisson/work/evapoweka/data/dados-climaticos-roncador/RONCADOR-DF-ENVIADO-CHIQUITTO-inverno.csv");
        
        // new CsvRemoveDataAttr(file);
        
    }

    public CsvRemoveDataAttr(File file) {
        
        this.file = file;
        
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public File getNewFile() {
        return newFile;
    }
    
    private CSVPrinter initCsvPrinter()
            throws IOException {

        String newFileAbsolutePath = this.file.getAbsolutePath() + "-FINAL.csv";

        this.newFile = new File(newFileAbsolutePath);
        if (this.newFile.exists()) {
            this.newFile.delete();
        }
        
        FileWriter fileWriter = new FileWriter(this.newFile);
        CSVPrinter localCsvPrinter = new CSVPrinter(fileWriter, CSVFormat.RFC4180);

        //BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        //CSVPrinter localCsvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180);

        List<String> headers = new ArrayList<>();

        headers.add("TMAX");
        headers.add("TMIN");
        headers.add("ETP");
        
        localCsvPrinter.printRecord(headers);

        return localCsvPrinter;

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

        int i = 2;
        for (CSVRecord record : records) {
            
            Map<String,String> m = record.toMap();
            
            List newRecord = new ArrayList();
            newRecord.add(m.get("TMAX"));
            newRecord.add(m.get("TMIN"));
            newRecord.add(m.get("ETP"));
            
            // System.out.print(i + " ");
            // System.out.println(newRecord);
            
            this.csvPrinter.printRecord(newRecord);
            this.csvPrinter.flush();
            
            i++;
            
        }

    }
    
    private void run()
            throws FileNotFoundException, IOException, ParseException {
        
        this.fileReader = new FileReader(this.file);
        
        this.csvPrinter = this.initCsvPrinter();
        this.parseLines();
        
    }
    
}

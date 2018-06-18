package br.edu.ifms.evapoweka.util;

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
import org.apache.commons.io.FilenameUtils;

/*
 * Esta classe faz a separacao dos dados por estacoes do ano.
 * Os arquivos precisam ter os cabecalhos: Data, TMAX, TMIN, TMED, ETP-Pmoith
 *
 * @author Alisson G. Chiquitto <chiquitto@gmail.com>
 * @link https://www.programcreek.com/java-api-examples/index.php?api=org.apache.commons.csv.CSVPrinter
 */
public class CsvSeparatorSeasonsByMonth {

    private File file;
    private Reader fileReader;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    private File csvFileInverno;
    private File csvFileOutono;
    private File csvFilePrimavera;
    private File csvFileVerao;

    private CSVPrinter csvPrinterVerao;
    private CSVPrinter csvPrinterOutono;
    private CSVPrinter csvPrinterInverno;
    private CSVPrinter csvPrinterPrimavera;

    public CsvSeparatorSeasonsByMonth(File file) {

        this.file = file;

        try {

            this.fileReader = new FileReader(this.file);

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

    /*
     * Testa a data e retorna 1 (Verao) SE 21 de dezembro a 20 de março / 2
     * (Outono) SE 20 de março a 21 de junho / 3 (Inverno) - 21 de junho a 22 de
     * setembro / 4 (Primavera) SE 22 de setembro a 21 de dezembro
     *
     * @return int Numero indicando a estacao
     */
    private int calculateEstacao(Date data) {
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

    public File getCsvFileInverno() {
        return csvFileInverno;
    }

    public File getCsvFileOutono() {
        return csvFileOutono;
    }

    public File getCsvFilePrimavera() {
        return csvFilePrimavera;
    }

    public File getCsvFileVerao() {
        return csvFileVerao;
    }
    
    private File initCsvFile(String estacao) {
        String dir = FilenameUtils.removeExtension(this.file.getAbsolutePath());
        new File(dir).mkdir();
        
        String newFileAbsolutePath = this.file
                .getAbsolutePath()
                .replace(".csv", "/" + estacao + ".csv")
                ;

        return new File(newFileAbsolutePath);
    }

    private CSVPrinter initCsvPrinter(File file)
            throws IOException {

        if (file.exists()) {
            file.delete();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180);

        List<String> headers = new ArrayList<String>();

        headers.add("MES");
        headers.add("ANO");
        headers.add("TMAX");
        headers.add("TMIN");
        headers.add("ETP");
        
        csvPrinter.printRecord(headers);

        return csvPrinter;

    }

    private void initCsvPrinterInverno() throws IOException {
        
        this.csvFileInverno = this.initCsvFile("inverno");
        this.csvPrinterInverno = this.initCsvPrinter(this.csvFileInverno);

    }

    private void initCsvPrinterOutono() throws IOException {

        this.csvFileOutono = this.initCsvFile("outono");
        this.csvPrinterOutono = this.initCsvPrinter(this.csvFileOutono);

    }

    private void initCsvPrinterPrimavera() throws IOException {

        this.csvFilePrimavera = this.initCsvFile("primavera");
        this.csvPrinterPrimavera = this.initCsvPrinter(this.csvFilePrimavera);

    }

    private void initCsvPrinterVerao() throws IOException {

        this.csvFileVerao = this.initCsvFile("verao");
        this.csvPrinterVerao = this.initCsvPrinter(this.csvFileVerao);

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
            // Mês,Ano,Tmáx,Tmin,ETP
            String mes = record.get("MES");
            String ano = record.get("ANO");
            String dataString = String.format("01/%s/%s", mes, ano);

            Date data = this.dateFormat.parse(dataString);
            int estacao = calculateEstacao(data);

            switch (estacao) {
                case 1:
                    csvPrinterVerao.printRecord(record);
                    csvPrinterVerao.flush();
                    break;
                case 2:
                    csvPrinterOutono.printRecord(record);
                    csvPrinterOutono.flush();
                    break;
                case 3:
                    csvPrinterInverno.printRecord(record);
                    csvPrinterInverno.flush();
                    break;
                case 4:
                    csvPrinterPrimavera.printRecord(record);
                    csvPrinterPrimavera.flush();
                    break;
            }
        }

    }

}

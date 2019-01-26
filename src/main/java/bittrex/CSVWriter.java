package bittrex;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVWriter {
    private static final String SAMPLE_CSV_FILE = "./sample.csv";

    //private writer;

    public static void main(String[] args) throws IOException {
            CSVWriter csvWriter = new CSVWriter();
            csvWriter.writeFile();
    }


    private void writeFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(BittrexParser.SAMPLE_CSV_OUTPUT_PATH)));

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL
                .withHeader("ID", "Name", "Designation", "Company"));

        csvPrinter.printRecord("1", "Sundar Pichai ?", "CEO", "Google");
        csvPrinter.printRecord("2", "Satya Nadella", "CEO", "Microsoft");
        csvPrinter.printRecord("3", "Tim cook", "CEO", "Apple");



        csvPrinter.printRecord(Arrays.asList("4", "Mark Zuckerberg", "CEO", "Facebook"));
        csvPrinter.printRecord(Arrays.asList("5", "sadas", "fsdfsdd", "vasasdasd"));
        ArrayList<String> a  = new ArrayList(0);
        a.add("6");
        a.add("a");
        a.add("dfsfs");
        a.add("GDGDGD");
        csvPrinter.printRecord(a);

        csvPrinter.flush();
    }
}

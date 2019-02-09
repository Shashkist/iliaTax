package bittrex;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

/**
 * Created by Iliap on 12/29/2018.
 */
public class BittrexParser {


    public static final String SAMPLE_CSV_FILE_PATH = "C:\\work\\projects\\Tax\\Tasks\\Bittrex\\igor2.txt";
    public static final String SAMPLE_CSV_OUTPUT_PATH = "C:\\work\\projects\\Tax\\Tasks\\Bittrex\\Outputigor2.csv";
    public static final String SAMPLE_CSV_OUTPUT_PATH3 = "C:\\work\\projects\\Tax\\Tasks\\Bittrex\\Outputigor3.csv";
    public static final String CSV_BITCOIN_2017_PRICE = "C:\\work\\projects\\Tax\\Tasks\\Bittrex\\BTCPRICE.csv";
    public static final String BITCOIN_USD_2017 = "C:\\work\\projects\\Tax\\Tasks\\Bittrex\\BTCPRICE.csv";


    public static final double TAX_PERCENT = 0.25;

    public static void main(String[] args) throws IOException, ParseException {
        BittrexParser2.dateIteratorCreateCSVBitcoinRateS();
/*
            BufferedReader Reader = new BufferedReader(new InputStreamReader(new
                FileInputStream(SAMPLE_CSV_FILE_PATH)));

            //Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH), Charset.forName("UTF-8"));
            CSVParser csvParser = new CSVParser(Reader, CSVFormat.DEFAULT);
            CSVParser csvParser2 = new CSVParser(Reader, CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());

            for (CSVRecord csvRecord : csvParser) {
                System.out.println("Record No - " + csvRecord.getRecordNumber());
                System.out.println("---------------");
                // Accessing Values by Column Index
                if (csvRecord.size() >= 1) {
                    String name = csvRecord.get(0);
                    System.out.println("Name : " + name);

                }
                if (csvRecord.size() >= 2) {
                    String email = csvRecord.get(1);
                    System.out.println("Email : " + email);

                }
                if (csvRecord.size() >= 3) {
                    String phone = csvRecord.get(2);
                    System.out.println("Phone : " + phone);

                }
                if (csvRecord.size() >= 4) {
                    String country = csvRecord.get(3);
                    System.out.println("Country : " + country);

                }
                if (csvRecord.size() >= 8) {
                    String date = csvRecord.get(7);
                    System.out.println("DAte : " + date);

                }

                System.out.println("---------------\n\n");
                }*/
            }
    }


package bittrex;

/**
 * Created by Iliap on 12/29/2018.
 */


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.*;
import java.util.*;

public class BittrexParser2
{

    public static void main(String[] args) throws ParseException, IOException {
        Wallet wallet = initWallet();

        //Input file which needs to be parsed
        String fileToParse = BittrexParser.SAMPLE_CSV_FILE_PATH;

        BufferedReader fileReader = null;


        //Delimiter used in CSV file
        final String DELIMITER = ",";
        final String DELIMITER2 = "\t";

        try
        {
            String line = "";
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));
            BufferedReader fileReader2 = new BufferedReader(new FileReader(fileToParse));

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(BittrexParser.SAMPLE_CSV_OUTPUT_PATH3)));

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL
                    .withHeader("Number", "From","To", "Buy-Sell", "Quantity", "Price for one", "All price","Buying date", "Selling Date", "balance from", "balance to", "priceBoughtUSd", "priceSoldUSD", "GainUSD", "taxToPayUSD", "taxToPayILS"));

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            int counter = 0;
            //Read the file line by line

            LinkedList<BittrexTransaction> bittrexTransactions = gatherBittrexTransactions(fileReader2);
            System.out.print(bittrexTransactions);
            Collections.sort(bittrexTransactions);//sort by closed
            System.out.print(bittrexTransactions);

            for (BittrexTransaction bittrexTransaction : bittrexTransactions)
            {
                counter++;
                String[] fromTo = bittrexTransaction.getExchange().split("-");
                Boolean buySell = bittrexTransaction.getType().equals("LIMIT_BUY") ? Boolean.TRUE : Boolean.FALSE;
                Double quantityFrom = !buySell ? Double.valueOf(bittrexTransaction.getQuantity()) : Double.valueOf(bittrexTransaction.getPrice());
                String fromCoin = buySell ? fromTo[0] : fromTo[1];//Buying bitcoin
                String toCoin = !buySell ? fromTo[0] : fromTo[1]; //selling bitcoin
                Double quantityTo = buySell ? Double.valueOf(bittrexTransaction.getQuantity()) : Double.valueOf(bittrexTransaction.getPrice());
                LocalDate transactionDate = new LocalDate(new SimpleDateFormat("MM/dd/yyyy hh:mm").parse(bittrexTransaction.getClosed()));

                addTransaction(wallet, fromCoin, toCoin, quantityFrom, quantityTo, transactionDate, buySell);
                Date startDate = df.parse(bittrexTransaction.getOpened());

                System.out.println(startDate.toString());




                counter++;
            }
            printWalletTransactions(csvPrinter, wallet);
            csvPrinter.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static LinkedList<BittrexTransaction> gatherBittrexTransactions(BufferedReader fileReader) throws IOException {
        LinkedList<BittrexTransaction> bittrexTransactions = new LinkedList<BittrexTransaction>();
        String line = "";
        int counter = 0;
        while ((line = fileReader.readLine()) != null) {
            if (counter == 0) { //skip first line
                counter++;
                continue;
            }
            //Get all tokens available in line
            String[] tokens = line.split("\t");
            BittrexTransaction bittrexTransaction = new BittrexTransaction(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7], tokens[8]);
            bittrexTransactions.add(bittrexTransaction);

        }
        return bittrexTransactions;
    }


    private static void printWalletTransactions(CSVPrinter csvPrinter, Wallet wallet) throws IOException {
        int counter = 0;
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(100000); // 340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
        Collections.sort(wallet.getTransactions());
        for (TransactionCrypto transactionCrypto : wallet.getTransactions()) {
            counter ++ ;
            ArrayList lineList = new ArrayList(0);
            lineList.add(String.valueOf(counter));//number
            lineList.add(transactionCrypto.getSellingCoin());//from
            lineList.add(transactionCrypto.getBuyingCoin());//To
            lineList.add(null);//buysell
            if (transactionCrypto.getSellingQuantity() == null) {
                lineList.add("");
            } else {
                lineList.add(df.format((double) transactionCrypto.getSellingQuantity()));//quantityfrom
            }


            lineList.add(null);//price for one
            if (transactionCrypto.getBuyingQuantity() == null) {
                lineList.add("");
            } else {
                lineList.add(df.format(transactionCrypto.getBuyingQuantity()));//all price
            }

            lineList.add(transactionCrypto.getSellingCoinBoughtDate());//bought date
            lineList.add(transactionCrypto.getTransactionDate());//date

            if (transactionCrypto.getBalanceFrom() == null) {
                lineList.add("");
            } else {
                lineList.add(df.format(transactionCrypto.getBalanceFrom()));
            }
            if (transactionCrypto.getBalanceTo() == null) {
                lineList.add("");
            } else {
                lineList.add(df.format(transactionCrypto.getBalanceTo()));
            }

            if (transactionCrypto.getSellingCoinBoughtPriceUSD() == null) {
                lineList.add("");
            } else {
                lineList.add(df.format(transactionCrypto.getSellingCoinBoughtPriceUSD()));
            }
            if (transactionCrypto.getSellingPriceUSD() == null) {
                lineList.add("");
            } else {
                lineList.add(df.format(transactionCrypto.getSellingPriceUSD()));
            }
            if (transactionCrypto.getSellingPriceUSD() != null && transactionCrypto.getSellingCoinBoughtPriceUSD() != null && transactionCrypto.getSellingQuantity() != null) {
                lineList.add(df.format((transactionCrypto.getSellingPriceUSD() - transactionCrypto.getSellingCoinBoughtPriceUSD()) * transactionCrypto.getSellingQuantity()));
                lineList.add(df.format((transactionCrypto.getSellingPriceUSD() - transactionCrypto.getSellingCoinBoughtPriceUSD()) * transactionCrypto.getSellingQuantity() * BittrexParser.TAX_PERCENT));
            } else {
                lineList.add("");
                lineList.add("");
            }


            csvPrinter.printRecord(lineList);
        }
    }

    //Mockup. we need to use map from file.
    private static Map initBitCoinMap() throws ParseException, IOException {
        String fileToParse = BittrexParser.BITCOIN_USD_2017;
        BufferedReader fileReader = new BufferedReader(new FileReader(fileToParse));
        String line = "";
        int counter = 0;
        HashMap bitcoinMap2017 = new HashMap();
        while ((line = fileReader.readLine()) != null) {
            if (counter == 0) { //skip first line
                counter++;
                continue;
            }
            //Get all tokens available in line
            String[] tokens = line.split(",");
            if (tokens.length >= 2) {
                bitcoinMap2017.put(tokens[0], tokens[1]);
            }


        }

        /*LocalDate sdate = new LocalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01"));
        LocalDate edate = new LocalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-12-31"));
        for (LocalDate date = sdate; date.isBefore(edate); date = date.plusDays(1)) {
            String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date.toDate());
            bitcoinMap2017.put(strDate, Double.valueOf(9000.23));
            // Do your job here with `date`.
            //System.out.println(date);
        }*/
        return bitcoinMap2017;
    }

    /**
     * Method to create transaction - to reduce balance of one coinf in exchange to adding balalnce of another coin.
     * @param wallet
     * @param fromCoin
     * @param toCoin
     * @param date
     * @param buySell
     */
    private static void addTransaction(Wallet wallet, String fromCoin, String toCoin, Double quantityFrom, Double quantityTo, LocalDate date, Boolean buySell) {


        /*if (buySell) {//calculation for buying currency for BTC (Selling BTC - the TAX should be calculated here

        } else {//for selling another Currency for BTC

        }*/
        wallet.makeExchange(quantityFrom, date, fromCoin, toCoin, quantityTo);





    }

    private static Wallet initWallet() throws ParseException, IOException {
        Wallet wallet = new Wallet();
        Map<String, Queue<Coins>> coinsHistory = new HashMap();
        //MOCKUP
        Queue<Coins> queue = new LinkedList<Coins>();
        LocalDate sdate = new LocalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-09-02"));
        Coins bitcoin = new Coins(sdate, "BTC", Double.valueOf(1));
        queue.add(bitcoin);
        LocalDate sdate2 = new LocalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-11-02"));
        Coins bitcoin2 = new Coins(sdate2, "BTC", Double.valueOf(0.81));
        queue.add(bitcoin2);
        coinsHistory.put("BTC", queue);
        wallet.setCoinsHistory(coinsHistory);
        wallet.setTransactions(new ArrayList(0));
        Map bitcoinPriceMap = initBitCoinMap();
        wallet.setBitcoinPriceMap(bitcoinPriceMap);
        return wallet;

    }

    private static void dateIterator() throws ParseException {
        LocalDate sdate = new LocalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01"));
        LocalDate edate = new LocalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-12-31"));
        for (LocalDate date = sdate; date.isBefore(edate); date = date.plusDays(1)) {

            // Do your job here with `date`.
            System.out.println(date);
        }
    }

    public static void dateIteratorCreateCSVBitcoinRateS() throws ParseException, IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(BittrexParser.CSV_BITCOIN_2017_PRICE)));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL
                .withHeader("DATE", "PRICE"));
        DateTime sdate = new DateTime(new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01")).withZone(DateTimeZone.forID("Asia/Jerusalem"));
        DateTime edate = new DateTime(new SimpleDateFormat("yyyy-MM-dd").parse("2017-12-31")).withZone(DateTimeZone.forID("Asia/Jerusalem"));
        //TODO to check this.
        for (DateTime date = sdate; !date.isAfter(edate); date = date.plusDays(1)) {
            ArrayList lineList = new ArrayList(0);
            lineList.add(date.toLocalDate());
            lineList.add(perfromRequest(date.toDate()));
            csvPrinter.printRecord(lineList);
            // Do your job here with `date`.
            System.out.println(date);
        }
        csvPrinter.flush();
    }


    private static Double perfromRequest(Date date) throws IOException {
        String staticURL = "https://min-api.cryptocompare.com/data/pricehistorical?fsym=BTC&tsyms=USD&ts=";
        //SimpleDateFormat df = new SimpleDateFormat();
        //df.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        //String timeStamp = df.format(date.getTime());
        String time = String.valueOf(date.getTime());
        int timeZoneOffset = date.getTimezoneOffset();
        String timeWithoutMillis = String.valueOf(date.getTime() / 1000 + timeZoneOffset*(-60));//+7200 (WA +2GMT +2 hours) to make it to the new date
        URL url = new URL(staticURL + timeWithoutMillis);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //  Map<String, String> parameters = new HashMap<>();
        //parameters.put("param1", "val");

        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setInstanceFollowRedirects(false);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        JSONObject jsonObj = new JSONObject(content.toString());
        Double resultinUSD = null;
        try {
            if (jsonObj.has("BTC") && !jsonObj.isNull("BTC")) {
                // Do something with object.
                JSONObject btcPrice = (JSONObject) jsonObj.get("BTC");
                resultinUSD = btcPrice.getDouble("USD");
            }

        } catch (Exception e) {

        }
        if(resultinUSD != null) {
            System.out.print(resultinUSD);
        }
        return resultinUSD;
        // DataOutputStream out = new DataOutputStream(con.getOutputStream());
        // out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        // out.flush();
        //out.close();
    }
}
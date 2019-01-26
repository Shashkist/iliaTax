package bittrex;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Iliap on 1/3/2019.
 */
public class Wallet {
    private String balance;
    private Map<String, Queue<Coins>> coinsHistory;
    private ArrayList<TransactionCrypto> transactions;
    private Map<String, String> bitcoinPriceMap;

    public Wallet() {
        super();
        transactions = new ArrayList<TransactionCrypto>(0);

    }

    public Map<String, Queue<Coins>> getCoinsHistory() {
        return coinsHistory;
    }

    public Double getBalanceForCoin(String coin) {
        Double balance = Double.valueOf(0);

        Queue<Coins> queueForCoin = coinsHistory.get(coin);
        if (queueForCoin != null) {
            balance = calculateQueueCoinsSum(queueForCoin);
        }
        return balance;
    }

    private double calculateQueueCoinsSum(Queue<Coins> queueForCoin) {
        double sum = 0;
        for(Coins coin : queueForCoin) {
            sum += coin.getBalance();
        }
        return sum;
    }

    public void setCoinsHistory(Map<String, Queue<Coins>> coinsHistory) {
        this.coinsHistory = coinsHistory;
    }

    public Queue<Coins> initQueueForNewCoin(String fromCoin) {
        Queue<Coins> coinQueue = new LinkedList();
        this.coinsHistory.put(fromCoin, coinQueue);
        return coinQueue;
    }

    /**
     * The purpose of the method is to reduce balance of coinQueue from and to increas in coins qeuee to
     * @param quantityFrom
     * @param date
     * @param fromCoin
     * @param toCoin
     * @param quantityTo
     */
    public void makeExchange(Double quantityFrom, LocalDate date, String fromCoin, String toCoin, Double quantityTo) {
        Queue coinsQueueFrom = this.getCoinsHistory().get(fromCoin);
        TransactionCrypto transactionCrypto = new TransactionCrypto();
        if (coinsQueueFrom == null) { //This one cannot be null as we are taking money from here.
            coinsQueueFrom = this.initQueueForNewCoin(fromCoin);
        }
        Queue coinsQueueTo = this.getCoinsHistory().get(toCoin);
        if (coinsQueueTo == null) {
            coinsQueueTo = this.initQueueForNewCoin(toCoin);
            this.getCoinsHistory().put(toCoin, coinsQueueTo);
        }
        Coins coinFrom = (Coins) coinsQueueFrom.peek();
        if (coinFrom == null) {
            //TODO need to handle unknown transaction.
            return;
        }
        double coinFromPrice = coinFrom.getPrice();
        if(coinFromPrice == 0 && coinFrom.getCoinName().equals("BTC")) {
            coinFromPrice = Double.valueOf(bitcoinPriceMap.get(new SimpleDateFormat("yyyy-MM-dd").format(coinFrom.getPurchaseDate().toDate())));

            coinFrom.setPrice(coinFromPrice);
        }
        double sellPrice = Double.valueOf(bitcoinPriceMap.get(new SimpleDateFormat("yyyy-MM-dd").format(date.toDate())));
        LocalDate coinFromPurchaseDate = coinFrom.getPurchaseDate();
        transactionCrypto.setBalanceFrom(getCoinsQueueBalance(coinsQueueFrom));
        transactionCrypto.setSellingCoin(fromCoin);
        transactionCrypto.setSellingPriceUSD(sellPrice);
        transactionCrypto.setTransactionDate(date);
        transactionCrypto.setSellingQuantity(quantityFrom);
        transactionCrypto.setBuyingQuantity(quantityTo);
        transactionCrypto.setBuyingCoin(toCoin);
        transactionCrypto.setSellingPrice(quantityTo);
        if (coinFrom.getBalance() > quantityFrom) {
            coinFrom.setBalance(coinFrom.getBalance() - quantityFrom);
            transactionCrypto.setSellingCoinBoughtDate(coinFromPurchaseDate);
            transactionCrypto.setSellingCoinBoughtPriceUSD(coinFromPrice);

        } else { //take from balance and pick another item from queue

            quantityFrom = quantityFrom - coinFrom.getBalance();
            Double usedQuantity = coinFrom.getBalance();
            coinFrom.setBalance((double) 0);
            Double usedQuantityPrice = coinFromPrice;
            //TODO in case when we have finished we are using price per the last coin we had
            coinsQueueFrom.remove();
            Double averageBuyingPrice = calculateAveragePriceAndRemove(coinsQueueFrom, usedQuantity, quantityFrom, usedQuantityPrice);
            transactionCrypto.setSellingCoinBoughtPriceUSD(averageBuyingPrice);

        }
        transactionCrypto.setBalanceTo(getCoinsQueueBalance(coinsQueueFrom));
        addNewCoinInQueue(date, toCoin, quantityTo, coinsQueueTo);
        this.getTransactions().add(transactionCrypto);


    }

    private Double getCoinsQueueBalance(Queue coinsQueueFrom) {
        double aggregatedBalance = 0;
        for (Object object : coinsQueueFrom) {
            Coins coin = (Coins) object;
            aggregatedBalance += coin.getBalance();
        }
        return aggregatedBalance;
    }

    private Double calculateAveragePriceAndRemove(Queue coinsQueueFrom, Double usedQuantity, Double unusedQuantity, Double usedQuantityPrice) {
        double averagePrice = usedQuantityPrice;
        double aggregatedUsedQuantity = usedQuantity;
        double aggregatedUnusedQuantity = unusedQuantity;
        while (!coinsQueueFrom.isEmpty()) {
            Coins coinFrom = (Coins) coinsQueueFrom.peek();
            double coinFromPrice = coinFrom.getPrice();
            if(coinFromPrice == 0 && coinFrom.getCoinName().equals("BTC")) {
                coinFromPrice = Double.valueOf(bitcoinPriceMap.get(new SimpleDateFormat("yyyy-MM-dd").format(coinFrom.getPurchaseDate().toDate())));

                coinFrom.setPrice(coinFromPrice);
            }
            if (coinFrom.getBalance() > aggregatedUnusedQuantity) {
                coinFrom.setBalance(coinFrom.getBalance() - aggregatedUnusedQuantity);
                averagePrice = (averagePrice * aggregatedUsedQuantity + coinFromPrice * aggregatedUnusedQuantity)/(aggregatedUnusedQuantity+aggregatedUsedQuantity);
                break;
            } else {
                averagePrice = (averagePrice*aggregatedUsedQuantity + coinFromPrice*coinFrom.getBalance())/(aggregatedUsedQuantity + coinFrom.getBalance());
                aggregatedUsedQuantity += coinFrom.getBalance();
                aggregatedUnusedQuantity -= coinFrom.getBalance();
                coinsQueueFrom.remove();
            }
        }

        return averagePrice;
    }

    private void addNewCoinInQueue(LocalDate date, String coinName, Double quantityTo, Queue coinsQueueTo) {
        Coins coin = new Coins(date, coinName, quantityTo);
        coinsQueueTo.add(coin);

    }

    public ArrayList<TransactionCrypto> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<TransactionCrypto> transactions) {
        this.transactions = transactions;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Map<String, String> getBitcoinPriceMap() {
        return bitcoinPriceMap;
    }

    public void setBitcoinPriceMap(Map<String, String> bitcoinPriceMap) {
        this.bitcoinPriceMap = bitcoinPriceMap;
    }
}

package bittrex;

import org.joda.time.LocalDate;

/**
 * Created by Iliap on 1/4/2019.
 */
public class TransactionCrypto implements Comparable {
    private String sellingCoin;
    private Double sellingQuantity;
    private Double sellingPrice;
    private Double sellingPriceUSD;
    private LocalDate transactionDate;
    private Double sellingCoinBoughtPriceUSD;
    private LocalDate sellingCoinBoughtDate;
    private String buyingCoin;
    private Double buyingQuantity;
    private Double balanceFrom;
    private Double balanceTo;

    public String getSellingCoin() {
        return sellingCoin;
    }

    public void setSellingCoin(String sellingCoin) {
        this.sellingCoin = sellingCoin;
    }

    public Double getSellingQuantity() {
        return sellingQuantity;
    }

    public void setSellingQuantity(Double sellingQuantity) {
        this.sellingQuantity = sellingQuantity;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getSellingPriceUSD() {
        return sellingPriceUSD;
    }

    public void setSellingPriceUSD(Double sellingPriceUSD) {
        this.sellingPriceUSD = sellingPriceUSD;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getSellingCoinBoughtPriceUSD() {
        return sellingCoinBoughtPriceUSD;
    }

    public void setSellingCoinBoughtPriceUSD(Double sellingCoinBoughtPriceUSD) {
        this.sellingCoinBoughtPriceUSD = sellingCoinBoughtPriceUSD;
    }

    public LocalDate getSellingCoinBoughtDate() {
        return sellingCoinBoughtDate;
    }

    public void setSellingCoinBoughtDate(LocalDate sellingCoinBoughtDate) {
        this.sellingCoinBoughtDate = sellingCoinBoughtDate;
    }

    public String getBuyingCoin() {
        return buyingCoin;
    }

    public void setBuyingCoin(String buyingCoin) {
        this.buyingCoin = buyingCoin;
    }

    public Double getBuyingQuantity() {
        return buyingQuantity;
    }

    public void setBuyingQuantity(Double buyingQuantity) {
        this.buyingQuantity = buyingQuantity;
    }

    public Double getBalanceFrom() {
        return balanceFrom;
    }

    public void setBalanceFrom(Double balanceFrom) {
        this.balanceFrom = balanceFrom;
    }

    public Double getBalanceTo() {
        return balanceTo;
    }

    public void setBalanceTo(Double balanceTo) {
        this.balanceTo = balanceTo;
    }


    public int compareTo(Object o) {
        TransactionCrypto otherTransactionCrypto = (TransactionCrypto) o;
        if (otherTransactionCrypto.getTransactionDate() == null) {
            return -1;
        }
        if (this.getTransactionDate() == null) {
            return 1;
        }
        return this.transactionDate.compareTo(((TransactionCrypto) o).getTransactionDate());
        //return 0;
    }
}

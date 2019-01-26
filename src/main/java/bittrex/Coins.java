package bittrex;

import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Created by Iliap on 1/3/2019.
 */
public class Coins {
    private LocalDate purchaseDate;
    private String coinName;
    private Double balance = Double.valueOf(0);
    private double price;



    public Coins(LocalDate purchaseDate, String name, Double amount) {
        this.purchaseDate = purchaseDate;
        this.coinName = name;
        this.balance = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
}

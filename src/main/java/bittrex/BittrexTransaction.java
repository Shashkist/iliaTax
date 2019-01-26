package bittrex;

import interfaces.CryptoTransaction;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Iliap on 1/9/2019.
 */
public class BittrexTransaction implements CryptoTransaction{
    private String orderUuId;
    private String exchange;
    private String type;
    private String quantity;
    private String limit;
    private String commissionPaid;
    private String price;
    private String opened;
    private String closed;

    public BittrexTransaction(String orderUuId, String exchange, String type, String quantity, String limit, String commissionPaid, String price, String opened, String closed) {
        this.orderUuId = orderUuId;
        this.exchange = exchange;
        this.type = type;
        this.quantity = quantity;
        this.limit = limit;
        this.commissionPaid = commissionPaid;
        this.price = price;
        this.opened = opened;
        this.closed = closed;
    }

    public String getOrderUuId() {
        return orderUuId;
    }

    public void setOrderUuId(String orderUuId) {
        this.orderUuId = orderUuId;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getCommissionPaid() {
        return commissionPaid;
    }

    public void setCommissionPaid(String commissionPaid) {
        this.commissionPaid = commissionPaid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    @Override
    public String toString() {
        return orderUuId + exchange + type + quantity + limit + commissionPaid + price + opened + closed;
    }


    public int compareTo(Object o) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        if (o == null) {
            return 1;
        }
        BittrexTransaction other = (BittrexTransaction) o;
        if (other.getClosed() == null) {
            return 1;
        }
        try {
            Date otherDate = df.parse(other.getClosed());
            Date thisDate = df.parse(getClosed());
            return thisDate.compareTo(otherDate);
        } catch (ParseException e) {
            return 1;
        }
    }
}

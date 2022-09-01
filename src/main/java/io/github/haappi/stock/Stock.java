package io.github.haappi.stock;

import java.util.Timer;
import java.util.TimerTask;

import static io.github.haappi.stock.Utils.getRandomInteger;
import static io.github.haappi.stock.Utils.getRandomPrice;

public class Stock {
    private final String name;
    private Double price;

    private void init() {
        setPrice();
    }

    public Stock(String name, Double price) {
        this.name = name;
        this.price = price;

        init();
    }

    public Stock(String name) {
        this.name = name;

        init();
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Double setPrice(Double price) {
        this.price = price;
        if (this.price < 0.00) {
            this.price = 0.00;
        }
        if (this.price > 60.00) {
            this.price = 60.00;
        }
        return this.getPrice();
    }

    public String toString() {
        return name + ": " + price;
    }

    private void setPrice() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setPrice(getPrice() + getRandomPrice(-4.0, 4.0));
            }
        }, 1000L * getRandomInteger(5, 10), 1000 * 15); // every 15 seconds
    }
}

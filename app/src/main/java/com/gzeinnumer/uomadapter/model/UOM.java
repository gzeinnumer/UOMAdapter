package com.gzeinnumer.uomadapter.model;

public class UOM {
    private final int position;
    private final String name;
    private final int price;
    private int lastData = 0;

    public UOM(int position, String name, int price) {
        this.position = position;
        this.name = name;
        this.price = price;
    }

    public UOM(int position, String name, int price, int lastData) {
        this.position = position;
        this.name = name;
        this.price = price;
        this.lastData = lastData;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getLastData() {
        return lastData;
    }

    public void setLastData(int lastData) {
        this.lastData = lastData;
    }
}

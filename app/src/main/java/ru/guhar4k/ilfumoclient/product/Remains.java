package ru.guhar4k.ilfumoclient.product;

public class Remains {
    private int productID;
    private Warehouse warehouse;
    private int remains;

    public Remains(int productID, Warehouse warehouse, int remains) {
        this.productID = productID;
        this.warehouse = warehouse;
        this.remains = remains;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public int getRemains() {
        return remains;
    }

    public int getProductID() {
        return productID;
    }
}

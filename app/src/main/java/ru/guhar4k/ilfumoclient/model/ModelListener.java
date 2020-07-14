package ru.guhar4k.ilfumoclient.model;

import android.graphics.Bitmap;

import java.util.List;

import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Remains;
import ru.guhar4k.ilfumoclient.product.Warehouse;

public interface ModelListener {
    void onProductFound(Product product);

    void onImageDownload(int productID, Bitmap image);

    void noImageForProduct(int productID);

    void availableProducts(boolean hasNextPage);

    void warehouseReceived(Warehouse warehouse);

    void warehouseListEnd();

    Warehouse getWarehouseByID(int id);

    void onRemainsReceived(List<Remains> remains);
    //void noImageForProduct(int parseInt);
}

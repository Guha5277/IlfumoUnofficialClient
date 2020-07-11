package ru.guhar4k.ilfumoclient.model;

import android.graphics.Bitmap;

import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Warehouse;

public interface ModelListener {
    void onProductFound(Product product);

    void onImageDownload(int productID, Bitmap image);

    void noImageForProduct(int productID);

    void availableProducts(boolean hasNextPage);

    void warehouseReceived(Warehouse warehouse);

    void warehouseListEnd();
    //void noImageForProduct(int parseInt);
}

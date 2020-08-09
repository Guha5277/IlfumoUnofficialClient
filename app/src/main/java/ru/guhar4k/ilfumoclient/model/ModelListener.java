package ru.guhar4k.ilfumoclient.model;

import android.graphics.Bitmap;

import java.util.List;

import ru.guhar4k.ilfumoclient.product.DailyOffer;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Remains;
import ru.guhar4k.ilfumoclient.product.Warehouse;

public interface ModelListener {
    void onProductReceived(Product product);

    void onImageDownload(int productID, Bitmap image);

    void noImageForProduct(int productID);

    void availableProducts(boolean hasNextPage);

    void warehouseReceived(Warehouse warehouse);

    void warehouseListEnd();

    Warehouse getWarehouseByID(int id);

    void onRemainsReceived(List<Remains> remains);

    void onDailyOfferReceived(DailyOffer dailyOffer);

    void onDailyOfferCategoryReceived(String offerName);
    //void noImageForProduct(int parseInt);
}

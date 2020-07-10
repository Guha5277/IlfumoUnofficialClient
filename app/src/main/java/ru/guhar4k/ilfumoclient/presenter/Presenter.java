package ru.guhar4k.ilfumoclient.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import ru.guhar4k.ilfumoclient.model.Model;
import ru.guhar4k.ilfumoclient.model.ModelListener;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.view.ViewListener;

public class Presenter implements ModelListener, ViewListener {
    PresenterListener.View viewListener;
    PresenterListener.Model modelListener;

    private boolean isAllProductsLoaded;
    private boolean readyToGetProduct;

    public Presenter(PresenterListener.View listener) {
        viewListener = listener;
        modelListener = new Model(this);
    }

    //UI evetns
    @Override
    public void onAppReady() {
        modelListener.onUIReady();
    }

    @Override
    public void onAppClosed() {
        modelListener.onAppClosed();
    }

    @Override
    public void onAppPaused() {
        modelListener.onAppPaused();
    }

    @Override
    public void getMoreProducts() {
        if (readyToGetProduct && !isAllProductsLoaded){
            readyToGetProduct = false;
            modelListener.getMoreProducts();
        }
    }

    @Override
    public void availableProducts(boolean hasNextPage) {
        isAllProductsLoaded = !hasNextPage;
        readyToGetProduct = true;
    }

    //Model events
    @Override
    public void onProductFound(Product product) {
        viewListener.onProductFound(product);
    }

    @Override
    public void onImageDownload(int productID, Bitmap image) {
        viewListener.onProductImageDownload(productID, image);
    }

    @Override
    public void noImageForProduct(int productID) {
        viewListener.noImageForProduct(productID);
    }
}

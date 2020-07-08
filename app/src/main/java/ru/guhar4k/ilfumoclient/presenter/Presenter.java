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

    public Presenter(PresenterListener.View listener) {
        viewListener = listener;
        modelListener = new Model(this);
    }

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


    //Model events
    @Override
    public void onProductFound(Product product) {
        viewListener.onProductFound(product);
    }

    @Override
    public void onImageDownload(int productID, Bitmap image) {
        viewListener.onProductImageDownload(productID, image);
    }
}

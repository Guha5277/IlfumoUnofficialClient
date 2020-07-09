package ru.guhar4k.ilfumoclient.view;

public interface ViewListener {
    void onAppReady();
    void onAppClosed();
    void onAppPaused();

    void getMoreProducts();
}

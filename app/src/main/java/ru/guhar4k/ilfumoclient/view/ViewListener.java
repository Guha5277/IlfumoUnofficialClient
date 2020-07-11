package ru.guhar4k.ilfumoclient.view;

import java.util.List;

public interface ViewListener {
    void onAppReady();
    void onAppClosed();
    void onAppPaused();
    void getMoreProducts();
    List<String> getCitiesList();
//    List<String> getWarehousesList();
}

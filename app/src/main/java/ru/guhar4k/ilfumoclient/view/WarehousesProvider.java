package ru.guhar4k.ilfumoclient.view;

import java.util.List;

public interface WarehousesProvider {
    List<String> getWarehouses(String selectedItem);
}

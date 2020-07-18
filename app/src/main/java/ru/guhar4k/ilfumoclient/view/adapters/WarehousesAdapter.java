package ru.guhar4k.ilfumoclient.view.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.util.List;

public class WarehousesAdapter extends ArrayAdapter<String> {
    WarehousesProvider listener;
    private Spinner spinner;
    public WarehousesAdapter(@NonNull Context context, int resource, WarehousesProvider listener, Spinner spinner) {
        super(context, resource);
        this.listener = listener;
        this.spinner = spinner;
    }

    public void onParentAdapterChanged(String selectedItem){
        List<String> list = listener.getWarehouses(selectedItem);
        if (list == null) {
            spinner.setEnabled(false);
            this.clear();
            notifyDataSetChanged();
        } else {
            spinner.setEnabled(true);
            updateList(list);
        }
    }

    private void updateList(List<String> list) {
        this.clear();
        this.addAll(list);
        notifyDataSetChanged();
    }
}

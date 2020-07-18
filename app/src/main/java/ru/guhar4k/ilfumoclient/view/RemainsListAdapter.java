package ru.guhar4k.ilfumoclient.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.product.Remains;

public class RemainsListAdapter extends BaseAdapter {
    private ArrayList<Remains> remainsList;

    public RemainsListAdapter() {
        remainsList = new ArrayList<>();
    }

    void add(Remains remains){
        remainsList.add(remains);
        notifyDataSetChanged();
    }

    void addAll(List<Remains> remains){
        remainsList.clear();
        remainsList.addAll(remains);
        notifyDataSetChanged();
    }

    void clear(){
        remainsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return remainsList.size();
    }

    @Override
    public Remains getItem(int position) {
        return remainsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remains_item_list, parent, false);
        Remains remains = getItem(position);
        String storeName = remains.getWarehouse().getAltName();
        TextView tvWarehouseName = view.findViewById(R.id.tv_warehouse_name);
        TextView tvRemains = view.findViewById(R.id.tv_remains);
        tvWarehouseName.setText(storeName);
        tvRemains.setText(String.valueOf(remains.getRemains()));
        return view;
    }
}

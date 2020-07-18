package ru.guhar4k.ilfumoclient.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.product.Remains;

public class RemainsListAdapter extends RecyclerView.Adapter<RemainsListAdapter.ViewHolder> {
    private ArrayList<Remains> remainsList = new ArrayList<>();

    public void addAll(List<Remains> remains) {
        remainsList.clear();
        remainsList.addAll(remains);
        notifyDataSetChanged();
    }

    public void clear() {
        remainsList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.remains_item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return remainsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWarehouseName;
        TextView tvRemains;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWarehouseName = itemView.findViewById(R.id.tv_warehouse_name);
            tvRemains = itemView.findViewById(R.id.tv_remains);
        }

        public void bind(int position) {
            Remains remains = remainsList.get(position);
            String storeName = remains.getWarehouse().getAltName();
            tvWarehouseName.setText(storeName);
            tvRemains.setText(String.valueOf(remains.getRemains()));
        }
    }
}

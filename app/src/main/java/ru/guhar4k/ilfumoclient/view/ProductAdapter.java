package ru.guhar4k.ilfumoclient.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.product.Product;

class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList = new ArrayList<>();

    public void addItem(Product product){
        productList.add(product);
        notifyItemChanged(productList.size() - 1);
        //notifyItemChanged(productList.size() - 1);
    }

    public void addImage(int productID, Bitmap image) {
        for (Product p : productList){
            if (productID == p.getId()) {
                productList.indexOf(p);
                int index = productList.indexOf(p);
                p.setImage(image);
                notifyItemChanged(index);
                break;
            }
        }
    }

    public void setItems(Collection<Product> products) {
        productList.addAll(products);
        notifyDataSetChanged();
    }

    public void clearItems() {
        productList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.product_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView tvProductName;
        TextView tvPrice;
        TextView tvVolume;
        TextView tvStrength;

        public ViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_item_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvVolume = itemView.findViewById(R.id.tv_volume);
            tvStrength = itemView.findViewById(R.id.tv_strength);
        }

        void bind(Product product){
            Bitmap productBitmap = product.getImage();
            if (productBitmap != null) productImage.setImageBitmap(productBitmap);
            tvProductName.setText(product.getName());
            tvPrice.setText(product.getPrice() + " рублей");
            tvStrength.setText(product.getStrength() + " мг/мл");
            tvVolume.setText(product.getVolume() + " мл");
        }
    }
}

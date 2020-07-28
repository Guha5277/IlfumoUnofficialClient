package ru.guhar4k.ilfumoclient.view.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.product.Product;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private static final String LOGTAG = "ProductListAdapter";
    private List<ProductItem> productItemList = new ArrayList<>();
    private OnClickListener listener;
    private Context context;

    public ProductListAdapter(OnClickListener listener) {
        this.listener = listener;
    }

    public void addItem(Product product) {
        productItemList.add(new ProductItem(product));
        notifyItemChanged(productItemList.size() - 1);
    }

    public void addImage(int productID, Bitmap image) {
        ProductItem item = findItemByProductID(productID);
        if (item == null) {
            Log.e(LOGTAG, "Failed to found a product by ID");
            return;
        }
        item.setImage(image);
        item.setImageStatus(ProductItem.HAVE_IMAGE);
        notifyItemChanged(item.getPosition());
    }

    public void noImageForProduct(int productID) {
        ProductItem item = findItemByProductID(productID);
        if (item == null) {
            Log.e(LOGTAG, "Failed to found a product by ID");
            return;
        }
        item.setImageStatus(ProductItem.NO_IMAGE);
        notifyItemChanged(item.getPosition());
    }

    private ProductItem findItemByProductID(int productID) {
        for (ProductItem p : productItemList) {
            if (productID == p.getProduct().getId()) return p;
        }
        return null;
    }

    public void clearItems() {
        productItemList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutID = R.layout.product_tem_search;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position, productItemList.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (productItemList != null) {
                listener.onClick(productItemList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return productItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView productImage;
        TextView tvProductName;
        TextView tvPrice;
        TextView tvVolume;
        TextView tvStrength;

        public ViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_item_image);
            tvProductName = itemView.findViewById(R.id.iv_name_background);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvVolume = itemView.findViewById(R.id.tv_volume);
            tvStrength = itemView.findViewById(R.id.tv_strength);
        }

        void bind(int position, ProductItem item) {
            item.setPosition(position);
            int imageStatus = item.getImageStatus();
            if (imageStatus == ProductItem.HAVE_IMAGE) {
                productImage.setImageBitmap(item.getImage());
            } else if (imageStatus == ProductItem.NO_IMAGE) {
                productImage.setImageResource(R.drawable.ic_no_image);
            } else {
                productImage.setImageResource(R.drawable.image_downloading);
            }
            tvProductName.setText(item.getProduct().getName());
            String price = item.getProduct().getPrice() +  context.getString(R.string.rubble_sign);
            String strength = item.getProduct().getStrength() + context.getString(R.string.mg_ml);
            String volume = item.getProduct().getVolume() + context.getString(R.string.ml);
            tvPrice.setText(price);
            tvStrength.setText(strength);
            tvVolume.setText(volume);
        }
    }

    public interface OnClickListener {
        void onClick(ProductItem item);
    }
}

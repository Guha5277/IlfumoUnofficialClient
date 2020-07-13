package ru.guhar4k.ilfumoclient.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.product.Product;

public class ProductFragment extends Fragment {
    private ProductItem productItem;
    private Product product;
    private ImageView productImage;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvVolume;
    private TextView tvStrength;

    public ProductFragment() {}

    public static ProductFragment newInstance(ProductItem item) {
        ProductFragment fragment = new ProductFragment();
        fragment.setProductItem(item);
        return fragment;
    }

    private void setProductItem(ProductItem item) {
        this.productItem = item;
        setProduct(productItem.getProduct());
    }

    private void setProduct(Product product){
        this.product = product;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        productImage = view.findViewById(R.id.iv_product);
        tvName = view.findViewById(R.id.tv_name);
        tvPrice = view.findViewById(R.id.tv_price);
        tvStrength = view.findViewById(R.id.tv_strength);
        tvVolume = view.findViewById(R.id.tv_volume);
        initializeView();
        return view;
    }

    private void initializeView() {
        if (productItem.getImageStatus() == ProductItem.HAVE_IMAGE){
            productImage.setImageBitmap(productItem.getImage());
        }
        tvName.setText(product.getName());
        tvPrice.setText(product.getPrice() + " ₽");
        tvVolume.setText(product.getVolume() + " мл");
        tvStrength.setText(product.getStrength() + " мг/мл");
    }
}
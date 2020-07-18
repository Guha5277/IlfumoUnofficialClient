package ru.guhar4k.ilfumoclient.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Remains;

public class ProductFragment extends Fragment {
    private static RemainsListAdapter adapter = new RemainsListAdapter();
    private ProductItem productItem;
    private Product product;
    private ImageView productImage;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvVolume;
    private TextView tvStrength;
    private ViewListener listener;
    private ProgressBar progressBar;
    private RecyclerView rvRemains;

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
        rvRemains = view.findViewById(R.id.rv_remains);
        rvRemains.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRemains.setAdapter(adapter);
        progressBar = view.findViewById(R.id.remains_progress);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        initializeView();
        return view;
    }

    private void initializeView() {
        listener.getRemainsForProduct(product.getId());
        if (productItem.getImageStatus() == ProductItem.HAVE_IMAGE){
            productImage.setImageBitmap(productItem.getImage());
        }
        tvName.setText(product.getName());
        tvPrice.setText(product.getPrice() + " ₽");
        tvVolume.setText(product.getVolume() + " мл");
        tvStrength.setText(product.getStrength() + " мг/мл");
    }

    public void setListener(ViewListener listener) {
        this.listener = listener;
    }

    public void onRemainsReceived(List<Remains> remains) {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        adapter.addAll(remains);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.clear();
    }


}
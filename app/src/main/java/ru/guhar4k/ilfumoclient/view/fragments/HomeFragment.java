package ru.guhar4k.ilfumoclient.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.product.DailyOffer;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.view.ProductItemSelected;
import ru.guhar4k.ilfumoclient.view.ViewListener;
import ru.guhar4k.ilfumoclient.view.adapters.ProductItem;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private final String LOGTAG = "HomeFragment";
    private ViewListener viewListener;
    private ProductItemSelected clickListener;
    private LayoutInflater inflater;
    private LinearLayout mainLayout;
    private OfferItemClickListener offerItemClickListener = new OfferItemClickListener();

    public HomeFragment(ViewListener viewListener, ProductItemSelected clickListener) {
        this.viewListener = viewListener;
        this.clickListener = clickListener;
//        dailyOfferMap = new HashMap<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGTAG, "On create fragment home");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOGTAG, "On create view fragment home");
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mainLayout = view.findViewById(R.id.main_layout);
        initBottomBar(view);
        return view;
    }

    private void initBottomBar(View view) {
        view.findViewById(R.id.ib_home).setOnClickListener(this);
        view.findViewById(R.id.ib_search).setOnClickListener(this);
        view.findViewById(R.id.ib_favorite).setOnClickListener(this);
    }

    //Bottom bar click listener
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_home:
                Log.i(LOGTAG, "HOME");
                break;
            case R.id.ib_search:
                Log.i(LOGTAG, "SEARCH");
                viewListener.onSearchClicked(this);
                break;
            case R.id.ib_favorite:
                Log.i(LOGTAG, "FAVORITE");
                viewListener.onFavoriteClicked(this);
                break;
        }
    }

    public void addDailyOffer(DailyOffer dailyOffer) {
        Log.i(LOGTAG, "Daily offer category received");

        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.category_home, null);
        ((TextView)constraintLayout.findViewById(R.id.tv_group_label)).setText(dailyOffer.getName());
        LinearLayout groupLayout = constraintLayout.findViewById(R.id.linear_layout_group);

        for (Product p : dailyOffer.getProductsList()){
            View itemView = getLayoutInflater().inflate(R.layout.product_item_home, null);
            CircleImageView circleImageView = itemView.findViewById(R.id.iv_product_image);
            circleImageView.setOnClickListener(offerItemClickListener);
            offerItemClickListener.addRecord(circleImageView, p);
            if (p.getImage() != null){
                circleImageView.setImageBitmap(p.getImage());
            }
            groupLayout.addView(itemView);
        }

        getView().findViewById(R.id.pb_home_fragment).setVisibility(ProgressBar.INVISIBLE);

        mainLayout.addView(constraintLayout);
    }

    class OfferItemClickListener implements View.OnClickListener {
        private Map<CircleImageView, Product> container = new HashMap<>();

        @Override
        public void onClick(View v) {
            Log.i(LOGTAG, "Clicked on HomeFragment item with product id: " + container.get(v).getId());
            Product product = container.get(v);
            ProductItem productItem = new ProductItem(product);
            if (product.isImageSet()) {
                productItem.setImage(product.getImage());
                productItem.setImageStatus(ProductItem.HAVE_IMAGE);
            } else {
                productItem.setImageStatus(ProductItem.NO_IMAGE);
            }

            clickListener.onClick(productItem);
        }

        void addRecord(CircleImageView imageView, Product product){
            container.put(imageView, product);
        }
    }
}

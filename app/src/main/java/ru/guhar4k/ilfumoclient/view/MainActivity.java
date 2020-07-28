package ru.guhar4k.ilfumoclient.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.List;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.presenter.Presenter;
import ru.guhar4k.ilfumoclient.presenter.PresenterListener;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Remains;
import ru.guhar4k.ilfumoclient.view.adapters.ProductItem;
import ru.guhar4k.ilfumoclient.view.fragments.ProductFragment;
import ru.guhar4k.ilfumoclient.view.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements PresenterListener.View, SearchFragment.OnClickListener, View.OnClickListener {
    private static final String LOGTAG = "MainActivity";
    private ViewListener listener;
    private SearchFragment searchFragment;
    private ProductFragment productFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchFragment = SearchFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, searchFragment).commit();

        //TODO extract to method
//        ImageView ibHome = findViewById(R.id.ib_home);
//        ibHome.setOnClickListener(this);
//        ImageView ibSearch = findViewById(R.id.ib_search);
//        ibSearch.setOnClickListener(this);
//        ImageView ibFav = findViewById(R.id.ib_favorite);
//        ibFav.setOnClickListener(this);

        listener = new Presenter(this);
    }

    //Listener for SearchFragment
    @Override
    public void onClick(ProductItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in, R.anim.parent_our, R.anim.parent_in, R.anim.fragment_out);
        productFragment = ProductFragment.newInstance(item);
        productFragment.setListener(listener);
        ft.hide(searchFragment);
        ft.add(R.id.fl_content, productFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    //Listener for bottom menu click
    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.ib_home:
//                Log.i(LOGTAG, "IB HOME");
//                break;
//            case R.id.ib_search:
//                Log.i(LOGTAG, "IB SEARCH");
//                break;
//            case R.id.ib_favorite:
//                Log.i(LOGTAG, "IB FAV");
//                break;
//        }
    }

    //Presenter events
    @Override
    public void onProductFound(Product product) {
        runOnUiThread(() -> searchFragment.onProductFound(product));
    }

    @Override
    public void onProductImageDownload(int productID, Bitmap image) {
        runOnUiThread(() -> searchFragment.onImageFound(productID, image));
    }

    @Override
    public void noImageForProduct(int productID) {
        runOnUiThread(() -> searchFragment.noImageForProduct(productID));
    }

    @Override
    public void onWarehousesInfoReceived() {
        runOnUiThread(() -> {
            searchFragment.setListener(listener);
            searchFragment.initFilterDialog(this);
            searchFragment.initSortDialog(this);
        });
    }

    @Override
    public void onRemainsReceived(List<Remains> remains) {
        runOnUiThread(() -> {
            if (productFragment != null) productFragment.onRemainsReceived(remains);
        });
    }

    //Lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        listener.onAppReady();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listener.onAppPaused();
        Log.i(LOGTAG, "Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listener.onAppClosed();
        Log.i(LOGTAG, "Destroy");
    }


}
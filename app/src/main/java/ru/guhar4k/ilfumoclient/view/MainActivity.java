package ru.guhar4k.ilfumoclient.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.presenter.Presenter;
import ru.guhar4k.ilfumoclient.presenter.PresenterListener;
import ru.guhar4k.ilfumoclient.product.DailyOffer;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Remains;
import ru.guhar4k.ilfumoclient.view.adapters.ProductItem;
import ru.guhar4k.ilfumoclient.view.fragments.HomeFragment;
import ru.guhar4k.ilfumoclient.view.fragments.ProductFragment;
import ru.guhar4k.ilfumoclient.view.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements PresenterListener.View, ProductItemSelected, View.OnClickListener {
    private static final String LOGTAG = "MainActivity";
    private ViewListener listener;
    private SearchFragment searchFragment;
    private ProductFragment productFragment;
    private HomeFragment homeFragment;
    private HomeFragment favoriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listener = new Presenter(this);

        homeFragment = new HomeFragment(listener, this);
        searchFragment = SearchFragment.newInstance(this);
        searchFragment.setListener(listener);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, searchFragment);
        ft.hide(searchFragment);
        ft.add(R.id.fl_content, homeFragment);
        ft.addToBackStack(null);
        ft.commit();

        Log.i(LOGTAG, "ON CREATE");
    }

    //ProductItemSelected listener
    @Override
    public void onClick(ProductItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in, R.anim.parent_our, R.anim.parent_in, R.anim.fragment_out);
        productFragment = ProductFragment.newInstance(item);
        productFragment.setListener(listener);
        //lastAddedFragment = productFragment;
        ft.hide(searchFragment);
        ft.hide(homeFragment);
        //ft.hide(favoriteFragment);
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

    @Override
    public void onHomeClicked(Fragment fragment) {
        Log.i(LOGTAG, "On home clicked");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.in_left_to_right, R.anim.out_left_to_right, R.anim.in_right_to_left, R.anim.out_right_to_left);

        ft.hide(fragment);
        if (homeFragment != null && homeFragment.isAdded()){
            ft.show(homeFragment);
        } else {
            ft.add(R.id.fl_content, homeFragment);
//            ft.replace(R.id.fl_content, homeFragment);
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onSearchClicked(Fragment fragment) {
        Log.i(LOGTAG, "On search clicked");
        //getSupportFragmentManager().findFragmentById(R.id.fl_content);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        if (fragment == favoriteFragment){
//            ft.setCustomAnimations(R.anim.in_left_to_right, R.anim.out_left_to_right, R.anim.in_right_to_left, R.anim.out_right_to_left);
//        } else {
//            ft.setCustomAnimations(R.anim.in_right_to_left, R.anim.out_right_to_left, R.anim.in_left_to_right, R.anim.out_left_to_right);
//        }

        ft.hide(fragment);
        if (homeFragment != null && searchFragment.isAdded()){
            ft.show(searchFragment);
        } else {
            ft.add(R.id.fl_content, searchFragment);
//            ft.replace(R.id.fl_content, searchFragment);
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onFavoriteClicked(Fragment fragment) {

    }

    @Override
    public void addDailyOffer(DailyOffer dailyOffer) {
        runOnUiThread(() -> homeFragment.addDailyOffer(dailyOffer));
    }

    //Lifecycle
    @Override
    protected void onStart() {
        Log.i(LOGTAG, "ON START");
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
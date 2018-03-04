package ba.sum.sum.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterListShopCategoryImg;
import ba.sum.sum.models.Image;
import ba.sum.sum.models.ShopCategory;
import ba.sum.sum.utils.DataGenerator;
import ba.sum.sum.utils.Tools;

public class FragmentFakultet extends Fragment {


    public FragmentFakultet() {
    }

    public static FragmentFakultet newInstance() {
        return new FragmentFakultet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_shopping_category_image, container, false);

        initComponent(root);

        return root;
    }

    private void initComponent(final View root) {

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        List<ShopCategory> items = DataGenerator.getShoppingCategory(getActivity());

        //set data and list adapter
        AdapterListShopCategoryImg mAdapter = new AdapterListShopCategoryImg(getActivity(), items);
        recyclerView.setAdapter(mAdapter);
    }

}
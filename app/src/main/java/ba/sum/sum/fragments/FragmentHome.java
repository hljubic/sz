package ba.sum.sum.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterPager;

public class FragmentHome extends Fragment {

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initComponent(root);

        return root;
    }

    private void initComponent(View root) {
        ViewPager view_pager = root.findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        TabLayout tab_layout = root.findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterPager adapter = new AdapterPager(getChildFragmentManager());
        adapter.addFragment(FragmentFaculties.newInstance(), "Naslovnica");
        adapter.addFragment(FragmentNews.newInstance(2), "Novosti");
        adapter.addFragment(FragmentNews.newInstance(3), "Društvene mreže");
        viewPager.setAdapter(adapter);
    }
}
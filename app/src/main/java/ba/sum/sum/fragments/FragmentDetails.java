package ba.sum.sum.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterPager;

public class FragmentDetails extends Fragment {

    public static FragmentDetails newInstance() {
        return new FragmentDetails();
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
        adapter.addFragment(FragmentAbout.newInstance(), "O nama");
        adapter.addFragment(FragmentExpand.newInstance(), "Studiji");
        adapter.addFragment(FragmentAbout.newInstance(), "Dokumenti");
        viewPager.setAdapter(adapter);
    }
}
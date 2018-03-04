package ba.sum.sum.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterFaculties;
import ba.sum.sum.models.Category;
import ba.sum.sum.utils.DataGenerator;

public class FragmentFaculties extends Fragment {

    public static FragmentFaculties newInstance() {
        return new FragmentFaculties();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_faculties, container, false);

        initComponent(root);

        return root;
    }

    private void initComponent(final View root) {

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        List<Category> items = DataGenerator.getShoppingCategory(getActivity());

        //set data and list adapter
        AdapterFaculties mAdapter = new AdapterFaculties(getActivity(), items);
        recyclerView.setAdapter(mAdapter);
    }

}
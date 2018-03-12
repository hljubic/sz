package ba.sum.sum.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterExpand;
import ba.sum.sum.models.Institution;
import ba.sum.sum.models.Social;
import ba.sum.sum.utils.DataGenerator;
import ba.sum.sum.utils.LineItemDecoration;

public class FragmentExpand extends Fragment {

    public static FragmentExpand newInstance() {
        return new FragmentExpand();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_expand, container, false);

        initComponent(root);

        return root;
    }

    private void initComponent(final View root) {
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new LineItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        Institution institution= Institution.findById(Institution.class, getActivity().getIntent().getExtras().getString("institution_id"));
        List<Institution> items = institution.getChildren();
        //set data and list adapter
        AdapterExpand mAdapter = new AdapterExpand(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterExpand.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Institution obj, int position) {
                // Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
package ba.sum.sum.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterExpand;
import ba.sum.sum.adapters.AdapterExpandDocument;
import ba.sum.sum.models.Document;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.LineItemDecoration;

public class FragmentExpand extends Fragment {

    public static final String ARG_STUDIES = "arg_studies";

    public static FragmentExpand newInstance(boolean useStudies) {
        FragmentExpand fragment = new FragmentExpand();
        Bundle args = new Bundle();
        args.putBoolean(ARG_STUDIES, useStudies);
        fragment.setArguments(args);
        return fragment;
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

        Institution institution = Institution.findById(Institution.class, getActivity().getIntent().getExtras().getString("institution_id"));
        List<Institution> items = institution.getChildren();
        List<Document> documents = institution.getDocuments();

        if (getArguments().getBoolean(ARG_STUDIES)) {
            AdapterExpand mAdapter = new AdapterExpand(getActivity(), items);
            recyclerView.setAdapter(mAdapter);

        } else {
            AdapterExpandDocument mAdapterDocument = new AdapterExpandDocument(getActivity(), documents);
            recyclerView.setAdapter(mAdapterDocument);
        }

    }
}
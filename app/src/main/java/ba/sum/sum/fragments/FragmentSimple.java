package ba.sum.sum.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterDocument;
import ba.sum.sum.adapters.AdapterListSectioned;
import ba.sum.sum.models.Institution;

public class FragmentSimple extends Fragment {

    public static final String ARG_STUDIES = "arg_studies";

    private Institution institution;

    public static FragmentSimple newInstance(boolean useStudies) {
        FragmentSimple fragment = new FragmentSimple();
        Bundle args = new Bundle();
        args.putBoolean(ARG_STUDIES, useStudies);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        institution = Institution.findParentOrChildById(getActivity().getIntent().getExtras().getString("institution_id"));

        if (!getArguments().getBoolean(ARG_STUDIES)) {
            if (institution.getDocuments() == null || institution.getDocuments().size() == 0) {
                return inflater.inflate(R.layout.fragment_no_documents, container, false);
            }
        }

        View root = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        initComponent(root);

        return root;
    }

    private void initComponent(final View root) {
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        if (getArguments().getBoolean(ARG_STUDIES)) {
            try {
                AdapterListSectioned mAdapter = new AdapterListSectioned(getActivity(), institution.getChildrenSectioned());
                recyclerView.setAdapter(mAdapter);
            } catch (Exception e) {
                AdapterListSectioned mAdapter = new AdapterListSectioned(getActivity(), institution.getChildren());
                recyclerView.setAdapter(mAdapter);
            }
        } else {
            AdapterDocument mAdapter = new AdapterDocument(getActivity(), institution.getDocuments());
            recyclerView.setAdapter(mAdapter);
        }
    }
}
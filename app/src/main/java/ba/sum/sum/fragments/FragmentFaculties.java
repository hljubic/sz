package ba.sum.sum.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import ba.hljubic.jsonorm.JsonTable;
import ba.sum.sum.utils.Constants;
import ba.sum.sum.DetailsActivity;
import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterFaculties;
import ba.sum.sum.models.Institution;

public class FragmentFaculties extends Fragment {

    private Gson gson;
    private ArrayList<Institution> institutions;

    public static FragmentFaculties newInstance() {
        return new FragmentFaculties();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_faculties, container, false);

        gson = new Gson();
        institutions = new ArrayList<>();

        initComponent(root);

        return root;
    }

    private void initComponent(final View root) {
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        final AdapterFaculties mAdapter = new AdapterFaculties(getActivity(), institutions);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterFaculties.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Institution institution, int position) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("institution_id", institution.getId());
                intent.putExtra("institution_name", institution.getName());
                startActivity(intent);
            }
        });

        StringRequest sastavniceRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "sastavnice/vazne", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Institution> list = gson.fromJson(response, new TypeToken<List<Institution>>() {
                }.getType());

                Institution.saveAllAsync(Institution.class, list);

                institutions.clear();

                for (Institution institution : list) {
                    // Toast.makeText(getActivity(), institution.getInstitutionId() + "---", Toast.LENGTH_SHORT).show();
                    if (institution.getInstitutionId() == 1) {
                        institutions.add(institution);
                    }
                }

                mAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), R.string.cant_connect, Toast.LENGTH_LONG).show();

                institutions.clear();
                institutions.addAll(Institution.listAll(Institution.class));

                mAdapter.notifyDataSetChanged();
            }
        });

        Volley.newRequestQueue(getActivity()).add(sastavniceRequest);
    }

}
package ba.sum.sum.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.DetailsActivity;
import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterFaculties;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.App;
import ba.sum.sum.utils.Constants;

public class FragmentFaculties extends Fragment {

    private Gson gson;
    private List<Institution> institutions;
    private AdapterFaculties mAdapter;
    private SwipeRefreshLayout swipe_refresh;

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

        List<Institution> list = App.get().getInstitutions();

        for (Institution institution : list) {
            if (institution.getInstitutionId() == 1) {
                institutions.add(institution);
            }
        }

        mAdapter = new AdapterFaculties(getActivity(), institutions);
        recyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new AdapterFaculties.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Institution institution, int position) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("institution_id", institution.getId());
                startActivity(intent);
            }
        });

        if (institutions.size() == 0) {
            getData();
        }

        swipe_refresh = root.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe_refresh.setRefreshing(false);

                        getData();
                        onPageFinished();
                    }
                }, 1000);
            }

        });

    }

    public void getData() {
        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_API_URL + "sastavnice/vazne", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Institution> list = gson.fromJson(response, new TypeToken<List<Institution>>() {
                }.getType());

                Institution.saveAllAsync(Institution.class, list);

                institutions.clear();

                for (Institution institution : list) {
                    if (institution.getInstitutionId() == 1) {
                        institutions.add(institution);
                    }
                }

                mAdapter.notifyDataSetChanged();

                App.get().getInstitutions().clear();
                App.get().getInstitutions().addAll(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), R.string.cant_connect, Toast.LENGTH_LONG).show();

                institutions.clear();

                List<Institution> list = Institution.listAll(Institution.class);

                for (Institution institution : list) {
                    if (institution.getInstitutionId() == 1) {
                        institutions.add(institution);
                    }
                }

                mAdapter.notifyDataSetChanged();

                if (institutions.size() == 0) {
                    showErrorDialog();
                } else {
                    App.get().getInstitutions().clear();
                    App.get().getInstitutions().addAll(institutions);
                }
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        /*
        queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
        */

        queue.add(request);
    }

    private void showErrorDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void onPageFinished() {
        swipe_refresh.setRefreshing(false);
    }
}
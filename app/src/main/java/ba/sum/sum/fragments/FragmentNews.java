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

import ba.sum.sum.PostActivity;
import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterNews;
import ba.sum.sum.models.Post;
import ba.sum.sum.utils.Constants;

/**
 * Created by hrvoje on 04/03/2018.
 */
public class FragmentNews extends Fragment {
    private static final String ARG_INSTITUTION_ID = "institution_id";

    private ArrayList<Post> posts;
    private AdapterNews mAdapter;

    public static FragmentNews newInstance(String institutionId) {
        FragmentNews fragment = new FragmentNews();
        Bundle args = new Bundle();
        args.putString(ARG_INSTITUTION_ID, institutionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        posts = new ArrayList<>();

        mAdapter = new AdapterNews(getActivity(), posts);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterNews.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Post post, int position) {

                Intent intent = new Intent(getContext(), PostActivity.class);

                intent.putExtra("user_id", post.getId());

                startActivity(intent);
            }
        });


        String institutionId = getArguments().getString(ARG_INSTITUTION_ID);

        getData(institutionId == null ? "" : "/" + institutionId);

        return rootView;
    }

    public void getData(String filterId) {

        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_API_URL + "novosti" + filterId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Post> list = new Gson().fromJson(response, new TypeToken<List<Post>>() {
                }.getType());

                Post.saveAllAsync(Post.class, list);

                posts.clear();
                posts.addAll(list);

                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), R.string.cant_connect, Toast.LENGTH_LONG).show();

                posts.addAll(Post.listAll(Post.class));
                mAdapter.notifyDataSetChanged();
            }
        });

        Volley.newRequestQueue(getActivity()).add(request);
    }
}
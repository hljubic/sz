package ba.sum.sum.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ba.sum.sum.R;

/**
 * Created by hrvoje on 04/03/2018.
 */
public class FragmentNews extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentNews() {
    }

    public static FragmentNews newInstance(int sectionNumber) {
        FragmentNews fragment = new FragmentNews();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        return rootView;
    }
}
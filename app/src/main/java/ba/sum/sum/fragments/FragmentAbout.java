package ba.sum.sum.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterImageSlider;
import ba.sum.sum.models.Image;
import ba.sum.sum.models.Institution;

public class FragmentAbout extends Fragment {

    private static final String ARG_INSTITUTION_ID = "institution_id";
    private View parent_view;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    private Institution institution;

    public FragmentAbout() {
    }

    public static FragmentAbout newInstance(String institutionId) {
        FragmentAbout fragment = new FragmentAbout();
        Bundle args = new Bundle();
        args.putString(ARG_INSTITUTION_ID, institutionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        institution = Institution.findParentOrChildById(String.valueOf(getArguments().getString(ARG_INSTITUTION_ID)));

        initComponent(root);

        return root;
    }

    private void initComponent(final View root) {
        HtmlTextView content = root.findViewById(R.id.tv_content);
        content.setHtml(institution.getContent());

        String[] array_image_place = new String[]{institution.getLogo()};
        String[] array_title_place = new String[]{institution.getName()};
        String[] array_subtitle_place = new String[]{institution.getAddress()};

        layout_dots = root.findViewById(R.id.layout_dots);
        viewPager = root.findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(getActivity(), new ArrayList<Image>());

        final List<Image> items = new ArrayList<>();

        final String[] arr_img = new String[institution.getImages().size()];

        for (int i = 0; i < institution.getImages().size(); i++) {
            arr_img[i] = institution.getImages().get(i).getFile();
        }

        if (arr_img.length != 0) {
            for (int i = 0; i < arr_img.length; i++) {
                Image obj = new Image();
                obj.image = arr_img[i];
                obj.name = institution.getName();
                obj.brief = institution.getAddress();
                items.add(obj);
            }
        } else {
            for (int i = 0; i < array_image_place.length; i++) {
                Image obj = new Image();
                obj.image = array_image_place[i];
                obj.name = array_title_place[i];
                obj.brief = array_subtitle_place[i];
                items.add(obj);
            }
        }


        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        ((TextView) root.findViewById(R.id.title)).setText(items.get(0).name);
        ((TextView) root.findViewById(R.id.brief)).setText(items.get(0).brief);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                ((TextView) root.findViewById(R.id.title)).setText(items.get(pos).name);
                ((TextView) root.findViewById(R.id.brief)).setText(items.get(pos).brief);
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
        }
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }

}
package ba.sum.sum.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.models.Institution;

/**
 * Created by Darko on 20.3.2018..
 */

public class AdapterSpinner extends ArrayAdapter<Institution> {
    private Context ctx;
    private List<Institution> items;
    private OnItemClickListener onItemClickListener;


    public AdapterSpinner(@NonNull Context context, List<Institution> items) {
        super(context, R.layout.spinner);
        this.ctx = context;
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Nullable
    @Override
    public Institution getItem(int position) {
        return this.items.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.spinner, parent, false);
        }
        TextView naziv = (TextView) convertView.findViewById(R.id.listTextViewSpinner);

        if (items.get(position).name != null)
            naziv.setText(items.get(position).getName());


        return convertView;


    }


    @Override
    public View getDropDownView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.spinner, parent, false);
        }
        final TextView naziv = (TextView) convertView.findViewById(R.id.listTextViewSpinner);

        if (items.get(position).name != null)
            naziv.setText(items.get(position).getName());

        return convertView;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Institution institution, int position);
    }
}

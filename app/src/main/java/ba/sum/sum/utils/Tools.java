package ba.sum.sum.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.adapters.AdapterSpinner;
import ba.sum.sum.models.Institution;

public class Tools {

    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }

    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static boolean toggleArrow(boolean show, View view) {
        return toggleArrow(show, view, true);
    }

    public static boolean toggleArrow(boolean show, View view, boolean delay) {
        if (show) {
            view.animate().setDuration(delay ? 200 : 0).rotation(180);
            return true;
        } else {
            view.animate().setDuration(delay ? 200 : 0).rotation(0);
            return false;
        }
    }

    public static String stripHtml(String html) {
        if (html == null) {
            return "";
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return String.valueOf(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            return String.valueOf(Html.fromHtml(html));
        }
    }

    public static void showContactDialog(final Context context, Institution institution) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_contact);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final AppCompatSpinner listFaculties = dialog.findViewById(R.id.con_faculties);
        final EditText naslov = dialog.findViewById(R.id.con_naslov);
        final AppCompatEditText sadrzaj = dialog.findViewById(R.id.con_sadrzaj);

        final List<Institution> institutions = App.get().getInstitutions();
        AdapterSpinner adapterSpinner = new AdapterSpinner(context, institutions);
        adapterSpinner.setDropDownViewResource(R.layout.spinner);
        listFaculties.setAdapter(adapterSpinner);

        int position = 0;

        if (institution != null) {
            for (Institution i : institutions) {
                if (i.getId().equals(institution.getId())) {
                    break;
                }
                position++;
            }
        }

        listFaculties.setSelection(position);

        (dialog.findViewById(R.id.bt_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Institution selectedInstitution = institutions.get(listFaculties.getSelectedItemPosition());
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", selectedInstitution.getEmail(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, naslov.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, sadrzaj.getText().toString());
                context.startActivity(Intent.createChooser(emailIntent, ""));
            }
        });

        dialog.show();
    }

}

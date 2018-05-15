package com.muoipt.ffm.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muoipt.ffm.R;
import com.muoipt.ffm.eventmodel.MainEventObject;
import com.muoipt.ffm.eventmodel.MainEventString;
import com.muoipt.ffm.eventmodel.MainSummaryFragmentEventInteger;
import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.ImportantCatListItem;
import com.muoipt.ffm.model.ReportDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by XalenMy on 3/5/2018.
 */

public class CatListviewAdapter extends ArrayAdapter<CategoryDetail> {

    private Activity context;
    private int layout;
    private List<CategoryDetail> list;
    private CategoryDetail selectedCat = null;

    public CatListviewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CategoryDetail> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.layout = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(layout, parent, false);

        TextView txtCatName = (TextView) row.findViewById(R.id.txtCatNameItem);
        TextView txtCatNo = (TextView) row.findViewById(R.id.txtCatNumberItem);
        RelativeLayout container = (RelativeLayout) row.findViewById(R.id.layout_cat_container);

        CategoryDetail r = list.get(position);
        txtCatName.setText(r.getCatName());
        int reportNumber = AppConfig.getCountReportEachCat(r);
        txtCatNo.setText(reportNumber+"");

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MainEventObject(ComonUtils.EDIT_CAT_MSG, list.get(position)));
            }
        });

        return row;
    }
}

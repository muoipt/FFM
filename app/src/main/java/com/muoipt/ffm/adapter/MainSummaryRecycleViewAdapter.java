package com.muoipt.ffm.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muoipt.ffm.R;
import com.muoipt.ffm.eventmodel.MainSummaryFragmentEventInteger;
import com.muoipt.ffm.model.MainSummaryReport;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class MainSummaryRecycleViewAdapter extends RecyclerView.Adapter<MainSummaryRecycleViewHolder> {
    List<MainSummaryReport> list = Collections.emptyList();
    Context context;

    public MainSummaryRecycleViewAdapter() {
    }

    public MainSummaryRecycleViewAdapter(List<MainSummaryReport> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MainSummaryRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_summary_recycle_view, parent, false);
        MainSummaryRecycleViewHolder holder = new MainSummaryRecycleViewHolder(v);

        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final MainSummaryRecycleViewHolder holder, final int position) {
        holder.txtCategory.setText(list.get(position).getCategory());
        holder.txtAmount.setText(list.get(position).getAmount() + "");

        boolean status = list.get(position).isStatus();
        if (status == true) {
            holder.txtStatus.setText(context.getString(R.string.string_main_summary_report_status_ok));
            holder.txtStatus.setTextColor(AppConfig.getThemeColor());
        } else {
            holder.txtStatus.setText(context.getString(R.string.string_main_summary_report_status_not));
            holder.txtStatus.setTextColor(context.getColor(R.color.main_color_listview_status_not));

        }

        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MainSummaryFragmentEventInteger(position));
            }
        });

        holder.itemContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                mFragment.onItemRecycleViewLongClicked(position);

                Integer integerObj = new Integer(position);
                if (ComonUtils.alreadyChoosen(integerObj, ComonUtils.deletedCategories)) {
                    ComonUtils.deletedCategories.remove(integerObj);
                    holder.itemContainer.setBackgroundResource(R.drawable.borderless_ripple_drawable);
                } else {
                    ComonUtils.deletedCategories.add(integerObj);
                    holder.itemContainer.setBackgroundResource(R.drawable.list_item_selected);
                }


                return true;
            }
        });

        holder.itemContainer.setBackgroundResource(R.drawable.borderless_ripple_drawable);

        holder.txtDivider.setBackgroundColor(AppConfig.getAlpha2BackgroundColorSetting());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, MainSummaryReport data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(MainSummaryReport data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }
}

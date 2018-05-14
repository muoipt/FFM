package com.xalenmy.ffm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xalenmy.ffm.R;
import com.xalenmy.ffm.eventmodel.MainSearchActivityEventObj;
import com.xalenmy.ffm.model.ReportDetailDisplay;
import com.xalenmy.ffm.utils.AppConfig;
import com.xalenmy.ffm.utils.ComonUtils;
import com.xalenmy.ffm.view.MainReportFragment;
import com.xalenmy.ffm.view.MainSearchActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class ReportDetailDisplayRecycleViewAdapter extends RecyclerView.Adapter<ReportDetailDisplayRecycleViewHolder> {

    private List<ReportDetailDisplay> list = Collections.emptyList();
    private Context context;
    private static MainReportFragment mFragment;

    public ReportDetailDisplayRecycleViewAdapter() {
    }

    public ReportDetailDisplayRecycleViewAdapter(MainReportFragment mainReportFragment, List<ReportDetailDisplay> listItem) {
        mFragment = mainReportFragment;
        list = listItem;
    }

    public ReportDetailDisplayRecycleViewAdapter(List<ReportDetailDisplay> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ReportDetailDisplayRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_detail_display_recycleview, parent, false);
        ReportDetailDisplayRecycleViewHolder holder = new ReportDetailDisplayRecycleViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ReportDetailDisplayRecycleViewHolder holder, final int position) {

        if(mFragment == null)
            return;

        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemContainer.setBackgroundResource(R.drawable.list_item_selected);

                if(context instanceof MainSearchActivity){
                    MainSearchActivityEventObj eventObj = new MainSearchActivityEventObj();
                    eventObj.setMsg(ComonUtils.EDIT_REPORT_MSG);
                    eventObj.setReportDetailDisplay(list.get(position));
                    EventBus.getDefault().post(eventObj);
                } else {
                    mFragment.onItemRecycleViewClicked(position);
                }
            }
        });

        holder.itemContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                mFragment.onItemRecycleViewLongClicked(position);

                Integer integerObj = new Integer(position);
                if (ComonUtils.alreadyChoosen(integerObj, ComonUtils.deletedReports)) {
                    ComonUtils.deletedReports.remove(integerObj);
                    holder.itemContainer.setBackgroundResource(R.drawable.borderless_ripple_drawable);
                } else {
                    ComonUtils.deletedReports.add(integerObj);
                    holder.itemContainer.setBackgroundResource(R.drawable.list_item_selected);
                }

                return true;
            }
        });

        holder.itemContainer.setBackgroundResource(R.drawable.borderless_ripple_drawable);
        holder.txtDateTime.setText(list.get(position).getReportDatetime());
        holder.txtTitle.setText(list.get(position).getReportTitle());
        holder.txtAmount.setText(list.get(position).getReportAmount() + "");
        holder.txtNote.setText(list.get(position).getReportNote());
        holder.txtDividerView.setBackgroundColor(AppConfig.getAlpha2BackgroundColorSetting());
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
    public void insert(int position, ReportDetailDisplay data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(ReportDetailDisplay data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    //define interface
    public interface OnItemClickListener {
        public void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }
}

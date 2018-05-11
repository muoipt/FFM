package com.xalenmy.ffm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xalenmy.ffm.R;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class ReportDetailDisplayRecycleViewHolder extends RecyclerView.ViewHolder {

    TextView txtDateTime, txtAmount, txtTitle, txtNote, txtDividerView;
    LinearLayout itemContainer;

    public ReportDetailDisplayRecycleViewHolder(View itemView) {
        super(itemView);
        itemContainer = (LinearLayout) itemView.findViewById(R.id.report_detail_display_recycle_row_layout);
        txtDateTime = (TextView) itemView.findViewById(R.id.txtReportListItemDateTime);
        txtTitle = (TextView) itemView.findViewById(R.id.txtReportListItemTitle);
        txtAmount = (TextView) itemView.findViewById(R.id.txtReportListItemAmount);
        txtNote = (TextView) itemView.findViewById(R.id.txtReportListItemNote);
        txtDividerView = (TextView) itemView.findViewById(R.id.divider_report_recycle_view);
    }
}

package com.muoipt.ffm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muoipt.ffm.R;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class MainSummaryRecycleViewHolder extends RecyclerView.ViewHolder {
    TextView txtCategory, txtAmount, txtStatus, txtDivider;
    LinearLayout itemContainer;

    public MainSummaryRecycleViewHolder(View itemView) {
        super(itemView);
        itemContainer = (LinearLayout) itemView.findViewById(R.id.main_summary_recycle_row_layout);
        txtCategory = (TextView) itemView.findViewById(R.id.txtMainSummaryCategory);
        txtAmount = (TextView) itemView.findViewById(R.id.txtMainSummaryAmount);
        txtStatus = (TextView) itemView.findViewById(R.id.txtMainSummaryStatus);
        txtDivider = (TextView) itemView.findViewById(R.id.txtMainSummaryDivider);
    }
}

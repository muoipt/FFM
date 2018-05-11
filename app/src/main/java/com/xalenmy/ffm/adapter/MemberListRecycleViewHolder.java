package com.xalenmy.ffm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xalenmy.ffm.R;

/**
 * Created by XalenMy on 3/7/2018.
 */

public class MemberListRecycleViewHolder extends RecyclerView.ViewHolder{
    LinearLayout group_item_container;
    ImageView img;
    TextView txtEmail;
    Button btnMore;

    public MemberListRecycleViewHolder(View itemView) {
        super(itemView);
        group_item_container = (LinearLayout) itemView.findViewById(R.id.group_item_container);
        img = (ImageView) itemView.findViewById(R.id.member_item_img);
        txtEmail = (TextView) itemView.findViewById(R.id.member_item_email);
        btnMore = (Button) itemView.findViewById(R.id.member_item_more_btn);
    }
}

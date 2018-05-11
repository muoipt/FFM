package com.xalenmy.ffm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.xalenmy.ffm.R;
import com.xalenmy.ffm.eventmodel.GroupListMemberActivityEventObject;
import com.xalenmy.ffm.eventmodel.GroupListMemberActivityEventString;
import com.xalenmy.ffm.eventmodel.MainSearchActivityEventObj;
import com.xalenmy.ffm.model.UserDetail;
import com.xalenmy.ffm.utils.AppConfig;
import com.xalenmy.ffm.utils.ComonUtils;
import com.xalenmy.ffm.view.GroupListMemberActivity;
import com.xalenmy.ffm.view.MainSearchActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by XalenMy on 3/7/2018.
 */

public class MemberListRecycleViewAdapter extends RecyclerView.Adapter<MemberListRecycleViewHolder> {
    List<UserDetail> list = Collections.emptyList();
    Context context;

    public MemberListRecycleViewAdapter(List<UserDetail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MemberListRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_member_list_recycle_view, parent, false);
        MemberListRecycleViewHolder holder = new MemberListRecycleViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MemberListRecycleViewHolder holder, final int position) {
        holder.txtEmail.setText(list.get(position).getUserEmail());

        AppConfig.changeRoundViewColor(holder.img);

        int groupAvatar = list.get(position).getUserAvatar();
        String groupAvatarImgPath = list.get(position).getUserAvatarImgPath();
        if(groupAvatarImgPath != null && groupAvatarImgPath != ""){
            holder.img.setImageBitmap(getAvatarBitmapFromPath(groupAvatarImgPath));
        } else if(groupAvatar != -1){
            holder.img.setImageDrawable(context.getDrawable(groupAvatar));
        }

        holder.group_item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new GroupListMemberActivityEventObject(ComonUtils.ACTION_OPEN_PROFILE_USER, list.get(position)));
            }
        });

        holder.group_item_container.setBackgroundResource(R.drawable.borderless_ripple_drawable);

        //normal user can not edit User list
        final UserDetail loginUser = AppConfig.getUserLogInInfor();
        if (loginUser.getUserRole() == ComonUtils.USER_ROLE_NORMAL) {
            holder.btnMore.setVisibility(View.GONE);
        } else if (loginUser.getUserRole() == ComonUtils.USER_ROLE_ADMIN) {
            holder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu menu = new PopupMenu(context, holder.btnMore, Gravity.RIGHT);

                    menu.inflate(R.menu.menu_group_list_member);

                    Menu mMenu = menu.getMenu();
                    if (list.get(position).getUserStatus() == ComonUtils.USER_STATUS_NORMAL) {
                        mMenu.findItem(R.id.action_approve).setVisible(false);
                        mMenu.findItem(R.id.action_reject).setVisible(false);
                    } else if (list.get(position).getUserStatus() == ComonUtils.USER_STATUS_NEW) {
                        mMenu.findItem(R.id.action_approve).setVisible(true);
                        mMenu.findItem(R.id.action_reject).setVisible(true);
                    } else if (list.get(position).getUserStatus() == ComonUtils.USER_STATUS_REJECT ||
                            list.get(position).getUserStatus() == ComonUtils.USER_STATUS_REMOVED) {
                        mMenu.findItem(R.id.action_approve).setVisible(true);
                        mMenu.findItem(R.id.action_reject).setVisible(false);
                        mMenu.findItem(R.id.action_remove).setVisible(false);
                    }

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_approve:
                                    EventBus.getDefault().post(new GroupListMemberActivityEventString(ComonUtils.ACTION_APPROVE_USER, position));
                                    break;
                                case R.id.action_reject:
                                    EventBus.getDefault().post(new GroupListMemberActivityEventString(ComonUtils.ACTION_REJECT_USER, position));
                                    break;
                                case R.id.action_remove:
                                    EventBus.getDefault().post(new GroupListMemberActivityEventString(ComonUtils.ACTION_REMOVE_USER, position));

                                    if (context instanceof MainSearchActivity) {
                                        MainSearchActivityEventObj eventObj = new MainSearchActivityEventObj();
                                        eventObj.setMsg(ComonUtils.EDIT_USER_MSG);
                                        eventObj.setPosition(position);
                                        eventObj.setUserDetail(list.get(position));
                                        EventBus.getDefault().post(eventObj);
                                    }

                                    break;
                            }

                            return false;
                        }
                    });

                    menu.show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Bitmap getAvatarBitmapFromPath(String imgPath) {
        File image = new File(imgPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) context.getResources().getDimension(R.dimen.img_header_main_icon_width), (int) context.getResources().getDimension(R.dimen.img_header_main_icon_width), true);

        return bitmap;
    }
}


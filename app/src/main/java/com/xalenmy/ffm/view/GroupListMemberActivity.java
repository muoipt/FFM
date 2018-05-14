package com.xalenmy.ffm.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xalenmy.ffm.R;
import com.xalenmy.ffm.adapter.MemberListRecycleViewAdapter;
import com.xalenmy.ffm.control.GroupDetailControl;
import com.xalenmy.ffm.control.GroupDetailServerControl;
import com.xalenmy.ffm.control.UserDetailControl;
import com.xalenmy.ffm.control.UserDetailCustomServerControl;
import com.xalenmy.ffm.control.UserDetailServerControl;
import com.xalenmy.ffm.eventmodel.GroupListMemberActivityEventObject;
import com.xalenmy.ffm.eventmodel.GroupListMemberActivityEventString;
import com.xalenmy.ffm.listener.CustomTouchListener;
import com.xalenmy.ffm.listener.OnRecycleViewItemClickListener;
import com.xalenmy.ffm.model.GroupDetail;
import com.xalenmy.ffm.model.MainSummaryReport;
import com.xalenmy.ffm.model.UserDetail;
import com.xalenmy.ffm.utils.AppConfig;
import com.xalenmy.ffm.utils.ComonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GroupListMemberActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapseToolbar;
    private RecyclerView recyclerViewNewUser, recyclerViewActiveUser, recyclerViewRemovedUser;
    private Toolbar toolbar;
    private UserDetailControl userDetailControl;
    private UserDetailCustomServerControl userDetailServerControl;
    private List<UserDetail> newUserList, activeUserList, removedUserList, userChangedList;
    private MemberListRecycleViewAdapter newUseradApter, activeUserAdapter, removedUserAdapter;
    private FloatingActionButton fab_add_new_group;
    private int currentGroupId;
    private GroupDetail currentGroup;
    private UserDetail currentUser;
    private ImageButton btnExtentNew, btnExtentCurrent, btnExtentRemoved;
    private RelativeLayout layout_new_user, layout_active_user, layout_removed_user;
    private TextView txtDiv4, txtDiv5, txtDiv6, txtDiv7, txtDiv8, txtDiv9;
    private ImageView imgGroupAvatar;

    public GroupListMemberActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list_member);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        getControlWidget();
        imgGroupAvatar.setImageDrawable(getDrawable(R.drawable.icon_default_camera_large));

        currentGroup = AppConfig.getCurrentGroup();
        currentUser = AppConfig.getUserLogInInfor();

        if(currentGroup.getGroupName() != null && !currentGroup.getGroupName().equals(""))
            toolbar.setTitle(currentGroup.getGroupName().toString());

        if (currentGroup.getGroupAvatarImgPath() != null && !currentGroup.getGroupAvatarImgPath().equals("")) {
            imgGroupAvatar.setImageBitmap(getAvatarBitmapFromPath(currentGroup.getGroupAvatarImgPath()));
            currentGroupId = currentGroup.getGroupId();

        } else if (currentUser.getUserEmail() != null && !currentUser.getUserEmail().equals("")) {
            GroupDetailControl control = new GroupDetailControl(this);
            GroupDetail groupDetail = control.getGroupDetailById(currentUser.getUserGroupId());
            toolbar.setTitle(groupDetail.getGroupName() + "");

            int groupAvatar = groupDetail.getGroupAvatar();
            String groupAvatarImgPath = groupDetail.getGroupAvatarImgPath();
            if (groupAvatar != -1) {
                imgGroupAvatar.setImageDrawable(getDrawable(groupAvatar));
            } else if (groupAvatarImgPath != null && !groupAvatarImgPath.equals("")) {
                imgGroupAvatar.setImageBitmap(getAvatarBitmapFromPath(groupAvatarImgPath));
            }

            currentGroupId = currentUser.getUserGroupId();
        } else {
            toolbar.setTitle(getResources().getString(R.string.my_group));
        }

        setSupportActionBar(toolbar);

        initControl();

        newUserList = userDetailControl.getNewUsersFromDB();

        newUseradApter = new MemberListRecycleViewAdapter(newUserList, this);
        recyclerViewNewUser.setAdapter(newUseradApter);
        recyclerViewNewUser.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNewUser.addOnItemTouchListener(new CustomTouchListener(this, new OnRecycleViewItemClickListener() {
            @Override
            public void onClick(View view, int index) {

            }
        }));

        newUseradApter.notifyDataSetChanged();

        activeUserList = userDetailControl.getActiveUsersFromDB(currentUser);
        activeUserAdapter = new MemberListRecycleViewAdapter(activeUserList, this);
        recyclerViewActiveUser.setAdapter(activeUserAdapter);
        recyclerViewActiveUser.setLayoutManager(new LinearLayoutManager(this));

        activeUserAdapter.notifyDataSetChanged();

        removedUserList = userDetailControl.getRemovedUsersFromDB();
        removedUserAdapter = new MemberListRecycleViewAdapter(removedUserList, this);
        recyclerViewRemovedUser.setAdapter(removedUserAdapter);
        recyclerViewRemovedUser.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRemovedUser.addOnItemTouchListener(new CustomTouchListener(this, new OnRecycleViewItemClickListener() {
            @Override
            public void onClick(View view, int index) {

            }
        }));

        removedUserAdapter.notifyDataSetChanged();

        EventBus.getDefault().register(this);

        fab_add_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAddNewUser();
            }
        });


        layout_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processExtendNewList();
            }
        });
        layout_active_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processExtendActiveList();
            }
        });
        layout_removed_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processExtendRemovedList();
            }
        });

        imgGroupAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processChangeGroupAvatar();
            }
        });

        initExtendNewList();
        initExtendNewList();
        initExtendNewList();

    }


    private void getControlWidget() {
        recyclerViewNewUser = (RecyclerView) findViewById(R.id.lst_new_user);
        recyclerViewActiveUser = (RecyclerView) findViewById(R.id.lst_active_user);
        recyclerViewRemovedUser = (RecyclerView) findViewById(R.id.lst_removed_user);
        toolbar = (Toolbar) findViewById(R.id.member_list_toolbar);
        collapseToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_member_list_layout);
        fab_add_new_group = (FloatingActionButton) findViewById(R.id.fab_add_new_group);
        btnExtentNew = (ImageButton) findViewById(R.id.btnExtentNewList);
        btnExtentCurrent = (ImageButton) findViewById(R.id.btnExtentActiveList);
        btnExtentRemoved = (ImageButton) findViewById(R.id.btnExtentRemovedList);
        layout_new_user = (RelativeLayout) findViewById(R.id.layout_new_user);
        layout_active_user = (RelativeLayout) findViewById(R.id.layout_active_user);
        layout_removed_user = (RelativeLayout) findViewById(R.id.layout_removed_user);
        txtDiv4 = (TextView) findViewById(R.id.txtDiv4);
        txtDiv5 = (TextView) findViewById(R.id.txtDiv5);
        txtDiv6 = (TextView) findViewById(R.id.txtDiv6);
        txtDiv7 = (TextView) findViewById(R.id.txtDiv7);
        txtDiv8 = (TextView) findViewById(R.id.txtDiv8);
        txtDiv9 = (TextView) findViewById(R.id.txtDiv9);
        imgGroupAvatar = (ImageView) findViewById(R.id.imgGroupAvatar);


    }

    private void initControl() {
        userDetailControl = new UserDetailControl(this);
        userDetailServerControl = new UserDetailCustomServerControl(this);

        userChangedList = new ArrayList<UserDetail>();

        initExtendNewList();
        initExtendActiveList();
        initExtendRemovedList();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = AppConfig.getUserLogInInfor();
        currentGroup = AppConfig.getCurrentGroup();

        txtDiv4.setBackgroundColor(AppConfig.getThemeColor());
        txtDiv5.setBackgroundColor(AppConfig.getThemeColor());
        txtDiv6.setBackgroundColor(AppConfig.getThemeColor());
        txtDiv7.setBackgroundColor(AppConfig.getThemeColor());
        txtDiv8.setBackgroundColor(AppConfig.getThemeColor());
        txtDiv9.setBackgroundColor(AppConfig.getThemeColor());
        fab_add_new_group.setBackgroundTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));

        collapseToolbar.setBackgroundTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));
        collapseToolbar.setBackgroundColor(AppConfig.getThemeColor());
        collapseToolbar.setStatusBarScrimColor(AppConfig.getBackgroundColorSetting());
        collapseToolbar.setContentScrimColor(AppConfig.getAlpha2BackgroundColorSetting());

        layout_new_user.setBackgroundColor(AppConfig.getAlpha1BackgroundColorSetting());
        layout_active_user.setBackgroundColor(AppConfig.getAlpha1BackgroundColorSetting());
        layout_removed_user.setBackgroundColor(AppConfig.getAlpha1BackgroundColorSetting());

        newUserList = userDetailControl.getNewUsersFromDB();
        newUseradApter = new MemberListRecycleViewAdapter(newUserList, this);
        recyclerViewNewUser.setAdapter(newUseradApter);
        newUseradApter.notifyDataSetChanged();

        activeUserList = userDetailControl.getActiveUsersFromDB(currentUser);
        activeUserAdapter = new MemberListRecycleViewAdapter(activeUserList, this);
        recyclerViewActiveUser.setAdapter(activeUserAdapter);
        activeUserAdapter.notifyDataSetChanged();


        if (newUserList.size() == 0) {
            btnExtentNew.setVisibility(View.GONE);
        } else {
            btnExtentNew.setVisibility(View.VISIBLE);
        }

        if (activeUserList.size() == 0) {
            btnExtentCurrent.setVisibility(View.GONE);
        } else {
            btnExtentCurrent.setVisibility(View.VISIBLE);
        }

        if (removedUserList.size() == 0) {
            btnExtentRemoved.setVisibility(View.GONE);
        } else {
            btnExtentRemoved.setVisibility(View.VISIBLE);
        }

        AppConfig.changeRoundViewColor(imgGroupAvatar);

        GroupDetail currentGroup = AppConfig.getCurrentGroup();
        if (currentGroup.getGroupAvatarImgPath() != null && !currentGroup.getGroupAvatarImgPath().equals("")) {
            imgGroupAvatar.setImageBitmap(getAvatarBitmapFromPath(currentGroup.getGroupAvatarImgPath()));
            toolbar.setTitle(currentGroup.getGroupName());
        }
    }

    private void refreshLists() {
        newUserList = userDetailControl.getNewUsersFromDB();
        newUseradApter = new MemberListRecycleViewAdapter(newUserList, this);
        recyclerViewNewUser.setAdapter(newUseradApter);
        newUseradApter.notifyDataSetChanged();

        activeUserList = userDetailControl.getActiveUsersFromDB(currentUser);
        activeUserAdapter = new MemberListRecycleViewAdapter(activeUserList, this);
        recyclerViewActiveUser.setAdapter(activeUserAdapter);
        activeUserAdapter.notifyDataSetChanged();

        removedUserList = userDetailControl.getRemovedUsersFromDB();
        removedUserAdapter = new MemberListRecycleViewAdapter(removedUserList, this);
        recyclerViewRemovedUser.setAdapter(removedUserAdapter);
        removedUserAdapter.notifyDataSetChanged();

        processExtendNewList();
        processExtendActiveList();
        processExtendRemovedList();
    }

    @Subscribe
    public void processEventString(GroupListMemberActivityEventString obj) {
        String event = obj.getS();
        int position = obj.getIndex();

        if (event.equals(ComonUtils.ACTION_APPROVE_USER)) {
            if (position < newUserList.size() && newUserList.get(position).getUserStatus() == ComonUtils.USER_STATUS_NEW) {
                newUserList.get(position).setUserStatus(ComonUtils.USER_STATUS_NORMAL);
                userChangedList.add(newUserList.get(position));
                userDetailControl.updateUser(newUserList.get(position));
            } else if (position < removedUserList.size() && (removedUserList.get(position).getUserStatus() == ComonUtils.USER_STATUS_REMOVED
                    || removedUserList.get(position).getUserStatus() == ComonUtils.USER_STATUS_REJECT)) {
                removedUserList.get(position).setUserStatus(ComonUtils.USER_STATUS_NORMAL);
                userChangedList.add(removedUserList.get(position));
                userDetailControl.updateUser(removedUserList.get(position));
            }
        } else if (event.equals(ComonUtils.ACTION_REJECT_USER)) {
            newUserList.get(position).setUserStatus(ComonUtils.USER_STATUS_REJECT);
            userChangedList.add(newUserList.get(position));
            userDetailControl.updateUser(newUserList.get(position));
        } else if (event.equals(ComonUtils.ACTION_REMOVE_USER)) {
            if (position < newUserList.size() && newUserList.get(position).getUserStatus() == ComonUtils.USER_STATUS_NEW) {
                newUserList.get(position).setUserStatus(ComonUtils.USER_STATUS_REMOVED);
                userChangedList.add(newUserList.get(position));
                userDetailControl.updateUser(newUserList.get(position));
            } else if (position < activeUserList.size() && activeUserList.get(position).getUserStatus() == ComonUtils.USER_STATUS_NORMAL) {
                activeUserList.get(position).setUserStatus(ComonUtils.USER_STATUS_REMOVED);
                userChangedList.add(activeUserList.get(position));
                userDetailControl.updateUser(activeUserList.get(position));
            }
        }

        refreshLists();
        new UpdateUserServerTaks().execute();
    }

    @Subscribe
    public void processEventObj(GroupListMemberActivityEventObject obj) {
        UserDetail userDetail = obj.getObj();
        processOpenUserProfile(userDetail);
    }

    class UpdateUserServerTaks extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return userDetailServerControl.updateUserToServer(userChangedList);
        }
    }

    private void processAddNewUser() {
        UserDetail currentUser = AppConfig.getUserLogInInfor();
        GroupDetail currentGroup = AppConfig.getCurrentGroup();

        //truong hop admin tao group truoc sau do moi add user
        if ((currentGroup.getGroupId() != -1 && currentUser.getUserEmail() == null)) {// || (currentUser.getUserEmail() != null && currentUser.getUserStatus() == ComonUtils.USER_STATUS_NORMAL)) {
            Intent intent = new Intent(this, SignUpActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(ComonUtils.SIGN_UP_ADMIN_BUNDLE, currentGroupId);
            intent.putExtra(ComonUtils.SIGN_UP_ADMIN_INTENT, bundle);
            startActivityForResult(intent, ComonUtils.CODE_ADD_ADMIN);
        } else if ((currentUser.getUserEmail() != null && currentUser.getUserRole() == ComonUtils.USER_ROLE_ADMIN)) {
            Intent intent = new Intent(this, SignUpActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(ComonUtils.SIGN_UP_USER_FROM_ADMIN_BUNDLE, currentGroupId);
            intent.putExtra(ComonUtils.SIGN_UP_USER_FROM_ADMIN_INTENT, bundle);
            startActivityForResult(intent, ComonUtils.CODE_ADD_MEMBER_BY_ADMIN);
        } else if (currentUser.getUserEmail() != null && currentUser.getUserStatus() == ComonUtils.USER_STATUS_NEW) {
            displayAlertDlg(2);
        } else{
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivityForResult(intent, ComonUtils.CODE_SIGN_UP);
        }
    }

    private void initExtendNewList() {
        if (newUserList != null && newUserList.size() == 0) {
            recyclerViewNewUser.setVisibility(View.GONE);
            btnExtentNew.setBackgroundResource(R.drawable.ic_arrow_drop_down_black);
            btnExtentNew.setVisibility(View.GONE);
            txtDiv9.setVisibility(View.GONE);
        } else if (newUserList != null && newUserList.size() > 0) {
            recyclerViewNewUser.setVisibility(View.VISIBLE);
            btnExtentNew.setVisibility(View.VISIBLE);
            btnExtentNew.setBackgroundResource(R.drawable.ic_arrow_drop_up_black);
            txtDiv9.setVisibility(View.VISIBLE);
        } else {
            txtDiv9.setVisibility(View.GONE);
        }
    }

    private void initExtendActiveList() {
        if (activeUserList != null && activeUserList.size() == 0) {
            recyclerViewActiveUser.setVisibility(View.GONE);
            btnExtentCurrent.setBackgroundResource(R.drawable.ic_arrow_drop_down_black);
            btnExtentCurrent.setVisibility(View.GONE);
            txtDiv7.setVisibility(View.GONE);
        } else if (activeUserList != null && activeUserList.size() > 0) {
            recyclerViewActiveUser.setVisibility(View.VISIBLE);
            btnExtentCurrent.setBackgroundResource(R.drawable.ic_arrow_drop_up_black);
            btnExtentCurrent.setVisibility(View.VISIBLE);
            txtDiv7.setVisibility(View.VISIBLE);
        } else {
            txtDiv7.setVisibility(View.GONE);
        }
    }

    private void initExtendRemovedList() {
        if (removedUserList != null && removedUserList.size() == 0) {
            recyclerViewRemovedUser.setVisibility(View.GONE);
            btnExtentRemoved.setBackgroundResource(R.drawable.ic_arrow_drop_down_black);
            btnExtentRemoved.setVisibility(View.GONE);
            txtDiv8.setVisibility(View.GONE);
        } else if (removedUserList != null && removedUserList.size() > 0) {
            recyclerViewRemovedUser.setVisibility(View.VISIBLE);
            btnExtentRemoved.setBackgroundResource(R.drawable.ic_arrow_drop_up_black);
            btnExtentRemoved.setVisibility(View.VISIBLE);
            txtDiv8.setVisibility(View.VISIBLE);
        } else {
            txtDiv8.setVisibility(View.GONE);
        }
    }

    private void processExtendNewList() {
        if (newUserList.size() == 0) return;

        if (recyclerViewNewUser.getVisibility() == View.VISIBLE) {
            recyclerViewNewUser.setVisibility(View.GONE);
            btnExtentNew.setBackgroundResource(R.drawable.ic_arrow_drop_down_black);
            txtDiv9.setVisibility(View.GONE);
        } else {
            recyclerViewNewUser.setVisibility(View.VISIBLE);
            btnExtentNew.setBackgroundResource(R.drawable.ic_arrow_drop_up_black);
            txtDiv9.setVisibility(View.VISIBLE);
        }
    }

    private void processExtendActiveList() {
        if (activeUserList.size() == 0) return;

        if (recyclerViewActiveUser.getVisibility() == View.VISIBLE) {
            recyclerViewActiveUser.setVisibility(View.GONE);
            btnExtentCurrent.setBackgroundResource(R.drawable.ic_arrow_drop_down_black);
            txtDiv6.setVisibility(View.GONE);
        } else {
            recyclerViewActiveUser.setVisibility(View.VISIBLE);
            btnExtentCurrent.setBackgroundResource(R.drawable.ic_arrow_drop_up_black);
            txtDiv6.setVisibility(View.VISIBLE);
        }
    }

    private void processExtendRemovedList() {
        if (removedUserList.size() == 0) return;

        if (recyclerViewRemovedUser.getVisibility() == View.VISIBLE) {
            recyclerViewRemovedUser.setVisibility(View.GONE);
            btnExtentRemoved.setBackgroundResource(R.drawable.ic_arrow_drop_down_black);
            txtDiv8.setVisibility(View.GONE);
        } else {
            recyclerViewRemovedUser.setVisibility(View.VISIBLE);
            btnExtentRemoved.setBackgroundResource(R.drawable.ic_arrow_drop_up_black);
            txtDiv8.setVisibility(View.VISIBLE);
        }
    }

    private void processOpenUserProfile(UserDetail selectedUser) {
        Intent intent = new Intent(this, SignUpActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(ComonUtils.OPEN_USER_PROFILE_BUNDLE, selectedUser);
        intent.putExtra(ComonUtils.OPEN_USER_PROFILE_INTENT, bundle);

        startActivity(intent);
    }

    private Bitmap getAvatarBitmapFromPath(String imgPath) {
        File image = new File(imgPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) getResources().getDimension(R.dimen.app_bar_main_img_banner_height), (int) getResources().getDimension(R.dimen.app_bar_main_img_banner_height), true);

        return bitmap;
    }

    private void processChangeGroupAvatar() {
        UserDetail currentUser = AppConfig.getUserLogInInfor();
        GroupDetail currentGroup = AppConfig.getCurrentGroup();

        if (currentGroup.getGroupId() == -1) {
            //add new group
            Intent newGroupIntent = new Intent(this, GroupAddNewActivity.class);
            newGroupIntent.setAction(ComonUtils.ACTION_ADD_GROUP_FROM_GROUP_LIST);
            startActivityForResult(newGroupIntent, ComonUtils.CODE_ADD_GROUP_FROM_GROUP_LIST);
        } else if ((currentGroup.getGroupId() != -1 && currentUser.getUserEmail() == null) || (currentUser.getUserEmail() != null && currentUser.getUserStatus() == ComonUtils.USER_STATUS_NORMAL)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else if (currentUser.getUserEmail() != null && currentUser.getUserStatus() == ComonUtils.USER_STATUS_NEW) {
            displayAlertDlg(2);
        } /*else{
            Toast.makeText(this, "You need to log in to use this function", Toast.LENGTH_LONG).show();
        } */


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.setPackage("com.sec.android.gallery3d");
                    photoPickerIntent.putExtra("return-data", true);
                    photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    photoPickerIntent.putExtra("noFaceDetection", true);
                    startActivityForResult(photoPickerIntent, ComonUtils.CODE_SELECT_AVATAR_FROM_GROUP_LIST);

                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == ComonUtils.CODE_UPDATE_ADMIN) {
//            Bundle bundle = data.getBundleExtra(ComonUtils.SIGN_UP_INTENT);
//            if (bundle != null) {
//
//            }
//        } else if (resultCode == ComonUtils.CODE_ADD_ADMIN || resultCode == ComonUtils.CODE_ADD_MEMBER_BY_ADMIN) {
//            Bundle bundle = data.getBundleExtra(ComonUtils.SIGN_UP_INTENT);
//            if (bundle != null) {
//                UserDetail userDetail = (UserDetail) bundle.getSerializable(ComonUtils.SIGN_UP_BUNDLE);
//
//                if (userDetail.getUserStatus() == ComonUtils.USER_STATUS_NEW) {
//                    newUserList.add(userDetail);
//                    newUseradApter = new MemberListRecycleViewAdapter(newUserList, this);
//                    recyclerViewNewUser.setAdapter(newUseradApter);
//                    newUseradApter.notifyDataSetChanged();
//                } else if(userDetail.getUserStatus() == ComonUtils.USER_STATUS_NORMAL){
//                    userDetail.setUserRole(ComonUtils.USER_ROLE_ADMIN);
//                    userDetail.setUserStatus(ComonUtils.USER_STATUS_NORMAL);
//                    activeUserList.add(userDetail);
//                    activeUserAdapter.notifyDataSetChanged();
//                    AppConfig.saveUserInfoToSharePreference(userDetail);
//                    //process auto login
//                    LoginActivity loginActivity = new LoginActivity();
//                    if (!loginActivity.autoLogInUser(userDetail.getUserEmail(), userDetail.getUserPassword())) {
//                        Log.i("AutoLogIn", "Failed");
//                    }
//                }
//            }
//        } else
        if (requestCode == ComonUtils.CODE_SELECT_AVATAR_FROM_GROUP_LIST) {
            if (data != null) {
                Uri selectedImage = data.getData();
                String imgPath = null;

                try {
                    Bitmap bitmap = ComonUtils.getCorrectlyOrientedImage(this, selectedImage);
                    imgGroupAvatar.setImageBitmap(bitmap);

                    File savedFile = new File(getExternalCacheDir(), ComonUtils.createNewCacheFileName());
                    try {
                        FileOutputStream out = new FileOutputStream(savedFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();

                        imgPath = savedFile.getAbsolutePath();
                    } catch (Exception e) {
                        Log.e("Image", "Convert");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                GroupDetailControl control = new GroupDetailControl(this);
                GroupDetail groupDetail = control.getGroupDetailById(currentUser.getUserGroupId());
                groupDetail.setGroupAvatarImgPath(imgPath);
                control.updateGroup(groupDetail);

                //update on server
                GroupDetailServerControl groupDetailServerControl = new GroupDetailServerControl(this);
                ArrayList<GroupDetail> groupDetails = new ArrayList<GroupDetail>();
                groupDetails.add(groupDetail);
                if (groupDetailServerControl.updateGroupToServer(groupDetails)) {
                    Toast.makeText(this, "Update group avatar successfull", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Update group avatar failed", Toast.LENGTH_LONG).show();
                }

                //update avatar path on share preference
                currentGroup.setGroupAvatarImgPath(imgPath);
                AppConfig.saveGroupInfoToSharePreference(currentGroup);

            }
        } else if (resultCode == ComonUtils.CODE_ADD_GROUP_FROM_GROUP_LIST) {
            processDisplayGroup(data);
        }
    }

    private void processDisplayGroup(Intent intent) {
        Bundle bundle = intent.getBundleExtra(ComonUtils.NEW_GROUP_INTENT);
        if (bundle != null) {
            GroupDetail groupDetail = (GroupDetail) bundle.getSerializable(ComonUtils.NEW_GROUP_BUNDLE);
            if (groupDetail.getGroupAvatarImgPath() != null && !groupDetail.getGroupAvatarImgPath().equals(""))
                imgGroupAvatar.setImageBitmap(getAvatarBitmapFromPath(groupDetail.getGroupAvatarImgPath()));
            toolbar.setTitle(groupDetail.getGroupName());
            currentGroup = new GroupDetail(groupDetail.getGroupId(), groupDetail.getGroupName(), groupDetail.getGroupAvatarImgPath());
            AppConfig.saveGroupInfoToSharePreference(currentGroup);
        }
    }

    private void displayAlertDlg(int item) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sync_layout);
        dialog.show();

        ImageView img_sync = dialog.findViewById(R.id.img_sync);
        img_sync.setImageResource(R.drawable.info_icon);
        TextView txt_message = dialog.findViewById(R.id.txt_message);

        if (item == 0) {
            txt_message.setText(getString(R.string.alert_login_info));
        } else if (item == 1) {
            txt_message.setText(getString(R.string.alert_internet_info));
        } else if(item == 2){
            txt_message.setText(getString(R.string.alert_user_status_info));
        }

        Button buttonOK = dialog.findViewById(R.id.btn_sync_ok);
        AppConfig.changeRoundViewColor(buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}

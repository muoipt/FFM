package com.muoipt.ffm.view;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.muoipt.ffm.R;
import com.muoipt.ffm.control.GroupDetailControl;
import com.muoipt.ffm.control.GroupDetailServerControl;
import com.muoipt.ffm.control.TypeDetailServerControl;
import com.muoipt.ffm.control.TypeDetaillControl;
import com.muoipt.ffm.eventmodel.GroupImgPathEventObj;
import com.muoipt.ffm.model.GroupDetail;
import com.muoipt.ffm.model.TypeDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;
import com.muoipt.ffm.utils.SyncUtils;

//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GroupAddNewActivity extends AppCompatActivity {

    private GroupAddTask mGroupAddTask = null;

    private LinearLayout add_group_banner;
    private ImageView imgGroupAvatar;
    private EditText groupNameView;
    private String groupName;
    private Button btnAddGroup;
    private View mGroupRegisterFormView;
    private View mProgressView;
    private Toolbar toolbar;
    private String groupInputAvatarImgPath = "";
    private int groupInputAvatar = -1;
    private boolean isUpdateGroup = false;

    private GroupDetail groupDetail;
    private int updateGroupId = -1;
    private String callingAction = null;

    public GroupAddNewActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_new);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getControlWidget();
//        actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(AppConfig.getThemeColor()));

        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempAddGroup();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getDrawable(R.drawable.back_icon_36dp));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
        toolbar.setTitle(getString(R.string.title_activity_group_add_new));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgGroupAvatar.setImageDrawable(getDrawable(R.drawable.icon_default_camera_large));
        }
        imgGroupAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processChooseAvatar();
            }
        });

        Intent callingIntent = getIntent();
        if (callingIntent != null) {
            Bundle callingBundle = callingIntent.getBundleExtra(ComonUtils.UPDATE_GROUP_INTENT);
            if (callingBundle != null) {
                GroupDetail group = (GroupDetail) callingBundle.getSerializable(ComonUtils.UPDATE_GROUP_BUNDLE);
                if (group != null) {
                    updateGroupId = group.getGroupId();
                    groupNameView.setText(group.getGroupName());
                    groupName = group.getGroupName();
                    groupInputAvatarImgPath = group.getGroupAvatarImgPath();
                    if (group.getGroupAvatarImgPath() != null && !group.getGroupAvatarImgPath().equals("")) {
                        imgGroupAvatar.setImageBitmap(getAvatarBitmapFromPath(group.getGroupAvatarImgPath()));
                    }
                    btnAddGroup.setText(getString(R.string.action_group_update));
                    toolbar.setTitle(getString(R.string.title_activity_group_update));

                    isUpdateGroup = true;
                }
            }

            callingAction = callingIntent.getAction();

        }

        groupNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnAddGroup.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(groupNameView.getWindowToken(), 0);

                    attempAddGroup();

                    return true;
                }
                return false;
            }
        });
    }

    private void handleOnBackPress() {
        this.finish();
    }

    private void getControlWidget() {
        toolbar = (Toolbar) findViewById(R.id.add_group_toolbar);
        imgGroupAvatar = (ImageView) findViewById(R.id.imgGroupAvatar);
        groupNameView = (EditText) findViewById(R.id.add_group_name);
        btnAddGroup = (Button) findViewById(R.id.btnAddGroup);
        mGroupRegisterFormView = findViewById(R.id.add_new_group_form);
        mProgressView = findViewById(R.id.add_group_progress);
        add_group_banner = (LinearLayout) findViewById(R.id.add_group_banner);
    }

    private void attempAddGroup() {
        groupNameView.setError(null);
        boolean cancel = false;
        View focusView = null;

        groupName = groupNameView.getText().toString();

        if (groupName.isEmpty()) {
            groupNameView.setError(getString(R.string.error_field_required));
            focusView = groupNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            groupDetail = new GroupDetail(groupName, groupInputAvatar, groupInputAvatarImgPath);
            mGroupAddTask = new GroupAddTask();
            mGroupAddTask.execute(groupDetail);
        }
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mGroupRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mGroupRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mGroupRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public class GroupAddTask extends AsyncTask<GroupDetail, Void, ComonUtils.ADD_GROUP_RESULT> {

        @Override
        protected ComonUtils.ADD_GROUP_RESULT doInBackground(GroupDetail... params) {
            boolean res = false;

            GroupDetail groupDetail = params[0];

            GroupDetailServerControl groupDetailServerControl = new GroupDetailServerControl(getApplicationContext());
            GroupDetailControl groupDetailControl = new GroupDetailControl(getApplicationContext());

            if (isUpdateGroup) {
                groupDetail.setGroupId(updateGroupId);
                ArrayList<GroupDetail> list = new ArrayList<GroupDetail>();
                list.add(groupDetail);
                if (!groupDetailServerControl.updateGroupToServer(list)) {
                    return ComonUtils.ADD_GROUP_RESULT.FAILED_COMMON;
                }

                if (!groupDetailControl.updateGroup(groupDetail)) {
                    return ComonUtils.ADD_GROUP_RESULT.FAILED_COMMON;
                }
            } else {

                if (groupDetailServerControl.checkGroupExistInServer(groupDetail.getGroupName())) {
                    return ComonUtils.ADD_GROUP_RESULT.FAILED_GROUP_EXISTED;
                }

                int groupId = groupDetailServerControl.getNextGroupTblId();
                groupDetail.setGroupId(groupId);

                //add group to server
                if (!groupDetailServerControl.saveGroupToServer(groupDetail)) {
                    return ComonUtils.ADD_GROUP_RESULT.FAILED_COMMON;
                }

                //add group to DB
                groupDetailControl.addGroup(groupDetail);

                //////////////////////////////////////////////////////
                //add type detail in DB and server
                //default is 2: income/outcome
                TypeDetaillControl typeDetaillDB = new TypeDetaillControl(getApplicationContext());
                TypeDetailServerControl typeDetailServer = new TypeDetailServerControl(getApplicationContext());

                int typeGroupId = groupDetailControl.findGroupByName(groupName).getGroupId();
                int typeId1 = typeDetailServer.getNextTypeDetailTblId();
                int typeId2 = typeDetailServer.getNextTypeDetailTblId();

                ArrayList<TypeDetail> types = new ArrayList<TypeDetail>();
                TypeDetail t1 = new TypeDetail(typeId1, getResources().getString(R.string.data_type_income), typeGroupId);
                TypeDetail t2 = new TypeDetail(typeId2, getResources().getString(R.string.data_type_outcome), typeGroupId);

                //add to DB
                typeDetaillDB.addTypeDetail(t1);
                typeDetaillDB.addTypeDetail(t2);

                //add to server
                t1.setTypeId(typeId1);
                types.add(t1);

                t2.setTypeId(typeId2);
                types.add(t2);

                if (!typeDetailServer.saveTypeDetailToServer(types)) {
                    return ComonUtils.ADD_GROUP_RESULT.FAILED_COMMON;
                }
            }

            return ComonUtils.ADD_GROUP_RESULT.SUCCESS;
        }

        @Override
        protected void onPostExecute(final ComonUtils.ADD_GROUP_RESULT res) {
            mGroupAddTask = null;
            showProgress(false);

            if (res == ComonUtils.ADD_GROUP_RESULT.SUCCESS) {
                if (isUpdateGroup) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ComonUtils.UPDATE_GROUP_BUNDLE_RESULT, groupDetail);
                    intent.putExtra(ComonUtils.UPDATE_GROUP_INTENT_RESULT, bundle);
                    setResult(ComonUtils.CODE_UPDATE_GROUP, intent);
                    Toast.makeText(getApplicationContext(), "Update group sucessfully", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ComonUtils.NEW_GROUP_BUNDLE, groupDetail);
                    intent.putExtra(ComonUtils.NEW_GROUP_INTENT, bundle);
                    if (callingAction != null && callingAction.equals(ComonUtils.ACTION_ADD_GROUP_FROM_MAIN)) {
                        setResult(ComonUtils.CODE_ADD_GROUP_FROM_MAIN, intent);
                    } else if (callingAction != null && callingAction.equals(ComonUtils.ACTION_ADD_GROUP_FROM_GROUP_LIST)) {
                        setResult(ComonUtils.CODE_ADD_GROUP_FROM_GROUP_LIST, intent);
                    } else {
                        setResult(ComonUtils.CODE_ADD_GROUP, intent);
                    }
                    Toast.makeText(getApplicationContext(), "Add group sucessfully", Toast.LENGTH_LONG).show();
                }

                AppConfig.saveGroupInfoToSharePreference(groupDetail);

                finish();
            } else if (res == ComonUtils.ADD_GROUP_RESULT.FAILED_GROUP_EXISTED) {
                groupNameView.setError(getString(R.string.add_group_name_exist));
                groupNameView.requestFocus();
            } else if (res == ComonUtils.ADD_GROUP_RESULT.FAILED_COMMON) {
                groupNameView.setError(getString(R.string.add_group_fail_common));
                groupNameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mGroupAddTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (requestCode == ComonUtils.CODE_SELECT_AVATAR_FROM_GROUP) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = ComonUtils.getCorrectlyOrientedImage(this, selectedImage);
                imgGroupAvatar.setImageBitmap(bitmap);

                File savedFile = new File(getExternalCacheDir(), ComonUtils.createNewCacheGroupFileName());
                try {
                    FileOutputStream out = new FileOutputStream(savedFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();

                    groupInputAvatarImgPath = savedFile.getAbsolutePath();
                } catch (Exception e) {
                    Log.e("Image", "Convert");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        toolbar.setBackgroundColor(AppConfig.getThemeColor());

        add_group_banner.setBackgroundColor(AppConfig.getAlpha1BackgroundColorSetting());
        btnAddGroup.setTextColor(getColor(AppConfig.current_theme_setting_color));
        groupNameView.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));

        AppConfig.changeRoundViewColor(btnAddGroup);
        AppConfig.changeRoundViewColor(imgGroupAvatar);
//        actionBar.setBackgroundDrawable(new ColorDrawable(AppConfig.getThemeColor()));
    }

    private void processChooseAvatar() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.setPackage("com.sec.android.gallery3d");
                    photoPickerIntent.putExtra("return-data", true);
                    photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    photoPickerIntent.putExtra("noFaceDetection", true);
                    startActivityForResult(photoPickerIntent, ComonUtils.CODE_SELECT_AVATAR_FROM_GROUP);

                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private Bitmap getAvatarBitmapFromPath(String imgPath) {
        File image = new File(imgPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;

        File file = new File(image.getPath());
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(image.getPath(), bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) getResources().getDimension(R.dimen.img_header_main_icon_width), (int) getResources().getDimension(R.dimen.img_header_main_icon_width), true);
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.muoipt.ffm.R;
import com.muoipt.ffm.control.GroupDetailControl;
import com.muoipt.ffm.control.GroupDetailServerControl;
import com.muoipt.ffm.control.UserDetailControl;
import com.muoipt.ffm.control.UserDetailCustomServerControl;
import com.muoipt.ffm.control.UserDetailServerControl;
import com.muoipt.ffm.model.GroupDetail;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private UserSignupTask mSignupTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mGroupView;
    private View mProgressView;
    private View mSignupFormView;
    private Button registerButton;
    private LinearLayout banner_sign_up;
    private ImageView imgUserAvatar;
    private ScrollView scrollView_signup;
    private RelativeLayout layout_scroll_view;
    private View viewEmpty;

    private Context mContext;
    private String signupEmail;
    private String signupPassword;
    private String signupGroup;
    private int signupStatus = ComonUtils.USER_STATUS_NEW;
    private int signupRole = ComonUtils.USER_ROLE_NORMAL;
    private int groupId = -1;
    private boolean isAdmin = false;
    private boolean isFromAdmin = false;
    private boolean isAdminUpdate = false;
    private String userInputAvatarImgPath = "";
    private int userInputAvatar = -1;

    private UserDetail signUpUser;
    private UserDetail userViewProfile;
    private Toolbar toolbar;
    private ImageView imgNewGroup;
    private GroupDetailServerControl groupDetailServerControl;
    private GroupDetailControl groupDetailControl;

    private boolean isFromSetting = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getControlWidget();

        groupDetailServerControl = new GroupDetailServerControl(getApplicationContext());
        groupDetailControl = new GroupDetailControl(getApplicationContext());

        imgNewGroup.setImageDrawable(getDrawable(R.drawable.icon_plus));
        imgUserAvatar.setImageDrawable(getDrawable(R.drawable.icon_default_camera_large));
        userInputAvatar = R.drawable.icon_default_camera_large;
        userInputAvatarImgPath = null;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getDrawable(R.drawable.back_icon_36dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });

        if (getIntent() != null) {
            Intent intent = getIntent();
            Bundle bundleAddAdmin = intent.getBundleExtra(ComonUtils.SIGN_UP_ADMIN_INTENT);
            if (bundleAddAdmin != null) {
                groupId = bundleAddAdmin.getInt(ComonUtils.SIGN_UP_ADMIN_BUNDLE);
                GroupDetail groupDetail = groupDetailControl.getGroupDetailById(groupId);
                if (groupDetail != null) {
                    mGroupView.setText(groupDetail.getGroupName() + "");
                    mGroupView.setEnabled(false);
                    if (groupDetail.getGroupAvatarImgPath() != null && !groupDetail.getGroupAvatarImgPath().equals(""))
                        imgNewGroup.setImageBitmap(getAvatarBitmapFromPath(groupDetail.getGroupAvatarImgPath()));
                    imgNewGroup.setEnabled(false);
                    isAdmin = true;
                }
            }

            if (ComonUtils.ACTION_ADD_USER_FROM_SETTING.equals(intent.getAction())) {
                isFromSetting = true;
            }

            Bundle bundleAddMemberByAdmin = intent.getBundleExtra(ComonUtils.SIGN_UP_USER_FROM_ADMIN_INTENT);
            if (bundleAddMemberByAdmin != null) {
                groupId = bundleAddMemberByAdmin.getInt(ComonUtils.SIGN_UP_USER_FROM_ADMIN_BUNDLE);
                GroupDetail groupDetail = groupDetailControl.getGroupDetailById(groupId);
                if (groupDetail != null) {
                    mGroupView.setText(groupDetail.getGroupName() + "");
                    mGroupView.setEnabled(false);
                    if (groupDetail.getGroupAvatarImgPath() != null && !groupDetail.getGroupAvatarImgPath().equals(""))
                        imgNewGroup.setImageBitmap(getAvatarBitmapFromPath(groupDetail.getGroupAvatarImgPath()));
                    imgNewGroup.setEnabled(false);
                    isFromAdmin = true;
                }
            }

            ///////////
            Bundle bundleOpenProfile = intent.getBundleExtra(ComonUtils.OPEN_USER_PROFILE_INTENT);
            if (bundleOpenProfile != null) {
                userViewProfile = (UserDetail) bundleOpenProfile.getSerializable(ComonUtils.OPEN_USER_PROFILE_BUNDLE);
                if (userViewProfile != null) {
                    initViewProfile();
                }
            }
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userViewProfile == null) {
                    attemptSignUp();
                } else if (userViewProfile != null && AppConfig.getUserLogInInfor().getUserEmail().equals(userViewProfile.getUserEmail())) {
                    attemptSignUp();
                } else {
                    finish();
                }
            }
        });

        imgNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processNewGroup();
            }
        });

        imgUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processChooseAvatar();
            }
        });

        mEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                if (hasFocus) {
                    processSoftKeyboard(true, mEmailView);
                }
            }
        });

        mEmailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSoftKeyboard(true, mEmailView);
            }
        });

        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                if (hasFocus) {
                    processSoftKeyboard(true, mPasswordView);
                }
            }
        });

        mPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSoftKeyboard(true, mPasswordView);
            }
        });

        mPasswordView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean isGroupEnable = mGroupView.isEnabled();

                if (actionId == EditorInfo.IME_ACTION_NEXT && isGroupEnable) {
                    mGroupView.requestFocus();
                    processSoftKeyboard(true, mGroupView);
                    return true;
                } else if (actionId == EditorInfo.IME_ACTION_NEXT && !isGroupEnable) {
                    registerButton.requestFocus();
                    processSoftKeyboard(false, registerButton);
                    return true;
                }
                return false;
            }
        });

        mGroupView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                if (hasFocus) {
                    processSoftKeyboard(true, mGroupView);
                }
            }
        });

        mGroupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSoftKeyboard(true, mGroupView);
            }
        });

        mGroupView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerButton.requestFocus();
                    processSoftKeyboard(true, registerButton);

                    if (userViewProfile == null) {
                        attemptSignUp();
                    } else if (userViewProfile != null && AppConfig.getUserLogInInfor().getUserEmail().equals(userViewProfile.getUserEmail())) {
                        attemptSignUp();
                    } else {
                        finish();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void getControlWidget() {
        toolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        imgNewGroup = (ImageView) findViewById(R.id.imgNewGroup);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_signup);
        mPasswordView = (EditText) findViewById(R.id.password_signup);
        mGroupView = (EditText) findViewById(R.id.group_signup);
        mSignupFormView = findViewById(R.id.email_signup_form);
        mProgressView = findViewById(R.id.signup_progress);
        registerButton = (Button) findViewById(R.id.sign_up_button);
        banner_sign_up = (LinearLayout) findViewById(R.id.banner_sign_up);
        imgUserAvatar = (ImageView) findViewById(R.id.imgUserAvatar);
        scrollView_signup = (ScrollView) findViewById(R.id.scrollView_signup);
        layout_scroll_view = (RelativeLayout) findViewById(R.id.layout_scroll_view);
        viewEmpty = findViewById(R.id.viewEmpty);
    }

    private void handleOnBackPress() {
        this.finish();
    }

    private void initViewProfile() {
        UserDetail currentUser = AppConfig.getUserLogInInfor();
        GroupDetail currentGroup = AppConfig.getCurrentGroup();

        if (currentUser.getUserId() != userViewProfile.getUserId()) {
            mEmailView.setEnabled(false);
            mPasswordView.setVisibility(View.GONE);
            mGroupView.setVisibility(View.GONE);
            imgUserAvatar.setEnabled(false);
            imgNewGroup.setVisibility(View.GONE);
            registerButton.setText(getString(R.string.string_close));
            toolbar.setTitle("View member");

            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams)registerButton.getLayoutParams();

            params.addRule(RelativeLayout.BELOW, R.id.layout_email);
            params.topMargin = (int)(getResources().getDimension(R.dimen.dp_10));
            registerButton.setLayoutParams(params);

            registerButton.setLayoutParams(params);


        } else {
            registerButton.setText(getString(R.string.string_update));
            mPasswordView.setText(userViewProfile.getUserPassword());
            mGroupView.setText(currentGroup.getGroupName());
            if (currentGroup.getGroupAvatarImgPath() != null && !currentGroup.getGroupAvatarImgPath().equals(""))
                imgNewGroup.setImageBitmap(getAvatarBitmapFromPath(currentGroup.getGroupAvatarImgPath()));
            isAdminUpdate = true;
            toolbar.setTitle("Update profile");
        }

        toolbar.setTitle(getString(R.string.user_view));
        mEmailView.setText(userViewProfile.getUserEmail());
        mGroupView.setEnabled(false);
        imgNewGroup.setEnabled(false);
        String userAvatarImgPath = userViewProfile.getUserAvatarImgPath();
        if (userAvatarImgPath != null && !userAvatarImgPath.equals("")) {
            imgUserAvatar.setImageBitmap(getAvatarBitmapFromPath(userAvatarImgPath));
            userInputAvatarImgPath = userAvatarImgPath;
        }

//        processSoftKeyboard(false, registerButton);
        mEmailView.clearFocus();
        mEmailView.setError(null);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);

    }

    private void attemptSignUp() {
        if (mSignupTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mGroupView.setError(null);

        // Store values at the time of the login attempt.
        signupEmail = mEmailView.getText().toString();
        signupPassword = mPasswordView.getText().toString();
        signupGroup = mGroupView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(signupEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!TextUtils.isEmpty(signupEmail) && !ComonUtils.isValidEmail(signupEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(signupPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!TextUtils.isEmpty(signupPassword) && !ComonUtils.isPasswordValid(signupPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid group, if the user entered one.
        if (TextUtils.isEmpty(signupGroup)) {
            mGroupView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            processSoftKeyboard(false, focusView);
        } else {
            showProgress(true);
            getInputUserDetail();
            mSignupTask = new UserSignupTask();
            mSignupTask.execute();
        }
    }

    private void getInputUserDetail() {

        //truong hop admin tao group truoc sau do moi add user vao sau
        if (AppConfig.getUserLogInInfor().getUserEmail() == null && AppConfig.getCurrentGroup().getGroupId() != -1) {
            signupRole = ComonUtils.USER_ROLE_ADMIN; //create group is admin role
            signupStatus = ComonUtils.USER_STATUS_NORMAL;
        }

        signUpUser = new UserDetail(signupEmail, signupPassword, userInputAvatar, userInputAvatarImgPath, signupStatus, signupRole);
    }

    class UserSignupTask extends AsyncTask<Void, Void, ComonUtils.SIGN_UP_RESULT> {

        @Override
        protected ComonUtils.SIGN_UP_RESULT doInBackground(Void... param) {

            //validate user data
            UserDetailServerControl userDetailServerControl = new UserDetailServerControl(getApplicationContext());
            UserDetailCustomServerControl userDetailCustomServerControl = new UserDetailCustomServerControl(getApplicationContext());
            int userId = 0;
            UserDetailControl userDetailDBControl = new UserDetailControl(getApplicationContext());

            if (!isAdminUpdate) {
                if (userDetailCustomServerControl.checkDataUserExistInServer(signUpUser.getUserEmail())) {
                    return ComonUtils.SIGN_UP_RESULT.FAILED_EMAIL_EXIST;
                }

                if (groupId == -1) {
                    GroupDetailServerControl groupDetailServerControl = new GroupDetailServerControl(getApplicationContext());
                    GroupDetail groupDetail = groupDetailServerControl.getGroupServerDataByName(signupGroup);
                    if (groupDetail == null) {
                        return ComonUtils.SIGN_UP_RESULT.FAILED_GROUP_NOT_EXIST;
                    }
                    groupId = groupDetail.getGroupId();
                }
                signUpUser.setUserGroupId(groupId);

                userId = userDetailCustomServerControl.getNextUserTblId();
                signUpUser.setUserId(userId);

                //save data to server
                if (!userDetailServerControl.addSignUpUserData(signUpUser)) {
                    return ComonUtils.SIGN_UP_RESULT.FAILED_COMMON;
                }

                if (!userDetailCustomServerControl.addSignUpUserData(signUpUser)) {
                    return ComonUtils.SIGN_UP_RESULT.FAILED_COMMON;
                }

                //save data to DB
                userDetailDBControl.addUser(signUpUser);
            } else {
                userId = userViewProfile.getUserId();
                signUpUser.setUserId(userId);
                signUpUser.setUserStatus(userViewProfile.getUserStatus());
                signUpUser.setUserRole(userViewProfile.getUserRole());
                signUpUser.setUserGroupId(userViewProfile.getUserGroupId());
                signUpUser.setUserAvatar(userViewProfile.getUserAvatar());

                //save data to server
                if (!userDetailServerControl.updateUserToServer(signUpUser)) {
                    return ComonUtils.SIGN_UP_RESULT.FAILED_UPDATE_NOT_CHANGED;
                }

                ArrayList<UserDetail> list = new ArrayList<UserDetail>();
                list.add(signUpUser);
                if (!userDetailCustomServerControl.updateUserToServer(list)) {
                    return ComonUtils.SIGN_UP_RESULT.FAILED_COMMON;
                }

                //save data to DB
                userDetailDBControl.updateUser(signUpUser);
            }

            //test sending email
//            SendSimpleMessage();

            return ComonUtils.SIGN_UP_RESULT.SUCCESS;
        }

        @Override
        protected void onPostExecute(final ComonUtils.SIGN_UP_RESULT result) {
            mSignupTask = null;
            showProgress(false);

            if (result == ComonUtils.SIGN_UP_RESULT.SUCCESS) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ComonUtils.SIGN_UP_BUNDLE, signUpUser);
                intent.putExtra(ComonUtils.SIGN_UP_INTENT, bundle);
                if (isAdmin) {
                    setResult(ComonUtils.CODE_ADD_ADMIN, intent);
                    AppConfig.saveUserInfoToSharePreference(signUpUser);
                } else if (isFromAdmin) {
                    setResult(ComonUtils.CODE_ADD_MEMBER_BY_ADMIN, intent);
                } else if (isAdminUpdate) {
                    setResult(ComonUtils.CODE_UPDATE_ADMIN, intent);
                    AppConfig.saveUserInfoToSharePreference(signUpUser);
                } else if (isFromSetting) {
                    AppConfig.saveUserInfoToSharePreference(signUpUser);
                    setResult(ComonUtils.CODE_SIGN_UP, intent);
                } else {
                    setResult(ComonUtils.CODE_SIGN_UP, intent);
                }

                finish();

            } else if (result == ComonUtils.SIGN_UP_RESULT.FAILED_EMAIL_EXIST) {
                mEmailView.setError(getString(R.string.error_sign_up_email_exist));
                mEmailView.requestFocus();
                processSoftKeyboard(false, mEmailView);

            } else if (result == ComonUtils.SIGN_UP_RESULT.FAILED_GROUP_NOT_EXIST) {
                mGroupView.setError(getString(R.string.error_sign_up_group_not_exist));
                mGroupView.requestFocus();
                processSoftKeyboard(false, mGroupView);
            } else if (result == ComonUtils.SIGN_UP_RESULT.FAILED_UPDATE_NOT_CHANGED) {
//                mGroupView.setError(getString(R.string.error_update_not_change));
//                mGroupView.requestFocus();
//                processSoftKeyboard(false, mGroupView);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mSignupTask = null;
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mSignupFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

        processSoftKeyboard(false, mProgressView);
    }

    private void processNewGroup() {
        //kiem tra neu chua co thong tin group thi tao moi
        //neu co roi thi update
        if (mGroupView.getText().toString() != null && !mGroupView.getText().toString().equals("")) {
            GroupDetail groupDetail = groupDetailControl.findGroupByName(mGroupView.getText().toString());
            Bundle bundle = new Bundle();
            bundle.putSerializable(ComonUtils.UPDATE_GROUP_BUNDLE, groupDetail);
            Intent intent = new Intent(this, GroupAddNewActivity.class);
            intent.putExtra(ComonUtils.UPDATE_GROUP_INTENT, bundle);
            startActivityForResult(intent, ComonUtils.CODE_UPDATE_GROUP);

        } else {
            Intent intent = new Intent(this, GroupAddNewActivity.class);
            startActivityForResult(intent, ComonUtils.CODE_ADD_GROUP);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        GroupDetail currentGroup = AppConfig.getCurrentGroup();
        if (currentGroup.getGroupName() != null && !currentGroup.getGroupName().equals("")) {
            mGroupView.setText(currentGroup.getGroupName());
        }
        if (currentGroup.getGroupAvatarImgPath() != null && !currentGroup.getGroupAvatarImgPath().equals("")) {
            imgNewGroup.setImageBitmap(getAvatarBitmapFromPath(currentGroup.getGroupAvatarImgPath()));
        }
        toolbar.setBackgroundColor(AppConfig.getThemeColor());

        banner_sign_up.setBackgroundColor(AppConfig.getAlpha1BackgroundColorSetting());
        mEmailView.setBackgroundTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));
        mPasswordView.setBackgroundTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));
        mGroupView.setBackgroundTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));
        mGroupView.setCompoundDrawableTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));

        registerButton.setTextColor(AppConfig.getThemeColor());

        AppConfig.changeRoundViewColor(registerButton);
        AppConfig.changeRoundViewColor(imgUserAvatar);
        AppConfig.changeRoundViewColor(imgNewGroup);
    }

    private void processChooseAvatar() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
                    startActivityForResult(photoPickerIntent, ComonUtils.CODE_SELECT_AVATAR_FROM_USER);

                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ComonUtils.CODE_SELECT_AVATAR_FROM_USER) {
            if (data == null) return;
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = ComonUtils.getCorrectlyOrientedImage(this, selectedImage);
                imgUserAvatar.setImageBitmap(bitmap);

                File savedFile = new File(getExternalCacheDir(), ComonUtils.createNewCacheUserFileName());
                try {
                    FileOutputStream out = new FileOutputStream(savedFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();

                    userInputAvatarImgPath = savedFile.getAbsolutePath();
                } catch (Exception e) {
                    Log.e("Image", "Convert");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        switch (resultCode) {
            case ComonUtils.CODE_ADD_GROUP:
                Bundle bundle1 = data.getBundleExtra(ComonUtils.NEW_GROUP_INTENT);
                GroupDetail groupDetail = (GroupDetail) bundle1.getSerializable(ComonUtils.NEW_GROUP_BUNDLE);
                if (groupDetail != null) {
                    String groupName = groupDetail.getGroupName();
                    int groupAvatar = groupDetail.getGroupAvatar();
                    String groupAvatarImgPath = groupDetail.getGroupAvatarImgPath();

                    if (groupAvatar != -1) {
                        imgNewGroup.setImageDrawable(getDrawable(groupAvatar));
                        userInputAvatar = groupAvatar;
                    } else if (groupAvatarImgPath != null && !groupAvatarImgPath.equals("")) {
                        imgNewGroup.setImageBitmap(getAvatarBitmapFromPath(groupAvatarImgPath));
                        userInputAvatar = -1;
                    }
                    mGroupView.setText(groupName);
                    signupRole = ComonUtils.USER_ROLE_ADMIN; //create group is admin role
                    signupStatus = ComonUtils.USER_STATUS_NORMAL;
                    processSoftKeyboard(false, registerButton);
                    registerButton.requestFocus();
                }
                break;
            case ComonUtils.CODE_UPDATE_GROUP:
                Bundle bundle2 = data.getBundleExtra(ComonUtils.UPDATE_GROUP_INTENT_RESULT);
                GroupDetail groupDetail2 = (GroupDetail) bundle2.getSerializable(ComonUtils.UPDATE_GROUP_BUNDLE_RESULT);
                if (groupDetail2 != null) {
                    String groupName = groupDetail2.getGroupName();
                    int groupAvatar = groupDetail2.getGroupAvatar();
                    String groupAvatarImgPath = groupDetail2.getGroupAvatarImgPath();

                    if (groupAvatar != -1) {
                        imgNewGroup.setImageDrawable(getDrawable(groupAvatar));
                        userInputAvatar = groupAvatar;
                    } else if (groupAvatarImgPath != null && groupAvatarImgPath != "") {
                        imgNewGroup.setImageBitmap(getAvatarBitmapFromPath(groupAvatarImgPath));
                        userInputAvatar = -1;
                    }
                    mGroupView.setText(groupName);
                    signupRole = ComonUtils.USER_ROLE_ADMIN; //update group is admin role
                    signupStatus = ComonUtils.USER_STATUS_NORMAL;
                    processSoftKeyboard(false, registerButton);
                    registerButton.requestFocus();
                }
                break;
        }

        resetError();
    }

    private void resetError(){
        if (mPasswordView.getError() != null) {
            mPasswordView.setError(null);
            mPasswordView.clearFocus();
        }

        if (mEmailView.getError() != null) {
            mEmailView.setError(null);
            mEmailView.clearFocus();
        }

        if (mGroupView.getError() != null) {
            mGroupView.setError(null);
            mGroupView.clearFocus();
        }

        registerButton.requestFocus();
    }

    private Bitmap getAvatarBitmapFromPath(String imgPath) {
        File image = new File(imgPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) getResources().getDimension(R.dimen.img_header_main_icon_width), (int) getResources().getDimension(R.dimen.img_header_main_icon_width), true);

        return bitmap;
    }

    public void setAvatarBitmap(Bitmap avatarBitmap) {
        imgUserAvatar.setImageBitmap(avatarBitmap);
    }

    private void processSoftKeyboard(boolean show, View view) {
        if (show) {
            if (view == mEmailView || view == mPasswordView || view == registerButton) {
                layout_scroll_view.scrollTo(0, 0);
                viewEmpty.setVisibility(View.VISIBLE);
            } else if (view == mGroupView) {
                layout_scroll_view.scrollTo(0, 0);
                layout_scroll_view.scrollTo(0, (int) getResources().getDimension(R.dimen.dp_200));
            }

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);

        } else {
            layout_scroll_view.scrollTo(0, 0);
            viewEmpty.setVisibility(View.GONE);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

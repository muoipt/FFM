package com.muoipt.ffm.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseUser;
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

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    private TextView txtForgotPass, txtSignup;

    private Context mContext;
    private String logInEmail;
    private String logInPassword;
    private UserDetail logInUser;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mContext = this;
        getControlWidget();

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

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        txtSignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                processSignUp();
            }
        });

        txtForgotPass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                processResetPassword();
            }
        });


    }

    private void getControlWidget() {
        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        txtForgotPass = (TextView) findViewById(R.id.txt_forgot_pass);
        txtSignup = (TextView) findViewById(R.id.txt_sign_up);
    }

    private void handleOnBackPress() {
        this.finish();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        logInEmail = mEmailView.getText().toString();
        logInPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(logInEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!TextUtils.isEmpty(logInEmail) && !ComonUtils.isValidEmail(logInEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(logInPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!TextUtils.isEmpty(logInPassword) && !ComonUtils.isPasswordValid(logInPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(logInEmail, logInPassword);
            mAuthTask.execute((Void) null);
        }
    }


    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            UserDetailServerControl control = new UserDetailServerControl(getApplicationContext());
            if (!control.logIn(mEmail, mPassword)) {
                return false;
            }

            //save log in data to DB
            saveLogInDataToDb();

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(ComonUtils.LOG_IN_BUNDLE, logInUser);
                intent.putExtra(ComonUtils.LOG_IN_INTENT, bundle);

                setResult(ComonUtils.CODE_LOG_IN, intent);

                Toast.makeText(mContext, "Log in sucessfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void processSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, ComonUtils.CODE_SIGN_UP);
    }

    private void saveLogInDataToDb() {
        //get user data
        UserDetailCustomServerControl userDetailCustomServerControl = new UserDetailCustomServerControl(this);
        UserDetailControl userDetailDbControl = new UserDetailControl(this);
        logInUser = userDetailCustomServerControl.getLogInUserFromServer(logInEmail);
        if (logInUser == null)
            return;

        logInUser.setUserEmail(logInEmail);
        logInUser.setUserPassword(logInPassword);

        //save group to DB
        GroupDetailControl groupDetailDBControl = new GroupDetailControl(this);
        GroupDetailServerControl groupDetailServerControl = new GroupDetailServerControl(this);
        GroupDetail group = groupDetailServerControl.getLogInGroupFromServer(logInUser);

        if (!groupDetailDBControl.checkDataGroupExistInDb(group.getGroupName())) {
            groupDetailDBControl.addGroup(group);
        }

        //save user to db
        if (!userDetailDbControl.checkDataUserExistInDb(logInUser.getUserEmail())) {
            userDetailDbControl.addUser(logInUser);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ComonUtils.CODE_SIGN_UP:
                if (data != null) {
                    Bundle bundle = data.getBundleExtra(ComonUtils.SIGN_UP_INTENT);
                    UserDetail signUpUser = (UserDetail) bundle.getSerializable(ComonUtils.SIGN_UP_BUNDLE);
                    mEmailView.setText(signUpUser.getUserEmail());
                    mPasswordView.setText(signUpUser.getUserPassword());
                }
                break;
        }
    }

    private void processResetPassword() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivityForResult(intent, ComonUtils.CODE_FORGOT_PASSWORD);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        toolbar.setBackgroundColor(AppConfig.getThemeColor());
        AppConfig.changeRoundViewColor(mEmailSignInButton);
        mEmailView.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        mPasswordView.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));

//        actionBar.setBackgroundDrawable(new ColorDrawable(AppConfig.getThemeColor()));
    }

    public boolean autoLogInUser(String email, String password){

        UserDetailServerControl control = new UserDetailServerControl(this);
        if (!control.logIn(email, password)) {
            return false;
        }

        return true;
    }
}


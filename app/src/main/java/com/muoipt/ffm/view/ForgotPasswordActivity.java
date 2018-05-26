package com.muoipt.ffm.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.muoipt.ffm.R;
import com.muoipt.ffm.control.UserDetailServerControl;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edit_email;
    private Button btn_reset_pass;
    private TextView txt_reset_result_hint;
    private View mProgressView;
    private String email;
    private ResetPasswordTask mTask = null;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getControlWidget();

        toolbar = (Toolbar) findViewById(R.id.forgot_password_toolbar);
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
        toolbar.setTitle(getString(R.string.title_activity_reset_password));

        btn_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptResetPassword();
            }
        });
    }

    private void getControlWidget() {
        edit_email = (EditText) findViewById(R.id.edit_reset_email);
        btn_reset_pass = (Button) findViewById(R.id.btn_reset_password);
        mProgressView = findViewById(R.id.reset_progress);
        txt_reset_result_hint = (TextView) findViewById(R.id.txt_reset_result_hint);
    }

    private void attemptResetPassword() {
        if (mTask != null) {
            return;
        }

        edit_email.setError(null);
        email = edit_email.getText().toString();

        if (email.isEmpty() || email.equals("")) {
            edit_email.setError(getString(R.string.error_field_required));
            edit_email.requestFocus();
        } else if (!ComonUtils.isValidEmail(email)) {
            edit_email.setError(getString(R.string.error_invalid_email));
            edit_email.requestFocus();
        } else {
            processResetPassword();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void processResetPassword() {
        displayAlertDlg(3);

    }

    public class ResetPasswordTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {

//            boolean res = true;

//            ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if(e !=  null){
//                        Toast.makeText(getApplicationContext(), "Reset failed, try again", Toast.LENGTH_SHORT).show();
//                        res[0] = false;
//                    } else{
//                    }
//                }
//            });

            try {
                ParseUser.requestPasswordReset(email);
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }

//            return res[0];
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTask = null;
            showProgress(false);

            if (success) {
                txt_reset_result_hint.setVisibility(View.VISIBLE);
//                finish();
            } else {
                displayAlertDlg(3);
            }
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(AppConfig.getThemeColor());
        AppConfig.changeRoundViewColor(btn_reset_pass);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void displayAlertDlg(final int item) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sync_layout);
        dialog.show();

        ImageView img_sync = dialog.findViewById(R.id.img_sync);
        img_sync.setImageResource(R.drawable.error_icon);
        TextView txt_message = dialog.findViewById(R.id.txt_message);

        if (item == 0) {
            txt_message.setText(getString(R.string.alert_login_info));
        } else if (item == 1) {
            txt_message.setText(getString(R.string.alert_internet_info));
        } else if (item == 2) {
            txt_message.setText(getString(R.string.alert_user_status_info));
        } else if (item == 3) {
            txt_message.setText(getString(R.string.alert_function_not_support_info));
        }

        Button buttonOK = dialog.findViewById(R.id.btn_sync_ok);
        AppConfig.changeRoundViewColor(buttonOK);
        buttonOK.setTextColor(AppConfig.getThemeColor());
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item == 0){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, ComonUtils.CODE_LOG_IN);
                }
                dialog.dismiss();
            }
        });
    }

    private void handleOnBackPress() {
        this.finish();
    }
}

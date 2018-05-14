package com.xalenmy.ffm.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.xalenmy.ffm.R;
import com.xalenmy.ffm.control.UserDetailServerControl;
import com.xalenmy.ffm.utils.AppConfig;
import com.xalenmy.ffm.utils.ComonUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edit_email;
    private Button btn_reset_pass;
    private TextView txt_reset_result_hint;
    private View mProgressView;
    private String email;
    private ResetPasswordTask mTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getControlWidget();

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
        showProgress(true);

        mTask = new ResetPasswordTask();
        mTask.execute((Void) null);

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
                Toast.makeText(getApplicationContext(), "Reset failed, try again", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppConfig.changeRoundViewColor(btn_reset_pass);
    }
}

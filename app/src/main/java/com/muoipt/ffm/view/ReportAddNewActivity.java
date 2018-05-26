package com.muoipt.ffm.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muoipt.ffm.R;
import com.muoipt.ffm.control.CategoryDetailControl;
import com.muoipt.ffm.control.MainSummaryControl;
import com.muoipt.ffm.control.ReportDetailControl;
import com.muoipt.ffm.control.ReportDetailServerControl;
import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.ReportDetail;
import com.muoipt.ffm.model.ReportDetailDisplay;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;

//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ReportAddNewActivity extends AppCompatActivity {

    private AddNewReportTask addNewReportTask;

    private EditText editReportTitle, editReportAmount, editReportNotes;
    private TextView txtDivide;
    private Button btnReportCategory, btnCancel, btnOk;
    private List<CategoryDetail> arrCategories;
    private ProgressBar mProgressView;
    private View AddReportFormView;

    private String title, category, notes;
    private int amount;
    private ReportDetail reportDetail;
    private ReportDetailDisplay selectedReportDetailDisplay;
    private boolean isUpdateReport = false;
    private Toolbar toolbar;
    private InputMethodManager imm;
    private LinearLayout layout_scroll_report;

    public ReportAddNewActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_add_new);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getControlWidget();
        initComponent();

        CategoryDetailControl categoryDetailControl = new CategoryDetailControl(this);
        arrCategories = categoryDetailControl.getAllCategoryDetailFromDBByUser();

        btnReportCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListCategory();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(editReportNotes.getWindowToken(),0);
                attemptAddReport();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initComponent() {
        processGetUpdateReport();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (MainReportFragment.selectedCategoryName != null) {
            btnReportCategory.setText(MainReportFragment.selectedCategoryName);
        }

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

        editReportTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    imm.hideSoftInputFromWindow(editReportTitle.getWindowToken(), 0);
                    btnReportCategory.requestFocus();
                    showListCategory();
                    return true;
                }


                return false;
            }
        });

        editReportAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    editReportNotes.requestFocus();
                    layout_scroll_report.scrollTo(0, (int)getResources().getDimension(R.dimen.dp_100));
                }

                return false;
            }
        });

        editReportNotes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    imm.hideSoftInputFromWindow(editReportNotes.getWindowToken(),0);
                    attemptAddReport();
                }
                return false;
            }
        });

    }

    private void handleOnBackPress() {
        this.finish();
    }

    private void processGetUpdateReport() {
        Intent callingIntent = getIntent();

        if (callingIntent != null) {
            Bundle bundle1 = callingIntent.getBundleExtra(ComonUtils.UPDATE_REPORT_INTENT);
            if (bundle1 != null) {
                selectedReportDetailDisplay = (ReportDetailDisplay) bundle1.getSerializable(ComonUtils.UPDATE_REPORT_BUNDLE);
            }

            Bundle bundle2 = callingIntent.getBundleExtra(ComonUtils.OPEN_REPORT_SEARCH_INTENT);
            if (bundle2 != null) {
                selectedReportDetailDisplay = (ReportDetailDisplay) bundle2.getSerializable(ComonUtils.OPEN_REPORT_SEARCH_BUNDLE);
            }
        }

        if (selectedReportDetailDisplay != null) {
            editReportTitle.setText(selectedReportDetailDisplay.getReportTitle());
            editReportAmount.setText(selectedReportDetailDisplay.getReportAmount() + "");
            editReportNotes.setText(selectedReportDetailDisplay.getReportNote());
            btnReportCategory.setText(selectedReportDetailDisplay.getCategoryName());
            isUpdateReport = true;
            toolbar.setTitle(getString(R.string.title_activity_report_update));
        }
    }

    private void getControlWidget() {
        toolbar = (Toolbar) findViewById(R.id.add_report_toolbar);
        editReportTitle = (EditText) findViewById(R.id.editAddReportTitle);
        editReportAmount = (EditText) findViewById(R.id.editAddReportAmount);
        editReportNotes = (EditText) findViewById(R.id.editAddReportNote);
        btnReportCategory = (Button) findViewById(R.id.btnListCategoryName);
        mProgressView = (ProgressBar) findViewById(R.id.add_report_progress);
        btnCancel = (Button) findViewById(R.id.btnAddReportCancel);
        btnOk = (Button) findViewById(R.id.btnAddReportOk);
        AddReportFormView = findViewById(R.id.add_new_report_form);
        layout_scroll_report = (LinearLayout)findViewById(R.id.layout_scroll_report);
    }

    private void showListCategory() {
        CategoryDetailControl categoryDetailControl = new CategoryDetailControl(this);
        final ArrayList<String> categoryNames = categoryDetailControl.getAllCategoryNameFromDBByUser();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_listview_category_name);
        dialog.show();

        ListView listView = (ListView) dialog.findViewById(R.id.listCatNameAddReport);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, categoryNames);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String catName = categoryNames.get(position);
                btnReportCategory.setText(catName);
                dialog.dismiss();
                editReportAmount.requestFocus();
                imm.showSoftInput(editReportAmount,InputMethodManager.SHOW_FORCED);
            }
        });
    }

    private void attemptAddReport() {
        if (addNewReportTask != null) {
            return;
        }

        editReportTitle.setError(null);
        editReportAmount.setError(null);
        editReportNotes.setError(null);
        btnReportCategory.setError(null);

        title = editReportTitle.getText().toString();
        category = btnReportCategory.getText().toString();

        notes = editReportNotes.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (category.equals(getResources().getString(R.string.add_new_report_category_text))) {
            btnReportCategory.setError(getString(R.string.error_field_required));
            focusView = btnReportCategory;
            cancel = true;
        }

        try {
            amount = Integer.parseInt(editReportAmount.getText().toString());
        } catch (Exception ex) {
            editReportAmount.setError(getString(R.string.error_field_format));
            focusView = editReportAmount;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (isUpdateReport) {
                if (title.equals(selectedReportDetailDisplay.getReportTitle()) &&
                        amount == selectedReportDetailDisplay.getReportAmount() &&
                        category.equals(selectedReportDetailDisplay.getCategoryName()) &&
                        notes.equals(selectedReportDetailDisplay.getReportNote())) {
                    //nothing upate
                    finish();
                } else {
                    startExecuteTask();
                }
            } else {
                startExecuteTask();
            }
        }
    }

    private void startExecuteTask() {
        showProgress(true);
        reportDetail = new ReportDetail();
        reportDetail.setReportAmount(amount);
        reportDetail.setReportTitle(title);
        reportDetail.setReportNote(notes);
        reportDetail.setReportMark(false);
        reportDetail.setReportDeleted(false);

        addNewReportTask = new AddNewReportTask();
        addNewReportTask.execute((Void) null);
    }

    class AddNewReportTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            CategoryDetailControl categoryDetailControl = new CategoryDetailControl(getApplicationContext());
            int catId = categoryDetailControl.getCategoryIdFromName(category);
            reportDetail.setReportCatId(catId);

            ReportDetailControl reportDetailControl = new ReportDetailControl(getApplicationContext());

            if (!isUpdateReport) {
                ReportDetailServerControl reportDetailServerControl = new ReportDetailServerControl(getApplicationContext());
                int reportId = reportDetailServerControl.getNextReportDetailTblId();
                reportDetail.setReportId(reportId);

                if (reportDetailControl.addReport(reportDetail)) {
                    return true;
                }
            } else {
                reportDetail.setReportId(selectedReportDetailDisplay.getReportId());
                reportDetail.setReportDatetime(selectedReportDetailDisplay.getReportDatetime());

                if (reportDetailControl.updateReport(reportDetail)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            addNewReportTask = null;
            showProgress(false);

            if (aBoolean) {
                resetInput();

                MainReportFragment.selectedCategoryName = category;

                if (!isUpdateReport) {
                    Toast.makeText(getBaseContext(), "Add report successful", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ComonUtils.OPEN_REPORT_SEARCH_BUNDLE_RESULT, reportDetail);
                    intent.putExtra(ComonUtils.OPEN_REPORT_SEARCH_INTENT_RESULT, bundle);
                    setResult(ComonUtils.CODE_EDIT_REPORT_SEARCH, intent);

                    Toast.makeText(getBaseContext(), "Update report successful", Toast.LENGTH_LONG).show();
                }
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Add report failed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            addNewReportTask = null;
            showProgress(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        AddReportFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        AddReportFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                AddReportFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    private void resetInput() {
        editReportTitle.setText("");
        editReportAmount.setText("");
        editReportNotes.setText("");
        btnReportCategory.setText(getResources().getString(R.string.add_new_report_category_text));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        toolbar.setBackgroundColor(AppConfig.getThemeColor());
        editReportTitle.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        editReportNotes.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        editReportAmount.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));

        AppConfig.changeRoundViewColor(btnReportCategory);

        ((ProgressBar)findViewById(R.id.add_report_progress))
                .getIndeterminateDrawable()
                .setColorFilter(AppConfig.getThemeColor(), PorterDuff.Mode.SRC_IN);
    }
}

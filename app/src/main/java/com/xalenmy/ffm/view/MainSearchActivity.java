package com.xalenmy.ffm.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.Image;
import android.media.Ringtone;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xalenmy.ffm.R;
import com.xalenmy.ffm.adapter.MainSummaryRecycleViewAdapter;
import com.xalenmy.ffm.adapter.MemberListRecycleViewAdapter;
import com.xalenmy.ffm.adapter.ReportDetailDisplayRecycleViewAdapter;
import com.xalenmy.ffm.control.MainSummaryControl;
import com.xalenmy.ffm.control.ReportDetailDisplayControl;
import com.xalenmy.ffm.control.UserDetailControl;
import com.xalenmy.ffm.eventmodel.GroupListMemberActivityEventObject;
import com.xalenmy.ffm.eventmodel.MainSearchActivityEventObj;
import com.xalenmy.ffm.eventmodel.MainSummaryFragmentEventInteger;
import com.xalenmy.ffm.listener.CustomTouchListener;
import com.xalenmy.ffm.listener.OnRecycleViewItemClickListener;
import com.xalenmy.ffm.model.CategoryDetail;
import com.xalenmy.ffm.model.MainSummaryReport;
import com.xalenmy.ffm.model.ReportDetail;
import com.xalenmy.ffm.model.ReportDetailDisplay;
import com.xalenmy.ffm.model.UserDetail;
import com.xalenmy.ffm.utils.AppConfig;
import com.xalenmy.ffm.utils.ComonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editSearchText;
    private ScrollView scrollViewBody;
    private LinearLayout inputKeySearchLayout, layoutListSearchResult;
    private RelativeLayout layoutSearchUserResult, layoutSearchCatResult, layoutSearchReportResult;
    private ImageButton btnExtentUserList, btnExtentCatList, btnExtentReportList;
    private TextView txtDiv, txtDiv1, txtDiv2, txtDiv3, txtDiv4, txtDiv5;
    private RecyclerView lstviewSearchUserResult, lstviewSearchCatResult, lstviewSearchReportResult;
    private List<UserDetail> arrTotalUsers, arrUserResults;
    private List<MainSummaryReport> arrTotalCats, arrCatResults;
    private List<ReportDetailDisplay> arrTotalReports, arrReportResults;
    private MemberListRecycleViewAdapter lstviewSearchUserAdapter;
    private MainSummaryRecycleViewAdapter lstviewSearchCatAdapter;
    private ReportDetailDisplayRecycleViewAdapter lstviewSearchReportAdapter;
    private UserDetailControl userDetailControl;
    private MainSummaryControl mainSummaryControl;
    private ReportDetailDisplayControl reportDetailControl;

    private String currentText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getControlWidget();

        initControl();

        editSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                processSearchByKeyword();
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateResults();
            }
        });

        layoutListSearchResult.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                processTouchResult();
                return true;
            }
        });
    }

    private void getControlWidget() {
        inputKeySearchLayout = (LinearLayout)findViewById(R.id.input_key_search_layout);
        editSearchText = (EditText) findViewById(R.id.edit_search_text);
        scrollViewBody = (ScrollView) findViewById(R.id.scrollView_body);
        layoutListSearchResult = (LinearLayout) findViewById(R.id.layout_list_search_result);
        layoutSearchUserResult = (RelativeLayout) findViewById(R.id.layout_search_user_result);
        layoutSearchCatResult = (RelativeLayout) findViewById(R.id.layout_search_cat_result);
        layoutSearchReportResult = (RelativeLayout) findViewById(R.id.layout_search_report_result);
        btnExtentUserList = (ImageButton) findViewById(R.id.btnExtentUserList);
        btnExtentCatList = (ImageButton) findViewById(R.id.btnExtentCatList);
        btnExtentReportList = (ImageButton) findViewById(R.id.btnExtentReportList);
        txtDiv = (TextView) findViewById(R.id.txtDiv);
        txtDiv1 = (TextView) findViewById(R.id.txtDiv1);
        txtDiv2 = (TextView) findViewById(R.id.txtDiv2);
        txtDiv3 = (TextView) findViewById(R.id.txtDiv3);
        txtDiv4 = (TextView) findViewById(R.id.txtDiv4);
        txtDiv5 = (TextView) findViewById(R.id.txtDiv5);
        lstviewSearchUserResult = (RecyclerView) findViewById(R.id.lst_search_user_result);
        lstviewSearchCatResult = (RecyclerView) findViewById(R.id.lst_search_cat_result);
        lstviewSearchReportResult = (RecyclerView) findViewById(R.id.lst_search_report_result);

        arrTotalUsers = new ArrayList<UserDetail>();
        arrUserResults = new ArrayList<UserDetail>();
        arrTotalCats = new ArrayList<MainSummaryReport>();
        arrCatResults = new ArrayList<MainSummaryReport>();
        arrTotalReports = new ArrayList<ReportDetailDisplay>();
        arrReportResults = new ArrayList<ReportDetailDisplay>();

    }

    private void initControl() {
        userDetailControl = new UserDetailControl(this);
        mainSummaryControl = new MainSummaryControl(this);
        reportDetailControl = new ReportDetailDisplayControl(this);

        //init default display all data

        ///////////////////USER/////////////////
        UserDetail currentUser = AppConfig.getUserLogInInfor();

        arrTotalUsers = userDetailControl.getActiveUsersFromDB(currentUser);
        arrUserResults = userDetailControl.getActiveUsersFromDB(currentUser);
        if(arrTotalUsers.size()>0) {
            lstviewSearchUserAdapter = new MemberListRecycleViewAdapter(arrTotalUsers, this);
            lstviewSearchUserResult.setAdapter(lstviewSearchUserAdapter);
            lstviewSearchUserResult.setLayoutManager(new LinearLayoutManager(this));
            lstviewSearchUserAdapter.notifyDataSetChanged();
        }
        //////////////////CATEGORY///////////////
        arrTotalCats = mainSummaryControl.getMainSummaryReportByUserFromDB(currentUser);
        arrCatResults = mainSummaryControl.getMainSummaryReportByUserFromDB(currentUser);
        if(arrTotalCats.size()>0) {
            lstviewSearchCatAdapter = new MainSummaryRecycleViewAdapter(arrTotalCats, this);
            lstviewSearchCatResult.setAdapter(lstviewSearchCatAdapter);
            lstviewSearchCatResult.setLayoutManager(new LinearLayoutManager(this));
            lstviewSearchCatAdapter.notifyDataSetChanged();
        }

        //////////////////REPORT//////////////////
        arrTotalReports = reportDetailControl.getReportDetailDisplayFromDBByUser(currentUser);
        arrReportResults = reportDetailControl.getReportDetailDisplayFromDBByUser(currentUser);
        if(arrTotalReports.size()>0) {
            lstviewSearchReportAdapter = new ReportDetailDisplayRecycleViewAdapter(arrTotalReports, this);
            lstviewSearchReportResult.setAdapter(lstviewSearchReportAdapter);
            lstviewSearchReportResult.setLayoutManager(new LinearLayoutManager(this));
            lstviewSearchReportAdapter.notifyDataSetChanged();
        }

        layoutSearchUserResult.setOnClickListener(this);
        layoutSearchCatResult.setOnClickListener(this);
        layoutSearchReportResult.setOnClickListener(this);

        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_search_user_result:
                processExtendUserList();
                break;
            case R.id.layout_search_cat_result:
                processExtendCatList();
                break;
            case R.id.layout_search_report_result:
                processExtendReportList();
                break;
        }
    }

    private void processTouchResult() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void processExtendUserList() {
        if (lstviewSearchUserResult.getVisibility() == View.VISIBLE) {
            lstviewSearchUserResult.setVisibility(View.GONE);
            btnExtentUserList.setBackgroundResource(R.drawable.ic_arrow_drop_down_black);
        } else {
            lstviewSearchUserResult.setVisibility(View.VISIBLE);
            btnExtentUserList.setBackgroundResource(R.drawable.ic_arrow_drop_up_black);
        }
    }

    private void processExtendCatList() {
        if (lstviewSearchCatResult.getVisibility() == View.VISIBLE) {
            lstviewSearchCatResult.setVisibility(View.GONE);
            btnExtentCatList.setBackgroundResource(R.drawable.ic_arrow_drop_down_black);
        } else {
            lstviewSearchCatResult.setVisibility(View.VISIBLE);
            btnExtentCatList.setBackgroundResource(R.drawable.ic_arrow_drop_up_black);
        }
    }

    private void processExtendReportList() {
        if (lstviewSearchReportResult.getVisibility() == View.VISIBLE) {
            lstviewSearchReportResult.setVisibility(View.GONE);
            btnExtentReportList.setBackgroundResource(R.drawable.ic_arrow_drop_down_black);
        } else {
            lstviewSearchReportResult.setVisibility(View.VISIBLE);
            btnExtentReportList.setBackgroundResource(R.drawable.ic_arrow_drop_up_black);
        }
    }

    private void processSearchByKeyword() {
        currentText = editSearchText.getText().toString();

        prepareSearch();

        for (int i = 0; i < arrTotalUsers.size(); i++) {
            UserDetail userDetail = arrTotalUsers.get(i);
            if (currentText.equals("")) {
                arrUserResults.add(userDetail);
            } else if (userDetail.getUserEmail().contains(currentText)) {
                arrUserResults.add(userDetail);
            }
        }

        for (int i = 0; i < arrTotalCats.size(); i++) {
            MainSummaryReport summaryReport = arrTotalCats.get(i);
            if (currentText.equals("")) {
                arrCatResults.add(summaryReport);
            } else if (summaryReport.getCategory().contains(currentText)) {
                arrCatResults.add(summaryReport);
            } else if (String.valueOf(summaryReport.getAmount()).contains(currentText)) {
                arrCatResults.add(summaryReport);
            }
        }

        for (int i = 0; i < arrTotalReports.size(); i++) {
            ReportDetailDisplay reportDetail = arrTotalReports.get(i);
            if (currentText.equals("")) {
                arrReportResults.add(reportDetail);
            } else if (reportDetail.getCategoryName().contains(currentText)) {
                arrReportResults.add(reportDetail);
            } else if (reportDetail.getReportNote().contains(currentText)) {
                arrReportResults.add(reportDetail);
            } else if (reportDetail.getReportTitle().contains(currentText)) {
                arrReportResults.add(reportDetail);
            } else if (String.valueOf(reportDetail.getReportAmount()).contains(currentText)) {
                arrReportResults.add(reportDetail);
            }
        }
    }

    private void prepareSearch() {
        arrUserResults.clear();
        arrCatResults.clear();
        arrReportResults.clear();

    }

    private void updateResults() {
        if (arrUserResults.size() == 0) {
            layoutSearchUserResult.setVisibility(View.GONE);
            lstviewSearchUserResult.setVisibility(View.GONE);
            txtDiv1.setVisibility(View.GONE);
            txtDiv4.setVisibility(View.GONE);
        } else {
            layoutSearchUserResult.setVisibility(View.VISIBLE);
            lstviewSearchUserResult.setVisibility(View.VISIBLE);
            txtDiv1.setVisibility(View.VISIBLE);
            txtDiv4.setVisibility(View.VISIBLE);
            lstviewSearchUserAdapter = new MemberListRecycleViewAdapter(arrUserResults, this);
            lstviewSearchUserResult.setAdapter(lstviewSearchUserAdapter);
            lstviewSearchUserResult.setLayoutManager(new LinearLayoutManager(this));
            lstviewSearchUserAdapter.notifyDataSetChanged();
        }

        if (arrCatResults.size() == 0) {
            layoutSearchCatResult.setVisibility(View.GONE);
            lstviewSearchCatResult.setVisibility(View.GONE);
            txtDiv2.setVisibility(View.GONE);
        } else {
            layoutSearchCatResult.setVisibility(View.VISIBLE);
            lstviewSearchCatResult.setVisibility(View.VISIBLE);
            txtDiv2.setVisibility(View.VISIBLE);
            lstviewSearchCatAdapter = new MainSummaryRecycleViewAdapter(arrCatResults, this);
            lstviewSearchCatResult.setAdapter(lstviewSearchCatAdapter);
            lstviewSearchCatAdapter.notifyDataSetChanged();
        }

        if (arrReportResults.size() == 0) {
            layoutSearchReportResult.setVisibility(View.GONE);
            lstviewSearchReportResult.setVisibility(View.GONE);
            txtDiv3.setVisibility(View.GONE);
            txtDiv5.setVisibility(View.GONE);
        } else {
            layoutSearchReportResult.setVisibility(View.VISIBLE);
            lstviewSearchReportResult.setVisibility(View.VISIBLE);
            txtDiv3.setVisibility(View.VISIBLE);
            txtDiv5.setVisibility(View.VISIBLE);
            lstviewSearchReportAdapter = new ReportDetailDisplayRecycleViewAdapter(arrReportResults, this);
            lstviewSearchReportResult.setAdapter(lstviewSearchReportAdapter);
            lstviewSearchReportAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void processViewUserProfile(GroupListMemberActivityEventObject obj) {
        UserDetail userDetail = obj.getObj();

        Intent intent = new Intent(this, SignUpActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(ComonUtils.OPEN_USER_PROFILE_BUNDLE, userDetail);
        intent.putExtra(ComonUtils.OPEN_USER_PROFILE_INTENT, bundle);

        startActivity(intent);

    }

    private void refreshUserList() {
        arrUserResults.clear();

        UserDetail currentUser = AppConfig.getUserLogInInfor();
        arrTotalUsers.clear();
        arrTotalUsers = userDetailControl.getActiveUsersFromDB(currentUser);

        currentText = editSearchText.getText().toString();

        for (int i = 0; i < arrTotalUsers.size(); i++) {
            UserDetail user = arrTotalUsers.get(i);
            if (currentText.equals("")) {
                arrUserResults.add(user);
            } else if (user.getUserEmail().contains(currentText)) {
                arrUserResults.add(user);
            }
        }

        if (arrUserResults.size() == 0) {
            layoutSearchUserResult.setVisibility(View.GONE);
            lstviewSearchUserResult.setVisibility(View.GONE);
        } else {
            layoutSearchUserResult.setVisibility(View.VISIBLE);
            lstviewSearchUserResult.setVisibility(View.VISIBLE);
            lstviewSearchUserAdapter = new MemberListRecycleViewAdapter(arrUserResults, this);
            lstviewSearchUserResult.setAdapter(lstviewSearchUserAdapter);
            lstviewSearchUserAdapter.notifyDataSetChanged();
        }
    }

    private void refreshCatList() {
        arrCatResults.clear();

        UserDetail currentUser = AppConfig.getUserLogInInfor();
        arrTotalCats.clear();
        arrTotalCats = mainSummaryControl.getMainSummaryReportByUserFromDB(currentUser);

        currentText = editSearchText.getText().toString();

        for (int i = 0; i < arrTotalCats.size(); i++) {
            MainSummaryReport summaryReport = arrTotalCats.get(i);
            if (currentText.equals("")) {
                arrCatResults.add(summaryReport);
            } else if (summaryReport.getCategory().contains(currentText)) {
                arrCatResults.add(summaryReport);
            } else if (String.valueOf(summaryReport.getAmount()).contains(currentText)) {
                arrCatResults.add(summaryReport);
            }
        }

        if (arrCatResults.size() == 0) {
            layoutSearchCatResult.setVisibility(View.GONE);
            lstviewSearchCatResult.setVisibility(View.GONE);
        } else {
            layoutSearchCatResult.setVisibility(View.VISIBLE);
            lstviewSearchCatResult.setVisibility(View.VISIBLE);
            lstviewSearchCatAdapter = new MainSummaryRecycleViewAdapter(arrCatResults, this);
            lstviewSearchCatResult.setAdapter(lstviewSearchCatAdapter);
            lstviewSearchCatAdapter.notifyDataSetChanged();
        }
    }

    private void refreshReportList() {
        arrReportResults.clear();

        UserDetail currentUser = AppConfig.getUserLogInInfor();
        arrTotalReports.clear();
        arrTotalReports = reportDetailControl.getReportDetailDisplayFromDBByUser(currentUser);

        currentText = editSearchText.getText().toString();

        for (int i = 0; i < arrTotalReports.size(); i++) {
            ReportDetailDisplay report = arrTotalReports.get(i);
            if (currentText.equals("")) {
                arrReportResults.add(report);
            } else if (report.getReportTitle().contains(currentText)) {
                arrReportResults.add(report);
            } else if (String.valueOf(report.getReportAmount()).contains(currentText)) {
                arrReportResults.add(report);
            } else if(report.getReportNote().contains(currentText)){
                arrReportResults.add(report);
            } else if(report.getCategoryName().contains(currentText)){
                arrReportResults.add(report);
            }
        }

        if (arrReportResults.size() == 0) {
            layoutSearchReportResult.setVisibility(View.GONE);
            lstviewSearchReportResult.setVisibility(View.GONE);
        } else {
            layoutSearchReportResult.setVisibility(View.VISIBLE);
            lstviewSearchReportResult.setVisibility(View.VISIBLE);
            lstviewSearchReportAdapter = new ReportDetailDisplayRecycleViewAdapter(arrReportResults, this);
            lstviewSearchReportResult.setAdapter(lstviewSearchReportAdapter);
            lstviewSearchReportAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ComonUtils.CODE_EDIT_CAT_SEARCH:
                refreshCatList();
                break;
            case ComonUtils.CODE_EDIT_REPORT_SEARCH:
                refreshReportList();
                break;
        }
    }

    @Subscribe
    public void processEditCategory(MainSummaryFragmentEventInteger data) {
        int position = data.getI();

        Intent intent = new Intent(this, CategoryAddNewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ComonUtils.OPEN_CAT_SEARCH_BUNDLE, arrCatResults.get(position));
        intent.putExtra(ComonUtils.OPEN_CAT_SEARCH_INTENT, bundle);

        startActivityForResult(intent, ComonUtils.CODE_EDIT_CAT_SEARCH);
    }

    @Subscribe
    public void processEditUser(MainSearchActivityEventObj obj){
        if(obj.getMsg().equals(ComonUtils.EDIT_USER_MSG)){
            UserDetail userDetail = obj.getUserDetail();
            userDetail.setUserStatus(ComonUtils.USER_STATUS_REMOVED);
            userDetailControl.updateUser(userDetail);
            refreshUserList();
        } else if(obj.getMsg().equals(ComonUtils.EDIT_REPORT_MSG)){
            ReportDetailDisplay reportDetailDisplay = obj.getReportDetailDisplay();

            Intent intent = new Intent(this, ReportAddNewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ComonUtils.OPEN_REPORT_SEARCH_BUNDLE, reportDetailDisplay);
            intent.putExtra(ComonUtils.OPEN_REPORT_SEARCH_INTENT, bundle);

            startActivityForResult(intent, ComonUtils.CODE_EDIT_REPORT_SEARCH);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        inputKeySearchLayout.setBackgroundColor(AppConfig.getThemeColor());
        layoutSearchUserResult.setBackgroundColor(AppConfig.getAlpha1BackgroundColorSetting());
        layoutSearchCatResult.setBackgroundColor(AppConfig.getAlpha1BackgroundColorSetting());
        layoutSearchReportResult.setBackgroundColor(AppConfig.getAlpha1BackgroundColorSetting());
        editSearchText.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        editSearchText.setForegroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        txtDiv1.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        txtDiv2.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        txtDiv3.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        txtDiv.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        txtDiv4.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        txtDiv5.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
    }
}

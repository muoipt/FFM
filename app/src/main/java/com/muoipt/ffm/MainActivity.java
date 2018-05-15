package com.muoipt.ffm;

import android.*;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.muoipt.ffm.adapter.CatListviewAdapter;
import com.muoipt.ffm.control.CategoryDetailControl;
import com.muoipt.ffm.control.CategoryDetailServerControl;
import com.muoipt.ffm.control.DataIdServerControl;
import com.muoipt.ffm.control.GroupDetailControl;
import com.muoipt.ffm.control.GroupDetailServerControl;
import com.muoipt.ffm.control.ReportDetailControl;
import com.muoipt.ffm.control.ReportDetailServerControl;
import com.muoipt.ffm.control.TypeDetailServerControl;
import com.muoipt.ffm.control.TypeDetaillControl;
import com.muoipt.ffm.control.UserDetailControl;
import com.muoipt.ffm.eventmodel.GroupImgPathEventObj;
import com.muoipt.ffm.eventmodel.MainEventObject;
import com.muoipt.ffm.eventmodel.MainEventString;
import com.muoipt.ffm.eventmodel.MainReportFragmentEventString;
import com.muoipt.ffm.eventmodel.MainSummaryFragmentEventString;
import com.muoipt.ffm.listener.IFabClickListener;
import com.muoipt.ffm.listener.MainReportFragmentFabClickImpl;
import com.muoipt.ffm.listener.MainSummaryFragmentFabClickImpl;
import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.GroupDetail;
import com.muoipt.ffm.model.MainSummaryReport;
import com.muoipt.ffm.model.ReportDetail;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.service.SyncService;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;
import com.muoipt.ffm.utils.SyncUtils;
import com.muoipt.ffm.view.AboutActivity;
import com.muoipt.ffm.view.CategoryAddNewActivity;
import com.muoipt.ffm.view.ChartViewActivity;
import com.muoipt.ffm.view.GroupAddNewActivity;
import com.muoipt.ffm.view.GroupListMemberActivity;
import com.muoipt.ffm.view.LoginActivity;
import com.muoipt.ffm.view.MainReportFragment;
import com.muoipt.ffm.view.MainSearchActivity;
import com.muoipt.ffm.view.MainSettingActivity;
import com.muoipt.ffm.view.MainSummaryFragment;
import com.muoipt.ffm.view.ReportAddNewActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainSummaryFragment.OnMainSummaryFragmentInteractionListener,
        MainReportFragment.OnMainReportFragmentInteractionListener, View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private View navHeaderView;
    private LinearLayout navBodyView;
    private ImageView imageViewHeaderMainIcon = null;
    private TextView txtHeaderMainGroupName, txtHeaderMainUserEmail;
    private LinearLayout navCat, navGroup, navChart, navSetting, navAbout;
    private ListView navCatList;
    private ImageButton imgBtnExtentList;
    private TextView txtDivider3, txtDivider4, txtDivider6;
    private TextView catNumber, txtUserNumber;
    private DrawerLayout mDrawerLayout;

    private ArrayList<CategoryDetail> catlist;
    private CatListviewAdapter catlistAdapter;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private UserDetail currentUser;
    private GroupDetail currentGroup;
    private GroupDetailControl groupDetailDBControl;
    private GroupDetailServerControl groupDetailServerControl;
    private UserDetailControl userDetailControl;
    private boolean isNoNeedToRefreshMainSummaryRecyle = true;
    private boolean isNoNeedToRefreshMainReportRecyle = true;
    private boolean isNoNeedToRefreshcatlist = true;
    private int viewPagerPos = 0;
    private SyncReceiver syncReceiver;

    private FloatingActionButton fab;

    private boolean internetConnected = false;
    private BroadcastReceiver internetConnectionReceiver = null;

    private CategoryDetailControl categoryDetailControl;

    private IFabClickListener iFabClickListener;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getControlWidget();

        setSupportActionBar(toolbar);
        toolbar.setMinimumHeight(150);
        toolbar.setCollapsible(false);
        collapsingToolbarLayout.setMinimumHeight(250);

        navCat.setOnClickListener(this);
        imgBtnExtentList.setOnClickListener(this);
        navGroup.setOnClickListener(this);
        navChart.setOnClickListener(this);
        navSetting.setOnClickListener(this);
        navAbout.setOnClickListener(this);
        fab.setOnClickListener(this);

        if (imageViewHeaderMainIcon != null)
            imageViewHeaderMainIcon.setOnClickListener(this);

        initControl();

        navigationView.setNavigationItemSelectedListener(this);

        setupViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPagerPos = position;
//                refreshRecycleViewMainSummary();
//                refreshRecycleViewMainReport();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);

        setupTabs();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        categoryDetailControl = new CategoryDetailControl(getApplicationContext());
        catlist = categoryDetailControl.getAllCategoryDetailFromDBByUser();

        if (catlist != null) {
            catlistAdapter = new CatListviewAdapter(this, R.layout.content_important_category_list, catlist);
            navCatList.setAdapter(catlistAdapter);
            catlistAdapter.notifyDataSetChanged();
            if (catlist.size() > 0) {
                imgBtnExtentList.setVisibility(View.VISIBLE);
            } else {
                imgBtnExtentList.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && viewPagerPos == 1) {
            viewPager.setCurrentItem(0);
            MainReportFragment.selectedCategoryName = null;
            EventBus.getDefault().post(new MainReportFragmentEventString(ComonUtils.ACTION_SHOW_REPORT));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (syncReceiver != null) {
            unregisterReceiver(syncReceiver);
            unregisterReceiver(internetConnectionReceiver);
        }

        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    private void updateDateInDBForTest() {

        String dateNotToday = "";
        String dateNotWeek = "2017-03-03";
        String dateNotMonth = "";

        String type = getString(R.string.data_type_income);

        ReportDetailControl reportDetailControl = new ReportDetailControl(this);
        ArrayList<ReportDetail> reportDetails = reportDetailControl.getReportDetailDBdataByUser();

        for (int i = 0; i < reportDetails.size(); i++) {
            reportDetails.get(i).setReportDatetime(dateNotWeek);
            reportDetailControl.updateReport(reportDetails.get(i));
        }

    }

    private void getControlWidget() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_member_list_layout);
        toolbar = (Toolbar) findViewById(R.id.app_bar_main_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.app_bar_main_tabs);
        viewPager = (ViewPager) findViewById(R.id.app_bar_viewpager);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
            imageViewHeaderMainIcon = (ImageView) navHeaderView.findViewById(R.id.imageViewHeaderMainIcon);
            txtHeaderMainGroupName = (TextView) navHeaderView.findViewById(R.id.txtHeaderMainGroupName);
            txtHeaderMainUserEmail = (TextView) navHeaderView.findViewById(R.id.txtHeaderMainUserEmail);
        } else {
            navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main_land);
            txtHeaderMainGroupName = (TextView) navHeaderView.findViewById(R.id.txtHeaderMainGroupName_land);
            txtHeaderMainUserEmail = (TextView) navHeaderView.findViewById(R.id.txtHeaderMainUserEmail_land);
        }

        navBodyView = (LinearLayout) navigationView.findViewById(R.id.nav_body_view);
        navCat = navBodyView.findViewById(R.id.layout_nav_cat);
        navCatList = (ListView) navBodyView.findViewById(R.id.lst_nav_cat);
        navGroup = navBodyView.findViewById(R.id.layout_nav_group);
        navChart = navBodyView.findViewById(R.id.layout_nav_chart);
        navSetting = navBodyView.findViewById(R.id.layout_nav_setting);
        navAbout = navBodyView.findViewById(R.id.layout_nav_about);
        imgBtnExtentList = navBodyView.findViewById(R.id.imgBtnExtentList);
        txtDivider3 = (TextView) navBodyView.findViewById(R.id.txtDivider3);
        txtDivider4 = (TextView) navBodyView.findViewById(R.id.txtDivider4);
        txtDivider6 = (TextView) navBodyView.findViewById(R.id.txtDivider6);
        catNumber = (TextView) navBodyView.findViewById(R.id.catNumber);
        txtUserNumber = (TextView) navBodyView.findViewById(R.id.txtUserNumber);

        fab = (FloatingActionButton) findViewById(R.id.mainFab);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    private Bitmap getAvatarBitmapFromPath(String imgPath) {
        File image = new File(imgPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) getResources().getDimension(R.dimen.img_header_main_icon_width), (int) getResources().getDimension(R.dimen.img_header_main_icon_width), true);

        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initControl() {
        groupDetailDBControl = new GroupDetailControl(this);
        groupDetailServerControl = new GroupDetailServerControl(this);
        userDetailControl = new UserDetailControl(this);

        syncReceiver = new SyncReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ComonUtils.SYNC_ACTION);
        registerReceiver(syncReceiver, intentFilter);

        internetConnectionReceiver = new InternetConnectionReceiver();

        IntentFilter intentFilterNetworkConnection = new IntentFilter();
        intentFilterNetworkConnection.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetConnectionReceiver, intentFilterNetworkConnection);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (imageViewHeaderMainIcon != null)
            imageViewHeaderMainIcon.setImageDrawable(getApplicationContext().getDrawable(R.drawable.icon_default_camera_large));

//        UserDetail currentUser = AppConfig.getUserLogInInfor();

//        updateDateInDBForTest();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    private void processAddNewCategory() {
        Intent intent = new Intent(this, CategoryAddNewActivity.class);
        startActivityForResult(intent, ComonUtils.CODE_ADD_CATEGORY);
    }

    private void processAddNewReport() {
        Intent intent = new Intent(this, ReportAddNewActivity.class);
        startActivityForResult(intent, ComonUtils.CODE_ADD_REPORT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            processSearch();
            return true;
        }

        if (id == R.id.action_signIn) {
            processSignIn();
            return true;
        }
        if (id == R.id.action_sync) {
            processSync();
            return true;
        }
        if (id == R.id.action_Delete) {
//            return deleteAllServerData() && deleteAllDBData();

            if(currentUser.getUserEmail() == null ){
                displayAlertDlg(0);
            } else {
                if (viewPagerPos == 0) {
                    processDeleteSelectedCategories();
                } else if (viewPagerPos == 1) {
                    processDeleteSelectedReport();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(new MainSummaryFragment());
        fragments.add(new MainReportFragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(super.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setupTabs() {
        TextView tab1 = (TextView) LayoutInflater.from(this).inflate(R.layout.main_custom_tab, null);
        tab1.setText(getResources().getString(R.string.main_textview_summary));
        tab1.setWidth((int) getResources().getDimension(R.dimen.tab_max_width));

        tabLayout.getTabAt(0).setCustomView(tab1);

        TextView tab2 = (TextView) LayoutInflater.from(this).inflate(R.layout.main_custom_tab, null);
        tab2.setText(getResources().getString(R.string.main_textview_report));
        tab2.setWidth((int) getResources().getDimension(R.dimen.tab_max_width));

        tabLayout.getTabAt(1).setCustomView(tab2);
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
//        if (AppConfig.getUserLogInInfor().getUserEmail() == null) {
//            return;
//        }

        switch (v.getId()) {
            case R.id.layout_nav_cat:
            case R.id.imgBtnExtentList:
                processShowHideCatlistView();
                break;
            case R.id.layout_nav_group:
                processListGroup();
                break;
            case R.id.layout_nav_chart:
                processViewChart();
                break;
            case R.id.layout_nav_setting:
                processSettings();
                break;
            case R.id.layout_nav_about:
                processDisplayFFMInfo();
                break;
            case R.id.mainFab:
                if (currentUser.getUserEmail() != null && !currentUser.getUserEmail().equals("")) {
                    if (viewPagerPos == 0) {
                        iFabClickListener = new MainSummaryFragmentFabClickImpl();
                    } else {
                        iFabClickListener = new MainReportFragmentFabClickImpl();
                    }
                    iFabClickListener.onFabClick(this);
                } else {
                    displayAlertDlg(0);
                }
                break;
            case R.id.imageViewHeaderMainIcon:
                processMyGroup();
//                processChangeGroupAvatar();
                break;
            default:
                break;
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        public int pos = 0;

        private List<Fragment> myFragments;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> myFrags) {
            super(fm);
            myFragments = myFrags;
        }

        @Override
        public Fragment getItem(int position) {
            return myFragments.get(position);
        }

        @Override
        public int getCount() {
            return myFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            setPos(position);
            String PageTitle = "";

            switch (pos) {
                case 0:
                    PageTitle = "SUMMARY";
                    break;
                case 1:
                    PageTitle = "REPORT";
                    break;
            }
            return PageTitle;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int position) {
            pos = position;
        }
    }

    @Override
    public void onMainSummaryFragmentInteraction(MainSummaryReport selectedCat) {
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onMainReportFragmentInteraction(int position) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        int a = 1;
    }

    private boolean deleteAllServerData() {
        if (ParseUser.getCurrentUser() == null) {
            SyncUtils.connectAutoUserToParseServer();
        }

        DataIdServerControl dataIdServerControl = new DataIdServerControl(this);
        boolean res1 = dataIdServerControl.deleteIdDataServer();

        ReportDetailServerControl reportDetailServerControl = new ReportDetailServerControl(this);
        boolean res2 = reportDetailServerControl.deleteAllReportData();

        CategoryDetailServerControl categoryDetailServerControl = new CategoryDetailServerControl(this);
        boolean res3 = categoryDetailServerControl.deleteAllCategoryServer();

        TypeDetailServerControl typeDetailServerControl = new TypeDetailServerControl(this);
        boolean res4 = typeDetailServerControl.deleteAllTypeServer();

//        UserDetailCustomServerControl userDetailServerControl = new UserDetailCustomServerControl(this);
//        boolean res5 = userDetailServerControl.deleteAllUserServer();
//        boolean res6 = userDetailServerControl.deleteAllUserSessionServer();

        GroupDetailServerControl groupDetailServerControl = new GroupDetailServerControl(this);
        boolean res7 = groupDetailServerControl.deleteAllGroupServer();

        ParseUser.logOut();

        return res1 && res2 && res3 && res4 && res7;
    }

    private boolean deleteAllDBData() {
        ReportDetailControl reportDetailControl = new ReportDetailControl(this);
        reportDetailControl.deleteAllReports();

        CategoryDetailControl categoryDetailControl = new CategoryDetailControl(this);
        categoryDetailControl.deleteAllCategories();

        TypeDetaillControl typeDetaillControl = new TypeDetaillControl(this);
        typeDetaillControl.deleteAllTypes();

//        UserDetailControl userDetailControl = new UserDetailControl(this);
//        userDetailControl.deleteAllUsers();

        GroupDetailControl groupDetailControl = new GroupDetailControl(this);
        groupDetailControl.deleteAllGroups();

        AppConfig.setSampleAppData(0);
        AppConfig.saveUserInfoToSharePreference(null);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void processSignIn() {
        if (!internetConnected) {
            displayAlertDlg(1);
        }

        if (isLoggedIn()) {
            ParseUser.logOut();
            AppConfig.saveUserInfoToSharePreference(null);
            AppConfig.saveGroupInfoToSharePreference(null);
            if (imageViewHeaderMainIcon != null)
                imageViewHeaderMainIcon.setImageDrawable(getApplicationContext().getDrawable(R.drawable.icon_default_camera_large));
            txtHeaderMainGroupName.setText(getResources().getString(R.string.default_group));
            txtHeaderMainUserEmail.setText(getResources().getString(R.string.default_user_email));
            refreshRecycleViewMainSummary();
            refreshRecycleViewMainReport();
            refrestcatlistView();
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) navCat.getLayoutParams();
            if (catlist.size() == 0) {
                layoutParams.setMargins(0,
                        (int) (getResources().getDimension(R.dimen.nav_drawer_item_margin_top)),
                        0, (int) (getResources().getDimension(R.dimen.nav_drawer_item_margin_top)));
            }
            txtUserNumber.setText("0");
            catNumber.setText("0");
            Toast.makeText(this, "Log out sucessfully", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, ComonUtils.CODE_LOG_IN);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ComonUtils.CODE_SELECT_AVATAR_FROM_MAIN) {
            processUpdateAvatar(data, true);
        }

        switch (resultCode) {
            case ComonUtils.CODE_LOG_IN:
                if (data != null) {
                    Bundle bundle = data.getBundleExtra(ComonUtils.LOG_IN_INTENT);
                    if (bundle != null) {
                        currentUser = (UserDetail) bundle.getSerializable(ComonUtils.LOG_IN_BUNDLE);

                        GroupDetail currentGroup = groupDetailDBControl.findGroupById(currentUser.getUserGroupId());

                        txtHeaderMainGroupName.setText(currentGroup.getGroupName());

                        txtHeaderMainUserEmail.setText(currentUser.getUserEmail());
                    }
                }
                //luu user info vao share preference
                AppConfig.saveUserInfoToSharePreference(currentUser);
                isNoNeedToRefreshMainSummaryRecyle = false;
                isNoNeedToRefreshMainReportRecyle = false;
                isNoNeedToRefreshcatlist = false;
                break;
            case ComonUtils.CODE_ADD_CATEGORY:
                isNoNeedToRefreshMainSummaryRecyle = false;
                isNoNeedToRefreshcatlist = false;

                if (data != null) {
                    Bundle bundle = data.getBundleExtra(ComonUtils.NEW_CATEGORY_INTENT);
                    if (bundle != null) {
                        CategoryDetail categoryDetail = (CategoryDetail) bundle.getSerializable(ComonUtils.NEW_CATEGORY_BUNDLE);
                        if (categoryDetail.isMark()) {
//                            catlist.add(categoryDetail.getCatName());
//                            catlistAdapter.notifyDataSetChanged();
                        }
                    }
                }


                break;
            case ComonUtils.CODE_ADD_REPORT:
                if (data != null) {
                    Bundle bundle = data.getBundleExtra(ComonUtils.NEW_REPORT_INTENT);
                    if (bundle != null) {
                        ReportDetail reportDetail = (ReportDetail) bundle.getSerializable(ComonUtils.NEW_REPORT_BUNDLE);
                        if (reportDetail == null) {
                            isNoNeedToRefreshMainSummaryRecyle = true;
                            isNoNeedToRefreshMainReportRecyle = true;
                            isNoNeedToRefreshcatlist = true;

                        } else {
                            isNoNeedToRefreshMainSummaryRecyle = false;
                            isNoNeedToRefreshMainReportRecyle = false;
                            isNoNeedToRefreshcatlist = false;
                        }
                    }
                }
                break;
            case ComonUtils.CODE_UPDATE_REPORT_RESULT:
                isNoNeedToRefreshMainSummaryRecyle = false;
                isNoNeedToRefreshMainReportRecyle = true;
                break;
            case ComonUtils.CODE_EDIT_CAT_MAIN:
                refrestcatlistView();
//                refreshRecycleViewMainSummary();
                break;

            case ComonUtils.CODE_ADD_GROUP_FROM_MAIN:
                processDisplayGroup(data);

            default:
                isNoNeedToRefreshMainSummaryRecyle = false;
                isNoNeedToRefreshMainReportRecyle = false;
                isNoNeedToRefreshcatlist = false;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isLoggedIn()) {
            menu.findItem(R.id.action_signIn).setTitle("Sign out");
        } else {
            menu.findItem(R.id.action_signIn).setTitle("Sign in");
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }

    @Override
    public boolean onNavigateUpFromChild(Activity child) {
        return super.onNavigateUpFromChild(child);
    }

    @Override
    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
        super.onCreateNavigateUpTaskStack(builder);
    }

    @Override
    public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
        super.onPrepareNavigateUpTaskStack(builder);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onCreateSupportNavigateUpTaskStack(@NonNull android.support.v4.app.TaskStackBuilder builder) {
        super.onCreateSupportNavigateUpTaskStack(builder);
    }

    @Override
    public void onPrepareSupportNavigateUpTaskStack(@NonNull android.support.v4.app.TaskStackBuilder builder) {
        super.onPrepareSupportNavigateUpTaskStack(builder);
    }

    private boolean isLoggedIn() {
        UserDetail logInUser = AppConfig.getUserLogInInfor();

        if (logInUser.getUserEmail() != null) {
            return true;
        } else {
            return false;
        }
    }

    //detach fragment and re-attach to refresh data
    @SuppressLint("RestrictedApi")
    private void refreshRecycleViewMainSummary() {
        // Reload current fragment
        Fragment frg = null;
        if (getSupportFragmentManager().getFragments() == null || getSupportFragmentManager().getFragments().size() == 0)
            return;
        frg = getSupportFragmentManager().getFragments().get(0);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    //detach fragment and re-attach to refresh data
    @SuppressLint("RestrictedApi")
    private void refreshRecycleViewMainReport() {
        // Reload current fragment
        Fragment frg = null;
        if (getSupportFragmentManager().getFragments() == null || getSupportFragmentManager().getFragments().size() == 0)
            return;
        frg = getSupportFragmentManager().getFragments().get(1);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();

        currentUser = AppConfig.getUserLogInInfor();
        currentGroup = AppConfig.getCurrentGroup();

        if (catlist != null)
            catNumber.setText(catlist.size() + "");

        if (currentGroup.getGroupName() != null && !currentGroup.getGroupName().equals("")) {
            txtHeaderMainGroupName.setText(currentGroup.getGroupName());
        } else {
            txtHeaderMainGroupName.setText(getResources().getString(R.string.default_group));
        }

        if (currentUser.getUserEmail() != null) {
            int userAmount = userDetailControl.getTotalUsersInGroup();
            txtUserNumber.setText(userAmount + "");
        } else {
            txtUserNumber.setText("0");
        }

        fab.setBackgroundColor(getBaseContext().getColor(AppConfig.current_theme_setting_color));
        fab.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        fab.setRippleColor(getColor(AppConfig.current_theme_setting_color));
        fab.setImageTintList(ColorStateList.valueOf(getColor(R.color.color_white)));

        collapsingToolbarLayout.setContentScrimColor(AppConfig.getAlpha2BackgroundColorSetting());

        txtDivider3.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        txtDivider4.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        txtDivider6.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        toolbar.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        navHeaderView.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));

        if (imageViewHeaderMainIcon != null)
            AppConfig.changeRoundViewColor(imageViewHeaderMainIcon);

        if (currentUser.getUserEmail() != null && currentUser.getUserEmail() != "") {
            GroupDetail currentGroup = groupDetailDBControl.findGroupById(currentUser.getUserGroupId());

            txtHeaderMainUserEmail.setText(currentUser.getUserEmail());

            if (imageViewHeaderMainIcon != null) {
                if (currentGroup.getGroupAvatarImgPath() != null && !currentGroup.getGroupAvatarImgPath().equals("")) {
                    imageViewHeaderMainIcon.setImageBitmap(getAvatarBitmapFromPath(currentGroup.getGroupAvatarImgPath()));
                } else {
                    imageViewHeaderMainIcon.setImageDrawable(getApplicationContext().getDrawable(R.drawable.icon_default_camera_large));
                }
            }

        }

//        if (!isNoNeedToRefreshMainSummaryRecyle) {
        refreshRecycleViewMainSummary();
//        }
//        if (!isNoNeedToRefreshMainReportRecyle) {
        refreshRecycleViewMainReport();
//        }

//        if (!isNoNeedToRefreshcatlist) {
        refrestcatlistView();
//        }

        catlist = categoryDetailControl.getAllCategoryDetailFromDBByUser();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) navCat.getLayoutParams();
        if (catlist.size() == 0) {
            layoutParams.setMargins(0,
                    (int) (getResources().getDimension(R.dimen.nav_drawer_item_margin_top)),
                    0, (int) (getResources().getDimension(R.dimen.nav_drawer_item_margin_top)));
        } else {
            layoutParams.setMargins(0,
                    (int) (getResources().getDimension(R.dimen.nav_drawer_item_margin_top)),
                    0, 0);
        }

        navCat.setLayoutParams(layoutParams);

        catNumber.setText(catlist.size() + "");
    }

    private void processSync() {
        if (!internetConnected) {
            displayAlertDlg(1);
        } else if (currentUser.getUserEmail() == null) {
            displayAlertDlg(0);
        } else if (currentUser.getUserEmail() != null && currentUser.getUserStatus() != 1) {
            displayAlertDlg(2);
        } else {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_sync_layout);
            dialog.show();

            Button buttonOK = dialog.findViewById(R.id.btn_sync_ok);
            AppConfig.changeRoundViewColor(buttonOK);
            buttonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    Intent serviceIntent = new Intent(MainActivity.this, SyncService.class);
                    startService(serviceIntent);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void refrestcatlistView() {
        catlist = categoryDetailControl.getAllCategoryDetailFromDBByUser();

        if (catlist.size() > 0) {
            imgBtnExtentList.setVisibility(View.VISIBLE);
            navCatList.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) navCatList.getLayoutParams();
            lp.height = (int) (catlist.size() * (getResources().getDimension(R.dimen.important_cat_list_item_height)));
            txtDivider3.setVisibility(View.VISIBLE);
            imgBtnExtentList.setBackground(getResources().getDrawable(R.drawable.ic_arrow_drop_up_black));

        } else {
            navCatList.setVisibility(View.GONE);
            txtDivider3.setVisibility(View.GONE);
            imgBtnExtentList.setVisibility(View.GONE);
        }

        catlistAdapter = new CatListviewAdapter(this, R.layout.content_important_category_list, catlist);

        navCatList.setAdapter(catlistAdapter);

        catlistAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void processShowHideCatlistView() {
        if (catlist.size() > 0) {
            imgBtnExtentList.setVisibility(View.VISIBLE);
            navCatList.setVisibility(navCatList.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) navCatList.getLayoutParams();
            lp.height = (int) (catlist.size() * (getResources().getDimension(R.dimen.important_cat_list_item_height)));
            txtDivider3.setVisibility(navCatList.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
            imgBtnExtentList.setBackground(navCatList.getVisibility() == View.VISIBLE ?
                    getResources().getDrawable(R.drawable.ic_arrow_drop_up_black) : getResources().getDrawable(R.drawable.ic_arrow_drop_down_black));

        } else {
            navCatList.setVisibility(View.GONE);
            txtDivider3.setVisibility(View.GONE);
            imgBtnExtentList.setVisibility(View.GONE);
        }
    }

    private class SyncReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                boolean syncResult = intent.getBooleanExtra(ComonUtils.SYNC_RESULT, false);
                if (syncResult) {
                    Toast.makeText(MainActivity.this, "Sync sucessfully with server", Toast.LENGTH_LONG).show();
                    refreshRecycleViewMainSummary();
                    refreshRecycleViewMainReport();
                    refrestcatlistView();
                } else {
                    Toast.makeText(MainActivity.this, "Sync failed", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class InternetConnectionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                Bundle extras = intent.getExtras();

                NetworkInfo info = (NetworkInfo) extras
                        .getParcelable("networkInfo");

                NetworkInfo.State state = info.getState();
                Log.d("TEST Internet", info.toString() + " "
                        + state.toString());

                if (state == NetworkInfo.State.CONNECTED) {
                    internetConnected = true;
                } else {
                    internetConnected = false;
                }
            }
        }
    }

    private void processListGroup() {
        Intent intent = new Intent(this, GroupListMemberActivity.class);
        startActivityForResult(intent, ComonUtils.CODE_VIEW_LIST_MEMBER);
    }

    @Subscribe
    public void processDeleteReport(LinearLayout layout) {
        layout.setBackgroundResource(R.drawable.button_spinner_ripple);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe
    public void processEventString(MainEventString action) {
        if (action.getS().equals(ComonUtils.ACTION_REFRESH_CAT_LIST)) {
            refreshRecycleViewMainSummary();
        } else if (action.getS().equals(ComonUtils.ACTION_REFRESH_REPORT_LIST)) {
            refreshRecycleViewMainReport();
        } else if (action.getS().equals(ComonUtils.ACTION_REFRESH_IMPORTANT_CAT)) {
            refrestcatlistView();
        } else if (action.getS().equals(ComonUtils.ACTION_REFRESH_ALL)) {
            refreshRecycleViewMainSummary();
            refreshRecycleViewMainReport();
            refrestcatlistView();
        }
    }

    @Subscribe
    public void processEventObject(MainEventObject obj) {
        CategoryDetail categoryDetail = obj.getObj();
        String msg = obj.getMsg();

        if (msg.equals(ComonUtils.EDIT_CAT_MSG)) {
            processEditCategory(categoryDetail);
        }

    }


    private void processDeleteSelectedReport() {
        if (ComonUtils.deletedReports.size() == 0) {
            Toast.makeText(this, "Select items to delete", Toast.LENGTH_LONG).show();
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sync_layout);
        dialog.show();

        ImageView img_sync = dialog.findViewById(R.id.img_sync);
        img_sync.setImageResource(R.drawable.img_delete);

        TextView txt_message = dialog.findViewById(R.id.txt_message);
        txt_message.setText(getString(R.string.delete_question));

        Button buttonOK = dialog.findViewById(R.id.btn_sync_ok);
        AppConfig.changeRoundViewColor(buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                //process delete report
                EventBus.getDefault().post(new MainReportFragmentEventString(ComonUtils.ACTION_DELETE_REPORT));
            }
        });
    }

    private void processDeleteSelectedCategories() {
        if (ComonUtils.deletedCategories.size() == 0) {
            Toast.makeText(this, "Select items to delete", Toast.LENGTH_LONG).show();
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sync_layout);
        dialog.show();

        ImageView img_sync = dialog.findViewById(R.id.img_sync);
        img_sync.setImageResource(R.drawable.img_delete);

        TextView txt_message = dialog.findViewById(R.id.txt_message);
        txt_message.setText(getString(R.string.delete_category_question));

        Button buttonOK = dialog.findViewById(R.id.btn_sync_ok);
        AppConfig.changeRoundViewColor(buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new MainSummaryFragmentEventString(ComonUtils.ACTION_DELETE_CATEGORY));
            }
        });
    }


    private void processSearch() {
        Intent intent = new Intent(this, MainSearchActivity.class);
        startActivity(intent);
    }

    private void processChangeFabButton() {
        fab.setBackgroundResource(R.drawable.icon_star);
    }

    private void processViewChart() {
        Intent intent = new Intent(this, ChartViewActivity.class);
        startActivity(intent);
    }

    private void processEditCategory(CategoryDetail categoryDetail) {
        Intent intent = new Intent(this, CategoryAddNewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ComonUtils.OPEN_CAT_MAIN_BUNDLE, categoryDetail);
        intent.putExtra(ComonUtils.OPEN_CAT_MAIN_INTENT, bundle);

        startActivityForResult(intent, ComonUtils.CODE_EDIT_CAT_MAIN);

    }

    private void processSettings() {
        Intent intent = new Intent(this, MainSettingActivity.class);
        startActivity(intent);
    }

    private void processMyGroup() {
        if (currentUser.getUserEmail() != null || currentGroup.getGroupAvatarImgPath() != null) {
            processChangeGroupAvatar();
        } else {
            Intent newGroupIntent = new Intent(this, GroupAddNewActivity.class);
            newGroupIntent.setAction(ComonUtils.ACTION_ADD_GROUP_FROM_MAIN);
            startActivityForResult(newGroupIntent, ComonUtils.CODE_ADD_GROUP_FROM_MAIN);
        }
    }


    private void processChangeGroupAvatar() {

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

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
                    startActivityForResult(photoPickerIntent, ComonUtils.CODE_SELECT_AVATAR_FROM_MAIN);


                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Subscribe
    public void doChangeGroupAvatar(GroupImgPathEventObj obj) {
        if (obj.getMsg().equals("MAIN_GROUP_AVATAR_IMG_PATH")) {
            String imgPath = obj.getImgPath();
            if (imgPath != null && imageViewHeaderMainIcon != null)
                imageViewHeaderMainIcon.setImageBitmap(getAvatarBitmapFromPath(imgPath));

            GroupDetail currentGroup = groupDetailDBControl.findGroupById(currentUser.getUserGroupId());
            currentGroup.setGroupAvatar(obj.getAvatarId());
            currentGroup.setGroupAvatarImgPath(imgPath);

            groupDetailDBControl.updateGroup(currentGroup);

            Toast.makeText(this, "Update group avatar successfull", Toast.LENGTH_LONG).show();
        }
    }

    private void processDisplayGroup(Intent intent) {
        Bundle bundle = intent.getBundleExtra(ComonUtils.NEW_GROUP_INTENT);
        if (bundle != null) {
            GroupDetail groupDetail = (GroupDetail) bundle.getSerializable(ComonUtils.NEW_GROUP_BUNDLE);
            if (imageViewHeaderMainIcon != null && groupDetail.getGroupAvatarImgPath() != null && !groupDetail.getGroupAvatarImgPath().equals(""))
                imageViewHeaderMainIcon.setImageBitmap(getAvatarBitmapFromPath(groupDetail.getGroupAvatarImgPath()));
            txtHeaderMainGroupName.setText(groupDetail.getGroupName());
            currentGroup = new GroupDetail(groupDetail.getGroupId(), groupDetail.getGroupName(), groupDetail.getGroupAvatarImgPath());
            AppConfig.saveGroupInfoToSharePreference(currentGroup);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void processUpdateAvatar(Intent data, boolean isUpdateIcon) {
        if (data == null) return;
        Uri selectedImage = data.getData();
        String imgPath = null;

        try {
            Bitmap bitmap = ComonUtils.getCorrectlyOrientedImage(this, selectedImage);
            if (imageViewHeaderMainIcon != null)
                imageViewHeaderMainIcon.setImageBitmap(bitmap);

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

        if (isUpdateIcon) {
            ArrayList<GroupDetail> groupDetails = new ArrayList<GroupDetail>();
            if (currentUser.getUserEmail() != null) {
                GroupDetail group = groupDetailDBControl.findGroupById(currentUser.getUserGroupId());
                group.setGroupAvatarImgPath(imgPath);
                //update on DB
                groupDetailDBControl.updateGroup(group);
                //update on server
                groupDetails.add(group);
                //update on share preference
                AppConfig.saveGroupInfoToSharePreference(group);
            } else if (currentGroup.getGroupId() != -1) {
                currentGroup.setGroupAvatarImgPath(imgPath);
                groupDetailDBControl.updateGroup(currentGroup);
                groupDetails.add(currentGroup);
                AppConfig.saveGroupInfoToSharePreference(currentGroup);
            }

            if (groupDetailServerControl.updateGroupToServer(groupDetails)) {
                Toast.makeText(this, "Update group avatar successfull", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Update group avatar failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void processDisplayFFMInfo() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }


}
package com.muoipt.ffm.view;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muoipt.ffm.R;
import com.muoipt.ffm.control.CategoryDetailControl;
import com.muoipt.ffm.control.CategoryDetailServerControl;
import com.muoipt.ffm.control.TypeDetailServerControl;
import com.muoipt.ffm.control.TypeDetaillControl;
import com.muoipt.ffm.eventmodel.GroupImgPathEventObj;
import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.MainSummaryReport;
import com.muoipt.ffm.model.TypeDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.CircularImageView;
import com.muoipt.ffm.utils.ComonUtils;
import com.muoipt.ffm.utils.SyncUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.muoipt.ffm.utils.ComonUtils.CODE_ADD_CATEGORY;
import static com.muoipt.ffm.utils.ComonUtils.NEW_CATEGORY_BUNDLE;
import static com.muoipt.ffm.utils.ComonUtils.NEW_CATEGORY_INTENT;
import static com.muoipt.ffm.utils.ComonUtils.OPEN_CAT_MAIN_BUNDLE;
import static com.muoipt.ffm.utils.ComonUtils.OPEN_CAT_MAIN_BUNDLE_RESULT;
import static com.muoipt.ffm.utils.ComonUtils.OPEN_CAT_MAIN_INTENT_RESULT;
import static com.muoipt.ffm.utils.ComonUtils.OPEN_CAT_SEARCH_BUNDLE_RESULT;
import static com.muoipt.ffm.utils.ComonUtils.OPEN_CAT_SEARCH_INTENT_RESULT;

public class CategoryAddNewActivity extends AppCompatActivity implements View.OnClickListener {

    private View addNewCatFormView;
    private TextView txt_banner_new_cat;
    private EditText editCatName;
    private TextView txtTitleType, txtTitleLimit, txtLimitMax, txtSelectedAvatarHome, txtSelectedAvatarBaby, txtSelectedAvatarFood, txtSelectedAvatarIncome, txtSelectedAvatarChoose;
    private RadioGroup radioGroupCatType;
    private RadioButton radioButtonIncome, radioButtonOutcome;
    private SeekBar seekbarCatLimit;
    private ImageButton imgAvatarHome, imgAvatarBaby, imgAvatarFood, imgAvatarIncome;
    private CircularImageView imgAvatarChoose;
    private Button btnCancel, btnOk;
    private View mProgressView;
    private FloatingActionButton fabMark;
    private ProgressDialog progressDialog;

    private AddNewCategoryTask mAddCategoryTask = null;
    private String catInputName, typeInputName;
    private int catInputLimit = 0;
    private int catInputAvatar = 0;

    private boolean isCatMarkImportant = false;
    private String catTypeName = "";
    private CategoryDetail categoryDetail = null;

    private CategoryDetailControl dbControl;
    private CategoryDetailServerControl serverControl;
    private TypeDetaillControl typeControl;

    private ArrayList<TypeDetail> arrTypes;
    private ArrayList<String> arrTypeNames;

    private boolean isNewlyCreated = true;
    private CategoryDetail selectedCategoryDetail = null;
    //    private android.support.v7.app.ActionBar actionBar;
    private Toolbar toolbar;
    private InputMethodManager imm;
    private boolean isAvatarFromGalary = false;
    private String catInputAvatarImgPath = "";
    private View viewEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add_new);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getControlWidget();

        initControl();

//        actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(AppConfig.getThemeColor()));

        seekbarCatLimit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                catInputLimit = progress;
                txtLimitMax.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                txtLimitMax.setError(null);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }

    private void getControlWidget() {
        addNewCatFormView = findViewById(R.id.add_new_cat_form);
        mProgressView = findViewById(R.id.add_cat_progress);
        fabMark = (FloatingActionButton) findViewById(R.id.fabCatMark);
        editCatName = (EditText) findViewById(R.id.editAddCatName);
        txtTitleType = (TextView) findViewById(R.id.txtTitleType);
        txtLimitMax = (TextView) findViewById(R.id.txtLimitMax);
        txtTitleLimit = (TextView) findViewById(R.id.txtTitleLimit);
        radioGroupCatType = (RadioGroup) findViewById(R.id.radioGroupCatType);
        radioButtonIncome = (RadioButton) findViewById(R.id.radioBtnCatTypeIncome);
        radioButtonOutcome = (RadioButton) findViewById(R.id.radioBtnCatTypeOutcome);
        seekbarCatLimit = (SeekBar) findViewById(R.id.seekbarCatLimit);
        imgAvatarHome = (ImageButton) findViewById(R.id.imgBtnCatAvatarRentHome);
        imgAvatarBaby = (ImageButton) findViewById(R.id.imgBtnCatAvatarBaby);
        imgAvatarFood = (ImageButton) findViewById(R.id.imgBtnCatAvatarFood);
        imgAvatarIncome = (ImageButton) findViewById(R.id.imgBtnCatAvatarIncome);
        imgAvatarChoose = (CircularImageView) findViewById(R.id.imgBtnCatAvatarChoose);
        btnCancel = (Button) findViewById(R.id.btnAddCatCancel);
        btnOk = (Button) findViewById(R.id.btnAddCatOk);
        txtSelectedAvatarHome = (TextView) findViewById(R.id.txtSelectedAvatarHome);
        txtSelectedAvatarBaby = (TextView) findViewById(R.id.txtSelectedAvatarBaby);
        txtSelectedAvatarFood = (TextView) findViewById(R.id.txtSelectedAvatarFood);
        txtSelectedAvatarIncome = (TextView) findViewById(R.id.txtSelectedAvatarIncome);
        txtSelectedAvatarChoose = (TextView) findViewById(R.id.txtSelectedAvatarChoose);
        txt_banner_new_cat = (TextView) findViewById(R.id.txt_banner_new_cat);
        toolbar = (Toolbar) findViewById(R.id.add_cat_toolbar);
//        imm.hideSoftInputFromWindow(editCatName.getWindowToken(), 0);
//        viewEmpty = findViewById(R.id.viewEmpty);


    }

    private void initControl() {
        imgAvatarChoose.setImageResource(R.drawable.icon_default_camera_small);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        imgAvatarHome.setOnClickListener(this);
        imgAvatarBaby.setOnClickListener(this);
        imgAvatarFood.setOnClickListener(this);
        imgAvatarIncome.setOnClickListener(this);
        fabMark.setOnClickListener(this);
        radioButtonIncome.setOnClickListener(this);
        radioButtonOutcome.setOnClickListener(this);
        imgAvatarHome.setOnClickListener(this);
        imgAvatarBaby.setOnClickListener(this);
        imgAvatarFood.setOnClickListener(this);
        imgAvatarIncome.setOnClickListener(this);
        imgAvatarChoose.setOnClickListener(this);

        typeControl = new TypeDetaillControl(this);
        dbControl = new CategoryDetailControl(this);
        serverControl = new CategoryDetailServerControl(this);

        arrTypes = new ArrayList<TypeDetail>();
        arrTypeNames = getTypesList();

        EventBus.getDefault().register(this);

        if (getIntent() != null) {
            Intent intent = getIntent();
            ///////////
            Bundle bundleOpenCatFromSearch = intent.getBundleExtra(ComonUtils.OPEN_CAT_SEARCH_INTENT);
            if (bundleOpenCatFromSearch != null) {
                MainSummaryReport summaryReport = (MainSummaryReport) bundleOpenCatFromSearch.getSerializable(ComonUtils.OPEN_CAT_SEARCH_BUNDLE);
                if (summaryReport != null) {
                    selectedCategoryDetail = getCatFromMainSummaryReport(summaryReport);
                    if (selectedCategoryDetail != null)
                        initViewProfile();
                }
            }

            ///
            Bundle bundleOpenCatFromMain = intent.getBundleExtra(ComonUtils.OPEN_CAT_MAIN_INTENT);
            if (bundleOpenCatFromMain != null) {
                selectedCategoryDetail = (CategoryDetail) bundleOpenCatFromMain.getSerializable(ComonUtils.OPEN_CAT_MAIN_BUNDLE);
                if (selectedCategoryDetail != null) {
                    initViewProfile();
                }
            }
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

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        editCatName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    imm.hideSoftInputFromWindow(editCatName.getWindowToken(), 0);
                    radioButtonIncome.requestFocus();
                    radioButtonIncome.setChecked(true);
                    editCatName.setCursorVisible(false);

                    return true;
                }
                return false;
            }
        });

        editCatName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editCatName.setFocusableInTouchMode(true);
                    editCatName.setFocusable(true);
                    editCatName.requestFocus();
                    editCatName.setCursorVisible(true);
                    editCatName.setActivated(true);
                    editCatName.setPressed(true);
                } else {
                    editCatName.setFocusableInTouchMode(false);
                    editCatName.setFocusable(false);
                }
            }
        });

        editCatName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCatName.setCursorVisible(true);
            }
        });

        /*
         * typeInputName = getString(R.string.data_type_income);
         * */
        radioButtonIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    typeInputName = getString(R.string.data_type_income);
                }
            }
        });

        radioButtonOutcome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    typeInputName = getString(R.string.data_type_outcome);
                }
            }
        });
    }

    private void initViewProfile() {
        isNewlyCreated = false;
        toolbar.setTitle(getString(R.string.title_activity_category_update));

        editCatName.setText(selectedCategoryDetail.getCatName());
        String typeName = getTypeNameFromId(selectedCategoryDetail.getCatTypeId());
        typeInputName = typeName;
        catInputName = selectedCategoryDetail.getCatName();

        isCatMarkImportant = selectedCategoryDetail.isMark();

        if (typeName.equals(getString(R.string.data_type_income))) {
            radioButtonIncome.setChecked(true);
            radioButtonOutcome.setChecked(false);
        } else {
            radioButtonIncome.setChecked(false);
            radioButtonOutcome.setChecked(true);
        }

        int limit = selectedCategoryDetail.getCatLimit();
        seekbarCatLimit.setProgress(limit);
        txtLimitMax.setText(limit + "");
        catInputLimit = limit;

        if (selectedCategoryDetail.isMark()) {
            isCatMarkImportant = true;
            fabMark.setImageResource(R.drawable.ic_category_star);
        }

        int avatar = selectedCategoryDetail.getCatAvatar();
        catInputAvatar = avatar;

        catInputAvatarImgPath = selectedCategoryDetail.getCatAvatarImagePath();

        if (catInputAvatarImgPath != null && !catInputAvatarImgPath.equals("")) {

            Bitmap b = getAvatarBitmapFromPath(catInputAvatarImgPath);

            imgAvatarChoose.setImageBitmap(b);
            txtSelectedAvatarChoose.setVisibility(View.VISIBLE);
        }

    }

    private void handleOnBackPress() {
        this.finish();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if(keyCode == )
//
//        return true;
//    }

    private CategoryDetail getCatFromMainSummaryReport(MainSummaryReport summaryReport) {
        int id = summaryReport.getId();

        CategoryDetail cat = dbControl.getCategoryFromId(id);

        return cat;
    }

    private void processAddCategory() {
        if (processNewCategory())
            new AddNewCategoryTask().execute();
    }

    @SuppressLint("ResourceType")
    private boolean processNewCategory() {

        if (mAddCategoryTask != null) {
            categoryDetail = null;
            return false;
        }

        editCatName.setError(null);
        radioButtonOutcome.setError(null);

        boolean cancel = false;
        View focusView = null;

        catInputName = editCatName.getText().toString();

        if (radioGroupCatType.getCheckedRadioButtonId() <= 0) {
//            typeInputName = getString(R.string.add_new_category_type_income_text);
            radioButtonOutcome.setError(getString(R.string.error_cat_type_required));
            focusView = radioButtonOutcome;
            cancel = true;
        }

        if (radioButtonIncome.isChecked()) {
            catTypeName = getResources().getString(R.string.data_type_income);
        } else if (radioButtonOutcome.isChecked()) {
            catTypeName = getResources().getString(R.string.data_type_outcome);
        }

        //check validity
        if (catInputName == null || catInputName.isEmpty() || catInputName.equals("")) {
            editCatName.setError(getString(R.string.error_cat_name_required));
            focusView = editCatName;
            cancel = true;
        }

        if (catInputLimit == 0) {
            txtLimitMax.setError(getString(R.string.error_cat_limit_required));
            focusView = txtLimitMax;
            cancel = true;
        }

        if (cancel) {
            categoryDetail = null;
            focusView.requestFocus();
            return false;
        } else {
            fabMark.setVisibility(View.GONE);
            showProgress(true);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddCatCancel:
                finish();
                break;
            case R.id.btnAddCatOk:
                processAddCategory();
                break;
            case R.id.fabCatMark:
                isCatMarkImportant = !isCatMarkImportant;
                if (isCatMarkImportant)
                    fabMark.setImageResource(R.drawable.ic_category_star);
                else
                    fabMark.setImageResource(R.drawable.ic_add_cat_mark);
                break;
            case R.id.radioBtnCatTypeIncome:
                typeInputName = getString(R.string.data_type_income);
                radioButtonOutcome.setError(null);
                break;
            case R.id.radioBtnCatTypeOutcome:
                typeInputName = getString(R.string.data_type_outcome);
                radioButtonOutcome.setError(null);
                break;
            case R.id.imgBtnCatAvatarRentHome:
                getSelectedAvatar(0);
                break;
            case R.id.imgBtnCatAvatarBaby:
                getSelectedAvatar(1);
                break;
            case R.id.imgBtnCatAvatarFood:
                getSelectedAvatar(2);
                break;
            case R.id.imgBtnCatAvatarIncome:
                getSelectedAvatar(3);
                break;
            case R.id.imgBtnCatAvatarChoose:
                getSelectedAvatar(4);
                break;

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getSelectedAvatar(int imgAvataIndex) {
        switch (imgAvataIndex) {
            case 0:
                catInputAvatar = R.drawable.home_icon;
                txtSelectedAvatarHome.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
                txtSelectedAvatarBaby.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarFood.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarIncome.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarChoose.setBackgroundColor(getColor(R.color.color_white));
                break;
            case 1:
                catInputAvatar = R.drawable.baby_icon;
                txtSelectedAvatarBaby.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
                txtSelectedAvatarHome.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarFood.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarIncome.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarChoose.setBackgroundColor(getColor(R.color.color_white));
                break;
            case 2:
                catInputAvatar = R.drawable.food_icon;
                txtSelectedAvatarFood.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
                txtSelectedAvatarBaby.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarHome.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarIncome.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarChoose.setBackgroundColor(getColor(R.color.color_white));
                break;
            case 3:
                catInputAvatar = R.drawable.money_icon;
                txtSelectedAvatarIncome.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
                txtSelectedAvatarBaby.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarHome.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarFood.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarChoose.setBackgroundColor(getColor(R.color.color_white));
                break;
            case 4:
                catInputAvatar = R.drawable.icon_default_camera_small;
                txtSelectedAvatarChoose.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
                txtSelectedAvatarBaby.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarFood.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarIncome.setBackgroundColor(getColor(R.color.color_white));
                txtSelectedAvatarHome.setBackgroundColor(getColor(R.color.color_white));
                processChooseImageAvatar();
                break;

        }
    }

//    private void showProgress(final boolean show) {
//        if (show) {
//            progressDialog = new ProgressDialog(CategoryAddNewActivity.this);
//            progressDialog.setTitle("Processing...");
//            progressDialog.setMessage("Please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.setIndeterminate(true);
//            progressDialog.show();
//        } else {
//            progressDialog.dismiss();
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void resetInput() {
        editCatName.setText("");
        radioButtonIncome.setChecked(false);
        radioButtonOutcome.setChecked(false);
        seekbarCatLimit.setProgress(0);
        txtSelectedAvatarHome.setBackgroundColor(getColor(R.color.color_white));
        txtSelectedAvatarBaby.setBackgroundColor(getColor(R.color.color_white));
        txtSelectedAvatarFood.setBackgroundColor(getColor(R.color.color_white));
        txtSelectedAvatarIncome.setBackgroundColor(getColor(R.color.color_white));
        txtSelectedAvatarChoose.setBackgroundColor(getColor(R.color.color_white));
    }

    private int getTypeIdFromName(String typeName) {
        for (int i = 0; i < arrTypes.size(); i++) {
            if (arrTypes.get(i).getTypeName().equals(typeName)) {
                return arrTypes.get(i).getTypeId();
            }
        }
        return -1;
    }

    private String getTypeNameFromId(int typeId) {
        for (int i = 0; i < arrTypes.size(); i++) {
            if (arrTypes.get(i).getTypeId() == typeId) {
                return arrTypes.get(i).getTypeName();
            }
        }
        return null;
    }

    private ArrayList<String> getTypesList() {
        ArrayList<String> list = new ArrayList<String>();

        arrTypes = typeControl.getDetailTypeFromDBByUser();

        for (int i = 0; i < arrTypes.size(); i++) {
            list.add(arrTypes.get(i).getTypeName());
        }

        return list;
    }

    //add thanh cong vao DB truoc sau do sync len server, neu bi duplicate thi tu dong
    //add_extra_order vao sau nam. vd food(1)
    class AddNewCategoryTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            categoryDetail = new CategoryDetail();
//            categoryDetail.setCatId(serverControl.getNextCategoryDetailTblId());
            categoryDetail.setCatName(catInputName);
            categoryDetail.setCatTypeId(getTypeIdFromName(typeInputName));
            categoryDetail.setCatLimit(catInputLimit);
            categoryDetail.setCatStatus(false);
            categoryDetail.setCatAvatar(catInputAvatar);
            categoryDetail.setCatAvatarImagePath(catInputAvatarImgPath);
            categoryDetail.setCatDeleted(false);
            categoryDetail.setMark(isCatMarkImportant);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (isNewlyCreated) {
                if (dbControl.checkCatNameExistInDb(categoryDetail.getCatName())) {
                    return false;
                }

                categoryDetail.setCatId(serverControl.getNextCategoryDetailTblId());

                //add category on server
                ArrayList<CategoryDetail> list = new ArrayList<CategoryDetail>();
                list.add(categoryDetail);
                if (!serverControl.saveCatDetailToServer(categoryDetail)) {
                    return false;
                }

                if (!dbControl.addCategory(categoryDetail)) {
                    return false;
                }
            } else {

                categoryDetail.setCatId(selectedCategoryDetail.getCatId());

                //add category on server
//                ArrayList<CategoryDetail> list = new ArrayList<CategoryDetail>();
//                list.add(categoryDetail);
//                if (!serverControl.updateCatDetailToServer(list)) {
//                    return false;
//                }

                dbControl.updateCategory(categoryDetail);
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mAddCategoryTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                if (isNewlyCreated) {
                    bundle.putSerializable(NEW_CATEGORY_BUNDLE, categoryDetail);
                    intent.putExtra(NEW_CATEGORY_INTENT, bundle);
                    setResult(CODE_ADD_CATEGORY, intent);

                    Toast.makeText(getBaseContext(), "Add category successful", Toast.LENGTH_LONG).show();
                } else {
                    bundle.putSerializable(OPEN_CAT_SEARCH_BUNDLE_RESULT, categoryDetail);
                    intent.putExtra(OPEN_CAT_SEARCH_INTENT_RESULT, bundle);
                    setResult(ComonUtils.CODE_EDIT_CAT_SEARCH, intent);

                    bundle.putSerializable(OPEN_CAT_MAIN_BUNDLE_RESULT, categoryDetail);
                    intent.putExtra(OPEN_CAT_MAIN_INTENT_RESULT, bundle);
                    setResult(ComonUtils.CODE_EDIT_CAT_MAIN, intent);

                    Toast.makeText(getBaseContext(), "Update category successful", Toast.LENGTH_LONG).show();
                }
                finish();
            } else {
                editCatName.setError(getString(R.string.error_cat_name_exist));
                editCatName.requestFocus();
                Toast.makeText(getBaseContext(), "Add category failed", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled() {
            mAddCategoryTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (requestCode == ComonUtils.CODE_SELECT_AVATAR_FROM_CATEGORY) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = ComonUtils.getCorrectlyOrientedImage(this, selectedImage);
                imgAvatarChoose.setImageBitmap(bitmap);

                File savedFile = new File(getExternalCacheDir(), ComonUtils.createNewCacheCatFileName());
                try {
                    FileOutputStream out = new FileOutputStream(savedFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    catInputAvatarImgPath = savedFile.getAbsolutePath();
                    isAvatarFromGalary = true;
                } catch (Exception e) {
                    Log.e("Image", "Convert");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();

        txt_banner_new_cat.setTextColor(getColor(AppConfig.current_theme_setting_color));
        toolbar.setBackgroundColor(AppConfig.getThemeColor());

        fabMark.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        fabMark.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        editCatName.setHintTextColor(getColor(AppConfig.current_theme_setting_color));
        editCatName.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        editCatName.setLinkTextColor(getColor(AppConfig.current_theme_setting_color));
        radioButtonIncome.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        radioButtonIncome.setButtonTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        radioButtonIncome.setForegroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        radioButtonIncome.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));

        radioButtonOutcome.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        radioButtonOutcome.setButtonTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        radioButtonOutcome.setForegroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        radioButtonOutcome.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));

        seekbarCatLimit.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        seekbarCatLimit.setProgressBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        seekbarCatLimit.setThumbTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        seekbarCatLimit.setForegroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        seekbarCatLimit.setIndeterminateTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        seekbarCatLimit.setProgressBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        seekbarCatLimit.setTickMarkTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));

        ((ProgressBar) findViewById(R.id.add_cat_progress))
                .getIndeterminateDrawable()
                .setColorFilter(AppConfig.getThemeColor(), PorterDuff.Mode.SRC_IN);
    }


    private void processChooseImageAvatar() {
        ActivityCompat.requestPermissions(CategoryAddNewActivity.this,
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
                    startActivityForResult(photoPickerIntent, ComonUtils.CODE_SELECT_AVATAR_FROM_CATEGORY);

                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        addNewCatFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        addNewCatFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                addNewCatFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void processGalaryAvatar(GroupImgPathEventObj obj) {
        if (obj.getMsg().equals("GROUP_AVATAR_IMG_PATH")) {
            String imgPath = obj.getImgPath();
            if (imgPath != null) {
                imgAvatarChoose.setImageBitmap(getAvatarBitmapFromPath(imgPath));
                isAvatarFromGalary = true;
                catInputAvatarImgPath = imgPath;
                catInputAvatar = -1;
            }
            AppConfig.current_category_avatar_img_filepath = imgPath;
        }
    }

    private Bitmap getAvatarBitmapFromPath(String imgPath) {
        File image = new File(imgPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) getResources().getDimension(R.dimen.dp_36), (int) getResources().getDimension(R.dimen.dp_36), true);

        return bitmap;
    }


}

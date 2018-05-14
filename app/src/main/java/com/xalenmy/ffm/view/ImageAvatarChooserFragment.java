package com.xalenmy.ffm.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xalenmy.ffm.MainActivity;
import com.xalenmy.ffm.R;
import com.xalenmy.ffm.eventmodel.GroupImgPathEventObj;
import com.xalenmy.ffm.utils.AppConfig;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageAvatarChooserFragment extends android.app.Fragment implements AdapterView.OnItemClickListener {
    private final ArrayList<BackgroundItem> mBackgroundItems = new ArrayList<BackgroundItem>();
    private BackgroundImgAdapter mImageAdapter;
    private GridView mImageGridView;
    private static final int GET_EXTERNEL_IMAGE = 101;
    private static final int GET_OPAQUE_IMAGE = 102;
    private static final int GET_EXTERNEL_CROPPED_IMAGE = 103;
    private static final int NO_OF_IMAGES_WITHOUT_CUSTOM = 20;
    private static final String IS_DELETE_MODE = "isDeleteMode";
    private static final String NEW_TEMP_FILE_NAME = "newTempFileName";
    private static final String CHECKED_ITEMS = "checkedItemList";
    private int noteWidthPixels;
    private int noteHeightPixels;
    private static final String JPG_EXT = ".jpg";
    private static final String PNG_EXT = ".png";
    private Uri tempOpaqueImageFileURI;
    private boolean isDeleteMode = false;
    private String newTempFileName;
    private Activity mActivity;
    private ActionBar mActionBar;
    private TextView mSelectedNumber;
    private CheckBox mSelectAllCheckBox;
    private Context mContext;
    private final Object mPauseWorkLock = new Object();
    protected boolean mPauseWork = false;
    private LruCache<String, Bitmap> imageCache;
    private Bitmap mBitmap;
    //    private String galleryImageLocation;
    private ContentObserver mGalleryUriContentObserver;
    private String TAG = "ImageAvatarChooser";
    private boolean isGalleryImageChanged = true;
    private boolean isNoteLandTemplate = false;
    private String newCroppedImageFilePath = null;

    private static String BGSETTING_LAND_DIR_4_3 = "BGSetting_Land_4_3";
    private static String BGSETTING_LAND_DIR = "BGSetting_Land";
    private static String BGSETTING_PORT_DIR_4_3 = "BGSetting_4_3";
    private static String BGSETTING_PORT_DIR = "BGSetting";
    public static String BGSETTING_LAND_TEMPLATE_4_3;
    public static String BGSETTING_LAND_TEMPLATE;
    public static String BGSETTING_PORT_TEMPLATE_4_3;
    public static String BGSETTING_PORT_TEMPLATE;
    public static String BGSETTING_LAND_THUMB;
    public static String BGSETTING_PORT_THUMB;
    public static String BGSETTING_LAND_BACKUP_4_3;
    public static String BGSETTING_LAND_BACKUP;
    public static String BGSETTING_PORT_BACKUP_4_3;
    public static String BGSETTING_PORT_BACKUP;
    public static String FFM_TEMPORARY_CACHE = "";

    public static double screenRatio_4_3 = 4.0 / 3;
    // 2017A...


    public double notePortRatio() {
        return 1.0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_avatar_chooser, container, false);
        mActivity = getActivity();
        mContext = getActivity();
        init(view, savedInstanceState);
        setHasOptionsMenu(true);

        return view;
    }

    private void init(View view, Bundle savedInstanceState) {
        mImageGridView = ((GridView) view.findViewById(R.id.sample_image_list));
        mImageGridView.setFocusable(true);
        mImageGridView.setGravity(Gravity.CENTER);
        mImageAdapter = new BackgroundImgAdapter(mActivity);
        createStorage();
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();

        if (savedInstanceState != null) {
            isDeleteMode = savedInstanceState.getBoolean(IS_DELETE_MODE);
            mImageAdapter.setCheckedArray(savedInstanceState.getIntegerArrayList(CHECKED_ITEMS));
            if (isDeleteMode) {
                setDeleteModeView();
            }
            newTempFileName = savedInstanceState.getString(NEW_TEMP_FILE_NAME);
        }
        mImageGridView.setOnItemClickListener(this);
        mImageGridView.setSelection(mImageAdapter.getCount() - 1);

//        Point mDisplaySize = ResourceUtil.getScreenSize(mActivity);
//        noteWidthPixels = Math.min(mDisplaySize.x, mDisplaySize.y);
//        noteHeightPixels = Math.max(mDisplaySize.x, mDisplaySize.y);

        registerMediaContentObserver();
    }

    private void displyDeleteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCustomImages();
                isDeleteMode = false;
                initBackgroundList();
                getActivity().invalidateOptionsMenu();
                mImageAdapter.notifyDataSetChanged();
                resetActionBar();
            }
        }).setNegativeButton(android.R.string.cancel, null);

        builder.setMessage("this_item_will_be_deleted");
        Dialog m_DeleteDialog = builder.create();
        m_DeleteDialog.show();
    }

    private void initBackgroundList() {
        mBackgroundItems.clear();
        initImagesList();
        initGalleryBackgroundImage();
    }

    private void initImagesList() {
        if (mActivity == null || !isAdded())
            return;
        TypedArray arr = getResources().obtainTypedArray(R.array.image_setting_thumbnail);
        TypedArray arrId = getResources().obtainTypedArray(R.array.image_setting_thumbnail);
        TypedArray arrIdSmall = getResources().obtainTypedArray(R.array.image_setting_thumbnail_small);


        BackgroundItem item_initial = new BackgroundItem();
        item_initial.thumbID = arr.getResourceId(0, -1);
        mBackgroundItems.add(item_initial);

        for (int i = 1; i < arr.length(); i++) {
            BackgroundItem item = new BackgroundItem();
            item.thumbID = arr.getResourceId(i, -1);
            item.thumbIDSmall = arrIdSmall.getResourceId(i, -1);
            item.actualImageId = arrId.getResourceId(i, -1);
            mBackgroundItems.add(item);
        }
        arr.recycle();
        arrId.recycle();
    }

    private void initGalleryBackgroundImage() {
        if (mImageAdapter != null && isExistThumbImage()) {
            File file = new File(getCustomThumbDir());
            if (file.list() != null) {
                String files[] = file.list();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        try {
                            BackgroundItem item = new BackgroundItem();
                            item.customImageThumbPath = getCustomThumbDirPath() + files[i];
                            item.customImageFileName = files[i];
                            mBackgroundItems.add(item);
                        } catch (NullPointerException e) {
                        }
                    }
                }
            }
        }
    }


    private void chooseImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setPackage("com.sec.android.gallery3d");
        photoPickerIntent.putExtra("crop", "true");
        photoPickerIntent.putExtra("return-data", true);
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        photoPickerIntent.putExtra("noFaceDetection", true);
        photoPickerIntent.putExtra("output", tempOpaqueImageFileURI);
        try {
            startActivityForResult(photoPickerIntent, GET_EXTERNEL_IMAGE);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            final PackageManager mgr = mContext.getPackageManager();
            String appName = mgr.getApplicationLabel(getAppInfo(mgr, AppConfig.GALLERY3D)).toString();
            List<ResolveInfo> list = mgr.queryIntentActivities(photoPickerIntent, PackageManager.MATCH_DEFAULT_ONLY);
            mImageGridView.setOnItemClickListener(this);
        }
    }

    private ApplicationInfo getAppInfo(PackageManager pm, String packageName) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = pm.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "can't find package");
        }
        return appInfo;
    }

    private Bitmap convertFileToBitmap(File paramFile) {
        Bitmap localBitmap = BitmapFactory.decodeFile(paramFile.getAbsolutePath());
        return localBitmap;
    }

    private void setOpacityImage() {
        Bundle bundle = new Bundle();
        bundle.putString("temp_opaque_file", getCustomCacheDirPath() + "opaque" + newTempFileName);
        bundle.putString("temp_cache_image", getCustomCacheDirPath() + newTempFileName);
        bundle.putString("temp_file_name", newTempFileName);

//        String imgPath = getCustomCacheDirPath() + "opaque" + newTempFileName;
        String imgPath = mActivity.getFilesDir() + newTempFileName;

        File file = new File(mActivity.getFilesDir(), newTempFileName);

        BackgroundItem item = new BackgroundItem();
        item.customImageThumbPath = imgPath;
        item.thumbID = -1;
        item.thumbIDSmall = -1;

//        ((ImageAvatarChooserActivity) mActivity).setAvatarItem(item);

//        GroupImgPathEventObj obj1 = new GroupImgPathEventObj();
//        obj1.setMsg("CATEGORY_AVATAR_IMG_PATH");
//        obj1.setImgPath(imgPath);
//        obj1.setAvatarId(-1);
//        EventBus.getDefault().post(obj1);
//
//        GroupImgPathEventObj obj2 = new GroupImgPathEventObj();
//        obj2.setMsg("GROUP_AVATAR_IMG_PATH");
//        obj2.setImgPath(imgPath);
//        obj2.setAvatarId(-1);
//        EventBus.getDefault().post(obj2);
//
//        GroupImgPathEventObj obj3 = new GroupImgPathEventObj();
//        obj3.setMsg("MAIN_GROUP_AVATAR_IMG_PATH");
//        obj3.setImgPath(imgPath);
//        obj3.setAvatarId(-1);
//        EventBus.getDefault().post(obj3);

        getActivity().finish();
    }

    private void createStorage() {

        BGSETTING_LAND_TEMPLATE_4_3 = AppConfig.getCacheDirExternal() + File.separator + BGSETTING_LAND_DIR_4_3;
        BGSETTING_LAND_TEMPLATE = AppConfig.getCacheDirExternal() + File.separator + BGSETTING_LAND_DIR;
        BGSETTING_PORT_TEMPLATE_4_3 = AppConfig.getCacheDirExternal() + File.separator + BGSETTING_PORT_DIR_4_3;
        BGSETTING_PORT_TEMPLATE = AppConfig.getCacheDirExternal() + File.separator + BGSETTING_PORT_DIR;

        BGSETTING_LAND_THUMB = mActivity.getFilesDir() + File.separator + BGSETTING_LAND_DIR;
        BGSETTING_PORT_THUMB = mActivity.getFilesDir() + File.separator + BGSETTING_PORT_DIR;

        BGSETTING_LAND_BACKUP_4_3 = mActivity.getFilesDir() + File.separator + "bg_backup" + File.separator +
                BGSETTING_LAND_DIR_4_3;
        BGSETTING_LAND_BACKUP = mActivity.getFilesDir() + File.separator + "bg_backup" + File.separator +
                BGSETTING_LAND_DIR;
        BGSETTING_PORT_BACKUP_4_3 = mActivity.getFilesDir() + File.separator + "bg_backup" + File.separator +
                BGSETTING_PORT_DIR_4_3;
        BGSETTING_PORT_BACKUP = mActivity.getFilesDir() + File.separator + "bg_backup" + File.separator +
                BGSETTING_PORT_DIR;

        File customDir_cache = new File(BGSETTING_LAND_TEMPLATE_4_3);
        if (!customDir_cache.exists())
            customDir_cache.mkdir();

        customDir_cache = new File(BGSETTING_LAND_TEMPLATE);
        if (!customDir_cache.exists())
            customDir_cache.mkdir();

        customDir_cache = new File(BGSETTING_PORT_TEMPLATE_4_3);
        if (!customDir_cache.exists())
            customDir_cache.mkdir();

        customDir_cache = new File(BGSETTING_PORT_TEMPLATE);
        if (!customDir_cache.exists())
            customDir_cache.mkdir();

        File customDir_thumb = new File(BGSETTING_LAND_THUMB);
        if (!customDir_thumb.exists())
            customDir_thumb.mkdir();

        customDir_thumb = new File(BGSETTING_PORT_THUMB);
        if (!customDir_thumb.exists())
            customDir_thumb.mkdir();

        File backup = new File(BGSETTING_LAND_BACKUP_4_3);
        if (!backup.exists())
            backup.mkdirs();
        backup = new File(BGSETTING_LAND_BACKUP);
        if (!backup.exists())
            backup.mkdirs();
        backup = new File(BGSETTING_PORT_BACKUP_4_3);
        if (!backup.exists())
            backup.mkdirs();
        backup = new File(BGSETTING_PORT_BACKUP);
        if (!backup.exists())
            backup.mkdirs();
    }

    private String getCustomCacheDir() {
        return BGSETTING_PORT_TEMPLATE;
    }

    private String getCustomCacheDirPath() {
        return getCustomCacheDir() + File.separator;
    }

    private String getCustomCacheOppositeDirpath() {
        return (notePortRatio() == screenRatio_4_3 ? BGSETTING_PORT_TEMPLATE : BGSETTING_PORT_TEMPLATE_4_3) + File.separator;
    }

    private String getCustomThumbDirPath() {
        if (isNoteLandTemplate) {
            return BGSETTING_LAND_THUMB + File.separator;
        } else {
            return BGSETTING_PORT_THUMB + File.separator;
        }
    }

    private String getCustomThumbDir() {
        if (isNoteLandTemplate) {
            return BGSETTING_LAND_THUMB;
        } else {
            return BGSETTING_PORT_THUMB;
        }
    }

    private void doDeleteCustomBackground(int position) {
        BackgroundItem mItem = mBackgroundItems.get(position);
        deleteBGFile(mItem.customImageFileName);
        imageCache.remove(mItem.customImageThumbPath);
        mBackgroundItems.remove(position);
    }

    private void deleteCustomImages() {

        SparseBooleanArray arr = mImageAdapter.checkedArray;
        int size = arr.size();
        for (int i = size; i > 0; i--) {
            int key = arr.keyAt(i - 1);
            if (arr.get(key)) {
                doDeleteCustomBackground(key);
            }
        }
        mImageAdapter.checkedArray.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        BackgroundItem mItem = mBackgroundItems.get(position);
        if (isDeleteMode) {
            CheckBox cb = (CheckBox) view.findViewById(R.id.check_delete);
            cb.setChecked(!cb.isChecked());
            if (cb.isChecked())
                mImageAdapter.checkedArray.put(position, cb.isChecked());
            else
                mImageAdapter.checkedArray.delete(position);
            mSelectAllCheckBox.setChecked(mImageAdapter.checkedArray.size() == mBackgroundItems.size());
            if (mImageAdapter.checkedArray.size() == 0) {
                mSelectedNumber.setText("Select image");
            } else {
                mSelectedNumber.setText("" + mImageAdapter.checkedArray.size());
            }
            mActivity.invalidateOptionsMenu();
        } else {
            if (position == 0) {
                mImageGridView.setOnItemClickListener(null);
                newTempFileName = createNewCacheFileName();
                tempOpaqueImageFileURI = Uri.fromFile(new File(getCustomCacheDirPath() + "opaque" + newTempFileName));
                chooseImageFromGallery();
            } else if (position > NO_OF_IMAGES_WITHOUT_CUSTOM) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_custom_image", true);
                File customCachepath = new File(getCustomCacheDirPath() + mItem.customImageFileName);
                String backup = (getCustomCacheDirPath() + mItem.customImageFileName)
                        .replace(AppConfig.getCacheDirExternal(),
                                mActivity.getFilesDir().getAbsolutePath() + File.separator + "bg_backup");
                File backupImage = new File(backup);
                if (customCachepath.exists()) {
                    bundle.putString("image_path", getCustomCacheDirPath() + mItem.customImageFileName);
//                    ((BGSettingActivity) mActivity).bgSettingMainFragment(bundle);
                } else if (backupImage.exists()) {
                    bundle.putString("image_path", backup);
//                    ((BGSettingActivity) mActivity).bgSettingMainFragment(bundle);
                } else {
                    newCroppedImageFilePath = getCustomCacheDirPath() + mItem.customImageFileName;
                    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
                        File tmp = new File(getCustomCacheOppositeDirpath() + mItem.customImageFileName);
                        Uri picUri = FileProvider.getUriForFile(mContext, "com.samsung.android.snote.fileprovider", tmp);
                        Intent cropIntent;
                        if (AppConfig.isGooglePhotosAvailable()) {
                            cropIntent = new Intent("com.android.camera.action.CROP", null);
                        } else {
                            cropIntent = new Intent("com.sec.android.gallery3d.app.CropImage");
                            cropIntent.setComponent(new ComponentName("com.sec.android.gallery3d",
                                    "com.sec.android.gallery3d.app.CropImage"));
                        }

                        cropIntent.setDataAndType(picUri, "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("return-data", true);
                        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
                        cropIntent.putExtra("noFaceDetection", true);
                        File tmp2 = new File(newCroppedImageFilePath);
                        Uri outputUri = FileProvider.getUriForFile(mContext, "com.samsung.android.snote.fileprovider",
                                tmp2);
                        cropIntent.putExtra("output", outputUri);
                        if (AppConfig.isGooglePhotosAvailable()) {
                            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(cropIntent,
                                    PackageManager.MATCH_DEFAULT_ONLY);
                            for (ResolveInfo resolveInfo : resInfoList) {
                                String packageName = resolveInfo.activityInfo.packageName;
                                mContext.grantUriPermission(packageName, picUri, Intent
                                        .FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                mContext.grantUriPermission(packageName, outputUri, Intent
                                        .FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        } else {
                            mContext.grantUriPermission("com.sec.android.gallery3d", picUri,
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            mContext.grantUriPermission("com.sec.android.gallery3d", outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent
                                    .FLAG_GRANT_READ_URI_PERMISSION);
                        }

                        try {
                            startActivityForResult(cropIntent, GET_EXTERNEL_CROPPED_IMAGE);
                        } catch (ActivityNotFoundException localActivityNotFoundException) {
                            mImageGridView.setOnItemClickListener(this);
                        }
                    } else {
                        Uri picUri =
                                Uri.fromFile(new File(getCustomCacheOppositeDirpath() + mItem.customImageFileName));
                        Intent cropIntent;
                        if (AppConfig.isGooglePhotosAvailable()) {
                            cropIntent = new Intent("com.android.camera.action.CROP", null);
                        } else {
                            cropIntent = new Intent("com.sec.android.gallery3d.app.CropImage");
                            cropIntent.setComponent(new ComponentName("com.sec.android.gallery3d",
                                    "com.sec.android.gallery3d.app.CropImage"));
                        }

                        cropIntent.setDataAndType(picUri, "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("return-data", true);
                        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
                        cropIntent.putExtra("noFaceDetection", true);
                        cropIntent.putExtra("output", Uri.fromFile(new File(newCroppedImageFilePath)));
                        try {
                            startActivityForResult(cropIntent, GET_EXTERNEL_CROPPED_IMAGE);
                        } catch (ActivityNotFoundException localActivityNotFoundException) {
                            mImageGridView.setOnItemClickListener(this);
                        }
                    }
                }
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_custom_image", false);
                bundle.putInt("image_id", mItem.actualImageId);
//                Intent intent = new Intent(AppConfig.ACTION_UPDATE_AVATAR);
//                getActivity().sendBroadcast(intent);

//                BackgroundItem backgroundItem = new BackgroundItem();
//                backgroundItem.actualImageId = mItem.actualImageId;
//                backgroundItem.thumbIDSmall = mItem.thumbIDSmall;
//                backgroundItem.msg = "MSG_SAMPLE_AVATAR_ITEM";
//                EventBus.getDefault().post(backgroundItem);


                BackgroundItem item = new BackgroundItem();
                item.customImageThumbPath = null;
                item.thumbID = mItem.actualImageId;
                item.thumbIDSmall = mItem.thumbIDSmall;

//                ((ImageAvatarChooserActivity) mActivity).setAvatarItem(item);

                getActivity().finish();
            }
        }
    }

    public boolean handleBackPress() {
        if (isDeleteMode) {
            isDeleteMode = false;
            mImageAdapter.checkedArray.clear();
            initBackgroundList();
            getActivity().invalidateOptionsMenu();
            mImageAdapter.notifyDataSetChanged();
            resetActionBar();
            return true;
        } else {
            return false;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mImageGridView.setOnItemClickListener(this);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                default:
                case GET_EXTERNEL_IMAGE:
//                    setOpacityImage();
//                    getSelectedImagePath(data);
                    if(data != null && data.getData() != null){
                        Uri uri = data.getData();
//                        Uri contentUri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        String realPath = uri.getPath();
                    }
                    break;
                case GET_EXTERNEL_CROPPED_IMAGE:
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("is_custom_image", true);
                    bundle.putString("image_path", newCroppedImageFilePath);
//                    ((BGSettingActivity) mActivity).bgSettingMainFragment(bundle);
                    break;
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_DELETE_MODE, isDeleteMode);
        outState.putString(NEW_TEMP_FILE_NAME, newTempFileName);
        SparseBooleanArray checkedArray = mImageAdapter.getCheckedArray();
        if (checkedArray != null) {
            final ArrayList<Integer> checkedItemList = new ArrayList<>(checkedArray.size());
            for (int i = 0; i < checkedArray.size(); i++) {
                if (checkedArray.valueAt(i)) {
                    checkedItemList.add(checkedArray.keyAt(i));
                }
            }
            outState.putIntegerArrayList(CHECKED_ITEMS, checkedItemList);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        } else {
            return;
        }
        inflater.inflate(R.menu.menu_image, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu == null) {
            return;
        }
        MenuItem delete = menu.findItem(R.id.action_bg_choose_delete);
        if (delete != null) {
            delete.setVisible((isDeleteMode == true) ? mImageAdapter.checkedArray.size() > 0 : isExistThumbImage());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mActivity.onBackPressed();
        }
        if (id == R.id.action_bg_choose_delete) {
            if (isDeleteMode) {
                displyDeleteDialog();
            } else {
                isDeleteMode = true;
                setDeleteModeView();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegisterMediaContentObserver();
        if (imageCache != null) {
            imageCache.evictAll();
        }
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    private void setDeleteModeView() {
        setSelectionModeActionBarCheckBox();
        for (int i = 0; i <= NO_OF_IMAGES_WITHOUT_CUSTOM; i++) {
            mBackgroundItems.remove(0);
        }
        mImageAdapter.notifyDataSetChanged();
        getActivity().invalidateOptionsMenu();
    }

    private String createNewCacheFileName() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return dateFormat.format(date) + PNG_EXT;
    }

    private void deleteBGFile(String name) {
        deleteFile(getCustomThumbDirPath() + name);
        if (isNoteLandTemplate) {
            deleteFile(BGSETTING_LAND_TEMPLATE_4_3 + File.separator + name);
            deleteFile(BGSETTING_LAND_TEMPLATE + File.separator + name);
            deleteFile(BGSETTING_LAND_BACKUP_4_3 + File.separator + name);
            deleteFile(BGSETTING_LAND_BACKUP + File.separator + name);
        } else {
            deleteFile(BGSETTING_PORT_TEMPLATE_4_3 + File.separator + name);
            deleteFile(BGSETTING_PORT_TEMPLATE + File.separator + name);
            deleteFile(BGSETTING_PORT_BACKUP_4_3 + File.separator + name);
            deleteFile(BGSETTING_PORT_BACKUP + File.separator + name);
        }
    }

    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                Toast.makeText(mActivity, "bg_setting_failed_to_delete_file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isExistThumbImage() {
        File file = new File(getCustomThumbDir());
        if (file.list() == null) {
            if (!file.exists()) {
                return false;
            }
        } else {
            if (!file.exists() || file.list().length == 0) {
                return false;
            }
        }
        return true;
    }


    private void setSelectionModeActionBarCheckBox() {
        View selectionActionBarView =
                LayoutInflater.from(mContext).inflate(R.layout.image_setting_selection_mode, null);
        final LinearLayout mSelectAllCheckBoxLayout;

        mSelectedNumber = (TextView) selectionActionBarView.findViewById(R.id.selectionmode_selectall_text);
        mSelectAllCheckBox =
                (CheckBox) selectionActionBarView.findViewById(R.id.selectionmode_selectall_checkbox);
        mSelectAllCheckBoxLayout =
                (LinearLayout) selectionActionBarView.findViewById(R.id.selectionmode_selectall_checkbox_layout);

        mSelectAllCheckBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performClickOnSelectAll();
            }

        });

        mSelectAllCheckBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((KeyEvent.KEYCODE_ENTER == event.getKeyCode()) && (event.getAction() == KeyEvent.ACTION_UP)) {
                    performClickOnSelectAll();
                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                }
                return false;
            }
        });

        if (mImageAdapter.checkedArray.size() == 0) {
            mSelectedNumber.setText("select_images");
        } else {
            mSelectedNumber.setText(String.format("%d", mImageAdapter.checkedArray.size()));
        }

//        mActionBar.setCustomView(selectionActionBarView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
//                ActionBar.LayoutParams.MATCH_PARENT));
//        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    private void performClickOnSelectAll() {
        boolean currentState = mSelectAllCheckBox.isChecked();
        for (int i = 0; i < mBackgroundItems.size(); i++) {
            if (!currentState)
                mImageAdapter.checkedArray.put(i, !currentState);
            else
                mImageAdapter.checkedArray.delete(i);
        }
        mImageAdapter.notifyDataSetChanged();
        mSelectAllCheckBox.setChecked(!currentState);
        if (mImageAdapter.checkedArray.size() == 0) {
            mSelectedNumber.setText("Select image");
        } else {
            mSelectedNumber.setText("" + mImageAdapter.checkedArray.size());
        }
        mActivity.invalidateOptionsMenu();
    }

    private void resetActionBar() {
//        mActionBar.setDisplayOptions(
//                ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
    }


    public static class BackgroundItem implements Serializable{
        private int thumbID;
        private String customImageThumbPath = null;
        private int actualImageId;
        private String customImageFileName = null;
        private String msg;
        private int thumbIDSmall;

        public BackgroundItem() {
        }

        public BackgroundItem(int thumbID, String customImageThumbPath, int thumbIDSmall) {
            this.thumbID = thumbID;
            this.customImageThumbPath = customImageThumbPath;
            this.thumbIDSmall = thumbIDSmall;
        }

        public int getThumbID() {
            return thumbID;
        }

        public void setThumbID(int thumbID) {
            this.thumbID = thumbID;
        }

        public String getCustomImageThumbPath() {
            return customImageThumbPath;
        }

        public void setCustomImageThumbPath(String customImageThumbPath) {
            this.customImageThumbPath = customImageThumbPath;
        }

        public int getActualImageId() {
            return actualImageId;
        }

        public void setActualImageId(int actualImageId) {
            this.actualImageId = actualImageId;
        }

        public String getCustomImageFileName() {
            return customImageFileName;
        }

        public void setCustomImageFileName(String customImageFileName) {
            this.customImageFileName = customImageFileName;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getThumbIDSmall() {
            return thumbIDSmall;
        }

        public void setThumbIDSmall(int thumbIDSmall) {
            this.thumbIDSmall = thumbIDSmall;
        }
    }


    private class BackgroundImgAdapter extends BaseAdapter {
        private final Context context;
        private SparseBooleanArray checkedArray;


        private BackgroundImgAdapter(Context ctx) {
            context = ctx;
            checkedArray = new SparseBooleanArray();
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 16;
            imageCache = new LruCache<>(cacheSize);
        }

        public SparseBooleanArray getCheckedArray() {
            return checkedArray;
        }

        public void setCheckedArray(ArrayList<Integer> checkedItems) {
            if (checkedItems != null) {
                for (int i : checkedItems) {
                    checkedArray.put(i, true);
                }
            }
        }

        @Override
        public int getCount() {
            return mBackgroundItems.size();
        }

        @Override
        public BackgroundItem getItem(int paramInt) {
            return mBackgroundItems.get(paramInt);
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View localView = convertView;
            BackgroundItem item = mBackgroundItems.get(position);
            HolderView holder = null;
            if (localView == null) {
                localView = LayoutInflater.from(context).inflate(R.layout.item_change_category_img, parent, false);
                holder = new HolderView();
                holder.imageView = (ImageView) localView.findViewById(R.id.note_change_background_img_thumbnail);
                holder.checkBox = (CheckBox) localView.findViewById(R.id.check_delete);
                holder.checkBox.setBackgroundResource(R.drawable.tw_btn_check_bg_for_t);
                holder.checkBox.setButtonTintList(mContext.getColorStateList(R.color.color_white));

                holder.textView = (TextView) localView.findViewById(R.id.view_from_gallery);
                localView.setTag(holder);
            } else {
                holder = (HolderView) localView.getTag();
                holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            int itemWidth = 0, itemHeight = 0, itemShadowWidth = 0, itemShadowHeight = 0;

            itemWidth = (int) getResources().getDimension(R.dimen.dp_121);
            itemHeight = (int) getResources().getDimension(R.dimen.dp_121);
            itemShadowWidth = (int) getResources().getDimension(R.dimen.dp_122);
            itemShadowHeight = (int) getResources().getDimension(R.dimen.dp_122);

            ImageView imageViewShadow = (ImageView) localView.findViewById(R.id.note_change_background_img_shadow_thumbnail);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
            params.width = itemWidth;
            params.height = itemHeight;
            holder.imageView.setLayoutParams(params);
            RelativeLayout.LayoutParams paramsShadow = (RelativeLayout.LayoutParams) imageViewShadow.getLayoutParams();
            paramsShadow.width = itemShadowWidth;
            paramsShadow.height = itemShadowHeight;
            imageViewShadow.setLayoutParams(paramsShadow);

            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
            holder.textView.setVisibility(View.GONE);
            if (!isDeleteMode) {
                if (isExistThumbImage() && position > NO_OF_IMAGES_WITHOUT_CUSTOM) {
                    if (item.customImageThumbPath != null) {
                        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        loadImage(item.customImageThumbPath, holder.imageView);
                        //holder.imageView.setImageBitmap(convertFileToBitmap(new File(item.customImageThumbPath)));
                        holder.imageView.setContentDescription("double_tap_to_select");
                    }
                } else {
                    if (position == 0) {
                        if (mBitmap == null) {
                            int imageWidth, imageHeight;
                            imageWidth = imageHeight = 0;
//                            if (isNoteLandTemplate) {
//                                imageWidth = (int) getResources().getDimension(R.dimen.note_change_background_select_bg_page_custom_gallery_image_width_land_template);
//                                imageHeight = (int) getResources().getDimension(R.dimen.note_change_background_select_bg_page_custom_gallery_image_height_land_template);
//                            } else {
                            imageWidth = (int) getResources().getDimension(R.dimen.dp_42);
                            imageHeight = (int) getResources().getDimension(R.dimen.dp_42);
//                            }


                            Drawable drawable = getResources().getDrawable(R.drawable.bg_setting_image_change_custom);
                            drawable.setBounds(0, 0, imageWidth, imageHeight);
                            holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
                            holder.imageView.setImageDrawable(drawable);
                            holder.textView.setTextColor(getResources().getColor(R.color.color_black));
                        } else {
                            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            holder.imageView.setImageBitmap(mBitmap);
                        }
                        holder.imageView.setContentDescription("select_from_gallery");
                        holder.textView.setVisibility(View.VISIBLE);
                    } else {
                        holder.imageView.setContentDescription("double_tap_to_select");
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.RGB_565;

                        holder.imageView.setImageBitmap(AppConfig.getBitmap(item.thumbID, options));
                    }
                }
            } else {
                if (item.customImageThumbPath != null) {
                    loadImage(item.customImageThumbPath, holder.imageView);
                    //holder.imageView.setImageBitmap(convertFileToBitmap(new File(item.customImageThumbPath)));
                }
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(checkedArray.get(position, false));
                holder.imageView.setContentDescription("Image " + (position + 1));
            }
            return localView;
        }
    }

    public void loadImage(String data, ImageView imageView) {

        if (data == null) {
            return;
        }

        Bitmap bitmap = imageCache.get(data);
        if (bitmap != null) {
            setImageBitmap(imageView, bitmap);
            return;
        }

        if (cancelPotentialWork(data, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, data);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(), bitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(data);

        }
    }

    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            if (TextUtils.isEmpty(data)) {
                return false;
            }
            final Object bitmapData = bitmapWorkerTask.mPath;
            if (bitmapData == null || !bitmapData.equals(data)) {
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress.
                return false;
            }
        }
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    private class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {

        private String mPath = null;

        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView, String path) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            mPath = path;
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            Bitmap bitmap = null;

            synchronized (mPauseWorkLock) {
                while (mPauseWork && !isCancelled()) {
                    try {
                        mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }

            if (bitmap == null && !isCancelled() && getAttachedImageView() != null) {
                bitmap = convertFileToBitmap(new File(mPath));
                if (bitmap != null)
                    imageCache.put(mPath, bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // if cancel was called on this task or the "exit early" flag is set
            // then we're done
            if (isCancelled()) {
                bitmap = null;
            }

            final ImageView imageView = getAttachedImageView();
            if (bitmap != null && imageView != null && !bitmap.isRecycled()) {
                setImageBitmap(imageView, bitmap);
            }
        }

        @Override
        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
            synchronized (mPauseWorkLock) {
                mPauseWorkLock.notifyAll();
            }
        }

        private ImageView getAttachedImageView() {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }

            return null;
        }
    }

    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    private static class HolderView {
        ImageView imageView;
        CheckBox checkBox;
        TextView textView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGalleryImageChanged) {
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute();
            isGalleryImageChanged = false;
        }

        int color = AppConfig.getThemeColor();
        int colorAlpha = AppConfig.getAlpha1BackgroundColorSetting();

        mImageGridView.setBackgroundColor(colorAlpha);
    }

    private void registerMediaContentObserver() {
        registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    private void unRegisterMediaContentObserver() {
        if (mContext != null)
            mContext.getContentResolver().unregisterContentObserver(mGalleryUriContentObserver);
    }

    private void registerContentObserver(final Uri uri) {
        final Context context = getActivity();
        mGalleryUriContentObserver = new ContentObserver(null) {
            public void onChange(boolean selfChange) {
                isGalleryImageChanged = true;
            }
        };
        context.getContentResolver().registerContentObserver(uri, false, mGalleryUriContentObserver);
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            initBackgroundList();
            loadGalleryImage();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            mImageGridView.setAdapter(mImageAdapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isGalleryImageChanged = false;
        }
    }


    public void loadGalleryImage() {
//        Long time = System.currentTimeMillis();
        Bitmap bitmap = null;
        String[] columns = {MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.ORIENTATION, MediaStore.Images.ImageColumns.DATE_TAKEN};
        Cursor cursor = MediaStore.Images.Media.query(mActivity.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC LIMIT 1");

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(columns[0]));
                bitmap = MediaStore.Images.Thumbnails.getThumbnail(mActivity.getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                int orientation = cursor.getInt(cursor.getColumnIndex(columns[2]));
                if (orientation != 0 && bitmap != null)
                    bitmap = rotateBitmap(bitmap, orientation, true);
            }
            bitmap = maskingImage(bitmap);
            cursor.close();
        } else {
            mBitmap = null;
        }
        if (bitmap != null)
            mBitmap = bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap source, int rotation, boolean recycle) {
        if (rotation == 0) return source;
        int w = source.getWidth();
        int h = source.getHeight();
        Matrix m = new Matrix();
        m.postRotate(rotation);
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, w, h, m, true);
        if (recycle) source.recycle();
        return bitmap;
    }

    private Bitmap maskingImage(Bitmap originalBitmap) {
        if (originalBitmap == null)
            return null;

        int baseWidth = originalBitmap.getWidth();
        int baseHeight = originalBitmap.getHeight();

        Bitmap resultBitmap = Bitmap.createBitmap(baseWidth, baseHeight, Bitmap.Config.ARGB_8888);
        Bitmap maskBitmap = Bitmap.createBitmap(baseWidth, baseHeight, Bitmap.Config.ARGB_8888);
        Bitmap maskBitmapResource = BitmapFactory
                .decodeResource(getContext().getResources(), R.drawable.snote_thumbnail_custom_masking);
        Bitmap scaledMaskBitmap = Bitmap.createScaledBitmap(maskBitmapResource, baseWidth, baseHeight, true);

        Canvas mCanvas = new Canvas(resultBitmap);
        mCanvas.setDensity(originalBitmap.getDensity());
        mCanvas.drawBitmap(originalBitmap, 0, 0, null);

        Paint paintBlending = new Paint();
        paintBlending.setColor(getColorFromBitmap(originalBitmap));
        paintBlending.setStyle(Paint.Style.FILL);

        Canvas mask = new Canvas(maskBitmap);
        mask.drawRect(0, 0, baseWidth, baseHeight, paintBlending);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mask.drawBitmap(scaledMaskBitmap, 0, 0, paint);
        paint.setXfermode(null);

        Paint combinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        combinePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mCanvas.drawBitmap(maskBitmap, 0, 0, combinePaint);
        combinePaint.setXfermode(null);

        originalBitmap.recycle();
        maskBitmap.recycle();
        maskBitmapResource.recycle();
        scaledMaskBitmap.recycle();

        return resultBitmap;
    }

    private int getColorFromBitmap(Bitmap bitmap) {

        final int defaultColor = R.color.color_transparent;

//        Palette palette = new Palette.Builder(bitmap).generate();
//
//        if (palette == null)
//            return defaultColor;
//
//        Palette.Swatch swatch = palette.getVibrantSwatch();
//        if (swatch != null) {
//            if (swatch.getHsl()[2] > 0.55) {
//                swatch =
//                        palette.getDarkVibrantSwatch();  //Returns a dark and vibrant swatch from the palette. Might be null.
//            }
//        } else {
//            swatch = palette.getMutedSwatch(); //Returns a muted swatch from the palette. Might be null.
//        }

        int color = defaultColor;

//        if (swatch != null) {
//            color = swatch.getRgb();
//        } else {
////            LogUtil.d(TAG, "PaletteItem is null");
//        }
        return color;
    }

}

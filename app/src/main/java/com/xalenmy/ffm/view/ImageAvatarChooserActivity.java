package com.xalenmy.ffm.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.xalenmy.ffm.R;
import com.xalenmy.ffm.utils.AppConfig;
import com.xalenmy.ffm.utils.ComonUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

public class ImageAvatarChooserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AvatarSelectFromGalleryFragment mImageFragment = null;
    private ImageItem avatarItem = null;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_avatar_chooser);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getControlWidget();

        initControl();

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        mImageFragment = new AvatarSelectFromGalleryFragment();
//        ft.replace(R.id.frame_sample_avatar_layout, mImageFragment, "mImageFragment");
////        ft.addToBackStack(null);
//        ft.commit();
    }

    private void getControlWidget() {
        toolbar = (Toolbar) findViewById(R.id.avatar_chooser_toolbar);
        imageView = (ImageView) findViewById(R.id.img_avatar);
    }

    private void initControl() {
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


//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        photoPickerIntent.setType("image/*");

//        startActivityForResult(photoPickerIntent, 10000);


        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setPackage("com.sec.android.gallery3d");
        photoPickerIntent.putExtra("return-data", true);
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        photoPickerIntent.putExtra("noFaceDetection", true);
        startActivityForResult(photoPickerIntent, ComonUtils.CODE_SELECT_AVATAR_FROM_GALLERY);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (resultCode == RESULT_OK) {

            //pick image from gallery(sd card)
            if (requestCode == ComonUtils.CODE_SELECT_AVATAR_FROM_GALLERY) {
                setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_USER, imageReturnedIntent);
            } else if (getIntent().getAction().equals(ComonUtils.ACTION_AVATAR_FROM_GROUP)) {
                setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_GROUP, imageReturnedIntent);
            } else if (getIntent().getAction().equals(ComonUtils.ACTION_AVATAR_FROM_CATEGORY)) {
                setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_CATEGORY, imageReturnedIntent);
            } else if (getIntent().getAction().equals(ComonUtils.ACTION_AVATAR_FROM_MAIN)) {
                setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_MAIN, imageReturnedIntent);
            } else if (getIntent().getAction().equals(ComonUtils.ACTION_AVATAR_FROM_USER)) {
                setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_USER, imageReturnedIntent);
            } else if (getIntent().getAction().equals(ComonUtils.ACTION_AVATAR_FROM_GROUP_LIST)) {
                setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_GROUP_LIST, imageReturnedIntent);
            }
        }

        finish();
    }

    private void handleOnBackPress() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        toolbar.setBackgroundColor(AppConfig.getThemeColor());
    }

//    public void setOpacityImage(Bundle b){
////        Bundle bundle = new Bundle();
////        bundle.putString("temp_opaque_file", getCustomCacheDirPath() + "opaque" + newTempFileName);
////        bundle.putString("temp_cache_image", getCustomCacheDirPath() + newTempFileName);
////        bundle.putString("temp_file_name", newTempFileName);
//
//
//
//    }

    public void setAvatarItem(ImageItem item) {
        this.avatarItem = item;

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ComonUtils.AVATAR_BUNDLE_RESULT, item);
        intent.putExtra(ComonUtils.AVATAR_INTENT_RESULT, bundle);

        if (getIntent().getAction().equals(ComonUtils.ACTION_AVATAR_FROM_GROUP)) {
            setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_GROUP, intent);
        } else if (getIntent().getAction().equals(ComonUtils.ACTION_AVATAR_FROM_CATEGORY)) {
            setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_CATEGORY, intent);
        } else if (getIntent().getAction().equals(ComonUtils.ACTION_AVATAR_FROM_MAIN)) {
            setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_MAIN, intent);
        } else if (getIntent().getAction().equals(ComonUtils.ACTION_AVATAR_FROM_USER)) {
            setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_USER, intent);
        } else if (getIntent().getAction().equals(ComonUtils.ACTION_AVATAR_FROM_GROUP_LIST)) {
            setResult(ComonUtils.CODE_SELECT_AVATAR_FROM_GROUP_LIST, intent);
        }
    }

    public static class ImageItem implements Serializable {
        private int thumbID;
        private String customImageThumbPath = null;
        private int actualImageId;
        private String customImageFileName = null;
        private String msg;
        private int thumbIDSmall;
        private Bitmap bitmap;
        private Uri uri;


        public ImageItem() {
        }

        public ImageItem(int thumbID, String customImageThumbPath, int thumbIDSmall) {
            this.thumbID = thumbID;
            this.customImageThumbPath = customImageThumbPath;
            this.thumbIDSmall = thumbIDSmall;
        }

        public ImageItem(int thumbID, String customImageThumbPath, int actualImageId, String customImageFileName, String msg, int thumbIDSmall, Bitmap bitmap, Uri uri) {
            this.thumbID = thumbID;
            this.customImageThumbPath = customImageThumbPath;
            this.actualImageId = actualImageId;
            this.customImageFileName = customImageFileName;
            this.msg = msg;
            this.thumbIDSmall = thumbIDSmall;
            this.bitmap = bitmap;
            this.uri = uri;
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

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public Uri getUri() {
            return uri;
        }

        public void setUri(Uri uri) {
            this.uri = uri;
        }
    }
}

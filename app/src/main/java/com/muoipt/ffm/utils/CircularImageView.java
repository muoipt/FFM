package com.muoipt.ffm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.muoipt.ffm.R;

import java.io.File;

/**
 * Created by XalenMy on 4/3/2018.
 */

public class CircularImageView extends android.support.v7.widget.AppCompatImageView {

    private String imgPath;

    public CircularImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private Bitmap getAvatarBitmapFromPath(String imgPath) {
        File image = new File(imgPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) getResources().getDimension(R.dimen.dp_36), (int) getResources().getDimension(R.dimen.dp_36), true);

        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap bitmap = null;
//        if (AppConfig.current_category_avatar_img_filepath != null && !AppConfig.current_category_avatar_img_filepath.equals("")) {
//            bitmap = getAvatarBitmapFromPath(AppConfig.current_category_avatar_img_filepath);
//        } else {
            Bitmap b = ((BitmapDrawable) drawable).getBitmap();
            bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
//        }
        int w = getWidth(), h = getHeight();

        Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
                    false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
//        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(AppConfig.getThemeColor());
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f,
                finalBitmap.getHeight() / 2 + 0.7f,
                finalBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }
}

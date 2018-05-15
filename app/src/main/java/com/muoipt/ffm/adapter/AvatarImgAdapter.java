package com.muoipt.ffm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by XalenMy on 4/3/2018.
 */

public class AvatarImgAdapter extends BaseAdapter {
    private Context context = null;
    private SparseBooleanArray checkedArray;
    private LruCache<String, Bitmap> imageCache;
    private int maxMemory = 0;
    private int cacheSize = 0;

    public AvatarImgAdapter(Context c){
        this.context = c;
        checkedArray = new SparseBooleanArray();
        maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        cacheSize = maxMemory / 16;
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
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}

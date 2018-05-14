package com.xalenmy.ffm.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.xalenmy.ffm.R;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvatarSelectFromGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvatarSelectFromGalleryFragment extends  android.app.Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView mImageGridView;


    public AvatarSelectFromGalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AvatarSelectFromGalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AvatarSelectFromGalleryFragment newInstance(String param1, String param2) {
        AvatarSelectFromGalleryFragment fragment = new AvatarSelectFromGalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_avatar_chooser, container, false);
        mImageGridView = ((GridView) view.findViewById(R.id.sample_image_list));
        mImageGridView.setFocusable(true);
        mImageGridView.setGravity(Gravity.CENTER);
//        mImageAdapter = new ImageAvatarChooserFragment.BackgroundImgAdapter(mActivity);

        return view;
    }

    class AvatarItem implements Serializable {
        private int thumbID;
        private String customImageThumbPath = null;
        private int actualImageId;
        private String customImageFileName = null;
        private String msg;
        private int thumbIDSmall;

        public AvatarItem() {
        }

        public AvatarItem(int thumbID, String customImageThumbPath, int thumbIDSmall) {
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

}

package com.muoipt.ffm.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muoipt.ffm.MainActivity;
import com.muoipt.ffm.R;
import com.muoipt.ffm.adapter.MainSummaryRecycleViewAdapter;
import com.muoipt.ffm.control.CategoryDetailControl;
import com.muoipt.ffm.control.MainSummaryControl;
import com.muoipt.ffm.eventmodel.MainEventString;
import com.muoipt.ffm.eventmodel.MainSummaryFragmentEventInteger;
import com.muoipt.ffm.eventmodel.MainSummaryFragmentEventString;
import com.muoipt.ffm.listener.CustomTouchListener;
import com.muoipt.ffm.listener.OnRecycleViewItemClickListener;
import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.MainSummaryReport;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.muoipt.ffm.utils.ComonUtils.CODE_ADD_CATEGORY;
import static com.muoipt.ffm.utils.ComonUtils.CODE_UPDATE_CATEGORY;
import static com.muoipt.ffm.utils.ComonUtils.NEW_CATEGORY_BUNDLE;
import static com.muoipt.ffm.utils.ComonUtils.NEW_CATEGORY_INTENT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainSummaryFragment.OnMainSummaryFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainSummaryFragment extends Fragment {

    public static final int REQUEST_CODE_1 = 1;
    public static final String REQUEST_BUNDLE = "BUNDLE1";
    public static final String REQUEST_INTENT = "INTENT1";

    private RecyclerView recyclerView;
    private MainSummaryRecycleViewAdapter adapter;
    private Button btnSetting, btnReport, btnAddOutcome;
    private TextView txtTotal, txtSummary, txtCurrentMonth, txtDivider, txtDivideSum1, txtDivideSum2;
    private TextView banner_text1, banner_text2;
    private TextView txtDivider1, txtDivider2;
    private RelativeLayout layoutToolBanner;
    private LinearLayout layout_title_listView;

    private MainSummaryControl mainSummaryControl;
    private ArrayList<MainSummaryReport> arrMainSummary = null;

    private OnMainSummaryFragmentInteractionListener mListener;
    private UserDetail currentUser;

    private ArrayList<MainSummaryReport> arrDeletedCategories = new ArrayList<MainSummaryReport>();

    public MainSummaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_summary, container, false);
        getControlWidget(view);

        mainSummaryControl = new MainSummaryControl(this.getContext());
        arrMainSummary = new ArrayList<MainSummaryReport>();
        currentUser = AppConfig.getUserLogInInfor();

        arrMainSummary = mainSummaryControl.getMainSummaryReportByUserFromDB(currentUser);

        adapter = new MainSummaryRecycleViewAdapter(arrMainSummary, this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter.notifyDataSetChanged();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainSummaryFragmentInteractionListener) {
            mListener = (OnMainSummaryFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onMainSummaryFragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public interface OnMainSummaryFragmentInteractionListener {
        public void onMainSummaryFragmentInteraction(MainSummaryReport selectedCat);
    }

    private void getControlWidget(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_main_summary);
        txtDivider1 = (TextView) v.findViewById(R.id.txtDivider1);
        txtDivider2 = (TextView) v.findViewById(R.id.txtDivider2);
        txtDivider = (TextView) v.findViewById(R.id.txtDivider);
        layout_title_listView = (LinearLayout) v.findViewById(R.id.layout_title_listView);
    }

    private void processSelectedItemInList(MainSummaryReport selectedCat) {
        EventBus.getDefault().post(selectedCat);
        mListener.onMainSummaryFragmentInteraction(selectedCat);
    }

    public void processAddNewCategory() {
        Intent intent = new Intent(getContext(), CategoryAddNewActivity.class);
        startActivityForResult(intent, CODE_ADD_CATEGORY);
    }

    @Subscribe
    public void processEventInteger(MainSummaryFragmentEventInteger data) {
        processSelectedItemInList(arrMainSummary.get(data.getI()));
    }

    @Subscribe
    public void processEventString(MainSummaryFragmentEventString data) {
        if (data.getS().equals(ComonUtils.ACTION_DELETE_CATEGORY)) {
            int count = ComonUtils.deletedCategories.size();
            for (int i = 0; i < count; i++) {
                arrDeletedCategories.add(arrMainSummary.get(ComonUtils.deletedCategories.get(i)));
            }

            for (int i = 0; i < count; i++) {
                arrMainSummary.remove(arrDeletedCategories.get(i));
            }

            deleteCategoriesInDB();

            ComonUtils.deletedCategories.clear();
            arrDeletedCategories.clear();

            Toast.makeText(this.getActivity(), "Delete categories successfully", Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new MainEventString(ComonUtils.ACTION_REFRESH_ALL));
        }
    }

    private void deleteCategoriesInDB() {
        CategoryDetailControl categoryDetailControl = new CategoryDetailControl(this.getActivity());
        categoryDetailControl.deleteCategoryTemporary(arrDeletedCategories);
    }

    @Override
    public void onResume() {
        super.onResume();

        layout_title_listView.setBackgroundColor(AppConfig.getAlpha1BackgroundColorSetting());
        txtDivider1.setBackgroundColor(AppConfig.getAlpha2BackgroundColorSetting());
        txtDivider2.setBackgroundColor(AppConfig.getAlpha2BackgroundColorSetting());
        txtDivider.setBackgroundColor(AppConfig.getAlpha2BackgroundColorSetting());


    }
}

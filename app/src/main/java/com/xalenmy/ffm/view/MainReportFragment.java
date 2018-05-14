package com.xalenmy.ffm.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xalenmy.ffm.MainActivity;
import com.xalenmy.ffm.R;
import com.xalenmy.ffm.adapter.ReportDetailDisplayRecycleViewAdapter;
import com.xalenmy.ffm.control.ReportDetailControl;
import com.xalenmy.ffm.control.ReportDetailDisplayControl;
import com.xalenmy.ffm.eventmodel.MainEventString;
import com.xalenmy.ffm.eventmodel.MainReportFragmentEventString;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainReportFragment.OnMainReportFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainReportFragment extends Fragment {

    private OnMainReportFragmentInteractionListener mListener;

    private List<ReportDetailDisplay> arrReports;
    private RecyclerView recyclerView;
    private TextView txtDivider;
    private LinearLayout layout_title_listView_report;
    private ReportDetailDisplayRecycleViewAdapter adapter;
    private ReportDetailDisplayControl control;
    public static String selectedCategoryName = null;
    private CategoryDetail selectedCategory = null;
    private List<ReportDetailDisplay> arrDeletedReports = null;

    public MainReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


//        if (selectedCategoryName == null) {
//            arrReports = control.getReportDetailDisplayFromDBByUser();
//        } else {
//            arrReports = control.getDetailReportFromDBByCategory(selectedCategoryName);
//        }
//
//        adapter = new ReportDetailDisplayRecycleViewAdapter(this, arrReports);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
//
//        adapter.notifyDataSetChanged();
//
//        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//            }
//        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_report, container, false);
        getControlWidget(view);



        control = new ReportDetailDisplayControl(this.getContext());
        arrDeletedReports = new ArrayList<ReportDetailDisplay>();
//        arrReports = control.getReportDetailDisplayFromDBByUser();
//        adapter = new ReportDetailDisplayRecycleViewAdapter(this, arrReports);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
////        recyclerView.addOnItemTouchListener(new CustomTouchListener(this.getContext(), new OnRecycleViewItemClickListener() {
////            @Override
////            public void onClick(View view, int index) {
////                processSelectedItemInList(index);
////            }
////        }));
//
////        recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
////            @Override
////            public boolean onLongClick(View v) {
////                return false;
////            }
////        });
//
//        adapter.notifyDataSetChanged();



        return view;
    }

    public void onButtonPressed(int position) {
        if (mListener != null) {
            mListener.onMainReportFragmentInteraction(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainReportFragmentInteractionListener) {
            mListener = (OnMainReportFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        selectedCategoryName = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        selectedCategoryName = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMainReportFragmentInteractionListener {
        void onMainReportFragmentInteraction(int position);
    }

    private void getControlWidget(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewMainReport);
        txtDivider = (TextView) v.findViewById(R.id.divider_report_recycle_view);
        layout_title_listView_report = (LinearLayout) v.findViewById(R.id.report_detail_display_row_title_layout);
    }


    @Override
    public void onResume() {
        super.onResume();

        layout_title_listView_report.setBackgroundColor(AppConfig.getAlpha1BackgroundColorSetting());
        txtDivider.setBackgroundColor(AppConfig.getAlpha2BackgroundColorSetting());

        UserDetail currentUser = AppConfig.getUserLogInInfor();
        if (currentUser == null || currentUser.getUserStatus() != 1) {
            return;
        }

        if (selectedCategoryName == null) {
            arrReports = control.getReportDetailDisplayFromDBByUser(currentUser);
        } else {
            arrReports = control.getDetailReportFromDBByCategory(selectedCategoryName);
        }

        adapter = new ReportDetailDisplayRecycleViewAdapter(this, arrReports);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter.notifyDataSetChanged();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe
    public void processDisplayWithSelectedCat(MainSummaryReport summaryReport) {
        selectedCategoryName = summaryReport.getCategory();
        arrReports = control.getDetailReportFromDBByCategory(selectedCategoryName);
        adapter = new ReportDetailDisplayRecycleViewAdapter(arrReports, this.getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void process_string(MainReportFragmentEventString str) {
        if (str.getS().equals(ComonUtils.ACTION_DELETE_REPORT)) {

            int count = ComonUtils.deletedReports.size();
            for (int i = 0; i < count; i++) {
                arrDeletedReports.add(arrReports.get(ComonUtils.deletedReports.get(i)));
            }

            for (int i = 0; i < count; i++) {
                arrReports.remove(arrDeletedReports.get(i));
            }

            deleteReportInDB();

            ComonUtils.deletedReports.clear();
            arrDeletedReports.clear();

            Toast.makeText(this.getActivity(), "Delete report successfully", Toast.LENGTH_LONG).show();

            EventBus.getDefault().post(new MainEventString(ComonUtils.ACTION_REFRESH_ALL));

        } else if (str.getS().equals(ComonUtils.ACTION_SHOW_REPORT)) {
            UserDetail currentUser = AppConfig.getUserLogInInfor();

            if (selectedCategoryName != null) {
                arrReports = control.getDetailReportFromDBByCategory(selectedCategoryName);
            } else {
                arrReports = control.getReportDetailDisplayFromDBByUser(currentUser);
            }
        }

        adapter = new ReportDetailDisplayRecycleViewAdapter(arrReports, this.getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void processClickItemInList(int position) {
        ReportDetailDisplay selectedReport = arrReports.get(position);

        Intent intent = new Intent(getActivity(), ReportAddNewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ComonUtils.UPDATE_REPORT_BUNDLE, selectedReport);
        intent.putExtra(ComonUtils.UPDATE_REPORT_INTENT, bundle);

        startActivityForResult(intent, ComonUtils.CODE_UPDATE_REPORT);
    }

    private void processLongClickItemInList(int position) {
        arrDeletedReports.add(arrReports.get(position));
        EventBus.getDefault().post(new MainReportFragmentEventString(ComonUtils.ACTION_DELETE_REPORT));
    }

    public void onItemRecycleViewClicked(int position) {
        processClickItemInList(position);

    }

    public void onItemRecycleViewLongClicked(int position) {
        processLongClickItemInList(position);
    }

    private void deleteReportInDB() {
        ReportDetailControl reportDetailControl = new ReportDetailControl(this.getActivity());
        reportDetailControl.deleteReports(arrDeletedReports);
    }



}

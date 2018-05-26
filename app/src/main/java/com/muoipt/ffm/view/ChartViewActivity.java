package com.muoipt.ffm.view;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Paint.Align;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;

import com.muoipt.ffm.R;
import com.muoipt.ffm.control.MainSummaryControl;
import com.muoipt.ffm.model.MainSummaryReport;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;

import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Random;

public class ChartViewActivity extends AppCompatActivity {

    private View chart;
    private Button btnWatchByMonth, btnWatchByWeek, btnWatchByToday;
    private ImageButton btnChartPie, btnChartBar;
    private TextView txtBarChartTitle;
    private Button btnYear;
    private FrameLayout chartContainer;
    private BottomNavigationView navigation;
    private LinearLayout layout_option, layout_title, layout_chart_label;
    private MainSummaryControl mainSummaryControl;
    private ArrayList<MainSummaryReport> arrMainSummary = null;
    private ArrayList<String> codes;
    private ArrayList<Integer> distributions;
    private ArrayList<Integer> colors;
    private ArrayList<Integer> incomes, outcomes;
    private int nav_pos = 0;
    private int selected_option = 0;
    private String selected_year;
    private int max_y_value = 0;
    private UserDetail currentUser;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_income:
                    nav_pos = 0;
                    pieChart();
                    return true;
                case R.id.navigation_outcome:
                    nav_pos = 1;
                    pieChart();
                    return true;
            }

            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getControlWidget();

        initControl();

        btnWatchByMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_option = 1;
                preparePieChartByMonth();
                createPieChart();
            }
        });

        btnWatchByWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_option = 2;
                preparePieChartByWeek();
                createPieChart();
            }
        });

        btnWatchByToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_option = 3;
                preparePieChartByToday();
                createPieChart();
            }
        });

        btnChartPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChart();
            }
        });

        btnChartBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barChart();
            }
        });

        btnYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processShowYearList();
            }
        });
    }

    private void getControlWidget() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        chartContainer = (FrameLayout) findViewById(R.id.chartContainer);
        btnWatchByMonth = (Button) findViewById(R.id.btnWatchByMonth);
        btnWatchByWeek = (Button) findViewById(R.id.btnWatchByWeek);
        btnWatchByToday = (Button) findViewById(R.id.btnWatchByDay);
        btnChartPie = (ImageButton) findViewById(R.id.chart_type_pie);
        btnChartBar = (ImageButton) findViewById(R.id.chart_type_bar);
        layout_option = (LinearLayout) findViewById(R.id.layout_option);
        layout_title = (LinearLayout) findViewById(R.id.layout_title);
        layout_chart_label = (LinearLayout) findViewById(R.id.layout_chart_label);
        txtBarChartTitle = (TextView) findViewById(R.id.txtBarChartTitle);
        btnYear = (Button) findViewById(R.id.btn_year);


    }

    private void initControl() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        chartContainer.removeAllViews();
        mainSummaryControl = new MainSummaryControl(this);
        colors = new ArrayList<Integer>();
        currentUser = AppConfig.getUserLogInInfor();
        incomes = new ArrayList<Integer>();
        outcomes = new ArrayList<Integer>();
        selected_year = ComonUtils.getCurrentYear();
        btnYear.setText(selected_year);
        nav_pos = 0;
        selected_option = 1;

        preparePieChart();
        createPieChart();
    }

    private void preparePieChartByMonth() {

        arrMainSummary = new ArrayList<MainSummaryReport>();
        codes = new ArrayList<String>();
        distributions = new ArrayList<Integer>();

        if (nav_pos == 0) {
            arrMainSummary = mainSummaryControl.getMainSummaryReportByTypeByUserByMonthFromDB(getResources().getString(R.string.data_type_income), currentUser);
        } else {
            arrMainSummary = mainSummaryControl.getMainSummaryReportByTypeByUserByMonthFromDB(getResources().getString(R.string.data_type_outcome), currentUser);
        }
    }

    private void preparePieChartByWeek() {
        arrMainSummary = new ArrayList<MainSummaryReport>();
        codes = new ArrayList<String>();
        distributions = new ArrayList<Integer>();

        if (nav_pos == 0) {
            arrMainSummary = mainSummaryControl.getMainSummaryReportByTypeByUserByWeekFromDB(getResources().getString(R.string.data_type_income), currentUser);
        } else {
            arrMainSummary = mainSummaryControl.getMainSummaryReportByTypeByUserByWeekFromDB(getResources().getString(R.string.data_type_outcome), currentUser);
        }
    }

    private void preparePieChartByToday() {
        arrMainSummary = new ArrayList<MainSummaryReport>();
        codes = new ArrayList<String>();
        distributions = new ArrayList<Integer>();

        if (nav_pos == 0) {
            arrMainSummary = mainSummaryControl.getMainSummaryReportByTypeByUserByTodayFromDB(getResources().getString(R.string.data_type_income), currentUser);
        } else {
            arrMainSummary = mainSummaryControl.getMainSummaryReportByTypeByUserByTodayFromDB(getResources().getString(R.string.data_type_outcome), currentUser);
        }
    }

    private int getRandomColor() {
        Random random = new Random();
        int R = random.nextInt(255);
        int G = random.nextInt(255);
        int B = random.nextInt(255);
        return Color.rgb(R, G, B);
    }

    private void processDisplayPieChart() {
        layout_option.setVisibility(View.VISIBLE);
        navigation.setVisibility(View.VISIBLE);
        layout_title.setVisibility(View.GONE);
        layout_chart_label.setVisibility(View.GONE);
        txtBarChartTitle.setVisibility(View.GONE);
        btnYear.setVisibility(View.GONE);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) chartContainer.getLayoutParams();
        params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.dp_8));
        chartContainer.setLayoutParams(params);
        chartContainer.setPadding((int) getResources().getDimension(R.dimen.dp_16), (int) getResources().getDimension(R.dimen.dp_36), 0, 0);
        chartContainer.setBackgroundColor(getColor(R.color.color_white));
    }

    private void preparePieChart() {
        if (selected_option == 1) {
            preparePieChartByMonth();
        } else if (selected_option == 2) {
            preparePieChartByWeek();
        } else if (selected_option == 3) {
            preparePieChartByToday();
        } else {
            preparePieChartByMonth();
        }
    }

    private void createPieChart() {
        for (int i = 0; i < arrMainSummary.size(); i++) {
            codes.add(arrMainSummary.get(i).getCategory());
            distributions.add(arrMainSummary.get(i).getAmount());
        }

        if (chartContainer.getChildAt(0) != null) {
            chartContainer.removeViewAt(0);
            chartContainer.removeView((View) chart.getParent());
        }

        CategorySeries distributionSeries = new CategorySeries("");
        for (int i = 0; i < distributions.size(); i++) {
            distributionSeries.add(codes.get(i), distributions.get(i));
        }

        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < distributions.size(); i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(getRandomColor());
            seriesRenderer.setDisplayBoundingPoints(true);
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        if (nav_pos == 0) {
            String title = getString(R.string.chart_des_income_title) + " ";
            defaultRenderer.setChartTitle(title.concat(ComonUtils.getCurrentMonth()));

            if (selected_option == 1)
                defaultRenderer.setChartTitle(title.concat(ComonUtils.getCurrentMonth()));
            else if (selected_option == 2)
                defaultRenderer.setChartTitle(title.concat(ComonUtils.getCurrentWeek()));
            else if (selected_option == 3)
                defaultRenderer.setChartTitle(title.concat(ComonUtils.getCurrentStrDatetimeFormatMMMdd()));
        } else {
            String title = getString(R.string.chart_des_outcome_title) + " ";
            defaultRenderer.setChartTitle(title.concat(ComonUtils.getCurrentMonth()));
            if (selected_option == 1)
                defaultRenderer.setChartTitle(title.concat(ComonUtils.getCurrentMonth()));
            else if (selected_option == 2)
                defaultRenderer.setChartTitle(title.concat(ComonUtils.getCurrentWeek()));
            else if (selected_option == 3)
                defaultRenderer.setChartTitle(title.concat(ComonUtils.getCurrentStrDatetimeFormatMMMdd()));
        }

//        defaultRenderer.setChartTitleTextSize(58);
        defaultRenderer.setChartTitleTextSize(getResources().getDimension(R.dimen.text_size_normal));
        defaultRenderer.setLabelsTextSize(40);
        defaultRenderer.setLegendTextSize(40);
        defaultRenderer.setPanEnabled(false);
        defaultRenderer.setClickEnabled(false);
        defaultRenderer.setZoomEnabled(false);
        defaultRenderer.setDisplayValues(true);
        defaultRenderer.setLabelsColor(R.color.color_black);
        defaultRenderer.setShowLabels(true);
        defaultRenderer.setStartAngle(0);
        defaultRenderer.setShowTickMarks(true);
        defaultRenderer.setShowLegend(true);

        chart = ChartFactory.getPieChartView(this, distributionSeries, defaultRenderer);

        chartContainer.addView(chart);
    }

    private void prepareBarChart() {
        int year = Integer.parseInt(selected_year);
        incomes = mainSummaryControl.getIncomesByMonths(year);
        outcomes = mainSummaryControl.getOutcomesByMonths(year);
        max_y_value = getMaxYValue() + 500;
    }

    private int getMaxYValue() {
        int maxY = 0;
        for (Integer i : incomes) {
            if (i > maxY) {
                maxY = i;
            }
        }

        for (Integer j : outcomes) {
            if (j > maxY) {
                maxY = j;
            }
        }
        return maxY;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createBarChart() {


        String[] mMonths = new String[]{
                getString(R.string.jan), getString(R.string.feb),
                getString(R.string.mar), getString(R.string.apr),
                getString(R.string.may), getString(R.string.jun),
                getString(R.string.jul), getString(R.string.aug),
                getString(R.string.sep), getString(R.string.oct),
                getString(R.string.nov), getString(R.string.dec)
        };

        int count = incomes.size();

        XYSeries incomeSeries = new XYSeries(getString(R.string.default_type_income));
        XYSeries expenseSeries = new XYSeries(getString(R.string.default_type_outcome));
        for (int i = 0; i < count; i++) {
            incomeSeries.add(i, incomes.get(i));
            expenseSeries.add(i, outcomes.get(i));
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(incomeSeries);
        dataset.addSeries(expenseSeries);

        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(getColor(R.color.main_color_banner));
        incomeRenderer.setFillPoints(false);
        incomeRenderer.setLineWidth(1);
        incomeRenderer.setChartValuesTextSize(30);
        incomeRenderer.setDisplayChartValues(true);
        incomeRenderer.setAnnotationsTextSize(40);
        incomeRenderer.setChartValuesTextAlign(Align.CENTER);

        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(getColor(R.color.red_color));
        expenseRenderer.setFillPoints(false);
        expenseRenderer.setLineWidth(10);
        expenseRenderer.setChartValuesTextSize(30);
        expenseRenderer.setDisplayChartValues(true);
        expenseRenderer.setAnnotationsTextSize(40);
        expenseRenderer.setDisplayChartValuesDistance(40);
        expenseRenderer.setChartValuesTextAlign(Align.CENTER);

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setYLabels(1);
        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setChartTitleTextSize(48);
        multiRenderer.setLabelsTextSize(30);
        multiRenderer.setLegendTextSize(30);
        multiRenderer.setFitLegend(true);
        multiRenderer.setLegendHeight(100);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setXAxisColor(Color.BLACK);
        multiRenderer.setAxesColor(Color.WHITE);
        multiRenderer.setYAxisColor(Color.WHITE);
        multiRenderer.setYLabelsColor(0, Color.WHITE);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setApplyBackgroundColor(true);

        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setClickEnabled(false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setDisplayValues(true);
        multiRenderer.setShowLabels(true);
        multiRenderer.setShowTickMarks(false);
        multiRenderer.setShowLegend(false);
        multiRenderer.setXAxisMax(count);
        multiRenderer.setYAxisMax(max_y_value);

        for (int i = 0; i < count; i++) {
            multiRenderer.addXTextLabel(i, mMonths[i]);
        }

        multiRenderer.addSeriesRenderer(incomeRenderer);
        multiRenderer.addSeriesRenderer(expenseRenderer);

        chart = ChartFactory.getBarChartView(getBaseContext(), dataset, multiRenderer, Type.DEFAULT);

        if (chartContainer.getChildAt(0) != null) {
            chartContainer.removeViewAt(0);
            chartContainer.removeView((View) chart.getParent());
        }

        chartContainer.addView(chart);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void processDisplayBarChart() {
        layout_option.setVisibility(View.INVISIBLE);
        navigation.setVisibility(View.INVISIBLE);
        layout_title.setVisibility(View.VISIBLE);
        txtBarChartTitle.setVisibility(View.VISIBLE);
        layout_chart_label.setVisibility(View.VISIBLE);
        btnYear.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) chartContainer.getLayoutParams();
        params.setMargins(20, 50, 20, 20);
        chartContainer.setLayoutParams(params);
        chartContainer.setPadding(20, 100, 20, 0);
        chartContainer.setBackgroundColor(getColor(R.color.color_white));
        layout_title.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
    }

    private void processShowYearList() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_listview_category_name);
        dialog.show();

        ListView listView = (ListView) dialog.findViewById(R.id.listCatNameAddReport);

        final ArrayList<String> yearList = mainSummaryControl.getYearList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, yearList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String year = yearList.get(position);
                btnYear.setText(year);
                selected_year = year;
                processShowBarChartByYear(year);
                dialog.dismiss();
            }
        });
    }

    private void processShowBarChartByYear(String year) {
        prepareBarChart();
        createBarChart();
    }

    private void pieChart() {
        processDisplayPieChart();
        preparePieChart();
        createPieChart();
    }

    private void barChart() {
        processDisplayBarChart();
        prepareBarChart();
        createBarChart();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        navigation.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        btnChartPie.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        btnChartPie.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));
        btnChartBar.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
        btnChartBar.setBackgroundTintList(ColorStateList.valueOf(getColor(AppConfig.current_theme_setting_color)));

        AppConfig.changeRoundViewColor(btnWatchByMonth, true);
        AppConfig.changeRoundViewColor(btnWatchByWeek, true);
        AppConfig.changeRoundViewColor(btnWatchByToday, true);

        layout_title.setBackgroundColor(getColor(AppConfig.current_theme_setting_color));
    }
}

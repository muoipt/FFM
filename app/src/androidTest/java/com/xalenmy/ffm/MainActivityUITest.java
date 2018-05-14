package com.xalenmy.ffm;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by XalenMy on 2/22/2018.
 */

public class MainActivityUITest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;

    public MainActivityUITest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        injectInsrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
    }
}

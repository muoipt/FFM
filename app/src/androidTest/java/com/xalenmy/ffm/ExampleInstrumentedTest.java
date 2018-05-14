package com.xalenmy.ffm;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xalenmy.ffm.view.CategoryAddNewActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.xalenmy.ffm", appContext.getPackageName());
    }

    @Test
    public void checkCateInputValue() throws Exception {
        MainActivity mainActivity = new MainActivity();
        FloatingActionButton t = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
        int i = t.getWidth();


        assertEquals(60, i);
    }


}

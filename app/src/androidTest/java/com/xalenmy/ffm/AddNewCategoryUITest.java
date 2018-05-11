package com.xalenmy.ffm;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.xalenmy.ffm.view.CategoryAddNewActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by XalenMy on 2/22/2018.
 */

public class AddNewCategoryUITest extends ActivityInstrumentationTestCase2<CategoryAddNewActivity> {

    private CategoryAddNewActivity activity;

    public AddNewCategoryUITest() {
        super(CategoryAddNewActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        injectInsrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
    }

    public void testChangeText_addNewCatActivity() {
        // Type text and then press the button.
        onView(withId(R.id.editAddCatName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.btnAddCatOk)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.editAddCatName)).check(matches(withText("abc")));
    }
}

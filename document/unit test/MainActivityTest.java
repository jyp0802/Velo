package company.velo.velo;

import android.os.IBinder;
import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.Root;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by jangsujin on 2017. 7. 26..
 */

@RunWith(AndroidJUnit4.class)

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void login_success() {

        try {
            onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
            //view is displayed logic

        } catch (NoMatchingViewException e) {
            //view not displayed logic

            onView(withId(R.id.emailInput)).perform(typeText("jsj@ucsc.edu"), closeSoftKeyboard());
            onView(withId(R.id.passwordInput)).perform(typeText("qwer"), closeSoftKeyboard());
            onView(withId(R.id.loginButton)).perform(click());

        }

    }

    @Test
    public void login_failure() {

        try {
            onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
            //view is displayed logic

        } catch (NoMatchingViewException e) {
            //view not displayed logic

            onView(withId(R.id.emailInput)).perform(typeText("jsj@ucsc.edu"), closeSoftKeyboard());
            onView(withId(R.id.loginButton)).perform(click());
            onView(withText("Login Failed")).
                    inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                    check(matches(isDisplayed()));
        }

    }

    @Test
    public void bike_list_page() {

        try {

            onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
            onView(withId(R.id.coordinator)).check(matches(isDisplayed()));
            onView(withId(R.id.viewpager)).check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {

        }

    }

}

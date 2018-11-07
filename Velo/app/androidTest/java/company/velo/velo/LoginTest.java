package company.velo.velo;

        import android.content.Context;
        import android.support.test.InstrumentationRegistry;
        import android.support.test.espresso.action.ViewActions;
        import android.support.test.espresso.assertion.ViewAssertions;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;

        import org.junit.Before;
        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import android.support.test.rule.ActivityTestRule;
        import android.test.ActivityInstrumentationTestCase2;
        import android.util.Log;

        import static android.support.test.espresso.Espresso.onView;
        import static android.support.test.espresso.action.ViewActions.click;
        import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
        import static android.support.test.espresso.action.ViewActions.typeText;
        import static android.support.test.espresso.assertion.ViewAssertions.matches;
        import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
        import static android.support.test.espresso.matcher.ViewMatchers.withId;
        import static android.support.test.espresso.matcher.ViewMatchers.withText;
        import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)

public class LoginTest extends ActivityInstrumentationTestCase2<Login> {

    private Login mActivity;
    private String ridingString = "Riding...";

    public LoginTest() {
        super(Login.class);
    }

    @Rule
    public ActivityTestRule<Login> mActivityRule = new ActivityTestRule<>(
            Login.class);

    /*
    @Before
    public void setUp() throws Exception {
        Log.d("LoginTest", "SETUP");
        super.setUp();

        // Injecting the Instrumentation instance is required
        // for your test to run with AndroidJUnitRunner.
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        Log.d("LoginTest", "getActivity");
    }
    */

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        //onView(withId(R.id.emailInput)).perform(ViewActions.typeText("jsj@ucsc.edu"));
        //onView(withId(R.id.passwordInput)).perform(ViewActions.typeText("qwer"));
        //onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.riding))
                .check(matches(withText(ridingString)));
    }

}

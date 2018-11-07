package company.velo.velo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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

public class FindUserTest extends ActivityInstrumentationTestCase2<FindUser> {


    public FindUserTest() {
        super(FindUser.class);
    }

    @Rule
    public ActivityTestRule<FindUser> mActivityRule = new ActivityTestRule<>(
            FindUser.class);

    @Test
    public void default_string() {

        onView(withId(R.id.button4))
                .check(matches(withText("Find User")));
    }

    @Test
    public void find_input_msg() {

        onView(withId(R.id.button4)).perform(click());
        onView(withText("Input")).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }


}

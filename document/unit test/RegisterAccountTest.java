package company.velo.velo;

import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by jangsujin on 2017. 7. 26..
 */


@RunWith(AndroidJUnit4.class)

public class RegisterAccountTest extends ActivityInstrumentationTestCase2<RegisterAccount> {

    private String title = "Sign Up";
    private String[] InputString = {"Name","ID","Password","Email","Phone Number"};

    public RegisterAccountTest() {
        super(RegisterAccount.class);
    }

    public static Matcher<View> hasTextInputLayoutHintText(final String expectedErrorText, final boolean is_error) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = null;

                if (is_error){
                    error = ((TextInputLayout) view).getError();
                } else {
                    error = ((TextInputLayout) view).getHint();
                }

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    @Rule
    public ActivityTestRule<RegisterAccount> mActivityRule = new ActivityTestRule<>(
            RegisterAccount.class);

    @Test
    public void default_string() {

        onView(withId(R.id.textView))
                .check(matches(withText(title)));
    }

    @Test
    public void input_display() {

        onView(withId(R.id.name_text)).check(matches(isDisplayed()));
        onView(withId(R.id.id_text)).check(matches(isDisplayed()));
        onView(withId(R.id.password_text)).check(matches(isDisplayed()));
        onView(withId(R.id.email_text)).check(matches(isDisplayed()));
        onView(withId(R.id.phone_number_text)).check(matches(isDisplayed()));

        onView(withId(R.id.button2)).check(matches(isDisplayed()));
    }

    @Test
    public void input_hint_show() {

        onView(withId(R.id.name_text_layout)).check(matches(hasTextInputLayoutHintText(InputString[0], false)));
        onView(withId(R.id.id_text_layout)).check(matches(hasTextInputLayoutHintText(InputString[1], false)));
        onView(withId(R.id.password_text_layout)).check(matches(hasTextInputLayoutHintText(InputString[2], false)));
        onView(withId(R.id.email_text_layout)).check(matches(hasTextInputLayoutHintText(InputString[3], false)));
        onView(withId(R.id.phone_number_text_layout)).check(matches(hasTextInputLayoutHintText(InputString[4], false)));

    }

    @Test
    public void input_error_show() {

        // error message for blank

        onView(withId(R.id.name_text)).perform(typeText("jang sujin"), closeSoftKeyboard());
        onView(withId(R.id.button2)).perform(click());

        String Enter = "Please enter ";

        onView(withId(R.id.id_text_layout)).check(matches(hasTextInputLayoutHintText(Enter+InputString[1], true)));
        onView(withId(R.id.password_text_layout)).check(matches(hasTextInputLayoutHintText(Enter+InputString[2], true)));
        onView(withId(R.id.email_text_layout)).check(matches(hasTextInputLayoutHintText(Enter+InputString[3], true)));
        onView(withId(R.id.phone_number_text_layout)).check(matches(hasTextInputLayoutHintText(Enter+InputString[4], true)));

    }
}
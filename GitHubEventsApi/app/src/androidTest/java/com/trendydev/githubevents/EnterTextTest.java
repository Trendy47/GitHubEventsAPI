package com.trendydev.githubevents;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Trendy on 9/15/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EnterTextTest {

    private String ownerTestString, repoTestString, eventTestString;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initValidString() {
        // set test string for this project's repo
        // and find all push events
        ownerTestString = "Trendy47";
        repoTestString = "SeeClickFixTest";
        eventTestString = "PushEvent";
    }

    @Test
    public void enterText_sameActivity() {
        // type text and then press the match button
        onView(withId(R.id.ownerEditText)).perform(typeText(ownerTestString));
        onView(withId(R.id.repoEditText)).perform(typeText(repoTestString));
        onView(withId(R.id.eventEditText)).perform(typeText(eventTestString), closeSoftKeyboard()); // close keyboard on last edit text

        onView(withId(R.id.matchButton)).perform(click());

        // check that the text was changed
        onView(withId(R.id.ownerEditText)).check(matches(withText(ownerTestString)));
        onView(withId(R.id.repoEditText)).check(matches(withText(repoTestString)));
        onView(withId(R.id.eventEditText)).check(matches(withText(eventTestString)));

        // go back
        onView(withId(R.id.backButton)).perform(click());
    }
}

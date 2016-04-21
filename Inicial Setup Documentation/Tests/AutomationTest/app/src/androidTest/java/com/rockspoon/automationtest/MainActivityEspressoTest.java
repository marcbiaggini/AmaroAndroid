package com.rockspoon.automationtest;

/**
 * Created by juancamilovilladuarte on 3/11/16.
 */

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.format.DateUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {

  private static final String USER_NAME = "Leanne Graham";

  @Rule
  public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule<>(MainActivity_.class);

  private static void waitFor(long waitingTime) {
    // Start
    // Type text and then press the button.
    onView(withId(R.id.inputField)).perform(typeText("Go to Next Scren"), closeSoftKeyboard());
    onView(withId(R.id.switchActivity)).perform(click());

    // Make sure Espresso does not time out
    IdlingPolicies.setMasterPolicyTimeout(waitingTime * 2, TimeUnit.MILLISECONDS);
    IdlingPolicies.setIdlingResourceTimeout(waitingTime * 2, TimeUnit.MILLISECONDS);

    // Now we wait
    IdlingResource idlingResource = new TimeIdlingResource(waitingTime);
    Espresso.registerIdlingResources(idlingResource);

    // Stop and get for users in the next Activity
    onView(withId(R.id.getUserButton)).perform(click());

    // Clean up
    Espresso.unregisterIdlingResources(idlingResource);
  }

  @Before
  public void resetTimeout() {
    IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
    IdlingPolicies.setIdlingResourceTimeout(26, TimeUnit.SECONDS);
  }

  @Test
  public void ensureTextChangesWork() {
    // Type Hello User in Edit Text.
    onView(withId(R.id.inputField)).perform(typeText("Hello User!"), closeSoftKeyboard());

    //and then press the button to Change The text.
    onView(withId(R.id.changeText)).perform(click());

    // Check that the text was changed.
    onView(withId(R.id.textViewHelloWorld)).check(matches(withText("Hello User!")));

  }

  @Test
  public void changeTo_newActivity() {
    //Wait For 5 Seconds
    waitFor(DateUtils.SECOND_IN_MILLIS * 5);
  }

}

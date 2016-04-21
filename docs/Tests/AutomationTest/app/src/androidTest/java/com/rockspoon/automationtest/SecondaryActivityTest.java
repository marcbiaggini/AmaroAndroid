package com.rockspoon.automationtest;

/**
 * Created by juancamilovilladuarte on 3/16/16.
 */

import android.os.Handler;
import android.support.test.espresso.contrib.CountingIdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.test.ActivityInstrumentationTestCase2;

import com.rockspoon.automationtest.Utils.HandlerEvents.Countdown;
import com.rockspoon.automationtest.Utils.HandlerEvents.EventCountDownHandler;

import org.junit.Before;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SecondaryActivityTest extends ActivityInstrumentationTestCase2<SecondaryActivity_> {

  public SecondaryActivityTest() {
    super(SecondaryActivity_.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    getActivity();
    onView(withId(R.id.getUserButton)).perform(click());
    final EventCountDownHandler eventCountDownHandler = new EventCountDownHandler();
    final CountingIdlingResource idlingResource = new CountingIdlingResource("Countdown");
    eventCountDownHandler.setCountdown(new CountdownWrapper(eventCountDownHandler.makeHandler(), idlingResource));
    registerIdlingResources(idlingResource);
  }

  @Before
  public void GetUser() {
    onView(withId(R.id.getUserButton)).perform(click());

  }

  public void testSelectUser() throws InterruptedException {
    onView(withId(R.id.user_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
  }

  private class CountdownWrapper extends Countdown {

    private final CountingIdlingResource idlingResource;

    public CountdownWrapper(Handler handler, CountingIdlingResource idlingResource) {
      super(handler);
      this.idlingResource = idlingResource;
    }

    @Override
    protected void onCountdownStarted() {
      idlingResource.increment();
    }

    @Override
    public void onCountdownFinished() {
      idlingResource.decrement();
    }

    @Override
    public void stop() {
      super.stop();
      idlingResource.decrement();
    }
  }
}

package com.rockspoon.automationtest;

/**
 * Created by juancamilovilladuarte on 3/16/16.
 */

import android.os.Handler;
import android.support.test.espresso.contrib.CountingIdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.test.ActivityInstrumentationTestCase2;

import com.rockspoon.automationtest.Models.User;
import com.rockspoon.automationtest.Utils.HandlerEvents.Countdown;
import com.rockspoon.automationtest.Utils.HandlerEvents.EventCountDownHandler;

import org.junit.Before;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;

public class ThirdActivityTest extends ActivityInstrumentationTestCase2<ThirdActivity_> {

  public ThirdActivityTest() {
    super(ThirdActivity_.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    getActivity();
    onView(withId(R.id.getUserListButton)).perform(click());
    final EventCountDownHandler eventCountDownHandler = new EventCountDownHandler();
    final CountingIdlingResource idlingResource = new CountingIdlingResource("Countdown");
    eventCountDownHandler.setCountdown(new CountdownWrapper(eventCountDownHandler.makeHandler(), idlingResource));
    registerIdlingResources(idlingResource);
  }

  @Before
  public void GetUser() {
    onView(withId(R.id.getUserListButton)).perform(click());

  }

  public void testSelectUser() throws InterruptedException {
    onData(instanceOf(User.class)).atPosition(0).perform(longClick());
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

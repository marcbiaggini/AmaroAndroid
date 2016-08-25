package amaro.amaroandroid;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import org.androidannotations.annotations.Background;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import amaro.amaroandroid.Helpers.MainHelper;
import amaro.api.Error.Error;
import amaro.api.Error.RestErrorEvent;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Helpers.BusProvider;
import amaro.api.Model.MyAccount.OrderInfo.OrdersResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by juan.villa on 26/05/2016.
 */
@RunWith(AndroidJUnit4.class)
public class OrdersScenario {

  MainHelper mainHelper = new MainHelper();

  @Before
  public void logOut() {
    AmaroServices.getMyAccount().logOut();
  }

  @Test
  public void getOrders() {
    try {
      mainHelper.checkoutOrder();
      OrdersResponse getOrders = AmaroServices.getMyAccount().getOrders();
      assertNotEquals(0, getOrders.getResponse().getOrders().size());
    } catch (Exception e) {
      Log.e("Error", e.getLocalizedMessage());
    }
  }


  @Test
  public void getEmptyOrders() {
    try {
      mainHelper.cleanOrders();
      OrdersResponse getOrders = AmaroServices.getMyAccount().getOrders();
      assertEquals(0, getOrders.getResponse().getOrders().size());
    } catch (Exception e) {
      Log.e("Error", e.getLocalizedMessage());
    }
  }

  protected static void onError(Error error) {

  }
}

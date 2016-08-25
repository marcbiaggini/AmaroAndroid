package amaro.amaroandroid;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.MySearch.SearchResult;

import static org.junit.Assert.assertEquals;

/**
 * Created by juan.villa on 01/08/2016.
 */
@RunWith(AndroidJUnit4.class)
public class MySearchScenarios {

  @Test
  public void mySearchAll() {
    SearchResult searchResult = AmaroServices.getMySearch().search("s=_&categoria=roupas");
    assertEquals("0", searchResult.getResponse_code());
  }

}

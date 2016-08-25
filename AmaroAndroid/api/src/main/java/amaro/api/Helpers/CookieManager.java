package amaro.api.Helpers;

import org.androidannotations.annotations.sharedpreferences.SharedPref;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;

/**
 * Created by juan.villa on 11/05/2016.
 */
@SharedPref(value= SharedPref.Scope.UNIQUE)
public interface CookieManager {
  String aecpDev();
  String aecpDevUser();
}

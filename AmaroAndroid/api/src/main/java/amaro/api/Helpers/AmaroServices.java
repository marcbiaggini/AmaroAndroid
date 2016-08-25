package amaro.api.Helpers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import amaro.api.Model.MyCart.CartInfo.Cart;
import amaro.api.Model.MyCart.LookupZipResponse;
import amaro.api.Model.MySearch.Filter.FilterValue;
import amaro.api.Model.MySearch.SearchResult;
import amaro.api.Model.MySearch.Sorting.Sorting;
import amaro.api.ServiceInterfaces.MyAccount;
import amaro.api.ServiceInterfaces.MyAccount_;
import amaro.api.Error.RSRestErrorHandler;
import amaro.api.ServiceInterfaces.MyCart;
import amaro.api.ServiceInterfaces.MyCart_;
import amaro.api.ServiceInterfaces.MyCheckOut;
import amaro.api.ServiceInterfaces.MyCheckOut_;
import amaro.api.ServiceInterfaces.MyProduct;
import amaro.api.ServiceInterfaces.MyProduct_;
import amaro.api.ServiceInterfaces.MySearch;
import amaro.api.ServiceInterfaces.MySearch_;

/**
 * Created by juan.villa on 03/05/2016.
 */
public class AmaroServices {

  protected static MyAccount myAccount = null;
  protected static MySearch mySearch =null;
  protected static MyCart myCart =null;
  protected static MyCheckOut myCheckOut = null;
  protected static MyProduct myProduct = null;
  protected static CookieManager_ cookieManager = null;
  protected static Cart cart = null;
  protected static LookupZipResponse lookupZipResponse = null;
  protected static List<Resource> filters;
  protected static List<Sorting> sortings;

  public static void initializeServices(Context context, RSRestErrorHandler errorHandler) {
    myAccount = new MyAccount_(context);
    myCart = new MyCart_(context);
    myCheckOut = new MyCheckOut_(context);
    mySearch = new MySearch_(context);
    myProduct = new MyProduct_(context);
    cookieManager = new CookieManager_(context);
    cart = new Cart();
    filters = new ArrayList<>();
    sortings = new ArrayList<>();
    lookupZipResponse = new LookupZipResponse();

    if (cookieManager.aecpDev().exists()) {
      setCookie("_aecp_dev", cookieManager.aecpDev().get(), false);
    }

    if (cookieManager.aecpDevUser().exists()) {
      setCookie("_aecp_dev_user", cookieManager.aecpDevUser().get(), false);
    }
  }
  public static Cart getCart() {
    return cart;
  }

  public static void setCart(Cart cart) {
    AmaroServices.cart = cart;
  }

  public static LookupZipResponse getLookupZipResponse() {
    return lookupZipResponse;
  }

  public static void setLookupZipResponse(LookupZipResponse lookupZipResponse) {
    AmaroServices.lookupZipResponse = lookupZipResponse;
  }

  public static List<Resource> getFilters() {
    return filters;
  }

  public static void setFilters(List<Resource> filters) {
    AmaroServices.filters = filters;
  }

  public static List<Sorting> getSortings() {
    return sortings;
  }

  public static void setSortings(List<Sorting> sortings) {
    AmaroServices.sortings = sortings;
  }

  public static MyAccount getMyAccount() {
    return myAccount;
  }

  public static MyCart getMyCart() {
    return myCart;
  }

  public static MyCheckOut getMyCheckOut() {
    return myCheckOut;
  }

  public static MySearch getMySearch() {
    return mySearch;
  }

  public static MyProduct getMyProduct(){return myProduct;}

  public static CookieManager_ getCookieManager() {
    return cookieManager;
  }

  public static void setCookie(String name, String value, boolean shouldPersist) {
    myAccount.setCookie(name, value);
    myCart.setCookie(name, value);
    myCheckOut.setCookie(name,value);

    CookieManager_.CookieManagerEditor_ cookieManagerEditor = cookieManager.edit();
    if (name.equals("_aecp_dev")) {
      cookieManagerEditor.aecpDev().put(value);
    } else if (name.equals("_aecp_dev_user")) {
      cookieManagerEditor.aecpDevUser().put(value);
    }
    cookieManagerEditor.apply();
  }

  public static void createFilters(SearchResult result) {
    List<Resource> filterList = new ArrayList<>();
    List<FilterValue> filterValues = new ArrayList<>();
    List<Sorting>sortOptions = new ArrayList<>();
    /*MustHave*/
    filterValues.addAll(result.getFilters().getMusthave().getValues());
    filterList.add(getResource(filterValues,result.getFilters().getMusthave().getName()));
    filterValues.clear();
    /*Cor*/
    filterValues.addAll(result.getFilters().getCor().getValues());
    filterList.add(getResource(filterValues,result.getFilters().getCor().getName()));
    filterValues.clear();
    /*Preço*/
    filterValues.addAll(result.getFilters().getPreco().getValues());
    filterList.add(getResource(filterValues,result.getFilters().getPreco().getName()));
    filterValues.clear();
    /*Tamanho*/
    filterValues.addAll(result.getFilters().getTamanho().getValues());
    filterList.add(getResource(filterValues,result.getFilters().getTamanho().getName()));
    filterValues.clear();
    /*Sort Options*/
    sortOptions.addAll(result.getSorting());
    AmaroServices.setSortings(sortOptions);
    AmaroServices.setFilters(filterList);
  }

  private static Resource getResource(List<FilterValue> filterValues, String name){
    Resource resource = new Resource();
    resource.setName(name);
    for (int i=0;i<filterValues.size();i++){
      Resource subcategory = new Resource();
      subcategory.setName(filterValues.get(i).getName());
      subcategory.setQuery(filterValues.get(i).getQuery_remove());
      resource.getSubcategory().add(subcategory);
    }
    return resource;
  }
}

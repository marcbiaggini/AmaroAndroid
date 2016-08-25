package amaro.amaroandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.squareup.otto.Subscribe;
import com.urbanairship.UAirship;
import com.urbanairship.google.PlayServicesUtils;
import com.urbanairship.richpush.RichPushInbox;
import com.urbanairship.richpush.RichPushMessage;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import amaro.amaroandroid.Adapters.CategoryPagerAdapter;
import amaro.amaroandroid.Adapters.ProductGridAdapter;
import amaro.amaroandroid.Adapters.SlideMenuListAdapter;
import amaro.amaroandroid.Areas.Account.AccountActivity_;
import amaro.amaroandroid.Areas.CheckOut.CartActivity_;
import amaro.amaroandroid.Areas.Login.LoginActivity_;
import amaro.amaroandroid.Fragments.FilterFragment;
import amaro.amaroandroid.Fragments.Message.LoadingFragment;
import amaro.amaroandroid.Fragments.CatalogFragment;
import amaro.amaroandroid.Model.ViewModels.NavItem;
import amaro.amaroandroid.Utils.ViewPager;
import amaro.api.*;
import amaro.api.Error.Error;
import amaro.api.Error.RestErrorEvent;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Helpers.BusProvider;
import amaro.api.Model.MyCart.CartInfo.GetCartResponse;
import amaro.api.Model.MySearch.SearchResult;
import amaro.api.ServiceInterfaces.MyAccount_;


@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements FilterFragment.CloseInterface {

  private static final int MESSAGE_CENTER_INDICATOR_DURATION_MS = 10000; // 10 seconds
  private static final int SAMPLE2_ID = 34535;
  private final Object busCallback = new Object() {
    @Subscribe
    public void handleRestErrorEvent(RestErrorEvent event) {
      Log.e("Amaro Error", event.getMessage());
      onError(new Error(event.getErrorCode(), event.getMessage()));
    }
  };
  @ViewById(R.id.tabs)
  PagerSlidingTabStrip tabs;
  @ViewById(R.id.pager)
  ViewPager pager;
  @ViewById(R.id.navList)
  ListView mDrawerList;
  @ViewById(R.id.drawerPane)
  RelativeLayout mDrawerPane;
  ActionBarDrawerToggle mDrawerToggle;
  @ViewById(R.id.drawerLayout)
  DrawerLayout mDrawerLayout;
  @ViewById(R.id.fabSort)
  FloatingActionButton fab;

  MenuItem itemBag, itemSearch, itemFilter;
  LoadingFragment loadingFragment = new LoadingFragment();
  FragmentManager fm = getSupportFragmentManager();
  FilterFragment filterFragment = new FilterFragment();
  String mainCategory = "ROUPAS";
  boolean isFiltered = false;


  CategoryPagerAdapter categoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager());
  private ContextMenuDialogFragment mMenuDialogFragment;
  private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
  private boolean isReceiverRegistered;
  private BroadcastReceiver mRegistrationBroadcastReceiver;
  private int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
  private int currentNavPosition = -1;
  private Snackbar messageCenterSnackbar;
  private long messageCenterLastSentDate;
  private RichPushInbox.Listener inboxListener = new RichPushInbox.Listener() {
    @Override
    public void onInboxUpdated() {
      showMessageCenterIndicator();
    }
  };
  private int badgeCount = 0;
  private SearchView searchView;
  private boolean isSearch = false;

  @AfterViews
  public void init() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle(mainCategory);
    //UAirship.shared().getPushManager().setAlias("Moto-X");
    loadingFragment.setCancelable(false);
    loadingFragment.show(fm, "fragment_loading_dialog");
    getProducts("s=_&categoria=roupas", mainCategory);
    getCart();
    setSlideMenu();

    fab.setImageDrawable(new IconicsDrawable(this)
        .icon(CommunityMaterial.Icon.cmd_sort_descending)
        .color(Color.WHITE)
        .sizeDp(12).getCurrent());
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        if (!isSearch) {
          AmaroServices.createFilters(getCurrentFragment().getSearchResult());
        }
        filterFragment.setType("sorting");
        if (filterFragment.getDialog() == null) {
          filterFragment.show(fm, "fragment_entrega_dialog");
        } else {
          filterFragment.getDialog().show();
        }
      }
    });
  }

  @Background
  public void getProducts(String s, String category) {
    getProducts(AmaroServices.getMySearch().search(s), category);
  }

  @UiThread
  public void getProducts(SearchResult result, String category) {
    loadingFragment.dismiss();
    setTitle(mainCategory);
    if (isSearch) {
      setTitle(getResources().getString(R.string.app_name));
      tabs.setVisibility(View.GONE);
    } else {
      tabs.setVisibility(View.VISIBLE);
    }
    if (isFiltered) {
      ProductGridAdapter productGridAdapter = new ProductGridAdapter(getCurrentFragment().setProducts(result.getProducts()), getApplicationContext(), this);
      getCurrentFragment().getProductsGrid().setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
      getCurrentFragment().getProductsGrid().setAdapter(productGridAdapter);
    } else {
      clearFragments();
      CategoryPagerAdapter categoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager());
      categoryPagerAdapter.setMainActivity(this);
      categoryPagerAdapter.setCategory(category);
      categoryPagerAdapter.setProductSearches(result.getProducts());
      for (int i = 0; i < categoryPagerAdapter.getTabquantity(); i++) {
        categoryPagerAdapter.addFragment(new CatalogFragment(), "Fragment " + category.toLowerCase() + i);
      }
      // Bind the tabs to the ViewPager
      pager.setAdapter(categoryPagerAdapter);
      tabs.setViewPager(pager);
    }
    AmaroServices.createFilters(result);
    mDrawerLayout.closeDrawer(Gravity.LEFT);
  }

  @Background
  public void getCart() {
    getCart(AmaroServices.getMyCart().getCart());
  }

  @UiThread
  public void getCart(GetCartResponse getCartResponse) {
    if (getCartResponse.getCode().equals("0")) {
      AmaroServices.setCart(getCartResponse.getCart());
      badgeCount = getCartResponse.getCart().getItems().size();
      if (badgeCount > 0) {
        ActionItemBadge.update(this, itemBag, new IconicsDrawable(this)
            .icon(FontAwesome.Icon.faw_shopping_bag)
            .color(Color.BLACK)
            .sizeDp(18).getCurrent(), ActionItemBadge.BadgeStyles.RED, badgeCount);
      } else {
        if (itemBag != null) {
          ActionItemBadge.hide(itemBag);
          itemBag.setVisible(true);
        }
      }
    }
  }

  @Click(R.id.welcomeLayout)
  public void myAccount(){
    if(AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.AUTENTICATION) == ""){
      LoginActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
    }else{
      AccountActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
    }
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    //Inflater Font-Awesome
    LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
    super.onCreate(savedInstanceState);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);

    itemBag = menu.findItem(R.id.bag);
    itemSearch = menu.findItem(R.id.search);
    itemFilter = menu.findItem(R.id.filter);

    searchView = (SearchView) itemSearch.getActionView();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        if (!searchView.isIconified()) {
          searchView.setIconified(true);
        }
        itemSearch.collapseActionView();
        isSearch = true;
        getProducts("s=" + query, "search");
        return false;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        return false;
      }
    });


    itemSearch.setIcon(new IconicsDrawable(this)
        .icon(MaterialDesignIconic.Icon.gmi_search)
        .color(Color.BLACK)
        .sizeDp(18).getCurrent());

    itemFilter.setIcon(new IconicsDrawable(this)
        .icon(Ionicons.Icon.ion_android_options)
        .color(Color.BLACK)
        .sizeDp(18).getCurrent());

    itemBag.setIcon(new IconicsDrawable(this)
        .icon(FontAwesome.Icon.faw_shopping_bag)
        .color(Color.BLACK)
        .sizeDp(18).getCurrent());

    if (badgeCount > 0) {
      ActionItemBadge.update(this, itemBag, new IconicsDrawable(this)
          .icon(FontAwesome.Icon.faw_shopping_bag)
          .color(Color.BLACK)
          .sizeDp(18).getCurrent(), ActionItemBadge.BadgeStyles.RED, badgeCount);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Pass the event to ActionBarDrawerToggle
    // If it returns true, then it has handled
    // the nav drawer indicator touch event
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      mDrawerLayout.bringToFront();
      View view = this.getCurrentFocus();
      if (view != null) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
      return true;
    }
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.filter:
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        if (!isSearch) {
          AmaroServices.createFilters(getCurrentFragment().getSearchResult());
        }
        filterFragment.setType("filters");
        if (filterFragment.getDialog() == null) {
          filterFragment.show(fm, "fragment_entrega_dialog");
        } else {
          filterFragment.getDialog().show();
        }
        return true;
      case R.id.bag:
        CartActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        return true;
      default:
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
      mMenuDialogFragment.dismiss();
    } else {
      finish();
    }
  }

  // Called when invalidateOptionsMenu() is invoked
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    // If the nav drawer is open, hide action items related to the content view
    boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerPane);
    if (drawerOpen) {
      mDrawerLayout.bringToFront();
    } else {
      tabs.bringToFront();
      pager.bringToFront();
    }
    return super.onPrepareOptionsMenu(menu);
  }


  private void registerReceiver() {
    if (!isReceiverRegistered) {
      LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
          new IntentFilter("registrationComplete"));
      isReceiverRegistered = true;
    }
  }

  public void onRestoreInstanceState(Bundle savedInstanceState) {
    // Always call the superclass so it can restore the view hierarchy
    super.onRestoreInstanceState(savedInstanceState);
    if (loadingFragment.getDialog() == null) {
      loadingFragment.setCancelable(false);
      loadingFragment.show(fm, "fragment_loading_dialog");
      getCart();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
//    registerReceiver();
    BusProvider.getInstance().register(busCallback);
    // Handle any Google Play services errors
    if (PlayServicesUtils.isGooglePlayStoreAvailable(this)) {
      PlayServicesUtils.handleAnyPlayServicesError(this);
    }
    // Handle the "com.urbanairship.VIEW_RICH_PUSH_INBOX" intent action.
    if (RichPushInbox.VIEW_INBOX_INTENT_ACTION.equals(getIntent().getAction())) {
      //navigate(R.id.nav_message_center);
      // Clear the action so we don't handle it again
      getIntent().setAction(null);
    }


    if (AmaroServices.getCart().getItems() != null && AmaroServices.getCart().getItems().size() > 0) {
      badgeCount = AmaroServices.getCart().getItems().size();
      ActionItemBadge.update(this, itemBag, new IconicsDrawable(this)
          .icon(FontAwesome.Icon.faw_shopping_bag)
          .color(Color.BLACK)
          .sizeDp(18).getCurrent(), ActionItemBadge.BadgeStyles.RED, badgeCount);
    } else {
      if (itemBag != null) {
        ActionItemBadge.hide(itemBag);
        itemBag.setVisible(true);
      }
    }
    UAirship.shared().getInbox().addListener(inboxListener);
    showMessageCenterIndicator();
  }

  @Override
  protected void onPause() {
//    LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//    isReceiverRegistered = false;
    super.onPause();
    BusProvider.getInstance().unregister(busCallback);
    UAirship.shared().getInbox().removeListener(inboxListener);
  }

  @Override
  public void onClose(String callServiceString, String params) {
    int index = pager.getCurrentItem();
    String query;
    CategoryPagerAdapter adapter = ((CategoryPagerAdapter) pager.getAdapter());
    if (callServiceString.equalsIgnoreCase("filter")) {
      if (mainCategory.equalsIgnoreCase("fitness")) {
        String category = "&categoria=roupas";
        String subcategory = "&subcategoria=" + mainCategory.toLowerCase();
        String tipo = ((CatalogFragment) adapter.getItem(index)).getSubcategory().toLowerCase();
        if (tipo.contains(" & ")) {
          tipo = tipo.replace(" & ", "-");
        }
        if (tipo.contains(" ")) {
          tipo = tipo.replace(" ", "-");
        }
        tipo = "&tipo=" + tipo;

        String queryBegin = params.substring(0, params.indexOf("_") + 1);
        String queryEnd = params.substring(params.indexOf("_") + 1);
        if (tipo.contains("todas") || tipo.contains("todos")) {
          query = queryBegin + category + subcategory + queryEnd;
        } else {
          query = queryBegin + category + subcategory + tipo + queryEnd;
        }
      } else {
        String category = "&categoria=" + mainCategory.toLowerCase();
        String subcategory = ((CatalogFragment) adapter.getItem(index)).getSubcategory().toLowerCase();
        if (subcategory.contains(" & ")) {
          subcategory = subcategory.replace(" & ", "-");
        }
        if (subcategory.contains(" ")) {
          subcategory = subcategory.replace(" ", "-");
        }
        subcategory = "&subcategoria=" + subcategory;

        String queryBegin = params.substring(0, params.indexOf("_") + 1);
        String queryEnd = params.substring(params.indexOf("_") + 1);
        if ((subcategory.contains("todas") || subcategory.contains("todos")) && !category.equalsIgnoreCase("FITNESS")) {
          query = queryBegin + category + queryEnd;
        } else {
          query = queryBegin + category + subcategory + queryEnd;
        }
      }
    } else {
      query = params;
    }
    isFiltered = true;
    getProducts(query, ((CatalogFragment) adapter.getItem(index)).getCategory());
  }

  /**
   * Shows a Message Center indicator.
   */
  private void showMessageCenterIndicator() {
    List<RichPushMessage> unreadMessage = UAirship.shared().getInbox().getUnreadMessages();

    // Skip showing the indicator if we have no unread messages or no new messages since the last display
    if (unreadMessage.isEmpty() || messageCenterLastSentDate >= unreadMessage.get(0).getSentDateMS()) {
      return;
    }
    // Track the message sent date to track if we have a new message
    messageCenterLastSentDate = unreadMessage.get(0).getSentDateMS();
    // Skip showing the indicator if its already displaying
    if (messageCenterSnackbar != null && messageCenterSnackbar.isShownOrQueued()) {
      return;
    }

    // Skip showing the indicator if the activity is already showing the Message Center
    if (currentNavPosition == R.id.bag) {
      return;
    }
    String text = getResources().getQuantityString(R.plurals.mc_indicator_text, unreadMessage.size(), unreadMessage.size());

    //noinspection ResourceType - For the duration field of the snackbar when defining a custom duration
    messageCenterSnackbar = Snackbar.make(findViewById(R.id.mainParent), text, Snackbar.LENGTH_LONG)
        .setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        .setAction("Action", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            messageCenterSnackbar.dismiss();
            UAirship.shared().getInbox().startInboxActivity();
          }
        });

    messageCenterSnackbar.show();
  }

  protected void onError(Error error) {

  }

  public void clearFragments() {
    List<Fragment> al = getSupportFragmentManager().getFragments();
    if (al != null) {
      for (Fragment frag : al) {
        if (frag != null) {
          getSupportFragmentManager().beginTransaction().remove(frag).commit();
        }
      }
    }
  }


  protected void setSlideMenu() {

    //Setup HomeIconSlide Menu must be 36x36
    final Drawable menu = new IconicsDrawable(this)
        .icon(MaterialDesignIconic.Icon.gmi_dehaze)
        .color(Color.parseColor("#FF000000"))
        .sizeDp(18).getCurrent();
    menu.setColorFilter(Color.parseColor("#FF000000"), PorterDuff.Mode.SRC_ATOP);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(menu);
//
    //Setup Slide Menu Items
//    mNavItems.add(new NavItem("NOVIDADES", "", new IconicsDrawable(this)
//        .icon(FoundationIcons.Icon.fou_burst_new)
//        .color(Color.parseColor("#FF00AA00"))
//        .sizeDp(32).getCurrent()));
    mNavItems.add(new NavItem("ROUPAS", ""));
    mNavItems.add(new NavItem("FITNESS", ""));
    mNavItems.add(new NavItem("BIJOUX", ""));
    mNavItems.add(new NavItem("ACESSORIOS", ""));
    mNavItems.add(new NavItem("TENDENCIAS", ""));
    mNavItems.add(new NavItem("SALE", ""));
    mNavItems.add(new NavItem("GUIDE SHOPS", ""));

    // Populate the Navigtion Drawer with options
    SlideMenuListAdapter adapter = new SlideMenuListAdapter(this, mNavItems);
    mDrawerList.setAdapter(adapter);

    //Open/Close Events of Drawer
    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
      @Override
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);

        invalidateOptionsMenu();
      }

      @Override
      public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);

        invalidateOptionsMenu();
      }
    };

    mDrawerLayout.addDrawerListener(mDrawerToggle);
    // Drawer Item click listeners
    mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadingFragment.setCancelable(false);
        loadingFragment.show(fm, "fragment_loading_dialog");
        mainCategory = ((NavItem) mDrawerList.getAdapter().getItem(position)).mTitle;
        if (mainCategory.equalsIgnoreCase("fitness")) {
          getProducts("s=_&categoria=roupas&subcategoria=" + mainCategory.toLowerCase(), mainCategory);
        } else {
          getProducts("s=_&categoria=" + mainCategory.toLowerCase(), mainCategory);
        }
        isSearch = false;
        isFiltered = false;

      }
    });
  }


  public CatalogFragment getCurrentFragment() {
    int index = pager.getCurrentItem();
    CategoryPagerAdapter categoryPagerAdapter = (CategoryPagerAdapter) pager.getAdapter();
    CatalogFragment fragment = (CatalogFragment) categoryPagerAdapter.getItem(index);
    return fragment;
  }

  /**
   * Check the device to make sure it has the Google Play Services APK. If
   * it doesn't, display a dialog that allows users to download the APK from
   * the Google Play Store or enable it in the device's system settings.
   */
  private boolean checkPlayServices() {
    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
    if (resultCode != ConnectionResult.SUCCESS) {
      if (apiAvailability.isUserResolvableError(resultCode)) {
        apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
            .show();
      } else {
        Log.i("not Supported: ", "This device is not supported.");
        finish();
      }
      return false;
    }
    return true;
  }

  public void urbanAirship() {
    UAirship.shared().getPushManager().editTags()
        .addTag("some_tag")
        .apply();
    UAirship.shared()
        .getInAppMessageManager()
        .setDisplayAsapEnabled(true);
    UAirship.shared()
        .getInAppMessageManager()
        .setDisplayAsapEnabled(true);
    UAirship.shared().getInbox().startInboxActivity();
  }
}

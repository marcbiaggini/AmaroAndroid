package amaro.amaroandroid.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import amaro.amaroandroid.Fragments.CatalogFragment;
import amaro.amaroandroid.R;
import amaro.api.Model.Product.ProductSearch;

/**
 * Created by juan.villa on 27/04/2016.
 */
public class CategoryPagerAdapter extends FragmentPagerAdapter {

  String category;
  private String[] TITLES;
  private ArrayList<ProductSearch> productSearches = new ArrayList<>();
  private AppCompatActivity mainActivity;
  /**
   * Contains all the fragments.
   */
  private List<Fragment> fragments = new ArrayList<>();

  /**
   * Contains all the tab titles.
   */
  private List<String> tabTitles = new ArrayList<>();

  private int tabquantity;


  public CategoryPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return TITLES[position];
  }

  @Override
  public int getCount() {
    return TITLES.length;
  }

  @Override
  public Fragment getItem(int position) {
    if (category.equals("search")) {
      ((CatalogFragment) fragments.get(position)).setSearch(true);
      ((CatalogFragment) fragments.get(position)).setProductSearches(productSearches);
    } else {
      ((CatalogFragment) fragments.get(position)).setSearch(false);
    }
    ((CatalogFragment) fragments.get(position)).setMainActivity(mainActivity);
    ((CatalogFragment) fragments.get(position)).setCategory(category);
    ((CatalogFragment) fragments.get(position)).setSubcategory(TITLES[position]);
    ((CatalogFragment) fragments.get(position)).setPosition(position);
    return fragments.get(position);
  }

  /**
   * Adds the fragment to the list, also adds the fragment's tab title.
   *
   * @param fragment New instance of the Fragment to be associated with this tab.
   * @param tabTitle A String containing the tab title for this Fragment.
   */
  public void addFragment(Fragment fragment, String tabTitle) {
    fragments.add(fragment);
    tabTitles.add(tabTitle);
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
    switch (category) {
      default:
        TITLES = mainActivity.getResources().getStringArray(R.array.roupas);
        break;
      case "FITNESS":
        TITLES = mainActivity.getResources().getStringArray(R.array.fitness);
        break;
      case "BIJOUX":
        TITLES = mainActivity.getResources().getStringArray(R.array.bijoux);
        break;
      case "ACESSORIOS":
        TITLES = mainActivity.getResources().getStringArray(R.array.acessorios);
        break;
      case "TENDENCIAS":
        TITLES = mainActivity.getResources().getStringArray(R.array.tendencias);
        break;
      case "SALE":
        TITLES = mainActivity.getResources().getStringArray(R.array.sale);
        break;
      case "search":
        TITLES = new String[]{"Resultados"};
        break;
    }
    tabquantity = TITLES.length;
  }

  public AppCompatActivity getMainActivity() {
    return mainActivity;
  }

  public void setMainActivity(AppCompatActivity mainActivity) {
    this.mainActivity = mainActivity;
  }

  public ArrayList<ProductSearch> getProductSearches() {
    return productSearches;
  }

  public void setProductSearches(Collection<ProductSearch> productSearchSet) {
    productSearches.clear();
    productSearches.addAll(productSearchSet);
  }

  public void setProducts(ArrayList<ProductSearch> productSearches) {
    this.productSearches = productSearches;
  }

  public void clearFragments() {
    fragments.clear();
    tabTitles.clear();
  }

  public int getTabquantity() {
    return tabquantity;
  }
}

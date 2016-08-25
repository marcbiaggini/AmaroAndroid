package amaro.amaroandroid.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import amaro.amaroandroid.Adapters.ProductGridAdapter;
import amaro.amaroandroid.R;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.Product.ProductSearch;
import amaro.api.Model.MySearch.SearchResult;

public class CatalogFragment extends Fragment {

  private static final String ARG_POSITION = "position";
  private List<ProductSearch> productSearches = new ArrayList<>();
  private RecyclerView productsGrid;
  private String subcategory;
  private String category;
  private int position;
  private boolean isSearch = false;
  private AppCompatActivity mainActivity;
  private ProductGridAdapter productGridAdapter;
  private SearchResult searchResult;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    FrameLayout fl = new FrameLayout(getActivity());
    fl.setLayoutParams(params);

    View view = inflater.inflate(R.layout.fragment_product, container, false);
    view.setHorizontalScrollBarEnabled(true);
    productsGrid = (RecyclerView) view.findViewById(R.id.products_grid);
    if (isSearch) {
      productGridAdapter = new ProductGridAdapter(productSearches, getActivity().getApplicationContext(), getActivity());
      productsGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
      productsGrid.setAdapter(productGridAdapter);
    } else {
      SearchProductTask searchProductTask = new SearchProductTask();
      searchProductTask.execute("s=_&categoria=" + category + "&subcategoria=" + subcategory);
    }

    return view;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public AppCompatActivity getMainActivity() {
    return mainActivity;
  }

  public void setMainActivity(AppCompatActivity mainActivity) {
    this.mainActivity = mainActivity;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public SearchResult getSearchResult() {
    return searchResult;
  }

  public void setSearchResult(SearchResult searchResult) {
    this.searchResult = searchResult;
  }

  public String getSubcategory() {
    return subcategory;
  }

  public void setSubcategory(String subcategory) {

    this.subcategory = Normalizer
        .normalize(subcategory, Normalizer.Form.NFD)
        .replaceAll("[^\\p{ASCII}]", "");
  }

  public RecyclerView getProductsGrid() {
    return productsGrid;
  }

  public List<ProductSearch> setProducts(Collection<ProductSearch> productSearchSet) {
    productSearches.clear();
    productSearches.addAll(productSearchSet);
    return productSearches;
  }

  public boolean isSearch() {
    return isSearch;
  }

  public void setSearch(boolean search) {
    isSearch = search;
  }

  public List<ProductSearch> getProductSearches() {
    return productSearches;
  }

  public void setProductSearches(List<ProductSearch> productSearches) {
    this.productSearches = productSearches;
  }

  class SearchProductTask extends AsyncTask<String, Void, SearchResult> {

    String s;

    @Override
    protected SearchResult doInBackground(String... params) {
      s = params[0];
      return AmaroServices.getMySearch().search(queryBuilder(params[0]));
    }

    public String queryBuilder(String s) {
      String query = s;
      if ((query.contains("Todas") || query.contains("Todos")) && !category.equalsIgnoreCase("FITNESS")) {
        return "s=_&categoria=" + category.toLowerCase();
      }
      if (query.contains(" & ")) {
        query = query.replace(" & ", "-");
      }
      if (query.contains(" ")) {
        query = query.replace(" ", "-");
      }
      switch (category) {
        default:
          if (query.contains("Jeans")) {
            query = "s=_&musthave=denim";
          }
          if (query.contains("Coletes") || query.contains("Casacos")) {
            query = "s=_&categoria=" + category + "&subcategoria=blazers-jaquetas&tipo=capas-ponchos,kimonos,coletes";
            query = query.toLowerCase();
          }
          break;
        case "FITNESS":
          if (subcategory.contains("Corrida")) {
            return "s=corrida";
          }
          if (subcategory.contains("Yoga")) {
            return "s=yoga";
          }
          if (subcategory.contains("Academia")) {
            return "s=academia";
          }
          String tipo = subcategory;
          if (tipo.contains(" & ")) {
            tipo = tipo.replace(" & ", "-");
          }
          if (tipo.contains(" ")) {
            tipo = tipo.replace(" ", "-");
          }
          if (query.contains("Todas") || query.contains("Todos")) {
            query = "s=_&categoria=roupas&subcategoria=" + category;
          } else {
            query = "s=_&categoria=roupas&subcategoria=" + category + "&tipo=" + tipo;
          }
          break;
        case "BIJOUX":
          query = "s=_&categoria=" + category.toLowerCase() + "&subcategoria=" + subcategory.toLowerCase();
          break;
        case "ACESSORIOS":
          String sub = subcategory;
          if (sub.contains(" & ")) {
            sub = sub.replace(" & ", "-");
          }
          if (sub.contains(" ")) {
            sub = sub.replace(" ", "-");
          }
          query = "s=_&categoria=" + category.toLowerCase() + "&subcategoria=" + sub.toLowerCase();
          break;
        case "TENDENCIAS":
          break;
        case "SALE":
          break;
      }
      return query;
    }

    @Override
    protected void onPostExecute(SearchResult result) {
      searchResult = result;
      ProductGridAdapter productGridAdapter = new ProductGridAdapter(setProducts(result.getProducts()), getActivity().getApplicationContext(), getActivity());
      productsGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
      productsGrid.setAdapter(productGridAdapter);
    }
  }
}

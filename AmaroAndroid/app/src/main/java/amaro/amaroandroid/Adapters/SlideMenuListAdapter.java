package amaro.amaroandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import amaro.amaroandroid.Model.ViewModels.NavItem;
import amaro.amaroandroid.R;

/**
 * Created by juan.villa on 28/04/2016.
 */
public class SlideMenuListAdapter extends BaseAdapter {

  Context mContext;
  ArrayList<NavItem> mNavItems;

  public SlideMenuListAdapter(Context context, ArrayList<NavItem> navItems) {
    mContext = context;
    mNavItems = navItems;
  }

  @Override
  public int getCount() {
    return mNavItems.size();
  }

  @Override
  public Object getItem(int position) {
    return mNavItems.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view;

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.item_slide_menu, null);
    } else {
      view = convertView;
    }
    TextView titleView = (TextView) view.findViewById(R.id.titleSlideMenuItem);
    titleView.setText(mNavItems.get(position).mTitle);
    return view;
  }
}
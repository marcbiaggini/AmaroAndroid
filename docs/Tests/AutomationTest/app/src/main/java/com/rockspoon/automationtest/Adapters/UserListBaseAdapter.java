package com.rockspoon.automationtest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rockspoon.automationtest.Models.User;
import com.rockspoon.automationtest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juancamilovilladuarte on 3/16/16.
 */
public class UserListBaseAdapter extends BaseAdapter {

  private final List<User> users = new ArrayList<>();
  private final Context ctx;

  public UserListBaseAdapter(final Context ctx) {
    this.ctx = ctx;
  }

  public UserListBaseAdapter(final List<User> users, final Context ctx) {
    this.users.addAll(users);
    this.ctx = ctx;
  }

  public void setData(final List<User> users) {
    this.users.clear();
    if (users != null) this.users.addAll(users);
    this.notifyDataSetChanged();
  }

  public int getCount() {
    return users.size();
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    final LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View v = (convertView == null) ? inflater.inflate(R.layout.user_item, null) : convertView;
    final ViewHolder vH = (convertView == null) ? new ViewHolder(v) : (ViewHolder) v.getTag();
    final User user = users.get(position);

    vH.userName.setText(user.getName());

    return v;
  }

  public User getItem(int position) {
    return users.get(position);
  }

  static class ViewHolder {
    TextView userName;

    public ViewHolder(View v) {
      userName = (TextView) v.findViewById(R.id.userName);
      v.setTag(this);
    }
  }
}
package com.rockspoon.automationtest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.rockspoon.automationtest.Models.User;
import com.rockspoon.automationtest.R;
//import com.rockspoon.automationtest.UserActivity_;

import java.util.List;

/**
 * Created by juancamilovilladuarte on 3/14/16.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserItemViewHolder> {

  private List<User> userList;
  private Context ctx;

  public UserListAdapter(List<User> userList, Context ctx) {
    this.userList = userList;
    this.ctx = ctx;

  }

  @Override
  public UserItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(ctx).inflate(R.layout.user_item, parent, false);
    return new UserItemViewHolder(ctx, view);
  }

  @Override
  public void onBindViewHolder(UserItemViewHolder holder, int position) {
    final User order = userList.get(position);
    holder.nametitleView.setText(order.getName());
  }

  @Override
  public int getItemCount() {
    return userList.size();
  }

  public List<User> getData()
  {
    return userList;
  }

  public class UserItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView nametitleView;
    Context ctx;

    public UserItemViewHolder(Context ctx, View itemView) {
      super(itemView);
      nametitleView = (TextView) itemView.findViewById(R.id.userName);
      this.ctx = ctx;
      itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
      Toast.makeText(ctx, "Clicked User: " + nametitleView.getText(), Toast.LENGTH_SHORT).show();
      //UserActivity_.intent(ctx).extra("name", nametitleView.getText()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
    }
  }

}

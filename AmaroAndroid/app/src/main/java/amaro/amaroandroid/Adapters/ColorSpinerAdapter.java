package amaro.amaroandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lid.lib.LabelImageView;

import java.util.List;

import amaro.amaroandroid.Model.ViewModels.ProductColor;
import amaro.amaroandroid.R;

/**
 * Created by juan.villa on 12/08/2016.
 */
public class ColorSpinerAdapter extends BaseAdapter {
  LayoutInflater inflator;
  List<ProductColor> mCounting;

  public ColorSpinerAdapter(Context context, List<ProductColor> counting) {
    inflator = LayoutInflater.from(context);
    mCounting = counting;
  }

  @Override
  public int getCount() {
    return mCounting.size();
  }

  @Override
  public ProductColor getItem(int position) {
    return mCounting.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ProductColor productColor = mCounting.get(position);
    convertView = inflator.inflate(R.layout.item_color_spinner, null);
    ImageView iconColorItem = (ImageView) convertView.findViewById(R.id.iconColorItem);
    iconColorItem.setImageDrawable(productColor.getImage().getDrawable());
    fadeImage(iconColorItem);
    TextView titleColorItem = (TextView) convertView.findViewById(R.id.titleColorItem);
    titleColorItem.setText(productColor.getName());
    return convertView;
  }

  private void fadeImage(final ImageView img) {
    Animation fade = new AlphaAnimation(0, 1);

    fade.setInterpolator(new AccelerateInterpolator());
    fade.setDuration(1000);

    fade.setAnimationListener(new Animation.AnimationListener() {
      public void onAnimationEnd(Animation animation) {
        img.setVisibility(View.VISIBLE);
      }

      public void onAnimationRepeat(Animation animation) {
      }

      public void onAnimationStart(Animation animation) {
      }
    });

    img.startAnimation(fade);
  }
}

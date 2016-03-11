package com.zengbobobase.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 实现平铺排版功能
 * 
 * @author admin
 * 
 */
public class ContainListView extends ListView {
  private boolean haveScrollbar = true;

  public ContainListView(Context context) {
    super(context);
  }

  public ContainListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ContainListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }


  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    System.out.println("TestPathEffectActivity getView position:0 onLayout");
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // if (haveScrollbar == false) {
    System.out.println("TestPathEffectActivity getView position:0 onMeasure");
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
//    setMeasuredDimension(measuredWidth, measuredHeight);
    // } else {
    // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    // }
  }

  /**
   * 设置是否有ScrollBar，当要在ScollView中显示时，应当设置为false。 默认为 true
   * 
   * @param haveScrollbars
   */
  public void setHaveScrollbar(boolean haveScrollbar) {
    this.haveScrollbar = haveScrollbar;
  }
  
  
}

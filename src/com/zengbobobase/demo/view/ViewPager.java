package com.zengbobobase.demo.view;

/**
 * 项目名称：xrz_application
 * 类描述：
 * 创建人：bobo
 * 创建时间：2015/11/24 17:45
 * 修改人：bobo
 * 修改时间：2015/11/24 17:45
 * 修改备注：
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPager extends ViewGroup {
    private static final String TAG = "ViewPager";
    private static final boolean DEBUG = false;
    private static final boolean USE_CACHE = false;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int[] LAYOUT_ATTRS = {16842931};
    private int mExpectedAdapterCount;
    private static final Comparator<ItemInfo> COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ItemInfo lhs, ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    };

    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0F;
            return t * t * t * t * t + 1.0F;
        }
    };

    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private final ItemInfo mTempItem = new ItemInfo();

    private final Rect mTempRect = new Rect();
    private PagerAdapter mAdapter;
    private int mCurItem;
    private int mRestoredCurItem = -1;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private Scroller mScroller;
    private PagerObserver mObserver;
    private int mPageMargin;
    private Drawable mMarginDrawable;
    private int mTopPageBounds;
    private int mBottomPageBounds;
    private float mFirstOffset = -3.402824E+037F;
    private float mLastOffset = 3.4028235E+38F;
    private int mChildWidthMeasureSpec;
    private int mChildHeightMeasureSpec;
    private boolean mInLayout;
    private boolean mScrollingCacheEnabled;
    private boolean mPopulatePending;
    private int mOffscreenPageLimit = 1;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private boolean mIgnoreGutter;
    private int mDefaultGutterSize;
    private int mGutterSize;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mActivePointerId = -1;
    private static final int INVALID_POINTER = -1;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mFlingDistance;
    private int mCloseEnough;
    private static final int CLOSE_ENOUGH = 2;
    private boolean mFakeDragging;
    private long mFakeDragBeginTime;
    private EdgeEffectCompat mLeftEdge;
    private EdgeEffectCompat mRightEdge;
    private boolean mFirstLayout = true;
    private boolean mNeedCalculatePageOffsets = false;
    private boolean mCalledSuper;
    private int mDecorChildCount;
    private OnPageChangeListener mOnPageChangeListener;
    private OnPageChangeListener mInternalPageChangeListener;
    private OnAdapterChangeListener mAdapterChangeListener;
    private PageTransformer mPageTransformer;
    private Method mSetChildrenDrawingOrderEnabled;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;
    private final Runnable mEndScrollRunnable = new Runnable() {
        public void run() {
            ViewPager.this.setScrollState(0);
            ViewPager.this.populate();
        }
    };

    private int mScrollState = 0;

    public ViewPager(Context context) {
        super(context);
        initViewPager();
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager();
    }

    void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        float density = context.getResources().getDisplayMetrics().density;

        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        this.mMinimumVelocity = (int) (400.0F * density);
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffectCompat(context);
        this.mRightEdge = new EdgeEffectCompat(context);

        this.mFlingDistance = (int) (25.0F * density);
        this.mCloseEnough = (int) (2.0F * density);
        this.mDefaultGutterSize = (int) (16.0F * density);

        ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());

        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    private void setScrollState(int newState) {
        if (this.mScrollState == newState) {
            return;
        }

        this.mScrollState = newState;
        if (this.mPageTransformer != null) {
            enableLayers(newState != 0);
        }
        if (this.mOnPageChangeListener != null)
            this.mOnPageChangeListener.onPageScrollStateChanged(newState);
    }

    public void setAdapter(PagerAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate(this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo ii = (ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem(this, ii.position, ii.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }

        PagerAdapter oldAdapter = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;

        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean wasFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (!wasFirstLayout) {
                populate();
            } else {
                requestLayout();
            }
        }

        if ((this.mAdapterChangeListener != null) && (oldAdapter != adapter))
            this.mAdapterChangeListener.onAdapterChanged(oldAdapter, adapter);
    }

    private void removeNonDecorViews() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (!lp.isDecor) {
                removeViewAt(i);
                i--;
            }
        }
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    void setOnAdapterChangeListener(OnAdapterChangeListener listener) {
        this.mAdapterChangeListener = listener;
    }

    private int getClientWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    public void setCurrentItem(int item) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        if ((this.mAdapter == null) || (this.mAdapter.getCount() <= 0)) {
            setScrollingCacheEnabled(false);
            return;
        }
        if ((!always) && (this.mCurItem == item) && (this.mItems.size() != 0)) {
            setScrollingCacheEnabled(false);
            return;
        }

        if (item < 0)
            item = 0;
        else if (item >= this.mAdapter.getCount()) {
            item = this.mAdapter.getCount() - 1;
        }
        int pageLimit = this.mOffscreenPageLimit;
        if ((item > this.mCurItem + pageLimit) || (item < this.mCurItem - pageLimit)) {
            for (int i = 0; i < this.mItems.size(); i++) {
                ((ItemInfo) this.mItems.get(i)).scrolling = true;
            }
        }
        boolean dispatchSelected = this.mCurItem != item;

        if (this.mFirstLayout) {
            this.mCurItem = item;
            if ((dispatchSelected) && (this.mOnPageChangeListener != null)) {
                this.mOnPageChangeListener.onPageSelected(item);
            }
            if ((dispatchSelected) && (this.mInternalPageChangeListener != null)) {
                this.mInternalPageChangeListener.onPageSelected(item);
            }
            requestLayout();
        } else {
            populate(item);
            scrollToItem(item, smoothScroll, velocity, dispatchSelected);
        }
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        ItemInfo curInfo = infoForPosition(item);
        int destX = 0;
        if (curInfo != null) {
            int width = getClientWidth();
            destX = (int) (width * Math.max(this.mFirstOffset, Math.min(curInfo.offset, this.mLastOffset)));
        }

        if (smoothScroll) {
            smoothScrollTo(destX, 0, velocity);
            if ((dispatchSelected) && (this.mOnPageChangeListener != null)) {
                this.mOnPageChangeListener.onPageSelected(item);
            }
            if ((dispatchSelected) && (this.mInternalPageChangeListener != null))
                this.mInternalPageChangeListener.onPageSelected(item);
        } else {
            if ((dispatchSelected) && (this.mOnPageChangeListener != null)) {
                this.mOnPageChangeListener.onPageSelected(item);
            }
            if ((dispatchSelected) && (this.mInternalPageChangeListener != null)) {
                this.mInternalPageChangeListener.onPageSelected(item);
            }
            completeScroll(false);
            scrollTo(destX, 0);
            pageScrolled(destX);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        if (Build.VERSION.SDK_INT >= 11) {
            boolean hasTransformer = transformer != null;
            boolean needsPopulate = hasTransformer != (this.mPageTransformer != null);
            this.mPageTransformer = transformer;
            setChildrenDrawingOrderEnabledCompat(hasTransformer);
            if (hasTransformer)
                this.mDrawingOrder = (reverseDrawingOrder ? 2 : 1);
            else {
                this.mDrawingOrder = 0;
            }
            if (needsPopulate) populate();
        }
    }

    void setChildrenDrawingOrderEnabledCompat(boolean enable) {
        if (Build.VERSION.SDK_INT >= 7) {
            if (this.mSetChildrenDrawingOrderEnabled == null)
                try {
                    this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{Boolean.TYPE});
                } catch (NoSuchMethodException e) {
                    Log.e("ViewPager", "Can't find setChildrenDrawingOrderEnabled", e);
                }
            try {
                this.mSetChildrenDrawingOrderEnabled.invoke(this, new Object[]{Boolean.valueOf(enable)});
            } catch (Exception e) {
                Log.e("ViewPager", "Error changing children drawing order", e);
            }
        }
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        int index = this.mDrawingOrder == 2 ? childCount - 1 - i : i;
        int result = ((LayoutParams) ((View) this.mDrawingOrderedChildren.get(index)).getLayoutParams()).childIndex;
        return result;
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
        OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            Log.w("ViewPager", new StringBuilder().append("Requested offscreen page limit ").append(limit).append(" too small; defaulting to ").append(1).toString());

            limit = 1;
        }
        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            populate();
        }
    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;

        int width = getWidth();
        recomputeScrollPosition(width, width, marginPixels, oldMargin);

        requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) refreshDrawableState();
        setWillNotDraw(d == null);
        invalidate();
    }

    public void setPageMarginDrawable(int resId) {
        setPageMarginDrawable(getContext().getResources().getDrawable(resId));
    }

    protected boolean verifyDrawable(Drawable who) {
        return (super.verifyDrawable(who)) || (who == this.mMarginDrawable);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if ((d != null) && (d.isStateful()))
            d.setState(getDrawableState());
    }

    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5F;
        f = (float) (f * 0.47123891676382D);
        return (float) Math.sin(f);
    }

    void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, 0);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int sx = getScrollX();
        int sy = getScrollY();
        int dx = x - sx;
        int dy = y - sy;
        if ((dx == 0) && (dy == 0)) {
            completeScroll(false);
            populate();
            setScrollState(0);
            return;
        }

        setScrollingCacheEnabled(true);
        setScrollState(2);

        int width = getClientWidth();
        int halfWidth = width / 2;
        float distanceRatio = Math.min(1.0F, 1.0F * Math.abs(dx) / width);
        float distance = halfWidth + halfWidth * distanceInfluenceForSnapDuration(distanceRatio);

        int duration = 0;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(1000.0F * Math.abs(distance / velocity));
        } else {
            float pageWidth = width * this.mAdapter.getPageWidth(this.mCurItem);
            float pageDelta = Math.abs(dx) / (pageWidth + this.mPageMargin);
            duration = (int) ((pageDelta + 1.0F) * 100.0F);
        }
        duration = Math.min(duration, 600);

        this.mScroller.startScroll(sx, sy, dx, dy, duration);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    ItemInfo addNewItem(int position, int index) {
        ItemInfo ii = new ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem(this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if ((index < 0) || (index >= this.mItems.size()))
            this.mItems.add(ii);
        else {
            this.mItems.add(index, ii);
        }
        return ii;
    }

    void dataSetChanged() {
        int adapterCount = this.mAdapter.getCount();
        this.mExpectedAdapterCount = adapterCount;
        boolean needPopulate = (this.mItems.size() < this.mOffscreenPageLimit * 2 + 1) && (this.mItems.size() < adapterCount);

        int newCurrItem = this.mCurItem;

        boolean isUpdating = false;
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            int newPos = this.mAdapter.getItemPosition(ii.object);

            if (newPos == -1) {
                continue;
            }
            if (newPos == -2) {
                this.mItems.remove(i);
                i--;

                if (!isUpdating) {
                    this.mAdapter.startUpdate(this);
                    isUpdating = true;
                }

                this.mAdapter.destroyItem(this, ii.position, ii.object);
                needPopulate = true;

                if (this.mCurItem != ii.position)
                    continue;
                newCurrItem = Math.max(0, Math.min(this.mCurItem, adapterCount - 1));
                needPopulate = true;
            } else if (ii.position != newPos) {
                if (ii.position == this.mCurItem) {
                    newCurrItem = newPos;
                }

                ii.position = newPos;
                needPopulate = true;
            }
        }

        if (isUpdating) {
            this.mAdapter.finishUpdate(this);
        }

        Collections.sort(this.mItems, COMPARATOR);

        if (needPopulate) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (!lp.isDecor) {
                    lp.widthFactor = 0.0F;
                }
            }

            setCurrentItemInternal(newCurrItem, false, true);
            requestLayout();
        }
    }

    void populate() {
        populate(this.mCurItem);
    }

    void populate(int newCurrentItem) {
        ItemInfo oldCurInfo = null;
        int focusDirection = 2;
        if (this.mCurItem != newCurrentItem) {
            focusDirection = this.mCurItem < newCurrentItem ? 66 : 17;
            oldCurInfo = infoForPosition(this.mCurItem);
            this.mCurItem = newCurrentItem;
        }

        if (this.mAdapter == null) {
            sortChildDrawingOrder();
            return;
        }

        if (this.mPopulatePending) {
            sortChildDrawingOrder();
            return;
        }

        if (getWindowToken() == null) {
            return;
        }

        this.mAdapter.startUpdate(this);

        int pageLimit = this.mOffscreenPageLimit;
        int startPos = Math.max(0, this.mCurItem - pageLimit);
        int N = this.mAdapter.getCount();
        int endPos = Math.min(N - 1, this.mCurItem + pageLimit);

        if (N != this.mExpectedAdapterCount) {
            String resName;
            try {
                resName = getResources().getResourceName(getId());
            } catch (Resources.NotFoundException e) {
                resName = Integer.toHexString(getId());
            }
            throw new IllegalStateException(new StringBuilder().append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ").append(this.mExpectedAdapterCount).append(", found: ").append(N).append(" Pager id: ").append(resName).append(" Pager class: ").append(getClass()).append(" Problematic adapter: ").append(this.mAdapter.getClass()).toString());
        }

        int curIndex = -1;
        ItemInfo curItem = null;
        for (curIndex = 0; curIndex < this.mItems.size(); curIndex++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(curIndex);
            if (ii.position >= this.mCurItem) {
                if (ii.position != this.mCurItem) break;
                curItem = ii;
                break;
            }

        }

        if ((curItem == null) && (N > 0)) {
            curItem = addNewItem(this.mCurItem, curIndex);
        }

        if (curItem != null) {
            float extraWidthLeft = 0.0F;
            int itemIndex = curIndex - 1;
            ItemInfo ii = itemIndex >= 0 ? (ItemInfo) this.mItems.get(itemIndex) : null;
            int clientWidth = getClientWidth();
            float leftWidthNeeded = clientWidth <= 0 ? 0.0F : 2.0F - curItem.widthFactor + getPaddingLeft() / clientWidth;

            for (int pos = this.mCurItem - 1; pos >= 0; pos--) {
                if ((extraWidthLeft >= leftWidthNeeded) && (pos < startPos)) {
                    if (ii == null) {
                        break;
                    }
                    if ((pos == ii.position) && (!ii.scrolling)) {
                        this.mItems.remove(itemIndex);
                        this.mAdapter.destroyItem(this, pos, ii.object);

                        itemIndex--;
                        curIndex--;
                        ii = itemIndex >= 0 ? (ItemInfo) this.mItems.get(itemIndex) : null;
                    }
                } else if ((ii != null) && (pos == ii.position)) {
                    extraWidthLeft += ii.widthFactor;
                    itemIndex--;
                    ii = itemIndex >= 0 ? (ItemInfo) this.mItems.get(itemIndex) : null;
                } else {
                    ii = addNewItem(pos, itemIndex + 1);
                    extraWidthLeft += ii.widthFactor;
                    curIndex++;
                    ii = itemIndex >= 0 ? (ItemInfo) this.mItems.get(itemIndex) : null;
                }
            }

            float extraWidthRight = curItem.widthFactor;
            itemIndex = curIndex + 1;
            if (extraWidthRight < 2.0F) {
                ii = itemIndex < this.mItems.size() ? (ItemInfo) this.mItems.get(itemIndex) : null;
                float rightWidthNeeded = clientWidth <= 0 ? 0.0F : getPaddingRight() / clientWidth + 2.0F;

                for (int pos = this.mCurItem + 1; pos < N; pos++) {
                    if ((extraWidthRight >= rightWidthNeeded) && (pos > endPos)) {
                        if (ii == null) {
                            break;
                        }
                        if ((pos == ii.position) && (!ii.scrolling)) {
                            this.mItems.remove(itemIndex);
                            this.mAdapter.destroyItem(this, pos, ii.object);

                            ii = itemIndex < this.mItems.size() ? (ItemInfo) this.mItems.get(itemIndex) : null;
                        }
                    } else if ((ii != null) && (pos == ii.position)) {
                        extraWidthRight += ii.widthFactor;
                        itemIndex++;
                        ii = itemIndex < this.mItems.size() ? (ItemInfo) this.mItems.get(itemIndex) : null;
                    } else {
                        ii = addNewItem(pos, itemIndex);
                        itemIndex++;
                        extraWidthRight += ii.widthFactor;
                        ii = itemIndex < this.mItems.size() ? (ItemInfo) this.mItems.get(itemIndex) : null;
                    }
                }
            }

            calculatePageOffsets(curItem, curIndex, oldCurInfo);
        }

        this.mAdapter.setPrimaryItem(this, this.mCurItem, curItem != null ? curItem.object : null);

        this.mAdapter.finishUpdate(this);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.childIndex = i;
            if ((lp.isDecor) || (lp.widthFactor != 0.0F))
                continue;
            ItemInfo ii = infoForChild(child);
            if (ii != null) {
                lp.widthFactor = ii.widthFactor;
                lp.position = ii.position;
            }
        }

        sortChildDrawingOrder();

        if (hasFocus()) {
            View currentFocused = findFocus();
            ItemInfo ii = currentFocused != null ? infoForAnyChild(currentFocused) : null;
            if ((ii == null) || (ii.position != this.mCurItem))
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    ii = infoForChild(child);
                    if ((ii != null) && (ii.position == this.mCurItem) &&
                            (child.requestFocus(focusDirection)))
                        break;
                }
        }
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null)
                this.mDrawingOrderedChildren = new ArrayList();
            else {
                this.mDrawingOrderedChildren.clear();
            }
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                this.mDrawingOrderedChildren.add(child);
            }
            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }
    }

    private void calculatePageOffsets(ItemInfo curItem, int curIndex, ItemInfo oldCurInfo) {
        int N = this.mAdapter.getCount();
        int width = getClientWidth();
        float marginOffset = width > 0 ? this.mPageMargin / width : 0.0F;

        if (oldCurInfo != null) {
            int oldCurPosition = oldCurInfo.position;

            if (oldCurPosition < curItem.position) {
                int itemIndex = 0;
                ItemInfo ii = null;
                float offset = oldCurInfo.offset + oldCurInfo.widthFactor + marginOffset;
                int pos = oldCurPosition + 1;
                for (; (pos <= curItem.position) && (itemIndex < this.mItems.size()); pos++) {
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                    while ((pos > ii.position) && (itemIndex < this.mItems.size() - 1)) {
                        itemIndex++;
                        ii = (ItemInfo) this.mItems.get(itemIndex);
                    }
                    while (pos < ii.position) {
                        offset += this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos++;
                    }
                    ii.offset = offset;
                    offset += ii.widthFactor + marginOffset;
                }
            } else if (oldCurPosition > curItem.position) {
                int itemIndex = this.mItems.size() - 1;
                ItemInfo ii = null;
                float offset = oldCurInfo.offset;
                int pos = oldCurPosition - 1;
                for (; (pos >= curItem.position) && (itemIndex >= 0); pos--) {
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                    while ((pos < ii.position) && (itemIndex > 0)) {
                        itemIndex--;
                        ii = (ItemInfo) this.mItems.get(itemIndex);
                    }
                    while (pos > ii.position) {
                        offset -= this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos--;
                    }
                    offset -= ii.widthFactor + marginOffset;
                    ii.offset = offset;
                }
            }

        }

        int itemCount = this.mItems.size();
        float offset = curItem.offset;
        int pos = curItem.position - 1;
        this.mFirstOffset = (curItem.position == 0 ? curItem.offset : -3.402824E+037F);
        this.mLastOffset = (curItem.position == N - 1 ? curItem.offset + curItem.widthFactor - 1.0F : 3.4028235E+38F);

        for (int i = curIndex - 1; i >= 0; pos--) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            while (pos > ii.position) {
                offset -= this.mAdapter.getPageWidth(pos--) + marginOffset;
            }
            offset -= ii.widthFactor + marginOffset;
            ii.offset = offset;
            if (ii.position == 0) this.mFirstOffset = offset;
            i--;
        }

        offset = curItem.offset + curItem.widthFactor + marginOffset;
        pos = curItem.position + 1;

        for (int i = curIndex + 1; i < itemCount; pos++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            while (pos < ii.position) {
                offset += this.mAdapter.getPageWidth(pos++) + marginOffset;
            }
            if (ii.position == N - 1) {
                this.mLastOffset = (offset + ii.widthFactor - 1.0F);
            }
            ii.offset = offset;
            offset += ii.widthFactor + marginOffset;

            i++;
        }

        this.mNeedCalculatePageOffsets = false;
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.position = this.mCurItem;
        if (this.mAdapter != null) {
            ss.adapterState = this.mAdapter.saveState();
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (this.mAdapter != null) {
            this.mAdapter.restoreState(ss.adapterState, ss.loader);
            setCurrentItemInternal(ss.position, false, true);
        } else {
            this.mRestoredCurItem = ss.position;
            this.mRestoredAdapterState = ss.adapterState;
            this.mRestoredClassLoader = ss.loader;
        }
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        LayoutParams lp = (LayoutParams) params;
        lp.isDecor |= child instanceof Decor;
        if (this.mInLayout) {
            if ((lp != null) && (lp.isDecor)) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }
            lp.needsMeasure = true;
            addViewInLayout(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    public void removeView(View view) {
        if (this.mInLayout)
            removeViewInLayout(view);
        else
            super.removeView(view);
    }

    ItemInfo infoForChild(View child) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }
        return null;
    }

    ItemInfo infoForAnyChild(View child) {
        ViewParent parent;
        while ((parent = child.getParent()) != this) {
            if ((parent == null) || (!(parent instanceof View))) {
                return null;
            }
            child = (View) parent;
        }
        return infoForChild(child);
    }

    ItemInfo infoForPosition(int position) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.position == position) {
                return ii;
            }
        }
        return null;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        int measuredWidth = getMeasuredWidth();
        int maxGutterSize = measuredWidth / 10;
        this.mGutterSize = Math.min(maxGutterSize, this.mDefaultGutterSize);

        int childWidthSize = measuredWidth - getPaddingLeft() - getPaddingRight();
        int childHeightSize = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if ((lp != null) && (lp.isDecor)) {
                    int hgrav = lp.gravity & 0x7;
                    int vgrav = lp.gravity & 0x70;
                    int widthMode = -2147483648;
                    int heightMode = -2147483648;
                    boolean consumeVertical = (vgrav == 48) || (vgrav == 80);
                    boolean consumeHorizontal = (hgrav == 3) || (hgrav == 5);

                    if (consumeVertical)
                        widthMode = 1073741824;
                    else if (consumeHorizontal) {
                        heightMode = 1073741824;
                    }

                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    if (lp.width != -2) {
                        widthMode = 1073741824;
                        if (lp.width != -1) {
                            widthSize = lp.width;
                        }
                    }
                    if (lp.height != -2) {
                        heightMode = 1073741824;
                        if (lp.height != -1) {
                            heightSize = lp.height;
                        }
                    }
                    int widthSpec = View.MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                    int heightSpec = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);
                    child.measure(widthSpec, heightSpec);

                    if (consumeVertical)
                        childHeightSize -= child.getMeasuredHeight();
                    else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
        }

        this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(childWidthSize, 1073741824);
        this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(childHeightSize, 1073741824);

        this.mInLayout = true;
        populate();
        this.mInLayout = false;

        size = getChildCount();
        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 8) {
                continue;
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if ((lp == null) || (!lp.isDecor)) {
                int widthSpec = View.MeasureSpec.makeMeasureSpec((int) (childWidthSize * lp.widthFactor), 1073741824);

                child.measure(widthSpec, this.mChildHeightMeasureSpec);
            }
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w != oldw)
            recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        if ((oldWidth > 0) && (!this.mItems.isEmpty())) {
            int widthWithMargin = width - getPaddingLeft() - getPaddingRight() + margin;
            int oldWidthWithMargin = oldWidth - getPaddingLeft() - getPaddingRight() + oldMargin;

            int xpos = getScrollX();
            float pageOffset = xpos / oldWidthWithMargin;
            int newOffsetPixels = (int) (pageOffset * widthWithMargin);

            scrollTo(newOffsetPixels, getScrollY());
            if (!this.mScroller.isFinished()) {
                int newDuration = this.mScroller.getDuration() - this.mScroller.timePassed();
                ItemInfo targetInfo = infoForPosition(this.mCurItem);
                this.mScroller.startScroll(newOffsetPixels, 0, (int) (targetInfo.offset * width), 0, newDuration);
            }
        } else {
            ItemInfo ii = infoForPosition(this.mCurItem);
            float scrollOffset = ii != null ? Math.min(ii.offset, this.mLastOffset) : 0.0F;
            int scrollPos = (int) (scrollOffset * (width - getPaddingLeft() - getPaddingRight()));

            if (scrollPos != getScrollX()) {
                completeScroll(false);
                scrollTo(scrollPos, getScrollY());
            }
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();

        int decorCount = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childLeft = 0;
                int childTop = 0;
                if (lp.isDecor) {
                    int hgrav = lp.gravity & 0x7;
                    int vgrav = lp.gravity & 0x70;
                    switch (hgrav) {
                        case 2:
                        case 4:
                        default:
                            childLeft = paddingLeft;
                            break;
                        case 3:
                            childLeft = paddingLeft;
                            paddingLeft += child.getMeasuredWidth();
                            break;
                        case 1:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);

                            break;
                        case 5:
                            childLeft = width - paddingRight - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                    }

                    switch (vgrav) {
                        default:
                            childTop = paddingTop;
                            break;
                        case 48:
                            childTop = paddingTop;
                            paddingTop += child.getMeasuredHeight();
                            break;
                        case 16:
                            childTop = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop);

                            break;
                        case 80:
                            childTop = height - paddingBottom - child.getMeasuredHeight();
                            paddingBottom += child.getMeasuredHeight();
                    }

                    childLeft += scrollX;
                    child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());

                    decorCount++;
                }
            }
        }

        int childWidth = width - paddingLeft - paddingRight;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                ItemInfo ii;
                if ((!lp.isDecor) && ((ii = infoForChild(child)) != null)) {
                    int loff = (int) (childWidth * ii.offset);
                    int childLeft = paddingLeft + loff;
                    int childTop = paddingTop;
                    if (lp.needsMeasure) {
                        lp.needsMeasure = false;
                        int widthSpec = View.MeasureSpec.makeMeasureSpec((int) (childWidth * lp.widthFactor), 1073741824);

                        int heightSpec = View.MeasureSpec.makeMeasureSpec(height - paddingTop - paddingBottom, 1073741824);

                        child.measure(widthSpec, heightSpec);
                    }

                    child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
                }
            }

        }

        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = (height - paddingBottom);
        this.mDecorChildCount = decorCount;

        if (this.mFirstLayout) {
            scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }

    public void computeScroll() {
        if ((!this.mScroller.isFinished()) && (this.mScroller.computeScrollOffset())) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();

            if ((oldX != x) || (oldY != y)) {
                scrollTo(x, y);
                if (!pageScrolled(x)) {
                    this.mScroller.abortAnimation();
                    scrollTo(0, y);
                }

            }

            ViewCompat.postInvalidateOnAnimation(this);
            return;
        }

        completeScroll(true);
    }

    private boolean pageScrolled(int xpos) {
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0F, 0);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }

            return false;
        }
        ItemInfo ii = infoForCurrentScrollPosition();
        int width = getClientWidth();
        int widthWithMargin = width + this.mPageMargin;
        float marginOffset = this.mPageMargin / width;
        int currentPage = ii.position;
        float pageOffset = (xpos / width - ii.offset) / (ii.widthFactor + marginOffset);

        int offsetPixels = (int) (pageOffset * widthWithMargin);

        this.mCalledSuper = false;
        onPageScrolled(currentPage, pageOffset, offsetPixels);
        if (!this.mCalledSuper) {
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }

        return true;
    }

    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        if (this.mDecorChildCount > 0) {
            int scrollX = getScrollX();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int width = getWidth();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (!lp.isDecor)
                    continue;
                int hgrav = lp.gravity & 0x7;
                int childLeft = 0;
                switch (hgrav) {
                    case 2:
                    case 4:
                    default:
                        childLeft = paddingLeft;
                        break;
                    case 3:
                        childLeft = paddingLeft;
                        paddingLeft += child.getWidth();
                        break;
                    case 1:
                        childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);

                        break;
                    case 5:
                        childLeft = width - paddingRight - child.getMeasuredWidth();
                        paddingRight += child.getMeasuredWidth();
                }

                childLeft += scrollX;

                int childOffset = childLeft - child.getLeft();
                if (childOffset != 0) {
                    child.offsetLeftAndRight(childOffset);
                }
            }
        }

        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }

        if (this.mPageTransformer != null) {
            int scrollX = getScrollX();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                if (lp.isDecor)
                    continue;
                float transformPos = (child.getLeft() - scrollX) / getClientWidth();
                this.mPageTransformer.transformPage(child, transformPos);
            }
        }

        this.mCalledSuper = true;
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate = this.mScrollState == 2;
        if (needPopulate) {
            setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if ((oldX != x) || (oldY != y)) {
                scrollTo(x, y);
            }
        }
        this.mPopulatePending = false;
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.scrolling) {
                needPopulate = true;
                ii.scrolling = false;
            }
        }
        if (needPopulate)
            if (postEvents)
                ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
            else
                this.mEndScrollRunnable.run();
    }

    private boolean isGutterDrag(float x, float dx) {
        return ((x < this.mGutterSize) && (dx > 0.0F)) || ((x > getWidth() - this.mGutterSize) && (dx < 0.0F));
    }

    private void enableLayers(boolean enable) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            int layerType = enable ? 2 : 0;

            ViewCompat.setLayerType(getChildAt(i), layerType, null);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & 0xFF;

        if ((action == 3) || (action == 1)) {
            this.mIsBeingDragged = false;
            this.mIsUnableToDrag = false;
            this.mActivePointerId = -1;
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            return false;
        }

        if (action != 0) {
            if (this.mIsBeingDragged) {
                return true;
            }
            if (this.mIsUnableToDrag) {
                return false;
            }
        }

        switch (action) {
            case 2:
                int activePointerId = this.mActivePointerId;
                if (activePointerId == -1) {
                    break;
                }

                int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                float x = MotionEventCompat.getX(ev, pointerIndex);
                float dx = x - this.mLastMotionX;
                float xDiff = Math.abs(dx);
                float y = MotionEventCompat.getY(ev, pointerIndex);
                float yDiff = Math.abs(y - this.mInitialMotionY);

                if ((dx != 0.0F) && (!isGutterDrag(this.mLastMotionX, dx)) && (canScroll(this, false, (int) dx, (int) x, (int) y))) {
                    this.mLastMotionX = x;
                    this.mLastMotionY = y;
                    this.mIsUnableToDrag = true;
                    return false;
                }
                if ((xDiff > this.mTouchSlop) && (xDiff * 0.5F > yDiff)) {
                    this.mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(1);
                    this.mLastMotionX = (dx > 0.0F ? this.mInitialMotionX + this.mTouchSlop : this.mInitialMotionX - this.mTouchSlop);

                    this.mLastMotionY = y;
                    setScrollingCacheEnabled(true);
                } else if (yDiff > this.mTouchSlop) {
                    this.mIsUnableToDrag = true;
                }
                if (!this.mIsBeingDragged)
                    break;
                if (!performDrag(x)) break;
                ViewCompat.postInvalidateOnAnimation(this);
                break;
            case 0:
                this.mLastMotionX = (this.mInitialMotionX = ev.getX());
                this.mLastMotionY = (this.mInitialMotionY = ev.getY());
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                this.mIsUnableToDrag = false;

                this.mScroller.computeScrollOffset();
                if ((this.mScrollState == 2) && (Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough)) {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    populate();
                    this.mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(1);
                } else {
                    completeScroll(false);
                    this.mIsBeingDragged = false;
                }

                break;
            case 6:
                onSecondaryPointerUp(ev);
        }

        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);

        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mFakeDragging) {
            return true;
        }

        if ((ev.getAction() == 0) && (ev.getEdgeFlags() != 0)) {
            return false;
        }

        if ((this.mAdapter == null) || (this.mAdapter.getCount() == 0)) {
            return false;
        }

        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);

        int action = ev.getAction();
        boolean needsInvalidate = false;

        switch (action & 0xFF) {
            case 0:
                this.mScroller.abortAnimation();
                this.mPopulatePending = false;
                populate();

                this.mLastMotionX = (this.mInitialMotionX = ev.getX());
                this.mLastMotionY = (this.mInitialMotionY = ev.getY());
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            case 2:
                if (!this.mIsBeingDragged) {
                    int pointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                    float x = MotionEventCompat.getX(ev, pointerIndex);
                    float xDiff = Math.abs(x - this.mLastMotionX);
                    float y = MotionEventCompat.getY(ev, pointerIndex);
                    float yDiff = Math.abs(y - this.mLastMotionY);

                    if ((xDiff > this.mTouchSlop) && (xDiff > yDiff)) {
                        this.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        this.mLastMotionX = (x - this.mInitialMotionX > 0.0F ? this.mInitialMotionX + this.mTouchSlop : this.mInitialMotionX - this.mTouchSlop);

                        this.mLastMotionY = y;
                        setScrollState(1);
                        setScrollingCacheEnabled(true);

                        ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }

                if (!this.mIsBeingDragged)
                    break;
                int activePointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);

                float x = MotionEventCompat.getX(ev, activePointerIndex);
                needsInvalidate |= performDrag(x);
                break;
            case 1:
                if (!this.mIsBeingDragged) break;
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);

                this.mPopulatePending = true;
                int width = getClientWidth();
                int scrollX = getScrollX();
                ItemInfo ii = infoForCurrentScrollPosition();
                int currentPage = ii.position;
                float pageOffset = (scrollX / width - ii.offset) / ii.widthFactor;
                 activePointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);

                 x = MotionEventCompat.getX(ev, activePointerIndex);
                int totalDelta = (int) (x - this.mInitialMotionX);
                int nextPage = determineTargetPage(currentPage, pageOffset, initialVelocity, totalDelta);

                setCurrentItemInternal(nextPage, true, true, initialVelocity);

                this.mActivePointerId = -1;
                endDrag();
                needsInvalidate = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                break;
            case 3:
                if (!this.mIsBeingDragged) break;
                scrollToItem(this.mCurItem, true, 0, false);
                this.mActivePointerId = -1;
                endDrag();
                needsInvalidate = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                break;
            case 5:
                int index = MotionEventCompat.getActionIndex(ev);
                 x = MotionEventCompat.getX(ev, index);
                this.mLastMotionX = x;
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            case 6:
                onSecondaryPointerUp(ev);
                this.mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId));
            case 4:
        }

        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    private boolean performDrag(float x) {
        boolean needsInvalidate = false;

        float deltaX = this.mLastMotionX - x;
        this.mLastMotionX = x;

        float oldScrollX = getScrollX();
        float scrollX = oldScrollX + deltaX;
        int width = getClientWidth();

        float leftBound = width * this.mFirstOffset;
        float rightBound = width * this.mLastOffset;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;

        ItemInfo firstItem = (ItemInfo) this.mItems.get(0);
        ItemInfo lastItem = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
        if (firstItem.position != 0) {
            leftAbsolute = false;
            leftBound = firstItem.offset * width;
        }
        if (lastItem.position != this.mAdapter.getCount() - 1) {
            rightAbsolute = false;
            rightBound = lastItem.offset * width;
        }

        if (scrollX < leftBound) {
            if (leftAbsolute) {
                float over = leftBound - scrollX;
                needsInvalidate = this.mLeftEdge.onPull(Math.abs(over) / width);
            }
            scrollX = leftBound;
        } else if (scrollX > rightBound) {
            if (rightAbsolute) {
                float over = scrollX - rightBound;
                needsInvalidate = this.mRightEdge.onPull(Math.abs(over) / width);
            }
            scrollX = rightBound;
        }

        this.mLastMotionX += scrollX - (int) scrollX;
        scrollTo((int) scrollX, getScrollY());
        pageScrolled((int) scrollX);

        return needsInvalidate;
    }

    private ItemInfo infoForCurrentScrollPosition() {
        int width = getClientWidth();
        float scrollOffset = width > 0 ? getScrollX() / width : 0.0F;
        float marginOffset = width > 0 ? this.mPageMargin / width : 0.0F;
        int lastPos = -1;
        float lastOffset = 0.0F;
        float lastWidth = 0.0F;
        boolean first = true;

        ItemInfo lastItem = null;
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);

            if ((!first) && (ii.position != lastPos + 1)) {
                ii = this.mTempItem;
                ii.offset = (lastOffset + lastWidth + marginOffset);
                ii.position = (lastPos + 1);
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                i--;
            }
            float offset = ii.offset;

            float leftBound = offset;
            float rightBound = offset + ii.widthFactor + marginOffset;
            if ((first) || (scrollOffset >= leftBound)) {
                if ((scrollOffset < rightBound) || (i == this.mItems.size() - 1))
                    return ii;
            } else {
                return lastItem;
            }
            first = false;
            lastPos = ii.position;
            lastOffset = offset;
            lastWidth = ii.widthFactor;
            lastItem = ii;
        }

        return lastItem;
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if ((Math.abs(deltaX) > this.mFlingDistance) && (Math.abs(velocity) > this.mMinimumVelocity)) {
            targetPage = velocity > 0 ? currentPage : currentPage + 1;
        } else {
            float truncator = currentPage >= this.mCurItem ? 0.4F : 0.6F;
            targetPage = (int) (currentPage + pageOffset + truncator);
        }

        if (this.mItems.size() > 0) {
            ItemInfo firstItem = (ItemInfo) this.mItems.get(0);
            ItemInfo lastItem = (ItemInfo) this.mItems.get(this.mItems.size() - 1);

            targetPage = Math.max(firstItem.position, Math.min(targetPage, lastItem.position));
        }

        return targetPage;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = false;

        int overScrollMode = ViewCompat.getOverScrollMode(this);
        if ((overScrollMode == 0) || ((overScrollMode == 1) && (this.mAdapter != null) && (this.mAdapter.getCount() > 1))) {
            if (!this.mLeftEdge.isFinished()) {
                int restoreCount = canvas.save();
                int height = getHeight() - getPaddingTop() - getPaddingBottom();
                int width = getWidth();

                canvas.rotate(270.0F);
                canvas.translate(-height + getPaddingTop(), this.mFirstOffset * width);
                this.mLeftEdge.setSize(height, width);
                needsInvalidate |= this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mRightEdge.isFinished()) {
                int restoreCount = canvas.save();
                int width = getWidth();
                int height = getHeight() - getPaddingTop() - getPaddingBottom();

                canvas.rotate(90.0F);
                canvas.translate(-getPaddingTop(), -(this.mLastOffset + 1.0F) * width);
                this.mRightEdge.setSize(height, width);
                needsInvalidate |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }

        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if ((this.mPageMargin > 0) && (this.mMarginDrawable != null) && (this.mItems.size() > 0) && (this.mAdapter != null)) {
            int scrollX = getScrollX();
            int width = getWidth();

            float marginOffset = this.mPageMargin / width;
            int itemIndex = 0;
            ItemInfo ii = (ItemInfo) this.mItems.get(0);
            float offset = ii.offset;
            int itemCount = this.mItems.size();
            int firstPos = ii.position;
            int lastPos = ((ItemInfo) this.mItems.get(itemCount - 1)).position;
            for (int pos = firstPos; pos < lastPos; pos++) {
                while ((pos > ii.position) && (itemIndex < itemCount)) {
                    itemIndex++;
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                }
                float drawAt;
                if (pos == ii.position) {
                    drawAt = (ii.offset + ii.widthFactor) * width;
                    offset = ii.offset + ii.widthFactor + marginOffset;
                } else {
                    float widthFactor = this.mAdapter.getPageWidth(pos);
                    drawAt = (offset + widthFactor) * width;
                    offset += widthFactor + marginOffset;
                }

                if (drawAt + this.mPageMargin > scrollX) {
                    this.mMarginDrawable.setBounds((int) drawAt, this.mTopPageBounds, (int) (drawAt + this.mPageMargin + 0.5F), this.mBottomPageBounds);

                    this.mMarginDrawable.draw(canvas);
                }

                if (drawAt > scrollX + width)
                    break;
            }
        }
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        setScrollState(1);
        this.mInitialMotionX = (this.mLastMotionX = 0.0F);
        if (this.mVelocityTracker == null)
            this.mVelocityTracker = VelocityTracker.obtain();
        else {
            this.mVelocityTracker.clear();
        }
        long time = SystemClock.uptimeMillis();
        MotionEvent ev = MotionEvent.obtain(time, time, 0, 0.0F, 0.0F, 0);
        this.mVelocityTracker.addMovement(ev);
        ev.recycle();
        this.mFakeDragBeginTime = time;
        return true;
    }

    public void endFakeDrag() {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }

        VelocityTracker velocityTracker = this.mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);

        this.mPopulatePending = true;
        int width = getClientWidth();
        int scrollX = getScrollX();
        ItemInfo ii = infoForCurrentScrollPosition();
        int currentPage = ii.position;
        float pageOffset = (scrollX / width - ii.offset) / ii.widthFactor;
        int totalDelta = (int) (this.mLastMotionX - this.mInitialMotionX);
        int nextPage = determineTargetPage(currentPage, pageOffset, initialVelocity, totalDelta);

        setCurrentItemInternal(nextPage, true, true, initialVelocity);
        endDrag();

        this.mFakeDragging = false;
    }

    public void fakeDragBy(float xOffset) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }

        this.mLastMotionX += xOffset;

        float oldScrollX = getScrollX();
        float scrollX = oldScrollX - xOffset;
        int width = getClientWidth();

        float leftBound = width * this.mFirstOffset;
        float rightBound = width * this.mLastOffset;

        ItemInfo firstItem = (ItemInfo) this.mItems.get(0);
        ItemInfo lastItem = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
        if (firstItem.position != 0) {
            leftBound = firstItem.offset * width;
        }
        if (lastItem.position != this.mAdapter.getCount() - 1) {
            rightBound = lastItem.offset * width;
        }

        if (scrollX < leftBound)
            scrollX = leftBound;
        else if (scrollX > rightBound) {
            scrollX = rightBound;
        }

        this.mLastMotionX += scrollX - (int) scrollX;
        scrollTo((int) scrollX, getScrollY());
        pageScrolled((int) scrollX);

        long time = SystemClock.uptimeMillis();
        MotionEvent ev = MotionEvent.obtain(this.mFakeDragBeginTime, time, 2, this.mLastMotionX, 0.0F, 0);

        this.mVelocityTracker.addMovement(ev);
        ev.recycle();
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if (this.mVelocityTracker != null)
                this.mVelocityTracker.clear();
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;

        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled)
            this.mScrollingCacheEnabled = enabled;
    }

    public boolean canScrollHorizontally(int direction) {
        if (this.mAdapter == null) {
            return false;
        }

        int width = getClientWidth();
        int scrollX = getScrollX();
        if (direction < 0)
            return scrollX > (int) (width * this.mFirstOffset);
        if (direction > 0) {
            return scrollX < (int) (width * this.mLastOffset);
        }
        return false;
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if ((v instanceof ViewGroup)) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int count = group.getChildCount();

            for (int i = count - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if ((x + scrollX >= child.getLeft()) && (x + scrollX < child.getRight()) && (y + scrollY >= child.getTop()) && (y + scrollY < child.getBottom()) && (canScroll(child, true, dx, x + scrollX - child.getLeft(), y + scrollY - child.getTop()))) {
                    return true;
                }
            }
        }

        return (checkV) && (ViewCompat.canScrollHorizontally(v, -dx));
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return (super.dispatchKeyEvent(event)) || (executeKeyEvent(event));
    }

    public boolean executeKeyEvent(KeyEvent event) {
        boolean handled = false;
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 21:
                    handled = arrowScroll(17);
                    break;
                case 22:
                    handled = arrowScroll(66);
                    break;
                case 61:
                    if (Build.VERSION.SDK_INT < 11) {
                        break;
                    }
                    if (KeyEventCompat.hasNoModifiers(event)) {
                        handled = arrowScroll(2);
                    } else {
                        if (!KeyEventCompat.hasModifiers(event, 1)) break;
                        handled = arrowScroll(1);
                    }
            }

        }

        return handled;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        } else if (currentFocused != null) {
            boolean isChild = false;
            for (ViewParent parent = currentFocused.getParent(); (parent instanceof ViewGroup); ) {
                if (parent == this) {
                    isChild = true;
                    break;
                }
                parent = parent.getParent();
            }

            if (!isChild) {
                StringBuilder sb = new StringBuilder();
                sb.append(currentFocused.getClass().getSimpleName());
                for (ViewParent parent = currentFocused.getParent(); (parent instanceof ViewGroup); ) {
                    sb.append(" => ").append(parent.getClass().getSimpleName());

                    parent = parent.getParent();
                }

                Log.e("ViewPager", new StringBuilder().append("arrowScroll tried to find focus based on non-child current focused view ").append(sb.toString()).toString());

                currentFocused = null;
            }
        }

        boolean handled = false;

        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);

        if ((nextFocused != null) && (nextFocused != currentFocused)) {
            if (direction == 17) {
                int nextLeft = getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left;
                int currLeft = getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
                if ((currentFocused != null) && (nextLeft >= currLeft))
                    handled = pageLeft();
                else
                    handled = nextFocused.requestFocus();
            } else if (direction == 66) {
                int nextLeft = getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left;
                int currLeft = getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
                if ((currentFocused != null) && (nextLeft <= currLeft))
                    handled = pageRight();
                else
                    handled = nextFocused.requestFocus();
            }
        } else if ((direction == 17) || (direction == 1)) {
            handled = pageLeft();
        } else if ((direction == 66) || (direction == 2)) {
            handled = pageRight();
        }
        if (handled) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return handled;
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        }
        outRect.left = child.getLeft();
        outRect.right = child.getRight();
        outRect.top = child.getTop();
        outRect.bottom = child.getBottom();

        ViewParent parent = child.getParent();
        while (((parent instanceof ViewGroup)) && (parent != this)) {
            ViewGroup group = (ViewGroup) parent;
            outRect.left += group.getLeft();
            outRect.right += group.getRight();
            outRect.top += group.getTop();
            outRect.bottom += group.getBottom();

            parent = group.getParent();
        }
        return outRect;
    }

    boolean pageLeft() {
        if (this.mCurItem > 0) {
            setCurrentItem(this.mCurItem - 1, true);
            return true;
        }
        return false;
    }

    boolean pageRight() {
        if ((this.mAdapter != null) && (this.mCurItem < this.mAdapter.getCount() - 1)) {
            setCurrentItem(this.mCurItem + 1, true);
            return true;
        }
        return false;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();

        int descendantFocusability = getDescendantFocusability();

        if (descendantFocusability != 393216) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == 0) {
                    ItemInfo ii = infoForChild(child);
                    if ((ii != null) && (ii.position == this.mCurItem)) {
                        child.addFocusables(views, direction, focusableMode);
                    }

                }

            }

        }

        if ((descendantFocusability != 262144) || (focusableCount == views.size())) {
            if (!isFocusable()) {
                return;
            }
            if (((focusableMode & 0x1) == 1) && (isInTouchMode()) && (!isFocusableInTouchMode())) {
                return;
            }
            if (views != null)
                views.add(this);
        }
    }

    public void addTouchables(ArrayList<View> views) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if ((ii != null) && (ii.position == this.mCurItem))
                    child.addTouchables(views);
            }
        }
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int count = getChildCount();
        int end;
        int index;
        int increment;
        if ((direction & 0x2) != 0) {
             index = 0;
             increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        for (int i = index; i != end; i += increment) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if ((ii != null) && (ii.position == this.mCurItem) &&
                        (child.requestFocus(direction, previouslyFocusedRect))) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(event);
        }

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if ((ii != null) && (ii.position == this.mCurItem) && (child.dispatchPopulateAccessibilityEvent(event))) {
                    return true;
                }
            }
        }

        return false;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return ((p instanceof LayoutParams)) && (super.checkLayoutParams(p));
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    static class ViewPositionComparator
            implements Comparator<View> {
        public int compare(View lhs, View rhs) {
            ViewPager.LayoutParams llp = (ViewPager.LayoutParams) lhs.getLayoutParams();
            ViewPager.LayoutParams rlp = (ViewPager.LayoutParams) rhs.getLayoutParams();
            if (llp.isDecor != rlp.isDecor) {
                return llp.isDecor ? 1 : -1;
            }
            return llp.position - rlp.position;
        }
    }



    public static class LayoutParams extends ViewGroup.LayoutParams {
        public boolean isDecor;
        public int gravity;
        float widthFactor = 0.0F;
        boolean needsMeasure;
        int position;
        int childIndex;

        public LayoutParams() {
            super(-1,-1);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context,attrs);

            TypedArray a = context.obtainStyledAttributes(attrs, ViewPager.LAYOUT_ATTRS);
            this.gravity = a.getInteger(0, 48);
            a.recycle();
        }
    }



    private class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }



    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(ViewPager.class.getName());
            AccessibilityRecordCompat recordCompat = AccessibilityRecordCompat.obtain();
            recordCompat.setScrollable(canScroll());
            if ((event.getEventType() == 4096) && (ViewPager.this.mAdapter != null)) {
                recordCompat.setItemCount(ViewPager.this.mAdapter.getCount());
                recordCompat.setFromIndex(ViewPager.this.mCurItem);
                recordCompat.setToIndex(ViewPager.this.mCurItem);
            }
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(ViewPager.class.getName());
            info.setScrollable(canScroll());
            if (ViewPager.this.canScrollHorizontally(1)) {
                info.addAction(4096);
            }
            if (ViewPager.this.canScrollHorizontally(-1))
                info.addAction(8192);
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            }
            switch (action) {
                case 4096:
                    if (ViewPager.this.canScrollHorizontally(1)) {
                        ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
                        return true;
                    }
                    return false;
                case 8192:
                    if (ViewPager.this.canScrollHorizontally(-1)) {
                        ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
                        return true;
                    }
                    return false;
            }
            return false;
        }

        private boolean canScroll() {
            return (ViewPager.this.mAdapter != null) && (ViewPager.this.mAdapter.getCount() > 1);
        }
    }



    public static class SavedState extends View.BaseSavedState {
        int position;
        Parcelable adapterState;
        ClassLoader loader;
        public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {
            public ViewPager.SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new ViewPager.SavedState(in, loader);
            }

            public ViewPager.SavedState[] newArray(int size) {
                return new ViewPager.SavedState[size];
            }
        });

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in);
            if (loader == null) {
                loader = getClass().getClassLoader();
            }
            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }



    static abstract interface Decor {
    }



    static abstract interface OnAdapterChangeListener {
        public abstract void onAdapterChanged(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2);
    }



    public static abstract interface PageTransformer {
        public abstract void transformPage(View paramView, float paramFloat);
    }



    public static class SimpleOnPageChangeListener
            implements ViewPager.OnPageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }



    public static abstract interface OnPageChangeListener {
        public abstract void onPageScrolled(int paramInt1, float paramFloat, int paramInt2);

        public abstract void onPageSelected(int paramInt);

        public abstract void onPageScrollStateChanged(int paramInt);
    }



    static class ItemInfo {
        Object object;
        int position;
        boolean scrolling;
        float widthFactor;
        float offset;
    }
}
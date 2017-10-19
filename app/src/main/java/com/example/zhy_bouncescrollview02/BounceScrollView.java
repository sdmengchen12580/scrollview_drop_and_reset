package com.example.zhy_bouncescrollview02;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 支持上下反弹效果的ScrollView
 * 
 * @author zhy
 * 
 */
public class BounceScrollView extends ScrollView
{

	private boolean isCalled ; 
	
	private Callback mCallback;

	/**
	 * 包含的View
	 */
	private View mView;
	/**
	 * 存储正常时的位置
	 */
	private Rect mRect = new Rect();

	/**
	 * y坐标
	 */
	private int y;

	private boolean isFirst = true;

	public BounceScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/***
	 * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
	 * 方法，也应该调用父类的方法，使该方法得以执行.
	 */
	@Override
	protected void onFinishInflate()
	{
		/**拿到listview，赋值给View*/
		if (getChildCount() > 0)
			mView = getChildAt(0);
		super.onFinishInflate();
	}

	/**触碰事件*/
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (mView != null)
		{
			commonOnTouch(ev);
		}
		return super.onTouchEvent(ev);
	}

	/**从MotionEvent获取意图和Y的坐标*/
	private void commonOnTouch(MotionEvent ev)
	{
		int action = ev.getAction();
		int cy = (int) ev.getY();
		switch (action)
		{

		case MotionEvent.ACTION_DOWN:
			break;
		/**
		 * 跟随手指移动
		 */
		case MotionEvent.ACTION_MOVE:
			/**cy为第触屏点的y轴位置*/
			/**y为移动前的Y轴位置*/
			/**dy为在y轴移动的位置*/
			int dy = cy - y;
			/**防止第一次手指稍微的往下滑了一下，后面再移动的时候就不是第一次了，不走这个if的判断*/
			if (isFirst)
			{
				dy = 0;
				isFirst = false;
			}
			/**每次移动的时候都将此时的触屏点的Y轴位置给y变量*/
			y = cy;
			if (isNeedMove())
			{
				if (mRect.isEmpty())
				{
					/**
					 * 记录移动前的位置
					 */
					mRect.set(mView.getLeft(), mView.getTop(),
							mView.getRight(), mView.getBottom());
				}
				mView.layout(mView.getLeft(), mView.getTop() + 2 * dy / 3,
						mView.getRight(), mView.getBottom() + 2 * dy / 3);
				/**移动后大于二分之一屏幕——去对应的操作——同时，重置位置*/
				if (shouldCallBack(dy))
				{
					if (mCallback != null)
					{
						if(!isCalled)
						{
							isCalled = true ; 
							resetPosition();
							mCallback.callback();
						}
					}
				}
			}
			break;
		/**
		 * 反弹回去
		 */
		case MotionEvent.ACTION_UP:
			if (!mRect.isEmpty())
			{
				resetPosition();
			}
			break;

		}
	}

	/**
	 * 当从上往下，移动距离达到一半时，回调接口
	 */
	private boolean shouldCallBack(int dy)
	{
		if (dy > 0 && mView.getTop() > getHeight() / 2)
			return true;
		return false;
	}

	/**重置位置——平移动画*/
	private void resetPosition()
	{
		Animation animation = new TranslateAnimation(0, 0, mView.getTop(),
				mRect.top);
		animation.setDuration(200);
		animation.setFillAfter(true);
		mView.startAnimation(animation);
		mView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
		mRect.setEmpty();
		isFirst = true;
		isCalled = false ; 
	}

	/***
	 * 是否需要移动布局 ——没有移动之前，返回的布尔值为true
	 * inner.getMeasuredHeight():获取的是控件的总高度
	 * getHeight()：获取的是屏幕的高度
	 */
	public boolean isNeedMove()
	{
		/**offset为scrollview距离顶部的距离*/
		int offset = mView.getMeasuredHeight() - getHeight();
		/**getScrollY（）返回这个视图的滚动顶部位置*/
		int scrollY = getScrollY();
		Log.e("视图的滚动顶部位置为: ", scrollY+"");
		/**没有下拉——0是顶部，后面那个是底部*/
		if (scrollY == 0 || scrollY == offset)
		{
			return true;
		}
		return false;
	}

	/**对外提供的接口*/
	public void setCallBack(Callback callback)
	{
		mCallback = callback;
	}

	interface Callback
	{
		void callback();
	}

}

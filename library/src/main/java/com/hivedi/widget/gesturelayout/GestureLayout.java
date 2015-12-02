package com.hivedi.widget.gesturelayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

/**
 * Created by Hivedi2 on 2015-12-02.
 *
 */
public class GestureLayout extends RelativeLayout {

	public interface OnDoubleTapListener {
		void onDoubleTap(float x, float y);
	}

	public interface OnSingleTapListener {
		void onSingleTap(float x, float y);
	}

	public interface OnSwipeListener {
		void onSwipeLeft();
		void onSwipeRight();
		void onSwipeTop();
		void onSwipeBottom();
	}

	private GestureDetector mGestureDetector;
	private OnDoubleTapListener mOnDoubleTapListener;
	private OnSwipeListener mOnSwipeListener;
	private OnSingleTapListener mOnSingleTapListener;
	private Paint paint = new Paint();
	private Paint paintLine = new Paint();
	private int color = 0x77FFFFFF;
	private int rippleSize = -1;
	private PointF touch, swipeTouchStart, swipeTouchEnd;
	private boolean drawLineUnderFinger = false;

	public GestureLayout(Context context) {
		super(context);
		init(context, null);
	}

	public GestureLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public GestureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public GestureLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GestureLayout);
			rippleSize = ta.getDimensionPixelSize(R.styleable.GestureLayout_gestureRippleRadius, -1);
			color = ta.getColor(R.styleable.GestureLayout_gestureRippleColor, 0x77FFFFFF);
			drawLineUnderFinger = ta.getBoolean(R.styleable.GestureLayout_gestureDrawLineUnderFinger, false);
			ta.recycle();
		}

		paint.setColor(color);
		paint.setAntiAlias(true);

		paintLine.setColor(color);                    // set the color
		paintLine.setStrokeWidth(6);               // set the size
		paintLine.setDither(true);                    // set the dither to true
		paintLine.setStyle(Paint.Style.STROKE);       // set to STOKE
		paintLine.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
		paintLine.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
		paintLine.setPathEffect(new CornerPathEffect(10) );   // set the path effect when they join.
		paintLine.setAntiAlias(true);

		// gesture detection http://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures
		mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

			private static final int SWIPE_THRESHOLD = 100;
			private static final int SWIPE_VELOCITY_THRESHOLD = 100;

			@Override
			public boolean onDown(MotionEvent e) {

				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				boolean result = false;
				try {
					float diffY = e2.getY() - e1.getY();
					float diffX = e2.getX() - e1.getX();
					if (Math.abs(diffX) > Math.abs(diffY)) {
						if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
							if (diffX > 0) {
								if (mOnSwipeListener != null) {
									mOnSwipeListener.onSwipeRight();
									startRippleAnimation(e2.getX(), e2.getY());
								}
							} else {
								if (mOnSwipeListener != null) {
									mOnSwipeListener.onSwipeLeft();
									startRippleAnimation(e2.getX(), e2.getY());
								}
							}
						}
						result = true;
					} else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffY > 0) {
							if (mOnSwipeListener != null) {
								mOnSwipeListener.onSwipeBottom();
								startRippleAnimation(e2.getX(), e2.getY());
							}
						} else {
							if (mOnSwipeListener != null) {
								mOnSwipeListener.onSwipeTop();
								startRippleAnimation(e2.getX(), e2.getY());
							}
						}
						result = true;
					}

				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return result;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if (mOnSingleTapListener != null) {
					mOnSingleTapListener.onSingleTap(e.getX(), e.getY());
				}
				//startRippleAnimation(e.getX(), e.getY());
				return super.onSingleTapConfirmed(e);
			}

			@Override
			public void onShowPress(MotionEvent e) {
				super.onShowPress(e);

			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				float x = e.getX();
				float y = e.getY();
				if (mOnDoubleTapListener != null) {
					mOnDoubleTapListener.onDoubleTap(x, y);
				}
				//startRippleAnimation(e.getX(), e.getY());
				return true;
			}
		});
	}

	private void startRippleAnimation(final float x, final float y) {
		if (rippleSize > 0) {
			touch = new PointF(x, y);
			Animation animation = new ValueAnimation(0, rippleSize);
			animation.setDuration(250);
			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					touch = null;
					invalidate();
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
			});
			startAnimation(animation);
		}
	}

	// inspired by https://androidreclib.wordpress.com/2014/11/18/the-touch-ripple-on-gingerbread/
	@Override
	protected void dispatchDraw(Canvas canvas) {
		Animation animation = getAnimation();
		if (animation != null && animation instanceof ValueAnimation && !animation.hasEnded()) {
			ValueAnimation valueAnimation = (ValueAnimation) animation;
			paint.setAlpha((int) (127 * (1 - valueAnimation.getInterpolation())));
			canvas.drawCircle(touch.x, touch.y, valueAnimation.getValue(), paint);
		}
		if (drawLineUnderFinger && swipeTouchStart != null && swipeTouchEnd != null) {
			canvas.drawLine(swipeTouchStart.x, swipeTouchStart.y, swipeTouchEnd.x, swipeTouchEnd.y, paintLine);
		}
		super.dispatchDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		switch (e.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				if (drawLineUnderFinger) {
					if (swipeTouchStart == null) {
						swipeTouchStart = new PointF(e.getX(), e.getY());
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				startRippleAnimation(e.getX(), e.getY());
				if (drawLineUnderFinger) {
					swipeTouchStart = null;
					swipeTouchEnd = null;
					invalidate();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (drawLineUnderFinger) {
					if (swipeTouchStart != null) {
						swipeTouchEnd = new PointF(e.getX(), e.getY());
						invalidate();
					}
				}
				break;
		}
		return mGestureDetector.onTouchEvent(e);
	}

	public OnDoubleTapListener getOnDoubleTapListener() {
		return mOnDoubleTapListener;
	}

	public void setOnDoubleTapListener(OnDoubleTapListener mOnDoubleTapListener) {
		this.mOnDoubleTapListener = mOnDoubleTapListener;
	}

	public OnSwipeListener getOnSwipeListener() {
		return mOnSwipeListener;
	}

	public void setOnSwipeListener(OnSwipeListener mOnSwipeListener) {
		this.mOnSwipeListener = mOnSwipeListener;
	}

	public OnSingleTapListener getOnSingleTapListener() {
		return mOnSingleTapListener;
	}

	public void setOnSingleTapListener(OnSingleTapListener mOnSingleTapListener) {
		this.mOnSingleTapListener = mOnSingleTapListener;
	}

	public void setGestureRippleColor(int color) {
		this.color = color;
	}

	public int getGestureRippleColor() {
		return color;
	}

	public void setGestureRippleSize(int size) {
		rippleSize = size;
	}

	public int getGestureRippleSize(int size) {
		return rippleSize;
	}

	public boolean isDrawLineUnderFinger() {
		return drawLineUnderFinger;
	}

	public void setDrawLineUnderFinger(boolean drawLineUnderFinger) {
		this.drawLineUnderFinger = drawLineUnderFinger;
	}

	public class ValueAnimation extends Animation {
		private final float from;
		private final float to;
		private float value;
		private float interpolation;
		public ValueAnimation(float from, float to) {
			this.from = from;
			this.to = to;
		}
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			value = from * (1 - interpolatedTime) + to * interpolatedTime;
			interpolation = interpolatedTime;
			super.applyTransformation(interpolatedTime, t);
			invalidate();
		}

		public float getValue() { return value; }
		public float getInterpolation() { return interpolation; }
	}

}

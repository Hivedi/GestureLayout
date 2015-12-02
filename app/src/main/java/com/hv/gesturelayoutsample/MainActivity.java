package com.hv.gesturelayoutsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hivedi.widget.gesturelayout.GestureLayout;

public class MainActivity extends AppCompatActivity {

	private GestureLayout mGestureLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mGestureLayout = (GestureLayout) findViewById(R.id.mGestureLayout);
		mGestureLayout.setOnSingleTapListener(new GestureLayout.OnSingleTapListener() {
			@Override
			public void onSingleTap(float x, float y) {
				// single tap
			}
		});
		mGestureLayout.setOnSwipeListener(new GestureLayout.OnSwipeListener() {
			@Override
			public void onSwipeLeft() {

			}

			@Override
			public void onSwipeRight() {

			}

			@Override
			public void onSwipeTop() {

			}

			@Override
			public void onSwipeBottom() {

			}
		});
		mGestureLayout.setOnDoubleTapListener(new GestureLayout.OnDoubleTapListener() {
			@Override
			public void onDoubleTap(float x, float y) {

			}
		});
	}
}

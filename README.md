# GestureLayout
Simple gesture detection layout

## Layout file
```xml
<com.hivedi.widget.gesturelayout.GestureLayout
	android:id="@+id/mGestureLayout"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	app:gestureRippleRadius="50dp"
	app:gestureRippleColor="#77FF0000"
	app:gestureDrawLineUnderFinger="true"/>
```

## Listeners
```java
mGestureLayout = (GestureLayout) findViewById(R.id.mGestureLayout);
mGestureLayout.setOnSingleTapListener(new GestureLayout.OnSingleTapListener() {
	@Override
	public void onSingleTap(float x, float y) {
		
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
```

# Include in project
## Gradle repo:
```
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```

## Dependency:
```
dependencies {
    compile 'com.github.Hivedi:GestureLayout:1.0.2'
}
```
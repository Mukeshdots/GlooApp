
package com.app.glooworkspace;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.TextView;

public class MyDragBuilder extends DragShadowBuilder implements OnDragListener{
	private Drawable mShadow;
	private View view;
	private int position;
	
	public MyDragBuilder(View v , int positionNo) {
		super(v);
		view = v;
		position = positionNo;
		mShadow = v.getResources().getDrawable(R.drawable.btn_default_pressed);
		mShadow.setCallback(v);
		mShadow.setBounds(0, 0, v.getWidth(), v.getHeight());
	}

	@Override
	public void onDrawShadow(Canvas canvas) {
		super.onDrawShadow(canvas);
		mShadow.draw(canvas);
		// draw shadow of view
		getView().draw(canvas);
		
		// add drag listener on items
		getView().setOnDragListener(this);

	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		TextView txt;
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_ENDED:
			txt = new TextView(view.getContext());
			txt.setText("Item Position " + position);
			MainActivity.dropLayout.addView(txt);
			break;
		case DragEvent.ACTION_DRAG_STARTED:
			MainActivity.slide_me.openRightSide();
			break;
		default:
			break;
		}
		return true;
	}
}

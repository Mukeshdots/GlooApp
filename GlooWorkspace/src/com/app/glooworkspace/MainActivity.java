package com.app.glooworkspace;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	private String[] listviewItemsName;
	private int[] listviewItemsImage;
	private ArrayList<MergeData> mergeListItems = new ArrayList<MergeData>();
	private LinearLayout dropLayout, listShowLayout , listHideLayout , projectsLayout , layoutMain;	
	private ImageView imageViewSlider;
	private ScrollView scrollLayout;
	private TextView undo;
	public  static SlideHolder slide_holder;
	private static View viewInflate;
	private int windowWidth;
	private ScaleGestureDetector scaleGestureDetector;
	private FrameLayout.LayoutParams frameLayout ;
	private int firstClicked = -1 , clicked=-1 , firstTimeClicked = 0;
	private ArrayList<Integer> arrClickedItems = new ArrayList<Integer>();
	private boolean islongClicked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		onViewWidth();
		onListItemsName(); // Add array of the items name
		onListItemsImage(); // Add array of the items image
		onMergeListViewItems(); // merge both the items array to add in a single list adapter
		initView(); // set all views of the layout include id
		setData(); 
		onItemClick(); // on items click event
		onDrop(); // set items on drop
	}

	private void onListItemsName() {
		listviewItemsName = new String[] {"Word" , "Pdf" , "Xls" ,"Word" , "Pdf" , "Xls" ,"Word" , "Pdf" , "Xls", "Word" , "Pdf" , "Xls"};
	}

	private void onListItemsImage() {
		listviewItemsImage = new int[] { R.drawable.word, R.drawable.pdf,
				R.drawable.xls, R.drawable.word, R.drawable.pdf,
				R.drawable.xls,R.drawable.word, R.drawable.pdf,
				R.drawable.xls,R.drawable.word, R.drawable.pdf,
				R.drawable.xls };
	}


	private void onMergeListViewItems() {

		for (int arrLength = 0; arrLength < listviewItemsImage.length; arrLength++) {
			MergeData mergeData = new MergeData(listviewItemsImage[arrLength],listviewItemsName[arrLength]);
			mergeListItems.add(mergeData);
		}
	}

	private void onViewWidth() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		windowWidth = displaymetrics.widthPixels;
		scaleGestureDetector = new ScaleGestureDetector(this, new OnPinchListener());
	}

	private void initView() {

		layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
		listShowLayout = (LinearLayout) findViewById(R.id.layout_list_items_show);
		listHideLayout = (LinearLayout) findViewById(R.id.layout_list_items_hideshadow);
		imageViewSlider = (ImageView) findViewById (R.id.imageViewSlider);
		// add sliding layout and inflate layout in it and get items id
		//slide_me = new SimpleSideDrawer (MainActivity.this);
		slide_holder = (SlideHolder) findViewById(R.id.slideLayout);
		//slide_me.setRightBehindContentView (R.layout.inflate_dropdown);

		viewInflate = (View) findViewById(R.id.slide_inflate_layout);
		frameLayout = new FrameLayout.LayoutParams((windowWidth/2+30),LayoutParams.MATCH_PARENT);
		viewInflate.setLayoutParams(frameLayout);


		projectsLayout = (LinearLayout) findViewById (R.id.projects_layout);
		dropLayout = (LinearLayout) findViewById (R.id.drop_layout);
		scrollLayout = (ScrollView) findViewById (R.id.scroll_layout);
		undo = (TextView) findViewById(R.id.textViewUndo);

		undo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*	slide_me.openRightSide();
				dragDropHide.setVisibility(View.VISIBLE);
				dragDropShow.setBackgroundResource(android.R.color.transparent);*/
			}
		});

		scrollLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				scaleGestureDetector.onTouchEvent(event);
				return true;
			}
		});
		
		layoutMain.setOnDragListener(new OnDragListener() {

			public boolean onDrag(View v, DragEvent event) {
				switch (event.getAction()) {
				/*case DragEvent.ACTION_DRAG_ENTERED:
					if(slide_me.isClosed()){
						slide_me.toggleRightDrawer();
					}
					slide_holder.toggle();
					Log.e("ACTION_DRAG_ENTERED " , "ACTION_DRAG_ENTERED ");
					v.setBackgroundColor(Color.GRAY);
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					if(!slide_me.isClosed()){
						slide_me.closeRightSide();
					}
					slide_holder.toggle();
					Log.e("ACTION_DRAG_EXITED " , "ACTION_DRAG_EXITED ");
					v.setBackgroundColor(Color.TRANSPARENT);
					break;
				 */
				case DragEvent.ACTION_DRAG_STARTED:
					Log.e("ACTION_DRAG_STARTED " , "ACTION_DRAG_STARTED ");
					return true;
				case DragEvent.ACTION_DRAG_ENTERED:
					slide_holder.toggle();
					Log.e("ACTION_DRAG_ENTERED " , "ACTION_DRAG_ENTERED ");
					break;
					
					/*
				case DragEvent.ACTION_DRAG_ENDED:
					Log.e("ACTION_DRAG_ENDED " , "ACTION_DRAG_ENDED ");
					break;

				case DragEvent.ACTION_DROP:
					v.setBackgroundColor(Color.TRANSPARENT);
					Log.e("ACTION_DROP " , "ACTION_DROP ");
					return processDrop(event);*/
				}
				return false;
			}
		});

	}


	private void onItemClick() {
		imageViewSlider.setOnClickListener(this);	
	}

	private void onDrop() {

		scrollLayout.setOnDragListener(new OnDragListener() {

			public boolean onDrag(View v, DragEvent event) {
				switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_ENTERED:
					Log.e("ACTION_DRAG_ENTERED " , "ACTION_DRAG_ENTERED ");
					v.setBackgroundColor(Color.GRAY);
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					Log.e("ACTION_DRAG_EXITED " , "ACTION_DRAG_EXITED ");
					v.setBackgroundColor(Color.TRANSPARENT);
					break;
				 
				case DragEvent.ACTION_DRAG_STARTED:
					Log.e("ACTION_DRAG_STARTED " , "ACTION_DRAG_STARTED ");
					return true;
				case DragEvent.ACTION_DRAG_ENDED:
					Log.e("ACTION_DRAG_ENDED " , "ACTION_DRAG_ENDED ");
					break;

				case DragEvent.ACTION_DROP:
					v.setBackgroundColor(Color.TRANSPARENT);
					Log.e("ACTION_DROP " , "ACTION_DROP ");
					return processDrop(event);
				}
				return false;
			}
		});
	}

	private boolean processDrop(DragEvent event) {
		Log.e("processDrop " , "processDrop ");
		addLayout(event);
		return true;
	}

	private void addLayout(DragEvent event) {

		LinearLayout layoutProject = new LinearLayout(this);
		layoutProject.setOrientation(LinearLayout.VERTICAL);
		layoutProject.setGravity(Gravity.CENTER_VERTICAL);

		LinearLayout layoutProjectHeading = new LinearLayout(this);
		topLayoutHeadingFolder(layoutProjectHeading);
		layoutProject.addView(layoutProjectHeading);

		for(int i= 0 ; i <arrClickedItems.size() ; i++){
			int tag = arrClickedItems.get(i);
			LinearLayout layoutAddDropInflate = new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0,5,0,0);
			layoutAddDropInflate.setLayoutParams(params);
			layoutAddDropInflate.setOrientation(LinearLayout.HORIZONTAL);
			layoutAddDropInflate.setGravity(Gravity.CENTER_VERTICAL);

			ImageView imageViewAddDropInflate = new ImageView(this);
			imageViewAddDropInflate.setLayoutParams(new LinearLayout.LayoutParams(70,70));
			imageViewAddDropInflate.setScaleType(ScaleType.FIT_XY);
			imageViewAddDropInflate.setImageResource(mergeListItems.get(tag).getListViewInflateImage());

			TextView textViewAddDropInflate= new TextView(this);
			textViewAddDropInflate.setPadding(10, 0,0,0);
			textViewAddDropInflate.setText(mergeListItems.get(tag).getListViewInflateName());
			textViewAddDropInflate.setGravity(Gravity.CENTER_VERTICAL);

			layoutAddDropInflate.addView(imageViewAddDropInflate);
			layoutAddDropInflate.addView(textViewAddDropInflate);

			layoutProject.addView(layoutAddDropInflate);
		}

		dropLayout.addView(layoutProject);
		bottomLayoutFolder();

		/*if(slide_me.isClosed()){
			slide_me.toggleRightDrawer();
		}
		else{
			slide_me.closeRightSide();
		}*/
		slide_holder.toggle();
	}

	private void topLayoutHeadingFolder(LinearLayout layoutProjectHeading) {

		layoutProjectHeading.setOrientation(LinearLayout.HORIZONTAL);
		layoutProjectHeading.setPadding(5,5,5,5);
		layoutProjectHeading.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layoutProjectHeading.setBackgroundResource(android.R.color.black);

		ImageView imageIcon = new ImageView(this);
		imageIcon.setBackgroundResource(R.drawable.ic_launcher);
		imageIcon.setLayoutParams(new LayoutParams(30, 30));

		TextView projectHeading = new TextView(this);
		projectHeading.setText("PROJECT ");
		projectHeading.setPadding(10,0,0,0);

		layoutProjectHeading.addView(imageIcon);
		layoutProjectHeading.addView(projectHeading);

	}

	private void bottomLayoutFolder() {

		LinearLayout layoutProjectsHeading = new LinearLayout(this);
		layoutProjectsHeading.setOrientation(LinearLayout.HORIZONTAL);
		layoutProjectsHeading.setPadding(5,5,5,5);
		layoutProjectsHeading.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layoutProjectsHeading.setBackgroundResource(android.R.color.black);

		ImageView imgIcon = new ImageView(this);
		imgIcon.setBackgroundResource(R.drawable.ic_launcher);
		imgIcon.setLayoutParams(new LayoutParams(30, 30));

		TextView projectsHeading = new TextView(this);
		projectsHeading.setText("PROJECT ");
		projectsHeading.setPadding(10,0,0,0);

		layoutProjectsHeading.addView(imgIcon);
		layoutProjectsHeading.addView(projectsHeading);

		projectsLayout.addView(layoutProjectsHeading);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageViewSlider:
			/*if(slide_me.isClosed()){
			//	slide_me.openRightSide();
				slide_me.toggleRightDrawer();
			}
			else{
				slide_me.closeRightSide();
			}*/
			slide_holder.toggle();
			break;
		default:
			break;
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setData(){

		for(int i=0; i<mergeListItems.size() ; i++){

			LinearLayout layoutInflate = new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0,5,0,0);
			layoutInflate.setLayoutParams(params);

			layoutInflate.setOrientation(LinearLayout.HORIZONTAL);
			layoutInflate.setGravity(Gravity.CENTER_VERTICAL);

			ImageView imageIcon = new ImageView(this);
			imageIcon.setLayoutParams(new LinearLayout.LayoutParams(70, 70));
			imageIcon.setScaleType(ScaleType.FIT_XY);
			imageIcon.setImageResource(mergeListItems.get(i).getListViewInflateImage());

			TextView textView = new TextView(this);
			textView.setPadding(10, 0,0,0);
			textView.setText(mergeListItems.get(i).getListViewInflateName());
			textView.setGravity(Gravity.CENTER_VERTICAL);

			layoutInflate.addView(imageIcon);
			layoutInflate.addView(textView);
			layoutInflate.setTag(i);

			listShowLayout.addView(layoutInflate);


			layoutInflate.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					int tag = (Integer) v.getTag();
					int tagItem;
					if(firstClicked == -1 || clicked == tag){
						tagItem = tag;
						if(islongClicked == false){
							v.setBackgroundResource(android.R.color.darker_gray);
							islongClicked = true;
							clicked = tagItem;
							firstClicked = tagItem;

							firstTimeClicked = 1;
							arrClickedItems.add(tag);

							addListSelectedItemsLayout(tag);

						}
						else{
							v.setBackground(null);
							islongClicked = false;
							firstClicked = -1;

							for(int i=0 ; i<arrClickedItems.size() ; i++){
								if(tag == arrClickedItems.get(i)){
									arrClickedItems.remove(i);	
									listHideLayout.removeViewAt(i);
								}
							}

						}
					}
					return false;
				}

			});

			layoutInflate.setOnClickListener(new View.OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {

					if(v.isLongClickable()){
						int tag = (Integer) v.getTag();
						if(clicked == tag){
							if(firstTimeClicked == 1){
								firstTimeClicked = 0;
							}else{
								v.setBackground(null);
								islongClicked = false;
								firstClicked = -1;

								for(int i=0 ; i<arrClickedItems.size() ; i++){
									if(tag == arrClickedItems.get(i)){
										arrClickedItems.remove(i);	
										listHideLayout.removeViewAt(i);
									}
								}
							}
						}

						else{
							if(clicked != -1){
								if(islongClicked == true){
									if(v.getBackground() == null){
										v.setBackgroundResource(android.R.color.darker_gray);
										arrClickedItems.add(tag);

										addListSelectedItemsLayout(tag);

									}else{
										v.setBackground(null);
										//	v.setBackgroundResource(android.R.color.transparent);
										for(int i=0 ; i<arrClickedItems.size() ; i++){
											if(tag == arrClickedItems.get(i)){
												arrClickedItems.remove(i);	
												listHideLayout.removeViewAt(i);
											}
										}
									}
								}
								else{
									v.setBackground(null);
									for(int i=0 ; i<arrClickedItems.size() ; i++){
										if(tag == arrClickedItems.get(i)){
											arrClickedItems.remove(i);	
											listHideLayout.removeViewAt(i);
										}
									}
								}
							}
						}
					}else{
					}
				}
			});


			layoutInflate.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(arrClickedItems.size()>0){
						switch (event.getAction()) {
						case MotionEvent.ACTION_MOVE:
							listHideLayout.startDrag(null, new MyDragShadowBuilder(listHideLayout), null , 0);
							break;
						default:
							break;
						}
					}
					return false;
				}
			});
		}

	}

	private void addListSelectedItemsLayout(int tagNo) {

		LinearLayout layoutSelectedItem = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0,5,0,0);
		layoutSelectedItem.setLayoutParams(params);
		layoutSelectedItem.setOrientation(LinearLayout.HORIZONTAL);
		layoutSelectedItem.setGravity(Gravity.CENTER_VERTICAL);

		ImageView imageViewSelectedItem = new ImageView(this);
		imageViewSelectedItem.setLayoutParams(new LinearLayout.LayoutParams(70, 70));
		imageViewSelectedItem.setScaleType(ScaleType.FIT_XY);
		imageViewSelectedItem.setImageResource(mergeListItems.get(tagNo).getListViewInflateImage());

		TextView textViewSelectedItem= new TextView(this);
		textViewSelectedItem.setPadding(10, 0,0,0);
		textViewSelectedItem.setText(mergeListItems.get(tagNo).getListViewInflateName());
		textViewSelectedItem.setGravity(Gravity.CENTER_VERTICAL);

		layoutSelectedItem.addView(imageViewSelectedItem);
		layoutSelectedItem.addView(textViewSelectedItem);

		listHideLayout.addView(layoutSelectedItem);
	}


	private class OnPinchListener extends SimpleOnScaleGestureListener {

		float startingSpan; 
		float endSpan;
		float startFocusX;
		float startFocusY;

		public boolean onScaleBegin(ScaleGestureDetector detector) {
			startingSpan = detector.getCurrentSpan();
			startFocusX = detector.getFocusX();
			startFocusY = detector.getFocusY();
			return true;
		}


		public boolean onScale(ScaleGestureDetector detector) {

			/*frameLayout = new FrameLayout.LayoutParams((int)detector.getFocusX(), (int) detector.getFocusY());
			viewInflate.setLayoutParams(frameLayout);*/
			return true;
		}

		public void onScaleEnd(ScaleGestureDetector detector) {

			float currentx = detector.getFocusX();
			float currenty = detector.getFocusY();


			Log.e("Current x" , "X"+ currentx);
			Log.e("Current y" , "y"+ currenty);
			Log.e("last x" , "X"+ startFocusX);
			Log.e("lasaty x" , "Y"+ startFocusY);


			if(currentx-15 > startFocusX && currenty+15 < startFocusY){
				frameLayout = new FrameLayout.LayoutParams((windowWidth/2+30),LayoutParams.MATCH_PARENT);
				viewInflate.setLayoutParams(frameLayout);
			}else{
				frameLayout = new FrameLayout.LayoutParams((windowWidth),LayoutParams.MATCH_PARENT);
				viewInflate.setLayoutParams(frameLayout);
			}


			/*if(viewInflate.getWidth() == windowWidth/2+30){
				frameLayout = new FrameLayout.LayoutParams((windowWidth),LayoutParams.MATCH_PARENT);
				viewInflate.setLayoutParams(frameLayout);
			}else{
				frameLayout = new FrameLayout.LayoutParams((windowWidth/2+30),LayoutParams.MATCH_PARENT);
				viewInflate.setLayoutParams(frameLayout);
			}*/
		}
	}
}

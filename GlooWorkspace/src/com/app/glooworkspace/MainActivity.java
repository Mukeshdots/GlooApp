package com.app.glooworkspace;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;

public class MainActivity extends Activity implements OnClickListener , OnTouchListener{

	private DrawerLayout drawerLayout;
	private ListView listviewItems;
	private String[] listviewItemsName;
	private int[] listviewItemsImage;
	private ArrayList<MergeData> mergeListItems = new ArrayList<MergeData>();
	private ListViewItemsAdapter listviewAdapter;
	private LinearLayout inflateBottomBar , dropLayout , projectsLayout , dragDropShow , dragDropHide;
	private ImageView imageViewSlider;
	private ScrollView scrollLayout;
	private TextView undo;
	public  static SimpleSideDrawer slide_me;
	private ArrayList<Integer> arrItem = new ArrayList<Integer>(); // this used to check the items which pick
	private static int  flag = 0 , // this flag used to change modes 
			flagCheckFocus  = 0,   // this flag used to change focus on slide view pinch to zoom  
			flagtouchState = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		onListItemsName(); // Add array of the items name
		onListItemsImage(); // Add array of the items image
		onMergeListViewItems(); // merge both the items array to add in a single list adapter
		initView(); // set all views of the layout include id
		setListViewAdapter(); // add items in adapter and set adapter
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

	private void initView() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		inflateBottomBar = (LinearLayout) findViewById (R.id.inflate_BottomBar);
		listviewItems = (ListView) findViewById (R.id.list_items);
		imageViewSlider = (ImageView) findViewById (R.id.imageViewSlider);
		// add sliding layout and inflate layout init and get items id
		slide_me = new SimpleSideDrawer (MainActivity.this);
		slide_me.setRightBehindContentView (R.layout.inflate_dropdown);
		dropLayout = (LinearLayout) findViewById (R.id.drop_layout);
		scrollLayout = (ScrollView) findViewById (R.id.scroll_layout);
		projectsLayout = (LinearLayout) findViewById (R.id.projects_layout);
		dragDropHide = (LinearLayout) findViewById (R.id.dragdrop_hidelayout);
		dragDropShow = (LinearLayout) findViewById (R.id.dragdrop_showlayout);
		undo = (TextView) findViewById(R.id.textViewUndo);

		if(flagCheckFocus == 0){
			scrollLayout.setOnTouchListener(this);
		}else{
			scrollLayout.setOnTouchListener(null);
		}
		
		undo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				slide_me.openRightSide();
				dragDropHide.setVisibility(View.VISIBLE);
				dragDropShow.setBackgroundResource(android.R.color.transparent);
			}
		});
	}


	private void onItemClick() {
		imageViewSlider.setOnClickListener(this);	
	}

	private void setListViewAdapter() {
		listviewAdapter = new ListViewItemsAdapter(this, R.layout.listview_items, mergeListItems);
		listviewItems.setAdapter(listviewAdapter);
		// select listview items on multiple click after long press
		listviewItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		// get response on listview choice mode
		listviewItems.setMultiChoiceModeListener(new ModeCallback());

	}

	private void onDrop() {
		scrollLayout.setOnDragListener(new OnDragListener() {

			public boolean onDrag(View v, DragEvent event) {
				switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_ENTERED:
					if(slide_me.isClosed()){
						slide_me.openRightSide();
					}
					Log.e("ACTION_DRAG_ENTERED " , "ACTION_DRAG_ENTERED ");
					v.setBackgroundColor(Color.GRAY);
					break;

				case DragEvent.ACTION_DRAG_EXITED:
					Log.e("ACTION_DRAG_EXITED " , "ACTION_DRAG_EXITED ");
					v.setBackgroundColor(Color.TRANSPARENT);
					break;

				case DragEvent.ACTION_DRAG_STARTED:
					flagCheckFocus = 1;
					Log.e("ACTION_DRAG_STARTED " , "ACTION_DRAG_STARTED ");
					return true;

				case DragEvent.ACTION_DRAG_ENDED:
					Log.e("ACTION_DRAG_ENDED " , "ACTION_DRAG_ENDED " + flag);
					if(flag == 1){
						listviewItems.post(new Runnable() {
							@Override
							public void run() {
								listviewItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
								flag = 0;
								flagCheckFocus = 0;
							}
						});
					}
					break;

				case DragEvent.ACTION_DROP:
					v.setBackgroundColor(Color.TRANSPARENT);

					Log.e("ACTION_DROP " , "ACTION_DROP ");

					listviewItems.post(new Runnable() {
						@Override
						public void run() {
							listviewItems.setChoiceMode(ListView.CHOICE_MODE_NONE);
						}
					});

					flag = 1;

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

		// This layout is for main layout, for top to add the main layout Heading and Sub Folders and than
		// it add into the drop layout.
		LinearLayout layoutProject = new LinearLayout(this);
		layoutProject.setOrientation(LinearLayout.VERTICAL);
		layoutProject.setGravity(Gravity.CENTER_HORIZONTAL);

		// This layout set top layout heading, declearing here due to add in drop Layout. 
		LinearLayout layoutProjectHeading = new LinearLayout(this);
		topLayoutHeadingFolder(layoutProjectHeading);

		// This layout used to set image and the name of the sublayout folder.  
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.setLayoutParams(new LayoutParams(60, LayoutParams.WRAP_CONTENT));
		layout.setPadding( 0 , 2 , 0 , 2 );


		if(arrItem.size()>0){
			for(int i=0 ; i<arrItem.size() ; i++){
				View v = listviewAdapter.getView(arrItem.get(i), null , null);
				if(v!=null){
					String val = listviewItemsName[arrItem.get(i)];

					LinearLayout layoutInside = new LinearLayout(this);
					layoutInside.setOrientation(LinearLayout.VERTICAL);
					layoutInside.setGravity(Gravity.CENTER_HORIZONTAL);
					layoutInside.setLayoutParams(new LayoutParams(60, 60));
					layoutInside.addView(v);

					TextView name = new TextView(this);
					name.setGravity(Gravity.CENTER_HORIZONTAL);
					name.setText(val);

					layout.addView(layoutInside);
					layout.addView(name);

				}
			}


			layoutProject.addView(layoutProjectHeading);
			layoutProject.addView(layout);
			dropLayout.addView(layoutProject);

			bottomLayoutFolder(); // This Method used to add the folder in to the list layout of the projects. 

			arrItem.clear();

			listviewItems.post(new Runnable() {
				@Override
				public void run() {
					listviewItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
				}
			});
		}
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
			if(slide_me.isClosed()){
				slide_me.openRightSide();

			}
			else{
				slide_me.closeRightSide(); 
			}
			break;
		default:
			break;
		}
	}

	private class ModeCallback implements ListView.MultiChoiceModeListener {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			Log.e("onCreateActionMode","onCreateActionMode");
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			Log.e("onPrepareActionMode","onPrepareActionMode");

			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			Log.e("onActionItemClicked","onActionItemClicked");

			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			Log.e("onDestroyActionMode","onDestroyActionMode");

			listviewAdapter.removeSelection();
			inflateBottomBar.setVisibility(View.GONE);
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {

			Log.e("onItemCheckedStateChanged","onItemCheckedStateChanged");
			inflateBottomBar.setVisibility(View.VISIBLE);
			arrItem.add(position);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:

			Log.e("ACTION_MOVE", "ACTION_MOVE");

			if(flagtouchState == 0){
			//	slide_me.openRightSide();
				
				drawerLayout.setVisibility(View.GONE);
				
				dragDropHide.setVisibility(View.GONE);
				dragDropShow.setBackgroundResource(R.drawable.trans_bg);
				

				/*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				dragDropShow.setLayoutParams(lp);
				slide_me.openRightSide();*/
				
			}

			break;
		case MotionEvent.ACTION_DOWN:
			flagtouchState = 0;
			Log.e("ACTION_DOWN", "ACTION_DOWN");
			break;
		case MotionEvent.ACTION_UP:
			//slide_me.openRightSide();
			flagtouchState = 1;
			break;
		default:
			break;
		}
		return false;
	}
}

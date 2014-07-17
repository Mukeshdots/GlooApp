package com.app.glooworkspace;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.navdrawer.SimpleSideDrawer;

public class MainActivity extends Activity implements OnClickListener {

	private ListView listviewItems;
	private String[] listviewItemsName;
	private int[] listviewItemsImage;
	private ArrayList<MergeData> mergeListItems = new ArrayList<MergeData>();
	private ListViewItemsAdapter listviewAdapter;
	private LinearLayout inflateBottomBar;
	private ImageView imageViewSlider;
	public  static SimpleSideDrawer slide_me;
	public  static LinearLayout dropLayout;

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
		inflateBottomBar = (LinearLayout) findViewById(R.id.inflate_BottomBar);
		listviewItems = (ListView) findViewById(R.id.list_items);
		imageViewSlider = (ImageView) findViewById(R.id.imageViewSlider);
		// add sliding layout and inflate layout init and get items id
		slide_me = new SimpleSideDrawer(MainActivity.this);
		slide_me.setRightBehindContentView(R.layout.inflate_dropdown);
		dropLayout = (LinearLayout) findViewById(R.id.drop_layout);
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
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			
			listviewAdapter.removeSelection();
			inflateBottomBar.setVisibility(View.GONE);

		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {

			inflateBottomBar.setVisibility(View.VISIBLE);
		}
	}
}

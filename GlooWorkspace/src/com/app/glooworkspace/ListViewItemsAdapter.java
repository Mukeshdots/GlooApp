package com.app.glooworkspace;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ClipData;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewItemsAdapter extends ArrayAdapter<MergeData>{

	private Activity inflateActivity;
	private ArrayList<MergeData> inflateMergeItems;
	private LayoutInflater inflateLayout;
	private SparseBooleanArray mSelectedItemsIds;

	public ListViewItemsAdapter(Activity activity, int listviewItems, ArrayList<MergeData> mergeListItems) {
		super(activity, listviewItems, mergeListItems);
		inflateActivity = activity;
		inflateMergeItems = mergeListItems;
		inflateLayout = LayoutInflater.from(inflateActivity.getApplicationContext());
		mSelectedItemsIds = new SparseBooleanArray();
	}

	private class ViewHolder {
		TextView itemName;
		ImageView itemImage;
	}

	public View getView(int position, View view, ViewGroup parent) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflateLayout.inflate(R.layout.listview_items, null);
			holder.itemName = (TextView) view.findViewById(R.id.inflate_TextViewName);
			// Locate the ImageView in listview_item.xml
			holder.itemImage = (ImageView) view.findViewById(R.id.inflate_ImageView);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.itemName.setText(inflateMergeItems.get(position).getListViewInflateName());
		holder.itemImage.setImageResource(inflateMergeItems.get(position).getListViewInflateImage());

		// Create a Builder to create shadow for drag and drop items

		final String title = "Drag";
		final String textData = title + ":" + position;
		ClipData data = ClipData.newPlainText(title, textData);
		view.startDrag(data, new MyDragShadowBuilder(view), null, 0);
		
		return view;
	}

	@Override
	public void remove(MergeData object) {
		inflateMergeItems.remove(object);
		notifyDataSetChanged();
	}

	public List<MergeData> getMergeData() {
		return inflateMergeItems;
	}

	public void toggleSelection(int position) {
		selectView(position, !mSelectedItemsIds.get(position));
	}

	public void removeSelection() {
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	public void selectView(int position, boolean value) {
		if (value)
			mSelectedItemsIds.put(position, value);
		else
			mSelectedItemsIds.delete(position);
		notifyDataSetChanged();
	}

	public int getSelectedCount() {
		return mSelectedItemsIds.size();
	}

	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}
}

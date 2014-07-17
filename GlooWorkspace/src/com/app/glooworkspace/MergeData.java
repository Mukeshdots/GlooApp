package com.app.glooworkspace;

public class MergeData {
	private int listViewInflateImage;
	private String listViewInflateName;

	public MergeData(int inflateImage , String inflateName) {
		listViewInflateImage = inflateImage;
		listViewInflateName = inflateName;
	}

	public int getListViewInflateImage() {
		return listViewInflateImage;
	}

	public void setListViewInflateImage(int listViewInflateImage) {
		this.listViewInflateImage = listViewInflateImage;
	}

	public String getListViewInflateName() {
		return listViewInflateName;
	}

	public void setListViewInflateName(String listViewInflateName) {
		this.listViewInflateName = listViewInflateName;
	}


}

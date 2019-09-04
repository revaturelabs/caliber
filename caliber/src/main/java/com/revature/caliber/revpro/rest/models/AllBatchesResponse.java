package com.revature.caliber.revpro.rest.models;

import java.util.List;

import com.revature.caliber.revpro.beans.RevProBatch;

public class AllBatchesResponse extends BasicHTTPResponseWrapper{

	private List<RevProBatch> data;

	public AllBatchesResponse() {
		super();
	}

	public List<RevProBatch> getData() {
		return data;
	}

	public void setData(List<RevProBatch> data) {
		this.data = data;
	}
	
}

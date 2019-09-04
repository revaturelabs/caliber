package com.revature.caliber.revpro.rest.models;

import com.revature.caliber.revpro.beans.RevProBatch;

public class BatchAssociatesResponse {

	private RevProBatch data;

	public BatchAssociatesResponse() {
		super();
	}

	public RevProBatch getData() {
		return data;
	}

	public void setData(RevProBatch data) {
		this.data = data;
	}
	
}

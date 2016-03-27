package com.maranhon.common;

import java.io.Serializable;

public class ErrorMessage implements Serializable{

	private static final long serialVersionUID = 316_000000_3L;
	private ErrorType type;
	
	public enum ErrorType{
		InvalidRequest,
		NoProductInStock,
		ClientDoesNotExist,
		ProductDoesNotExist,
		ConnectionError,
		NoServerOnline
	}
	
	public ErrorMessage(ErrorType type){
		this.type = type;
	}
	
	public ErrorType getType(){
		return type;
	}
	
}

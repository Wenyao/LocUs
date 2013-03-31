package com.example.locus.entity;

public class Result {
	public static Result Success = new Result(true, ErrorCodes.None);
	
	private boolean isSuccessful;
	private ErrorCodes errorCode;
	
	public Result(boolean isSuccessful, ErrorCodes errorCode) {
		this.isSuccessful = isSuccessful;
		this.errorCode = errorCode;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public ErrorCodes getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodes errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "Result [isSuccessful=" + isSuccessful + ", errorCode="
				+ errorCode + "]";
	}
}

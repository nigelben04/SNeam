package main.com.model;

public class TransactionHeader {

	protected String transactionId;
	protected String userId;
	
	public TransactionHeader(String transactionId, String userId) {
		super();
		this.transactionId = transactionId;
		this.userId = userId;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}

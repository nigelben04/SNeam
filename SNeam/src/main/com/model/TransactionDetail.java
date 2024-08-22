package main.com.model;

public class TransactionDetail {

	protected String transactionId;
	protected String gameId;
	protected Integer quantity;
	
	public TransactionDetail(String transactionId, String gameId, Integer quantity) {
		super();
		this.transactionId = transactionId;
		this.gameId = gameId;
		this.quantity = quantity;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
	
}

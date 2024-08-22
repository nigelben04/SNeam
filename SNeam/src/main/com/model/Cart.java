package main.com.model;

public class Cart {

	protected String userId;
	protected String gameId;
	protected Integer quantity;
	
	public Cart(String userId, String gameId, Integer quantity) {
		super();
		this.userId = userId;
		this.gameId = gameId;
		this.quantity = quantity;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

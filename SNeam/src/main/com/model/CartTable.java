package main.com.model;

public class CartTable {

	protected String name;
	protected Integer price;
	protected Integer quantity;
	protected Integer total;
	
	public CartTable(String name, Integer price, Integer quantity, Integer total) {
		super();
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.total = total;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
	
}

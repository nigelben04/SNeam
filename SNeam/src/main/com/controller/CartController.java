package main.com.controller;

import java.sql.ResultSet;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.com.model.CartTable;
import main.com.model.TransactionHeader;
import main.com.model.User;
import main.com.util.Connect;
import main.com.view.CartPage;
import main.com.view.HomePage;
import main.com.view.LoginPage;

public class CartController {

	private final CartPage cartPage;
	HomePage homePage;
	Connect connect;
	ObservableList<CartTable> cartTable = FXCollections.observableArrayList();
	Vector<TransactionHeader> transactionHeaderData = new Vector<>();

	public CartController(CartPage cartPage) {
		this.cartPage = cartPage;
		
		eventHandlers();
	}
	
	private void eventHandlers() {
		cartPage.getToLogOutMenuItem().setOnAction(event -> {
			switchToLoginPage();
		});
		
		cartPage.getToDashboardMenuItem().setOnAction(event -> {
			switchToDashboard();
		});
		
		cartPage.getCheckOutButton().setOnAction(event -> {
			if(cartPage.isTableEmpty()) {
				showAlert("Invalid Request", "Cart is Empty", "Transaction not possible");
			}else {
				createTransactionHeader(cartPage.getUserData());
				getTableData(cartPage.getUserData());
				cartPage.getGrossPriceLabel().setText("Cart is Empty");	
			}
		});
		
	}
	
	private void switchToLoginPage() {
    	LoginPage loginPage = new LoginPage();
    	cartPage.getScene().setRoot(loginPage);
    }
	
	private void switchToDashboard() {
		homePage = new HomePage(cartPage.getUserData());
		cartPage.getScene().setRoot(homePage);
	}
	
	private void createTransactionHeader(User userData) {
		connect = new Connect();
		getTransactionHeaderData();
		String id = String.format("TR%03d", transactionHeaderData.size() + 1);
		
		try {
			connect.runUpdate(String.format("INSERT INTO TransactionHeader VALUES ('%s', '%s')",
					id, userData.getId()));
			
			for(CartTable cartItem : cartTable) {
				String gameId = getGameID(cartItem.getName());
				createTransactionDetail(id, gameId, cartItem.getQuantity());
			}
			deleteCart(userData);
			
			connect.close();
		} catch (Exception e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void createTransactionDetail(String transactionId, String gameId, Integer quantity) {
		connect = new Connect();
		
		try {
			connect.runUpdate(String.format("INSERT INTO TransactionDetail VALUES ('%s', '%s', '%s')",
					transactionId, gameId, quantity));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String getGameID(String gameName) {
		connect = new Connect();
	    try {
	        ResultSet result = connect.runQuery(String.format("SELECT GameID FROM Game WHERE GameName LIKE '%s'", gameName));
	        if (result.next()) {
	            return result.getString("GameID");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	private void deleteCart(User userData) {
		connect = new Connect();
		
		try {
			connect.runUpdate(String.format("DELETE FROM Cart WHERE UserID LIKE '%s'", 
					userData.getId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getTransactionHeaderData() {
		connect = new Connect();
		transactionHeaderData.clear();
		
		try {
			ResultSet res = connect.runQuery("SELECT * FROM TransactionHeader");
			while(res.next()) {
				String transactionId = res.getString("TransactionID");
				String userId = res.getString("UserID");
				
				transactionHeaderData.add(new TransactionHeader(transactionId, userId));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ObservableList<CartTable> getTableData(User userData) {
		connect = new Connect();
		
	    try {
	        ResultSet res = connect.runQuery(String.format("SELECT * FROM Cart WHERE UserID LIKE '%s'", 
	        		userData.getId()));
	        cartTable.clear();
	        
	        while (res.next()) {
	            String gameID = res.getString("GameID");
	            String name = getGameName(gameID);
	            Integer price = getGamePrice(gameID);
	            Integer quantity = res.getInt("Quantity");
	            Integer total = price * quantity;

	            CartTable cartTableData = new CartTable(name, price, quantity, total);
	            cartTable.add(cartTableData);
	        }
	        connect.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return cartTable;
	}

	private String getGameName(String gameID) {
		connect = new Connect();
	    try {
	        ResultSet result = connect.runQuery(String.format("SELECT GameName FROM Game WHERE GameID LIKE '%s'", gameID));
	        if (result.next()) {
	            return result.getString("GameName");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return "";
	}

	private Integer getGamePrice(String gameID) {
		connect = new Connect();
	    try {
	        ResultSet result = connect.runQuery(String.format("SELECT Price FROM Game WHERE GameID LIKE '%s'", gameID));
	        if (result.next()) {
	            return result.getInt("Price");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return 0;
	}
	
	public Integer getGrossPriceValue(User userData) {
		connect = new Connect();
		int grossPrice = 0;
		
	    try {
	        ResultSet res = connect.runQuery(String.format("SELECT * FROM Cart WHERE UserID LIKE '%s'", 
	        		userData.getId()));

	        while (res.next()) {
	            String gameID = res.getString("GameID");
	            Integer price = getGamePrice(gameID);
	            Integer quantity = res.getInt("Quantity");
	            Integer total = price * quantity;

	            grossPrice = grossPrice + total;
	        }
	        connect.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return grossPrice;
	}
	
	private void showAlert(String title, String header,String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
	
}

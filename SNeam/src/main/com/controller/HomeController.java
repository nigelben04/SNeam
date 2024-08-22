package main.com.controller;

import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.com.model.Cart;
import main.com.model.Game;
import main.com.model.User;
import main.com.util.Connect;
import main.com.view.CartPage;
import main.com.view.HomePage;
import main.com.view.LoginPage;

public class HomeController {

	private final HomePage homePage;
	Connect connect;
	ObservableList<Game> games = FXCollections.observableArrayList();

	public HomeController(HomePage homePage) {
		this.homePage = homePage;
		
		eventHandlers();
	}
	
	private void eventHandlers() {
		homePage.getToLogOutMenuItem().setOnAction(event -> {
			switchToLoginPage();
		});
		
		//customer
		homePage.getToCartMenuItem().setOnAction(event -> {
			switchToCartPage(homePage.getUserData());
		});
		
		homePage.getAddToCartPopUpButton().setOnAction(event -> {
			configureCart();
		});
		
		//admin
		homePage.getAddGameButton().setOnAction(event -> {
			if(homePage.validateAddForm()) {
				addGame();
				homePage.clearForm();
				homePage.refreshGameList();
			}
		});
		
		homePage.getUpdateGameButton().setOnAction(event -> {
			if(homePage.validateUpdateForm()) {
				updateGame();
				homePage.clearForm();
				homePage.refreshGameList();
			}
		});
		
		homePage.getDeleteGameButton().setOnAction(event -> {
			if(homePage.validateUpdateForm()) {
				if(homePage.getDeleteConfirmation()) {
					deleteGame();
				}
				homePage.clearForm();
				homePage.refreshGameList();
			}
		});
	}
	
	private void switchToLoginPage() {
    	LoginPage loginPage = new LoginPage();
        homePage.getScene().setRoot(loginPage);
    }
	
	//customer
	private void switchToCartPage(User userData) {
		CartPage cartPage = new CartPage(userData);
		homePage.getScene().setRoot(cartPage);
	}
	
	public Integer getPreviousValue() {
		Cart cartData = homePage.getCartData();
		connect = new Connect();
		
		try {
			ResultSet res = connect.runQuery(String.format("SELECT * FROM Cart WHERE UserID LIKE '%s' AND GameID LIKE '%s'", 
					cartData.getUserId(), cartData.getGameId()));
			
			if(res.next()) {
				return res.getInt("Quantity");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	private void configureCart() {
		Cart cartData = homePage.getCartData();
		connect = new Connect();
		
		try {
			ResultSet res = connect.runQuery(String.format("SELECT * FROM Cart WHERE UserID LIKE '%s' AND GameID LIKE '%s'", 
					cartData.getUserId(), cartData.getGameId()));
			
			boolean isCartEmpty = !res.next();
	        int quantity = cartData.getQuantity();

	        if (isCartEmpty && quantity == 0) {
	            homePage.getPopUpStage().close();
	        } else if (!isCartEmpty && quantity == 0) {
	            deleteCart(cartData);
	        } else if (!isCartEmpty && quantity > 0) {
	            updateCart(cartData);
	        } else if (isCartEmpty && quantity > 0) {
	            addCart(cartData);
	        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deleteCart(Cart cartData) {
		try {
			connect.runUpdate(String.format("DELETE FROM Cart WHERE UserID = '%s' AND GameID = '%s'", 
					cartData.getUserId(), cartData.getGameId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showAlert("Cart Deleted", "Cart Successfully Deleted!", "Cart quantity has been deleted.");
	}
	
	private void updateCart(Cart cartData) {
		try {
			connect.runUpdate(String.format("UPDATE Cart SET Quantity = %d WHERE UserID = '%s' AND GameID = '%s'", 
			        cartData.getQuantity(), cartData.getUserId(), cartData.getGameId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showAlert("Cart Updated", "Cart Successfully Updated!", "Cart quantity has been updated.");
	}
	
	private void addCart(Cart cartData) {
		try {
			connect.runUpdate(String.format("INSERT INTO Cart VALUES('%s', '%s', %d)", 
					cartData.getUserId(), cartData.getGameId(), cartData.getQuantity()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showAlert("Cart Added", "Cart Successfully Added!", "Cart quantity has been added.");
	}
	
	//admin
	private void addGame() {
		getGameData();
		Game newGame = homePage.getFormValues();
		newGame.setId(String.format("GA%03d", games.size() + 1));
		connect = new Connect();
		
		try {
			connect.runUpdate(String.format("INSERT INTO Game VALUES ('%s', '%s', '%s', '%d')", 
					newGame.getId(), newGame.getName(), newGame.getDescription(), newGame.getPrice()));
			connect.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateGame() {
		Game updateGame = homePage.getFormValues();
		updateGame.setId(homePage.getSelectedGameId());
		getGameData();
		connect = new Connect();
		
		try {
	        StringBuilder updateQuery = new StringBuilder("UPDATE Game SET ");
	        boolean isFirstField = true;

	        if (!updateGame.getName().isEmpty()) {
	            updateQuery.append(String.format("GameName = '%s'", updateGame.getName()));
	            isFirstField = false;
	        }

	        if (!updateGame.getDescription().isEmpty()) {
	            if (!isFirstField) {
	                updateQuery.append(", ");
	            }
	            updateQuery.append(String.format("GameDescription = '%s'", updateGame.getDescription()));
	            isFirstField = false;
	        }

	        if (updateGame.getPrice() != null) {
	            if (!isFirstField) {
	                updateQuery.append(", ");
	            }
	            updateQuery.append(String.format("Price = '%d'", updateGame.getPrice()));
	        }

	        updateQuery.append(String.format(" WHERE GameID = '%s'", updateGame.getId()));

	        connect.runUpdate(updateQuery.toString());
	        connect.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void deleteGame() {
		String id = homePage.getSelectedGameId();
		connect = new Connect();
		
		try {
			connect.runUpdate(String.format("DELETE FROM Game WHERE GameID = '%s'" , id));
			getGameData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ObservableList<Game> getGameData() {
        try {
        	connect = new Connect();
            games.clear();
            ResultSet res = connect.runQuery("SELECT * FROM Game");
            while (res.next()) {
                String id = res.getString("GameID");
                String name = res.getString("GameName");
                String desc = res.getString("GameDescription");
                Integer price = res.getInt("Price");

                Game game = new Game(id, name, desc, price);
                games.add(game);
            }
            
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return games;
    }
	
	private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setOnCloseRequest(e -> {
        	homePage.getPopUpStage().close();
        });
        alert.showAndWait();
    }
	
}

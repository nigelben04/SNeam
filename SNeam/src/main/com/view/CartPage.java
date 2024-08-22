package main.com.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.com.controller.CartController;
import main.com.model.CartTable;
import main.com.model.User;

public class CartPage extends BorderPane{

	private final CartController cartController;
	private User userData;
	
	private final Label cartHeaderLabel = new Label("Your Cart");
	private Label grossPriceLabel = new Label("");
	private final Button checkOutButton = new Button("Check Out");
	
	private TableView<CartTable> table = new TableView<>();
	private TableColumn<CartTable, String> nameColumn;
	private TableColumn<CartTable, Integer> priceColumn, quantityColumn, totalColumn;
	
	private MenuBar menuBar = new MenuBar();
    private Menu dashboardMenu = new Menu("DashBoard"), logOutmenu = new Menu("Log Out");
    private MenuItem toDashboard = new MenuItem("Dashboard"), toCart = new MenuItem("Cart"), toLogOut = new MenuItem("Log Out");
	
	public CartPage(User userData) {
    	this.cartController = new CartController(this);
    	this.userData = userData;
    	
        this.setTop(createMenuBar());
        this.setCenter(createCenter());
    }
	
	private MenuBar createMenuBar() {
        dashboardMenu.getItems().addAll(toDashboard, toCart);
        logOutmenu.getItems().addAll(toLogOut);
        menuBar.getMenus().addAll(dashboardMenu, logOutmenu);
  
        return menuBar;
    }
	
	private VBox createCenter() {
		VBox vBox = new VBox();
		createTable();
		
		cartHeaderLabel.setFont(Font.font("Arial", FontWeight.BLACK, 25));
		if(cartController.getGrossPriceValue(userData) == 0) {
			grossPriceLabel.setText("Cart is Empty");
		}else {
			grossPriceLabel.setText("Gross Price: Rp" + cartController.getGrossPriceValue(userData).toString());
		}
		grossPriceLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 25));
		checkOutButton.setMinSize(150, 40);
		checkOutButton.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		
		vBox.getChildren().addAll(cartHeaderLabel, table, grossPriceLabel, checkOutButton);
		vBox.setSpacing(15);
		vBox.setAlignment(Pos.CENTER);
		
		return vBox;
	}
	
	private void createTable() {
		nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<CartTable, String>("name"));
		nameColumn.setMinWidth(250);
		nameColumn.setMaxWidth(250);
		
		priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<CartTable, Integer>("price"));
		priceColumn.setMinWidth(70);
		priceColumn.setMaxWidth(70);
		
		quantityColumn = new TableColumn<>("Quantity");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<CartTable, Integer>("quantity"));
		quantityColumn.setMinWidth(70);
		quantityColumn.setMaxWidth(70);
		
		totalColumn = new TableColumn<>("Total");
		totalColumn.setCellValueFactory(new PropertyValueFactory<CartTable, Integer>("total"));
		totalColumn.setMinWidth(70);
		totalColumn.setMaxWidth(70);
		
		table.setMinWidth(nameColumn.getWidth() + priceColumn.getWidth() + quantityColumn.getWidth() + totalColumn.getWidth() + 3);
		table.setMaxWidth(nameColumn.getWidth() + priceColumn.getWidth() + quantityColumn.getWidth() + totalColumn.getWidth() + 3);
		table.getColumns().addAll(nameColumn, priceColumn, quantityColumn, totalColumn);
		
		table.getItems().clear();
		table.setItems(cartController.getTableData(userData));
	}
	
	
	public User getUserData() {
		return userData;
	}
	
	public boolean isTableEmpty() {
		if(cartController.getTableData(userData).isEmpty()) return true;
		return false;
	}
	
	public Label getGrossPriceLabel() {
		return grossPriceLabel;
	}
	
	public Button getCheckOutButton() {	
		return checkOutButton;
	}
	
	public MenuItem getToDashboardMenuItem() {
        return toDashboard;
    }
    
    public MenuItem getToLogOutMenuItem() {
    	return toLogOut;
    }
	
}

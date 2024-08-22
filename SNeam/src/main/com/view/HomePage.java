package main.com.view;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import main.com.controller.HomeController;
import main.com.model.Cart;
import main.com.model.Game;
import main.com.model.User;

public class HomePage extends BorderPane {

	private final HomeController homeController;
	private User userData;
	
	//Customer
    private Label headerLabel = new Label();
    private Label gameNameLabel = new Label("");
    private Label gameDescriptionLabel = new Label("");
    private Label gamePriceLabel = new Label("");
    private final Button addToCartButton = new Button("Add To Cart");
    
    private Stage popUpStage = new Stage();
    private BorderPane popUpBP = new BorderPane();
    private VBox popUpVBox = new VBox();
    private Scene popUpScene = new Scene(popUpBP, 350, 400);
    private Window popUpWindow = new Window("Add To Cart");
    
    //PopUp
    private Label popUpGameName = new Label();
    private Label popUpGameDescription = new Label();
    private Label popUpGamePrice = new Label();
    private Spinner<Integer> quantitySpinner = new Spinner<>();
    private SpinnerValueFactory<Integer> quantityFactory;
    private Button popUpAddToCartButton = new Button("Add To Cart");
    
    //Admin
    private final Label adminHeaderLabel = new Label("Admin Menu");
    private final Label addNameLabel = new Label("Game Title");
    private final Label addDescriptionLabel = new Label("Game Description");
    private final Label addPriceLabel = new Label("Price");
    private TextField addNameField = new TextField();
    private TextArea addDescriptionField = new TextArea();
    private TextField addPriceField = new TextField();
    private final Button addGameButton = new Button("Add Game");
    private final Button updateGameButton = new Button("Update Game");
    private final Button deleteGameButton = new Button("Delete Game");
	private Alert alert = null;
    
    private MenuBar menuBar = new MenuBar();
    private Menu dashboardMenu = new Menu("DashBoard"), logOutmenu = new Menu("Log Out");
    private MenuItem toDashboard = new MenuItem("Dashboard"), toCart = new MenuItem("Cart"), toLogOut = new MenuItem("Log Out");

    //ListView
    private ListView<Game> listGames = new ListView<>();
    Game selectedGame = null;
    
    public HomePage(User userData) {
    	this.homeController = new HomeController(this);
    	this.userData = userData;
    	
        this.setTop(createMenuBar(userData.getRole()));
        this.setCenter(createCenter(userData.getRole(), userData.getName()));
    }

    private MenuBar createMenuBar(String userRole) {
        
        if ("admin".equalsIgnoreCase(userRole)) {
            logOutmenu.getItems().addAll(toLogOut);
            menuBar.getMenus().add(logOutmenu);
        } else if ("customer".equalsIgnoreCase(userRole)) {
            dashboardMenu.getItems().addAll(toDashboard, toCart);
            logOutmenu.getItems().addAll(toLogOut);
            menuBar.getMenus().addAll(dashboardMenu, logOutmenu);
        }
        
        return menuBar;
    }

    
    private VBox createCenter(String userRole, String userName) {
        VBox vBox = new VBox();
        
        if ("admin".equalsIgnoreCase(userRole)) {
            vBox.getChildren().add(createAdminCenter(userRole));
        } else if ("customer".equalsIgnoreCase(userRole)) {
            vBox.getChildren().add(createCustomerCenter(userName));
        }

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        return vBox;
    }
    
    private void createGameList(String userRole) {
    	ObservableList<Game> gameData = homeController.getGameData();
    	
    	listGames.setItems(gameData);
    	
    	listGames.setCellFactory(param -> new javafx.scene.control.ListCell<Game>() {
            @Override
            protected void updateItem(Game item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    	
    	addToCartButton.setVisible(false);
    	
    	listGames.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        gameNameLabel.setText(newValue.getName());
                        gameDescriptionLabel.setText(newValue.getDescription());
                        gamePriceLabel.setText("Price: Rp " + newValue.getPrice());

                        selectedGame = newValue;

                        addNameField.setText(newValue.getName());
                        addDescriptionField.setText(newValue.getDescription());
                        addPriceField.setText(newValue.getPrice().toString());
                        
                        popUpGameName.setText(newValue.getName());
                        popUpGameDescription.setText(newValue.getDescription());
                        popUpGamePrice.setText("Rp " + newValue.getPrice().toString());
                        
                        if ("customer".equalsIgnoreCase(userRole)) {
                            addToCartButton.setVisible(true);
                            createAddToCartPopUp();
                        }
                        
                    } else {
                        addToCartButton.setVisible(false);
                    }
                });
    }
    
    private VBox createCustomerCenter(String userName) {
    	//Header Label for normal user
    	headerLabel.setText(String.format("Hello, %s", userName));
    	createGameList("Customer");
    	
        // Set Layout
        GridPane GP = new GridPane();
        GP.add(gameNameLabel, 0, 0);
        GP.add(gameDescriptionLabel, 0, 1);
        GP.add(gamePriceLabel, 0, 2);
        GP.add(addToCartButton, 0, 3);
        GP.setAlignment(Pos.CENTER);
        GP.setVgap(5);
        GridPane.setHalignment(gamePriceLabel, HPos.CENTER);
        GridPane.setHalignment(addToCartButton, HPos.CENTER);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(listGames, GP);
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.setSpacing(10);
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(headerLabel, hBox);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(15);
        VBox.setMargin(vBox, new Insets(0, 0, 300, 0));
        
        // Set Style
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        gameNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gameNameLabel.setWrapText(true);
        gameNameLabel.setMaxSize(200, 100);
        gameDescriptionLabel.setWrapText(true);
        gameDescriptionLabel.setMinSize(260,130);
        gameDescriptionLabel.setMaxSize(260,130);
        addToCartButton.setMinSize(200, 40);
        listGames.setMinSize(250, 300);
        listGames.setMaxSize(250, 300);
        
        return vBox;
    }
    
    private void createAddToCartPopUp() {
    	popUpStage.setScene(popUpScene);
    	popUpStage.setResizable(false);
        popUpVBox.getChildren().clear();
        popUpWindow.getContentPane().getChildren().clear();
        popUpWindow.getContentPane().setStyle("-fx-background-color: white;");
        
        Integer quantity = homeController.getPreviousValue();
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, quantity));
        
        popUpVBox.getChildren().add(popUpGameName);
        popUpVBox.getChildren().add(popUpGameDescription);
        popUpVBox.getChildren().add(popUpGamePrice);
        popUpVBox.getChildren().add(quantitySpinner);
        popUpVBox.getChildren().add(popUpAddToCartButton);
        popUpVBox.setAlignment(Pos.CENTER);
        popUpVBox.setSpacing(15);
        popUpVBox.setPadding(new Insets(15));

        //Set Style
        popUpGameName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        popUpGameDescription.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        popUpGameDescription.setMinHeight(120);
        popUpGameDescription.setMaxHeight(120);
        popUpGameDescription.setAlignment(Pos.TOP_LEFT);
        popUpGameDescription.setWrapText(true);
        
        popUpBP.setCenter(popUpWindow);
        popUpWindow.getContentPane().getChildren().add(popUpVBox);

        // Event handlers
        popUpWindow.setOnCloseAction(event -> {
            popUpStage.close();
        });

        addToCartButton.setOnAction(event -> {
            popUpStage.show();
        });
    }
    
    private ScrollPane createAdminCenter(String userName) {
    	headerLabel.setText(String.format("Hello, %s", userName));
    	createGameList("Admin");
    	
    	GridPane GP = new GridPane();
        GP.add(gameNameLabel, 0, 0);
        GP.add(gameDescriptionLabel, 0, 1);
        GP.add(gamePriceLabel, 0, 2);
        GP.add(addToCartButton, 0, 3);
        GP.setAlignment(Pos.CENTER);
        GP.setVgap(5);
        GridPane.setHalignment(gamePriceLabel, HPos.CENTER);
        GridPane.setHalignment(addToCartButton, HPos.CENTER);
        
        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(listGames, GP);
        hBox1.setAlignment(Pos.TOP_CENTER);
        hBox1.setSpacing(10);
        
        VBox formVBox = new VBox();
        formVBox.getChildren().addAll(addNameLabel, addNameField, addDescriptionLabel, addDescriptionField, addPriceLabel, addPriceField);
        formVBox.setAlignment(Pos.CENTER_LEFT);
        formVBox.setSpacing(5);
        
        VBox formButtonVBox = new VBox();
        formButtonVBox.getChildren().addAll(addGameButton, updateGameButton, deleteGameButton);
        formButtonVBox.setAlignment(Pos.CENTER);
        formButtonVBox.setSpacing(30);
        
        HBox hBox2 = new HBox();
        hBox2.getChildren().addAll(formVBox ,formButtonVBox);
        hBox2.setAlignment(Pos.CENTER);
        hBox2.setSpacing(10);
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(headerLabel, hBox1, adminHeaderLabel, hBox2);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        vBox.setPadding(new Insets(0, 0, 50, 0));
        
        // Set Styles
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        gameNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gameNameLabel.setWrapText(true);
        gameNameLabel.setMaxSize(200, 100);
        gameDescriptionLabel.setWrapText(true);
        gameDescriptionLabel.setMinSize(260,130);
        gameDescriptionLabel.setMaxSize(260,130);
        addToCartButton.setMinSize(200, 40);
        listGames.setMinSize(250, 300);
        listGames.setMaxSize(250, 300);
        
        adminHeaderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        addNameField.setMinWidth(350);
        addDescriptionField.setMinWidth(350);
        addDescriptionField.setWrapText(true);
        addPriceField.setMinWidth(350);
        addGameButton.setMinSize(100, 50);
        updateGameButton.setMinSize(100, 50);
        deleteGameButton.setMinSize(100, 50);
        
        //Create ScrollPane
        ScrollPane sp = new ScrollPane();
        sp.setContent(vBox);
        sp.setFitToWidth(true);
        
        return sp;
    }
    
    public void refreshGameList() {
        listGames.getItems().clear();
        createGameList("Admin");
    }
    
    public boolean validateForm(String name, String desc, String priceStr) {
    	if(name.isEmpty()) {
    		showAlert("Warning", "Invalid Input", "You haven't inputted a game title", "Please fill the game title form");
    		return false;
    	}
    	
    	if(desc.isEmpty()) {
    		showAlert("Warning", "Invalid Input", "You haven't inputted a game description", "Please fill the game description form");
    		return false;
    	}
    	
    	if(priceStr.isEmpty()) {
    		showAlert("Warning", "Invalid Input", "You haven't inputted a game price", "Please fill the game price form");
    		return false;
    	}
    	
    	if(desc.length() > 250) {
    		showAlert("Warning", "Invalid Input", "Game description cannot exceed  250 characters", "Please fill the game description form again");
            return false;
        }

    	if(name.length() > 50) {
    		showAlert("Warning", "Invalid Input", "Game title cannot exceed 50 characters", "Please fill the game description form again");
            return false;
        }
    	
    	if (priceStr.length() >= 10) {
        	showAlert("Warning", "Invalid Input", "Game price cannot exceed 10 characters", "Please fill the game price form again");
            return false;
        }
    	
    	if (!priceStr.matches("\\d+")) {
    		showAlert("Warning", "Invalid Input", "Game price must be numeric", "Please fill the game price form again");
            return false;
        }
    	
    	return true;
    }
    
    public boolean validateAddForm() {
    	String name = addNameField.getText();
    	String desc = addDescriptionField.getText();
    	String priceStr = addPriceField.getText();

    	if(!validateForm(name, desc, priceStr)) return false;
    	
    	// true -> pass
    	return true;
    }
    
    public boolean validateUpdateForm() {
    	if(listGames.getSelectionModel().getSelectedItem() == null) {
	        showAlert("Warning", "Invalid Input", "No Game Selected", "Please select a game to update.");
	        return false;
	        }
    	
    	if(addNameField.getText().isEmpty() && addDescriptionField.getText().isEmpty() && addPriceField.getText().isEmpty()) {
	        showAlert("Warning", "Invalid Input", "Empty Fields", "Please fill at least one field to update.");
	        return false;
	        }
    	
    	boolean isNameEmpty = addNameField.getText().isEmpty();
        boolean isDescEmpty = addDescriptionField.getText().isEmpty();
        boolean isPriceEmpty = addPriceField.getText().isEmpty();

    	
    	if(!isNameEmpty && addNameField.getText().length() > 50) {
            showAlert("Warning", "Invalid Input", "Game title cannot exceed 50 characters", "Please fill the game description form again");
            return false;
        }

        if(!isDescEmpty && addDescriptionField.getText().length() > 250) {
            showAlert("Warning", "Invalid Input", "Game description cannot exceed 250 characters", "Please fill the game description form again");
            return false;
        }

        if(!isPriceEmpty) {
            String priceStr = addPriceField.getText();

            if(priceStr.length() >= 10) {
                showAlert("Warning", "Invalid Input", "Game price cannot exceed 10 characters", "Please fill the game price form again");
                return false;
            }

            if(!priceStr.matches("\\d+")) {
                showAlert("Warning", "Invalid Input", "Game price must be numeric", "Please fill the game price form again");
                return false;
            }
        }
    	
    	// true -> pass
    	return true;
    }
    
    public boolean validateDeleteForm() {
    	deleteGameButton.setOnAction(event -> {
            if (listGames.getSelectionModel().getSelectedItem() == null) {
                showAlert("Warning", "Invalid Input", "No Game Selected", "Please select a game to update.");
            }
        });
    	return true;
    }
    
    public boolean getDeleteConfirmation() {
    	 alert = new Alert(AlertType.CONFIRMATION);
         alert.setTitle("Delete Game");
         alert.setHeaderText("Are you sure to delete this game?");
         alert.showAndWait();
    	
    	if (alert.getResult() == ButtonType.OK) {
            return true;
        }
    	
    	return false;
    }
    
    public Game getFormValues() {
    	String name = addNameField.getText();
    	String desc = addDescriptionField.getText();
    	String priceStr = addPriceField.getText();
    	
    	
    	if(name.isEmpty()) name = "";
    	if(desc.isEmpty()) desc = "";
    	if(priceStr.isEmpty()) priceStr = selectedGame.getPrice().toString();
    	
    	int price = Integer.parseInt(priceStr);
    	
    	Game newGame = new Game("", name, desc, price);
    	return newGame;
    }
    
    private void showAlert(String type, String title, String header,String content) {
    	if(type.equalsIgnoreCase("Warning")) {
    		alert = new Alert(AlertType.WARNING);
    	}else if(type.equalsIgnoreCase("Confirmation")) {
    		alert = new Alert(AlertType.CONFIRMATION);
    	}
    	
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void clearForm() {
    	addNameField.clear();
    	addDescriptionField.clear();
    	addPriceField.clear();
    }
    
    public String getSelectedGameId() {
    	return listGames.getSelectionModel().getSelectedItem().getId();
    }
    
    public User getUserData() {
    	return userData;
    }
    
    public MenuItem getToLogOutMenuItem() {
    	return toLogOut;
    }
    
    public Button getAddGameButton() {
    	return addGameButton;
    }
    
    public Button getUpdateGameButton() {
    	return updateGameButton;
    }
    
    public Button getDeleteGameButton() {
    	return deleteGameButton;
    }
    
    public Button getAddToCartPopUpButton() {
    	return popUpAddToCartButton;
    }
    
    public Cart getCartData() {
    	return new Cart(userData.getId(), getSelectedGameId(), quantitySpinner.getValue());
    }
    
    public Stage getPopUpStage() {
    	return popUpStage;
    }
    
    public MenuItem getToCartMenuItem() {
        return toCart;
    }
    
    
}

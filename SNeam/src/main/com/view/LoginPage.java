package main.com.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.com.controller.LoginController;

public class LoginPage extends BorderPane {

	private LoginController loginController;
	
    private final Label loginLabel = new Label("LOGIN");
    private final Label emailLabel = new Label("Email");
    private final Label passwordLabel = new Label("Password");
    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button signInButton = new Button("Sign In");

    private MenuBar menuBar = new MenuBar();
    private Menu menu = new Menu("Menu");
    private MenuItem toLogin = new MenuItem("Login"), toRegister = new MenuItem("Register");

    public LoginPage() {
    	this.loginController = new LoginController(this);
    	
        this.setTop(createMenuBar());
        this.setCenter(createCenter());      
    }

    private MenuBar createMenuBar() {
        menu.getItems().addAll(toLogin, toRegister);
        menuBar.getMenus().addAll(menu);
        return menuBar;
    }

    private VBox createCenter() {
        // Set Layout
        GridPane GP = new GridPane();
        GP.add(emailLabel, 0, 0);
        GP.add(emailField, 0, 1);
        GP.add(passwordLabel, 0, 2);
        GP.add(passwordField, 0, 3);
        GP.setAlignment(Pos.CENTER);
        GP.setVgap(5);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(loginLabel, GP, signInButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        // Set Style
        loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        emailField.setMinWidth(250);
        passwordField.setMinWidth(250);
        signInButton.setMinSize(80, 35);

        return vBox;
    }
    
    public String getEmailValue() {
    	return emailField.getText();
    }
    public String getPasswordValue() {
    	return passwordField.getText();
    }

    public Button getSignInButton() {
        return signInButton;
    }

    public MenuItem getToRegisterMenuItem() {
        return toRegister;
    }
}

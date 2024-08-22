package main.com.view;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import main.com.controller.RegisterController;
import main.com.model.User;

public class RegisterPage extends BorderPane {
	
	private RegisterController registerController;
	
	private final Label registerLabel = new Label("REGISTER");
	private final Label userNameLabel = new Label("Username");
	private final Label emailLabel = new Label("Email");
	private final Label passwordLabel = new Label("Password");
	private final Label confirmPasswordLabel = new Label("Confirm Password");
	private final Label phoneNumberLabel = new Label("Phone Numberr");
	private final TextField userNameField = new TextField();
	private final TextField emailField = new TextField();
	private final PasswordField passwordField = new PasswordField();
	private final PasswordField confirmPasswordField = new PasswordField();
	private final TextField phoneNumberField = new TextField();
	private final Button signUpButton = new Button("Sign Up");
	
	private MenuBar	menuBar = new MenuBar();
	private Menu menu = new Menu("Menu");
	private MenuItem toLogin = new MenuItem("Login"), toRegister = new MenuItem("Register");
	
	public RegisterPage() {	
		this.registerController = new RegisterController(this);
		
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
		GP.add(userNameLabel, 0, 0);
		GP.add(userNameField, 0, 1);
		GP.add(emailLabel, 0, 2);
		GP.add(emailField, 0, 3);
		GP.add(passwordLabel, 0, 4);
		GP.add(passwordField, 0, 5);
		GP.add(confirmPasswordLabel, 0, 6);
		GP.add(confirmPasswordField, 0, 7);
		GP.add(phoneNumberLabel, 0, 8);
		GP.add(phoneNumberField, 0, 9);
		GP.setAlignment(Pos.CENTER);
		GP.setVgap(5);
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(registerLabel, GP, signUpButton);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		
		//set Style
		registerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 50));
		emailField.setMinWidth(250);
		passwordField.setMinWidth(250);
		signUpButton.setMinSize(80, 35);
		
		return vBox;
	}
	
	public boolean validateForm() {
        String name = userNameField.getText();
        String email = emailField.getText();
        String pass = passwordField.getText();
        String conformPass = confirmPasswordField.getText();
        String phoneNumber = phoneNumberField.getText();

        if (name.length() < 4 || name.length() > 20) {
            showAlert("Invalid Input" ,"Invalid Username", "Username must contain 4 – 20 characters");
            return false;
        }

        if (!email.contains("@")) {
            showAlert("Invalid Input" ,"Invalid Email", "Email must contain '@' in it");
            return false;
        }

        if (registerController.isEmailUnique(email)) {
            showAlert("Invalid Input" ,"Invalid Email", "Email must be unique");
            return false;
        }

        if (pass.length() < 6 || pass.length() > 20) {
            showAlert("Invalid Input" ,"Invalid Password", "Password must contain 6 – 20 characters");
            return false;
        }
        
        if (!pass.matches("[a-zA-Z0-9]+")) {
        	showAlert("Invalid Input" ,"Invalid Password", "Password must be alphanumeric");
        	return false;
        }

        if (!pass.equals(conformPass)) {
            showAlert("Invalid Input" ,"Invalid Confirm Password", "Confirm Password must be the same as Password");
            return false;
        }

        if (!phoneNumber.matches("\\d+")) {
            showAlert("Invalid Input" ,"Invalid Phone Number", "Phone Number can only be numeric");
            return false;
        }
        
        if (phoneNumber.length() < 9 || phoneNumber.length() > 20) {
        	showAlert("Invalid Input" ,"Invalid Phone Number", "Phone Number must be 9 – 20 numbers");
        	return false;
        }
        
        // true -> pass
        return true;
	}
	
	public User createUser() {
		String name = userNameField.getText();
	    String email = emailField.getText();
	    String password = passwordField.getText();
	    String phoneNumber = phoneNumberField.getText();
	    String role = "customer";

	    User userData = new User("", name,  password, phoneNumber, email, role);

	    return userData;
	}
	
    private void showAlert(String title, String header,String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
	
	public MenuItem getToLoginMenuItem() {
		return toLogin;
	}
	
	public Button getSignUpButton() {
		return signUpButton;
	}

}

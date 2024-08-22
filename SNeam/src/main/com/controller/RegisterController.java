package main.com.controller;

import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.com.model.User;
import main.com.util.Connect;
import main.com.view.LoginPage;
import main.com.view.RegisterPage;

public class RegisterController {

    private final RegisterPage registerPage;    
    Connect connect;
    ObservableList<User> users = FXCollections.observableArrayList();
    
    public RegisterController(RegisterPage registerPage) {
        this.registerPage = registerPage;

        eventHandlers();
    }
    
    private void eventHandlers() {
        registerPage.getToLoginMenuItem().setOnAction(event -> {
        	switchToLoginPage();
        });
        
        registerPage.getSignUpButton().setOnAction(event -> {
			if(registerPage.validateForm()) {
				createUser();	
				showAlert("Request Success", "Success!", "Account has been registered, please fill the login form");
			}
        });
    }

    private void switchToLoginPage() {
    	LoginPage loginPage = new LoginPage();
        registerPage.getScene().setRoot(loginPage);
    }
    
    
    public void getData() {
    	try {
    		connect = new Connect();
    		users.clear();
			ResultSet res = connect.runQuery("SELECT * FROM User");
			
			while(res.next()) {
				String id = res.getString("UserID");
				String name = res.getString("UserName");
				String password = res.getString("Password");
				String phoneNumber = res.getString("PhoneNumber");
				String email = res.getString("Email");
				String role = res.getString("Role");
				
				users.add(new User(id, name, password, phoneNumber, email, role));
			}
			
			connect.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void createUser() {
    	getData();
    	User newUser = registerPage.createUser();
    	newUser.setId(String.format("AC%03d", users.size() + 1));
    	connect = new Connect();
    	
    	try {
    		
			connect.runUpdate(String.format("INSERT INTO User VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", 
					newUser.getId(), newUser.getName(), newUser.getPassword(), newUser.getPhoneNumber(), newUser.getEmail(), newUser.getRole()));
			connect.close();			
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public boolean isEmailUnique (String email) {
    	connect = new Connect();
    	
    	try {
			ResultSet res = connect.runQuery(String.format("SELECT Email FROM User WHERE Email LIKE '%s'", email));
			if(res.next()) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//true -> pass
    	return false;
    }
    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setOnCloseRequest(e -> {
        	switchToLoginPage();
        });
        alert.showAndWait();
    }

}

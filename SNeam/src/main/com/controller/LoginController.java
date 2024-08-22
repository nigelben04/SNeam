package main.com.controller;

import java.sql.ResultSet;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.com.model.User;
import main.com.util.Connect;
import main.com.view.HomePage;
import main.com.view.LoginPage;
import main.com.view.RegisterPage;

public class LoginController {

    private final LoginPage loginPage;
    Connect connect = new Connect();

    public LoginController(LoginPage loginPage) {
        this.loginPage = loginPage;

        eventHandlers();
    }

    private void eventHandlers() {
        loginPage.getToRegisterMenuItem().setOnAction(event -> {
        	switchToRegisterPage();
        });
        
        loginPage.getSignInButton().setOnAction(event -> {
        	validateUserData();
        });
    }

    private void switchToRegisterPage() {
        RegisterPage registerPage = new RegisterPage();
        loginPage.getScene().setRoot(registerPage);
    }
    
    private void switchToHomePage(User userData) {
        HomePage homePage = new HomePage(userData);
        loginPage.getScene().setRoot(homePage);
    }
    
    private void validateUserData() {
        try {
        	connect = new Connect();
            ResultSet res = connect.runQuery(String.format(
                    "SELECT * FROM User WHERE Email='%s' AND Password='%s'",
                    loginPage.getEmailValue(), loginPage.getPasswordValue()
            ));
            User tempData;

            if (res.next()) {
            	String userId = res.getString("UserID");
            	String userName = res.getString("UserName");
            	String password = res.getString("Password");
            	String phoneNumber = res.getString("PhoneNumber");
            	String email = res.getString("Email");
                String userRole = res.getString("Role");
                switchToHomePage(tempData = new User(userId, userName, password, phoneNumber, email, userRole));
            } else {
                showAlert("Invalid Request", "Wrong Credentials", "Email or password is invalid");
                
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
}

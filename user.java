package PROJECT.Banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class user {
    private Connection connection;
    private Scanner scanner;

    public user(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();
        if(!isValidName(full_name)) {
            System.out.println("Invalid Name!");
            return;
        }
        System.out.print("Email: ");
        String email = scanner.nextLine();
        if(!isValidEmail(email)) {
            System.out.println("Invalid Email!");
            return;
        }
        System.out.print("Password: ");
        String password = scanner.nextLine();
        if(!isValidPassword(password)) {
            System.out.println("Invalid Password!");
            return;
        }
        if(user_exist(email)) {
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }
        String register_query = "INSERT INTO User(full_name, email, password) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Registration Successfull!");
            } else {
                System.out.println("Registration Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login(){
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String login_query = "SELECT * FROM User WHERE email = ? AND password = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            }else{
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exist(String email){
        String query = "SELECT * FROM user WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

     public boolean close_account(){
        scanner.nextLine();
        System.out.print("Enter Email: ");
        String email=scanner.nextLine();
//        System.out.print("Enter Security Pin: ");
//        String password=scanner.nextLine();
//            String query1="SELECT account_number from Accounts WHERE email = ? and security_pin = ?";
        String query = "DELETE acc, us " +
                "FROM accounts AS acc " +
                "INNER JOIN user AS us " +
                "ON acc.email = us.email " +
                "WHERE acc.email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
//            preparedStatement.setString(2, password);
//                ResultSet resultSet = preparedStatement.executeQuery();
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
//                    PreparedStatement preparedStatement1 = connection.prepareStatement(query);
//                    preparedStatement1.setString(1, email);
//                    preparedStatement1.setString(2, password);
//                    preparedStatement1.executeUpdate();
                return true;
            }
            else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
    public static boolean isValidName(String name) {

        String regex = "^[a-zA-Z ]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);
        return m.matches();
    }
    public boolean isValidEmail(String email) {
        String emailRegex= "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(emailRegex);
        Matcher m = p.matcher(email);
        return m.matches();
    }
    public boolean isValidPassword( String password) {
        String pw= "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        Pattern p= Pattern.compile(pw);
        Matcher m= p.matcher(password);
        boolean b= m.matches();
        return b;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xemacscode.demo;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class MainController implements Initializable {
    
    
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfAuthor;
    @FXML
    private TextField tfYear;
    @FXML
    private TextField tfPages;
    @FXML
    private TableView<Books> tvBooks;
    @FXML
    private TableColumn<Books, Integer> colId;
    @FXML
    private TableColumn<Books, String> colTitle;
    @FXML
    private TableColumn<Books, String> colAuthor;
    @FXML
    private TableColumn<Books, Integer> colYear;
    @FXML
    private TableColumn<Books, Integer> colPages;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {        
        
        if(event.getSource() == btnInsert){
            insertRecord();
        }else if (event.getSource() == btnUpdate){
            updateRecord();
        }else if(event.getSource() == btnDelete){
            deleteButton();
        }
            
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showBooks();
    }
    
    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root","");
            return conn;
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }
    
    public ObservableList<Books> getBooksList(){
        ObservableList<Books> bookList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM books";
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Books books;
            while(rs.next()){
                books = new Books(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getInt("year"),rs.getInt("pages"));
                bookList.add(books);
            }
                
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return bookList;
    }
    
    public void showBooks(){
        ObservableList<Books> list = getBooksList();
        
        colId.setCellValueFactory(new PropertyValueFactory<Books, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<Books, Integer>("year"));
        colPages.setCellValueFactory(new PropertyValueFactory<Books, Integer>("pages"));
        
        tvBooks.setItems(list);
    }
    private void insertRecord(){
        String query = "INSERT INTO books VALUES (" + tfId.getText() + ",'" + tfTitle.getText() + "','" + tfAuthor.getText() + "',"
                + tfYear.getText() + "," + tfPages.getText() + ")";
        executeQuery(query);
        showBooks();
    }
    private void updateRecord(){
        String query = "UPDATE  books SET title  = '" + tfTitle.getText() + "', author = '" + tfAuthor.getText() + "', year = " +
                tfYear.getText() + ", pages = " + tfPages.getText() + " WHERE id = " + tfId.getText() + "";
        executeQuery(query);
        showBooks();
    }
    private void deleteButton(){
        String query = "DELETE FROM books WHERE id =" + tfId.getText() + "";
        executeQuery(query);
        showBooks();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
        
    
}

package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import se.lu.ics.models.Product;
import se.lu.ics.models.Stored;
import se.lu.ics.models.Supplier;
import se.lu.ics.data.ProductDAO;
import se.lu.ics.data.SupplierDAO;



public class ProductController {
    
    @FXML
    private TextField textFieldProductId;
    @FXML
    private TextField textFieldSupplierId; 
    @FXML
    private TextField textFieldProductName;
      @FXML
    private TextField textFieldProductCategory;
      @FXML
    private Button buttonAddProduct;
      @FXML
    private TableView<Product> tableViewProduct;
      @FXML
    private TableColumn<Product, String> columnProductId;
      @FXML
    private TableColumn<Product, String> columnProductName;
      @FXML
    private TableColumn<Product, String> columnProductCategory;
      @FXML
    private TableColumn<Product, String> columnProductSupplierId;
    
    @FXML private Label label_errorMessage;
    
    @FXML private Label labelSupplierId; 

    @FXML private Button buttonRemoveProduct;


    public void initialize () 
    {
        columnProductId.setCellValueFactory(new PropertyValueFactory<Product, String>("productId"));
        columnProductName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
        columnProductCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("productCategory"));
        columnProductSupplierId.setCellValueFactory(new PropertyValueFactory<Product, String>("supplierId"));
        
        tableViewProduct.getItems().addAll(ProductDAO.getProducts());

        tableViewProduct.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldSelection, newSelection) -> { if (newSelection != null) {
                Product selectedProduct = tableViewProduct.getSelectionModel().getSelectedItem();

                textFieldProductId.setText(selectedProduct.getProductId());
                textFieldProductName.setText(selectedProduct.getProductName());
                textFieldProductCategory.setText(selectedProduct.getProductCategory());
            }
        });
    };
    
    public void buttonAddProduct_OnClick(ActionEvent event) {
      try {
          String productId = textFieldProductId.getText();
          String productName = textFieldProductName.getText();
          String productCategory = textFieldProductCategory.getText();
          Supplier supplier = SupplierDAO.getSupplierById(textFieldSupplierId.getText());
  
          // Create a Product object with the provided data
          Product product = new Product(productId, productName, productCategory, supplier);
  
          // Add the product to the TableView
          tableViewProduct.getItems().add(product);
  
          // You might want to update your data source (ProductDAO) if needed
          ProductDAO.addProductToDatabase(product);
  
          // Clear the input fields after adding the product
          textFieldProductId.clear();
          textFieldProductName.clear();
          textFieldProductCategory.clear();
          textFieldSupplierId.clear(); // Clear the supplier field as well
  
      } catch (Exception e) {
          label_errorMessage.setText("Error: " + e.getMessage());
      }
  }
    
/* public void buttonAddProduct_OnClick() {

    try {
        String productId = textFieldProductId.getText();
        String productName = textFieldProductName.getText();
        String productCategory = textFieldProductCategory.getText();
        Supplier supplierId = SupplierDAO.getSupplierById(textFieldSupplierId.getText());
        // Create a Product object with the provided data

        Product product = new Product(productId, productName, productCategory, supplierId);


        // Add the product to the TableView
        
        //tableViewProduct.getItems().add(product);

        // You might want to update your data source (ProductDAO) if needed
        //ProductDAO.addProductToDatabase(product);

        // Clear the input fields after adding the product
        textFieldProductId.clear();
        textFieldProductName.clear();
        textFieldProductCategory.clear();
    

    } catch (Exception e) {
        label_errorMessage.setText("Error: " + e.getMessage());
    }
} */

public void buttonRemoveProduct_OnClick(ActionEvent event) {
  // Get the selected product from the TableView
  Product productToRemove = tableViewProduct.getSelectionModel().getSelectedItem();

  if (productToRemove != null) {
      // Remove the product from the TableView
      tableViewProduct.getItems().remove(productToRemove);

      // Remove the product from the database
      ProductDAO.removeProductFromDatabase(productToRemove);
      
      // Clear the input fields (if needed)
      clearInputFields();
      clearLabels();
  } else {
      label_errorMessage.setText("Error: Please select a product to remove.");
  }
}

private void clearInputFields() {
  textFieldProductId.clear();
  textFieldProductName.clear();
  textFieldProductCategory.clear();
  textFieldSupplierId.clear();
}

    public void buttonUpdateProduct_OnClick(ActionEvent event) {
        String productId = textFieldProductId.getText();
        String productName = textFieldProductName.getText();
        String productCategory = textFieldProductCategory.getText();

        Product productToUpdate = tableViewProduct.getSelectionModel().getSelectedItem();
        productToUpdate.setProductId(productId);
        productToUpdate.setProductName(productName);
        productToUpdate.setProductCategory(productCategory);

        tableViewProduct.refresh();

    }

    private void clearLabels() {
      labelSupplierId.setText(null);
      label_errorMessage.setText(null);
    }

}
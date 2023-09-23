package se.lu.ics.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import se.lu.ics.models.Product;
import se.lu.ics.models.Supplier;
import se.lu.ics.data.ProductDAO;
import se.lu.ics.data.SupplierDAO;

public class ProductController {

  @FXML
  private TextField textFieldProductId;
 // @FXML
  //private TextField textFieldSupplierId;
  @FXML
  private TextField textFieldProductName;
  @FXML
  private TextField textFieldProductCategory;
  @FXML
  private TextField textFieldSearchProduct;
  @FXML
  private ComboBox<String> comboBoxSupplierId;
  @FXML
  private Button buttonAddProduct;
  @FXML
  private TableView<Product> tableViewProduct;
  @FXML
  private TableView<Product> tableView_supplierProductList;
  @FXML
  private TableColumn<Product, String> columnProductId;
  @FXML
  private TableColumn<Product, String> columnProductName;
  @FXML
  private TableColumn<Product, String> columnProductCategory;
  @FXML
  private TableColumn<Product, String> columnProductSupplierId;

  @FXML
  private Label label_errorMessage;

  @FXML
  private Label labelSupplierId;

  @FXML
  private Button buttonRemoveProduct;

  @FXML
  private Button buttonUpdateProduct;

  private FilteredList<Product> filteredProducts;

  public void initialize() {
    columnProductId.setCellValueFactory(new PropertyValueFactory<Product, String>("productId"));
    columnProductName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
    columnProductCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("productCategory"));
    columnProductSupplierId.setCellValueFactory(new PropertyValueFactory<Product, String>("supplierId"));

    tableViewProduct.getItems().addAll(ProductDAO.getProducts());
    tableViewProduct.setItems(ProductDAO.getProducts());

    filteredProducts = new FilteredList<>(ProductDAO.getProducts(), p -> true);
    tableViewProduct.setItems(filteredProducts);

    ObservableList<String> supplierIds = FXCollections.observableArrayList();
    for (Supplier supplier : SupplierDAO.getSuppliers()) {
      supplierIds.add(supplier.getSupplierId());
    }
    comboBoxSupplierId.setItems(supplierIds);

    textFieldSearchProduct.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredProducts.setPredicate(product -> {
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        String lowerCaseFilter = newValue.toLowerCase();

        if (product.getProductId().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (product.getProductName().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (product.getProductCategory().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (product.getSupplierId().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        }
        return false;
      });
    });

    tableViewProduct.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldSelection, newSelection) -> {
          if (newSelection != null) {
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

      if (productName.isEmpty() || productId.isEmpty() || productCategory.isEmpty()) {
        label_errorMessage.setText("Product ID, Name and Category cannot be empty. Please fill in all fields.");
        return;
      }
      {
        for (Product existingProduct : tableViewProduct.getItems()) {
          if (existingProduct.getProductId().equals(productId)) {
              label_errorMessage.setText("Product ID must be unique. Please enter a non-existing ID.");
              return; // Exit the method to prevent adding the product
          }
      }
      }

      productName = productName.substring(0, 1).toUpperCase() + productName.substring(1).toLowerCase();
      productCategory = productCategory.substring(0, 1).toUpperCase() + productCategory.substring(1).toLowerCase();
      
      String supplierId = comboBoxSupplierId.getValue();
      if (supplierId == null) {
        label_errorMessage.setText("Please select a supplier.");
        return;
      }
      Supplier supplier = SupplierDAO.getSupplierById(supplierId);
      if (supplier == null) {
        label_errorMessage.setText("Supplier cannot be found.");
        return;
      }

      // Create a Product object with the provided data
      //Product product = new Product(productId, productName, productCategory, supplier);
      
      // Add the product to the TableView
      //tableViewProduct.getItems().addAll(product);

      // You might want to update your data source (ProductDAO) if needed
      ProductDAO.addProductToDatabase(productId, productName, productCategory, supplier);

      // Clear the input fields after adding the product
      textFieldProductId.clear();
      textFieldProductName.clear();
      textFieldProductCategory.clear();

    } catch (Exception e) {
      label_errorMessage.setText("Error: " + e.getMessage());
    }
  }

  /*
   * public void buttonAddProduct_OnClick() {
   * 
   * try {
   * String productId = textFieldProductId.getText();
   * String productName = textFieldProductName.getText();
   * String productCategory = textFieldProductCategory.getText();
   * Supplier supplierId =
   * SupplierDAO.getSupplierById(textFieldSupplierId.getText());
   * // Create a Product object with the provided data
   * 
   * Product product = new Product(productId, productName, productCategory,
   * supplierId);
   * 
   * 
   * // Add the product to the TableView
   * 
   * //tableViewProduct.getItems().add(product);
   * 
   * // You might want to update your data source (ProductDAO) if needed
   * //ProductDAO.addProductToDatabase(product);
   * 
   * // Clear the input fields after adding the product
   * textFieldProductId.clear();
   * textFieldProductName.clear();
   * textFieldProductCategory.clear();
   * 
   * 
   * } catch (Exception e) {
   * label_errorMessage.setText("Error: " + e.getMessage());
   * }
   * }
   */

  @FXML
  public void buttonUpdateProduct_OnClick(ActionEvent event) {
    Product selectedProduct = tableViewProduct.getSelectionModel().getSelectedItem();

    if (selectedProduct == null) {
      label_errorMessage.setText("Error: Please select a product to update.");
      return;
    }

    try {
      // Get the updated values from the text fields
      String productName = textFieldProductName.getText();
      String productCategory = textFieldProductCategory.getText();

      // Update the selected product's information
      selectedProduct.setProductName(productName);
      selectedProduct.setProductCategory(productCategory);

      // Update the product in the data source (assuming ProductDAO handles this)
      ProductDAO.updateProductInDatabase(selectedProduct);

      // Refresh the table view to reflect the changes
      tableViewProduct.refresh();
      tableView_supplierProductList.refresh();

      clearInputFields();
      clearLabels();

      // Display a success message
      label_errorMessage.setText("Product updated successfully");
    } catch (Exception e) {
      label_errorMessage.setText("Error: Please make sure you have proper data inputs in all fields");
    }
  }

  @FXML
  public void buttonRemoveProduct_OnClick(ActionEvent event) {
    // Get the selected product from the TableView
    Product productToRemove = tableViewProduct.getSelectionModel().getSelectedItem();
    String productId = productToRemove.getProductId();

    

    //if (productToRemove != null) {
      // Remove the product from the TableView
      //tableViewProduct.getItems().remove(productToRemove);

      // Remove the product from the database
      ProductDAO.removeProductFromDatabase(productToRemove);
      tableViewProduct.refresh();

      // Clear the input fields (if needed)
      clearInputFields();
      clearLabels();
    
  }
  

  private void clearInputFields() {
    textFieldProductId.clear();
    textFieldProductName.clear();
    textFieldProductCategory.clear();
   
  }

  // Search for product by id method
  public void productSearchById() {
    String productId = textFieldSearchProduct.getText().toLowerCase();
    for (Product product : ProductDAO.getProducts()) {
      tableViewProduct.getSelectionModel().select(product);
      if (product.getProductId().toLowerCase().equals(productId)) {

      }

    }

  }

  /*
   * public void buttonUpdateProduct_OnClick(ActionEvent event) {
   * String productId = textFieldProductId.getText();
   * String productName = textFieldProductName.getText();
   * String productCategory = textFieldProductCategory.getText();
   * 
   * Product productToUpdate =
   * tableViewProduct.getSelectionModel().getSelectedItem();
   * productToUpdate.setProductId(productId);
   * productToUpdate.setProductName(productName);
   * productToUpdate.setProductCategory(productCategory);
   * 
   * tableViewProduct.refresh();
   * 
   * }
   */

  private void clearLabels() {
    labelSupplierId.setText(null);
    label_errorMessage.setText(null);
  }

}
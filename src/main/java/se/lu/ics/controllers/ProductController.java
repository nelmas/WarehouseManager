package se.lu.ics.controllers;

import java.sql.SQLException;

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
import se.lu.ics.models.Stored;
import se.lu.ics.models.Supplier;
import se.lu.ics.data.ProductDAO;
import se.lu.ics.data.StoredDAO;
import se.lu.ics.data.SupplierDAO;

public class ProductController {

  @FXML
  private TextField textFieldProductId;

  // @FXML
  // private TextField textFieldSupplierId;

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

  @FXML
  private Button buttonCategoryFilterReset;

  @FXML
  private ComboBox<String> comboBoxCategoryFilter;

  @FXML
  private Label labelComboBoxFilterInfo;

  @FXML
  private FilteredList<Product> filteredProducts;

 @FXML
 private Label label_successMessageProduct; 

  public void initialize() {
    columnProductId.setCellValueFactory(new PropertyValueFactory<Product, String>("productId"));
    columnProductName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
    columnProductCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("productCategory"));
    columnProductSupplierId.setCellValueFactory(new PropertyValueFactory<Product, String>("supplierId"));

    tableViewProduct.getItems().addAll(ProductDAO.getProducts());
    tableViewProduct.setItems(ProductDAO.getProducts());

    filteredProducts = new FilteredList<>(ProductDAO.getProducts(), p -> true);
    tableViewProduct.setItems(filteredProducts);

    // Populate the product categories in the comboBoxCategoryFilter
    ObservableList<String> productCategories = ProductDAO.getProductCategories();
    comboBoxCategoryFilter.setItems(productCategories);

    // Set up the event handler for the combo box selection change
    comboBoxCategoryFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      // Call a method to filter products based on the selected category
      filterProductsByCategory(newValue);
    });

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
    ObservableList<Product> products = FXCollections.observableArrayList();
      String productId = textFieldProductId.getText();
      String productName = textFieldProductName.getText();
      String productCategory = textFieldProductCategory.getText();
      String supplierId = comboBoxSupplierId.getValue();

      try {

        if (productId.isEmpty() || productName.isEmpty() || productCategory.isEmpty() || supplierId == null) {
          label_successMessageProduct.setText("");
          label_errorMessage.setText("Make sure all fields are filled in.");
          
        } else {
          Supplier supplier = SupplierDAO.getSupplierById(supplierId);
          Product product = new Product(productId, productName, productCategory, supplier );
          ProductDAO.addProductToDatabase(productId, productName, productCategory, supplier);
          tableViewProduct.getItems().addAll(products);
          label_errorMessage.setText("");
          label_successMessageProduct.setText("Product added successfully");

        }

      } catch (SQLException e1) {
        if(e1.getErrorCode() == 2627) {
          label_successMessageProduct.setText("");
          label_errorMessage.setText("Product ID already exists. Please enter a new Product ID.");
        }
        if(e1.getErrorCode() == 0) {
          label_successMessageProduct.setText("");
          label_errorMessage.setText("Connection to database lost.");
        }
      
      } catch (NullPointerException e3) {
        label_successMessageProduct.setText("");
        label_errorMessage.setText("Please select a supplier.");
      
      } 
      
      
    }


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

    } catch (Exception e) {
      label_errorMessage.setText("Error: Please make sure you have proper data inputs in all fields");
    }
  }

  @FXML
  public void buttonRemoveProduct_OnClick(ActionEvent event) {
    try {
      Product productToRemove = tableViewProduct.getSelectionModel().getSelectedItem();


      if (productToRemove == null) {
        label_errorMessage.setText("Error: Please select a product to remove.");
        return;
      } else {
        ProductDAO.removeProductFromProductTable(productToRemove);
        tableViewProduct.getItems().remove(productToRemove);
        tableViewProduct.refresh();
        label_errorMessage.setText("");
        label_successMessageProduct.setText("Product removed successfully");
      }

      // Clear the input fields (if needed)
      clearInputFields();
      clearLabels();

    } catch (SQLException e1) {
      label_successMessageProduct.setText("");
      label_errorMessage.setText("Product must be removed from warehouse first");
    
    } catch (NumberFormatException e2) {
      label_successMessageProduct.setText("");
      label_errorMessage.setText("Error: Please make sure you have proper data inputs in all fields");
    }
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



  private void clearLabels() {
    label_errorMessage.setText(null);
    label_successMessageProduct.setText(null);
    labelSupplierId.setText(null);
    label_errorMessage.setText(null);
  }

  private void filterProductsByCategory(String selectedCategory) {
    if (selectedCategory != null && !selectedCategory.isEmpty()) {
      // Filter products by category and create a new list
      filteredProducts.setPredicate(product -> product.getProductCategory().equalsIgnoreCase(selectedCategory));
    } else {
      // If no category is selected, show all products
      filteredProducts.setPredicate(p -> true);
    }
  }

  @FXML
  public void buttonCategoryFilterReset_OnClick(ActionEvent event) {
    comboBoxCategoryFilter.getSelectionModel().clearSelection();
    clearInputFields();
    clearLabels();
  }

}
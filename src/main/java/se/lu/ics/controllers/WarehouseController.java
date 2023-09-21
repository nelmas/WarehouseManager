package se.lu.ics.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import se.lu.ics.data.ProductDAO;
import se.lu.ics.data.StoredDAO;
import se.lu.ics.data.WarehouseDAO;
import se.lu.ics.models.Product;
import se.lu.ics.models.Stored;
import se.lu.ics.models.Warehouse;

public class WarehouseController {
    // Warehouse
    @FXML
    private TableView<Warehouse> warehouseTableView;
    @FXML
    private TableColumn<Warehouse, String> warehouseIdColumn;
    @FXML
    private TableColumn<Warehouse, String> warehouseAddressColumn;
    @FXML
    private TableColumn<Warehouse, Integer> warehouseCapacityColumn;
    @FXML
    private TextField warehouseSearchtextField;
    @FXML
    private Button warehouseAddButton;
    @FXML
    private Label warehouseLabel;
    @FXML
    private Label labelClickOnWarehouse;
    @FXML
    private Label labelWarehouseId; 
    @FXML
    private Label labelWarehouseAddress; 
    @FXML
    private Label labelWarehouseCapacity; 
    @FXML 
    private Label labelAddWarehouse;
    @FXML 
    private TextField TextFieldWarehouseId; 
    @FXML
    private TextField TextFieldWarehouseAddress;
    @FXML
    private TextField TextFieldWarehouseCapacity;
    @FXML 
    private Button resetButtonWarehouse; 

    // Stored
    @FXML
    private TableView<Stored> storedTableView;
    @FXML
    private TableColumn<Stored, String> StoredProductIdColumn;
    @FXML
    private TableColumn<Stored, String> StoredProductNameColumn;
    @FXML
    private TableColumn<Stored, Integer> StoredStockColumn;
    @FXML
    private TableColumn<Stored, String> storedSupplierIdColumn;
    @FXML
    private Label labelStock; 

    // Category
    @FXML
    private TableView<Product> tableViewCategory;
    @FXML
    private TableColumn<Product, String> categoryColumnProduct;
    @FXML
    private Label labelClickOnCategory;

    public void initialize() {
        // Warehouse table
        warehouseIdColumn.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("warehouseId"));
        warehouseAddressColumn.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("warehouseAddress"));
        warehouseCapacityColumn.setCellValueFactory(new PropertyValueFactory<Warehouse, Integer>("warehouseCapacity"));
        warehouseTableView.getItems().addAll(WarehouseDAO.getWarehouses());

        // Stored table
        StoredProductIdColumn.setCellValueFactory(new PropertyValueFactory<Stored, String>("productId"));
        StoredProductNameColumn.setCellValueFactory(new PropertyValueFactory<Stored, String>("productName"));
        StoredStockColumn.setCellValueFactory(new PropertyValueFactory<Stored, Integer>("stock"));
        storedSupplierIdColumn.setCellValueFactory(new PropertyValueFactory<Stored, String>("supplierId"));
        storedTableView.getItems().addAll(StoredDAO.getStoredItems());

        // Category table
        categoryColumnProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("productCategory"));
        // Distinct categories stored in hashSet. 
        Set<String> distinctCategories = new HashSet<>();
        
        for (Product product : ProductDAO.getProducts()) {
            distinctCategories.add(product.getProductCategory());
        }

        // Create dummy Product objects for distinct categories
        List<Product> distinctCategoryProducts = new ArrayList<>();
        for (String category : distinctCategories) {
            distinctCategoryProducts.add(new Product("", "", category, null));
        }
        // Populate tableViewCategory with the dummy products
        tableViewCategory.getItems().addAll(distinctCategoryProducts);
        
        
        
        // Add listener to warehouse table
        warehouseTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                labelClickOnWarehouse.setVisible(false);
                showProductsFromWarehouse();
                
            }
        });

        // Add listener to Category table
        tableViewCategory.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                labelClickOnCategory.setVisible(false);
                showProductsFromCategory();
            }
        });
    }

    // method that shows products based on the warehouse we press
    public void showProductsFromWarehouse() {
        Warehouse selectedWarehouse = warehouseTableView.getSelectionModel().getSelectedItem();
        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(StoredDAO.getStoredInfoWithWarehouse(selectedWarehouse));
        calculateStock(selectedWarehouse); 
    }

    // Method that shows product based on the category we press
    public void showProductsFromCategory() {
        Product selectedCategory = tableViewCategory.getSelectionModel().getSelectedItem();
        storedTableView.getItems().clear();
        storedTableView.getItems()
                .addAll(StoredDAO.getStoredInfoWithProductCategory(selectedCategory.getProductCategory()));
    }
    //Method for adding a warehouse
    public void addWarehouseButtonClicked() {
        String warehouseId = TextFieldWarehouseId.getText();
        String warehouseAddress = TextFieldWarehouseAddress.getText();
        Integer warehouseCapacity = Integer.parseInt(TextFieldWarehouseCapacity.getText());
        WarehouseDAO.addWarehouse(warehouseId, warehouseAddress, warehouseCapacity);
        warehouseTableView.getItems().clear();
        warehouseTableView.getItems().addAll(WarehouseDAO.getWarehouses());
        
    }
    //Calculate stock method
    public void calculateStock (Warehouse selectedWarehouse) {
        int stock = 0;
       for(Stored stored : storedTableView.getItems()) {
           stock += stored.getStock();
       }
         int warehouseCapacity = selectedWarehouse.getWarehouseCapacity();
        int availableCapacity = warehouseCapacity - stock;               

                labelStock.setText("Available stock: " + availableCapacity);
           } 
           
        public void resetButtonWarehouse() {
            TextFieldWarehouseId.clear();
            TextFieldWarehouseAddress.clear();
            TextFieldWarehouseCapacity.clear();
            
            storedTableView.getItems().clear();
            storedTableView.getItems().addAll(StoredDAO.getStoredItems());
      
            System.out.println("Reset button clicked");
        }
       }



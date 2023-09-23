package se.lu.ics.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private Label labelAddWarehouseError;
    @FXML
    private Label labelAddWarehouseSuccess;
    @FXML
    private Button lowStockButton;

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
    @FXML
    private Label labelProductAmount;

    // Category
    @FXML
    private TableView<Product> tableViewCategory;
    @FXML
    private TableColumn<Product, String> categoryColumnProduct;
    @FXML
    private Label labelClickOnCategory;

    // Add/remove products

    @FXML
    private Label label_errorMessageAddRemoveProducts;

    @FXML
    private Label label_chooseProduct;

    @FXML
    private Label label_infoAddRemoveProduct;

    @FXML
    private Label label_chooseWarehouse;

    @FXML
    private Label labelEnterQuantity;

    @FXML
    private Button buttonAddProduct;

    @FXML
    private Button buttonRemoveProduct;

    @FXML
    private TextField textFieldEnterQuantity;

    @FXML
    private ComboBox<String> ComboBoxChooseProduct;


    @FXML
    private ComboBox<String> ComboBoxChooseWarehouse;

    private ObservableList<String> productIds = FXCollections.observableArrayList();
    private ObservableList<String> warehouseIds = FXCollections.observableArrayList();





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
        calculateProductAmount();

        // Populate ComboBoxChooseProduct with product IDs
    productIds.addAll(ProductDAO.getProducts().stream()
            .map(Product::getProductId)
            .distinct()
            .collect(Collectors.toList()));
    ComboBoxChooseProduct.setItems(productIds);

    // Populate ComboBoxChooseWarehouse with warehouse IDs
    warehouseIds.addAll(WarehouseDAO.getWarehouses().stream()
            .map(Warehouse::getWarehouseId)
            .collect(Collectors.toList()));
    ComboBoxChooseWarehouse.setItems(warehouseIds);

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
                calculateProductAmount();

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
        calculateProductAmount();
        Warehouse selectedWarehouse = warehouseTableView.getSelectionModel().getSelectedItem();
        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(StoredDAO.getStoredInfoWithWarehouse(selectedWarehouse));
        calculateCapacity(selectedWarehouse);
        labelStock.setVisible(true);
        labelProductAmount.setVisible(true);
    }

    // Method that shows product based on the category we press
    public void showProductsFromCategory() {
        labelProductAmount.setVisible(true);
        labelStock.setVisible(true);
        calculateProductAmount();
        Product selectedCategory = tableViewCategory.getSelectionModel().getSelectedItem();
        storedTableView.getItems().clear();
        storedTableView.getItems()
                .addAll(StoredDAO.getStoredInfoWithProductCategory(selectedCategory.getProductCategory()));

    }

    public void addWarehouseButtonClicked() {

        try {
            String warehouseId = TextFieldWarehouseId.getText();
            String warehouseAddress = TextFieldWarehouseAddress.getText();
            String warehouseCapacityString = TextFieldWarehouseCapacity.getText();

            if (warehouseId.isEmpty() || warehouseAddress.isEmpty() || warehouseCapacityString.isEmpty()) {
                labelAddWarehouseError.setText("Please fill in all fields");
                System.out.println("Please fill in all fields");

            } else {
                int warehouseCapacity = Integer.parseInt(warehouseCapacityString);
                WarehouseDAO.addWarehouse(warehouseId, warehouseAddress, warehouseCapacity);
                warehouseTableView.getItems().clear();
                warehouseTableView.getItems().addAll(WarehouseDAO.getWarehouses());
                labelAddWarehouseSuccess.setText("Warehouse added");
                labelAddWarehouseError.setText("");
            }
        } catch (SQLException e1) {

            if (e1.getErrorCode() == 2627) {
                labelAddWarehouseSuccess.setText("");
                labelAddWarehouseError.setText("Warehouse already exists");
                System.out.println("Warehouse already exists");

            }
            if (e1.getErrorCode() == 0) {
                labelAddWarehouseSuccess.setText("");
                labelAddWarehouseError.setText("Connection to database lost");
                System.out.println("No connection to database");
            }

        } catch (NumberFormatException e2) {
            labelAddWarehouseSuccess.setText("");
            labelAddWarehouseError.setText("Capacity needs to be a number");

        } catch (Exception e) {
            labelAddWarehouseSuccess.setText("");
            labelAddWarehouseError.setText("Something went wrong");
            System.out.println("Something went wrong");
        }

    }

    // Calculate capacity method
    public void calculateCapacity(Warehouse selectedWarehouse) {
        int stock = 0;
        for (Stored stored : storedTableView.getItems()) {
            stock += stored.getStock();
        }
        int warehouseCapacity = selectedWarehouse.getWarehouseCapacity();
        int availableCapacity = warehouseCapacity - stock;

        labelStock.setText("Available capacity: " + availableCapacity);
    }

    public void calculateProductAmount() {
        int productAmount = 0;
        for (Stored stored : storedTableView.getItems()) {
            productAmount += storedTableView.getItems().size();
            break;
        }

        labelProductAmount.setText("Product amount: " + productAmount);
    }

    public void resetButtonWarehouse() {
        labelStock.setVisible(false);

        TextFieldWarehouseId.clear();
        TextFieldWarehouseAddress.clear();
        TextFieldWarehouseCapacity.clear();
        labelAddWarehouseError.setText("");
        labelAddWarehouseSuccess.setText("");

        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(StoredDAO.getStoredItems());
        calculateProductAmount();
        System.out.println("Reset button clicked");
    }


    public void lowStockButtonClicked() {
        
        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(StoredDAO.getLowStockProducts());
        calculateProductAmount();

    }

    // Add product to warehouse
    @FXML
    public void button_addProductToWarehouse_OnClick(ActionEvent event) {
        try {
            String selectedProduct = ComboBoxChooseProduct.getValue();
            String selectedWarehouse = ComboBoxChooseWarehouse.getValue();
            int quantity = Integer.parseInt(textFieldEnterQuantity.getText());

            if (selectedProduct == null || selectedWarehouse == null || quantity <= 0) {
                label_errorMessageAddRemoveProducts.setText("Please select a product, a warehouse, and enter a valid quantity.");
            } else {
                Product product = ProductDAO.getProductById(selectedProduct);
                Warehouse warehouse = WarehouseDAO.getWarehouseById(selectedWarehouse);

                // Check if the product is already stored in the warehouse
                Stored existingStoredItem = null;
                for (Stored stored : storedTableView.getItems()) {
                    if (stored.getProduct().equals(product) && stored.getWarehouse().equals(warehouse)) {
                        existingStoredItem = stored;
                        break;
                    }
                }

                if (existingStoredItem != null) {
                    // Product already exists in the warehouse, update the quantity
                    int newQuantity = existingStoredItem.getStock() + quantity;
                    existingStoredItem.setStock(newQuantity);
                } else {
                    // Product doesn't exist in the warehouse, create a new Stored item
                    Stored newStoredItem = new Stored(product, warehouse, quantity);
                    storedTableView.getItems().add(newStoredItem);
                }

                // Update the database and clear input fields
                updateDatabase();
                clearAddRemoveProductFields();
                label_errorMessageAddRemoveProducts.setText("");
            }
        } catch (NumberFormatException e) {
            label_errorMessageAddRemoveProducts.setText("Please enter a valid quantity.");
        }
    }

    // Remove product from warehouse
    @FXML
    public void button_removeProductFromWarehouse_OnClick(ActionEvent event) {
        try {
            String selectedProduct = ComboBoxChooseProduct.getValue();
            String selectedWarehouse = ComboBoxChooseWarehouse.getValue();
            int quantity = Integer.parseInt(textFieldEnterQuantity.getText());

            if (selectedProduct == null || selectedWarehouse == null || quantity <= 0) {
                label_errorMessageAddRemoveProducts.setText("Please select a product, a warehouse, and enter a valid quantity.");
            } else {
                Product product = ProductDAO.getProductById(selectedProduct);
                Warehouse warehouse = WarehouseDAO.getWarehouseById(selectedWarehouse);

                // Find the existing Stored item
                Stored existingStoredItem = null;
                for (Stored stored : storedTableView.getItems()) {
                    if (stored.getProduct().equals(product) && stored.getWarehouse().equals(warehouse)) {
                        existingStoredItem = stored;
                        break;
                    }
                }

                if (existingStoredItem != null) {
                    int currentQuantity = existingStoredItem.getStock();
                    if (quantity >= currentQuantity) {
                        // Remove the entire quantity of the product from the warehouse
                        storedTableView.getItems().remove(existingStoredItem);
                    } else {
                        // Reduce the quantity of the product in the warehouse
                        existingStoredItem.setStock(currentQuantity - quantity);
                    }

                    // Update the database and clear input fields
                    updateDatabase();
                    clearAddRemoveProductFields();
                    label_errorMessageAddRemoveProducts.setText("");
                } else {
                    label_errorMessageAddRemoveProducts.setText("Product not found in the selected warehouse.");
                }
            }
        } catch (NumberFormatException e) {
            label_errorMessageAddRemoveProducts.setText("Please enter a valid quantity.");
        }
    }

    private void updateDatabase() {
        // Update the database with the changes made in the storedTableView
        // You can iterate through storedTableView.getItems() and update the database accordingly.
        // This involves updating the 'stock' column in the 'Stored' table for each item.
        // You should implement the database update logic here.
    }

    private void clearAddRemoveProductFields() {
        ComboBoxChooseProduct.getSelectionModel().clearSelection();
        ComboBoxChooseWarehouse.getSelectionModel().clearSelection();
        textFieldEnterQuantity.clear();
    }

    

}

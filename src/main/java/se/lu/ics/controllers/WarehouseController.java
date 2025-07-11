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
    @FXML 
    private Label labelTotalStock;

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
    private TableColumn<Stored, String> storedWarehouseIdColumn;
    @FXML
    private Label labelStock;
    @FXML
    private Label labelProductAmount;
    @FXML 
    private Button button_CalculateTotalStock; 
    @FXML
    private Label label_totalProductStock; 

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
    private Label labelAddProductToWarehouseSuccess;

    
    @FXML
    private ComboBox<String> ComboBoxChooseWarehouse;
    
    /*
    @FXML
    private ComboBox<Warehouse> ComboBoxChooseWarehouse;
    */

    @FXML
    private Button buttonUpdateStock;

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
        storedWarehouseIdColumn.setCellValueFactory(new PropertyValueFactory<Stored, String>("warehouseId"));
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
                calculateProductAmount();
            }
        });
    }

    // method that shows products based on the warehouse we press
    @FXML
    public void showProductsFromWarehouse() {

        calculateProductAmount();
        calculateTotalStockInWarehouse(); 
        Warehouse selectedWarehouse = warehouseTableView.getSelectionModel().getSelectedItem();
        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(StoredDAO.getStoredInfoWithWarehouse(selectedWarehouse));
        calculateCapacity(selectedWarehouse);
        labelStock.setVisible(true);
        labelProductAmount.setVisible(true);
    }

    @FXML 
    //Calculate total stock of one product across all warehouses
    public void calculateTotalStockForProduct_OnClick(ActionEvent event) {
        String selectedProduct = ComboBoxChooseProduct.getValue(); 
        Product product = ProductDAO.getProductById(selectedProduct);
        clearLabels();
        label_errorMessageAddRemoveProducts.setText("");
        label_totalProductStock.setText("Total stock: " + StoredDAO.getTotalStock(product));
        labelTotalStock.setVisible(true);
        labelProductAmount.setVisible(true);
    }

    // Method that shows product based on the category we press

    @FXML
    public void showProductsFromCategory() {
        labelProductAmount.setVisible(true);
        labelStock.setVisible(true);
        calculateTotalStockInWarehouse(); 
        calculateProductAmount();
        Product selectedCategory = tableViewCategory.getSelectionModel().getSelectedItem();

        Set<Stored> uniqueItems = new HashSet<>(); // Use a Set to store unique items

        // Iterate through stored items and add unique items to the set
        for (Stored stored : StoredDAO.getStoredInfoWithProductCategory(selectedCategory.getProductCategory())) {
            uniqueItems.add(stored);
        }

        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(uniqueItems); // Populate the table with unique items
    }

    @FXML
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
                warehouseTableView.refresh();
                warehouseIds.clear();
                warehouseIds.addAll(WarehouseDAO.getWarehouses().stream()
                        .map(Warehouse::getWarehouseId)
                        .collect(Collectors.toList()));
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
    @FXML
    public void calculateCapacity(Warehouse selectedWarehouse) {
        int stock = 0;
        for (Stored stored : storedTableView.getItems()) {
            stock += stored.getStock();
        }
        int warehouseCapacity = selectedWarehouse.getWarehouseCapacity();
        int availableCapacity = warehouseCapacity - stock;

        labelStock.setText("Available capacity: " + availableCapacity);
    }

    @FXML
    public void calculateProductAmount() {
        int productAmount = 0;
        productAmount += storedTableView.getItems().size();
        labelProductAmount.setText("Product amount: " + productAmount);
    }

    @FXML
    public void resetButtonWarehouse() {
        labelStock.setVisible(false);

        TextFieldWarehouseId.clear();
        TextFieldWarehouseAddress.clear();
        TextFieldWarehouseCapacity.clear();
        labelAddWarehouseError.setText("");
        labelAddWarehouseSuccess.setText("");
        labelClickOnWarehouse.setVisible(true);
        labelClickOnCategory.setVisible(true);
        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(StoredDAO.getStoredItems());
        ComboBoxChooseProduct.getSelectionModel().clearSelection();
        ComboBoxChooseWarehouse.getSelectionModel().clearSelection();
        textFieldEnterQuantity.clear();
        labelAddProductToWarehouseSuccess.setText("");
        label_errorMessageAddRemoveProducts.setText("");
        labelTotalStock.setVisible(false);
        label_totalProductStock.setText("");

        calculateProductAmount();
        System.out.println("Reset button clicked");
    }

    @FXML
    public void lowStockButtonClicked() {

        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(StoredDAO.getLowStockProducts());
        calculateProductAmount();

    }

    @FXML
    public void button_addProductToWarehouse_OnClick(ActionEvent event) {
        try {
            String selectedProduct = ComboBoxChooseProduct.getValue();
            String selectedWarehouse = ComboBoxChooseWarehouse.getValue();
            String quantityString = textFieldEnterQuantity.getText();

            if (selectedProduct == null || selectedWarehouse == null || quantityString.isEmpty()) {
                clearLabels(); 
                labelAddProductToWarehouseSuccess.setText("");
                label_errorMessageAddRemoveProducts.setText("Please make sure all fields are filled in");
                System.out.println("Please fill in all fields");

            } else if (Integer.parseInt(quantityString) < 0) {
                clearLabels(); 
                label_errorMessageAddRemoveProducts.setText("Stock cannot be negative.");

            } else {
                int quantity = Integer.parseInt(quantityString);
                Product product = ProductDAO.getProductById(selectedProduct);
                Warehouse warehouse = WarehouseDAO.getWarehouseById(selectedWarehouse);

                // Calculate the remaining capacity in the warehouse
                int remainingCapacity = StoredDAO.calculateRemainingCapacity(warehouse, selectedProduct,
                        selectedWarehouse);

                if (quantity > remainingCapacity) {
                    clearLabels(); 
                    labelAddProductToWarehouseSuccess.setText("");
                    label_errorMessageAddRemoveProducts.setText("Adding this quantity will exceed warehouse capacity.");
                } else {
                    // Proceed with adding the product to the Stored table
                    Stored stored = new Stored(product, warehouse, quantity);
                    StoredDAO.addProductToWarehouse(stored);
                    clearLabels(); 
                    updateDatabase();
                    storedTableView.refresh();
                    label_errorMessageAddRemoveProducts.setText("");
                    labelAddProductToWarehouseSuccess.setText("Products added to warehouse");
                }

            }
        } catch (SQLException e1) {
            if (e1.getErrorCode() == 2627) {
                label_errorMessageAddRemoveProducts
                        .setText("Product already exists in this warehouse, press update instead");
                labelAddProductToWarehouseSuccess.setText("");
            }
        } catch (NumberFormatException e2) {
            label_errorMessageAddRemoveProducts.setText("Please enter a valid quantity.");
        }

    }



    @FXML
    public void button_UpdateProductFromWarehouse_OnClick(ActionEvent event) throws SQLException {
        try {
            String selectedProduct = ComboBoxChooseProduct.getValue();
            String selectedWarehouse = ComboBoxChooseWarehouse.getValue();
            String stockString = textFieldEnterQuantity.getText();

            if (selectedProduct == null || selectedWarehouse == null || stockString.isEmpty()) {
                clearLabels(); 
                label_errorMessageAddRemoveProducts
                        .setText("Please select a product, a warehouse, and enter a valid quantity.");

            } else if (Integer.parseInt(stockString) < 0) {
                clearLabels();
                label_errorMessageAddRemoveProducts.setText("Stock cannot be negative.");

            } else {
                int stock = Integer.parseInt(stockString);

                Product product = ProductDAO.getProductById(selectedProduct);
                Warehouse warehouse = WarehouseDAO.getWarehouseById(selectedWarehouse);

                // Calculate the remaining capacity in the warehouse
                int remainingCapacity = StoredDAO.calculateRemainingCapacity(warehouse, selectedProduct,
                        selectedWarehouse);

                 if (stock > remainingCapacity) {
                    clearLabels(); 
                    labelAddProductToWarehouseSuccess.setText("");
                    label_errorMessageAddRemoveProducts.setText("Adding this quantity will exceed warehouse capacity.");

                } else {

                    Stored stored = new Stored(product, warehouse, stock);
                    StoredDAO.updateProductFromWarehouse(stored);
                    clearLabels(); 
                    updateDatabase();
                    storedTableView.refresh();
                    label_errorMessageAddRemoveProducts.setText("");
                    labelAddProductToWarehouseSuccess.setText("Stock successfully updated");
                    label_errorMessageAddRemoveProducts.setText("");
                }
            }
        } catch (NumberFormatException e) {
            label_errorMessageAddRemoveProducts.setText("Please enter a valid quantity.");
        }
    }

    @FXML
    private void updateDatabase() {

        StoredDAO.updateStoredItemsFromDatabase();
        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(StoredDAO.getStoredItems());

    }

    @FXML 
    public void calculateTotalStockInWarehouse() {
        
        Warehouse selectedWarehouse = warehouseTableView.getSelectionModel().getSelectedItem();
        StoredDAO.getTotalStockInWarehouse(selectedWarehouse); 
        labelTotalStock.setText("Total stock: " + StoredDAO.getTotalStockInWarehouse(selectedWarehouse));
        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(StoredDAO.getStoredInfoWithWarehouse(selectedWarehouse));
        calculateCapacity(selectedWarehouse);
        labelTotalStock.setVisible(true);
        labelProductAmount.setVisible(true);
    }



    @FXML
    private void clearAddRemoveProductFields() {
        ComboBoxChooseProduct.getSelectionModel().clearSelection();
        ComboBoxChooseWarehouse.getSelectionModel().clearSelection();
        textFieldEnterQuantity.clear();
    }

    @FXML
    private void button_removeProductFromStoredTable_Clicked(ActionEvent event) {
        try {
            String selectedProduct = ComboBoxChooseProduct.getValue();

            if (selectedProduct == null) {
                clearLabels(); 
                labelAddProductToWarehouseSuccess.setText("");
                label_errorMessageAddRemoveProducts.setText("Please select a product to remove.");

            } else {

                Stored product = StoredDAO.getStoredProductById(selectedProduct);
                StoredDAO.removeProductFromStoredTable(product);

                labelAddProductToWarehouseSuccess.setText("Product was successfully removed");
                label_errorMessageAddRemoveProducts.setText("");
                clearLabels(); 
                updateDatabase();
                storedTableView.refresh();
            }
        } catch (SQLException e1) {
            clearLabels();
            labelAddProductToWarehouseSuccess.setText("");
            label_errorMessageAddRemoveProducts.setText(e1.getMessage());
        } catch (Exception e2) {
            clearLabels();
            labelAddProductToWarehouseSuccess.setText("");
            label_errorMessageAddRemoveProducts.setText("Product does not exist in warehouse");
        } 
          
    }

    @FXML
    private void clearLabels() {
        labelAddProductToWarehouseSuccess.setText("");
        label_errorMessageAddRemoveProducts.setText("");
        labelTotalStock.setText("");
        label_totalProductStock.setText(""); 
        labelAddWarehouseError.setText("");
    }

    public static void refreshStoredTable() {
        StoredDAO.updateStoredItemsFromDatabase();

    }

    // Method to update the warehouse ComboBox
    public void updateWarehouseComboBox() {
        warehouseIds.clear();
        warehouseIds.addAll(WarehouseDAO.getWarehouses().stream()
                .map(Warehouse::getWarehouseId)
                .collect(Collectors.toList()));
        ComboBoxChooseWarehouse.setItems(warehouseIds);
    }

}
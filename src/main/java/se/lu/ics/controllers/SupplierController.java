package se.lu.ics.controllers;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import se.lu.ics.models.Supplier;
import se.lu.ics.data.ProductDAO;
import se.lu.ics.data.SupplierDAO;
import se.lu.ics.models.Product;

public class SupplierController {

    @FXML
    private Label label_supplierSearchField;

    @FXML
    private Label label_supplierUpdateInfo;

    @FXML
    private Label label_supplierAddInfo;

    @FXML
    private Label label_supplierId;

    @FXML
    private Label label_supplierProductInfo;

    @FXML
    private Label label_supplierAddress;

    @FXML
    private Label label_supplierName;

    @FXML
    private Label label_errorMessage;

    @FXML
    private Label label_supplierRemove;

    @FXML
    private Label label_supplierEmail;

    @FXML
    private Label label_successMessage;

    @FXML
    private TextField textField_supplierSearchField;

    @FXML
    private TextField textField_supplierId;

    @FXML
    private TextField textField_supplierAddress;

    @FXML
    private TextField textField_supplierEmail;

    @FXML
    private TextField textField_supplierName;

    @FXML
    private TableView<Supplier> tableView_supplier;

    @FXML
    private TableView<Product> tableView_supplierProductList;

    @FXML
    private TableColumn<Product, String> tableColumn_supplierProductId;

    @FXML
    private TableColumn<Product, String> tableColumn_supplierProductName;

    @FXML
    private TableColumn<Supplier, String> column_supplierId;

    @FXML
    private TableColumn<Supplier, String> column_supplierName;

    @FXML
    private TableColumn<Supplier, String> column_supplierAddress;

    @FXML
    private TableColumn<Supplier, String> column_supplierEmail;

    @FXML
    private Button button_addSupplier;

    @FXML
    private Button button_removeSupplier;

    @FXML
    private Button button_updateSupplier;

    // Define a FilteredList to filter the suppliers
    private FilteredList<Supplier> filteredSuppliers;

    // method for initializing the tableview
    public void initialize() {
        column_supplierId.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierId"));
        column_supplierName.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierName"));
        column_supplierAddress.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierAddress"));
        column_supplierEmail.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierEmail"));

        tableView_supplier.setItems(SupplierDAO.getSuppliers());

        // Create a FilteredList and bind it to the original list of suppliers
        filteredSuppliers = new FilteredList<>(SupplierDAO.getSuppliers(), p -> true);
        tableView_supplier.setItems(filteredSuppliers);

        // Add an event listener to the textField_supplierSearchField for filtering
        textField_supplierSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredSuppliers.setPredicate(supplier -> {
                // If the search field is empty, show all suppliers
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convert the search text to lowercase for case-insensitive filtering
                String searchText = newValue.toLowerCase();

                // Check if the supplier's name or any of its products' IDs contain the search
                // text
                if (supplier.getSupplierName().toLowerCase().contains(searchText)) {
                    return true;
                }

                if (supplier.getSupplierId().toLowerCase().contains(searchText)) {
                    return true;
                }

                return false; // No match found
            });
        });

        // Products table
        tableColumn_supplierProductId.setCellValueFactory(new PropertyValueFactory<Product, String>("productId"));
        tableColumn_supplierProductName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));

        tableView_supplierProductList.getItems().addAll(ProductDAO.getProducts());

        // Add an event listener for selecting a supplier in tableView_supplier
        tableView_supplier.getSelectionModel().selectedItemProperty().addListener((obs, oldSupplier, newSupplier) -> {
            if (newSupplier != null) {
                // Populate tableView_supplierProductList with the products supplied by the
                // selected supplier
                ObservableList<Product> suppliedProducts = newSupplier.getSuppliedProducts();
                tableView_supplierProductList.setItems(suppliedProducts);
            } else {
                // Clear tableView_supplierProductList if no supplier is selected
                tableView_supplierProductList.getItems().clear();
            }
        });

        // Add an event listener for selecting a supplier in tableView_supplier
        tableView_supplier.getSelectionModel().selectedItemProperty().addListener((obs, oldSupplier, newSupplier) -> {
            if (newSupplier != null) {
                // Populate text fields with the selected supplier's information
                // textField_supplierName.setText(newSupplier.getSupplierName());
                // textField_supplierAddress.setText(newSupplier.getSupplierAddress());
                // textField_supplierEmail.setText(newSupplier.getSupplierEmail());

                // Populate tableView_supplierProductList with the products supplied by the
                // selected supplier
                ObservableList<Product> suppliedProducts = newSupplier.getSuppliedProducts();
                tableView_supplierProductList.setItems(suppliedProducts);
            } else {
                // Clear text fields and tableView_supplierProductList if no supplier is
                // selected
                textField_supplierName.clear();
                textField_supplierAddress.clear();
                textField_supplierEmail.clear();
                tableView_supplierProductList.getItems().clear();
            }
        });

    }

    // Search for supplier by id method
    public void supplierSearchById() {
        String supplierId = textField_supplierSearchField.getText().toLowerCase();
        for (Supplier supplier : SupplierDAO.getSuppliers()) {
            tableView_supplier.getSelectionModel().select(supplier);
            if (supplier.getSupplierId().toLowerCase().equals(supplierId)) {

            }

        }

    }

    @FXML
    public void button_addSupplier_OnClick() {
        try {
            String supplierId = textField_supplierId.getText();
            String name = textField_supplierName.getText();
            String address = textField_supplierAddress.getText();
            String email = textField_supplierEmail.getText();

            if (supplierId.isEmpty() || name.isEmpty() || address.isEmpty() || email.isEmpty()) {
                label_successMessage.setText("");
                label_errorMessage.setText("Please fill in all fields");
            
            } else {
                SupplierDAO.addSupplierToDatabase(name, supplierId, address, email);
                label_errorMessage.setText("");
                label_successMessage.setText("Supplier added successfully");
            }

        } catch (SQLException e1) {
            if (e1.getErrorCode() == 2627) {
                label_successMessage.setText("");
                label_errorMessage.setText("Supplier with this ID already exists");
                System.out.println("Supplier with this ID already exists");
            }
            if (e1.getErrorCode() == 0) {
                label_successMessage.setText("");
                label_errorMessage.setText("Connection to database lost");
            }
            e1.printStackTrace(); // Add this line to print the exception details
        } catch (Exception e) {
            e.printStackTrace(); // Add this line to print the exception details
            label_errorMessage.setText("Something went wrong, please try again: " + e.getMessage());
        }
        
    }

    @FXML
    public void button_removeSupplier_OnClick() {
        try {
            Supplier supplier = tableView_supplier.getSelectionModel().getSelectedItem();
            if (supplier == null) {
                label_errorMessage.setText("Error: Please choose a supplier from the table above");
            
            } else if (SupplierDAO.hasProducts(supplier)) {
                clearLabels();
                label_errorMessage.setText("Error: Supplier has products, please remove them first");

            } else {
                // Remove the selected supplier from the data source (assuming SupplierDAO
                // handles this)
                SupplierDAO.removeSupplierFromDatabase(supplier);

                // Remove the supplier from the table view and refresh it
                tableView_supplier.getItems().remove(supplier);
                tableView_supplier.refresh(); // Refresh the table view
                clearLabels();
            }
        } catch (SQLException e) {
            label_errorMessage.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void button_updateSupplier_OnClick() {
        Supplier selectedSupplier = tableView_supplier.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null) {
            label_errorMessage.setText("Error: Please choose a supplier from the table above");
            return;
        }

        try {
            // Get the updated values from the text fields
            String name = textField_supplierName.getText();
            String address = textField_supplierAddress.getText();
            String email = textField_supplierEmail.getText();

            // Update the selected supplier's information
            selectedSupplier.setSupplierName(name);
            selectedSupplier.setSupplierAddress(address);
            selectedSupplier.setSupplierEmail(email);

            // Update the supplier in the data source (assuming SupplierDAO handles this)
            SupplierDAO.updateSupplierInDatabase(selectedSupplier);

            // Refresh the table view to reflect the changes
            tableView_supplier.refresh();

            clearTextFields();

            // Display a success message
            label_errorMessage.setText("Supplier updated successfully");
        } catch (Exception e) {
            label_errorMessage.setText("Error: Please make sure you have proper data inputs in all fields");
        }
    }

    private void clearTextFields() {
        textField_supplierId.clear();
        textField_supplierName.clear();
        textField_supplierAddress.clear();
        textField_supplierEmail.clear();
    }

    private void clearLabels() {
        label_errorMessage.setText("");
        label_successMessage.setText("");
    }

}

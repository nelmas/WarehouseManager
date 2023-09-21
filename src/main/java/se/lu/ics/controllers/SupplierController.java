package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import se.lu.ics.models.Supplier;
import se.lu.ics.data.SupplierDAO;
import se.lu.ics.data.WarehouseDAO;
import se.lu.ics.models.Product;
import se.lu.ics.models.Stored;
import se.lu.ics.models.Warehouse;

public class SupplierController {

    @FXML private Label label_supplierSearchField;

    @FXML private Label label_supplierAddInfo;

    @FXML private Label label_supplierId;

    @FXML private Label label_supplierProductInfo;

    @FXML private Label label_supplierAddress;

    @FXML private Label label_supplierName;

    @FXML private Label label_errorMessage;

    @FXML private Label label_supplierRemove;

    @FXML private Label label_supplierEmail;

    @FXML private TextField textField_supplierSearchField;

    @FXML private TextField textField_supplierId;

    @FXML private TextField textField_supplierAddress;

     @FXML private TextField textField_supplierEmail;

    @FXML private TextField textField_supplierName;

    @FXML private TableView<Supplier> tableView_supplier;

    @FXML private TableView<Supplier> tableView_supplierProductList;

    @FXML private TableColumn<Supplier, String> column_supplierId;

    @FXML private TableColumn<Supplier, String> tableColumn_supplierProductList;

    @FXML private TableColumn<Supplier, String> column_supplierName;

    @FXML private TableColumn<Supplier, String> column_supplierAddress;

    @FXML private TableColumn<Supplier, String> column_supplierEmail;

    @FXML private Button button_addSupplier;

    @FXML private Button button_removeSupplier;

    //method for initializing the tableview
    public void initialize() {        
        column_supplierId.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierId"));
        column_supplierName.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierName"));
        column_supplierAddress.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierAddress"));
        column_supplierEmail.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierEmail"));

        tableView_supplier.setItems(SupplierDAO.getSuppliers());
    }

    //Search for supplier by id method 
    public void supplierSearchById () {
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

        SupplierDAO.addSupplierToDatabase(name, supplierId, address, email);

        clearTextFields();

        } catch (Exception e1) {
			label_errorMessage.setText("Error: Please make sure you have proper data inputs in all fields");
        }
    }

    @FXML
    public void button_removeSupplier_OnClick(){
        Supplier supplier = tableView_supplier.getSelectionModel().getSelectedItem();
		if (supplier == null) {
			label_errorMessage.setText("Error: Please choose a supplier from the table above");
		} else {
            tableView_supplier.getItems().remove(supplier);

            // Remove the selected supplier from the data source (assuming SupplierDAO handles this)
            SupplierDAO.removeSupplierFromDatabase(supplier);
        }
    }

    private void clearTextFields() {
        textField_supplierId.clear();
        textField_supplierName.clear();
        textField_supplierAddress.clear();
        textField_supplierEmail.clear();
    }
    
    
}


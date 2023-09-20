package se.lu.ics.controllers;

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

    @FXML private Label label_supplierAddRemoveInfo;

    @FXML private Label label_supplierId;

    @FXML private Label label_supplierAddress;

    @FXML private Label label_supplierCapacity;

    @FXML private Label label_errorMessage;

    @FXML private TextField textField_supplierSearchField;

    @FXML private TextField textField_supplierId;

    @FXML private TextField textField_supplierAddress;

    @FXML private TextField textField_supplierCapacity;

    @FXML private TableView<Supplier> tableView_supplier;

    @FXML private TableColumn<Supplier, String> column_supplierId;

    @FXML private TableColumn<Supplier, String> column_supplierName;

    @FXML private TableColumn<Supplier, String> column_supplierAddress;

    @FXML private TableColumn<Supplier, String> column_supplierEmail;

    @FXML private Button button_addSupplier;

    @FXML private Button button_removeSupplier;

    //method for initializing the tableview
    public void initialize() {        
        column_supplierId.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierId"));
        column_supplierName.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierName"));
        column_supplierAddress.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
        column_supplierEmail.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));

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

    public void button_removeSupplier() {
        //TODO
    }

    public void button_addSupplier() {
        //TODO
    }
    
}


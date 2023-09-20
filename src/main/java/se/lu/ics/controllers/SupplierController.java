package se.lu.ics.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import se.lu.ics.models.Supplier;
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

    @FXML private TableColumn<Supplier, Double> column_supplierCapacity;

    @FXML private Button button_addSupplier;

    @FXML private Button button_removeSupplier;

    public void button_removeSupplier() {
        //TODO
    }

    public void button_addSupplier() {
        //TODO
    }
    
}


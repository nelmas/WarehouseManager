package se.lu.ics.controllers;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import se.lu.ics.models.Product;
import se.lu.ics.models.Stored;
import se.lu.ics.models.Supplier;
import se.lu.ics.data.ExcelHandler;
import se.lu.ics.data.MetadataDAO;
import se.lu.ics.data.ProductDAO;
import se.lu.ics.data.StoredDAO;
import se.lu.ics.data.SupplierDAO;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleIntegerProperty;

public class MetaDataController {

    @FXML
    private TableColumn<Product, String> columnProductId;
    @FXML
    private TableColumn<Product, String> columnProductName;
    @FXML
    private TableColumn<Product, String> columnProductCategory;
    @FXML
    private TableColumn<Product, Supplier> columnProductSupplierId;
    @FXML
    TableColumn<String, String> tableNameColumn = new TableColumn<>("Table Name");
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableView<String> metadataNamesOfAllTables;
    @FXML
    private TableView<String> columnNamesTable;
    @FXML
    private TableView<String> columnTypesTable;
    @FXML
    private TableView<String> metadataNameOfAllPrimaryKeys;
    @FXML
    private TableColumn<String, String> primaryKeyColumn;
    @FXML
    private TableView<String> metadataNamesOfAllForeignKeys;
    @FXML
    private TableColumn<String, String> foreignKeyColumn;
    @FXML
    private TableColumn<String, String> productColumnNamesColumn;
    @FXML
    private TableView<String> metadataNamesOfAllColumnsInProductTable;
    @FXML
    private TableColumn<String, String> mostRowsTableColumn;
    @FXML
    private TableColumn<String, String> mostRowsRowAmountColumn;
    @FXML
    private TableView<String> metadataTableWithMostRows;
    @FXML
    private TextArea metadataMostRowsTextArea;
    @FXML
    private Button buttonOpenExcel;

    public void initialize() {
        // TABLES NAME INTO METADATA TABLE
        // Set up the columns in the table
        tableNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        // Retrieve table names from the MetadataDAO
        ObservableList<String> tableNames = MetadataDAO.getAllTableNames();
        // Populate the TableView with table names
        metadataNamesOfAllTables.setItems(tableNames);
        // NAMES OF ALL PRIMARY KEYS INTO METADATA TABLE
        primaryKeyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        metadataNameOfAllPrimaryKeys.setItems(MetadataDAO.getPrimaryKeyNamesForTable());
        // NAMES OF ALL FOREIGN KEYS INTO METADATA TABLE
        foreignKeyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        metadataNamesOfAllForeignKeys.setItems(MetadataDAO.getForeignKeysForTable());
        // PRODUCT COLUMN NAMES INTO METADATA TABLE
        productColumnNamesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        ObservableList<String> productColumnNames = MetadataDAO.getProductColumn();
        metadataNamesOfAllColumnsInProductTable.setItems(productColumnNames);
        // MOST ROWS INTO METADATA TABLE
        metadataMostRowsTextArea.setText(MetadataDAO.getTableWithMostRows());

        
    }
    // OPEN EXCEL-FILE
    @FXML
        public void openExcel_OnClick() {
                ExcelHandler.openExcelFile();
        }
}
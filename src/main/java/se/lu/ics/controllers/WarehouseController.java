package se.lu.ics.controllers;

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

    // Stored
    @FXML
    private TableView<Stored> storedTableView;
    @FXML
    private TableColumn<Stored, String> StoredProductIdColumn;;
    @FXML
    private TableColumn<Stored, String> StoredProductNameColumn;
    @FXML
    private TableColumn<Stored, Integer> StoredStockColumn;

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
        storedTableView.getItems().addAll(StoredDAO.getStoredItems());

        // Add listener to warehouse table
        warehouseTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showProductsFromWarehouse();
            }
        });
    }

    // //Search for warehouse by id method
    // public void searchWarehouseById () {
    // String warehouseId = warehouseSearchtextField.getText().toLowerCase();
    // for (Warehouse warehouse : WarehouseDAO.getWarehouses()) {
    // if (warehouse.getWarehouseId().toLowerCase().equals(warehouseId)) {
    // warehouseTableView.getSelectionModel().select(warehouse);

    // return;
    // }
    // warehouseTableView.getSelectionModel().clearSelection();
    // }

    // }

    // method that shows products based on the warehouse i press
    public void showProductsFromWarehouse() {
        Warehouse selectedWarehouse = warehouseTableView.getSelectionModel().getSelectedItem();
        storedTableView.getItems().clear();
        storedTableView.getItems().addAll(StoredDAO.getStoredInfoWithWarehouse(selectedWarehouse));
    }
}

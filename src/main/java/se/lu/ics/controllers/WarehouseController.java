package se.lu.ics.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import se.lu.ics.data.WarehouseDAO;
import se.lu.ics.models.Warehouse;

public class WarehouseController {

    // Warehouse
    @FXML
    private AnchorPane warehousePane;
    @FXML
    private TableView<Warehouse> tableViewWarehouse;
    @FXML
    private TableColumn<Warehouse, String> warehouseIdColumn;
    @FXML
    private TableColumn<Warehouse, String> warehouseCapacityColumn;
    @FXML
    private TableColumn<Warehouse, String> warehouseAddressColumn;
    @FXML
    private TextField warehouseSearchTextField;
    @FXML
    private Button warehouseAddButton;

    public void initialize() {
        warehouseIdColumn.setCellValueFactory(new PropertyValueFactory<>("warehouseId"));
        warehouseCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("warehouseCapacity"));
        warehouseAddressColumn.setCellValueFactory(new PropertyValueFactory<>("warehouseAddress"));

        tableViewWarehouse.setItems(WarehouseDAO.getWarehouses());

        warehousePane.setVisible(false);
    }
}

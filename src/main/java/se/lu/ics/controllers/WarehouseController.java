package se.lu.ics.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import se.lu.ics.data.WarehouseDAO;
import se.lu.ics.models.Warehouse;

public class WarehouseController {
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
   

    public void initialize() {        
        warehouseIdColumn.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("warehouseId"));
        warehouseAddressColumn.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("warehouseAddress"));
        warehouseCapacityColumn.setCellValueFactory(new PropertyValueFactory<Warehouse, Integer>("warehouseCapacity"));

        warehouseTableView.setItems(WarehouseDAO.getWarehouses());
    }

    
    //Search for warehouse by id method 
    public void searchWarehouseById () {
    String warehouseId = warehouseSearchtextField.getText().toLowerCase(); 
    for (Warehouse warehouse : WarehouseDAO.getWarehouses()) {
        warehouseTableView.getSelectionModel().select(warehouse); 
        if (warehouse.getWarehouseId().toLowerCase().equals(warehouseId)) {
            
            
        }
    
    }  

}
}


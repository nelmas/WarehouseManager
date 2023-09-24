package se.lu.ics.models;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.data.StoredDAO;

public class Warehouse {

    // Attributes
    private String warehouseId;
    private String warehouseAddress;
    private int warehouseCapacity;

    private ObservableList<Stored> stockList = FXCollections.observableArrayList();

    // Constructor
    public Warehouse(String warehouseId, String warehouseAddress, int warehouseCapacity) {
        this.warehouseId = warehouseId;
        this.warehouseAddress = warehouseAddress;
        this.warehouseCapacity = warehouseCapacity;
    }

    // Getters and Setters
    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public int getWarehouseCapacity() {
        return warehouseCapacity;
    }




    public void setCapacity(int warehouseCapacity) {
        this.warehouseCapacity = warehouseCapacity;
    }

    public void addStock(Stored stock) {
        this.stockList.add(stock);
    }

    public ObservableList<Stored> getStockList() {
        return stockList;
    }

}
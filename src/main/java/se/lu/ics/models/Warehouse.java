package se.lu.ics.models;

public class Warehouse {

    // Attributes
    private String warehouseId;
    private String warehouseAddress;
    private int warehouseCapacity;

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
    
}
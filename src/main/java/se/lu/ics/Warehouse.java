package se.lu.ics;

public class Warehouse {

    // Attributes warehouseId, Address, Capacity
    private int warehouseId;
    private String address;
    private int capacity;

    // Constructor
    public Warehouse(int warehouseId, String address, int capacity) {
        this.warehouseId = warehouseId;
        this.address = address;
        this.capacity = capacity;
    }

    // Getters and Setters
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int id) {
        this.warehouseId = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }  

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
}
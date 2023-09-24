package se.lu.ics.models;

import javafx.collections.ObservableList;
import se.lu.ics.data.SupplierDAO;

public class Supplier {
    private String supplierName;
    private String supplierId;
    private String address;
    private String email;

    // Constructor
    public Supplier(String supplierName, String supplierId, String address, String email) {
        this.supplierName = supplierName;
        this.supplierId = supplierId;
        this.address = address;
        this.email = email;
    }

    //Axels tillagda kod
    @Override
    public String toString() {
        return supplierId;
    }

    // Getters and Setters
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierAddress() {
        return address;
    }

    public void setSupplierAddress(String address) {
        this.address = address;
    }

    public String getSupplierEmail() {
        return email;
    }

    public void setSupplierEmail(String email) {
        this.email = email;
    }

    // Implement the getSuppliedProducts() method
    public ObservableList<Product> getSuppliedProducts() {
        // Call the SupplierDAO method to retrieve the products supplied by this supplier
        return SupplierDAO.getSuppliedProducts(this);
    }

    public String getId() {
        return null;
    }

}


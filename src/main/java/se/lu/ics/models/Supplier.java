package se.lu.ics.models;


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

}


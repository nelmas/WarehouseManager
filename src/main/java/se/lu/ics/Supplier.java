package se.lu.ics;

public class Supplier {
    private String name;
    private int supplierId;
    private String address;
    private String email;

    // Constructor
    public Supplier(String name, int supplierId, String address, String email) {
        this.name = name;
        this.supplierId = supplierId;
        this.address = address;
        this.email = email;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}


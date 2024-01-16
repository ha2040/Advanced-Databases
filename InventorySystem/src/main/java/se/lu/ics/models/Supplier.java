package se.lu.ics.models;

public class Supplier {
    private String supplierId;
    private String supplierName;
    private String supplierAddress;
    private String contactName;
    private String contactPhoneNbr;
    private Product product;

    public Supplier(String supplierId, String supplierName, String supplierAddress, String contactName,
            String contactPhoneNbr, Product product) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.contactName = contactName;
        this.contactPhoneNbr = contactPhoneNbr;
        this.product = product;
    }
    public String getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    public String getSupplierAddress() {
        return supplierAddress;
    }
    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }
    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getContactPhoneNbr() {
        return contactPhoneNbr;
    }
    public void setContactPhoneNbr(String contactPhoneNbr) {
        this.contactPhoneNbr = contactPhoneNbr;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    
}

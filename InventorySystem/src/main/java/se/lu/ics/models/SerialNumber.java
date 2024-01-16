package se.lu.ics.models;

public class SerialNumber {
    private String serialId;
    private Product product;
  
    public SerialNumber(String serialId, Product product) {
        this.serialId = serialId;
        this.product = product;
    }
    public String getSerialId() {
        return serialId;
    }
    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    
}

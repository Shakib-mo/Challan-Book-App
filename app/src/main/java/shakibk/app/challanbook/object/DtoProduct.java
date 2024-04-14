package shakibk.app.challanbook.object;

import java.io.Serializable;

public class DtoProduct implements Serializable {
    public String productNama;
    public float totalItemPrice;
    public float price;
    public int quantity;
    public int itemNo;

    public DtoProduct(){

    }

    public DtoProduct(String productName, float price, int quantity){
        this.price = price;
        this.productNama = productName;
        this.quantity = quantity;
    }

    public float getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(float totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public String getProductNama() {
        return productNama;
    }

    public float getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }
}

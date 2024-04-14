package shakibk.app.challanbook.object;

public class TotalQtyPrice {
    public int totalItem;
    public long totalPrice;
    public int totalQty;

    public TotalQtyPrice(){

    }
    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public long getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }
}

package shakibk.app.challanbook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import shakibk.app.challanbook.object.DtoCustomer;
import shakibk.app.challanbook.object.DtoProduct;
import shakibk.app.challanbook.object.TotalQtyPrice;

public class Invoice implements Serializable {

    public int billNo;
    public String shopName;  //Abstract
    public String shopEmail;
    public String shopMobile;
    public String shopAddress;
    public String shopStreet;
    public String shopState;
    public String shopCity;
    public String shopPostCode;
    public String billData;
    public String docID;


    public DtoCustomer dtoCustomer = new DtoCustomer();
    public TotalQtyPrice totalQtyPrice = new TotalQtyPrice();
    public List<DtoProduct> dtoProducts = new ArrayList<>();

    public Invoice(){
    }
    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public void setBillData(String billData) {
        this.billData = billData;
    }

    public String getBillData() {
        return billData;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public int getBillNo() {
        return billNo;
    }
}

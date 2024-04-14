package shakibk.app.challanbook;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import shakibk.app.challanbook.adapter.DtoCustomerAdapter;
import shakibk.app.challanbook.adapter.DtoProductAdapter;
import shakibk.app.challanbook.object.DtoCustomer;
import shakibk.app.challanbook.object.DtoProduct;
import shakibk.app.challanbook.object.DtoShopInfo;
public class Sell_Act extends AppCompatActivity {

    public TextView time_date,te_name,te_mobile,total_product,teSrNo;
    public String CoustomerEmail,CoustomerAddress;
    private DatePickerDialog datePickerDialog;
    public Button bt_select,bt_addProduct;
    public ImageView bt_back,bt_saveAllDtl,btSimpleTost;
    public BottomSheetDialog bottomSheetDialog;

    DtoCustomerAdapter customerAdapter;
    List<DtoCustomer> listCustomer;
    DtoCustomer dtoCustomer;
    List<DtoProduct> listProduct = new ArrayList<>();

    RecyclerView recyclerProduct;
    DtoProductAdapter productAdapter;
    Invoice invoice;
    float i = 1;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        intDatePicker();
        time_date = findViewById(R.id.time_date);
        time_date.setText(getTodatDate());
        bt_select = findViewById(R.id.bt_select);
        te_name = findViewById(R.id.te_name);
        te_mobile = findViewById(R.id.te_mobile);
        bt_addProduct = findViewById(R.id.bt_addProduct);
        bt_back = findViewById(R.id.image_back);
        recyclerProduct = findViewById(R.id.recycler_viewPro);
        bt_saveAllDtl = findViewById(R.id.image_save);
        total_product = findViewById(R.id.total_product);
        teSrNo = findViewById(R.id.te_sr_number);
        btSimpleTost = findViewById(R.id.imageSaveSimple);
        listCustomer = MyApplication.getIntence().getCustomerList();
        dtoCustomer = new DtoCustomer();
        invoice = new Invoice();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            invoice = (Invoice) bundle.getSerializable("value");
            Toast.makeText(this, invoice.toString(), Toast.LENGTH_SHORT).show();

        }
        teSrNo.setText(String.valueOf(MyApplication.getIntence().mapIncoice.size()+i));
        btSimpleTost.setOnClickListener(v -> Toast.makeText(Sell_Act.this, "Fill Your Product", Toast.LENGTH_SHORT).show());
        bt_select.setOnClickListener(vi -> {
            bottomSheetDialog = new BottomSheetDialog(Sell_Act.this);
            bottomSheetDialog.setContentView(R.layout.add_contect_dtl);
            RecyclerView recyclCustomer = bottomSheetDialog.findViewById(R.id.recy_cler_view);
            Button add_con = bottomSheetDialog.findViewById(R.id.add_co);
            TextView totalCo = bottomSheetDialog.findViewById(R.id.total_con);
            bottomSheetDialog.show();

            if (listCustomer == null){
                listCustomer = new ArrayList<>();
            }else {
                listCustomer = MyApplication.getIntence().getCustomerList();
                totalCo.setText("Total Contects ("+listCustomer.size()+")");
                customerAdapter = new DtoCustomerAdapter(Sell_Act.this, listCustomer);
                recyclCustomer.setLayoutManager(new LinearLayoutManager(Sell_Act.this));
                recyclCustomer.setAdapter(customerAdapter);
                customerAdapter.notifyDataSetChanged();
            }
            if (te_name.getText().toString().isEmpty()){
                bt_addProduct.setEnabled(false);
            }else {
                bt_addProduct.setEnabled(true);

            }
            add_con.setOnClickListener(new View.OnClickListener() {
                @SuppressLint({"ResourceType", "SetTextI18n"})
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(Sell_Act.this);
                    dialog.setContentView(R.layout.fill_contect_xml);
                    dialog.setCancelable(false);
                    dialog.show();
                    dialog.getCurrentFocus();
                    EditText ed_name = dialog.findViewById(R.id.ed_name);
                    EditText ed_mobile = dialog.findViewById(R.id.ed_mobile);
                    EditText ed_email = dialog.findViewById(R.id.ed_email);
                    EditText ed_address = dialog.findViewById(R.id.ed_shop_addressLine);
                    Button bt_saveContect = dialog.findViewById(R.id.bt_save);
                    Button bt_cancle = dialog.findViewById(R.id.bt_cancle);

                    bt_saveContect.setOnClickListener(v1 -> {

                        dtoCustomer.setName(ed_name.getText().toString().trim());
                        dtoCustomer.setMobile(ed_mobile.getText().toString().trim());
                        dtoCustomer.setEmail(ed_email.getText().toString().trim());
                        dtoCustomer.setAddress(ed_address.getText().toString().trim());

                        if (dtoCustomer.name.isEmpty()){
                            ed_name.setError("Enter Name");
                            ed_name.requestFocus();
                            return;

                        }else if (dtoCustomer.mobile.isEmpty()){
                            ed_mobile.setError("Enter mobile Number");
                            ed_mobile.requestFocus();
                            return;
                        }else if (dtoCustomer.email.isEmpty()) {
                            ed_email.setError("Enter Email");
                            ed_email.requestFocus();
                            return;
                        }else if (dtoCustomer.address.isEmpty()) {
                            ed_address.setError("Enter Address");
                            ed_address.requestFocus();
                        }else {
                            MyApplication.getIntence().addCustomer(dtoCustomer);
                            listCustomer.add(dtoCustomer);
                            listCustomer = MyApplication.getIntence().getCustomerList();
                            totalCo.setText("Total Contects ("+listCustomer.size()+")");
                            customerAdapter = new DtoCustomerAdapter(Sell_Act.this, listCustomer);
                            recyclCustomer.setLayoutManager(new LinearLayoutManager(Sell_Act.this));
                            recyclCustomer.setAdapter(customerAdapter);
                            customerAdapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    });
                    bt_cancle.setOnClickListener(v14 -> {
                        dialog.cancel();
                        bottomSheetDialog.cancel();
                    });
                }
            });
        });
        bt_addProduct.setOnClickListener(v -> {
            Dialog dialog = new Dialog(Sell_Act.this);
            dialog.setContentView(R.layout.edit_text_add_product);
            EditText ed_product = dialog.findViewById(R.id.ed_product);
            EditText ed_price = dialog.findViewById(R.id.ed_price);
            EditText ed_quantity = dialog.findViewById(R.id.ed_quantity);
            Button bt_save = dialog.findViewById(R.id.bt_save);
            Button bt_cancle = dialog.findViewById(R.id.bt_cancle);
            bt_cancle.setOnClickListener(v12 -> dialog.cancel());
            bt_save.setOnClickListener(v13 -> {
                String product = ed_product.getText().toString().trim();
                String price = ed_price.getText().toString().trim();
                String quanitity = ed_quantity.getText().toString().trim();
                if (product.isEmpty()){
                    ed_product.setError("Enter Product");
                    ed_product.requestFocus();

                }else if (price.isEmpty()){
                    ed_price.setError("Enter Price");
                    ed_price.requestFocus();

                }else if (quanitity.isEmpty()){
                    ed_quantity.setError("Enter Quantity");
                    ed_quantity.requestFocus();
                }else {
                    float prices = Float.valueOf(price);
                    int qty = Integer.valueOf(quanitity);
                    DtoProduct dtoProduct = new DtoProduct(product,prices,qty);
                    listProduct.add(dtoProduct);
                    invoice.dtoProducts.add(dtoProduct);

                    invoice.totalQtyPrice.setTotalItem(listProduct.size());
                    float totalPrice = 0;
                    int totalQty = 0;
                    for (int i=0; i<listProduct.size();i++){
                        totalPrice += listProduct.get(i).price * listProduct.get(i).quantity;
                        totalQty += listProduct.get(i).quantity;
                    }
                    invoice.totalQtyPrice.setTotalPrice((int) totalPrice);
                    invoice.totalQtyPrice.setTotalQty(totalQty);

                    total_product.setText("Total ("+listProduct.size()+")");
                    productAdapter = new DtoProductAdapter(Sell_Act.this, listProduct);
                    recyclerProduct.setHasFixedSize(true);
                    recyclerProduct.setLayoutManager(new LinearLayoutManager(Sell_Act.this));
                    recyclerProduct.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();
                    dialog.cancel();
                    bt_saveAllDtl.setVisibility(View.VISIBLE);
                    btSimpleTost.setVisibility(View.GONE);
                }
            });
            dialog.show();
        });
        bt_back.setOnClickListener(v -> onBackPressed());
        bt_saveAllDtl.setOnClickListener(v -> saveInvoice());
    }
    private void saveInvoice(){
        invoice.setBillData(time_date.getText().toString());
        invoice.setBillNo(MyApplication.getIntence().mapIncoice.size()+1);
        invoice.dtoCustomer.setName(te_name.getText().toString());
        invoice.dtoCustomer.setMobile(te_mobile.getText().toString());
        invoice.dtoCustomer.setEmail(CoustomerEmail);
        invoice.dtoCustomer.setAddress(CoustomerAddress);
        DtoShopInfo dtoShopInfo = MyApplication.getIntence().getShopInfo();

        if (dtoShopInfo !=null){
            invoice.shopName = dtoShopInfo.getShopName();
            invoice.shopMobile = dtoShopInfo.getShopMobileNo();
            invoice.shopEmail = dtoShopInfo.getShopEmail();
            invoice.shopAddress = dtoShopInfo.getShopAddress();
            invoice.shopCity = dtoShopInfo.getShopCity();
            invoice.shopStreet = dtoShopInfo.getShopStreet();
            invoice.shopState = dtoShopInfo.getShopState();
            invoice.shopPostCode = dtoShopInfo.getShopPostCode();
        }
        MyApplication.getIntence().saveInvoice(invoice);


        Intent intent = new Intent(Sell_Act.this,Home_Act.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }

    private String getTodatDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month +1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }
    private void intDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            month = month +1;
            String date = makeDateString(day,month,year);
            time_date.setText(date);
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int styel = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,styel,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }
    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";
        return "JAN";
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

}
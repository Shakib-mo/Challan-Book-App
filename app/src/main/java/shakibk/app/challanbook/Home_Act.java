package shakibk.app.challanbook;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import shakibk.app.challanbook.adapter.InvoiceAdapter;
import shakibk.app.challanbook.object.DtoShopInfo;
public class Home_Act extends AppCompatActivity {

    ImageView image_menu;
    CardView card_sell,card_view;
    TextView te_shopName,te_shopAddress,te_shopStreet,te_shopCity,te_shopState,te_shopPostcode;

    DtoShopInfo dtoShopInfo;
    RecyclerView recyclerView;
    List<Invoice> listinvoice;
    InvoiceAdapter adapter;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        te_shopName = findViewById(R.id.te_shopName);
        te_shopAddress = findViewById(R.id.te_shopAddress);
        te_shopStreet = findViewById(R.id.te_shopStreet);
        te_shopCity = findViewById(R.id.te_shopCity);
        te_shopState = findViewById(R.id.te_shopState);
        te_shopPostcode = findViewById(R.id.te_shopPostcode);
        image_menu = findViewById(R.id.image_menu);
        card_sell = findViewById(R.id.card_sell);
        recyclerView = findViewById(R.id.recycler_view);
        card_view = findViewById(R.id.card_view);

        dtoShopInfo = MyApplication.getIntence().getShopInfo();
        listinvoice = MyApplication.getIntence().getInvoiceList();

        if (dtoShopInfo == null){
            dtoShopInfo = new DtoShopInfo();
        }else {
            te_shopName.setText(dtoShopInfo.getShopName());
            te_shopAddress.setText(dtoShopInfo.getShopAddress());
            te_shopStreet.setText(dtoShopInfo.getShopStreet());
            te_shopCity.setText(dtoShopInfo.getShopCity());
            te_shopState.setText(dtoShopInfo.getShopState());
            te_shopPostcode.setText(dtoShopInfo.getShopPostCode());
        }

        if (listinvoice ==null){
            listinvoice = new ArrayList<>();
        }else {
            adapter = new InvoiceAdapter(Home_Act.this,listinvoice);
            recyclerView.setLayoutManager(new LinearLayoutManager(Home_Act.this,LinearLayoutManager.HORIZONTAL,true));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        card_sell.setOnClickListener(v -> {
            Intent intent = new Intent(Home_Act.this,Sell_Act.class);
            startActivity(intent);
        });
        image_menu.setOnClickListener(v -> {
            Intent intent = new Intent(Home_Act.this,Setting_Act.class);
            startActivity(intent);
            finish();
        });

        card_view.setOnClickListener(v -> {
            Intent intent = new Intent(Home_Act.this,View_Activity.class);
            startActivity(intent);
        });
    }
}
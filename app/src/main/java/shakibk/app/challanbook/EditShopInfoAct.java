package shakibk.app.challanbook;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import shakibk.app.challanbook.object.DtoShopInfo;

public class EditShopInfoAct extends AppCompatActivity {

    ImageView bt_back,bt_save;
    EditText ed_shopName,ed_shopAddress,ed_shopStreet,ed_shopCity,ed_shopState,
            ed_shopPostcode,ed_shopMobil,ed_shopEmail;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    DtoShopInfo dtoShopInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_dtl);
        bt_back = findViewById(R.id.image_back);
        bt_save = findViewById(R.id.image_save);
        ed_shopName = findViewById(R.id.ed_shop_name);
        ed_shopAddress = findViewById(R.id.ed_shop_address);
        ed_shopStreet = findViewById(R.id.ed_shop_street);
        ed_shopCity = findViewById(R.id.ed_shop_city);
        ed_shopState = findViewById(R.id.ed_shop_state);
        ed_shopPostcode = findViewById(R.id.ed_shop_postCode);
        ed_shopMobil = findViewById(R.id.ed_shop_mobile);
        ed_shopEmail = findViewById(R.id.ed_shop_email);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        dtoShopInfo = MyApplication.getIntence().getShopInfo();

        if (dtoShopInfo == null) {
            dtoShopInfo = new DtoShopInfo();
            ed_shopName.setText("");
            ed_shopAddress.setText("");
            ed_shopStreet.setText("");
            ed_shopCity.setText("");
            ed_shopState.setText("");
            ed_shopPostcode.setText("");
            ed_shopMobil.setText(firebaseUser.getPhoneNumber());
            ed_shopEmail.setText("");
        }else {
            ed_shopName.setText(dtoShopInfo.getShopName());
            ed_shopAddress.setText(dtoShopInfo.getShopAddress());
            ed_shopStreet.setText(dtoShopInfo.getShopStreet());
            ed_shopCity.setText(dtoShopInfo.getShopCity());
            ed_shopState.setText(dtoShopInfo.getShopState());
            ed_shopPostcode.setText(dtoShopInfo.getShopPostCode());
            ed_shopMobil.setText(dtoShopInfo.getShopMobileNo());
            ed_shopEmail.setText(dtoShopInfo.getShopEmail());
        }

        bt_save.setOnClickListener(view -> {
            dtoShopInfo.setShopName(ed_shopName.getText().toString().trim());
            dtoShopInfo.setShopMobileNo(ed_shopMobil.getText().toString().trim());
            dtoShopInfo.setShopEmail(ed_shopEmail.getText().toString().trim());
            dtoShopInfo.setShopAddress(ed_shopAddress.getText().toString().trim());
            dtoShopInfo.setShopCity(ed_shopCity.getText().toString().trim());
            dtoShopInfo.setShopState(ed_shopState.getText().toString().trim());
            dtoShopInfo.setShopStreet(ed_shopStreet.getText().toString().trim());
            dtoShopInfo.setShopPostCode(ed_shopPostcode.getText().toString().trim());


            if (dtoShopInfo.shopName.isEmpty()){
                ed_shopName.setError("Enter Shop name");
                ed_shopName.requestFocus();
                return;

            }else  if (dtoShopInfo.shopMobileNo.isEmpty()){
                ed_shopMobil.setError("Enter Shop mo.number");
                ed_shopMobil.requestFocus();
                return;

            }else  if (dtoShopInfo.shopAddress.isEmpty()){
                ed_shopAddress.setError("Enter Shop address");
                ed_shopAddress.requestFocus();
                return;

            }
            MyApplication.getIntence().setShopInfo(dtoShopInfo);
            finish();
        });
        bt_back.setOnClickListener(view -> onBackPressed());
    }
}
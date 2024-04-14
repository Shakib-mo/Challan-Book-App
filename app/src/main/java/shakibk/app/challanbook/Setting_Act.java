package shakibk.app.challanbook;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Setting_Act extends AppCompatActivity {

    LinearLayout linear_edt_shop,linear_logOut;
    ImageView bt_back;
    FirebaseAuth firebaseAuth;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        linear_edt_shop = findViewById(R.id.linear_edt_shop);
        bt_back = findViewById(R.id.image_back_);
        linear_logOut = findViewById(R.id.linear_logOut);

        firebaseAuth = FirebaseAuth.getInstance();
        phone = firebaseAuth.getCurrentUser().getPhoneNumber();
        checkUserStatus();


        linear_logOut.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Setting_Act.this,AlertDialog.THEME_HOLO_DARK);
            builder.setTitle("Are you sure you want to LogOut this Contect"+"  "+phone);
            builder.setCancelable(false);
            builder.setIcon(R.drawable.warning);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                firebaseAuth.signOut();
                checkUserStatus();
                try {
                    Intent intent = new Intent(Setting_Act.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }catch (Exception e){
                    e.printStackTrace();                       }
            }).setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog mdialog = builder.create();
            mdialog.show();
        });

        linear_edt_shop.setOnClickListener(v -> {
            Intent intent = new Intent(Setting_Act.this, EditShopInfoAct.class);
            startActivity(intent);
        });

        bt_back.setOnClickListener(view -> {
            Intent intent = new Intent(Setting_Act.this,Home_Act.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }


    private void checkUserStatus() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser !=null){
            phone = firebaseUser.getPhoneNumber();
        }else {
            finish();
        }
    }
}
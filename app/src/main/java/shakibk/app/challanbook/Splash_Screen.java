package shakibk.app.challanbook;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Screen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            currentUser = mAuth.getCurrentUser();
        }
        new Handler().postDelayed(() -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user==null){
                Intent intent = new Intent(Splash_Screen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(Splash_Screen.this, Home_Act.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        },3000);
    }
}
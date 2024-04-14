package shakibk.app.challanbook;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;
public class MainActivity extends AppCompatActivity {

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private static final String TAG =" MAIN_TAG";
    public ProgressDialog pd;
    private LinearLayout linear_code_sent;
    private ConstraintLayout constraintLayout_number;
    private EditText ed_phone_number;
    private EditText ed_code;
    CardView bt_login;
    TextView te_codeSentDescription,te_resendCode;
    TextView text_view1,text_view2,text_view3;

    public CountryCodePicker countryCodePicker;
    private String contryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linear_code_sent = findViewById(R.id.linear_code_sent);
        constraintLayout_number = findViewById(R.id.constrant_layout1);
        ed_phone_number = findViewById(R.id.ed_phone_number);
        ed_code = findViewById(R.id.ed_code);
        bt_login = findViewById(R.id.bt_login);
        Button bt_codeSubmitBtn = findViewById(R.id.bt_codeSubmitBtn);
        te_codeSentDescription = findViewById(R.id.codeSentDescription);
        te_resendCode = findViewById(R.id.te_resendCode);
        text_view1 = findViewById(R.id.text_view1);
        text_view2 = findViewById(R.id.text_view2);
        text_view3 = findViewById(R.id.text_view3);
        countryCodePicker = findViewById(R.id.country_code);

        pd = new ProgressDialog(this);
        pd.setTitle("Please Wait......");
        pd.setCanceledOnTouchOutside(false);
        text_view1.setBackgroundResource(R.drawable.round_text_red);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredentiat(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, (MainActivity.this.forceResendingToken));
                Log.d(TAG, "onCodeSent: "+verificationId);
                mVerificationId = verificationId;
                forceResendingToken = token;
                pd.dismiss();

                constraintLayout_number.setVisibility(View.GONE);
                linear_code_sent.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,"Verification Code Sent.....", Toast.LENGTH_SHORT).show();
                te_codeSentDescription.setText("Please Enter the verification Code we sent\nto"+contryCode);

            }
        };
        bt_login.setOnClickListener(v -> {
            String phone= ed_phone_number.getText().toString().trim();
            if (phone.isEmpty()){
                ed_phone_number.setError("Please enter Phone Number");
                ed_phone_number.requestFocus();
            }else {
                constraintLayout_number.setVisibility(View.GONE);
                linear_code_sent.setVisibility(View.VISIBLE);
                text_view2.setBackgroundResource(R.drawable.round_text_red);
                startPhoneNumberVerification(phone);
            }
        });
        te_resendCode.setOnClickListener(v -> {
            String phone= ed_phone_number.getText().toString().trim();
            if (phone.isEmpty()){
                Toast.makeText(MainActivity.this, "Please enter Phone Number", Toast.LENGTH_SHORT).show();
            }else {
                resendVerificationCode(phone,forceResendingToken);
            }

        });
        bt_codeSubmitBtn.setOnClickListener(v -> {
            String code = ed_code.getText().toString().trim();
            if (code.isEmpty()|| code.length() <6){
                ed_code.setError("Invalid Code");
                ed_code.requestFocus();
            }else {
                text_view3.setBackgroundResource(R.drawable.round_text_red);
                verifyPhoneNumberWithCode(MainActivity.this.mVerificationId, code);
            }
        });
    }

    private void startPhoneNumberVerification(String phone) {
        pd.setMessage("Verifying Phone Number");
        pd.show();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(MyApplication.getIntence().firebaseAuth)
                .setPhoneNumber(contryCode = countryCodePicker.getSelectedCountryCodeWithPlus()+phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void resendVerificationCode(String phone,PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("Resending Code...");
        pd.show();
        contryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(MyApplication.getIntence().firebaseAuth)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setPhoneNumber(contryCode = countryCodePicker.getSelectedCountryCodeWithPlus()+phone)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(token)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        pd.setMessage("Verifying Code");
        pd.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        constraintLayout_number.setVisibility(View.VISIBLE);
        linear_code_sent.setVisibility(View.GONE);
        signInWithPhoneAuthCredentiat(credential);
    }
    private void signInWithPhoneAuthCredentiat(PhoneAuthCredential credential) {
        pd.setMessage("Loding In");

        MyApplication.getIntence().firebaseAuth.signInWithCredential(credential)

                .addOnSuccessListener((OnSuccessListener<AuthResult>) authResult -> {

                    pd.dismiss();
                    try {
                        String phone = MyApplication.getIntence().firebaseAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(MainActivity.this, "Logged In as " + phone, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Home_Act.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                })
                .addOnFailureListener((OnFailureListener) e -> {

                    pd.dismiss();
                    constraintLayout_number.setVisibility(View.GONE);
                    linear_code_sent.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Rong Password", Toast.LENGTH_SHORT).show();
                });
    }
}
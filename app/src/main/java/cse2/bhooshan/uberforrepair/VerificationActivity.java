package cse2.bhooshan.uberforrepair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText editTextPhone;
    private Button btnSend;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mAuth = FirebaseAuth.getInstance();
        ccp = findViewById(R.id.ccp);
        editTextPhone = findViewById(R.id.editTextPhone);
        btnSend = findViewById(R.id.btnSend);

        ccp.registerCarrierNumberEditText(editTextPhone);

        btnSend.setOnClickListener(v -> {
            String phoneNumber = ccp.getFullNumberWithPlus();
            if (!phoneNumber.isEmpty()) {
                sendVerificationCode(phoneNumber);
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification. In some cases, the phone number can be instantly verified without needing to send or enter a verification code.
                        // 2 - Auto-retrieval. On some devices, Google Play services can automatically detect the incoming verification SMS and perform verification without user action.
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made, for instance, if the phone number format is not valid.
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);
                        VerificationActivity.this.verificationId = verificationId;
                        String phoneNumber = ccp.getFullNumberWithPlus();
                        showOTPFragment(phoneNumber);
                    }

                });
    }

    private void showOTPFragment(String phoneNumber) {
        OTPFragment fragment = OTPFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerOTPFragment, fragment);
        transaction.commit();
    }
}

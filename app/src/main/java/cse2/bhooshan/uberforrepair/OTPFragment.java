package cse2.bhooshan.uberforrepair;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPFragment extends Fragment {

    private static final String ARG_VERIFICATION_ID = "verificationId";
    private static final String ARG_PHONE_NUMBER = "phoneNumber";
    private String verificationId;

    public OTPFragment() {
        // Required empty public constructor
    }

    public static OTPFragment newInstance(String verificationId, String phoneNumber) {
        OTPFragment fragment = new OTPFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VERIFICATION_ID, verificationId);
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static OTPFragment newInstance() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_o_t_p, container, false);
        EditText editTextOTP = view.findViewById(R.id.editTextOTP);
        Button btnVerifyOTP = view.findViewById(R.id.btnVerifyOTP);
        TextView phoneNumberTextView = view.findViewById(R.id.textView11);

        if (getArguments() != null) {
            verificationId = getArguments().getString(ARG_VERIFICATION_ID);
            String phoneNumber = getArguments().getString(ARG_PHONE_NUMBER, ""); // Default to empty if not found
            phoneNumberTextView.setText(phoneNumber);
        }

        btnVerifyOTP.setOnClickListener(v -> {
            String code = editTextOTP.getText().toString().trim();
            if (!code.isEmpty()) {
                verifyCode(code);
            }
        });

        return view;
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getActivity(), RegActivity.class); // Adjust with your target activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        // Handle the error
                    }
                });
    }
}

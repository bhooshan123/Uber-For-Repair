package cse2.bhooshan.uberforrepair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class OTPFragment extends Fragment {
    EditText etotp;
    Button votp;

    String message = "is your verification code.";
    String etPhone;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OTPFragment() {
        // Required empty public constructor
    }

    public static OTPFragment newInstance(String param1, String param2) {
        OTPFragment fragment = new OTPFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_o_t_p, container, false);
        etotp = view.findViewById(R.id.editTextOTP);
        votp = view.findViewById(R.id.btnVerifyOTP);

        Bundle bundle = getArguments();
        etPhone = bundle.getString("phoneno", "invalid");

        votp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    //sendOTP();*/
                    Intent intent = new Intent(requireContext(), RegActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
               /* } else {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, 100);
                }*/
            }
        });

        return view;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendOTP();
            } else {
                Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendOTP() {
        String otp = etotp.getText().toString();

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(otp + " " + message);
        String phoneNumber = etPhone;
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
    }
}

package cse2.bhooshan.uberforrepair;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomLocationFragment extends Fragment {

    private EditText addressEditText;
    private Button getAddressButton;
    private TextView resultTextView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomLocationFragment newInstance(String param1, String param2) {
        CustomLocationFragment fragment = new CustomLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_location, container, false);

        addressEditText = view.findViewById(R.id.addressEditText);
        getAddressButton = view.findViewById(R.id.getAddressButton);
        resultTextView = view.findViewById(R.id.resultTextView);

        getAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to retrieve address using the entered text
                retrieveAddress();
            }
        });

        return view;
    }

    private void retrieveAddress() {
        // Get the address from the EditText (You can use Geocoding or any other method)
        String enteredAddress = addressEditText.getText().toString();

        // For simplicity, just set the address to the TextView
        resultTextView.setText("Entered Address: " + enteredAddress);
    }

}
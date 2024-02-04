package cse2.bhooshan.uberforrepair;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.BreakIterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TwoWheelerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TwoWheelerFragment extends Fragment {
    private   EditText editTextBrand, editTextModel, editTextColor;
    private Button btnSaveTwoWheelerDetails;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TwoWheelerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TwoWheelerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TwoWheelerFragment newInstance(String param1, String param2) {
        TwoWheelerFragment fragment = new TwoWheelerFragment();
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
        View view = inflater.inflate(R.layout.fragment_two_wheeler, container, false);

        editTextBrand = view.findViewById(R.id.editTextBrand);
        editTextModel = view.findViewById(R.id.editTextModel);
        editTextColor = view.findViewById(R.id.editTextColor);
        btnSaveTwoWheelerDetails = view.findViewById(R.id.btnSaveTwoWheelerDetails);

        btnSaveTwoWheelerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String brand = editTextBrand.getText().toString().trim();
                String model = editTextModel.getText().toString().trim();
                String color = editTextColor.getText().toString().trim();

                String details = "Brand: " + brand + "\nModel: " + model + "\nColor: " + color;
                Toast.makeText(getActivity(), details, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                startActivity(intent);


            }
        });

        return view;
    }
}
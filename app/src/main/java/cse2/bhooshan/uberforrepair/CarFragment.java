package cse2.bhooshan.uberforrepair;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarFragment extends Fragment {
    private EditText editTextCarBrand, editTextCarModel, editTextCarColor;
    private Button btnSaveCarDetails;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarFragment newInstance(String param1, String param2) {
        CarFragment fragment = new CarFragment();
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
        View view = inflater.inflate(R.layout.fragment_car, container, false);

        // Initialize views
        editTextCarBrand = view.findViewById(R.id.editTextCarBrand);
        editTextCarModel = view.findViewById(R.id.editTextCarModel);
        editTextCarColor = view.findViewById(R.id.editTextCarColor);
        btnSaveCarDetails = view.findViewById(R.id.btnSaveCarDetails);

        // Set click listener for the save button
        btnSaveCarDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user inputs
                String carBrand = editTextCarBrand.getText().toString().trim();
                String carModel = editTextCarModel.getText().toString().trim();
                String carColor = editTextCarColor.getText().toString().trim();

                // Perform save logic (replace with your actual logic)
                String details = "Car Brand: " + carBrand + "\nCar Model: " + carModel + "\nCar Color: " + carColor;
                Toast.makeText(getActivity(), details, Toast.LENGTH_LONG).show();
            }
        });

        return view;

    }
}
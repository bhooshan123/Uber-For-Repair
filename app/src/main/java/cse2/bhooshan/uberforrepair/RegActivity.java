package cse2.bhooshan.uberforrepair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class RegActivity<button> extends AppCompatActivity {
    private Button userreg;
    private Button mechreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        userreg=findViewById(R.id.btnRegisterUser);
        mechreg=findViewById(R.id.btnRegisterMechanic);
        userreg.setOnClickListener(view -> {
            Intent intent=new Intent(RegActivity.this,UserregActivity.class);
            startActivity(intent);
        });
        mechreg.setOnClickListener(view -> {
            Intent intent1=new Intent(RegActivity.this,MechanicRegistrationActivity.class);
            startActivity(intent1);
        });
    }
}
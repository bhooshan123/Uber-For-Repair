package cse2.bhooshan.uberforrepair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class GetstartedActivity extends AppCompatActivity {
    TextView gstv;
    Button gsbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);
        gstv=findViewById(R.id.textView3);
        gsbutton=findViewById(R.id.button);
        gsbutton.setOnClickListener(view -> {
            Intent intent=new Intent(GetstartedActivity.this, RegActivity.class);
            startActivity(intent);
        });
    }
}
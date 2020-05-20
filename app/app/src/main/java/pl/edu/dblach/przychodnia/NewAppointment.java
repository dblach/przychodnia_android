package pl.edu.dblach.przychodnia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class NewAppointment extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        Toast.makeText(this,"newappointment created",Toast.LENGTH_SHORT).show();
    }
}

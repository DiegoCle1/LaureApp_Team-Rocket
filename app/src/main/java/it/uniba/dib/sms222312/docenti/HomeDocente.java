package it.uniba.dib.sms222312.docenti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.uniba.dib.sms222312.R;

public class HomeDocente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_docente);
        BottomNavigationView navBar = findViewById(R.id.bottom_navigation);

    }
}
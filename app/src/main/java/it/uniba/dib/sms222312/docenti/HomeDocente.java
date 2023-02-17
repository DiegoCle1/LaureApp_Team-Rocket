package it.uniba.dib.sms222312.docenti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import it.uniba.dib.sms222312.Login;
import it.uniba.dib.sms222312.R;

public class HomeDocente extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeDocenteFragment homeFragment = new HomeDocenteFragment();
    ProfiloDocenteFragment profiloFragment = new ProfiloDocenteFragment();
    RegistraTesiFragment registraTesiFragment = new RegistraTesiFragment();
    ListaTesiFragment listaTesiFragment = new ListaTesiFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_docente);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.page_1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.page_2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, listaTesiFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeDocente.this, Login.class));
        finish();
    }
}
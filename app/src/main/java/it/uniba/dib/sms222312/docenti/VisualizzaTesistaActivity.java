package it.uniba.dib.sms222312.docenti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import it.uniba.dib.sms222312.R;

public class VisualizzaTesistaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_tesista);

        String tesista = getIntent().getStringExtra("Tesista");

        Button btnTask = findViewById(R.id.btnTask);

        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Da aggiornare per poi far aggiornare la recycler
                Intent intent = new Intent(VisualizzaTesistaActivity.this, AggiungiTaskActivity.class);

                intent.putExtra("Tesista", tesista);

                startActivity(intent);
            }
        });
    }
}
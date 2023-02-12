package it.uniba.dib.sms222312;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtEmail = findViewById(R.id.edt_email);
                EditText edtPassword = findViewById(R.id.edt_password);
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                mAuth = FirebaseAuth.getInstance();
                signIn(email,password);
            }
        });
    }
    private void signIn(String email, String password) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Controlla se l'utente è uno studente o un docente
                                    checkUserType();
                                } else {
                                    Toast.makeText(Login.this, "Autenticazione fallita.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }


    private void checkUserType() {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("utente").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String role = documentSnapshot.getString("tipo");
                        if (role.equals("studente")) {
                            Toast.makeText(Login.this, "Autenticazione come studente.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, Registry.class));
                            finish();
                        } else if (role.equals("docente")) {
                            Toast.makeText(Login.this, "Autenticazione come docente.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, RegistryDocente.class));
                            finish();
                        }
                    }
                });
    }

}
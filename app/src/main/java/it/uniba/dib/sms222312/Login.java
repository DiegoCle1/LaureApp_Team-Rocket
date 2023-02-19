package it.uniba.dib.sms222312;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import it.uniba.dib.sms222312.docenti.HomeDocente;
import it.uniba.dib.sms222312.registrazione.Registry;
import it.uniba.dib.sms222312.studenti.HomeStudente;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnLogin = findViewById(R.id.btn_login);

        final LoadingDialog loadingDialog = new LoadingDialog(Login.this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtEmail = findViewById(R.id.edt_email);
                EditText edtPassword = findViewById(R.id.edt_password);
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "Tutti i campi sono obbligatori", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth = FirebaseAuth.getInstance();
                signIn(email,password,loadingDialog);
                loadingDialog.startLoadingDialog();
            }
        });

    }
    private void signIn(String email, String password, LoadingDialog loadingDialog) {
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
                                    loadingDialog.dismissDialog();
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
                            Toast.makeText(Login.this, R.string.authStudente,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, HomeStudente.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finishAffinity();

                        } else if (role.equals("docente")) {
                            Toast.makeText(Login.this, R.string.authDocente,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, HomeDocente.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }
                });
    }

    public void esci(View view) {
        finish();
    }
}
package edmt.dev.womensos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    EditText email;
    EditText password;
    FirebaseAuth firebaseAuth;
    Button registerbtn;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);
        email = findViewById(R.id.email);
        password=findViewById(R.id.password);
        firebaseAuth= FirebaseAuth.getInstance();
        registerbtn=findViewById(R.id.registerbtn);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this,Login.class);
                startActivity(i);
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = password.getText().toString();
                String emailid= email.getText().toString();
                if(emailid.isEmpty()){
                    email.setError("Please enter your Email");
                    email.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter your Password");
                    password.requestFocus();
                }
                else if(emailid.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(Registration.this, "Fields are Empty!", Toast.LENGTH_LONG).show();
                }
                else if(!(emailid.isEmpty() && pwd.isEmpty())){

                    firebaseAuth.createUserWithEmailAndPassword(emailid,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                firebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Registration.this, "Registration successful. Please check your Email for Verification", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(Registration.this, Login.class);
                                            startActivity(i);
                                        }else{
                                            Log.e("Error", task.getException().toString());
                                            Toast.makeText(Registration.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else {

                                Log.e("Error", task.getException().toString());
                                Toast.makeText(Registration.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }


}

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText lemail;
    private EditText lpassword;
    FirebaseAuth firebaseAuth;
    Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        lemail=findViewById(R.id.lemail);
        lpassword=findViewById(R.id.lpassword);
        firebaseAuth= FirebaseAuth.getInstance();
        loginbtn=findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lpwd = lpassword.getText().toString();
                String lemailid= lemail.getText().toString();
                if(lemailid.isEmpty()){
                    lemail.setError("Please enter your Email");
                    lemail.requestFocus();
                }
                else if(lpwd.isEmpty()){
                    lpassword.setError("Please enter your Password");
                    lpassword.requestFocus();
                }
                else if(lemailid.isEmpty() && lpwd.isEmpty()) {
                    Toast.makeText(Login.this, "Fields are Empty!", Toast.LENGTH_LONG).show();
                }
                else if(!(lemailid.isEmpty() && lpwd.isEmpty())){
                    firebaseAuth.signInWithEmailAndPassword(lemailid,lpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(Login.this,Profile.class);
                                    i.putExtra("Email",firebaseAuth.getCurrentUser().getEmail());
                                    startActivity(i);
                                }else{
                                    Toast.makeText(Login.this,"Please Verify your Email",Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.e("Error", task.getException().toString());
                                Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }


}

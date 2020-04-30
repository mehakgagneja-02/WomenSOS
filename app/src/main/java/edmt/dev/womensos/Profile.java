package edmt.dev.womensos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Profile extends AppCompatActivity {

    EditText name, guardian,alternate;
    Button submit;
    DatabaseReference mDatabaseReference;
    public FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name=findViewById(R.id.name);
        guardian=findViewById(R.id.guardian);
        alternate=findViewById(R.id.alternate);
        submit=findViewById(R.id.submit);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user;
        user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Details of user").child(uid);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store();
                String strname = name.getText().toString();
                String strguardian = guardian.getText().toString();
                String stralternate = alternate.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("Name_info",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Name",strname);
                editor.commit();
                SharedPreferences sharedPreferences1 = getSharedPreferences("Guardian_info",0);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("Guardian",strguardian);
                editor1.commit();
                SharedPreferences sharedPreferences2 = getSharedPreferences("Alternate_info",0);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor1.putString("Alternate",stralternate);
                editor2.commit();
                Intent i = new Intent(Profile.this,SOS.class);
                startActivity(i);
            }
        });
    }
    public void store(){
        String id = mDatabaseReference.push().getKey();
        String dname = name.getText().toString();
        String dguardian = guardian.getText().toString();
        String dalternate = alternate.getText().toString();
        Db db=new Db(id,dname,dguardian,dalternate);
        mDatabaseReference.child(dname).setValue(db);
    }
}

package edmt.dev.womensos;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SOS extends AppCompatActivity {
    TextView txt, altclick;
    Button sos, update;

    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    final static int REQUESTCODE_PERMISSION_SMS = 301;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        SharedPreferences sharedPreferences = getSharedPreferences("Name_info", 0);
        String strname = sharedPreferences.getString("Name", "");
        SharedPreferences sharedPreferences1 = getSharedPreferences("Guardian_info", 0);
        final String strguardian = sharedPreferences1.getString("Guardian", "");
        SharedPreferences sharedPreferences2 = getSharedPreferences("Alternate_info", 0);
        final String stralternate = sharedPreferences2.getString("Alternate", "");

        txt = findViewById(R.id.txt);
        sos = findViewById(R.id.sos);
        altclick = findViewById(R.id.altclick);
        update = findViewById(R.id.update);
        txt.setText(strname + ",");

        final String msg = "EMERGENCY ! Your ward, " + strname + " is in Danger. RUSH RUSH RUSH !";

        sentPI = PendingIntent.getBroadcast(SOS.this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(SOS.this, 0, new Intent(DELIVERED), 0);

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 28) {
                    ActivityCompat.requestPermissions(SOS.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, REQUESTCODE_PERMISSION_SMS);
                } else {
                    ActivityCompat.requestPermissions(SOS.this, new String[]{Manifest.permission.SEND_SMS}, REQUESTCODE_PERMISSION_SMS);
                }
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(strguardian, null, msg, sentPI, deliveredPI);
            }
        });

        altclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 28) {
                    ActivityCompat.requestPermissions(SOS.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, REQUESTCODE_PERMISSION_SMS);
                } else {
                    ActivityCompat.requestPermissions(SOS.this, new String[]{Manifest.permission.SEND_SMS}, REQUESTCODE_PERMISSION_SMS);
                }
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(stralternate, null, msg, sentPI, deliveredPI);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SOS.this,Profile.class);
                startActivity(i);
            }
        });
    }

        @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case SOS.RESULT_OK:
                        Toast.makeText(context, "SMS sent successfully!", Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong and there's no way to tell what, why or how.
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure!", Toast.LENGTH_SHORT).show();
                        break;

                    //Your device simply has no cell reception. You're probably in the middle of
                    //nowhere, somewhere inside, underground, or up in space.
                    //Certainly away from any cell phone tower.
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service!", Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong in the SMS stack, while doing something with a protocol
                    //description unit (PDU) (most likely putting it together for transmission).
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;

                    //You switched your device into airplane mode, which tells your device exactly
                    //"turn all radios off" (cell, wifi, Bluetooth, NFC, ...).
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off!", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch(getResultCode())
                {
                    case SOS.RESULT_OK:
                        Toast.makeText(context, "SMS delivered!", Toast.LENGTH_SHORT).show();
                        break;

                    case SOS.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }
}
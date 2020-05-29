package android.uday.blindstick.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.uday.blindstick.Bluetooth.BluetoothConn;
import android.uday.blindstick.Commands.Commands;
import android.uday.blindstick.R;
import android.uday.blindstick.utils.Constants;
import android.uday.blindstick.utils.PreferenceUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission_group.CAMERA;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.INTERNET;

public class UserActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static final int RequestPermissionCode = 10;

    private TextView ShowUsSpeak;
    public static String module="";
    ImageButton speak;
    String command="blabla";
    boolean check = false;
    private final int REQ_CODE = 100;
    private TextToSpeech tts;
    FragmentManager f;
    String welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ShowUsSpeak = (TextView) findViewById(R.id.textViewShow);
        speak = (ImageButton) findViewById(R.id.imageButtonSpeak);
        tts = new TextToSpeech(this, this);

        Intent intent = getIntent();
        f=getSupportFragmentManager();

            String email = PreferenceUtils.getEmail(this);
                welcome="Hi"+ email +" what can i do for you today ?";

         ShowUsSpeak.setText(welcome);

        new MyTask().execute();
        //Welcome
        tts.speak(welcome,TextToSpeech.QUEUE_FLUSH,null);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Prompt speech input
                promptSpeechInput();
                check = true;
            }
        });


        // If All permission is enabled successfully then this block will execute.
        if (CheckingPermissionIsEnabledOrNot()) {
           // Toast.makeText(UserPermission.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
            Toast.makeText(UserActivity.this,"All Permissions Granted Successfully",Toast.LENGTH_LONG).show();
        }
        // If, If permission is not enabled then else condition will execute.
        else {

            //Calling method to enable permission.
            RequestMultiplePermission();
        }



    }

    private void launchModule(String commandTolaunch)
    {
        switch (commandTolaunch)
        {
            case Commands.callModule:
                Toast.makeText(getBaseContext(), "Call Module", Toast.LENGTH_SHORT).show();
                Intent intentc = new Intent(UserActivity.this, PhoneModule.class);
                startActivity(intentc);
                break;
        }
    }
    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE);

        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    class MyTask extends AsyncTask<Void, Void, Void>
    {

        //myXMLWorker doing;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL web = new URL(baseUrl);
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser sp = saxParserFactory.newSAXParser();
                XMLReader xmlReader = sp.getXMLReader();
                doing = new myXMLWorker();
                xmlReader.setContentHandler(doing);
                xmlReader.parse(new InputSource(web.openStream()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
        String command = Commands.TEMP;
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            switch (command) {
                case Commands.TEMP:
                    weatherText = doing.getTemp();
                    break;
            }
        }
    }
    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ShowUsSpeak.setText(result.get(0));

                    //Speak out
                    speakOut();

                }
                break;
            }

        }
    }
    //Speak Out
    private void speakOut() {

        String text = ShowUsSpeak.getText().toString();

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

        command = text;

        //Launch Module
        if (check) {
            launchModule(command);
        }
    }







    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.log_out:
                PreferenceUtils.savePassword(null, this);
                PreferenceUtils.saveEmail(null, this);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.activity_bluetooth_conn:
                Intent intent1=new Intent(this, BluetoothConn.class);
                startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {

                    boolean InternetPermission=grantResults[9]==PackageManager.PERMISSION_GRANTED;
                    boolean BluetoothPermission=grantResults[8]==PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordAudioPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean SendSMSPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean GetAccountsPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    boolean ReadContactsPermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteContactsPermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean CallPhonePermission = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    boolean LocationPermission = grantResults[7] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && RecordAudioPermission && SendSMSPermission && GetAccountsPermission && ReadContactsPermission && WriteContactsPermission && CallPhonePermission && LocationPermission && BluetoothPermission && InternetPermission) {

                        Toast.makeText(UserActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UserActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

        }
    }
    private void RequestMultiplePermission()
    {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(UserActivity.this, new String[]
                {
                        BLUETOOTH,
                        INTERNET,
                        CAMERA,
                        RECORD_AUDIO,
                        SEND_SMS,
                        GET_ACCOUNTS,
                        READ_CONTACTS,
                        WRITE_CONTACTS,
                        CALL_PHONE,
                        ACCESS_FINE_LOCATION
                }, RequestPermissionCode);
    }
    private boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);

        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);
        int SeventhPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        int EighthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int NinthPermissionResult=ContextCompat.checkSelfPermission(getApplicationContext(),BLUETOOTH);
        int TenthPermissionResult=ContextCompat.checkSelfPermission(getApplicationContext(),INTERNET);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SeventhPermissionResult == PackageManager.PERMISSION_GRANTED &&
                EighthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                NinthPermissionResult==PackageManager.PERMISSION_GRANTED &&
                TenthPermissionResult==PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speak.setEnabled(true);
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }
    public void onDestroy() {
        // Shuts Down TTS
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}

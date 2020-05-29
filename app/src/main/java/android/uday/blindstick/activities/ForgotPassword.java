package android.uday.blindstick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.uday.blindstick.R;
import android.uday.blindstick.helper.InputValidation;
import android.uday.blindstick.sql.DatabaseHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

public class ForgotPassword extends AppCompatActivity {
    private TextInputEditText textInputEditTextEmail;
    private TextInputLayout textInputLayoutEmail;
    private Button ButtonConfirm;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    private NestedScrollView nestedScrollView;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        ButtonConfirm = (Button) findViewById(R.id.textEmail);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        databaseHelper = new DatabaseHelper(this);
        inputValidation = new InputValidation(this);

        setTitle("Recover password");
        ButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyFromSQLite();
            }
        });



    }
    private void verifyFromSQLite(){

        if (textInputEditTextEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill your email", Toast.LENGTH_SHORT).show();
            return;
        }


        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {
            Intent accountsIntent = new Intent(this, ConfirmPassword.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        } else {
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }

    }
    private void emptyInputEditText(){
        textInputEditTextEmail.setText("");
    }


}

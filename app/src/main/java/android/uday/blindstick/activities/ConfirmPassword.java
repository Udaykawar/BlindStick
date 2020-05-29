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
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

public class ConfirmPassword extends AppCompatActivity {
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private NestedScrollView nestedScrollView;
    private Button ButtonReset;

    String email;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmpassword);

        inputValidation = new InputValidation(this);
        databaseHelper = new DatabaseHelper(this);

        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditpass);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditConfirmPassword);

        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPass);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        ButtonReset = (Button) findViewById(R.id.buttonReset);

        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL");

        setTitle("Reset password");

        ButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }
    private void updatePassword() {

        String value1 = textInputEditTextPassword.getText().toString().trim();
        String value2 = textInputEditTextConfirmPassword.getText().toString().trim();

        if (value1.isEmpty() && value2.isEmpty()){
            Toast.makeText(this, "fill all fields ", Toast.LENGTH_LONG).show();
            return;
        }

        if (!value1.contentEquals(value2)){
            Toast.makeText(this, "password doesn't match", Toast.LENGTH_LONG).show();
            return;
        }

        if (!databaseHelper.checkUser(email)) {

            Snackbar.make(nestedScrollView, "email doesn't exist", Snackbar.LENGTH_LONG).show();
            return;

        } else {
           databaseHelper.updatePassword(email,value1);
           Toast.makeText(this,"password reset successfuly",Toast.LENGTH_SHORT).show();
           emptyInputEditText();

           Intent intent=new Intent(this,LoginActivity.class);
           startActivity(intent);
           finish();
        }

    }
    private void emptyInputEditText()
    {
        textInputEditTextPassword.setText("");
        textInputEditTextConfirmPassword.setText("");
    }


}


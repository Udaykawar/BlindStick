package android.uday.blindstick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.uday.blindstick.R;
import android.uday.blindstick.helper.InputValidation;
import android.uday.blindstick.model.User;
import android.uday.blindstick.sql.DatabaseHelper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.widget.NestedScrollView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutMobileno;
    private TextInputLayout inputLayoutPassword;
    private TextInputLayout inputLayoutConfirmPassword;

    private TextInputEditText inputEditTextName;
    private  TextInputEditText inputEditTextMobileno;
    private TextInputEditText inputEditTextEmail;
    private TextInputEditText inputEditTextPassword;
    private TextInputEditText inputEditTextConfirmPassword;

    private Button ButtonRegister;
    private TextView TextViewLoginLink;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //  getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }
    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        inputLayoutName=findViewById(R.id.textInputLayoutName);
        inputLayoutEmail=findViewById(R.id.textInputLayoutEmail);
        inputLayoutMobileno=findViewById(R.id.textInputLayoutMobileNo);
        inputLayoutPassword=findViewById(R.id.textInputLayoutPassword);
        inputLayoutConfirmPassword=findViewById(R.id.textInputLayoutConfirmPassword);

        inputEditTextName=findViewById(R.id.textInputEditTextName);
        inputEditTextEmail=findViewById(R.id.textInputEditTextEmail);
        inputEditTextMobileno=findViewById(R.id.textInputEditTextMobileNo);
        inputEditTextPassword=findViewById(R.id.textInputEditTextPassword);
        inputEditTextConfirmPassword=findViewById(R.id.textInputEditTextConfirmPassword);

        ButtonRegister=findViewById(R.id.buttonRegister);
        TextViewLoginLink=findViewById(R.id.loginLink);


    }

    private void initListeners() {
        ButtonRegister.setOnClickListener(this);
        TextViewLoginLink.setOnClickListener(this);

    }
    private void initObjects() {

        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonRegister:
                postDataToSQLite();
                break;

            case R.id.loginLink:
                finish();
                break;
        }

    }

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(inputEditTextName, inputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputEditTextMobileno, inputLayoutMobileno, getString(R.string.error_message_mobileno))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputEditTextEmail, inputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputEditTextEmail, inputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputEditTextPassword, inputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(inputEditTextPassword, inputEditTextConfirmPassword,
                inputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if (!databaseHelper.checkUser(inputEditTextEmail.getText().toString().trim()))
        {
            user.setName(inputEditTextName.getText().toString().trim());
            user.setMobileno(inputEditTextMobileno.getText().toString().trim());
            user.setEmail(inputEditTextEmail.getText().toString().trim());
            user.setPassword(inputEditTextPassword.getText().toString().trim());
            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
           // Toast.makeText(getApplicationContext(),getString(R.string.success_message),Toast.LENGTH_LONG).show();
            emptyInputEditText();

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }
        else
        {
            Snackbar.make(nestedScrollView,getString(R.string.error_email_exists),Snackbar.LENGTH_LONG).show();
        }



    }

    private void emptyInputEditText() {
        inputEditTextName.setText(null);
        inputEditTextMobileno.setText(null);
        inputEditTextEmail.setText(null);
        inputEditTextPassword.setText(null);
        inputEditTextConfirmPassword.setText(null);

    }

}

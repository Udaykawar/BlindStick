package android.uday.blindstick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.uday.blindstick.R;
import android.uday.blindstick.helper.InputValidation;
import android.uday.blindstick.sql.DatabaseHelper;
import android.uday.blindstick.utils.PreferenceUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static android.uday.blindstick.R.id.buttonLogin;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;

    private TextInputEditText inputEditTextEmail;
    private TextInputEditText inputEditTextPassword;

    private Button ButtonLogin;

    private TextView TextViewLinkRegister;
    private TextView TextViewLinkForgotPassword;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        inputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        inputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        inputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        inputEditTextPassword = findViewById(R.id.textInputEditTextPassword);

        ButtonLogin = findViewById(R.id.buttonLogin);

        TextViewLinkRegister = findViewById(R.id.textViewLinkRegister);
        TextViewLinkForgotPassword = findViewById(R.id.forgotPassword);
        PreferenceUtils utils = new PreferenceUtils();

        if (PreferenceUtils.getEmail(this) != null) {
            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
            startActivity(intent);
        } else {

        }
    }

    private void initListeners() {
        ButtonLogin.setOnClickListener(this);
        TextViewLinkRegister.setOnClickListener(this);
        TextViewLinkForgotPassword.setOnClickListener(this);
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                verifyFromSqlite();
                break;
            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.forgotPassword:
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
                break;
        }

    }

    private void verifyFromSqlite()
    {
        if (!inputValidation.isInputEditTextFilled(inputEditTextEmail, inputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(inputEditTextEmail, inputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputEditTextPassword, inputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }


        String email = inputEditTextEmail.getText().toString();
        String password = inputEditTextPassword.getText().toString();

        if (databaseHelper.checkUser(email, password)) {
            PreferenceUtils.saveEmail(email, this);
            PreferenceUtils.savePassword(password, this);
            Intent accountsIntent = new Intent(activity, UserActivity.class);
            accountsIntent.putExtra("EMAIL", inputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
            finish();
        } else {
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        inputEditTextEmail.setText(null);
        inputEditTextPassword.setText(null);
    }
}



   /* protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();
    }
    private void initViews(){
        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);

        ButtonLogin = findViewById(buttonLogin);

        textViewLinkRegister = findViewById(R.id.textViewLinkRegister);
        textViewLinkForgotPassword = findViewById(R.id.forgotPassword);
        PreferenceUtils utils = new PreferenceUtils();

        if (PreferenceUtils.getEmail(this) != null ){
            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
            startActivity(intent);
        }
    }

    private void initListeners(){
        ButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
        textViewLinkForgotPassword.setOnClickListener(this);
    }

    private void initObjects(){
        databaseHandler= new DatabaseHandler(activity);
        inputValidation = new InputValidation(activity);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.buttonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.forgotPassword:
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
                break;
        }

    }
    private void verifyFromSQLite()
    {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }
        String email = textInputEditTextEmail.getText().toString().trim();
        String password = textInputEditTextPassword.getText().toString().trim();

        if (databaseHandler.checkUser(email, password)) {
            PreferenceUtils.saveEmail(email, this);
            PreferenceUtils.savePassword(password, this);
            Intent accountsIntent = new Intent(activity, UserActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
            finish();
        } else {
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }
    private void emptyInputEditText(){
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }*/




package com.edu.fsu.cs.cen4020.pizzapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.fsu.cs.cen4020.flaskinterface.GetData;
import com.edu.fsu.cs.cen4020.flaskinterface.UserData;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    static final String TAG = "LoginActivity";
    Button Login;

    TextView PromptText;

    EditText Username;
    EditText Password;
    EditText ConfirmPassword;

    private static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login = (Button) findViewById(R.id.login_button);
        PromptText = (TextView) findViewById(R.id.signUpText);

        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        ConfirmPassword = (EditText) findViewById(R.id.confirmPswd);
    }

    public static String getUsername() {
        return username;
    }

    public void myLoginHandler(View v) {
        if (v == Login) {
            if (Login.getText().toString().equals("Log In"))
                pressedLogin();
            else
                pressedSignUp();
        } else if(v == PromptText)
            switchForm();
    }

    private void pressedLogin() {
        ConfirmPassword.getText().clear();
        if (checkFields()) {
            if (checkDatabase()) {
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);
            } else
                Toast.makeText(this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
        }
    }

    private void pressedSignUp() {
        if (checkFields()) {
            if (!checkDatabaseUsername()) {
                UserData createUser = new UserData();
                createUser.execute(getString(R.string.serverIP) + "create/", username, Password.getText().toString());
                try {
                    if (createUser.get()) {
                        Intent myIntent = new Intent(this, MainActivity.class);
                        startActivity(myIntent);
                    } else {
                        Log.e(TAG, "Error creating user");
                    }
                } catch (Exception x) {
                    x.printStackTrace();
                    Log.e(TAG, "Create User Exception");
                }
            } else
                Toast.makeText(this, "Username already exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchForm() {
        if (Login.getText().toString().equals("Log In")) {
            ConfirmPassword.setVisibility(View.VISIBLE);
            Login.setText(getString(R.string.signupText));
            PromptText.setText(getString(R.string.signupPrompt));
        } else {
            ConfirmPassword.setVisibility(View.GONE);
            Login.setText(getString(R.string.loginText));
            PromptText.setText(getString(R.string.loginPrompt));
        }
    }

    private boolean checkFields() {//checks if a valid username or password has been entered
        boolean check = true;
        String message = "Invalid ";

        username = Username.getText().toString();
        if (username.length() == 0) {
            message = message.concat("Username");
            check = false;
        } else {
            for (int i = 0; i < username.length(); i++) {
                if (!Character.isLetterOrDigit(username.charAt(i))) {
                    Username.getText().clear();
                    message = message.concat("Username");
                    check = false;
                    break;
                }
            }
        }

        if (ConfirmPassword.getVisibility() == View.VISIBLE) {
            if (!Password.getText().toString().equals(ConfirmPassword.getText().toString())) {
                Password.getText().clear();
                ConfirmPassword.getText().clear();
                message = message.concat((message.equals("Invalid Username")) ? "/Password" : "Password");
                check = false;
            } else if (Password.getText().toString().length() == 0) {
                Password.getText().clear();
                ConfirmPassword.getText().clear();
                message = message.concat((message.equals("Invalid Username")) ? "/Password" : "Password");
                check = false;
            }
        } else if (Password.getText().toString().length() == 0) {
            message = message.concat((message.equals("Invalid Username")) ? "/Password" : "Password");
            check = false;
        }
        if (!check) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        return check;
    }

    private boolean checkDatabaseUsername() {
        ArrayList<String> result;
        String url = getString(R.string.serverIP) + "users/";
        url += username;
        GetData checkUsername = new GetData();
        checkUsername.execute(url);
        try {
            result = checkUsername.get();
            return (result.get(0).equals("true"));
        } catch (Exception x) {
            x.printStackTrace();
        }
        return false;
    }

    private boolean checkDatabase() {
        UserData checkLogin = new UserData();
        boolean check = false;
        checkLogin.execute(getString(R.string.serverIP) + "login/", username, Password.getText().toString());

        try {
            check = checkLogin.get();
        } catch (Exception x) {
            x.printStackTrace();
            Log.e("CheckDatabase", "Failed to get CheckLogin");
        }

        return check;
    }

    @Override
    public void onBackPressed() {/*makes back button do nothing*/}


}


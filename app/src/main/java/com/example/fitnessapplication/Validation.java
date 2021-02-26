package com.example.fitnessapplication;

import android.util.Log;
import android.widget.EditText;

public class Validation {

    public Validation() {
    }

    public boolean validateUserName(EditText username) {
        String val = username.getText().toString().trim();

        if (val.isEmpty()) {
            username.setError("Field cannot be empty OR only spaces");
            return false;
        } else if (val.length() >= 10) {
            username.setError("Username too long");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    public boolean validateEmail(EditText email) {
        String val = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty OR only spaces");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    public boolean validatePassword(EditText password) {
        String val = password.getText().toString().trim();

        if (val.isEmpty()) {
            password.setError("Field cannot be empty OR only spaces");
            return false;
        } else if (val.length() < 6) {
            password.setError("Password too short, you need more " + (6 - val.length() + " char"));
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public boolean validateSamePassword(EditText password, EditText reEnteredPassword) {
        String val = password.getText().toString().trim();
        String val2 = reEnteredPassword.getText().toString().trim();
        Log.d("val", val);
        Log.d("val2", val2);
        if (val.equals(val2))
            return true;
        else {
            reEnteredPassword.setError("Not the same password!");
            return false;
        }
    }
}

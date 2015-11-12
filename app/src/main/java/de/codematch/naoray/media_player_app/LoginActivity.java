package de.codematch.naoray.media_player_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText email_field;
    EditText passwort_field;
    String email;
    String passwort;
    Button login_button;

    String[] gueltigeEmails = {"Nico@web.de", "Krishan@gmx.de"};
    String[] gueltigePasswoerter = {"admin1", "coadmin2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_field = (EditText) findViewById(R.id.login_email);
        passwort_field = (EditText) findViewById(R.id.login_passwort);


        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEntries()) {
                    loginCheck();
                }
            }
        });
    }

    public boolean checkEntries() {
        email = email_field.getText().toString();
        passwort = passwort_field.getText().toString();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(LoginActivity.this, getString(R.string.warnung_falsche_email), Toast.LENGTH_SHORT).show();
            email_field.requestFocus();
            return false;
        } else if (passwort.isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.warnung_fehlendes_passwort), Toast.LENGTH_SHORT).show();
            passwort_field.requestFocus();
            return false;
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.meldung_pruefung_erfolgt), Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public void loginCheck() {

        Boolean email_ok = false;
        Boolean passwort_ok = false;
        int passwortIndex = 0;

        for (int i = 0; i < gueltigeEmails.length; i++) {
            if (email.equalsIgnoreCase(gueltigeEmails[i])) {
                email_ok = true;
                passwortIndex = i;
                break;
            }
        }

        if (email_ok) {
            if (passwort.equals(gueltigePasswoerter[passwortIndex])) {
                passwort_ok = true;
            }
        }

        if (email_ok && passwort_ok) {
            login();
        } else {
            handleError(email_ok, passwort_ok);
        }
    }

    public void handleError(Boolean email_ok, Boolean passwort_ok) {
        if (!email_ok) {
            Toast.makeText(LoginActivity.this, getString(R.string.warnung_unbekannte_email), Toast.LENGTH_SHORT).show();
        } else if (!passwort_ok) {
            Toast.makeText(LoginActivity.this, getString(R.string.warnung_falsches_passwort), Toast.LENGTH_SHORT).show();
        }
    }

    public void login() {

        startActivity(new Intent(this, MainMenueActivity.class));
    }
}
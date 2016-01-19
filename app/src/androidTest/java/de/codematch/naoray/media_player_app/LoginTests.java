package de.codematch.naoray.media_player_app;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Schmidt on 25.12.2015.
 */
public class LoginTests extends ActivityInstrumentationTestCase2<LoginActivityOriginal> {

    public LoginTests () {
        super(LoginActivityOriginal.class);

    }



    public void testActivityExists() {
    LoginActivityOriginal activity = getActivity();
        assertNotNull(activity);

    }
    public void testLogin(){


        // Send string input value mail
        startLogin("nico@web.de", "admin1");





        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor mainActivityMonitor =
                getInstrumentation().addMonitor(MainMenuActivity.class.getName(),
                        null, false);

        MainMenuActivity mainMenuActivity = (MainMenuActivity)
               mainActivityMonitor.waitForActivityWithTimeout(3500);

        assertNotNull("MainMenuActivity is null", mainMenuActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, mainActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                MainMenuActivity.class, mainMenuActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(mainActivityMonitor);


    }
    public void startLogin(String mail, String pw){
        final AutoCompleteTextView  email = (AutoCompleteTextView) getActivity().findViewById(R.id.email);
        final EditText  passwort = (EditText) getActivity().findViewById(R.id.password);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                email.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();


        for(int x = 0; x < 30; x++) {
            getInstrumentation().sendCharacterSync(KeyEvent.KEYCODE_DPAD_RIGHT);
            getInstrumentation().sendCharacterSync(KeyEvent.KEYCODE_DEL);
        }


             getInstrumentation().sendStringSync(mail);
             getInstrumentation().waitForIdleSync();

             // Send string input value passwort
             getInstrumentation().runOnMainSync(new Runnable() {
                 @Override
                 public void run() {
                     passwort.requestFocus();
                 }
             });
        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync(pw);
        getInstrumentation().waitForIdleSync();

        final Button login =
                (Button) getActivity()
                        .findViewById(R.id.email_sign_in_button);

        getInstrumentation().waitForIdleSync();
        // Folgende beiden Zeilen führen beide Click aus; hier gibt es auch oft Probleme
        //TouchUtils alleine klappt nicht und performclick wirft oft Exceptions bzw. wird ausgeführt
        // bevor der REst übertragen wird???
            TouchUtils.clickView(this, login);
        //getInstrumentation().waitForIdle(new SetActivityMoitor());
                //login.performClick();
        getInstrumentation().waitForIdleSync();

    }
    /*
    public class SetActivityMoitor implements Runnable{

        public void run(){
            Instrumentation.ActivityMonitor mainActivityMonitor =
                    getInstrumentation().addMonitor(MainMenuActivity.class.getName(),
                            null, false);

            MainMenuActivity mainMenuActivity = (MainMenuActivity)
                    mainActivityMonitor.waitForActivityWithTimeout(3500);

            assertNotNull("MainMenuActivity is null", mainMenuActivity);
            assertEquals("Monitor for ReceiverActivity has not been called",
                    1, mainActivityMonitor.getHits());
            assertEquals("Activity is of wrong type",
                    MainMenuActivity.class, mainMenuActivity.getClass());

            // Remove the ActivityMonitor
            getInstrumentation().removeMonitor(mainActivityMonitor);
        }
    }
    */

}
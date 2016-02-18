package de.codematch.naoray.media_player_app;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class LoginTests extends ActivityInstrumentationTestCase2<LoginActivity> {

    public LoginTests() {
        super(LoginActivity.class);

    }

    /**
     * simpy tests, if the LoginActivityOriginal exists
     */
    public void testActivityExists() {
        LoginActivity activity = getActivity();
        assertNotNull(activity);

    }

    /**
     * tests if the Login works and the user passes to the MainMenuActivity
     */
    public void testLogin() {

        startLogin("test@test.de", "test");

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor mainActivityMonitor =
                getInstrumentation().addMonitor(MainMenuActivity.class.getName(),
                        null, false);
        // waits 3500 milliseconds, in which the MainMenuActivity should be initialized
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

    /**
     * @param mail the username which is to be tested
     * @param pw   the passwort which is to be tested
     *             <p/>
     *             This Method handles the simulated user-input during the login-process
     *             Mail und Passwort are send to the app like a user tipping the data in the textfields
     */
    public void startLogin(String mail, String pw) {
        final AutoCompleteTextView email = (AutoCompleteTextView) getActivity().findViewById(R.id.email);
        final EditText passwort = (EditText) getActivity().findViewById(R.id.password);

        // sets the focus on the email-field, so sting inputs are injected in this field
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                email.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();

        // clears the textfield for the email. The sendStringSync-Method doesn't override
        // existing content, so it should be ensured, that there is no saved username already
        // contained when the app starts
        for (int x = 0; x < 30; x++) {
            getInstrumentation().sendCharacterSync(KeyEvent.KEYCODE_DPAD_RIGHT);
            getInstrumentation().sendCharacterSync(KeyEvent.KEYCODE_DEL);
        }


        getInstrumentation().sendStringSync(mail);
        getInstrumentation().waitForIdleSync();


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
                        .findViewById(R.id.sign_in_button);

        getInstrumentation().waitForIdleSync();
        //performs a click on the loginbutton
        TouchUtils.clickView(this, login);

        getInstrumentation().waitForIdleSync();
    }
    /*
    public class SetActivityMonitor implements Runnable{
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
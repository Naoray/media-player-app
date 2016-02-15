package de.codematch.naoray.media_player_app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

public class CardDescriptionActivity extends AppCompatActivity {

    String titel;
    String largeDescription;
    String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_description);

        titel = getIntent().getStringExtra("CardTitle");
        largeDescription = getIntent().getStringExtra("CardLargeDescription");
        color = getIntent().getStringExtra("CardColor");

        // Titel setzen
        this.setTitle(titel);

        // Text setzen
        TextView text = (TextView) findViewById(R.id.largeText);
        text.setText(largeDescription);

        // Farbe der Titelbar setzen
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));


        // Farbe der Stausbar setzen
        // Offen: das die Stautusbar einen Tick dunkler ist
        Window window = this.getWindow();
        window.setStatusBarColor(darker(Color.parseColor(color), 0.8f));
    }

    /**
     * Returns darker version of specified <code>color</code>.
     */
    public int darker(int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }
}

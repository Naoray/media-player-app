package de.codematch.naoray.media_player_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private String[] menuItems = {"Live Stream", "Mediathek", "Credits"};
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView menuView = (ListView) findViewById(R.id.menu_list);
        ArrayAdapter<String> menuViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems);
        menuView.setAdapter(menuViewAdapter);
        menuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    intent = new Intent(MainActivity.this, LiveStreamActivity.class);
                } else if (id == 1) {
                    intent = new Intent(MainActivity.this, MediathekActivity.class);
                } else if (id == 2) {
                    intent = new Intent(MainActivity.this, CreditsActivity.class);
                }
                startActivity(intent);
                //Toast.makeText(MainActivity.this, parent + " - " + id, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package de.codematch.naoray.media_player_app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String[] menuItems = {"Live Stream", "Mediathek", "Credits"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView menuView = (ListView) findViewById(R.id.menu_list);
        ArrayAdapter<String> menuViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuItems);
        menuView.setAdapter(menuViewAdapter);
        menuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, parent + " - " + id, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

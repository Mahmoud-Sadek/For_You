package com.a3shank.apps.ashank.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.a3shank.apps.ashank.Activites.MainActivity;
import com.a3shank.apps.ashank.R;

public class AdminPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        Button addClientBtn = (Button) findViewById(R.id.btnAddClient);
        addClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddClientActivity.class));
            }
        });
        Button MainActivityBtn = (Button) findViewById(R.id.btnMainActivity);
        MainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
        Button addItemBtn = (Button) findViewById(R.id.btnAddItems);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddItemActivity.class));
            }
        });
        Button addNewBtn = (Button) findViewById(R.id.btnAddNew);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddClientActivity.class));
            }
        });
        Button addAdvBtn = (Button) findViewById(R.id.btnAddNew);
        addAdvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddClientActivity.class));
            }
        });
    }
}

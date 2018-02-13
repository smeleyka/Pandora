package ru.smeleyka.pandora;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final String SP_BUTTONS_KEY = "json_buttons";
    private SharedPreferences sharedPref;
    private Button button;
    private AlertDialog.Builder ad;
    private AlertDialog aDialog;
    private LinearLayout contentLayout;
    private ArrayList<ButtonPref> buttonsArr;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getPreferences(MODE_PRIVATE);
        loadSharedPreferences();
        initView();
        initDialog();
        permissonRequest();
    }

    @Override
    protected void onStart() {
        loadSharedPreferences();
        super.onStart();
    }

    @Override
    protected void onStop() {
        createSharedPreferences();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if (true) {
            System.out.println("OnClickButton_true");
            makeCallPrepare();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


    }

    private void createSharedPreferences() {
        if(!buttonsArr.isEmpty()) {
            SharedPreferences.Editor sharedPrefEdit = sharedPref.edit();
            String json = gson.toJson(buttonsArr);
            sharedPrefEdit.putString(SP_BUTTONS_KEY, json);
            sharedPrefEdit.commit();
        }
    }

    private void loadSharedPreferences() {
        if (sharedPref.contains(SP_BUTTONS_KEY)) {
            String json = sharedPref.getString(SP_BUTTONS_KEY, "");
            buttonsArr = gson.fromJson(json, ArrayList.class);
            System.out.println("BUTTONS_ARRAY");
            System.out.println(buttonsArr.toString());
        }
    }

    private void makeCallPrepare() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            makeCall();
        } else {
            permissonRequest();
        }

    }

    @SuppressLint("MissingPermission")
    private void makeCall() {
        Uri number = Uri.parse("tel:123456789");
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        System.out.println("StartCalling");
        startActivity(callIntent);
    }

    private void permissonRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Snackbar.make(contentLayout, "Необходимо разрешение на звонки.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            }).show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }

    private void initButtons() {
        contentLayout = findViewById(R.id.content_layout);
        button = new Button(this);
        contentLayout.addView(button);
        button.setOnClickListener(this);

    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });

        initButtons();

    }

    private void initDialog() {
        ad = new AlertDialog.Builder(this);
        ad.setView(R.layout.dialog_addbutton);
        ad.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String bName = "";
                String bNumber = "";
                EditText eTextName = ((EditText) findViewById(R.id.command_name));
                EditText eTextNumber =((EditText) findViewById(R.id.command_number));
                bName = eTextName.getText().toString();
                bNumber = eTextNumber.getText().toString();
                buttonsArr.add(new ButtonPref(bName,bNumber));
                System.out.println("OK");
            }
        });
        ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("NO");
                System.out.println(aDialog.getListView().getChildCount());
            }
        });
        aDialog=ad.create();
    }

    private class ButtonPref {
        private String name;
        private String command;

        public ButtonPref(String name, String command) {
            this.name = name;
            this.command = command;
        }

        @Override
        public String toString() {
            return name + ": " + command;
        }

    }


}

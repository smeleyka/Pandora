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
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final String SP_BUTTONS_KEY = "json_buttons";
    private ArrayList<ButtonPref> buttonsArr;
    private SharedPreferences sharedPref;
    private Button button;
    private LinearLayout contentLayout;
    private Gson gson = new Gson();

    enum DialogType {NO, OK}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buttonsArr = new ArrayList<>();
        sharedPref = getPreferences(MODE_PRIVATE);
        loadSharedPreferences();
        initView();
        initFab();
        initDialog();
        initButtons();
        permissonRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("ON START");
        loadSharedPreferences();
    }

    @Override
    protected void onResume() {
        System.out.println("ON RESUME");
        //initButtons();
        super.onResume();

    }

    @Override
    protected void onPause() {
        System.out.println("ON PAUSE");
        super.onPause();
    }

    @Override
    protected void onStop() {
        System.out.println("ON STOP");
        createSharedPreferences();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        System.out.println(v.getId());
        if (true) {
            System.out.println("OnClickButton_true");
            makeCallPrepare();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private void createSharedPreferences() {
        System.out.println("CREATE SHARED PREFERENCES");
        if (!buttonsArr.isEmpty()) {
            SharedPreferences.Editor sharedPrefEdit = sharedPref.edit();
            String json = gson.toJson(buttonsArr);
            sharedPrefEdit.putString(SP_BUTTONS_KEY, json);
            System.out.println(json);
            sharedPrefEdit.commit();
        }
    }

    private void loadSharedPreferences() {
        System.out.println("LOAD SHARED PREFERENCES");
        if (sharedPref.contains(SP_BUTTONS_KEY)) {
            String json = sharedPref.getString(SP_BUTTONS_KEY, "");
            buttonsArr = gson.fromJson(json, new TypeToken<ArrayList<ButtonPref>>(){}.getType());
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
        System.out.println("INIT BUTTONS");
        System.out.println(buttonsArr.toString());
        System.out.println(buttonsArr.isEmpty());
        if (!buttonsArr.isEmpty()) {
            contentLayout = findViewById(R.id.content_layout);
            contentLayout.removeAllViews();
            for (ButtonPref o : buttonsArr) {
                button = new Button(this);
                button.setText(o.name);
                contentLayout.addView(button);
                button.setOnClickListener(this);
                System.out.println(button.getId());

            }
        }
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog().show();
            }
        });
    }

    private void initDialog() {
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_addbutton, null);
        ad.setView(dialogView);
        final EditText eTextNumber = (EditText) dialogView.findViewById(R.id.command_num);
        final EditText eTextName = (EditText) dialogView.findViewById(R.id.command_name);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String bNumber = eTextNumber.getText().toString();
                String bName = eTextName.getText().toString();
                buttonsArr.add(new ButtonPref(bName, bNumber));
                initButtons();
                System.out.println("Dialog PUSHED OK");
            }
        });
        ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("Dialog PUSHED NO");
            }
        });
        return ad.create();
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

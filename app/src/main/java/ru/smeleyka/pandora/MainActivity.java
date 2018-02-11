package ru.smeleyka.pandora;

import android.Manifest;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 999;
    private SharedPreferences sharedPref;
    private Button button;
    private AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getPreferences(MODE_PRIVATE);

        initView();
        initDialog();
        initButton();

    }

    private void initButton() {
        button = findViewById(R.id.button1);
        button.setOnClickListener(this);

    }

    private void initView() {
        Log.d("OnClick", "Init View");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });
    }

    private void initDialog() {
        ad = new AlertDialog.Builder(this);
        ad.setView(R.layout.dialog_addbutton);
        ad.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("OK");
            }
        });
        ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("NO");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            System.out.println("OnClickButton_true");
            Uri number = Uri.parse("tel:123456789");
            Intent callIntent = new Intent(Intent.ACTION_CALL, number);
            while (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("PermissionDenied");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }

            System.out.println("StartCalling");
            startActivity(callIntent);

            Log.d("OnClick", "Button Clicked");
            Snackbar
                    .make(v, R.string.snack_erro, Snackbar.LENGTH_LONG)
                    .show();

        } else {
            System.out.println("OnClickButton_else");
            Snackbar
                    .make(v, R.string.snack_erro, Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}

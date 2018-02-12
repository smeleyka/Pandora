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
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final String BUTTON_KEY = "button_key";
    private SharedPreferences sharedPref;
    private Button button;
    private AlertDialog.Builder ad;
    private View contentLayout;
    private String[] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createSharedPreferences();
        initView();
        initDialog();
        initButton();
        permissonRequest();

    }

//    @Override
//    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
//        System.out.println("ON-CREATE VIEW   "+name);
//        if (parent!=null){
//            System.out.print("   "+parent.toString());
//        }
//        return super.onCreateView(parent, name, context, attrs);
//    }


    @Override
    protected void onStart() {

        ArrayList<String> buttons  = new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            buttons.add("Test "+i);
            //buttons.get(i).setText("Button NO "+i);
        }
        Gson gson = new Gson();
        String json = gson.toJson(buttons);
        System.out.println("JSON");
        System.out.println(json);
        loadSharedPreferences();
        System.out.println(Arrays.toString(arr));


        super.onStart();
    }

    private void loadSharedPreferences() {
        SharedPreferences loadSP = getPreferences(MODE_PRIVATE);
        String prefValue = loadSP.getString("button1", "DEFAULT");
        arr = loadSP.getStringSet("buttons",new HashSet<String>()).toArray(new String[2]);
        System.out.println(prefValue);
        System.out.println("TEST");
        System.out.println(loadSP.getAll().toString());
        System.out.println("ARRAY");
        System.out.println(Arrays.toString(arr));
    }

    private void createSharedPreferences() {
        sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEdit = sharedPref.edit();

        Set<String> arrSet = new HashSet<>();
        arrSet.add("TEST1");
        arrSet.add("TEST2");
        arrSet.add("TEST3");
        arrSet.add("TEST4");



        sharedPrefEdit.putString("button1", "test1");
        sharedPrefEdit.putString("button2", "test2");
        sharedPrefEdit.putString("button3", "test3");
        sharedPrefEdit.putString("button4", "test4");

        sharedPrefEdit.putStringSet("buttons",arrSet);

        sharedPrefEdit.commit();
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

    private void initButton() {
        button = findViewById(R.id.button1);
        button.setOnClickListener(this);

    }

    private void initView() {
        contentLayout = findViewById(R.id.content_layout);

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

        ConstraintSet set = new ConstraintSet();


        button = new Button(this);
        button.setText("TEST1");
        button.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

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
            Log.d("OnClick", "Button Clicked");
            makeCallPrepare();
        }
    }
}

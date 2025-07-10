package edu.unlpam.vet.ponzonosos;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.util.Log;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import edu.unlpam.vet.ponzonosos.model.Animal;
import edu.unlpam.vet.ponzonosos.model.DaoMaster;
import edu.unlpam.vet.ponzonosos.model.DaoSession;
import edu.unlpam.vet.ponzonosos.model.Imagen;

import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//import java.util.*;
//import java.awt.Color;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RMD-MainActivity";
    private static final String IP = "170.210.45.164:8080";
    public static final String DIR_IMAGES = "http://"+MainActivity.IP+"/images/";
    private static MainActivity instance;
    private DaoSession mDaoSession;
    private List<String> toImport;
    private SharedPreferences mPrefs;
    private Dialog dialog;

    @Override //metodo main
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_main);
        Log.d("rmdebug", "MainActivity - onCreate");
        showActionDialog();
        toImport = new ArrayList<>();
        instance = this;
        mDaoSession = new DaoMaster(
                new DaoMaster.DevOpenHelper(this, "ponzonosos.db")
                        .getWritableDb()).newSession();
        if(shouldUpdate()){
            Log.d(TAG, "onCreate: I should update data base");
            updateDataBase();
        }else {
            startAplication();
        }
    }

    private void showActionDialog() {

        @SuppressLint("InflateParams")

        View view = getLayoutInflater().inflate(R.layout.dialog_action, null);

        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);

        dialog = new Dialog(this,R.style.CustomToolbarStyle);

        TextView actionName = view.findViewById(R.id.action_name);

        ImageView logo = view.findViewById(R.id.loading_icon);

        actionName.setText(R.string.iniciando);

        dialog.setContentView(view);

        dialog.show();

        Window window = dialog.getWindow();

        if (window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mi_color_loading));

            window.setNavigationBarColor(Color.TRANSPARENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

        }
        //logo.startAnimation(rotation);
        animateTextTyping(actionName, "Iniciando...");

        ObjectAnimator animator = ObjectAnimator.ofFloat(logo, "rotation", 0f, 360f);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();

    }

    private void animateTextTyping(final TextView textView, final String text) {
        final int length = text.length();

        ValueAnimator animator = ValueAnimator.ofInt(0, length);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(animation -> {
            int currentLength = (int) animation.getAnimatedValue();
            textView.setText(text.substring(0, currentLength));
        });

        animator.start();
    }


    private void removeActionDialog(){
        if (dialog != null){
            dialog.dismiss();
        }
    }

    private void startAplication() {
        Intent mIntent = new Intent(this, MostrarCatalogoPruebaActivity.class);
        startActivity(mIntent);
        finish();
        removeActionDialog();
    }

    private boolean shouldUpdate() {
        boolean shouldUpdate = false;
        mPrefs = getSharedPreferences("mPreferences", Context.MODE_PRIVATE);
        long lastUpdate = mPrefs.getLong("lastUpdate",0);
        if (lastUpdate == 0){
            shouldUpdate = true;
        }else {
            Calendar rightNow = Calendar.getInstance();
            Long daysInMilli = 1000L * 60L * 60L * 24L;
            Long difference = rightNow.getTimeInMillis() - lastUpdate;
            long elapsedDays = difference / daysInMilli;
            if (elapsedDays >= 0){
                shouldUpdate = true;
            }
        }
        return shouldUpdate;
    }

    private void updateDataBase() {
        Log.d(TAG, "updateDataBase: Updating data ...");
        updatePreference();
        importData();
    }

    private void updatePreference() {
        Log.d("rmdebug", "MainActivity - updatePreference");
        Calendar rightNow = Calendar.getInstance();
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putLong("lastUpdate", rightNow.getTimeInMillis());
        editor.apply();
    }

    private void importData() {
        toImport = new ArrayList<>();
        toImport.add("animal");
        toImport.add("imagen");
        importEntities();
    }

    private void importEntities() {
        if (!toImport.isEmpty()){
            String imp = toImport.get(0);
            toImport.remove(0);
            switch (imp){
                case "animal":
                    importAnimal();
                    break;
                case "imagen":
                    importImagen();
                    break;
                default:
                    importEntities();
                    break;
            }
        }else {
            startAplication();
        }
    }

    private void importAnimal() {
        Log.d(TAG, "importAnimal: Importing animals...");
        String url = "http://"+MainActivity.IP+"/app/obtener_animales.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                response -> {
                    try {
                        if (response.getString("state").equals("1")) {
                            JSONArray responseJSONArray = response.getJSONArray("animals");
                            Animal animal = new Animal();
                            animal.abmAnimales(responseJSONArray);
                        }else {
                            Log.e(TAG, "onResponse: Error on response, state: " +
                                    response.getString("state"));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: Something went wrong!", e);
                    }
                    importEntities();
                },
                error -> {
                    Log.d(TAG, "onErrorResponse: Error on response: " + error.toString());
                    importEntities();
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void importImagen() {
        Log.d(TAG, "importImagen: Importing images ...");
        String url = "http://"+MainActivity.IP+"/app/obtener_imagenes.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                response -> {
                    try {
                        if (response.getString("state").equals("1")) {
                            JSONArray responseJSONArray = response.getJSONArray("images");
                            Imagen img = new Imagen();
                            img.abmImagenes(responseJSONArray, getApplicationContext());
                        }else{
                            Log.e(TAG, "onResponse: Error on response, state: " +
                                    response.getString("state"));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: Something went wrong!", e);
                    }
                    importEntities();
                },
                error -> {
                    Log.d(TAG, "onErrorResponse: Error on response: " + error.toString());
                    importEntities();
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public static MainActivity getInstance(){
        return instance;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
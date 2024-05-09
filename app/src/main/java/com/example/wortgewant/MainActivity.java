package com.example.wortgewant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.example.wortgewant.BottomSheets.ShowWordList;
import com.example.wortgewant.DataStorage.Data;
import com.example.wortgewant.BottomSheets.AddNewWordBottomSheet;
import com.example.wortgewant.DataStorage.DataManagement;
import com.example.wortgewant.ExternalRequest.ConnectionReceiver;
import com.example.wortgewant.ExternalRequest.VolleyNetworkRequest;
import com.example.wortgewant.Fragments.GameFragment;
import com.example.wortgewant.Fragments.PlayAudioFragment;
import com.example.wortgewant.Interfaces.DatenAnderungMitteilen;
import com.example.wortgewant.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements ConnectionReceiver.ReceiverListener, DatenAnderungMitteilen {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    private Data data;
    private DataManagement dataManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init volley
        VolleyNetworkRequest.getInstance(this);
        //init Data and DataManagement
        DataManagement.getInstance(this).readLocalData(
                Data.getInstance(this));

        StrictMode.ThreadPolicy policy = new StrictMode
                .ThreadPolicy
                .Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);

        // create method
        checkConnection();

        initViews();

    }

    private void initViews(){
        initBottomNavView();


        binding.btnListenManActivity.setOnClickListener(View->{
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("play-pronunciation");

            if(fragment != null && fragment.isAdded())
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            else {
                fragment = getSupportFragmentManager().findFragmentByTag("play-game");
                if (fragment != null && fragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragView_playAudio_mainActivity, new PlayAudioFragment(), "play-pronunciation")
                        .setReorderingAllowed(true)
                        .commit();

            }
        });

        binding.btnWortSpielMainActivity.setOnClickListener(View->{
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("play-game");

            if(fragment != null && fragment.isAdded())
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            else {
                fragment = getSupportFragmentManager().findFragmentByTag("play-pronunciation");
                if (fragment != null && fragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragView_playAudio_mainActivity, new GameFragment(), "play-game")
                        .setReorderingAllowed(true)
                        .commit();

            }
        });


    }

    private void initBottomNavView(){
        Log.d(TAG, "initBottomNavView: started");
        //initial selected item
        binding.bottomNavMainActivity.setSelectedItemId(R.id.navigation_home);

        binding.bottomNavMainActivity.setOnItemSelectedListener(item ->{
            int id = item.getItemId();

            if (id==R.id.navigation_home){
                Log.d(TAG, "initBottomNavView: home button is press");
                //recreate();
                updateSaveWordSizeTextView();
            } else if (id == R.id.add_new_word){
                showAddNewWordBottomSheet();
            } else if (id == R.id.show_all_word){
                //
                showWordList();
            } else if (id == R.id.profile){
                //
            }

            return true;
        });
    } //end of initBottomNavView()

    public void showWordList(){
        ShowWordList dialog = new ShowWordList(this);
        dialog.show();
    }

    public void showAddNewWordBottomSheet() {
        AddNewWordBottomSheet dialog = new AddNewWordBottomSheet(this);
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RequestQueue requestQueue =  VolleyNetworkRequest.getInstance(this).getQueue();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    @Override
    public void onAnderungen(){
        DataManagement.getInstance(this).writeLocalData( (Void)->{
            Log.d(TAG, "onAnderungen: called");
            updateSaveWordSizeTextView();
        });
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        // display snack bar
        showSnackBar(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // call method
        checkConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // call method
        checkConnection();
    }

    private void updateSaveWordSizeTextView(){
        binding.tvTotalWordsMainActivity.setText(String.valueOf(Data.getInstance().getDataSize()).concat(" Words"));
    }

    @SuppressLint("InlinedApi")
    private void checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        registerReceiver(new ConnectionReceiver(), intentFilter, Context.RECEIVER_NOT_EXPORTED);

        // Initialize listener
        ConnectionReceiver.Listener = this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        // display snack bar
        //showSnackBar(isConnected);
    }




    private void showSnackBar(boolean isConnected) {

        // initialize color and message
        String message;
        int color;

        // check condition
        if (isConnected) {

            // when internet is connected
            // set message
            message = "Connected to Internet";

            // set text color
            color = Color.WHITE;

        } else {

            // when internet
            // is disconnected
            // set message
            message = "Not Connected to Internet";

            // set text color
            color = Color.RED;
        }

        // initialize snack bar
        Snackbar snackbar = Snackbar.make(findViewById(R.id.btn_wortSpiel_mainActivity), message, Snackbar.LENGTH_LONG);

        // initialize view
        View view = snackbar.getView();


        // show snack bar
        snackbar.show();
    }
}


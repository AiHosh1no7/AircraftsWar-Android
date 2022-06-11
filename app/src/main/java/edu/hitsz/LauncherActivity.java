package edu.hitsz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import edu.hitsz.databinding.ActivityLauncherBinding;
import edu.hitsz.socket.PlayerStatus;

public class LauncherActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityLauncherBinding binding;

    private RadioGroup diffChoice;
    private Switch audioSwitch;
    private Switch networkSwitch;

    public Socket socket;
    public PlayerStatus localPlayer;
    public PlayerStatus opposite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLauncherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        diffChoice = findViewById(R.id.diffGroup);
        audioSwitch = findViewById(R.id.audioSwitch);
        networkSwitch = findViewById(R.id.networkSwitch);

        setSupportActionBar(binding.appBarLauncher.toolbar);
        binding.appBarLauncher.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!networkSwitch.isChecked()) {
                    Intent startGame = new Intent(LauncherActivity.this, MainActivity.class);
                    switch (diffChoice.getCheckedRadioButtonId()) {
                        case R.id.easyButton: {
                            startGame.putExtra("diff", 0);
                            break;
                        }
                        case R.id.mediumButton: {
                            startGame.putExtra("diff", 1);
                            break;
                        }
                        case R.id.hardButton: {
                            startGame.putExtra("diff", 2);
                            break;
                        }
                    }
                    startGame.putExtra("audio", audioSwitch.isChecked());
                    startActivity(startGame);
                } else {
                    showWaitingDialog();
                    new Thread(new NetConn()).start();
                    while(opposite == null);
                    Intent startGame = new Intent(LauncherActivity.this, MainActivity.class);
                    switch (diffChoice.getCheckedRadioButtonId()) {
                        case R.id.easyButton: {
                            startGame.putExtra("diff", 0);
                            break;
                        }
                        case R.id.mediumButton: {
                            startGame.putExtra("diff", 1);
                            break;
                        }
                        case R.id.hardButton: {
                            startGame.putExtra("diff", 2);
                            break;
                        }
                    }
                    startGame.putExtra("audio", audioSwitch.isChecked());
                    startActivity(startGame);
                }
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_launcher);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launcher, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_launcher);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void showWaitingDialog() {
        ProgressDialog waiting = new ProgressDialog(this);
        waiting.setTitle("等待玩家连接");
        waiting.setMessage("少女祈祷中");
        waiting.setCancelable(false);
        waiting.setIndeterminate(true);
        waiting.show();
    }

    protected class NetConn extends Thread {
        @Override
        public void run() {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("10.250.152.127",9999), 5000);
                localPlayer = new PlayerStatus();
                localPlayer.setPlayerID("test");

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(localPlayer);
                oos.close();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                opposite = (PlayerStatus) ois.readObject();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
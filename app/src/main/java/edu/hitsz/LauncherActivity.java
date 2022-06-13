package edu.hitsz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import edu.hitsz.dao.Player;
import edu.hitsz.databinding.ActivityLauncherBinding;
import edu.hitsz.socket.PlayerStatus;

public class LauncherActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityLauncherBinding binding;

    private RadioGroup diffChoice;
    private Switch audioSwitch;
    private Switch networkSwitch;
    private TextView playerIdText;

    public static Socket socket;
    public PlayerStatus localPlayer;
    public static PlayerStatus rivalPlayer;
    public static String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLauncherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        diffChoice = findViewById(R.id.diffGroup);
        audioSwitch = findViewById(R.id.audioSwitch);
        networkSwitch = findViewById(R.id.networkSwitch);
        playerIdText = findViewById(R.id.playerIdText);

        setSupportActionBar(binding.appBarLauncher.toolbar);
        binding.appBarLauncher.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerName = playerIdText.getText().toString();
                if(networkSwitch.isChecked()) {
                    ProgressDialog waiting = showWaitingDialog();
                    connecting();
                }
                startGameActivity();
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

    public ProgressDialog showWaitingDialog() {
        ProgressDialog waiting = new ProgressDialog(this);
        waiting.setTitle("等待玩家连接");
        waiting.setMessage("少女祈祷中");
        waiting.setCancelable(false);
        waiting.setIndeterminate(true);
        waiting.show();

        return waiting;
    }

    public void sendLocalMessage(PlayerStatus player) {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            writer.println("Player START");
            writer.println(player.getPlayerID());
            writer.println(player.getPlayerScore());
            writer.println(player.getPlayerReady());
            writer.println("Player END");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void connecting() {
        Callable<PlayerStatus> callable = new Callable<PlayerStatus>() {
            @Override
            public PlayerStatus call() throws Exception {
                socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress("10.249.58.38", 9999), 5000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                localPlayer = new PlayerStatus(playerName, 0, true);
                System.out.println(localPlayer.getPlayerScore());
                sendLocalMessage(localPlayer);
                return receiveRivalMessage();
            }
        };
        FutureTask<PlayerStatus> task = new FutureTask<>(callable);
        new Thread(task).start();

        try {
            rivalPlayer = task.get();
        } catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("RIVAL INFO----------");
        System.out.println(rivalPlayer.getPlayerScore());
    }

    public PlayerStatus receiveRivalMessage() {
        PlayerStatus player = new PlayerStatus("", 0, false);
        String content;
        try {
            BufferedReader b_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while((content = b_in.readLine()) != null) {
                if(content.equals("Player START")) {
                    player.setPlayerID(b_in.readLine());
                    player.setPlayerScore(Integer.parseInt(b_in.readLine()));
                    player.setPlayerReady(Boolean.parseBoolean(b_in.readLine()));
                    if(b_in.readLine().equals("Player END")) {
                        return player;
                    }
                } else if(content.equals("WAITING")) {
                    continue;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startGameActivity() {
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
        startGame.putExtra("online", networkSwitch.isChecked());
        startActivity(startGame);
    }
}
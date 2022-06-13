package edu.hitsz.application;

import edu.hitsz.LauncherActivity;
import edu.hitsz.MainActivity;
import edu.hitsz.R;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.dao.Player;
import edu.hitsz.factory.*;
import edu.hitsz.item.AbstractItem;
import edu.hitsz.socket.PlayerStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    public final HeroAircraft heroAircraft;
    public static List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    public static List<BaseBullet> enemyBullets;
    private final List<AbstractItem> itemList;
    final EliteEnemyFactory eliteFactory = new EliteEnemyFactory();
    final MobEnemyFactory mobFactory = new MobEnemyFactory();
    final BossEnemyFactory bossFactory = new BossEnemyFactory();
    private final HealingItemFactory healingItemFactory = new HealingItemFactory();
    private final FireSupplyItemFactory fireSupplyItemFactory = new FireSupplyItemFactory();
    private final BombSupplyItemFactory bombSupplyItemFactory = new BombSupplyItemFactory();
    public boolean onlineMode;
    private PlayerStatus rivalPlayer;
    public boolean victoryFlag;

    int enemyMaxNumber = 5;

    public boolean gameOverFlag = false;

    int bossHp = 1500;
    double bossHpRate = 1.5;
    int mobHp = 100;
    int eliteHp = 200;
    public int score = 0;
    private int time = 0;
    Context context;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 500;
    private int cycleTime = 0;
    int eliteGenerationFlag = 0;
    int mobGenerationFlag = 0;

    MediaPlayer bgmPlayer;
    MediaPlayer bossBgmPlayer;
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();

    int shootPeriodFlag = 0;
    int shootPeriodLimit = 5;
    int bossScore = 0;
    private double shootPeriodRate = 0.8;
    private double maxNumberRate = 1.2;
    private double enemyGenerationRate = 0.8;
    int mobGenerationLimit = 5;
    int eliteGenerationLimit = 20;
    int bossGenerationLimit = 200;

    boolean bossGenerationFlag = true;

    boolean mbLoop = false; //控制绘画线程的标志位
    private SurfaceHolder mSurfaceHolder;
    private Canvas canvas;  //绘图的画布
    private Paint mPaint;
    private Socket socket;

    public Game(Context context) {
        super(context);
        this.context = context;
        bgmPlayer = MediaPlayer.create(context, R.raw.bgm);
        bgmPlayer.setVolume(1, 1);
        bossBgmPlayer = MediaPlayer.create(context, R.raw.bgm_boss);
        bossBgmPlayer.setVolume(1, 1);
        mSoundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
        soundID.put(1, mSoundPool.load(context, R.raw.bullet_hit, 1));
        soundID.put(2, mSoundPool.load(context, R.raw.game_over, 1));
        soundID.put(3, mSoundPool.load(context, R.raw.bomb_explosion, 1));
        soundID.put(4, mSoundPool.load(context, R.raw.bullet, 1));
        soundID.put(5, mSoundPool.load(context, R.raw.get_supply, 1));
        soundID.put(6, mSoundPool.load(context, R.raw.bgm, 1));
        soundID.put(7, mSoundPool.load(context, R.raw.bgm_boss, 1));
        mbLoop = true;
        mPaint = new Paint();  //设置画笔
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);

        heroAircraft = HeroAircraft.getInstance();
        heroAircraft.setStatus(
                MainActivity.screenWidth / 2,
                MainActivity.screenHeight - ImageManager.HERO_IMAGE.getHeight() ,
                0, 0, 1000);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        itemList = new LinkedList<>();

        //Scheduled 线程池，用于定时任务调度
        ThreadFactory gameThread = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("game thread");
                return t;
            }
        };
        executorService = new ScheduledThreadPoolExecutor(1, gameThread);
    }

    @Override
    public void run() {
        //设置一个循环来绘制，通过标志位来控制开启绘制还是停止
        while (mbLoop){
            synchronized (mSurfaceHolder){
                draw();
            }
        }
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        new Thread(this).start();
    }
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        MainActivity.screenWidth = width;
        MainActivity.screenHeight = height;
    }
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        mbLoop = false;
    }

    public abstract void difficultyTag();

    public abstract void generateEnemy();
    public abstract void difficultyEvolve();

    public static void stopMusic(MediaPlayer player) {
        if (player != null) {
            player.stop();
            player.reset();//重置
            player.release();//释放
        }
    }

    public void playBullet(){
        mSoundPool.play(soundID.get(4), 0.5f, 0.5f, 0,0,1);
    }

    public void playGameOver(){
        mSoundPool.play(soundID.get(2), 0.5f, 0.5f, 0, 0, 1);
    }

    public void playBulletHit() {
        mSoundPool.play(soundID.get(1), 0.5f, 0.5f, 0, 0, 1);
    }

    public void playBombExplosion() {
        mSoundPool.play(soundID.get(3), 0.5f, 0.5f, 0, 0, 1);
    }

    public void playGetSupply() {
        mSoundPool.play(soundID.get(5), 0.5f, 0.5f, 0, 0, 1);
    }

    public void connecting() {
        Runnable conn = new Runnable() {
            @Override
            public void run() {
                socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress("10.249.58.38", 9999), 5000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(conn).start();
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        // connecting();

        if(MainActivity.bgmFlag) {
            bgmPlayer.start();
            bgmPlayer.setLooping(true);
        }
        difficultyTag();
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            System.out.println(time);
            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                // System.out.println(time);
                generateEnemy();
                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            //道具移动
            itemMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            // 联机通讯
            if(onlineMode) {
                communicationAction();
            }

            // 游戏结束检查
            if ((!onlineMode && heroAircraft.getHp() <= 0) || (onlineMode && (!rivalPlayer.getPlayerReady() || heroAircraft.getHp() <= 0))) {
                // 游戏结束
                victoryFlag = !(heroAircraft.getHp() <= 0);
                if(MainActivity.bgmFlag) {
                    stopMusic(bgmPlayer);
                    stopMusic(bossBgmPlayer);
                    playGameOver();
                }
                gameOverFlag = true;
                executorService.shutdown();
                System.out.println("Game Over!");
            }
        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void communicationAction() {
        PlayerStatus localPlayer = new PlayerStatus(LauncherActivity.playerName, score, heroAircraft.getHp() > 0 ? true : false);
        sendLocalMessage(localPlayer);
        rivalPlayer = receiveRivalMessage();
    }

    public void sendLocalMessage(PlayerStatus player) {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(LauncherActivity.socket.getOutputStream())), true);
            writer.println("Player START");
            writer.println(player.getPlayerID());
            writer.println(player.getPlayerScore());
            writer.println(player.getPlayerReady());
            writer.println("Player END");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerStatus receiveRivalMessage() {
        PlayerStatus player = new PlayerStatus("", 0, false);
        String content;
        try {
            BufferedReader b_in = new BufferedReader(new InputStreamReader(LauncherActivity.socket.getInputStream()));
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

    private void shootAction() {
        // 敌机射击
        if(shootPeriodFlag == shootPeriodLimit) {
            for (AbstractAircraft enemy : enemyAircrafts) {
                enemyBullets.addAll(enemy.shoot());
            }

            shootPeriodFlag = 0;
        }
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
        if(MainActivity.bgmFlag) {
            playBullet();
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void itemMoveAction() {
        for(AbstractItem item: itemList) {
            item.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // 敌机子弹攻击英雄
        for(BaseBullet bullet: enemyBullets) {
            if(bullet.notValid()) {
                continue;
            }

            if(heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());

                if(MainActivity.bgmFlag) {
                    playBulletHit();
                }
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值

                    if(MainActivity.bgmFlag) {
                        playBulletHit();
                    }

                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
                        if(enemyAircraft.eliteFlag) {
                            score += 20;
                            bossScore += bossGenerationFlag ? 20 : 0;
                        } else if(enemyAircraft.bossFlag) {
                            score += 50;
                            bossScore += 0;
                        } else {
                            score += 10;
                            bossScore += bossGenerationFlag ? 10 : 0;
                        }
                        if(enemyAircraft.bossFlag) {
                            bossGenerationFlag = true;

                            if(MainActivity.bgmFlag) {
                                stopMusic(bossBgmPlayer);
                                bossBgmPlayer = MediaPlayer.create(context, R.raw.bgm_boss);
                                bossBgmPlayer.setVolume(1, 1);
                                bgmPlayer.start();
                                bgmPlayer.setLooping(true);
                            }

                            difficultyEvolve();
                        }

                        double prob = Math.random();
                        AbstractItem newItem = null;
                        if (prob < 0.3) {
                            newItem = enemyAircraft.dropItem(healingItemFactory);
                        } else if (prob >= 0.3 && prob <= 0.6) {
                            newItem = enemyAircraft.dropItem(fireSupplyItemFactory);
                        } else if (prob > 0.6 && prob <= 0.9) {
                            newItem = enemyAircraft.dropItem(bombSupplyItemFactory);
                        }

                        if(newItem != null) {
                            itemList.add(newItem);
                        }

                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for(AbstractItem item: itemList) {
            if(item.notValid()) {
                continue;
            }

            if(heroAircraft.crash(item)) {

                if(MainActivity.bgmFlag && !item.isBomb) {
                    playGetSupply();
                } else if(MainActivity.bgmFlag && item.isBomb) {
                    playBombExplosion();
                }

                item.itemFunction();
                item.vanish();
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        itemList.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     */
    public void draw() {
        canvas = mSurfaceHolder.lockCanvas();
        if(mSurfaceHolder == null || canvas == null){
            return;
        }

        canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - ImageManager.BACKGROUND_IMAGE.getHeight(), mPaint);
        canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, mPaint);

        backGroundTop += 1;

        if(backGroundTop == ImageManager.BACKGROUND_IMAGE.getHeight()) {
            backGroundTop = 0;
        }

        paintImageWithPositionRevised(enemyBullets);
        paintImageWithPositionRevised(itemList);
        paintImageWithPositionRevised(heroBullets);
        paintImageWithPositionRevised(enemyAircrafts);

        canvas.drawBitmap(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, mPaint);

        paintScoreAndLife();

        mSurfaceHolder.unlockCanvasAndPost(canvas);

    }

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (int i = 0; i < objects.size(); i++) {
            Bitmap image = objects.get(i).getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image, objects.get(i).getLocationX() - image.getWidth() / 2,
                    objects.get(i).getLocationY() - image.getHeight() / 2, mPaint);
        }
    }

    private void paintScoreAndLife() {
        int x = 10;
        int y = 100;
        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(12);
        textPaint.setTextSize(80);
        textPaint.setColor(Color.parseColor("red"));
        canvas.drawText("SCORE:" + this.score, x, y, textPaint);
        y = y + 100;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, textPaint);

        if(onlineMode) {
            y = y + 100;
            if(rivalPlayer == null) {
                rivalPlayer = LauncherActivity.rivalPlayer;
            }
            canvas.drawText("RIVAL SCORE:" + rivalPlayer.getPlayerScore(), x, y, textPaint);
        }
    }
}

package edu.hitsz.application;

import android.content.Context;

import edu.hitsz.MainActivity;

public class GameEasy extends Game{
    public GameEasy(Context context) {
        super(context);
    }

    @Override
    public void difficultyTag() {
        System.out.println("当前难度:EASY");
        System.out.println("无BOSS机生成;难度不随时间变化");
    }

    public void difficultyEvolve(){}
    public void generateEnemy() {
        System.out.println("Generation Flag: " + mobGenerationFlag);
        if(eliteGenerationFlag == eliteGenerationLimit) {
            eliteGenerationFlag = 0;
            if(enemyAircrafts.size() < enemyMaxNumber) {
                enemyAircrafts.add(eliteFactory.createEnemy(
                        (int) ( Math.random() * (MainActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                        (int) (Math.random() * MainActivity.screenHeight * 0.2)*1,
                        Math.random() > 0.3 ? 4 : 0,
                        4,
                        200
                ));
            }
        }
        // 新敌机产生
        if(mobGenerationFlag == mobGenerationLimit) {
            mobGenerationFlag = 0;
            if (enemyAircrafts.size() < enemyMaxNumber) {
                enemyAircrafts.add(mobFactory.createEnemy(
                        (int) (Math.random() * (MainActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
                        (int) (Math.random() * MainActivity.screenHeight * 0.2) * 1,
                        0,
                        10,
                        100
                ));
            }
        }
        eliteGenerationFlag++;
        mobGenerationFlag++;
        shootPeriodFlag++;
    }
}

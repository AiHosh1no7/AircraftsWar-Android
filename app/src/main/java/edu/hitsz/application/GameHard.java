package edu.hitsz.application;

import static edu.hitsz.application.Main.*;

public class GameHard extends Game{
    private double shootPeriodRate = 0.8;
    private double maxNumberRate = 1.2;
    private double enemyGenerationRate = 0.8;
    private double enemyHpRate = 1.2;
    public void difficultyTag() {
        System.out.println("当前难度:HARD");
        System.out.println("有BOSS机生成;难度随时间增长变化");
        System.out.println("BOSS机生命增长系数:" + bossHpRate);
        System.out.println("普通&精英敌机生命值增长系数:" + enemyHpRate);
        System.out.println("敌机生成周期缩短系数:" + enemyGenerationRate);
        System.out.println("敌机攻击周期缩短系数:" + shootPeriodRate);
        System.out.println("BOSS机生成得分阈值减少系数:" + enemyGenerationRate);
        System.out.println("同一时刻出现敌机最大值增长系数:" + maxNumberRate);
    }

    public void generateEnemy() {
        if(bossScore >= bossGenerationLimit && bossGenerationFlag) {
            if(bgmFlag) {
                bossThread = new MusicThread("src/videos/bgm_boss.wav");
                bossThread.start();
                bossThread.setLoop(true);
                bgmThread.over();
            }
            bossGenerationFlag = false;
            bossScore -= 200;
            bossHp = (int)(bossHp * bossHpRate);
            enemyAircrafts.add(bossFactory.createEnemy(
                    (int) (Math.random() * (WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
                    0,
                    1,
                    0,
                    bossHp
            ));
        }
        if(eliteGenerationFlag == eliteGenerationLimit) {
            eliteGenerationFlag = 0;
            if(enemyAircrafts.size() < enemyMaxNumber) {
                enemyAircrafts.add(eliteFactory.createEnemy(
                        (int) ( Math.random() * (WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                        (int) (Math.random() * WINDOW_HEIGHT * 0.2)*1,
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
                        (int) (Math.random() * (WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
                        (int) (Math.random() * WINDOW_HEIGHT * 0.2) * 1,
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

    public void difficultyEvolve() {
        shootPeriodLimit = (int) (shootPeriodLimit * shootPeriodRate);
        enemyMaxNumber = (int)(enemyMaxNumber * maxNumberRate);
        mobHp = (int)(mobHp * enemyHpRate);
        eliteHp = (int)(eliteHp * enemyHpRate);
        mobGenerationLimit = (int)(mobGenerationLimit * enemyGenerationRate);
        eliteGenerationLimit = (int)(eliteGenerationLimit * enemyGenerationRate);
        bossGenerationLimit = (int)(bossGenerationLimit * enemyGenerationRate);
    }
}

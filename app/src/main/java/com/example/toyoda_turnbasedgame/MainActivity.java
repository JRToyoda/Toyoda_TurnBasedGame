package com.example.toyoda_turnbasedgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button skill1,skill2,skill3,skill4,start,skillInfo,skillInfo2;
    TextView hp,mp,name,hp2,mp2,name2, dialogue;
    ProgressBar hpBar,mpBar,hpBar2,mpBar2;
    FrameLayout heroStatsLayout,monsStatsLayout,controller,infoTextBox,infoTextBox2;
    ConstraintLayout ctrlBox;
    MediaPlayer mediaPlayer, atk;

    String heroName = "Slark";
    int heroHP = 1000;
    int heroMP = 100;
    int maxHP = 1000;
    int maxMP = 100;
    int heroAtk;
    int heroHPPercent,heroMPPercent;

    String monsName = "Huskar";
    int monsHP = 1500;
    int monsMP = 100;
    int maxHP2 = 1500;
    int maxMP2 = 100;
    int monsAtk;
    int monsHPPercent,monsMPPercent;

    //strike skill stats
    int strikeMaxDmg = 100;
    int strikeMinDmg = 50;
    int strikeChance = 75;
    int strikeHpCost = 100;

    //life steal skill stats
    int lifeStealMaxDmg = 250;
    int lifeStealMinDmg = 150;
    int lifeStealChance = 50;
    int lifeStealMpCost = 30;

    //stun skill stats
    int stunMaxDmg = 60;
    int stunMinDmg = 40;
    int stunChance = 75;
    int stunHeroMpCost = 20;
    int stunMonsMpCost = 40;
    int heroStunned, monsStunned;

    //regen skill stats
    int regenChance = 50;
    int regenMpCost = 30;

    //super strike skill stats
    int superStrikeMaxDmg = 250;
    int superStrikeMinDmg = 150;
    int superStrikeChance = 50;
    int superStrikeHpCost = 200;

    //inferno skill stats
    int infernoDmg = 500;
    int infernoChance = 50;
    int infernoFail = 300;

    boolean strike = false;
    boolean lifeSteal = false;
    boolean stun = false;
    boolean regen = false;
    boolean win = false;

    int turn = 1;
    int rng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //buttons
        start = findViewById(R.id.startTurn);
        skill1 = findViewById(R.id.strike);
        skill2 = findViewById(R.id.lifeSteal);
        skill3 = findViewById(R.id.stun);
        skill4 = findViewById(R.id.regen);
        skillInfo = findViewById(R.id.skillInfo);
        skillInfo2 = findViewById(R.id.skillInfo2);

        //textView
        hp = findViewById(R.id.hp);
        mp = findViewById(R.id.mp);
        name = findViewById(R.id.name);
        hp2 = findViewById(R.id.hp2);
        mp2 = findViewById(R.id.mp2);
        name2 = findViewById(R.id.name2);
        dialogue = findViewById(R.id.dialogue);
        hp.setText(String.valueOf(heroHP));
        mp.setText(String.valueOf(heroMP));
        hp2.setText(String.valueOf(monsHP));
        mp2.setText(String.valueOf(monsMP));
        name.setText(String.valueOf(heroName));
        name2.setText(String.valueOf(monsName));

        //progressBar
        hpBar = findViewById(R.id.hpBar);
        mpBar = findViewById(R.id.mpBar);
        hpBar2 = findViewById(R.id.hpBar2);
        mpBar2 = findViewById(R.id.mpBar2);
        hpBar.setMax(100);
        mpBar.setMax(100);
        hpBar2.setMax(100);
        mpBar2.setMax(100);

        //layouts
        heroStatsLayout = findViewById(R.id.heroStatsLayout);
        monsStatsLayout = findViewById(R.id.monsStatsLayout);
        controller = findViewById(R.id.controller);
        ctrlBox = findViewById(R.id.ctrlBox);
        infoTextBox = findViewById(R.id.infoTextBox);
        infoTextBox2 = findViewById(R.id.infoTextBox2);

        //onClickListeners
        start.setOnClickListener(this);
        skill1.setOnClickListener(this);
        skill2.setOnClickListener(this);
        skill3.setOnClickListener(this);
        skill4.setOnClickListener(this);

        start();
        mediaPlayer();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.strike:
                strike = true;
                skill();
                break;
            case R.id.lifeSteal:
                if (heroMP - lifeStealMpCost >= 0) { //checking if hero has mana
                    lifeSteal = true;
                    skill();
                } else {
                    dialogue.setText("Not enough Mana");
                    buttonInvisible();
                }
                break;
            case R.id.stun:
                if (heroMP - stunHeroMpCost >= 0) { //checking if hero has mana
                    stun = true;
                    skill();
                } else {
                    dialogue.setText("Not enough Mana");
                    buttonInvisible();
                }
                break;
            case R.id.regen:
                if (heroMP - regenMpCost >= 0) { //checking if hero has mana
                    regen = true;
                    skill();
                } else {
                    dialogue.setText("Not enough Mana");
                    buttonInvisible();
                }
                break;
            case R.id.startTurn:
                postStart();
        }
    }

    // starting screen
    public void start() {
        //buttons
        start.setVisibility(View.VISIBLE);
        skill1.setVisibility(View.GONE);
        skill2.setVisibility(View.GONE);
        skill3.setVisibility(View.GONE);
        skill4.setVisibility(View.GONE);
        skillInfo.setVisibility(View.GONE);
        skillInfo2.setVisibility(View.GONE);
        //enabling & disabling buttons
        start.setClickable(true);
        skill1.setClickable(false);
        skill2.setClickable(false);
        skill3.setClickable(false);
        skill4.setClickable(false);
        skillInfo.setClickable(false);
        skillInfo2.setClickable(false);
        //layouts
        heroStatsLayout.setVisibility(View.INVISIBLE);
        monsStatsLayout.setVisibility(View.INVISIBLE);
        controller.setVisibility(View.GONE);
    }

    // BGM
    public void mediaPlayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.turnbasedgame_bgm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    // attack sound
    public void atk() {
        atk = MediaPlayer.create(MainActivity.this,R.raw.attacksound);
        atk.start();
    }

    // fighting screen
    public void postStart() {
        start.setVisibility(View.GONE);
        heroStatsLayout.setVisibility(View.VISIBLE);
        monsStatsLayout.setVisibility(View.VISIBLE);
        controller.setVisibility(View.VISIBLE);
        skillInfo.setVisibility(View.VISIBLE);
        skillInfo2.setVisibility(View.VISIBLE);
        start.setClickable(false);
        skillInfo.setClickable(true);
        skillInfo2.setClickable(true);
        turnSystem();
    }

    // turn system
    public void turnSystem() {
        if (turn == 1) {
            buttonVisible();
        } else {
            buttonInvisible();
        }
    }

    public void next(View v) {
        turnSystem();
        skill();
    }

    //Slark skill info
    public void info(View v) {
        if(infoTextBox.getVisibility() == View.GONE){
            infoTextBox.setVisibility(View.VISIBLE);
        } else {
            infoTextBox.setVisibility(View.GONE);
        }
    }

    //Huskar skill info
    public void info2(View v) {
        if(infoTextBox2.getVisibility() == View.GONE){
            infoTextBox2.setVisibility(View.VISIBLE);
        } else {
            infoTextBox2.setVisibility(View.GONE);
        }
    }

    public void buttonVisible(){
        skill1.setVisibility(View.VISIBLE);
        skill2.setVisibility(View.VISIBLE);
        skill3.setVisibility(View.VISIBLE);
        skill4.setVisibility(View.VISIBLE);
        skill1.setClickable(true);
        skill2.setClickable(true);
        skill3.setClickable(true);
        skill4.setClickable(true);
        dialogue.setVisibility(View.GONE);
        ctrlBox.setClickable(false);
    }

    public void buttonInvisible(){
        skill1.setVisibility(View.GONE);
        skill2.setVisibility(View.GONE);
        skill3.setVisibility(View.GONE);
        skill4.setVisibility(View.GONE);
        skill1.setClickable(false);
        skill2.setClickable(false);
        skill3.setClickable(false);
        skill4.setClickable(false);
        dialogue.setVisibility(View.VISIBLE);
        ctrlBox.setClickable(true);
    }

    public void progressBar() {
        hp.setText(String.valueOf(heroHP));
        mp.setText(String.valueOf(heroMP));
        hp2.setText(String.valueOf(monsHP));
        mp2.setText(String.valueOf(monsMP));
        heroHPPercent = heroHP * 100 / maxHP;
        heroMPPercent = heroMP * 100 / maxMP;
        monsHPPercent = monsHP * 100 / maxHP2;
        monsMPPercent = monsMP * 100 / maxMP2;
        hpBar.setProgress(heroHPPercent, true);
        mpBar.setProgress(heroMPPercent, true);
        hpBar2.setProgress(monsHPPercent, true);
        mpBar2.setProgress(monsMPPercent, true);
    }

    public void skill() {
        Random randomizer = new Random();
        rng = randomizer.nextInt(100) + 1;
        if (turn == 1) {
            if (heroStunned > 0) { //checks if slark is stunned
                dialogue.setText(heroName + " is stunned");
                heroStunned--;
                progressBar();
                buttonInvisible();
                turn++;
            } else {
                    if (strike) { //slark skill 1
                        if (rng < strikeChance) {
                            heroAtk = randomizer.nextInt(strikeMaxDmg - strikeMinDmg) + strikeMinDmg;
                            monsHP -= heroAtk;
                            dialogue.setText(heroName + " used Strike");
                            if (monsHP < 0) {
                                monsHP = 0;
                            }
                            if (heroMP != maxMP && heroMP < maxMP) {
                                heroMP += 20;
                            }
                            atk();
                        } else {
                            dialogue.setText(heroName + "'s Strike missed");
                            heroHP -= strikeHpCost;
                            if (heroHP < 0) {
                                heroHP = 0;
                            }
                            if (monsMP != maxMP2 && monsMP < maxMP2) {
                                monsMP += 5;
                            }
                        }
                        strike = false;
                        progressBar();
                        buttonInvisible();
                        turn++;
                    }
                    if (lifeSteal) { //slark skill 2
                        if (rng < lifeStealChance) {
                            heroAtk = randomizer.nextInt(lifeStealMaxDmg - lifeStealMinDmg) + lifeStealMinDmg;
                            monsHP -= heroAtk;
                            heroHP += heroAtk;
                            dialogue.setText(heroName + " used Life Steal");
                            if (monsHP < 0) {
                                monsHP = 0;
                            }
                            if (heroHP > maxHP) {
                                heroHP = maxHP;
                            }
                            atk();
                        } else {
                            dialogue.setText(heroName + "'s Life Steal missed");
                            if (monsMP != maxMP2 && monsMP < maxMP2) {
                                monsMP += 5;
                            }
                        }
                        heroMP -= lifeStealMpCost;
                        lifeSteal = false;
                        progressBar();
                        buttonInvisible();
                        turn++;
                    }
                    if (stun) {
                        if (rng < stunChance) { //slark skill 3
                            heroAtk = randomizer.nextInt(stunMaxDmg - stunMinDmg) + stunMinDmg;
                            monsHP -= heroAtk;
                            monsStunned = 1;
                            dialogue.setText(heroName + " used Stun");
                            if (monsHP < 0) {
                                monsHP = 0;
                            }
                            atk();
                        } else {
                            dialogue.setText(heroName + "'s Stun missed");
                            if (monsMP != maxMP2 && monsMP < maxMP2) {
                                monsMP += 5;
                            }
                        }
                        heroMP -= stunHeroMpCost;
                        stun = false;
                        progressBar();
                        buttonInvisible();
                        turn++;
                    }
                    if (regen) { //slark skill 4
                        if (rng < regenChance) {
                            heroAtk = maxHP - heroHP;
                            heroHP += heroAtk;
                            dialogue.setText(heroName + " used Regeneration");
                        } else {
                            dialogue.setText(heroName + " failed to Regenerate");
                            heroHP -= heroHP;
                        }
                        heroMP -= regenMpCost;
                        regen = false;
                        atk();
                        progressBar();
                        buttonInvisible();
                        turn++;
                    }
            }
            if (monsHP <= 0) { //slark wins
                win = true;
                reset();
                progressBar();
            }
        } else {
            buttonInvisible();
            if (monsStunned > 0) { //checks if huskar is stunned
                dialogue.setText(monsName + " has been stunned");
                monsStunned--;
                progressBar();
            } else {
                monsSkills();
            }
            turn--;
            if (heroHP <= 0) { //huskar wins
                reset();
                progressBar();
            }
        }
    }

    public void monsSkills() {
        Random randomizer = new Random();
        rng = randomizer.nextInt(100) + 1;
        int monsSkill = randomizer.nextInt(4);
        switch (monsSkill) {
            case 0:
                if (rng < strikeChance) { //huskar skill 1
                    monsAtk = randomizer.nextInt(strikeMaxDmg - strikeMinDmg) + strikeMinDmg;
                    dialogue.setText(monsName + " used Strike");
                    heroHP -= monsAtk;
                    if (heroHP < 0) {
                        heroHP = 0;
                    }
                    if (monsMP != maxMP2 && monsMP < maxMP2) {
                        monsMP += 10;
                    }
                    atk();
                } else {
                    dialogue.setText(monsName + "'s Strike missed");
                    monsHP -= strikeHpCost;
                    if (monsHP < 0) {
                        monsHP = 0;
                    }
                    if (heroMP != maxMP && heroMP < maxMP) {
                        heroMP += 10;
                    }
                }
                progressBar();
                break;
            case 1:
                if (rng < superStrikeChance) { //huskar skill 2
                    monsAtk = randomizer.nextInt(superStrikeMaxDmg - superStrikeMinDmg) + superStrikeMinDmg;
                    heroHP -= monsAtk;
                    dialogue.setText(monsName + " used Super Strike");
                    if (heroHP < 0) {
                        heroHP = 0;
                    }
                    if (monsMP != maxMP2 && monsMP < maxMP2) {
                        monsMP += 20;
                    }
                    atk();
                } else {
                    dialogue.setText(monsName + "'s Super Strike missed");
                    if (heroMP != maxMP && heroMP < maxMP) {
                        heroMP += 10;
                    }
                }
                monsHP -= superStrikeHpCost;
                if (monsHP < 0) {
                    monsHP = 0;
                }
                progressBar();
                break;
            case 2:
                monsStun();
                break;
            case 3:
                monsUlt();
                break;
        }
    }

    public void monsStun() {
        Random randomizer = new Random();
        rng = randomizer.nextInt(100) + 1;
        if (monsMP >= stunMonsMpCost) { //huskar skill 3
            if (rng < stunChance) {
                monsAtk = randomizer.nextInt(stunMaxDmg - stunMinDmg) + stunMinDmg;
                heroHP -= monsAtk;
                heroStunned = 1;
                dialogue.setText(monsName + " used Stun");
                if (heroHP < 0) {
                    heroHP = 0;
                }
                atk();
            } else {
                dialogue.setText(monsName + "'s stun missed");
                if (heroMP != maxMP && heroMP < maxMP) {
                    heroMP += 10;
                }
            }
            monsMP -= stunMonsMpCost;
            progressBar();
        } else {
            monsSkills();
        }
    }

    public void monsUlt() {
        Random randomizer = new Random();
        rng = randomizer.nextInt(100) + 1;
        if (rng < 50) {
            if (rng < infernoChance) { //huskar skill 4
                heroHP -= infernoDmg;
                dialogue.setText(monsName + " used Inferno");
                if (heroHP < 0) {
                    heroHP = 0;
                }
                atk();
            } else {
                dialogue.setText(monsName + "'s Inferno missed");
                if (monsHP != 0 && monsHP > 0) {
                    monsHP -= infernoFail;
                    if (monsHP < 0) {
                        monsHP = 0;
                    }
                }
            }
            progressBar();
        } else {
            monsSkills();
        }
    }
    public void reset() { //resets the game
        if (win) {
            dialogue.setText(heroName + " wins!");
            win = false;
        } else {
            dialogue.setText(monsName + " wins!");
        }
        heroHP = maxHP;
        heroMP = maxMP;
        monsHP = maxHP2;
        monsMP = maxMP2;
        heroStunned = 0;
        monsStunned = 0;
        turn = 1;
    }
}
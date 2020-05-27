package com.example.admin.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static int userOverallScore,userTurnScore, cpuOverallScore, cpuTurnScore=0;
    static final int GAME_POINTS=100;
    TextView mScoreTV;
    ImageView mDiceFaceIV;
    Button mRollBtn;
    Button mHoldBtn;
    Button mResetBtn;

    Handler timerHandler= new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            timerHandler.postDelayed(this, 500);
        }
    };

    int[] diceFaces={
            R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScoreTV=(TextView)findViewById(R.id.tv_score);
        mDiceFaceIV=(ImageView)findViewById(R.id.iv_dice);
        mRollBtn=(Button)findViewById(R.id.btn_roll);
        mHoldBtn=(Button)findViewById(R.id.btn_hold);
        mResetBtn=(Button)findViewById(R.id.btn_reset);


        mRollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRoll();
            }
        });
        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickReset();
            }
        });
        mHoldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHold();
            }
        });

    }
    void computerTurn(){
        mRollBtn.setEnabled(false);
        mHoldBtn.setEnabled(false);
        timerHandler.postDelayed(timerRunnable, 5000);
        int diceValue =rollDice();
        if(diceValue==1){
            cpuTurnScore=0;
            updateLabelCPU(1);

        }else{
            cpuTurnScore+=diceValue;
            updateLabelCPU(diceValue);
        }

        cpuOverallScore+=cpuTurnScore;
        cpuTurnScore=0;
        if(cpuOverallScore>=GAME_POINTS){
            mScoreTV.setText("Computer won");
        }
        else{
        //Update label computers turn score or computer hold
        //adding to decide to hold or rolling if it is not one
            updateLabelCPU(2);
            mRollBtn.setEnabled(true);
            mHoldBtn.setEnabled(true);
        }

    }
    void updateLabelCPU(int n){
        if(n==1){
            mScoreTV.setText("Computer rolled a 1");
        }else if(n>1){
            mScoreTV.setText("Computer overall score: "+cpuOverallScore+"\nUser Score: "+userOverallScore+"\nComputer turn score:"+cpuTurnScore);
        }else{
            mScoreTV.setText("Computer holds");
        }

    }
    void clickHold(){
        userOverallScore+=userTurnScore;
        userTurnScore=0;
        updateLabel();
        if(userOverallScore>=GAME_POINTS){
            mScoreTV.setText("You won");
            mRollBtn.setEnabled(false);
            mHoldBtn.setEnabled(false);
        }else{
            computerTurn();
        }
    }

    void clickReset(){
        userOverallScore=cpuOverallScore=userTurnScore=cpuTurnScore=0;
        updateLabel();
        mRollBtn.setEnabled(true);
        mHoldBtn.setEnabled(true);
    }
    void clickRoll(){
        int diceValue =rollDice();
        if(diceValue==1){
          clickHold();
        }else{
            userTurnScore+=diceValue;
            updateLabel();
        }
    }
    void updateLabel(){
        mScoreTV.setText("Your Score: "+userOverallScore+" Computer Score:"+cpuOverallScore+"\nYour turn score: "+userTurnScore);
    }
    int rollDice(){
        Random rand=new Random();
        int randNumber= rand.nextInt(6);
        mDiceFaceIV.setImageResource(diceFaces[randNumber]);
        return randNumber+1;
    }


}

package com.clocharts.oopid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clocharts.oopid.objs.GameStatusObj;
import com.clocharts.oopid.objs.NumBirdObj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<NumBirdObj> numBirds;
    List<Button> playerButtons;
    List<TextView> computerLabels;

    TextView computerInfoLabel;
    TextView playerInfoLabel;
    TextView gameStatusLabel;
    TextView computerScoreLabel;
    TextView playerScoreLabel;

    GameStatusObj gameStatusObj;

    int computerScore;
    int playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init buttons array
        playerButtons = new ArrayList<>();
        playerButtons.add((Button) findViewById(R.id.btn1));
        playerButtons.add((Button) findViewById(R.id.btn2));
        playerButtons.add((Button) findViewById(R.id.btn3));
        playerButtons.add((Button) findViewById(R.id.btn4));

        // Init labels array
        computerLabels = new ArrayList<>();
        computerLabels.add((TextView) findViewById(R.id.lbl1));
        computerLabels.add((TextView) findViewById(R.id.lbl2));
        computerLabels.add((TextView) findViewById(R.id.lbl3));
        computerLabels.add((TextView) findViewById(R.id.lbl4));


        // Init other UI views
        computerInfoLabel = findViewById(R.id.computerInfoLabel);
        playerInfoLabel = findViewById(R.id.playerInfoLabel);
        gameStatusLabel = findViewById(R.id.gameStatusLabel);
        computerScoreLabel = findViewById(R.id.computerScoreLabel);
        playerScoreLabel = findViewById(R.id.playerScoreLabel);

        // Init the game
        initGame();

    }

    private void initGame(){

        computerInfoLabel.setText(null);
        playerInfoLabel.setText(null);
        gameStatusLabel.setText(null);
        computerScore = 0;
        playerScore = 0;
        computerScoreLabel.setText(null);
        playerScoreLabel.setText(null);
        gameStatusObj = null;

        numBirds = new ArrayList<NumBirdObj>();
        // Add 50 numbers
        for (int i = 1; i <= 50; i++){
            NumBirdObj numBird = new NumBirdObj();
            numBird.setNumber(i);
            numBirds.add(numBird);
        }
        // Shuffle
        Collections.shuffle(numBirds);
        // Set 10 take all
        for (int i = 0; i < 10; i++){
            numBirds.get(i).setTakeAll(true);
        }
        // Shuffle
        Collections.shuffle(numBirds);
        // Set MAD
        for (NumBirdObj numBird : numBirds){
            if (!numBird.isTakeAll()){
                numBird.setMad(true);
                break;
            }
        }
        // Shuffle
        Collections.shuffle(numBirds);
        feed(true, true);
    }

    private void feed(Boolean player, Boolean computer){

        if (player) {
            for (int i = 0; i < playerButtons.size(); i++) {
                playerButtons.get(i).setText(numBirds.get(i).getNumber().toString());
                playerButtons.get(i).setTag(numBirds.get(i));
                playerButtons.get(i).setVisibility(View.VISIBLE);

                if (numBirds.get(i).isTakeAll()) playerButtons.get(i).setTextColor(Color.BLUE);
                else if (numBirds.get(i).isMad()) playerButtons.get(i).setTextColor(Color.RED);
                else playerButtons.get(i).setTextColor(Color.BLACK);

                numBirds.remove(i);
            }
        }
        if (computer) {
            for (int i = 0; i < computerLabels.size(); i++) {
                //computerLabels.get(i).setText(numBirds.get(i).getNumber().toString());
                computerLabels.get(i).setText("X");
                computerLabels.get(i).setTag(numBirds.get(i));
                computerLabels.get(i).setVisibility(View.VISIBLE);

                /*
                if (numBirds.get(i).isTakeAll()) computerLabels.get(i).setTextColor(Color.BLUE);
                else if (numBirds.get(i).isMad()) computerLabels.get(i).setTextColor(Color.RED);
                else computerLabels.get(i).setTextColor(Color.BLACK);
                */
                numBirds.remove(i);
            }
        }
    }

    private void singleFeed(Boolean player, Boolean computer, View view){
        if (player && numBirds.size() > 0) {
            ((Button)view).setText(numBirds.get(0).getNumber().toString());
            view.setTag(numBirds.get(0));
            view.setVisibility(View.VISIBLE);

            if (numBirds.get(0).isTakeAll()) ((Button)view).setTextColor(Color.BLUE);
            else if (numBirds.get(0).isMad()) ((Button)view).setTextColor(Color.RED);
            else ((Button)view).setTextColor(Color.BLACK);

            numBirds.remove(0);
        }
        if (computer && numBirds.size() > 0) {
            ((TextView)view).setText("X");
            view.setTag(numBirds.get(0));
            view.setVisibility(View.VISIBLE);

            numBirds.remove(0);
        }
    }

    private void makeMove(NumBirdObj numBird){

        if (gameStatusObj == null) gameStatusObj = new GameStatusObj();

        TextView choicedLabel = null;

        if (numBird != null) { // RESPONSE TO PLAYER

            if (!numBird.isMad()) { // Player not play MAD

                if (!numBird.isTakeAll()) { // Player not play take all

                    // First scan - Have a normal number > than player
                    int numDiff = 1000;
                    for (int i = 0; i < 4; i++) {
                        if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                            // Label object
                            NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                            if (lblNumBird.getNumber() > numBird.getNumber() && (lblNumBird.getNumber() - numBird.getNumber()) < numDiff && !lblNumBird.isTakeAll() && !lblNumBird.isMad()) {
                                numDiff = lblNumBird.getNumber() - numBird.getNumber();
                                choicedLabel = computerLabels.get(i);
                            }
                        }
                    }

                    // Second scan, consider to using a takeall number (the lower as possible)
                    if (numBird.getNumber() > 25 && choicedLabel == null) {
                        numDiff = 1000;
                        for (int i = 0; i < 4; i++) {
                            if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                                // Label object
                                NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                                if (lblNumBird.getNumber() < numDiff && lblNumBird.isTakeAll() && !lblNumBird.isMad()) {
                                    numDiff = lblNumBird.getNumber();
                                    choicedLabel = computerLabels.get(i);
                                }
                            }
                        }
                    }

                    // Third scan, consider to using a MAD number
                    if (numBird.getNumber() > 40 && choicedLabel == null) {
                        for (int i = 0; i < 4; i++) {
                            if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                                // Label object
                                NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                                if (lblNumBird.isMad()) {
                                    choicedLabel = computerLabels.get(i);
                                }
                            }
                        }
                    }

                } else { // Player play take all

                    // First scan - Have a takeall number > than player
                    int numDiff = 1000;
                    for (int i = 0; i < 4; i++) {
                        if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                            // Label object
                            NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                            if (lblNumBird.getNumber() > numBird.getNumber() && (lblNumBird.getNumber() - numBird.getNumber()) < numDiff && lblNumBird.isTakeAll() && !lblNumBird.isMad()) {
                                numDiff = lblNumBird.getNumber() - numBird.getNumber();
                                choicedLabel = computerLabels.get(i);
                            }
                        }
                    }

                    // Second scan, consider to using a MAD number
                    if (numBird.getNumber() > 40 && choicedLabel == null) {
                        for (int i = 0; i < 4; i++) {
                            if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                                // Label object
                                NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                                if (lblNumBird.isMad()) {
                                    choicedLabel = computerLabels.get(i);
                                }
                            }
                        }
                    }
                }
            }

            // Nothing was choosen, check for the lower normal number
            if (choicedLabel == null) {
                int numDiff = 1000;
                for (int i = 0; i < 4; i++) {
                    if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                        // Label object
                        NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                        if (lblNumBird.getNumber() < numDiff && !lblNumBird.isTakeAll() && !lblNumBird.isMad()) {
                            numDiff = lblNumBird.getNumber();
                            choicedLabel = computerLabels.get(i);
                        }
                    }
                }
            }

            // Nothing was choosen, check for the lower take all number
            if (choicedLabel == null) {
                int numDiff = 1000;
                for (int i = 0; i < 4; i++) {
                    if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                        // Label object
                        NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                        if (lblNumBird.getNumber() < numDiff && lblNumBird.isTakeAll() && !lblNumBird.isMad()) {
                            numDiff = lblNumBird.getNumber();
                            choicedLabel = computerLabels.get(i);
                        }
                    }
                }
            }

            // Nothing was choosen check for MAD
            if (choicedLabel == null) {
                for (int i = 0; i < 4; i++) {
                    if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                        // Label object
                        NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                        if (lblNumBird.isMad()) {
                            choicedLabel = computerLabels.get(i);
                        }
                    }
                }
            }

            gameStatusObj.setPlayerMove(numBird);
            gameStatusObj.setComputerMove((NumBirdObj) choicedLabel.getTag());

        } else { // COMPUTER MOVE

            // First scan, the lower move
            int numDiff = 1000;
            for (int i = 0; i < 4; i++) {
                if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                    // Label object
                    NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                    if (lblNumBird.getNumber() < numDiff && !lblNumBird.isTakeAll() && !lblNumBird.isMad()) {
                        numDiff = lblNumBird.getNumber();
                        choicedLabel = computerLabels.get(i);
                    }
                }
            }

            // Nothing was choiced, try with take all
            if (choicedLabel == null){
                numDiff = 1000;
                for (int i = 0; i < 4; i++) {
                    if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                        // Label object
                        NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                        if (lblNumBird.getNumber() < numDiff && lblNumBird.isTakeAll() && !lblNumBird.isMad()) {
                            numDiff = lblNumBird.getNumber();
                            choicedLabel = computerLabels.get(i);
                        }
                    }
                }
            }

            // Nothing was choiced, try with MAD
            if (choicedLabel == null){
                for (int i = 0; i < 4; i++) {
                    if (computerLabels.get(i).getVisibility() == View.VISIBLE) {
                        // Label object
                        NumBirdObj lblNumBird = (NumBirdObj) computerLabels.get(i).getTag();
                        if (lblNumBird.isMad()) {
                            choicedLabel = computerLabels.get(i);
                        }
                    }
                }
            }
            if (choicedLabel != null){
                gameStatusObj.setPlayerMove(null);
                gameStatusObj.setComputerMove((NumBirdObj) choicedLabel.getTag());
            }
        }

        if (choicedLabel != null) choicedLabel.setVisibility(View.INVISIBLE);
    }

    private void checkStatusGame(){

        if (gameStatusObj.getComputerMove() != null) {
            if (gameStatusObj.getComputerMove().isTakeAll()) computerInfoLabel.setTextColor(Color.BLUE);
            else if (gameStatusObj.getComputerMove().isMad()) computerInfoLabel.setTextColor(Color.RED);
            else computerInfoLabel.setTextColor(Color.BLACK);
            computerInfoLabel.setText("COMPUTER: " + gameStatusObj.getComputerMove().getNumber());
        } else {
            ((TextView) findViewById(R.id.computerInfoLabel)).setText("MASTER AI ERROR");
        }

        if (gameStatusObj.getPlayerMove() != null){
            if (gameStatusObj.getPlayerMove().isTakeAll()) playerInfoLabel.setTextColor(Color.BLUE);
            else if (gameStatusObj.getPlayerMove().isMad()) playerInfoLabel.setTextColor(Color.RED);
            else playerInfoLabel.setTextColor(Color.BLACK);
            playerInfoLabel.setText("PLAYER: " + gameStatusObj.getPlayerMove().getNumber());
        }

        // Stabilisce chi ha vinto la mano
        if (gameStatusObj.getPlayerMove() != null && gameStatusObj.getComputerMove() != null){
            // Pair of moves, check for status
            Boolean playerWin = false;
            if (gameStatusObj.getPlayerMove().isMad()) playerWin = true;
            else if (gameStatusObj.getPlayerMove().isTakeAll() && !gameStatusObj.getComputerMove().isTakeAll()) playerWin = true;
            else if (gameStatusObj.getPlayerMove().getNumber() > gameStatusObj.getComputerMove().getNumber()){
                if (!gameStatusObj.getComputerMove().isTakeAll() && !gameStatusObj.getComputerMove().isMad()) playerWin = true;
                else if (gameStatusObj.getPlayerMove().isTakeAll() && gameStatusObj.getComputerMove().isTakeAll()) playerWin = true;
            }

            if (playerWin) {
                gameStatusLabel.setText("PLAYER MATCH!");
                gameStatusLabel.setTextColor(Color.parseColor("#008f00"));
                playerScore+=gameStatusObj.getPlayerMove().getNumber()+gameStatusObj.getComputerMove().getNumber();
                playerScoreLabel.setText(playerScore + "");

                // Reset status
                gameStatusObj = null;
            }
            else {
                gameStatusLabel.setText("COMPUTER MATCH!");
                gameStatusLabel.setTextColor(Color.RED);
                computerScore+=gameStatusObj.getComputerMove().getNumber()+gameStatusObj.getPlayerMove().getNumber();
                computerScoreLabel.setText(computerScore + "");

                for (int i = 0; i < 4; i++) {
                    playerButtons.get(i).setEnabled(false);
                    playerButtons.get(i).setAlpha(0.4f);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Reset status
                        gameStatusObj = null;
                        // Clear UI
                        playerInfoLabel.setText(null);
                        computerInfoLabel.setText(null);
                        gameStatusLabel.setText(null);
                        makeMove(null);
                        checkStatusGame();
                        for (int i = 0; i < 4; i++) {
                            playerButtons.get(i).setEnabled(true);
                            playerButtons.get(i).setAlpha(1f);
                        }
                    }
                }, 3000);
            }

            Boolean haveVisibleBirds = false;
            // Refeed and check for buttons
            for (Button btn : playerButtons){
                if (btn.getVisibility() == View.INVISIBLE) singleFeed(true, false, btn);
                if (btn.getVisibility() == View.VISIBLE) haveVisibleBirds = true;
            }
            for (TextView lbl : computerLabels){
                if (lbl.getVisibility() == View.INVISIBLE) singleFeed(false, true, lbl);
                if (lbl.getVisibility() == View.VISIBLE) haveVisibleBirds = true;
            }

            // Check for game over
            if (!haveVisibleBirds){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String messageStr = null;
                        if (computerScore > playerScore) messageStr = "LOSER";
                        else if (computerScore < playerScore) messageStr = "WINNER";
                        else messageStr = "OX";

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("GAME OVER")
                                .setMessage(messageStr)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        initGame();
                                    }
                                })
                                .show();
                    }
                }, 3000);
            }
        }
    }

    public void playerMove(View view){
        view.setVisibility(View.INVISIBLE);
        if (gameStatusObj != null && gameStatusObj.getComputerMove() != null){
            gameStatusObj.setPlayerMove((NumBirdObj) view.getTag());
        } else {
            makeMove((NumBirdObj) view.getTag());
        }
        checkStatusGame();
    }

}

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static android.R.attr.label;
import static com.google.engedu.ghost.R.id.gameStatus;
import static com.google.engedu.ghost.R.id.ghostText;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private boolean gameOver;
    private Random random = new Random();
    private int userScore;
    private int computerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        if(savedInstanceState != null) {
            ((TextView) findViewById(R.id.ghostText)).setText(savedInstanceState.getCharSequence("wordFragment"));
            ((TextView) findViewById(gameStatus)).setText(savedInstanceState.getCharSequence("gameStatus"));
            gameOver = savedInstanceState.getBoolean("gameOver");
            userScore = savedInstanceState.getInt("userScore");
            updateUserScore();
            computerScore = savedInstanceState.getInt("computerScore");
            updateComputerScore();
        }
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        if(savedInstanceState == null) {
            resetRestart(null);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z && !gameOver){
            TextView wordFragment = ((TextView)findViewById(ghostText));
            String letter = Character.toString((char)event.getUnicodeChar());
            String currentWord = wordFragment.getText() + letter;
            wordFragment.setText(currentWord);

            TextView label = ((TextView) findViewById(gameStatus));
            label.setText(COMPUTER_TURN);
            computerTurn();
            return true;
        }
        else{
            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putCharSequence("wordFragment", ((TextView)findViewById(R.id.ghostText)).getText());
        savedInstanceState.putCharSequence("gameStatus", ((TextView)findViewById(gameStatus)).getText());
        savedInstanceState.putInt("userScore", userScore);
        savedInstanceState.putInt("computerScore", computerScore);
        savedInstanceState.putBoolean("gameOver", gameOver);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        gameOver = false;
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        enableChallengeButton(true);
        gameOver = false;
        return true;
    }

    public void resetRestart(View view) {
        userScore = 0;
        computerScore = 0;
        updateComputerScore();
        updateUserScore();
        onStart(null);
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(gameStatus);
        TextView ghostText = ((TextView)findViewById(R.id.ghostText));

        String wordFragment = ghostText.getText().toString();

        if(wordFragment.length() >= 4 && dictionary.isWord(wordFragment)){
            label.setText("Fragment is a Word - Computer wins!");
            computerWin();
            return;
        }

        String someWord = dictionary.getGoodWordStartingWith(wordFragment, userTurn);

        if(someWord == null){
            label.setText("No possible words exist - Computer wins!");
            computerWin();
            return;
        }
        else{
            wordFragment += someWord.substring(wordFragment.length(), wordFragment.length()+1);
            ghostText.setText(wordFragment);
        }
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }

    public void challengeButton(View v){
        TextView label = (TextView) findViewById(gameStatus);
        TextView ghostText = ((TextView)findViewById(R.id.ghostText));

        String wordFragment = ghostText.getText().toString();

        if(wordFragment.length() >= 4 && dictionary.isWord(wordFragment)){
            label.setText("Fragment is a word - User wins!");
            userWin();
            return;
        }

        String someWord = dictionary.getGoodWordStartingWith(wordFragment, userTurn);

        if(someWord == null){
            label.setText("No possible words exists - User wins!");
            userWin();
        }
        else{
            wordFragment = someWord;
            ghostText.setText(wordFragment);
            label.setText("A word can be formed - Computer wins!");
            computerWin();
        }
    }

    private void enableChallengeButton(boolean bool){
        findViewById(R.id.challengeButtonView).setEnabled(bool);
    }

    private void computerWin(){
        gameOver = true;
        ++computerScore;
        updateComputerScore();
        enableChallengeButton(false);
    }

    private void userWin(){
        gameOver = true;
        ++userScore;
        updateUserScore();
        enableChallengeButton(false);
    }

    private void updateUserScore(){
        ((TextView)findViewById(R.id.userScoreView)).setText("User Score: " + userScore);
    }

    private void updateComputerScore(){
        ((TextView)findViewById(R.id.computerScoreView)).setText("Computer Score: " + computerScore);
    }
}

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static android.R.attr.label;
import static com.google.engedu.ghost.R.id.ghostText;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
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

        if(keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z){
            TextView wordFragment = ((TextView)findViewById(ghostText));
            String letter = Character.toString((char)event.getUnicodeChar());
            String currentWord = wordFragment.getText() + letter;
            wordFragment.setText(currentWord);
//            if(dictionary.isWord(currentWord)) {
//                ((TextView) findViewById(R.id.gameStatus)).setText("This is a word!");
//            }
            TextView label = ((TextView) findViewById(R.id.gameStatus));
            label.setText(COMPUTER_TURN);
            computerTurn();
            return true;
        }
        else{
            return super.onKeyUp(keyCode, event);
        }
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView ghostText = ((TextView)findViewById(R.id.ghostText));

        String wordFragment = ghostText.getText().toString();

        if(wordFragment.length() >= 4 && dictionary.isWord(wordFragment)){
            label.setText("Fragment is a Word - Computer wins!");
            return;
        }

        String someWord = dictionary.getAnyWordStartingWith(wordFragment);

        if(someWord == null){
            label.setText("No possible words exist - Computer wins!");
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
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView ghostText = ((TextView)findViewById(R.id.ghostText));

        String wordFragment = ghostText.getText().toString();

        if(wordFragment.length() >= 4 && dictionary.isWord(wordFragment)){
            label.setText("Fragment is a word - User wins!");
            return;
        }

        String someWord = dictionary.getAnyWordStartingWith(wordFragment);

        if(someWord == null){
            label.setText("No possible words exist - User wins!");
        }
        else{
            wordFragment = someWord;
            ghostText.setText(wordFragment);
            label.setText("A word can be formed - Computer wins!");
        }
    }
}

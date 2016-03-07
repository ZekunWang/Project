package com.zwang_000.tictactoe;

import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {
    private int user;
    private boolean notActive;
    private int[] placed;
    private int[][] patterns;
    public void dropIn(View view){
        ImageView counter = (ImageView) view;
        int tag = Integer.parseInt(counter.getTag().toString());
        if(placed[tag] == -1 && !notActive) {
            placed[tag] = user;
            counter.setTranslationY(-1000F);
            if (user == 0) {
                counter.setImageResource(R.drawable.red);
                user = 1;
            } else {
                counter.setImageResource(R.drawable.yellow);
                user = 0;
            }
            counter.animate().translationYBy(1000F).rotation(360F).setDuration(800);
            if(!checkWin()){
                checkNoWin();
            }
        }
    }

    private boolean checkWin(){
        GridLayout gameLayout = (GridLayout) findViewById(R.id.gameLayout);
        LinearLayout resultLayout = (LinearLayout) findViewById(R.id.restartLayout);
        TextView result = (TextView) findViewById(R.id.winner);
        for(int[] pa : patterns){
            if(placed[pa[0]] != -1 && placed[pa[0]] == placed[pa[1]] && placed[pa[1]] == placed[pa[2]]){
                String s = (placed[pa[0]] == 0 ? "Red" : "Yellow");
                result.setText(s + " has won!");
                resultLayout.setVisibility(View.VISIBLE);
                notActive = true;
                return true;
            }
        }
        return false;
    }

    private void checkNoWin(){
        GridLayout gameLayout = (GridLayout) findViewById(R.id.gameLayout);
        LinearLayout resultLayout = (LinearLayout) findViewById(R.id.restartLayout);
        TextView result = (TextView) findViewById(R.id.winner);
        for(int i : placed){
            if(i == -1){
                return;
            }
        }
        String s = "It's a draw";
        result.setText(s);
        resultLayout.setVisibility(View.VISIBLE);
        notActive = true;
    }

    public void restartGame(View view){;
        notActive = false;
        LinearLayout resultLayout = (LinearLayout) findViewById(R.id.restartLayout);
        resultLayout.setVisibility(View.INVISIBLE);
        user = 0;
        for(int i = 0; i < placed.length; i++){
            placed[i] = -1;
        }
        GridLayout gameLayout = (GridLayout) findViewById(R.id.gameLayout);
        for(int i = 0; i < gameLayout.getChildCount(); i++){
            ((ImageView) gameLayout.getChildAt(i)).setImageResource(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        patterns = new int[][]{{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
        placed = new int[9];
        for(int i = 0; i < 9; i++){
            placed[i] = -1;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

package com.example.sanskriti.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    Random random;
    LinearLayout root;
    public int SIZE1 = 8;
    public int SIZE2 = 8;
    public int count = 0;



    public static final int[] x = {-1, -1, 0, 1, 1, 1, 0, -1};
    public static final int[] y = {0, 1, 1, 1, 0, -1, -1, -1};

    public static final int INCOMPLETE = 1;
    public static final int PLAYER_WON = 2;
    public static final int GAME_OVER = 3;
    public  int currentStatus;
   // public int currentPlayer;



    public MSButton[][] board;

    public ArrayList<LinearLayout> rows = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        root = findViewById(R.id.root);
        root.setBackgroundResource(R.drawable.c);
        random = new Random();
        setUpBoard();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);


        // Find the menuItem to add your SubMenu
        MenuItem myMenuItem = menu.findItem(R.id.m1);

        // Inflating the sub_menu menu this way, will add its menu items
        // to the empty SubMenu you created in the xml
      //  getMenuInflater().inflate(R.menu.sub_menu, myMenuItem.getSubMenu());
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //menu ka ite jispr click hua h
            if(id == R.id.reset){
                setUpBoard();
            }

           else if (id == R.id.sm1) {

                SIZE1 = 8;
                SIZE2 = 8;
                setUpBoard();
            } else if (id == R.id.sm2) {


                SIZE1 = 12;
                SIZE2 = 12;
                setUpBoard();
            }



        return super.onOptionsItemSelected(item);
    }

    private void setUpBoard() {
        rows = new ArrayList<>();
        count = 0;
        currentStatus = INCOMPLETE;
        board = new MSButton[SIZE1][SIZE2];

        root.removeAllViews();

        for (int i = 0; i<SIZE1; i++){
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            linearLayout.setLayoutParams(layoutParams);

            root.addView(linearLayout);
            rows.add(linearLayout);

        }

        for (int i = 0; i<SIZE1; i++){
            for (int j = 0; j<SIZE2; j++){
                MSButton button = new MSButton(this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                button.setLayoutParams(layoutParams);
                button.setOnLongClickListener(this);
                button.setOnClickListener(this);
               // button.setEnabled(false);
                LinearLayout row = rows.get(i);

                row.addView(button);
                button.x = i;
                button.y = j;
                board[i][j] = button;
                board[i][j].setBackgroundResource(R.drawable.b_bg);
            }
        }
        int p = 0;
            if(SIZE1 == 8) {
                while (p < 9) {
                    int i = random.nextInt(SIZE1 - 1);
                    int j = random.nextInt(SIZE2 - 1);
                    Log.i("MainActivity", "i =" + i + " j = " + j);
                    board[i][j].MINE = -1;
                  //  board[i][j].IS_REVEALED = true;
                    countNeighbour(i, j);
                    p++;
                }
            }else if(SIZE1 == 12) {
                while (p < 20) {
                    int i = random.nextInt(SIZE1 - 1);
                    int j = random.nextInt(SIZE2 - 1);
                    Log.i("MainActivity", "i =" + i + " j = " + j);
                    board[i][j].MINE = -1;
                   // board[i][j].IS_REVEALED = true;
                    countNeighbour(i, j);
                    p++;
                }
            }








    }




    @Override
    public void onClick(View view) {

        MSButton button = (MSButton) view;
        int x = button.x;
        int y = button.y;

       if(currentStatus == INCOMPLETE ){
           if(!button.IS_FLAG) {
               if (button.MINE == -1) {

                   for (int i = 0; i < SIZE1; i++) {
                       for (int j = 0; j < SIZE2; j++) {

                           if (board[i][j].MINE == -1) {
                              board[i][j].setBackgroundResource(R.drawable.d);
                           }
                       }
                   }


                   Toast.makeText(this, "Game Over", Toast.LENGTH_LONG).show();
                   currentStatus = GAME_OVER;
               } else if (button.MINE != -1 && button.VALUE != 0) {
                   button.setText(button.VALUE + "");
                   checkGameStatus();

               } else if (button.MINE != -1 && button.VALUE == 0) {
                   reveal(button.x, button.y);
                   checkGameStatus();
               }
           }


       }

        if(currentStatus == GAME_OVER || currentStatus == PLAYER_WON){
            for (int i = 0; i<SIZE1; i++){
                for(int j = 0; j<SIZE2; j++){
                    board[i][j].setClickable(false);
                    board[i][j].setLongClickable(false);
                 //   Toast.makeText(this, currentStatus, Toast.LENGTH_LONG).show();
                }
            }
        }



    }

    private void checkGameStatus() {
        boolean ans = true;
        for(int i = 0; i<SIZE1; i++){
            for (int j = 0; j<SIZE2; j++){
                String t = board[i][j].getText().toString();
                if(t == "" && board[i][j].MINE != -1){
                    ans = false;
                    break;
                }
            }
            if(ans == false){
                break;
            }
        }
        if(ans == true){
            Toast.makeText(this, "You Won", Toast.LENGTH_LONG).show();
            currentStatus = PLAYER_WON;

        }else{
            currentStatus = INCOMPLETE;
        }
    }

    private void reveal(int a, int b) {
        for(int i = 0; i<8; i++){
            int currX = a + x[i];
            int currY = b + y[i];
            if((currX >= 0) && (currX < SIZE1) && (currY >= 0) && (currY < SIZE2) && board[a][b].MINE != -1) {

                if(board[currX][currY].IS_REVEALED == false){
                    board[currX][currY].setText(board[currX][currY].VALUE + "");
                    board[currX][currY].IS_REVEALED = true;
                    if(board[currX][currY].VALUE == 0){
                        reveal(currX, currY);
                    }
                }
            }
            }

    }

    private void countNeighbour(int a, int b) {



        for(int i = 0; i<8; i++){
            int currX = a + x[i];
            int currY = b + y[i];
            if((currX >= 0) && (currX < SIZE1) && (currY >= 0) && (currY < SIZE2) && board[a][b].MINE == -1){


                board[currX][currY].VALUE++;
                Log.i("MainActivity", "i =" + currX + " j = " +  currY +  "value = " + board[currX][currY].VALUE);
            }


        }



    }

    @Override
    public boolean onLongClick(View view) {
        MSButton button = (MSButton) view;
       // if(!button.IS_FLAG){
        //    button.IS_FLAG = true;
       // }

//        for(int i = 0; i<SIZE1; i++ ){
//            for(int j = 0; j<SIZE2; j++){

        if(SIZE1 == 8) {
            if (button.IS_FLAG == true && button.IS_REVEALED == true) {
               // button.setText("");
                button.setBackgroundResource(R.drawable.b_bg);
                count--;
                button.IS_FLAG = false;
                button.setClickable(true);

                button.IS_REVEALED = false;
            } else if (count == 9) {
                Toast.makeText(this, "Limit Exceeded", Toast.LENGTH_LONG).show();
            } else if (button.IS_FLAG == false) {
               // button.setText("D");
                button.setBackgroundResource(R.drawable.e);
                count++;
                button.IS_FLAG = true;
                button.IS_REVEALED = true;
                button.setClickable(false);
            }
        }else if(SIZE1 == 12){
            if (button.IS_FLAG == true && button.IS_REVEALED == true) {
                button.setBackgroundResource(R.drawable.b_bg);
                //button.setText("");
                count--;
                button.IS_FLAG = false;
                button.IS_REVEALED = false;
                button.setClickable(true);
            } else if (count == 20) {
                Toast.makeText(this, "Limit Exceeded", Toast.LENGTH_LONG).show();
            } else if (button.IS_FLAG == false && button.IS_REVEALED == false) {
              //  button.setText("D");
                button.setBackgroundResource(R.drawable.e);
                count++;
                button.IS_FLAG = true;
                button.IS_REVEALED = true;
                button.setClickable(false);
            }

        }
//                }
//            }


        checkGameStatus();

        return true;
    }
}

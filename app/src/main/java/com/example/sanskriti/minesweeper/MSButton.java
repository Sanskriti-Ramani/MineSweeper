package com.example.sanskriti.minesweeper;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

public class MSButton extends AppCompatButton {



    int MINE = 0;
    int x;
    int y;
    int VALUE = 0;
    boolean IS_REVEALED = false;
    int flag = 0;
    boolean IS_FLAG = false;
    public MSButton(Context context) {
        super(context);
        //setEnabled(false);


    }



}

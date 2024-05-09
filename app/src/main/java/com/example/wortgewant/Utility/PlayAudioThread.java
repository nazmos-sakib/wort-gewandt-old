package com.example.wortgewant.Utility;

import android.content.Context;
import android.widget.TextView;

import com.example.wortgewant.Model.Wort;

public class PlayAudioThread extends PlayAudioRunable{
    TextView word_,meaning_;
    Wort wort_;

    PlayAudioThread next_,turn_;
    public PlayAudioThread(Context context, TextView  word, TextView meaning, Wort wort) {
        super(context, wort.getPronunciation());
        word_ = word;
        meaning_ = meaning;
        wort_ = wort;
        turn_ = null;
        next_ = null;
    }

    public synchronized void setNext_(PlayAudioThread p){
        next_ = p;
    }

    public synchronized void turn(){
        turn_ = this;
        notifyAll();
    }

    public synchronized void run(){
        for (;;){
            while (turn_!=this){
                try {
                    wait();
                } catch (InterruptedException e){
                    return;
                }
            }

            word_.setText(wort_.getGermanWord());
            meaning_.setText(wort_.getEnglishMeaning());

            turn_ = null;
            next_.turn();

            try {
                Thread.currentThread().sleep((long) (Math.random()*10000));
            } catch (InterruptedException e){return;}
        }
    }
}

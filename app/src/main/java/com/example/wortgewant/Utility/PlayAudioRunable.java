package com.example.wortgewant.Utility;

import android.content.Context;

public class PlayAudioRunable implements Runnable {

    Context context_;
    byte[] audio_;

    public PlayAudioRunable(Context context, byte[] audio) {
        this.context_ = context;
        this.audio_ = audio;
    }

    @Override
    public void run() {
        Pronunciation.playPronunciationMp3(context_,audio_);
    }
}

package com.example.wortgewant.ExternalRequest;


import android.util.Log;

import com.deepl.api.*;
import com.example.wortgewant.Interfaces.Callback;


public class DeepLApi {
    Translator translator;

    public DeepLApi(){
        Log.d("TAG", "DeepLApi: --------------created");
        //api key
        String authKey = "c1486d69-bb78-416b-b0a8-a9b750e57792:fx";
        translator = new Translator(authKey);
    }

    public DeepLApi(String word,Callback<String> callback) throws Exception {
        Log.d("TAG", "DeepLApi: --------------"+word);
        //api key
        String authKey = "7cdc1dfb-e931-4ad8-ade1-3b8c6ff313a1:fx";
        translator = new Translator(authKey);

        TextResult result =
                translator.translateText(word, "de", "en-us");
        Log.d("TAG", "DeepLApi: -------------"+result.getText());
        callback.onCallback(result.getText());
    }

    /*
    public String wordTranslate() throws DeepLException, InterruptedException {
        TextResult result =
                translator.translateText("Hello, world!", null, "fr");
        Log.d("TAG", "DeepLApi: -------------"+result.getText());
        System.out.printf(result.getText());
        return result.getText();
    }*/
}
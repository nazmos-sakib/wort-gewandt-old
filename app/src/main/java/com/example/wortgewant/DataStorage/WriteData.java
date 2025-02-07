package com.example.wortgewant.DataStorage;

import com.example.wortgewant.Interfaces.Callback;
import com.example.wortgewant.Model.Wort;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WriteData {
    //Multimap<String, Wort> wordList = ArrayListMultimap.create();

    public WriteData(File file, Callback<Void> callback) {
        try {//list approach

            List<Map.Entry<String, Wort>> wordEntries = new ArrayList<>(
                    Data.getInstance().getWordList().entries());
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(wordEntries);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in wordList.ser");

            callback.onCallback(null);
            //Data.getInstance().setWordList(wordList);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

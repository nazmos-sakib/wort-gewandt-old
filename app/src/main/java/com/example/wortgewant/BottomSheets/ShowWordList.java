package com.example.wortgewant.BottomSheets;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wortgewant.Adapter.RecyclerAdapter;
import com.example.wortgewant.DataStorage.Data;
import com.example.wortgewant.Model.Wort;
import com.example.wortgewant.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShowWordList extends Dialog {
    RecyclerView recView;
    private RecyclerAdapter recAdapter;
    public ShowWordList(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bottom_sheet_word_list);

        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setGravity(Gravity.TOP);



        initViews();
    }

    private void initViews() {
        recView = this.findViewById(R.id.recView_wordList);
        recAdapter = new RecyclerAdapter(getContext());
        setRecViewAdapter();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setRecViewAdapter() {
        recView.setAdapter(recAdapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        //list approach
        //covert Multimap into List with multiList built in function entries()
        ArrayList<Map.Entry<String, Wort>> wordEntries = new ArrayList<>(Data.getInstance().getWordList().entries());
        recAdapter.setAdapterData(wordEntries);
        recAdapter.notifyDataSetChanged();
    }

}

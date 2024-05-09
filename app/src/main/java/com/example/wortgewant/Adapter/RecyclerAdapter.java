package com.example.wortgewant.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wortgewant.BottomSheets.AddNewWordBottomSheet;
import com.example.wortgewant.DataStorage.Data;
import com.example.wortgewant.Model.Wort;
import com.example.wortgewant.R;
import com.example.wortgewant.Utility.Pronunciation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<Map.Entry<String,Wort>> recArrayList = new ArrayList<>();
    private final WeakReference<Context> context;

    public RecyclerAdapter(Context context) {
        // Private constructor to prevent instantiation
        this.context = new WeakReference<>(context);
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_word_list,parent,false);

        return  new ViewHolder(view);    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        int index = position;
        Wort wort = recArrayList.get(index).getValue();
        holder.tv_germanWord.setText(wort.getGermanWord());
        holder.tv_englishMeaning.setText(wort.getEnglishMeaning());

        byte[] pronunciation = wort.getPronunciation();
        //if pronunciation is available change color to green
        holder.playAudio.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,
                //toggle green color
                pronunciation.length>0?255:0,
                0)));
        holder.playAudio.setOnClickListener(View->{
            if (pronunciation.length>0){
                Pronunciation.playPronunciationMp3(context.get(), pronunciation);
            }
        });

        holder.deleteBtn.setOnClickListener(View->{
            if (Data.getInstance().deleteEntry(recArrayList.get(index))){
                setAdapterData(new ArrayList<>(Data.getInstance().getWordList().entries()));
                //notifyDataSetChanged();
            }
        });

        holder.editBtn.setOnClickListener(View->{
            AddNewWordBottomSheet dialog = new AddNewWordBottomSheet(context.get());
            dialog.setValues(wort.getGermanWord(),wort.getEnglishMeaning(),wort.getPronunciation());
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    setAdapterData(new ArrayList<>(Data.getInstance().getWordList().entries()));
                    //notifyDataSetChanged();
                }
            });
        });

    }


    @Override
    public int getItemCount() {
        return recArrayList.size();
    }

    //getting clicked data
    public Map.Entry<String,Wort> getItemData(int position) {
        return recArrayList.get(position);
    }



    //updating the data of the recView array
    @SuppressLint("NotifyDataSetChanged")
    public void setAdapterData(ArrayList<Map.Entry<String, Wort>> object) {
        this.recArrayList = object;
        notifyDataSetChanged();
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_germanWord,tv_englishMeaning;
        public ImageButton playAudio,editBtn,deleteBtn;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tv_germanWord = itemView.findViewById(R.id.tv_germanWord_cardView);
            tv_englishMeaning = itemView.findViewById(R.id.tv_englishMeaning_cardView);
            playAudio = itemView.findViewById(R.id.btn_playAudio_cardView);
            deleteBtn = itemView.findViewById(R.id.img_delete_cardView);
            editBtn = itemView.findViewById(R.id.img_editButton_cardView);
        }
    }
}

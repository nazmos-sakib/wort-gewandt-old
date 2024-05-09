package com.example.wortgewant.BottomSheets;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.wortgewant.DataStorage.Data;
import com.example.wortgewant.ExternalRequest.VolleyNetworkRequest;
import com.example.wortgewant.Model.Wort;
import com.example.wortgewant.R;
import com.example.wortgewant.Utility.InternetConnection;
import com.example.wortgewant.Utility.Pronunciation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.MessageFormat;
import java.util.Objects;

public class AddNewWordBottomSheet extends Dialog {

    private ProgressBar progressBar;
    private EditText word,englishMeaning;
    private TextView notFoundWarning,shoUrl;
    private Button btn_search, btn_add, btn_cancel, btn_enterManually, btn_enterEnglishMeaningManually;
    private LinearLayout warningSection,mainDiv;

    private ImageButton playPronunciation, downloadPronunciation;

    private String audioUrlMain;

    byte[] aMp3;

    public AddNewWordBottomSheet(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bottom_sheet_add_new_word);

        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setGravity(Gravity.TOP);

        initViews();
    }

    private void initViews(){
        word = this.findViewById(R.id.edTv_germanWord_addBottomSheet);
        btn_search = this.findViewById(R.id.btn_searchEngMeaning_addBottomSheet);
        notFoundWarning = this.findViewById(R.id.tv_englishMeaningNotFoundWarning_addBottomSheet);
        englishMeaning = this.findViewById(R.id.edTv_englishMeaning_addBottomSheet);
        progressBar = this.findViewById(R.id.progressBar_addBottomSheet);
        btn_add = this.findViewById(R.id.btn_add_addBottomSheet);
        btn_cancel = this.findViewById(R.id.btn_cancel_addBottomSheet);
        shoUrl = this.findViewById(R.id.shoUrl);
        btn_enterManually = findViewById(R.id.btn_insertManually);
        btn_enterEnglishMeaningManually = findViewById(R.id.btn_englishWordEnterManually_addNewWordBottomSheet);
        warningSection = findViewById(R.id.warningDiv_bottomSheet);
        playPronunciation = findViewById(R.id.imgB_playPronunciation);
        downloadPronunciation = findViewById(R.id.imgB_downloadPronunciation);
        mainDiv = findViewById(R.id.mainDiv_addNewWordBottomSheet);


        addFunctionalityToButton();
    }

    private void addFunctionalityToButton(){
        //search functionality
        btn_search.setOnClickListener(View->{
            //check if word text view is empty
            showProgressbar();
            hideWarning();
            englishMeaning.setText("");
            makeInternetRequestToGetEnglishMeaning(word.getText().toString().trim());
        });

        //close
        btn_cancel.setOnClickListener(View->{
            closeBottomSheet();
        });

        //word edit view
        word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*btn_search.setEnabled(true);
                englishMeaning.setText("");
                englishMeaning.setEnabled(false);
                hideWarning();*/
                downloadPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,0,0)));
                playPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,0,0)));
                playPronunciation.setEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!word.getText().toString().trim().isEmpty()){
                    showMainDiv();
                    englishMeaning.setText("");
                    englishMeaning.setEnabled(false);
                } else hideMainDiv();
            }
        });

        //english word edit view
        englishMeaning.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_add.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn_add.setEnabled(!englishMeaning.getText().toString().trim().isEmpty());
            }
        });

        //
        btn_enterEnglishMeaningManually.setOnClickListener(View->{
            englishMeaning.setEnabled(true);
            hideWarning();
            hideProgressbar();
        });



        //image button download sound
        //download pronunciation
        downloadPronunciation.setOnClickListener(View->{
            downloadPronunciation();
        });

        //play audion
        // image button play sound
        //
        playPronunciation.setOnClickListener(View->{
            playPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,250,0)));
            if (aMp3!=null && aMp3.length!=0){
                //Pronunciation.playPronunciationOnline(audioUrlMain);
                Pronunciation.playPronunciationMp3(getContext(),aMp3);
                //Pronunciation.playPronunciationRaw(getContext(),R.raw.schon);
            }
        });

        //
        btn_add.setOnClickListener(View->{
            addNewWord();
        });


    }

    private void addNewWord() {
        if (Data.getInstance().addNewWord(
                word.getText().toString().trim(),
                new Wort(
                        word.getText().toString().trim(),
                        englishMeaning.getText().toString().trim(),
                        audioUrlMain,
                        aMp3
                )
        )){
            this.dismiss();
        }
    }

    private void makeInternetRequestToGetEnglishMeaning(String word){
        if (!word.isEmpty()){
            //String gWord = word.replace(" ","+");
            //https://translate.google.com/?sl=de&tl=en&text=gut+und+dir&op=translate
            //String url = String.format("https://translate.google.com/?sl=de&tl=en&text=%s&op=translate",gWord);
            String url = MessageFormat.format("https://m.dict.cc/deutsch-englisch/{0}.html", word);
            VolleyNetworkRequest.getInstance(this.getContext()).makeWebRequest(
                    url, (response)->{
                        hideProgressbar();
                        if (response!=null){
                            //shoUrl.setText(url);
                            findEnglishMeaning(response);
                        } else {
                            showWarning();
                        }
                    }
            );
        }
    }


    private void findEnglishMeaning(String response){
        Document doc = Jsoup.parse(response);
        Element content = doc.getElementById("searchres_table");

        hideProgressbar();

        if (content!=null){
            Elements table = content.getElementsByTag("tr");

            Element tr = table.get(0);
            Elements td = tr.getElementsByTag("td") ;

            englishMeaning.setText(td.get(1).attr("data-term"));
        } else {
            showWarning();
            //notFoundWarning.setText("word not found");
        }

    }



    //1 download url
    //1.1 download web page
    //1.2 parse web page and find pronunciation url
    //2 from .mp3 url download sound
    //2.1 download and convert
    //2.2 convert
    //2.3 call afterPronunciationDownloadSuccess() function
    private void downloadPronunciation(){
        //1
        Pronunciation.downloadPronunciationPage(getContext(),
                word.getText().toString().trim(),
                //1.1 returns whole web page
                response->{
                    if (response!=null){
                        // 1.2 find pronunciation url
                        Pronunciation.getPronunciationUrl(getContext(),
                                response,
                                //2 download audio file convert it in byte
                                audioUrl->{
                                    if (!audioUrl.isEmpty()){
                                        shoUrl.setText(audioUrl);
                                        shoUrl.setVisibility(android.view.View.VISIBLE);
                                        audioUrlMain = audioUrl;

                                        //2.1 download
                                        Pronunciation.downloadToFile(getContext(),audioUrl,word.getText().toString().trim(),
                                                //2.2 converted byte received
                                                bytes -> {
                                            //downloaded successfully
                                            aMp3 = bytes;
                                            //2.3
                                            afterPronunciationDownloadSuccess();
                                        });

                                        //aMp3 = Pronunciation.downloadPronunciation(getContext(),audioUrl,word.getText().toString().trim());

                                    }else {
                                        shoUrl.setText("no audio");
                                        shoUrl.setVisibility(android.view.View.VISIBLE);
                                    }
                                });
                    } else {
                        shoUrl.setText("no audio");
                        shoUrl.setVisibility(android.view.View.VISIBLE);
                    }
                });
    }

    //1 change color
    //2 enable play sound image button
    private void afterPronunciationDownloadSuccess(){
        downloadPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,250,0)));
        playPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,250,0)));
        //2
        playPronunciation.setEnabled(true);
    }

    public void setValues(String w, String e, byte[] m){
        word.setText(w);
        englishMeaning.setText(e);
        aMp3 = m;
        if (m.length>0){
            playPronunciation.setEnabled(true);
        }

    }


    private void showMainDiv(){
        mainDiv.setVisibility(View.VISIBLE);
    }
    private void hideMainDiv(){
        mainDiv.setVisibility(View.GONE);
    }

    private void showProgressbar(){
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressbar(){
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showWarning(){warningSection.setVisibility(View.VISIBLE);}
    private void hideWarning(){warningSection.setVisibility(View.GONE);}

    private void closeBottomSheet(){
        this.dismiss();
    }


}

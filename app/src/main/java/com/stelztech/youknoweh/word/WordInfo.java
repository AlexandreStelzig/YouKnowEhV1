package com.stelztech.youknoweh.word;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stelztech.youknoweh.Database.DatabaseManager;
import com.stelztech.youknoweh.Database.Word;
import com.stelztech.youknoweh.R;

/**
 * Created by Alexandre on 4/26/2016.
 */
public class WordInfo extends Activity {

    private DatabaseManager dbManager;
    private Word wordInfo;
    private EditText questionEditText;
    private EditText answerEditText;
    private EditText moreInfoEditText;
    private TextView numberOfPackages;

    private LinearLayout editTopLayout;
    private LinearLayout editBottomLayout;
    private LinearLayout packageLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_word_info);
        dbManager = DatabaseManager.getInstance(getApplicationContext());

        String wordId = getIntent().getStringExtra("id");

        wordInfo = dbManager.getWordFromId(wordId);

        if (wordInfo == null) {
            Toast.makeText(WordInfo.this, "error: word couldn't be found", Toast.LENGTH_SHORT).show();
            finish();
        }

        questionEditText = (EditText) findViewById(R.id.question_text);
        answerEditText = (EditText) findViewById(R.id.answer_text);
        moreInfoEditText = (EditText) findViewById(R.id.moreinfo_text);
        editBottomLayout = (LinearLayout) findViewById(R.id.word_edit_layout);
        editTopLayout = (LinearLayout) findViewById(R.id.word_add_layout);
        packageLayout = (LinearLayout) findViewById(R.id.word_package_layout);
        numberOfPackages = (TextView) findViewById(R.id.word_package_number_text);

        TextView title = (TextView) findViewById(R.id.word_title);
        title.setText("Word Info");
        setWordEditable(false);
        setNumberOfPackages();

    }

    private void setWordEditable(boolean canEdit) {

        questionEditText.setClickable(canEdit);
        questionEditText.setCursorVisible(canEdit);
        questionEditText.setFocusable(canEdit);
        questionEditText.setFocusableInTouchMode(canEdit);

        answerEditText.setClickable(canEdit);
        answerEditText.setCursorVisible(canEdit);
        answerEditText.setFocusable(canEdit);
        answerEditText.setFocusableInTouchMode(canEdit);

        moreInfoEditText.setClickable(canEdit);
        moreInfoEditText.setCursorVisible(canEdit);
        moreInfoEditText.setFocusable(canEdit);
        moreInfoEditText.setFocusableInTouchMode(canEdit);

        Button topNextButton = (Button) findViewById(R.id.word_next_button);
        topNextButton.setVisibility(View.GONE);

        Button topAddButton = (Button) findViewById(R.id.word_add_button);
        topAddButton.setText("OK");

        if (!canEdit) {


            editTopLayout.setVisibility(View.GONE);
            editBottomLayout.setVisibility(View.VISIBLE);
            packageLayout.setVisibility(View.VISIBLE);

            questionEditText.setText(wordInfo.getQuestion());
            answerEditText.setText(wordInfo.getAnswer());
            moreInfoEditText.setText(wordInfo.getMoreInfo());
            moreInfoEditText.setHint("");


        } else {
            editBottomLayout.setVisibility(View.GONE);
            editTopLayout.setVisibility(View.VISIBLE);
            packageLayout.setVisibility(View.GONE);
            questionEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(questionEditText, InputMethodManager.SHOW_IMPLICIT);
            questionEditText.setSelection(questionEditText.getText().length());
            answerEditText.setSelection(answerEditText.getText().length());
            moreInfoEditText.setSelection(moreInfoEditText.getText().length());
        }

    }

    public void buttonEditClicked(View view) {
        setWordEditable(true);
    }


    public void addButtonClicked(View view) {
        hideKeyBoard(view);
        updateWord();
        setWordEditable(false);
    }


    public void cancelButtonClicked(View view) {
        hideKeyBoard(view);
        setWordEditable(false);
    }


    private void updateWord() {

        String newQuestion = questionEditText.getText().toString();
        String newAnswer = answerEditText.getText().toString();
        String newMoreInfo = moreInfoEditText.getText().toString();

        if (wordInfo.getQuestion().equals(newQuestion) && wordInfo.getAnswer().equals(newAnswer) &&
                wordInfo.getMoreInfo().equals(newMoreInfo)) {
            Toast.makeText(WordInfo.this, "no data changed", Toast.LENGTH_SHORT).show();
        } else if (wordInfo.getQuestion().equals("") && wordInfo.getAnswer().equals("")) {
            Toast.makeText(WordInfo.this, "question or answer cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            dbManager.updateFromTable_Word(wordInfo.getIdWord(), newQuestion, newAnswer, newMoreInfo);
            wordInfo.setQuestion(newQuestion);
            wordInfo.setAnswer(newAnswer);
            wordInfo.setMoreInfo(newMoreInfo);
        }

    }

    private void hideKeyBoard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setNumberOfPackages() {
        int numPackages = dbManager.getNumberOfPackagesForAWord(wordInfo.getIdWord());
        numberOfPackages.setText("" + numPackages);
    }

    private void resetVariables(){
        setWordEditable(false);
        setNumberOfPackages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resetVariables();
    }

    public void viewPackageButtonClicked(View view) {
        Intent intent = new Intent(this, UniqueWordPackagesInfo.class);
        intent.putExtra("idWord", wordInfo.getIdWord());
        startActivityForResult(intent, 0);
    }
}

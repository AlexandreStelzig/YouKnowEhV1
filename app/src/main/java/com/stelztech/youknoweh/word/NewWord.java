package com.stelztech.youknoweh.word;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stelztech.youknoweh.Database.DatabaseManager;
import com.stelztech.youknoweh.R;

/**
 * Created by Alexandre on 4/25/2016.
 */
public class NewWord extends Activity {

    private DatabaseManager dbManager;

    private EditText questionEditText;
    private EditText answerEditText;
    private EditText moreInfoEditText;

    private String packageId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_word_info);

        TextView title = (TextView) findViewById(R.id.word_title);
        title.setText("New Word");

        packageId = getIntent().getStringExtra("idPackage");

        questionEditText = (EditText) findViewById(R.id.question_text);
        answerEditText = (EditText) findViewById(R.id.answer_text);
        moreInfoEditText = (EditText) findViewById(R.id.moreinfo_text);

        LinearLayout editLayout = (LinearLayout) findViewById(R.id.word_edit_layout);
        editLayout.setVisibility(View.GONE);

        LinearLayout packageLayout = (LinearLayout) findViewById(R.id.word_package_layout);
        packageLayout.setVisibility(View.GONE);
        dbManager = DatabaseManager.getInstance(getApplicationContext());

    }

    public void cancelButtonClicked(View view) {
        finish();
    }

    public void addButtonClicked(View view) {

        boolean wordAdded = addWord();

        if (wordAdded) {

            finish();
        }
    }

    public void nextButtonClicked(View view) {
        boolean wordAdded = addWord();
        if (wordAdded) {
            resetTextFields();
        }
    }

    private void resetTextFields() {
        questionEditText.setText("");
        answerEditText.setText("");
        moreInfoEditText.setText("");
        questionEditText.requestFocus();
    }

    private boolean addWord() {


        String questionText = questionEditText.getText().toString();
        String answerText = answerEditText.getText().toString();
        String moreInfoText = moreInfoEditText.getText().toString();

        if (questionText.equals("") && answerText.equals("")) {
            Toast.makeText(NewWord.this, "question and answer cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (answerText.equals("")) {
            Toast.makeText(NewWord.this, "answer cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (questionText.equals("")) {
            Toast.makeText(NewWord.this, "question cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            long newId = dbManager.insertIntoTable_Word(questionText, answerText, moreInfoText);
            if(!(packageId==null)){
                dbManager.insertIntoTable_WordPackage(""+newId, packageId);
                Toast.makeText(NewWord.this, "word added to package", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(NewWord.this, "word added", Toast.LENGTH_SHORT).show();
            }
            return true;
        }


    }



}

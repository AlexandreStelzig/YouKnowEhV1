package com.stelztech.youknoweh.Practice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.stelztech.youknoweh.Database.DatabaseManager;
import com.stelztech.youknoweh.Database.Package;
import com.stelztech.youknoweh.Database.Word;
import com.stelztech.youknoweh.R;
import com.stelztech.youknoweh.pack.UniquePackageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre on 4/24/2016.
 */
public class PracticeLayout extends Activity {

    private DatabaseManager dbManager;
    private Spinner spinner;
    List<Word> wordListForPackage = new ArrayList<Word>();
    List<Package> packageList;
    int currentPosition = 0;
    int currentPackage = 0;
    private List<String> packagesName;
    private ArrayAdapter<String> adapter;

    private TextView questionText = null;
    private TextView answerText = null;

    private List<String> questionList = new ArrayList<String>();
    private List<String> answerList = new ArrayList<String>();

    private int questionLength = 0;
    private int[] order;

    private Button showAnswerButton;
    private boolean showingAnswer = false;
    // true = show questions
    // false = show answer
    private boolean showByQuestion = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = DatabaseManager.getInstance(getApplicationContext());
        setContentView(R.layout.layout_practice);
        spinner = (Spinner) findViewById(R.id.practice_package_spinner);
        showAnswerButton = (Button) findViewById(R.id.showAnswerButton);

        answerText = (TextView) findViewById(R.id.practice_answer_textview);
        questionText = (TextView) findViewById(R.id.practice_question_textview);

        packageList = new ArrayList<Package>();
        packagesName = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_list_practice, packagesName);

        resetVariables();

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPackage = position;
                resetVariables();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void resetVariables() {
        packagesName.clear();
        packageList.clear();

        packageList = dbManager.getPackages();
        List<String> temp = new ArrayList<String>();

        for (int i = 0; i < packageList.size(); i++) {
            temp.add(packageList.get(i).getPackageName());
        }

        packagesName.addAll(temp);
        adapter.notifyDataSetChanged();



        if(!packageList.isEmpty()) {
            resetQuestionsAndAnswers();
            resetOrder();
            setText();
        }else{

                questionText.setText("No Package");
                answerText.setText("");

        }
    }

    private void resetOrder() {
        order = null;
        order = new int[questionLength];

        for (int x = 0; x < questionLength; x++)
            order[x] = x;

        for (int i = 0; i < questionLength; i++) {
            int random = (int) (Math.random() * questionLength);

            int temp = order[random];
            order[random] = order[i];
            order[i] = temp;
        }
        currentPosition = 0;

    }

    private void setText() {

        if (questionLength != 0) {
            if (showByQuestion) {
                questionText.setText(questionList.get(order[currentPosition]));
            } else {
                questionText.setText(answerList.get(order[currentPosition]));
            }
            answerText.setText("----");
            showAnswerButton.setText("show answer");
            showingAnswer = false;
        } else {
            questionText.setText("No Question");
            answerText.setText("No Answer");
        }
    }

    private void resetQuestionsAndAnswers() {
        wordListForPackage.clear();
        questionList.clear();
        answerList.clear();

        wordListForPackage = dbManager.getWordsFromPackage(packageList.get(currentPackage).getIdPackage());
        questionLength = wordListForPackage.size();

        if (questionLength != 0) {

            for (int i = 0; i < questionLength; i++) {
                questionList.add(i, wordListForPackage.get(i).getQuestion());
                answerList.add(i, wordListForPackage.get(i).getAnswer());
            }
        }

    }

    public void infoButtonClicked(View view) {
        if(!packageList.isEmpty()) {
            Intent intent = new Intent(this, UniquePackageInfo.class);
            intent.putExtra("id", packageList.get(currentPackage).getIdPackage());
            startActivityForResult(intent, 0);
        }
    }

    public void buttonNextClicked(View view) {

        showNextQuestion();
    }

    private void showNextQuestion() {

        if (questionLength > 1) {


            currentPosition++;


            if (currentPosition == questionLength) {


                int lastOrder = order[questionLength - 1];

                resetOrder();

                int firstOrder = order[0];


                if (lastOrder == firstOrder && questionLength > 1) {
                    swapWithRandom();
                }


            }
            setText();
            showAnswerButton.setText("show answer");
            showingAnswer = false;

        }
    }

    private void swapWithRandom() {
        int random = (int) (Math.random() * questionLength - 1) + 1;


        int temp = order[random];
        order[random] = order[0];
        order[0] = temp;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resetVariables();
    }

    public void showAnswerButtonClicked(View view) {
        if(!packageList.isEmpty()) {
            if (questionLength == 0) {
                answerText.setText("No Answer");
            } else {


                if (!showingAnswer) {
                    showAnswerButton.setText("hide answer");
                    showingAnswer = true;
                    if (showByQuestion) {
                        answerText.setText(answerList.get(order[currentPosition]));
                    } else {
                        answerText.setText(questionList.get(order[currentPosition]));
                    }
                } else {
                    showAnswerButton.setText("show answer");
                    showingAnswer = false;
                    answerText.setText("----");
                }
            }
        }


    }

    public void reverseButtonClicked(View view) {
        if(!packageList.isEmpty()) {
            if (showByQuestion) {
                showByQuestion = false;
            } else {
                showByQuestion = true;
            }
            setText();
        }
    }
}

package com.stelztech.youknoweh;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.stelztech.youknoweh.Practice.PracticeLayout;
import com.stelztech.youknoweh.pack.PackageList;
import com.stelztech.youknoweh.word.WordListLayout;

public class MainMenu extends Activity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_menu);

        TextView title = (TextView) findViewById(R.id.menu_title);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/gulim.ttc");
        title.setTypeface(type);

        title.setText("You Know Eh?\n\n한국어");

    }


    public void buttonPackageClicked(View view) {
        Intent intent = new Intent(this, PackageList.class);
        startActivity(intent);
    }

    public void buttonWordClicked(View view) {
        Intent intent = new Intent(this, WordListLayout.class);
        startActivity(intent);
    }

    public void buttonPracticeClicked(View view) {
        Intent intent = new Intent(this, PracticeLayout.class);
        startActivity(intent);
    }

    public void buttonQuizClicked(View view) {
        Intent intent = new Intent(this, QuizLayout.class);
        startActivity(intent);
    }

    public void buttonHelpClicked(View view) {
        Intent intent = new Intent(this, HelpLayout.class);
        startActivity(intent);
    }
}

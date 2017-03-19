package com.stelztech.youknoweh.pack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stelztech.youknoweh.Database.DatabaseManager;
import com.stelztech.youknoweh.Database.Package;
import com.stelztech.youknoweh.Database.Word;
import com.stelztech.youknoweh.Database.WordPackage;
import com.stelztech.youknoweh.R;
import com.stelztech.youknoweh.word.WordInfo;

import java.util.List;

/**
 * Created by Alexandre on 4/26/2016.
 */
public class AddWordToPackage extends Activity {
    private int indexSelected = -1;
    private DatabaseManager dbManager;
    List<Word> allWordsNotInPackage;
    private String idPackage;
    private ListView listView;
    private CustomListAdapter customAdapter;
    private Package pack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_create_edit);


        dbManager = DatabaseManager.getInstance(getApplicationContext());

        TextView title = (TextView) findViewById(R.id.package_name);

        LinearLayout lv = (LinearLayout) findViewById(R.id.add_word_options);
        lv.setVisibility(View.GONE);

        idPackage = getIntent().getStringExtra("idPackage");
        pack = dbManager.getPackageFromId(idPackage);

        title.setText("Add Words: " + pack.getPackageName());


        listView = (ListView) findViewById(R.id.list);
        setUpWordList();

    }

    private void setUpWordList() {
        List<Word> wordListForPackage = dbManager.getWordsFromPackage(idPackage);
        allWordsNotInPackage = (dbManager.getWords());

        for (int i = 0; i < wordListForPackage.size(); i++) {
            for (int x = 0; x < allWordsNotInPackage.size(); x++) {
                if (wordListForPackage.get(i).getIdWord().equals(allWordsNotInPackage.get(x).getIdWord())) {
                    allWordsNotInPackage.remove(x);
                }
            }
        }

        customAdapter = new CustomListAdapter(this);
        listView.setAdapter(customAdapter);

        registerForContextMenu(listView);


    }


    private void resetVariables() {
        allWordsNotInPackage.clear();
        List<Word> wordListForPackage = dbManager.getWordsFromPackage(idPackage);
        List<Word> temp = (dbManager.getWords());

        for (int i = 0; i < wordListForPackage.size(); i++) {
            for (int x = 0; x < temp.size(); x++) {
                if (wordListForPackage.get(i).getIdWord().equals(temp.get(x).getIdWord())) {
                    temp.remove(x);
                }
            }
        }

        allWordsNotInPackage.addAll(temp);
        customAdapter.notifyDataSetChanged();


    }


    private class CustomListAdapter extends BaseAdapter {
        Context context;

        private LayoutInflater inflater = null;

        public CustomListAdapter(Context context) {
            // TODO Auto-generated constructor stub
            super();
            this.context = context;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return allWordsNotInPackage.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder {
            TextView questionHolder;
            TextView answerHolder;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.custom_list_words, null);
            holder.questionHolder = (TextView) rowView.findViewById(R.id.text_view_question);
            holder.answerHolder = (TextView) rowView.findViewById(R.id.text_view_answer);
            holder.questionHolder.setText(allWordsNotInPackage.get(position).getQuestion());
            holder.answerHolder.setText(allWordsNotInPackage.get(position).getAnswer());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub


                    long newId = dbManager.insertIntoTable_WordPackage(allWordsNotInPackage.get(position).getIdWord(), idPackage);

                    Toast.makeText(AddWordToPackage.this, "Word added", Toast.LENGTH_SHORT).show();

                    resetVariables();


                }
            });

            rowView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    indexSelected = position;
                    return false;
                }
            });

            return rowView;

        }

    }

}

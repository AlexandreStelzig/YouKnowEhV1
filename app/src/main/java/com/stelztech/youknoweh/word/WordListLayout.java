package com.stelztech.youknoweh.word;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stelztech.youknoweh.Database.DatabaseManager;
import com.stelztech.youknoweh.Database.Word;
import com.stelztech.youknoweh.R;

import java.util.List;

/**
 * Created by Alexandre on 4/24/2016.
 */
public class WordListLayout extends Activity {

    private int indexSelected = -1;

    private ListView list;
    private List<Word> wordsList;
    private DatabaseManager dbManager;
    CustomListAdapter customAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);

        TextView title = (TextView) findViewById(R.id.list_title);
        title.setText("Word List");

        dbManager = DatabaseManager.getInstance(getApplicationContext());
        wordsList = dbManager.getWords();

        list = (ListView) findViewById(R.id.list);
        setUpWordList();

        for(int i = 0; i < wordsList.size();i++)
            System.out.println(wordsList.get(i).getQuestion());

    }

    private void setUpWordList() {

        customAdapter = new CustomListAdapter(this);
        list.setAdapter(customAdapter);

        registerForContextMenu(list);

    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        indexSelected = info.position;

        if (v.getId() == R.id.list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.option_dialog_2, menu);
        }
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete:
                dialogYesNo().show();
                return true;
            default:
                indexSelected = -1;
                return super.onContextItemSelected(item);
        }
    }

    public void newPackageButtonClicked(View view) {
        Intent intent = new Intent(this, NewWord.class);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resetVariables();
        System.out.println("here");
    }

    private void resetVariables() {
        wordsList.clear();
        wordsList.addAll(dbManager.getWords());
        customAdapter.notifyDataSetChanged();
    }


    private AlertDialog dialogYesNo() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView alertText = new EditText(this);
        alertText.setText("Are you sure you want to delete \'" + wordsList.get(indexSelected).getQuestion() + "\' word?");
        builder.setView(alertText);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            // when button OK is press
            public void onClick(DialogInterface dialog, int which) {
                deleteWordFromDatabase();
            }
        });
        // if cancel button is press, close dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;

    }

    private void deleteWordFromDatabase() {
        dbManager.deleteFromTable_Word((String) wordsList.get(indexSelected).getIdWord());
        Toast.makeText(WordListLayout.this, "word deleted", Toast.LENGTH_SHORT).show();
        resetVariables();
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
            return wordsList.size();
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
            holder.questionHolder.setText(wordsList.get(position).getQuestion());
            holder.answerHolder.setText(wordsList.get(position).getAnswer());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Intent intent = new Intent(getApplicationContext(), WordInfo.class);
                    intent.putExtra("id", wordsList.get(position).getIdWord());
                    startActivityForResult(intent, 0);


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

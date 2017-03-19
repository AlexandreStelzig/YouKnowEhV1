package com.stelztech.youknoweh.pack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stelztech.youknoweh.Database.DatabaseManager;
import com.stelztech.youknoweh.Database.Package;
import com.stelztech.youknoweh.Database.Word;
import com.stelztech.youknoweh.R;
import com.stelztech.youknoweh.word.NewWord;
import com.stelztech.youknoweh.word.WordInfo;
import com.stelztech.youknoweh.word.WordListLayout;

import java.util.List;

/**
 * Created by Alexandre on 4/25/2016.
 */
public class UniquePackageInfo extends Activity {

    private int indexSelected = -1;
    private DatabaseManager dbManager;
    List<Word> wordsInPackageList;
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




        idPackage = getIntent().getStringExtra("id");
        pack = dbManager.getPackageFromId(idPackage);

        title.setText(pack.getPackageName());


        wordsInPackageList = dbManager.getWordsFromPackage(idPackage);


        System.out.println("current words in package = " + wordsInPackageList.size());
        listView = (ListView) findViewById(R.id.list);
        setUpWordList();
        registerForContextMenu(listView);
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

    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete:
                dbManager.deleteFromTable_WordPackage(wordsInPackageList.get(indexSelected).getIdWord(), idPackage);
                Toast.makeText(UniquePackageInfo.this, "word delete from package", Toast.LENGTH_SHORT).show();
                resetValues();
                return true;
            default:
                indexSelected = -1;
                return super.onContextItemSelected(item);
        }
    }

    private void resetValues() {
        wordsInPackageList.clear();
        List<Word> temp = dbManager.getWordsFromPackage(idPackage);
        wordsInPackageList.addAll(temp);
        customAdapter.notifyDataSetChanged();
    }

    public void addExistingButtonClicked(View view) {
        Intent intent = new Intent(this, AddWordToPackage.class);
        intent.putExtra("idPackage", idPackage);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resetValues();
    }

    private void setUpWordList() {

        customAdapter = new CustomListAdapter(this);
        listView.setAdapter(customAdapter);

        registerForContextMenu(listView);

    }

    public void createNewButtonClicked(View view) {
        Intent intent = new Intent(this, NewWord.class);
        intent.putExtra("idPackage", idPackage);
        startActivityForResult(intent, 0);


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
            return wordsInPackageList.size();
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
            holder.questionHolder.setText(wordsInPackageList.get(position).getQuestion());
            holder.answerHolder.setText(wordsInPackageList.get(position).getAnswer());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Intent intent = new Intent(getApplicationContext(), WordInfo.class);
                    intent.putExtra("id", wordsInPackageList.get(position).getIdWord());
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

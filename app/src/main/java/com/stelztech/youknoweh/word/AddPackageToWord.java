package com.stelztech.youknoweh.word;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stelztech.youknoweh.Database.DatabaseManager;
import com.stelztech.youknoweh.Database.Package;
import com.stelztech.youknoweh.Database.Word;
import com.stelztech.youknoweh.Database.WordPackage;
import com.stelztech.youknoweh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre on 4/28/2016.
 */
public class AddPackageToWord extends Activity {
    private int indexSelected = -1;
    private DatabaseManager dbManager;
    List<Package> allPackagesForWord;
    private String idWord;
    private ListView listView;
    private Word word;

    private List<String> adapterPackageName;
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_create_edit);




        dbManager = DatabaseManager.getInstance(getApplicationContext());

        TextView title = (TextView) findViewById(R.id.package_name);

        LinearLayout lv = (LinearLayout) findViewById(R.id.add_word_options);
        lv.setVisibility(View.GONE);

        idWord = getIntent().getStringExtra("idWord");
        word = dbManager.getWordFromId(idWord);

        title.setText(word.getQuestion());


        listView = (ListView) findViewById(R.id.list);
        setUpPackageList();
        
    }

    private void setUpPackageList() {

        List<Package> packageListForWord = dbManager.getPackagesFromWords(idWord);
        allPackagesForWord = (dbManager.getPackages());


        for (int i = 0; i < packageListForWord.size(); i++) {
            for (int x = 0; x < allPackagesForWord.size(); x++) {
                if (packageListForWord.get(i).getIdPackage().equals(allPackagesForWord.get(x).getIdPackage())) {
                    allPackagesForWord.remove(x);
                }
            }
        }

        adapterPackageName = new ArrayList<String>();
        for(int y = 0; y < allPackagesForWord.size();y++){
            adapterPackageName.add(allPackagesForWord.get(y).getPackageName());
        }

        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_list, adapterPackageName);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long newId = dbManager.insertIntoTable_WordPackage(idWord, allPackagesForWord.get(position).getIdPackage());

                Toast.makeText(AddPackageToWord.this, "Package added", Toast.LENGTH_SHORT).show();

                resetVariables();
            }
        });

    }


    private void resetVariables() {
        allPackagesForWord.clear();
        List<Package> packageListForWord = dbManager.getPackagesFromWords(idWord);
        allPackagesForWord = (dbManager.getPackages());

        for (int i = 0; i < packageListForWord.size(); i++) {
            for (int x = 0; x < allPackagesForWord.size(); x++) {
                if (packageListForWord.get(i).getIdPackage().equals(allPackagesForWord.get(x).getIdPackage())) {
                    allPackagesForWord.remove(x);
                }
            }
        }

        List<String> temp = new ArrayList<String>();
        for(int y = 0; y < allPackagesForWord.size();y++){
            temp.add(allPackagesForWord.get(y).getPackageName());
        }


        adapterPackageName.clear();
        adapterPackageName.addAll(temp);
        adapter.notifyDataSetChanged();


    }

}

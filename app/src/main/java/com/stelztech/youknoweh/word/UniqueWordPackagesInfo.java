package com.stelztech.youknoweh.word;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stelztech.youknoweh.Database.DatabaseManager;
import com.stelztech.youknoweh.Database.Package;
import com.stelztech.youknoweh.Database.Word;
import com.stelztech.youknoweh.R;
import com.stelztech.youknoweh.pack.AddWordToPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre on 4/28/2016.
 */
public class UniqueWordPackagesInfo extends Activity {

    private DatabaseManager dbManager;
    private String idWord;
    private Word word;
    List<Package> packagesInWord;
    private ListView listView;
    private int indexSelected = -1;
    private String message = "";

    private List<String> adapterPackageName;

    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_create_edit);
        dbManager = DatabaseManager.getInstance(getApplicationContext());
        TextView title = (TextView) findViewById(R.id.package_name);

        idWord = getIntent().getStringExtra("idWord");
        word = dbManager.getWordFromId(idWord);

        title.setText(word.getQuestion());


        Button addButton = (Button) findViewById(R.id.add_button_id);
        Button createButton = (Button) findViewById(R.id.create_button_id);
        addButton.setText("Existing Package");
        createButton.setText("New Package");

        packagesInWord = dbManager.getPackagesFromWords(idWord);


        System.out.println("current words in package = " + packagesInWord.size());
        listView = (ListView) findViewById(R.id.list);
        setUpList();
        registerForContextMenu(listView);
    }

    private void setUpList() {

        adapterPackageName = new ArrayList<String>();
        for (int i = 0; i < packagesInWord.size(); i++) {
            adapterPackageName.add(packagesInWord.get(i).getPackageName());
        }
        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_list, adapterPackageName);

        listView.setAdapter(adapter);
        registerForContextMenu(listView);

    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        indexSelected = info.position;

        if (v.getId() == R.id.list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.option_dialog_1, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit:
                packageDialog("update").show();
                return true;
            case R.id.delete:
                boolean worked = dbManager.deleteFromTable_WordPackage(idWord, packagesInWord.get(indexSelected).getIdPackage());

                if (worked) {
                    Toast.makeText(UniqueWordPackagesInfo.this, "package remove from word", Toast.LENGTH_SHORT).show();
                }
                resetVariables();
                return true;
            default:
                indexSelected = -1;
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resetVariables();
    }

    private void resetVariables() {

        packagesInWord.clear();
        adapterPackageName.clear();


        packagesInWord = dbManager.getPackagesFromWords(idWord);
        List<String> temp = new ArrayList<String>();
        for (int i = 0; i < packagesInWord.size(); i++) {
            temp.add(packagesInWord.get(i).getPackageName());
        }

        adapterPackageName.addAll(temp);

        adapter.notifyDataSetChanged();
    }


    public void createNewButtonClicked(View view) {
        packageDialog("new").show();
    }

    public void addExistingButtonClicked(View view) {
        Intent intent = new Intent(this, AddPackageToWord.class);
        intent.putExtra("idWord", idWord);
        startActivityForResult(intent, 0);
    }


    private AlertDialog packageDialog(final String dialogType) {
        message = "";

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        // set if here
        if (dialogType.equals("new")) {
            input.setHint("Package name");
            builder.setTitle("New Package");
        } else if (dialogType.equals("update")) {
            input.setText((String) adapterPackageName.get(indexSelected));
            builder.setTitle("Updating \'" + adapterPackageName.get(indexSelected) + "\'");
        } else {
            Toast.makeText(UniqueWordPackagesInfo.this, "Error in dialog", Toast.LENGTH_SHORT).show();
        }

        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            // when button OK is press
            public void onClick(DialogInterface dialog, int which) {

                message = input.getText().toString();
                // check if valid name
                if (message.equals("")) {
                    Toast.makeText(UniqueWordPackagesInfo.this, "invalid name", Toast.LENGTH_SHORT).show();
                }
                // if new package
                else if (dialogType.equals("new")) {
                    addToDatabase();
                    dialog.dismiss();
                    Toast.makeText(UniqueWordPackagesInfo.this, "package added", Toast.LENGTH_SHORT).show();
                }
                // if updating package
                else if (dialogType.equals("update")) {
                    updateDatabase();
                    dialog.dismiss();
                    Toast.makeText(UniqueWordPackagesInfo.this, "package name changed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UniqueWordPackagesInfo.this, "Error in dialog", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // if cancel button is press, close dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // opens keyboard on creation with selection at the end
        AlertDialog dialog = builder.create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        input.setSelection(input.getText().length());
        return dialog;


    }

    private void addToDatabase() {
        long packageId = dbManager.insertIntoTable_Package(message);
        dbManager.insertIntoTable_WordPackage(idWord, packageId + "");
        resetVariables();
    }

    private void updateDatabase() {

        if (!message.equals((String) adapterPackageName.get(indexSelected))) {
            dbManager.updateFromTable_Package((String) packagesInWord.get(indexSelected).getIdPackage(), message);
            resetVariables();
        } else {
            Toast.makeText(UniqueWordPackagesInfo.this, "invalid: same name", Toast.LENGTH_SHORT).show();

        }
    }
}

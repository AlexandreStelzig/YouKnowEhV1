package com.stelztech.youknoweh;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Alexandre on 4/24/2016.
 */
public class WordPackageList extends Activity {

    ListView listView;
    ArrayAdapter<String> adapter;
    private String message = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);


        listView = (ListView) findViewById(R.id.list);

        ArrayList<String> arrayList = new ArrayList<String>();

        for(int i = 0 ; i < 100; i++){
            arrayList.add(""+ i);
        }

        adapter = new ArrayAdapter<String>(WordPackageList.this, R.layout.custom_list, arrayList);
        listView.setAdapter(adapter);
    }


//    // when the button new is clicked, pop a dialog to create a new one
//    public void newPackageButtonClicked(View view) {
//        AlertDialog dialog = packageDialog("new");
//        dialog.show();
//    }
//
//    // Alertdialog for new and update
//    private AlertDialog packageDialog(final String dialogType) {
//        message = "";
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        // set if here
//        input.setHint("Package name");
//        builder.setTitle("New Package");
//        builder.setView(input);
//
//        // Set up the buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            // when button OK is press
//            public void onClick(DialogInterface dialog, int which) {
//
//                m_Text = input.getText().toString();
//                // check if valid name
//                if (m_Text.equals("")) {
//                    Toast.makeText(ClassList.this, "invalid name", Toast.LENGTH_SHORT).show();
//                }
//                // if new package
//
//                    MainMenu.dbManager.insertIntoTable_Package(m_Text);
//                    resetVariables();
//                    dialog.dismiss();
//
//                // if updating package
//
//
//            }
//        });
//    }

}

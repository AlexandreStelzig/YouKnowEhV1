package com.stelztech.youknoweh.pack;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stelztech.youknoweh.Database.DatabaseManager;
import com.stelztech.youknoweh.Database.Package;
import com.stelztech.youknoweh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre on 4/24/2016.
 */
public class PackageList extends Activity {

    private ListView listView;

    private DatabaseManager dbManager;

    private String message = "";
    private int indexSelected;

    private List<Package> packageList;
    private List<String> adapterPackageName;

    private ArrayAdapter<String> adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);

        TextView title = (TextView) findViewById(R.id.list_title);
        title.setText("Package List");

        indexSelected = -1;

        listView = (ListView) findViewById(R.id.list);
        dbManager = DatabaseManager.getInstance(getApplicationContext());
        packageList = dbManager.getPackages();

        setUpList();
    }

    private void setUpList() {
        adapterPackageName = new ArrayList<String>();
        for (int i = 0; i < packageList.size(); i++) {
            adapterPackageName.add(packageList.get(i).getPackageName());
        }
        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_list, adapterPackageName);

        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                openPackageInfo(position);
            }
        });
    }

    private void openPackageInfo(int position) {
        Intent intent = new Intent(this, UniquePackageInfo.class);
        intent.putExtra("id", packageList.get(position).getIdPackage());
        startActivity(intent);
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit:
                updatePackageName();
                return true;
            case R.id.delete:
                dialogYesNo().show();
                return true;
            default:
                indexSelected = -1;
                return super.onContextItemSelected(item);
        }
    }

    private void updatePackageName() {
        AlertDialog dialog = packageDialog("update");
        dialog.show();
    }


    public void newPackageButtonClicked(View view) {
        AlertDialog dialog = packageDialog("new");
        dialog.show();
    }



    // DIALOGS

    // Alertdialog for new and update
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
            Toast.makeText(PackageList.this, "Error in dialog", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PackageList.this, "invalid name", Toast.LENGTH_SHORT).show();
                }
                // if new package
                else if (dialogType.equals("new")) {
                    addToDatabase();
                    dialog.dismiss();
                    Toast.makeText(PackageList.this, "package added", Toast.LENGTH_SHORT).show();
                }
                // if updating package
                else if (dialogType.equals("update")) {
                    updateDatabase();
                    dialog.dismiss();

                } else {
                    Toast.makeText(PackageList.this, "Error in dialog", Toast.LENGTH_SHORT).show();
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

    private AlertDialog dialogYesNo() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView alertText = new EditText(this);
        alertText.setText("Are you sure you want to delete \'" + adapterPackageName.get(indexSelected) + "\' package?");
        builder.setView(alertText);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            // when button OK is press
            public void onClick(DialogInterface dialog, int which) {
                deleteFromDatabase();
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

    // DATABASE HANDLING

    private void addToDatabase() {
        dbManager.insertIntoTable_Package(message);
        resetVariables();
    }
    private void deleteFromDatabase() {
        dbManager.deleteFromTable_Package((String) packageList.get(indexSelected).getIdPackage());
        Toast.makeText(PackageList.this, "package deleted", Toast.LENGTH_SHORT).show();
        resetVariables();
    }

    private void updateDatabase() {

        if (!message.equals((String) adapterPackageName.get(indexSelected))) {
            dbManager.updateFromTable_Package((String) packageList.get(indexSelected).getIdPackage(), message);
            resetVariables();
            Toast.makeText(PackageList.this, "package name changed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PackageList.this, "invalid: same name", Toast.LENGTH_SHORT).show();

        }
    }

    private void resetVariables() {

        packageList.clear();
        adapterPackageName.clear();


        packageList = dbManager.getPackages();
        List<String> temp = new ArrayList<String>();
        for (int i = 0; i < packageList.size(); i++) {
            temp.add(packageList.get(i).getPackageName());
        }

        adapterPackageName.addAll(temp);

        adapter.notifyDataSetChanged();
    }


}

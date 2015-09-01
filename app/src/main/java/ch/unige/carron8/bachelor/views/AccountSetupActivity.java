package ch.unige.carron8.bachelor.views;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.controllers.account.AccountController;
import ch.unige.carron8.bachelor.controllers.sensor.SensorController;
import ch.unige.carron8.bachelor.models.Account;
import ch.unige.carron8.bachelor.models.OfficeRoom;

/**
 * Creates a view to set up or update the user's account.
 * Created by Blaise on 27.04.2015.
 */
public class AccountSetupActivity extends AppCompatActivity {
    private AccountController mAccountController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the layout
        setContentView(R.layout.activity_account_setup);
        //Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get the Account Controller
        this.mAccountController = AccountController.getInstance(getApplicationContext());

        //Set the values
        populate();
    }

    public void populate() {
        //Set the office spinner rooms
        Spinner spinner = (Spinner) findViewById(R.id.office);
        ArrayAdapter<OfficeRoom> adapter = new ArrayAdapter<OfficeRoom>(this, android.R.layout.simple_list_item_1, OfficeRoom.values());
        spinner.setAdapter(adapter);

        Account account = mAccountController.getAccount();
        if (account != null) {
            ((EditText) findViewById(R.id.name)).setText(account.getmName());
            ((EditText) findViewById(R.id.surname)).setText(account.getmSurname());
            spinner.setSelection(adapter.getPosition(account.getmOffice()));
            ((EditText) findViewById(R.id.light_target)).setText(String.valueOf(account.getmTargetLight()));
            ((EditText) findViewById(R.id.temp_target)).setText(String.valueOf(account.getmTargetTemp()));
        }
    }

    /**
     * Creates or updates the user's account according to the data entered by the user in the fields displayed by the activity.
     *
     * @param view the view who called the method
     */
    public void createAccount(View view) {
        //Create intent to return to the main screen
        Intent intent = new Intent(this, MainActivity.class);
        //Get the values from the fields
        EditText nameText = (EditText) findViewById(R.id.name);
        EditText surnameText = (EditText) findViewById(R.id.surname);
        Spinner officeSpinner = (Spinner) findViewById(R.id.office);
        EditText lightText = (EditText) findViewById(R.id.light_target);
        EditText tempText = (EditText) findViewById(R.id.temp_target);
        String name = nameText.getText().toString();
        String surname = surnameText.getText().toString();
        OfficeRoom office = (OfficeRoom) officeSpinner.getSelectedItem();
        String targetLight = lightText.getText().toString();
        String targetTemp = tempText.getText().toString();

        if (name.equals("") || surname.equals("") || office.equals("") || targetLight.equals("") || targetTemp.equals("")) {
            Toast.makeText(this, R.string.account_setup_empty_fields, Toast.LENGTH_LONG).show();
        } else {
            String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            Account account = new Account(id, name, surname, office, Integer.parseInt(targetLight), Integer.parseInt(targetTemp), SensorController.getInstance(this).getmAvailableSensors());

            if (mAccountController.getAccount() == null) {
                //Save account
                AccountController.getInstance(getApplicationContext()).createAccount(account);
                Toast.makeText(this, R.string.account_setup_acc_created, Toast.LENGTH_LONG).show();
                startActivity(intent);
            } else {
                //Update account
                Account oldAccount = AccountController.getInstance(this).getAccount();
                oldAccount.updateAccount(account.getmName(),account.getmSurname(),account.getmOffice(),account.getmTargetLight(),account.getmTargetTemp());
                AccountController.getInstance(getApplicationContext()).saveAccount(oldAccount);
                Toast.makeText(this, R.string.account_setup_acc_updated, Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        }
    }
}

package ch.unige.carron8.bachelor.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ch.unige.carron8.bachelor.R;
import ch.unige.carron8.bachelor.controllers.account.AccountController;
import ch.unige.carron8.bachelor.models.Account;

/**
 * Creates a view to set up or update the user's account.
 * Created by Blaise on 27.04.2015.
 */
public class AccountSetup extends AppCompatActivity {
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
        Account account = mAccountController.getAccount();
        if (account != null) {
            ((EditText) findViewById(R.id.name)).setText(account.getmName());
            ((EditText) findViewById(R.id.surname)).setText(account.getmSurname());
            ((EditText) findViewById(R.id.office)).setText(account.getmOffice());
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
        EditText officeText = (EditText) findViewById(R.id.office);
        String name = nameText.getText().toString();
        String surname = surnameText.getText().toString();
        String office = officeText.getText().toString();

        if (name.equals("") || surname.equals("") || office.equals("")) {
            Toast.makeText(this, R.string.account_setup_empty_fields, Toast.LENGTH_LONG).show();
        } else {
            Account account = new Account(name, surname, office);

            if (mAccountController.getAccount() == null) {
                //Save account
                AccountController.getInstance(getApplicationContext()).createAccount(account);
                Toast.makeText(this, R.string.account_setup_acc_created, Toast.LENGTH_LONG).show();
                startActivity(intent);
            } else {
                //Update account
                AccountController.getInstance(getApplicationContext()).saveAccount(account);
                Toast.makeText(this, R.string.account_setup_acc_updated, Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        }
    }
}

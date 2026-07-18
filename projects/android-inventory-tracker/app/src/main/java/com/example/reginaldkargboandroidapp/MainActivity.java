package com.example.reginaldkargboandroidapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.reginaldkargboandroidapp.data.AuthRepository;
import com.example.reginaldkargboandroidapp.data.DatabaseHelper;
import com.example.reginaldkargboandroidapp.data.InventoryRepository;
import com.example.reginaldkargboandroidapp.model.InventoryItem;
import com.example.reginaldkargboandroidapp.model.User;
import com.example.reginaldkargboandroidapp.viewmodel.AuthViewModel;
import com.example.reginaldkargboandroidapp.viewmodel.InventoryViewModel;

/** View layer. Business and persistence logic are delegated to ViewModels and repositories. */
public class MainActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE = 101;
    private AuthViewModel authViewModel;
    private InventoryViewModel inventoryViewModel;
    private DatabaseHelper databaseHelper;
    private User currentUser;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        authViewModel.initialize(new AuthRepository(databaseHelper));
        showLogin();
    }

    private void showLogin() {
        setContentView(R.layout.activity_login);
        EditText username = findViewById(R.id.usernameInput);
        EditText password = findViewById(R.id.passwordInput);
        findViewById(R.id.loginButton).setOnClickListener(v -> {
            AuthViewModel.Result result = authViewModel.login(username.getText().toString(), password.getText().toString());
            if (!result.success) { message(result.message); return; }
            currentUser = result.user;
            inventoryViewModel.initialize(new InventoryRepository(databaseHelper), currentUser.getUsername());
            showInventory();
        });
        findViewById(R.id.createAccountButton).setOnClickListener(v -> showCreateAccount());
    }

    private void showCreateAccount() {
        setContentView(R.layout.activity_create_account);
        EditText username = findViewById(R.id.newUsernameInput);
        EditText password = findViewById(R.id.newPasswordInput);
        findViewById(R.id.saveAccountButton).setOnClickListener(v -> {
            AuthViewModel.Result result = authViewModel.createAccount(username.getText().toString(), password.getText().toString());
            message(result.message);
            if (result.success) showLogin();
        });
        findViewById(R.id.backToLoginButton).setOnClickListener(v -> showLogin());
    }

    private void showInventory() {
        setContentView(R.layout.activity_inventory);
        ((TextView)findViewById(R.id.welcomeText)).setText("Logged in as: " + currentUser.getUsername());
        EditText name = findViewById(R.id.itemNameInput);
        EditText quantity = findViewById(R.id.itemQuantityInput);
        EditText location = findViewById(R.id.itemLocationInput);
        findViewById(R.id.addItemButton).setOnClickListener(v -> {
            InventoryViewModel.Result result = inventoryViewModel.add(name.getText().toString(), quantity.getText().toString(), location.getText().toString());
            message(result.message);
            if (result.success) showInventory();
        });
        findViewById(R.id.smsSettingsButton).setOnClickListener(v -> showSms());
        findViewById(R.id.logoutButton).setOnClickListener(v -> { currentUser = null; showLogin(); });
        renderInventory((TableLayout)findViewById(R.id.inventoryTable));
    }

    private void renderInventory(TableLayout table) {
        table.removeAllViews();
        TableRow header = new TableRow(this);
        for (String text : new String[]{"Item", "Qty", "Location", "Actions"}) header.addView(cell(text, true));
        table.addView(header);
        for (InventoryItem item : inventoryViewModel.items()) {
            TableRow row = new TableRow(this);
            row.addView(cell(item.getName(), false));
            row.addView(cell(String.valueOf(item.getQuantity()), false));
            row.addView(cell(item.getLocation(), false));
            LinearLayout actions = new LinearLayout(this);
            Button edit = smallButton("Edit");
            Button delete = smallButton("Delete");
            edit.setOnClickListener(v -> showEditDialog(item));
            delete.setOnClickListener(v -> confirmDelete(item));
            actions.addView(edit); actions.addView(delete); row.addView(actions); table.addView(row);
        }
        if (inventoryViewModel.items().isEmpty()) {
            TableRow row = new TableRow(this); row.addView(cell("No inventory items yet.", false)); table.addView(row);
        }
    }

    private void showEditDialog(InventoryItem item) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_item, null);
        EditText name = view.findViewById(R.id.editItemName);
        EditText quantity = view.findViewById(R.id.editItemQuantity);
        EditText location = view.findViewById(R.id.editItemLocation);
        name.setText(item.getName()); quantity.setText(String.valueOf(item.getQuantity())); location.setText(item.getLocation());
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Edit Inventory Item").setView(view)
                .setNegativeButton("Cancel", null).setPositiveButton("Save", null).create();
        dialog.setOnShowListener(x -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            InventoryViewModel.Result result = inventoryViewModel.update(item.getId(), name.getText().toString(), quantity.getText().toString(), location.getText().toString());
            message(result.message);
            if (result.success) { dialog.dismiss(); showInventory(); }
        }));
        dialog.show();
    }

    private void confirmDelete(InventoryItem item) {
        new AlertDialog.Builder(this).setTitle("Delete item?").setMessage("Delete " + item.getName() + " from your inventory?")
                .setNegativeButton("Cancel", null).setPositiveButton("Delete", (d,w) -> {
                    InventoryViewModel.Result result = inventoryViewModel.delete(item.getId()); message(result.message); if (result.success) showInventory();
                }).show();
    }

    private void showSms() {
        setContentView(R.layout.activity_sms);
        TextView status = findViewById(R.id.smsPermissionStatus);
        boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        status.setText(granted ? "SMS permission status: Granted" : "SMS permission status: Not granted");
        findViewById(R.id.requestSmsPermissionButton).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
            else message("SMS permission is already granted.");
        });
        findViewById(R.id.backToInventoryButton).setOnClickListener(v -> showInventory());
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            message(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ? "SMS permission granted." : "SMS permission denied. The app still works without SMS alerts.");
            showSms();
        }
    }

    private TextView cell(String text, boolean bold) {
        TextView cell = new TextView(this); cell.setText(text); cell.setPadding(10,12,10,12); if (bold) cell.setTextAppearance(android.R.style.TextAppearance_Material_Body1); return cell;
    }
    private Button smallButton(String text) { Button b = new Button(this); b.setText(text); b.setAllCaps(false); return b; }
    private void message(String text) { Toast.makeText(this, text, Toast.LENGTH_LONG).show(); }
}

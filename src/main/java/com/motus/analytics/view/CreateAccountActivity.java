package com.motus.analytics.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.motus.analytics.R;
import com.motus.analytics.model.database.AppDatabase;
import com.motus.analytics.model.entity.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateAccountActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPhone, editTextPassword, editTextPasswordAgain;
    Button buttonSubmit;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextTextCreatePassword);
        editTextPasswordAgain = findViewById(R.id.editTextTextCreatePasswordAgain);
        buttonSubmit = findViewById(R.id.button_submit);

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "users")
                .build();
    }

    public boolean validateFields(String name, String email, String phone, String password, String passwordAgain) {
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(passwordAgain)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void submitForm(View view) {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordAgain = editTextPasswordAgain.getText().toString();

        if (validateFields(name, email, phone, password, passwordAgain)) {
            User user = new User(name, phone, email, password);
            addUserBackground(user);
            finish();
        }
    }

    public void addUserBackground(User user) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            // Background Task
            database.userDao().addUser(user);
            // On Finish Task
            handler.post(() -> {
                Toast.makeText(CreateAccountActivity.this, "User Added", Toast.LENGTH_SHORT).show();
            });
        });
    }
}
package com.motus.analytics.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.motus.analytics.R;
import com.motus.analytics.model.database.AppDatabase;
import com.motus.analytics.model.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText textEmail, textPassword;
    AppDatabase database;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textEmail = findViewById(R.id.text_email);
        textPassword = findViewById(R.id.text_password);

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "users")
                .build();
    }

    public void signIn(View view) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();

        executorService.execute(() -> {
            // Background Task
            user = database.userDao().getUserByEmail(email);
            // On Finish Task
            handler.post(() -> {
                if (verifyLogin(user, password)) {
                    goToMotus();
                } else {
                    Toast.makeText(LoginActivity.this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private boolean verifyLogin(User user, String password) {
        if (user != null) {
            return user.getPassword().equals(password);
        } else {
            return false;
        }
    }
    public void goToCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
    public void goToMotus() {
        Intent intent = new Intent(this, MotusActivity.class);
        startActivity(intent);
    }
}
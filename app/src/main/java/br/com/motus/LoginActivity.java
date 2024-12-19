package br.com.motus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.motus.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextTextEmailAddressLogin);
        editTextPassword = findViewById(R.id.editTextTextPassword2);
    }

    public void validateLogin(View view) {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Carregar os dados do usuário salvos
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String savedPhone = sharedPreferences.getString("phone", "");
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");

        // Criar uma instância do usuário salvo
        User usuarioSalvo = new User(savedPhone, savedEmail, savedPassword);

        // Validar as credenciais
        User usuarioAtual = new User("", email, password);
        if (usuarioAtual.getEmail().equals(usuarioSalvo.getEmail()) &&
                usuarioAtual.getPassword().equals(usuarioSalvo.getPassword())) {
            goToMotus();
        } else {
            Toast.makeText(this, "Credenciais incorretas. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMotus() {
        Intent intent = new Intent(this, MotusActivity.class);
        startActivity(intent);
    }
}

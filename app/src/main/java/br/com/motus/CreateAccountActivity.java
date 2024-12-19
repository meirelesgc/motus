package br.com.motus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.motus.models.User;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextRePassword = findViewById(R.id.editTextReTextPassword);
    }

    public void createAccount(View view) {
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String rePassword = editTextRePassword.getText().toString().trim();

        if (!password.equals(rePassword)) {
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(phone, email, password);

        // Validar dados
        if (!user.isValid()) {
            StringBuilder errorMessage = new StringBuilder("Erros encontrados:");
            if (!user.isValidPhone()) {
                errorMessage.append("\n- Telefone inválido.");
            }
            if (!user.isValidEmail()) {
                errorMessage.append("\n- E-mail inválido.");
            }
            if (!user.isValidPassword()) {
                errorMessage.append("\n- Senha deve ter pelo menos 6 caracteres.");
            }

            Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Persistir dados utilizando SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", user.getPhone());
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.apply();

        Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }

}

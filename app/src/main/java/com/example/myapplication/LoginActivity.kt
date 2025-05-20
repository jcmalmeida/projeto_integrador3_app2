package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val intent = Intent(this, ListaRiscosActivity::class.java)
            startActivity(intent)
        }

        var layoutEmail = findViewById<EditText>(R.id.userEmail)
        val layoutSenha = findViewById<EditText>(R.id.userPassword)

        val botaoEntrar = findViewById<Button>(R.id.btnLogin)

        botaoEntrar.setOnClickListener {
            val emailDigitado = layoutEmail.text.toString()
            val senhaDigitada = layoutSenha.text.toString()

            login(emailDigitado, senhaDigitada)
        }
    }

    private fun login(email: String, psw: String) {
        auth.signInWithEmailAndPassword(email, psw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // daqui dá pra direcionar pra outras atividades já logado
                    // por exemplo:
                     val intent = Intent(this, ListaRiscosActivity::class.java)
                     startActivity(intent)
                } else {
                    Toast.makeText(
                        baseContext,
                        "A autenticação falhou! Por favor, cheque o email e senha.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    override fun onResume() {
        super.onResume()

        var layoutEmail = findViewById<EditText>(R.id.userEmail)
        val layoutSenha = findViewById<EditText>(R.id.userPassword)

        layoutEmail.text.clear()
        layoutSenha.text.clear()
    }
}

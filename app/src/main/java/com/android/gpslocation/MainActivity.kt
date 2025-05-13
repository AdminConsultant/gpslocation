import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.gpslocation.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var buttonStartConnection: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

        // Inizializza Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Trova il bottone "Avvia connessione"
        buttonStartConnection = findViewById(R.id.buttonStartConnection)

        // Imposta il listener per il bottone
        buttonStartConnection.setOnClickListener {
            // Mostra il popup di conferma connessione
            showConnectionConfirmationDialog()
        }
    }

    // Funzione per mostrare il popup di conferma connessione
    private fun showConnectionConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Connessione")
        builder.setMessage("Vuoi avviare la connessione?")

        // Aggiungi il tasto "OK"
        builder.setPositiveButton("Avvia") { dialog, which ->
            startConnection()
        }

        // Aggiungi il tasto "Annulla"
        builder.setNegativeButton("Annulla") { dialog, which ->
            dialog.dismiss()  // Chiude il dialogo senza fare nulla
        }

        // Mostra il dialogo
        builder.show()
    }

    // Funzione per avviare la connessione
    private fun startConnection() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Connessione riuscita
                    val user = auth.currentUser
                    Toast.makeText(this, "Connessione riuscita come utente anonimo", Toast.LENGTH_SHORT).show()
                } else {
                    // Connessione fallita
                    Toast.makeText(this, "Errore di connessione: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

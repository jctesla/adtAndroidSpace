
/*
 Puedes utilizar la clase Timer y TimerTask en Android para crear un temporizador con un periodo de 1 segundo.
 */
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

  private TextView timerTextView;
  private Timer timer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    timerTextView = findViewById(R.id.timerTextView);

    // Crea un temporizador que se ejecuta cada 1000 milisegundos (1 segundo)
    timer = new Timer();
    timer.scheduleAtFixedRate(new UpdateTimerTask(), 0, 1000);
  }

  private class UpdateTimerTask extends TimerTask {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void run() {
      // Actualiza la interfaz de usuario en el hilo principal
      handler.post(new Runnable() {
        @Override
        public void run() {
          updateUI();
        }
      });
    }
  }

  private void updateUI() {
    // Actualiza la interfaz de usuario según sea necesario
    // Este método se llama cada segundo
    // Puedes realizar acciones adicionales aquí
    timerTextView.setText("Segundos transcurridos: " + timerCount);
    timerCount++;
  }

  private int timerCount = 0;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Detén el temporizador cuando la actividad se destruye para evitar posibles
    // fugas de memoria
    timer.cancel();
  }
}

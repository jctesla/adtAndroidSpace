/*
   En Android, puedes utilizar la clase CountDownTimer para implementar un temporizador que ejecute una tarea cada segundo.
   Aquí hay un ejemplo básico:
   Este ejemplo utiliza CountDownTimer para ejecutar onTick cada segundo. Dentro de onTick, puedes realizar cualquier acción
   que desees ejecutar cada segundo. En este caso, se actualiza un TextView con la cuenta regresiva. Asegúrate de adaptar el
   código según tus necesidades específicas.
 */

import android.os.CountDownTimer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final TextView timerTextView = findViewById(R.id.timerTextView);

    CountDownTimer countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {

      // Este método se llama cada segundo
      public void onTick(long millisUntilFinished) {
        timerTextView.setText("Segundos restantes: " + millisUntilFinished / 1000);
      }

      // Este método se llama cuando el temporizador llega a su fin
      public void onFinish() {
        // Aquí puedes realizar acciones cuando el temporizador llega a su fin
      }
    };

    countDownTimer.start(); // Inicia el temporizador
  }

}

  /* NOTAS */

public MyCountDownTimer(TextView timerTextView) {
  super(60000, 1000);                                         // Duración total de 60 segundos y un intervalo de 1 segundo
  this.timerTextView = timerTextView;
}

public MyCountDownTimer(TextView timerTextView) {
  super(Long.MAX_VALUE, 2000);                                  // Duración total indefinida y un intervalo de 2 segundos
  this.timerTextView = timerTextView;
}


/*
 separar el metodo onTick() fuera de la declaracio de la Instancia de la clase CountDownTimer  ?
 Puedes separar el método onTick() en una clase aparte implementando la interfaz CountDownTimer 
 como una subclase o utilizando clases anónimas. Aquí tienes un ejemplo utilizando una subclase:
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

    MyCountDownTimer countDownTimer = new MyCountDownTimer(timerTextView);
    countDownTimer.start(); // Inicia el temporizador
  }

  private static class MyCountDownTimer extends CountDownTimer {
    private TextView timerTextView;

    public MyCountDownTimer(TextView timerTextView) {
      super(Long.MAX_VALUE, 1000);
      this.timerTextView = timerTextView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
      timerTextView.setText("Segundos restantes: " + millisUntilFinished / 1000);
      // Aquí puedes realizar acciones adicionales si es necesario
    }

    @Override
    public void onFinish() {
      // Aquí puedes realizar acciones cuando el temporizador llega a su fin
    }
  }
}

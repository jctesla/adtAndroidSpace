/*
 Si deseas tener un temporizador que se ejecute en intervalos de segundos sin una duración específica 
 (es decir, que se ejecute indefinidamente hasta que lo detengas), puedes ajustar la duración total del
  temporizador a un valor grande o usar CountDownTimer#start() sin un valor total.
 */

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private TextView timerTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    timerTextView = findViewById(R.id.timerTextView);

    MyCountDownTimer countDownTimer = new MyCountDownTimer(timerTextView);
    countDownTimer.start(); // Inicia el temporizador sin duración total
  }

  private static class MyCountDownTimer extends CountDownTimer {
    private TextView timerTextView;

    public MyCountDownTimer(TextView timerTextView) {
      super(Long.MAX_VALUE, 1000); // Duración total indefinida y un intervalo de 1 segundo
      this.timerTextView = timerTextView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
      timerTextView.setText("Segundos transcurridos: " + (millisUntilFinished / 1000));
      // Aquí puedes realizar acciones adicionales si es necesario
    }

    @Override
    public void onFinish() {
      // Este método se llama cuando el temporizador llega a su fin (no aplicable en
      // este caso)
    }
  }
}

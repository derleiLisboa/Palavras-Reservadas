package br.edu.unidavi.palavrasreservadas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.io.InputStream;
import java.util.Random;
import java.util.Timer;

public class JogoDasPalavras extends AppCompatActivity implements Jogo {

    private ListView listViewPalavras;
    private EditText editTextPalavra;
    private TextView textViewDescobertas;
    private TextView textViewAjudasRestantes;
    private Button buttonOk;
    private Chronometer cronometro;

    private ArrayAdapter<Palavra> adapter;
    private Palavra[] palavras;

    private int descobertas;
    private int ajudasRestantes;

    private boolean ganhou = false;
    private boolean finalizado = false;

    /** Temporizadores*/
    private CountDownTimer countDownTimerJogo;
    private CountDownTimer countDownTimerPrimeiraAjuda;
    private CountDownTimer countDownTimerSegundaAjuda;
    private CountDownTimer countDownTimerTerceiraAjuda;
    private CountDownTimer countDownTimerQuartaAjuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jogo_das_palavras);

        listViewPalavras = (ListView) findViewById(R.id.listViewPalavras);
        editTextPalavra = (EditText) findViewById(R.id.editTextPalavra);
        cronometro = (Chronometer) findViewById(R.id.chronometer1);
        textViewDescobertas = (TextView) findViewById(R.id.textViewDescobertas);
        textViewAjudasRestantes = (TextView) findViewById(R.id.textViewAjudasRestantes);
        buttonOk = (Button) findViewById(R.id.buttonOk);

        jogar();
    }

    @Override
    public void jogar() {
        long pauseDuration = SystemClock.elapsedRealtime() - SystemClock.elapsedRealtime();
        cronometro.setBase(cronometro.getBase()+pauseDuration);
        cronometro.start();

        palavras = new Storage().getPalavrasReservadas();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, palavras);

        listViewPalavras.setAdapter(adapter);

        inicializarTemporizadores();

        descobertas = 0;
        ajudasRestantes = 4;
    }

    private void inicializarTemporizadores() {
        countDownTimerJogo = new CountDownTimer(300000,1000) {
            AlertDialog alerta;
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cronometro.stop();
                        textViewAjudasRestantes.setEnabled(false);
                        buttonOk.setEnabled(false);
                        AlertDialog.Builder builder = new AlertDialog.Builder(JogoDasPalavras.this);

                        builder.setTitle("Mensagem do Jogo");

                        if (!ganhou()){

                            builder.setPositiveButton("Ok, Sair", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish();
                                }
                            });

                            builder.setMessage("Você perdeu, mais sorte na próxima!");
                            alerta = builder.create();
                            alerta.show();
                        }
                    }
                });
            }
        }.start();

        countDownTimerPrimeiraAjuda = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                revelarPrimeiraLetraCincoPalavras();
                ajudasRestantes--;
                updateAjudas();
            }
        }.start();

        countDownTimerSegundaAjuda = new CountDownTimer(120000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                revelarPrimeiraLetraCincoPalavras();
                ajudasRestantes--;
                updateAjudas();
            }
        }.start();

        countDownTimerTerceiraAjuda = new CountDownTimer(180000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                revelarDuasPrimeirasLetrasCincoPalavras();
                ajudasRestantes--;
                updateAjudas();
            }
        }.start();

        countDownTimerQuartaAjuda = new CountDownTimer(240000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                revelarCincoPalavras();
                ajudasRestantes--;
                updateAjudas();
            }
        }.start();
    }

    private void revelarPrimeiraLetraCincoPalavras() {
        Random randomGenerator = new Random();
        int reveladas = 0;
        while (reveladas < 5){
            int index = randomGenerator.nextInt(50);
            if ((palavras[index]).exibirPrimeiraLetra())
                reveladas ++;
        }
        adapter.notifyDataSetChanged();
    }

    private void revelarDuasPrimeirasLetrasCincoPalavras() {
        Random randomGenerator = new Random();
        int reveladas = 0;
        while (reveladas < 5){
            int index = randomGenerator.nextInt(50);
            if ((palavras[index]).exibirDuasPrimeirasLetras())
                reveladas ++;
        }
        adapter.notifyDataSetChanged();
    }

    private void revelarCincoPalavras() {
        Random randomGenerator = new Random();
        int reveladas = 0;
        while (reveladas < 5){
            int index = randomGenerator.nextInt(50);
            if ((palavras[index]).exibirPalavraCompleta()) {
                reveladas++;
                descobertas++;
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void verificarPalavra(View view) {
        AlertDialog alerta;

        String palavraInformada = editTextPalavra.getText().toString();

        for (Palavra p : palavras) {
            if (p.verificarPalavra(palavraInformada)) {
                descobertas ++;
                updatePlacar();
                break;
            }
        }
        adapter.notifyDataSetChanged();

        if (descobertas == 50) {
            cronometro.stop();
            textViewAjudasRestantes.setEnabled(false);
            buttonOk.setEnabled(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(JogoDasPalavras.this);

            builder.setTitle("Mensagem do Jogo");

            builder.setPositiveButton("Ok, Sair", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });

            builder.setMessage("Você ganhou, parabéns!");
            alerta = builder.create();
            alerta.show();
        }
    }

    private void updatePlacar() {
        textViewDescobertas.setText(String.valueOf(descobertas));
    }

    private void updateAjudas(){
        textViewAjudasRestantes.setText(String.valueOf(ajudasRestantes));
    }

    @Override
    public boolean finalizado() {
        return finalizado;
    }

    @Override
    public boolean ganhou() {
        return ganhou;
    }
}

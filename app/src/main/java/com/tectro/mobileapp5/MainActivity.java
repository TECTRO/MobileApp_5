package com.tectro.mobileapp5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tectro.mobileapp5.Adapters.GamePanelAdapter;
import com.tectro.mobileapp5.Models.GameModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GModel = GameModel.CreateInstance(9);
        GModel.setEndGameProvider(this::GameEnded);
        GModel.setScoreProvider(this::UpdateScore);
        GModel.setProgressProvider(this::UpdateProgressBar);
        RecyclerView GPanel = (RecyclerView) findViewById(R.id.GamePanel);
        GPanel.setLayoutManager(new GridLayoutManager(this, 3));
        GamePanelAdapter GPanelAdapter = new GamePanelAdapter(this, GModel);
        GPanel.setAdapter(GPanelAdapter);
        GModel.setUpdateStateProvider((r, s) -> GPanelAdapter.notifyItemChanged(r));
    }

    private GameModel GModel;

    public void StartGame(View view) {
        GModel.StartGame(10f, 0.5f, 2f, 3);
    }

    @SuppressLint("SetTextI18n")
    private void GameEnded(Integer score) {
        ((TextView) findViewById(R.id.GameInfo)).setText("Игра окончена! Ваш итоговый счет " + score);
    }

    @SuppressLint("SetTextI18n")
    private void UpdateScore(Integer score) {
        ((TextView) findViewById(R.id.GameInfo)).setText("Счет " + score);
    }

    private void UpdateProgressBar(Integer percentage) {
        ProgressBar p = ((ProgressBar) findViewById(R.id.ProgressBarInfo));
        if (p != null)
            p.setProgress(percentage);
    }
}
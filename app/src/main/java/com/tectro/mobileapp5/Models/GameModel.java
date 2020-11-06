package com.tectro.mobileapp5.Models;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GameModel {
    //region Singleton
    private static GameModel current;

    public static GameModel CreateInstance(int itemCount) {
        if (current == null) current = new GameModel(itemCount);
        return current;
    }

    public static GameModel GetInstance() {
        return current;
    }

    public static GameModel RebuildInstance(int itemCount) {
        current = new GameModel(itemCount);
        return current;
    }
    //endregion

    //region Constructor
    private GameModel(int itemCount) {
        Score = 0;
        rand = new Random();
        CellsContainer = new ArrayList<>();
        for (int i = 0; i < itemCount; i++)
            CellsContainer.add(new Cell(false, CellsContainer, null));
    }
    //endregion

    //region Accessors
    public void setEndGameProvider(Consumer<Integer> endGameProvider) {
        EndGameProvider = endGameProvider;
    }

    public void setUpdateStateProvider(BiConsumer<Integer, Boolean> UpdateStateProvider) {
        //  for (Cell cell : CellsContainer)
        //      cell.setUpdateStateProvider(UpdateStateProvider);
        this.UpdateStateProvider = UpdateStateProvider;
    }

    public void setScoreProvider(Consumer<Integer> scoreProvider) {
        ScoreProvider = scoreProvider;
    }

    public void setProgressProvider(Consumer<Integer> progressProvider) {
        ProgressProvider = progressProvider;
    }

    public Cell getCell(int position) {
        return CellsContainer.get(position);
    }

    public int getCellsContainerSize() {
        return CellsContainer.size();
    }
    //endregion

    private Random rand;

    private ArrayList<Cell> CellsContainer;
    private Integer Score;

    private BiConsumer<Integer, Boolean> UpdateStateProvider;
    private Consumer<Integer> ProgressProvider;
    private Consumer<Integer> ScoreProvider;
    private Consumer<Integer> EndGameProvider;
    private MyAsync GameThread = null;

    public void StartGame(float Seconds, float minOnTimeSec, float maxOnTimeSec, float maxDelaySec) {
        if (GameThread != null && !GameThread.isCancelled()) {
            GameThread.cancel(false);
            for (int i = 0; i < CellsContainer.size(); i++) {
                Cell cell = CellsContainer.get(i);
                cell.setPressed(false);
                UpdateStateProvider.accept(i, cell.getPressed());
            }

        }
        Score = 0;
        ScoreProvider.accept(Score);
        GameThread = new MyAsync();
        GameThread.execute(Seconds, minOnTimeSec, maxOnTimeSec, maxDelaySec);
    }

    class MyAsync extends AsyncTask<Float, Object, Void> {


        @Override
        protected Void doInBackground(Float... seconds) {
            long sec = (long) seconds[0].floatValue();
            float minOnTime = seconds[1];
            float maxOnTime = seconds[2];
            float maxDelay = seconds[3];
            while (sec > 0 && !isCancelled()) {
                publishProgress(sec, seconds[0]);
                ///////////////////////////////////////////
                float delay = rand.nextFloat() * maxDelay;
                SystemClock.sleep((long) (delay * 1000));
                sec -= delay;
                ///////////////////////////////////////////

                Cell chosen = CellsContainer.get(rand.nextInt(CellsContainer.size()));
                chosen.setPressed(true);
                this.publishProgress(chosen);
                float onTime = rand.nextFloat() * (maxOnTime - minOnTime) + minOnTime;
                SystemClock.sleep((long) (onTime * 1000));
                if (!isCancelled()) {
                    if (chosen.getPressed())
                        Score--;
                    else
                        Score++;
                    chosen.setPressed(false);
                    this.publishProgress(chosen);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);

            if(values[0] instanceof Cell) {
                Cell chosen = (Cell) values[0];

                if (UpdateStateProvider != null)
                    UpdateStateProvider.accept(CellsContainer.indexOf(chosen), chosen.getPressed());
            }

            if(values[0] instanceof Long)
            {
                long remaining = (Long) values[0];
                long total = (long) ((Float) values[1]).floatValue();
                if(ProgressProvider!=null)
                    ProgressProvider.accept((int) (remaining*100/total));
            }

            if (ScoreProvider != null)
                ScoreProvider.accept(Score);
        }

        @Override
        protected void onPostExecute(Void runnable) {
            super.onPostExecute(runnable);
            CallEndGameProvider(Score);
            if(ProgressProvider!=null)
                ProgressProvider.accept(0);
        }
    }

    //region Helpers
    private boolean CallEndGameProvider(Integer FinalScore) {
        if (EndGameProvider != null) {
            EndGameProvider.accept(FinalScore);
            return true;
        }
        return false;
    }
    //endregion
}

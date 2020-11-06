package com.tectro.mobileapp5.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tectro.mobileapp5.Models.Cell;
import com.tectro.mobileapp5.Models.GameModel;
import com.tectro.mobileapp5.R;

import java.util.List;
import java.util.function.Consumer;

public class GamePanelAdapter extends RecyclerView.Adapter<GamePanelAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private GameModel GModel;

    public GamePanelAdapter(Context context, GameModel GModel) {
        inflater = LayoutInflater.from(context);
        this.GModel = GModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.game_table_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cell current = GModel.getCell(position);
        if (current.getPressed())
            holder.ButtonView.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.colorPrimary, null));
        else
            holder.ButtonView.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.colorPanel, null));

        holder.ButtonView.setOnClickListener(v ->
        {
            int pos = position;
            if (GModel.getCell(pos).getPressed())
                Toast.makeText(inflater.getContext(), "Нажали!", Toast.LENGTH_SHORT).show();

            GModel.getCell(pos).setPressed(false);
            holder.isPressed = true;
            this.notifyItemChanged(pos);
        });

    }

    @Override
    public int getItemCount() {
        return GModel.getCellsContainerSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        boolean isPressed = false;
        final Button ButtonView;

        ViewHolder(View view) {
            super(view);
            ButtonView = view.findViewById(R.id.GameTableCell);
        }
    }
}

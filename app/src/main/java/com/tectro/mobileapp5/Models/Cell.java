package com.tectro.mobileapp5.Models;

import androidx.core.util.Consumer;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

public class Cell {
    //region Accessors
    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
        if (updateStateProvider != null)
            updateStateProvider.accept(parentCollection.indexOf(this), isPressed);
    }
    public boolean getPressed() { return isPressed; }

    public void setUpdateStateProvider(BiConsumer<Integer, Boolean> updateStateProvider) {
        this.updateStateProvider = updateStateProvider;
    }
    //endregion

    //region Constructor
    public Cell(boolean isPressed, List<Cell> parentCollection, BiConsumer<Integer, Boolean> UpdateStateProvider) {
        this.isPressed = isPressed;
        this.parentCollection = parentCollection;
        updateStateProvider = UpdateStateProvider;
    }
    //endregion

    private boolean isPressed;
    private List<Cell> parentCollection;
    private BiConsumer<Integer, Boolean> updateStateProvider;
}

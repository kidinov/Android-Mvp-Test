package org.kidinov.mvp_test.ui.generic;


import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class Binder<T> extends RecyclerView.ViewHolder {
    public Binder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T chatMessage);
}
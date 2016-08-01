package org.kidinov.mvp_test.ui.insta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.kidinov.mvp_test.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstaActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.retry_button)
    void onRetryClick() {

    }

    @OnClick(R.id.fab)
    void onFabClick() {

    }

}

package uk.co.ribot.androidboilerplate.ui.main;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.Ribot;

public class RibotsAdapter extends RecyclerView.Adapter<RibotsAdapter.RibotViewHolder> {
    private List<Ribot> ribots;

    @Inject
    RibotsAdapter() {
        ribots = new ArrayList<>();
    }

    public void setRibots(List<Ribot> ribots) {
        this.ribots = ribots;
    }

    @Override
    public RibotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ribot, parent, false);
        return new RibotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RibotViewHolder holder, int position) {
        Ribot ribot = ribots.get(position);
        holder.hexColorView.setBackgroundColor(Color.parseColor(ribot.getProfile().getHexColor()));
        holder.nameTextView.setText(String.format("%s %s",
                ribot.getProfile().getName().getFirst(), ribot.getProfile().getName().getLast()));
        holder.emailTextView.setText(ribot.getProfile().getEmail());
    }

    @Override
    public int getItemCount() {
        return ribots.size();
    }

    class RibotViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_hex_color)
        View hexColorView;
        @BindView(R.id.text_name)
        TextView nameTextView;
        @BindView(R.id.text_email)
        TextView emailTextView;

        RibotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

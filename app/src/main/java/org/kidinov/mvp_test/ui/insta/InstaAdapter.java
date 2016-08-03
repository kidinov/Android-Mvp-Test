package org.kidinov.mvp_test.ui.insta;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.kidinov.mvp_test.R;
import org.kidinov.mvp_test.data.model.InstaItem;
import org.kidinov.mvp_test.data.model.Location;
import org.kidinov.mvp_test.ui.generic.Binder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import timber.log.Timber;


public class InstaAdapter extends RecyclerView.Adapter<Binder<InstaItem>> {
    private static final String PROGRESS_ITEM_ID = "-1";
    public static DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);

    private static final int NORMAL_ITEM = 0;
    private static final int PROGRESS = 1;

    private SortedList<InstaItem> sortedList;

    @Inject
    InstaAdapter() {
        sortedList = new SortedList<>(InstaItem.class, new SortedListAdapterCallback<InstaItem>(this) {

            @Override
            public int compare(InstaItem o1, InstaItem o2) {
                return (-1) * o1.compareTo(o2);
            }

            @DebugLog
            @Override
            public boolean areContentsTheSame(InstaItem oldItem, InstaItem newItem) {
                Location oldLoca = oldItem.getLocation();
                Location newLoc = newItem.getLocation();
                if (oldLoca == null && newLoc != null || oldLoca != null && newLoc == null) {
                    return false;
                }
                return (oldLoca == newLoc ||
                        oldLoca.equals(newLoc)) &&
                        oldItem.getImages().equals(newItem.getImages());
            }

            @Override
            public boolean areItemsTheSame(InstaItem item1, InstaItem item2) {
                return item1.equals(item2);
            }
        });
    }

    @Override
    public Binder<InstaItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case NORMAL_ITEM:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_insta, parent, false);

                return new InstaViewHolder(itemView);
            case PROGRESS:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_progress, parent, false);
                return new ProgressViewHolder(itemView);
        }

        throw new RuntimeException(String.format("wrong view type - %d", viewType));
    }

    @Override
    public int getItemViewType(int position) {
        if (sortedList.get(position).getId().equals(PROGRESS_ITEM_ID)) {
            return PROGRESS;
        }
        return NORMAL_ITEM;
    }

    @Override
    public void onBindViewHolder(Binder<InstaItem> holder, int position) {
        holder.bind(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    void showLoading(boolean show) {
        Timber.d("showLoading show - %s", show);
        //Just for sample
        InstaItem item = new InstaItem();
        item.setId(PROGRESS_ITEM_ID);
        item.setCreatedTime(0L);
        if (show) {
            sortedList.add(item);
        } else {
            sortedList.remove(item);
        }
    }

    void addInstaItems(Collection<InstaItem> instaItems) {
        Timber.d("addInstaItems - %d", instaItems.size());
        sortedList.addAll(instaItems);
    }

    class InstaViewHolder extends Binder<InstaItem> {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.location)
        TextView location;
        @BindView(R.id.image)
        ImageView image;

        InstaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(InstaItem item) {
            date.setText(dateFormat.format(new Date(item.getCreatedTime() * 1000)));
            if (item.getLocation() != null) {
                location.setText(item.getLocation().getName());
            }
            Glide.with(image.getContext())
                    .load(item.getImages().getStandardResolution().getUrl())
                    .into(image);
        }
    }

    private class ProgressViewHolder extends Binder<InstaItem> {

        ProgressViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(InstaItem chatMessage) {
        }
    }
}

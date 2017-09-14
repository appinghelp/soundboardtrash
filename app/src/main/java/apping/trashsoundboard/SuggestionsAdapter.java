package apping.trashsoundboard;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.ListItemHolder> {
    private final List<String> suggestions = new ArrayList<>();

    SuggestionsAdapter() {
        setHasStableIds(true);
    }

    @Override
    public ListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListItemHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(ListItemHolder holder, int position) {
        if (holder.itemView instanceof TextView) {
            ((TextView) holder.itemView).setText(suggestions.get(position));
            ((TextView) holder.itemView).setTextColor(Color.BLACK);
        }
    }

    @Override
    public long getItemId(int position) {
        return suggestions.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    void setSuggestions(@NonNull List<String> newStrings) {
        final SuggestionDiff suggestionDiff = new SuggestionDiff(suggestions, newStrings);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(suggestionDiff, true);
        suggestions.clear();
        suggestions.addAll(newStrings);
        diffResult.dispatchUpdatesTo(this);
    }

    static class ListItemHolder extends RecyclerView.ViewHolder {
        ListItemHolder(View itemView) {
            super(itemView);
        }
    }

    private static class SuggestionDiff extends DiffUtil.Callback {

        private final List<String> newList;
        private final List<String> oldList;

        SuggestionDiff(@NonNull List<String> oldList, @NonNull List<String> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return caseInsensitiveComparison(oldItemPosition, newItemPosition);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return caseInsensitiveComparison(oldItemPosition, newItemPosition);
        }

        private boolean caseInsensitiveComparison(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equalsIgnoreCase(newList.get(newItemPosition));
        }
    }
}

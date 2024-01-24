package com.nacare.capture.data.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nacare.capture.R;
import com.nacare.capture.data.model.HomeData;

import java.util.List;

public class DashAdapter extends RecyclerView.Adapter<DashAdapter.ViewHolder> {

    private Context context;
    private List<HomeData> dataList;
    private ItemClickListener itemClickListener;

    public DashAdapter(Context context, List<HomeData> dataList, ItemClickListener itemClickListener) {
        this.context = context;
        this.dataList = dataList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeData homeData = dataList.get(position);
        holder.bind(homeData);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface ItemClickListener {
        void onItemClick(HomeData homeData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView idTextView;
        private TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.tv_title);
            nameTextView = itemView.findViewById(R.id.tv_description);
            itemView.setOnClickListener(this);
        }

        public void bind(HomeData homeData) {
            idTextView.setText(homeData.getId());
            nameTextView.setText(homeData.getName());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                HomeData clickedItem = dataList.get(position);
                itemClickListener.onItemClick(clickedItem);
            }
        }
    }
}
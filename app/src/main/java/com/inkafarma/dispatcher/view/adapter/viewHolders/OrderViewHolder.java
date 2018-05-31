package com.inkafarma.dispatcher.view.adapter.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkafarma.dispatcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textView_ordering)
    public TextView textViewOrdering;
    @BindView(R.id.btn_go_detail)
    public ImageView btnGoDetail;
    @BindView(R.id.textView_number_shelf)
    public TextView textViewNumberShelf;
    @BindView(R.id.textView_number_command)
    public TextView textViewNumberCommand;
    @BindView(R.id.ln_ordering)
    public LinearLayout lnOrdering;

    public OrderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

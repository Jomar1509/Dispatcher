package com.inkafarma.dispatcher.view.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inkafarma.dispatcher.R;
import com.inkafarma.dispatcher.data.model.OrderModel;
import com.inkafarma.dispatcher.data.model.PosModel;
import com.inkafarma.dispatcher.presenter.util.Util;
import com.inkafarma.dispatcher.view.adapter.viewHolders.OrderViewHolder;
import com.inkafarma.dispatcher.view.adapter.viewHolders.PosViewHolder;

import java.util.List;

import static com.inkafarma.dispatcher.presenter.util.Constants.CARD;
import static com.inkafarma.dispatcher.presenter.util.Constants.CARD_MASTER;
import static com.inkafarma.dispatcher.presenter.util.Constants.CARD_MASTER_ID;
import static com.inkafarma.dispatcher.presenter.util.Constants.CARD_VISA;
import static com.inkafarma.dispatcher.presenter.util.Constants.CARD_VISA_ID;
import static com.inkafarma.dispatcher.presenter.util.DispatcherException.logException;

public class OrderPosAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private List<OrderModel> orders;
    private List<PosModel> posList;
    private static final int ORDER = 0;
    private static final int POS = 1;

    public OrderPosAdapter(Context context, List<OrderModel> orders, List<PosModel> posList) {
        this.context = context;
        this.orders = orders;
        this.posList = posList;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            if (holder instanceof OrderViewHolder) {
                OrderModel orderModel = orders.get(position);
                OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
                orderViewHolder.textViewOrdering.setText(String.valueOf(position + 1));
                orderViewHolder.textViewNumberCommand.setText(Html.fromHtml(context.getString
                        (R.string.command, String.valueOf(orderModel.getExternalId()))));
                if (orderModel.isChecked()) {
                    orderViewHolder.lnOrdering.setBackgroundResource(R.drawable.shape_corner_holder_green);
                    orderViewHolder.textViewOrdering.setTextColor(context.getColor(R.color.white));
                    orderViewHolder.btnGoDetail.setVisibility(View.VISIBLE);
                } else {
                    orderViewHolder.lnOrdering.setBackgroundResource(R.drawable.shape_corner_holder_green_gray);
                    orderViewHolder.textViewOrdering.setTextColor(context.getColor(R.color.text_gray_order));
                    orderViewHolder.btnGoDetail.setVisibility(View.GONE);
                }
                orderViewHolder.textViewNumberShelf.setText(context.getString(R.string.shelf, orderModel.getShelf()));
            } else if (holder instanceof PosViewHolder) {
                PosModel posModel = posList.get(position - orders.size());
                PosViewHolder posViewHolder = (PosViewHolder) holder;
                if (CARD_VISA.equals(posModel.getProvider().toUpperCase()))
                    posViewHolder.textViewOrdering.setText(CARD_VISA_ID);
                else if (CARD_MASTER.equals(posModel.getProvider().toUpperCase()))
                    posViewHolder.textViewOrdering.setText(CARD_MASTER_ID);
                if (posModel.isChecked()) {
                    posViewHolder.lnOrdering.setBackgroundResource(R.drawable.shape_corner_holder_blue);
                    posViewHolder.btnGoDetail.setVisibility(View.VISIBLE);
                } else {
                    posViewHolder.lnOrdering.setBackgroundResource(R.drawable.shape_corner_holder_blue_gray);
                    posViewHolder.btnGoDetail.setVisibility(View.GONE);
                }
                posViewHolder.textViewNumberCommand.setText(CARD + posModel.getProvider());
            }

        } catch (Exception ex) {
            logException(ex, Util.class.getSimpleName(), "Exception caught");
        }
    }


    @Override
    public int getItemCount() {
        int nCount = 0;

        if (orders != null)
            nCount = orders.size();

        if (posList != null)
            nCount += posList.size();

        return nCount;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (position < orders.size())
                return ORDER;
            else
                return POS;

        } catch (Exception ex) {
            logException(ex, Util.class.getSimpleName(), "Exception caught");
        }
        return ORDER;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView;
            switch (viewType) {
                case ORDER:
                    itemView = inflater.inflate(R.layout.viewholder_orders, parent, false);
                    return new OrderViewHolder(itemView);
                case POS:
                    itemView = inflater.inflate(R.layout.viewholder_pos, parent, false);
                    return new PosViewHolder(itemView);
            }
        } catch (Exception ex) {
            logException(ex, Util.class.getSimpleName(), "Exception caught");
        }
        return null;
    }

}

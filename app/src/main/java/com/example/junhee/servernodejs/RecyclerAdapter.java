package com.example.junhee.servernodejs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JunHee on 2017. 7. 25..
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    private List<Bbs> bbsList = new ArrayList<>();
    private Context mContext;
    public String exId = "";

    public RecyclerAdapter(List<Bbs> bbsList, Context context){
        this.bbsList = bbsList;
        this.mContext = context;
    }

    public void setDate(List<Bbs> bbsList){
        this.bbsList = bbsList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Bbs bbs = bbsList.get(position);
        exId = bbs.getId();
        Log.e("RecyclerAdapter", "exId :: " + exId);
        holder.setTxtAuthor(bbs.getAuthor());
        holder.setTxtDate(bbs.getDate());
        holder.setTxtTitle(bbs.getTitle());
    }

    @Override
    public int getItemCount() {
        return bbsList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtDate;
        public TextView txtAuthor;

        public Holder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtAuthor = (TextView) itemView.findViewById(R.id.txt_author);
        }

        public void setTxtTitle(String txtTitle) {
            this.txtTitle.setText(txtTitle);
        }

        public void setTxtDate(String txtDate) {
            this.txtDate.setText(txtDate);
        }

        public void setTxtAuthor(String txtAuthor) {
            this.txtAuthor.setText(txtAuthor);
        }
    }
}

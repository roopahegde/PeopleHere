package com.foursquare.takehome;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;


public final class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.VisitorViewHolder> {

    //Data source list
    private List<Person> data;
    private SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
    private Context context;

    public PersonAdapter(List<Person> data, Context context){
        this.data = data;
        this.context = context;
    }

    public void updateVisitorList(List<Person> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class VisitorViewHolder extends  RecyclerView.ViewHolder{

        TextView visitorName;
        TextView visitorTimePeriod;

        public VisitorViewHolder(View itemView) {
            super(itemView);
            this.visitorName = itemView.findViewById(R.id.visitorName);
            this.visitorTimePeriod = itemView.findViewById(R.id.visitTimeperiod);
        }
    }

    @Override
    public VisitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =
                inflater.inflate(R.layout.visitor_row, parent, false);
        //Create new view
        VisitorViewHolder vh = new VisitorViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(VisitorViewHolder holder, int position) {
        //Reuse existing view
        if(data.get(position).getId() == -999){
           holder.visitorName.setText(R.string.no_visitors);
           holder.visitorName.setTextColor(ContextCompat.getColor(context, R.color.gray));
           holder.visitorTimePeriod.setTextColor(ContextCompat.getColor(context, R.color.gray));
        }else{
            holder.visitorName.setText(data.get(position).getName());
        }
        String arrivalTime =  formatter.format(data.get(position).getArriveTime());
        String departureTime =  formatter.format(data.get(position).getLeaveTime());
        holder.visitorTimePeriod.setText(
                context.getString(R.string.timestamp, arrivalTime, departureTime));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

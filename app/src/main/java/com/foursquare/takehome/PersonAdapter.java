package com.foursquare.takehome;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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

    public void updateVisitorList(Venue venue) {
        this.data = venue.getVisitors();
        Collections.sort(this.data);
        this.data = getVisitorsAndIdleTime(venue.getOpenTime(), venue.getCloseTime());
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

    /*
     find idle time slots
    */
    public List<Person> getVisitorsAndIdleTime(long openTime, long closeTime){
        int numberOfVisitors = 0;
        Person person;
        List<Person> dataWithIdle = new ArrayList<>();
        //Start with the venue open time as the earliest a visitor can leave the venue.
        long departureTime = openTime;
        while(numberOfVisitors < this.data.size()){

            person = this.data.get(numberOfVisitors);
            //If this visitor arrived before the previous visitor left the venue then
            // the venue is occupied at this time
            if(person.getArriveTime() <= departureTime){
                //If this person left the venue before the last visitor left the venue then
                // the venue is occupied even after this person left the venue until the
                // previous visitor departs
                if(person.getLeaveTime() > departureTime){
                    departureTime = person.getLeaveTime();
                }
            }else{
                //If this visitor did not arrive the before the previous visitor left the
                // venue then we have an idle time slot

                dataWithIdle.add(getNoVisitor(departureTime, person.getArriveTime()));
                departureTime =  person.getLeaveTime();
            }
            dataWithIdle.add(person);
            numberOfVisitors = numberOfVisitors + 1;
        }
        //If the last visitor left the venue before the venue close time.
        if(departureTime != closeTime){
            dataWithIdle.add(getNoVisitor(departureTime, closeTime));
        }
        return dataWithIdle;
    }

    private Person getNoVisitor(long arriveTime, long departureTime){
        Person noVisitor = new Person();
        noVisitor.setId(-999);
        noVisitor.setArriveTime(arriveTime);
        noVisitor.setLeaveTime(departureTime);
        return noVisitor;
    }
}

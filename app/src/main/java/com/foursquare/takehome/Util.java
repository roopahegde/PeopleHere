package com.foursquare.takehome;


import java.util.ArrayList;
import java.util.List;
/*
    -This class sorts the visitor list in ascending order based on the arrival time of the user and
     identifies the idle time slots of the venue.
    -Here heap sort is the foundation of sorting and the logic to find the idle time slots is
     incorporated as a part of sorting steps.
    -Sorting the visitor list seemed to be a necessary step for 2 reasons
        -- User can understand the data better when sorted
        -- Algorithm to find idle time slot from a sorted list is more efficient.
    -I could use java inbuilt sorting APIs, however I chose to implement it by myself because,
     using java API to sort would result in a time complexity of O(nlogn),
     plus finding idle time slot would require at least O(n). But, I could incorporate
     the logic to find the idle time slots into sorting algorithm avoiding
     a for loop of n iterations thus arriving at a time complexity of O(nlogn)
 */
public class Util {
    //List of visitors at the venue
    List<Person> visitors;

    public Util( List<Person> visitors){
        this.visitors = visitors;
    }

    /*
    Sort the visitors list and find idle time slots
     */
    public List<Person> getVisitorsAndIdleTime(long openTime, long closeTime){
        int numberOfVisitors = visitors.size();
        Person person;
        //As the adapter understands the person object a dummy person object is used to
        //represent a no visitor time slot
        Person dummyPerson;
        List<Person> data = new ArrayList<>();
        //Start with the venue open time as the earliest a visitor can leave the venue.
        long departureTime = openTime;
        while(numberOfVisitors > 0){
            getNextVisitor(numberOfVisitors);
            person = visitors.get(0);
            //If this visitor arrived before the previous visitor left the venue then
            // the venue is occupied at this time
            if(person.getArriveTime() <= departureTime){
                //If this person left the venue before the last visitor left the venue then
                // the venue is occupied even after this person left the venue until the
                // pervious visitor departs
                if(person.getLeaveTime() > departureTime){
                    departureTime = person.getLeaveTime();
                }
            }else{
                //If this visitor did not arrive the before the previous visitor left the
                // venue then we have an idle time slot
                dummyPerson = new Person();
                dummyPerson.setId(-999);
                dummyPerson.setArriveTime(departureTime);
                dummyPerson.setLeaveTime(person.getArriveTime());
                data.add(dummyPerson);
                departureTime =  person.getLeaveTime();
            }
            data.add(person);
            numberOfVisitors = numberOfVisitors - 1;
            visitors.set(0, visitors.get(numberOfVisitors));
        }
        //If the last visitor left the venue before the venue close time.
        if(departureTime != closeTime){
            dummyPerson = new Person();
            dummyPerson.setId(-999);
            dummyPerson.setArriveTime(departureTime);
            dummyPerson.setLeaveTime(closeTime);
            data.add(dummyPerson);
        }
        return data;
    }

    //Constructing a min heap to find the next arrived visitor.
    private void getNextVisitor(int numberOfVisitors){
        int mid = numberOfVisitors / 2;
        while(mid >= 0){
            heapify(mid, numberOfVisitors);
            mid--;
        }
    }

    private void heapify(int parentIndex, int len){
        int index = parentIndex;
        int leftChild = 2*index+1;
        int rightChild = leftChild + 1;
        if(leftChild < len &&
                visitors.get(leftChild).getArriveTime() < visitors.get(index).getArriveTime()){
            index = leftChild;
        }
        if(rightChild < len &&
                visitors.get(rightChild).getArriveTime() < visitors.get(index).getArriveTime()){
            index = rightChild;
        }
        if(index == parentIndex) return;
        swap(parentIndex, index);
    }
    private void swap(int parentIndex , int index){
        Person temp = visitors.get(parentIndex);
        visitors.set(parentIndex,visitors.get(index));
        visitors.set(index,temp);
    }

}

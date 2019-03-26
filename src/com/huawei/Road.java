package com.huawei;

import java.util.ArrayList;

class Road{
    private String roadID = "";
    private int length = -1;
    private int limitSpeed = -1;
    private int laneNum = -1;
    private String startID = "-1";
    private String endID = "-1";
    private int isDual = 0;

    private boolean isFree ;

    private ArrayList<Lane> lanes = new ArrayList<>(); //车道编号和我们的比赛要求相同

    Road(){

    }

    Road(String ri, int l, int ls, int ln, String si, String ei, int isd){
        roadID = ri;
        length = l;
        limitSpeed = ls;
        laneNum = ln;
        startID = si;
        endID = ei;
        isDual = isd;


        for(int i = 0; i< laneNum; i++)
            lanes.add(new Lane(length));

    }

    public String toString() {
        return roadID + "," + length + "," + limitSpeed + "," + laneNum + "," + startID + "," + endID + "," + isDual;
    }

    public String getRoadID() {
        return roadID;
    }

    public void setRoadID(String roadID) {
        this.roadID = roadID;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLimitSpeed() {
        return limitSpeed;
    }

    public void setLimitSpeed(int limitSpeed) {
        this.limitSpeed = limitSpeed;
    }

    public int getLaneNum() {
        return laneNum;
    }

    public void setLaneNum(int laneNum) {
        this.laneNum = laneNum;
    }

    public String getStartID() {
        return startID;
    }

    public void setStartID(String startID) {
        this.startID = startID;
    }

    public String getEndID() {
        return endID;
    }

    public void setEndID(String endID) {
        this.endID = endID;
    }

    public int getIsDual() {
        return isDual;
    }

    public void setIsDual(int isDual) {
        this.isDual = isDual;
    }

    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(ArrayList<Lane> lanes) {
        this.lanes = lanes;
    }

    public boolean isFree() {
        Lane lane;
        for(int i = 0; i< laneNum;i++ )
        {
            lane = this.getLanes().get(i);
            if(lane.isFree())
                return true;
        }
        return false;
    }
}
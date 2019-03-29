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

    private boolean isUseable = true; //当前道路是否可用

    private int GAMEOVER = 50; //拥塞临界点的基础值
    private int jamValue = 0; //交通拥塞值
    private int jamValueParam = 1; //交通拥塞值的系数
    private int overValue = 200;

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

        overValue = GAMEOVER * laneNum; //车道越多，越能容纳车子

        for(int i = 0; i< laneNum; i++)
            lanes.add(new Lane(length));

    }

    public boolean isUseable() {
//        if(roadID.equals("5004")&&jamValue==1)
//            return false;
//        if(roadID.equals("5046")||roadID.equals("5053")||roadID.equals("5045")||roadID.equals("5039"))
//            if(jamValue*jamValueParam > overValue/2)
//                return false;

        if(jamValue*jamValueParam > overValue)
            return false;
        return true;
    }



    public void setUseable(boolean useable) {
        isUseable = useable;
    }

    public int getOverValue() {
        return overValue;
    }

    public void setOverValue(int overValue) {
        this.overValue = overValue;
    }

    public int getJamValue() {
        return jamValue;
    }

    //这里传进来的参数是变动值，+1，或者-1
    public void updateJamValue(int alter) {
        this.jamValue = this.jamValue + alter*jamValueParam;
    }

    public int getJamValueParam() {
        return jamValueParam;
    }

    public void setJamValueParam(int jamValueParam) {
        this.jamValueParam = jamValueParam;
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
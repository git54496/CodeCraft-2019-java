package com.huawei;

import java.util.ArrayList;


class Cross{
    private String crossID = "";
    private String north = "-1";
    private String east = "-1";
    private String south = "-1";
    private String west = "-1";

    //与该Cross通过road可到达的的Cross都放在这里
	//注意必须是可达的neighborCross
	//注意这里存的只是ID
    private ArrayList<Integer> neighborCrossIds = new ArrayList<>();

    // 默认构造函数
    Cross(){

    }

    Cross(String id, String n, String e, String s, String w){
        crossID = id;
        north = n;
        east = e;
        south = s;
        west = w;
    }

    public String getCrossID() {
        return crossID;
    }

    public void setCrossID(String crossID) {
        this.crossID = crossID;
    }

    public String getNorth() {
        return north;
    }

    public void setNorth(String north) {
        this.north = north;
    }

    public String getEast() {
        return east;
    }

    public void setEast(String east) {
        this.east = east;
    }

    public String getSouth() {
        return south;
    }

    public void setSouth(String south) {
        this.south = south;
    }

    public String getWest() {
        return west;
    }

    public void setWest(String west) {
        this.west = west;
    }

    public void addNeighborCrossId(int neighborCrossId){
        neighborCrossIds.add(neighborCrossId);
    }

    public ArrayList<Integer> getNeighborCrossIds() {
        return neighborCrossIds;
    }

    public String toString() {
        return crossID + "," + north + "," + east + "," + south + "," + west;
    }

//    public void calcF() {
//        if(this.G == Integer.MAX_VALUE || this.H == Integer.MAX_VALUE)
//        {
//            this.F = Integer.MAX_VALUE;
//            return;
//        }
//        this.F = this.G + this.H;
//    }
}
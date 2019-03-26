package com.huawei;

import java.util.ArrayList;

class Car implements Comparable<Car>{
    private String carID = "";
    private int startPlace = -1;
    private int destination = -1;
    private int maxSpeed = -1;

    private int curState = -1;  //当前车辆状态，-1表示还没出发，0表示在某条道路上，1表示等待过路口

    private int curRoad = -1 ;  //当前所在road的编号
    private int curLane = -1 ;  //在当前道路上的哪条车道上

    private int inStartTime = -1; //car.txt文件中获取到的的汽车出发时间

    private int outStartTime = -1; //输出在answer.txt文件中的汽车出发时间

    private int dispatchTime = -1; //按照A*算法在一条空无人烟的车道上从起点到终点的时间

    private ArrayList<String> path;
    private ArrayList<String> roads;


    Car(){

    }

    public ArrayList<String> getRoads() {
        return roads;
    }

    public void setRoads(ArrayList<String> roads) {
        this.roads = roads;
    }

    public ArrayList<String> getPath() {
        return path;
    }

    Car(String ci, String sp, String d, int ms, int st){
        carID = ci;
        startPlace = Integer.parseInt(sp);
        destination = Integer.parseInt(d);
        maxSpeed = ms;
        inStartTime = st;
        outStartTime = inStartTime;
        path = null;
        roads = null;
    }

    public int getInStartTime() {
        return inStartTime;
    }

    public void setInStartTime(int inStartTime) {
        this.inStartTime = inStartTime;
    }

    public int getCurState() {
        return curState;
    }

    public void setCurState(int curState) {
        this.curState = curState;
    }

    public int getOutStartTime() {
        return outStartTime;
    }

    public void setOutStartTime(int outStartTime) {
        this.outStartTime = outStartTime;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public int getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(int startPlace) {
        this.startPlace = startPlace;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getCurRoad() {
        return curRoad;
    }

    public void setCurRoad(int curRoad) {
        this.curRoad = curRoad;
    }

    public int getCurLane() {
        return curLane;
    }

    public void setCurLane(int curLane) {
        this.curLane = curLane;
    }

    public String toString() {
        return carID + "," + startPlace + "," + destination + "," + maxSpeed + "," + inStartTime;
    }

    //用A*算法计算，并计算耗时
    public void dispatch(){
        AStar aStar = new AStar();
        path = aStar.dispatch(this);
        calDispatchTime();
    }

    private void calDispatchTime(){
        dispatchTime = 0;
        for(int i= 0;i<path.size()-1;i++){
            dispatchTime += OtherUtils.getDispatchTime(path.get(i),path.get(i+1),this);
        }
    }

    public int getDispatchTime() {
        return dispatchTime;
    }

    //A.compareTo(B)代表 比较者A 和 被比较者B 之间在比较
    //A>B,则返回正整数
    //A=B,返回0
    //A<B,返回负整数
    //所以我们这里可以操作
    @Override
    public int compareTo(Car o) {
        if(this.inStartTime > o.getInStartTime())
            return 1;
        else if(this.inStartTime == o.getInStartTime())
            return 0;
        else
            return -1;
    }
}

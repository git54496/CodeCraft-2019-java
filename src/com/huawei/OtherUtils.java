package com.huawei;

import java.io.*;
import java.util.*;

public class OtherUtils {

    public static HashMap<Integer, Integer> idToIndex = new HashMap<>();  //hashmap的key要唯一，则key:CrossID,value:Index
    public static HashMap<Integer, Integer> indexToId = new HashMap<>();  //hashmap的key要唯一，则key:Index,value:CrossID

    public static int index = 0 ;

    public static Iterator<Cross> getCrossIterator(){
        Iterator iterator = InputData.crossMap.entrySet().iterator();
        return iterator;
    }

    public static int crossIdToIndex(int crossID) {
        //当前crossID没有被加入
        if(idToIndex.get(crossID) == null){
            idToIndex.put(crossID,++index);
            indexToId.put(index,crossID);
            return index;
        }
        else
            return idToIndex.get(crossID);

    }


    public static int crossIdToIndex(String crossID) {
        int crossIdInt = Integer.parseInt(crossID);

        if(idToIndex.get(crossIdInt) == null){
            idToIndex.put(crossIdInt,++index);
            indexToId.put(index,crossIdInt);
            return index;
        }
        else
            return idToIndex.get(crossIdInt);

    }



    public static int indexToCrossId(int index) {
        return indexToId.get(index);
    }


    //这里对InputData中的CarMap中存的数据用inStartTime从小到大进行排序
    public static List<Map.Entry<Integer, Car>> getCarListOrderByStartTime(){
        List<Map.Entry<Integer, Car>> hashList = new ArrayList<Map.Entry<Integer, Car>>(InputData.carMap.entrySet());
        Collections.sort(hashList, new Comparator<Map.Entry<Integer, Car>>() {
            @Override
            public int compare(Map.Entry<Integer, Car> o1, Map.Entry<Integer, Car> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        return hashList;


        // 示例输出方式参照下面
//        for (Map.Entry<Integer, Car> m : hashList) {
//            System.out.println("Key=" + m.getKey() + ", carID=" + m.getValue().getCarID()+ ", inStartTime=" + m.getValue().getInStartTime());
//        }
    }

    //输入两个crossID,得到一条连接这两个Cross的RoadID
    public static String getLinkRoadID(String startCrossID , String endCrossID)
    {
        int startCrossIdInt = Integer.parseInt(startCrossID);
        int endCrossIdInt = Integer.parseInt(endCrossID);

        Road road;

        Cross startCross = InputData.crossMap.get(startCrossIdInt);
        Cross endCross = InputData.crossMap.get(endCrossIdInt);

        String roadID = startCross.getNorth();
        int roadIDInt = Integer.parseInt(roadID);


        if(!roadID.equals("-1"))
        {
            road = InputData.roadMap.get(roadIDInt);
            if(road.getIsDual()==1){
                if(road.getEndID().equals(endCrossID)||road.getStartID().equals(endCrossID))
                    return roadID;
            }
            else
                if(road.getEndID().equals(endCrossID))
                    return roadID;
        }

        roadID = startCross.getEast();
        roadIDInt = Integer.parseInt(roadID);

        if(!roadID.equals("-1"))
        {
            road = InputData.roadMap.get(roadIDInt);
            if(road.getIsDual()==1){
                if(road.getEndID().equals(endCrossID)||road.getStartID().equals(endCrossID))
                    return roadID;
            }
            else
            if(road.getEndID().equals(endCrossID))
                return roadID;
        }
        roadID = startCross.getSouth();
        roadIDInt = Integer.parseInt(roadID);

        if(!roadID.equals("-1"))
        {
            road = InputData.roadMap.get(roadIDInt);
            if(road.getIsDual()==1){
                if(road.getEndID().equals(endCrossID)||road.getStartID().equals(endCrossID))
                    return roadID;
            }
            else
            if(road.getEndID().equals(endCrossID))
                return roadID;
        }

        roadID = startCross.getWest();
        roadIDInt = Integer.parseInt(roadID);

        if(!roadID.equals("-1"))
        {
            road = InputData.roadMap.get(roadIDInt);
            if(road.getIsDual()==1){
                if(road.getEndID().equals(endCrossID)||road.getStartID().equals(endCrossID))
                    return roadID;
            }
            else
            if(road.getEndID().equals(endCrossID))
                return roadID;
        }


        return "0";

    }



    //判断一条Road是否还有空间（后部）容纳车辆,返回第几条车道上的第几个位置之前都有空间
    public static int[] getFreeLane(String roadID)
    {
        Road curRoad = InputData.roadMap.get(roadID);
        Lane curLane ;

        //下标0：第几条车道，下标1：该车道上的第几个位置
        int[] returnInfo = new int[2];
        returnInfo[0]=-1;
        returnInfo[1]=-1;
        Lane lane;

        for(int i = 0; i<curRoad.getLaneNum();i++)
        {
            if(getFreeLane(curRoad,i)){
                returnInfo[0] = i;
                returnInfo[1] = getLaneIndexFree(curRoad,i);

                return returnInfo;
            }

        }

        return returnInfo;
    }


    //这里返回的是一条车道上最近的有空的那个index下标，如laneInfo[1]=-1,laneInfo[2]=1044,则返回1
    public static int getLaneIndexFree(Road curRoad,int laneIndex){
        Lane curLane = curRoad.getLanes().get(laneIndex);
        int[] laneInfo = curLane.getLaneInfo();

        for(int j = 1; j<= curRoad.getLength(); j++)
        {
            if(laneInfo[j] != -1)
                if(j==1)
                    return -1;
                else
                    return j-1;
        }

        return -1;
    }

    //判断一条Road上某一个车道上是否还有空间（后部）容纳车辆
    public static boolean getFreeLane(Road curRoad, int laneIndex)
    {
        Lane curLane = curRoad.getLanes().get(laneIndex);
        int[] laneInfo = curLane.getLaneInfo();
        //等于-1,说明有空间
        if(laneInfo[1] == -1)
            return true;
        else
            return false;
    }

    public static int getDispatchTime(String startCrossID,String endCrossID,Car curCar){
        int minFIndex = OtherUtils.crossIdToIndex(startCrossID);
        int updateNodeIndex = OtherUtils.crossIdToIndex(endCrossID);

        int real_speed = Graph.limit_speed[minFIndex][updateNodeIndex] > curCar.getMaxSpeed() ?
                curCar.getMaxSpeed() : Graph.limit_speed[minFIndex][updateNodeIndex];
        int real_time = (Graph.road_length[minFIndex][updateNodeIndex] + real_speed - 1) / real_speed;

        return real_time;
    }

//    //判断一条Road上某一个车道上是否还有空间（后部）容纳车辆
//    public static boolean getFreeLane(String roadID,int laneNum)
//    {
//        curLane = curRoad.getLanes().get(i);
//        int[] laneInfo = curLane.getLaneInfo();
//        //等于-1,说明有空间
//        if(laneInfo[0] == -1)
//    }


    public static void charOutStream(List<Map.Entry<Integer, Car>> carList,String[] args) throws Exception{
        // 1：利用File类找到要操作的对象
        File file = new File(args[3]);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        ArrayList<String> path =new ArrayList<>();

        //2：准备输出流
        Writer out = new FileWriter(file);

        for (Map.Entry<Integer, Car> m : carList) {
            Car car = m.getValue();
            path = car.getRoads();

            out.write("("+car.getCarID()+", "+car.getOutStartTime()+", ");

            for(int i = 0;i<path.size();i++)
            {
                if(i == path.size()-1)
                    out.write(path.get(i) +")");
                else
                    out.write(path.get(i) +", ");
            }

            out.write("\n");

        }


        out.close();

    }





}

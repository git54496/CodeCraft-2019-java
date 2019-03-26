package com.huawei;

import java.util.*;

import static java.lang.Integer.MAX_VALUE;

public class Main {
    public static void main(String[] args) {
        InputData.inputData(args);
        Graph.createGraph();

//        easyDispatch();
        try {
            easyDispatch2(args);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //简单分批调度，每次发100辆车，然后100辆都到终点之后，再发
    private static void easyDispatch1(String[] args) throws Exception {
        int BATCH = 450;  //每一批次505
        double reboot = 0.5;  //当同一批次50%的车到达之后，启动下一批次
        int batchNum = InputData.carMap.size()/BATCH ; //共需多少批次
        int t = 0;
        int tempOut = 0;
        int addUpTime = 0 ;//当前这一批次出发时间
        int maxTimeInCurBatch = 0; //当前这一个批次中耗时最多的车辆时间
        int waitCar = InputData.carMap.size(); //待出发车辆
        int reachCar = 0; //到达车辆

        //按出发时间从小到大排序
        List<Map.Entry<Integer, Car>> carList = OtherUtils.getCarListOrderByStartTime();
        addUpTime = carList.get(0).getValue().getInStartTime();


        //遍历所有车辆
        for(int i=0;i < InputData.carMap.size();i++){
            t++;
            Car curCar = carList.get(i).getValue();
            curCar.dispatch();


            if(addUpTime < curCar.getInStartTime())
            {
                addUpTime = curCar.getInStartTime();
            }
            curCar.setOutStartTime(addUpTime);

            if(maxTimeInCurBatch < curCar.getDispatchTime()){
                maxTimeInCurBatch = curCar.getDispatchTime();
                tempOut = curCar.getOutStartTime();
            }

            //进入下一批次
            if( t == BATCH )
            {
                t=0;
                if(addUpTime + maxTimeInCurBatch < tempOut + maxTimeInCurBatch )
                    addUpTime = addUpTime + maxTimeInCurBatch;
                else
                    addUpTime = tempOut + maxTimeInCurBatch;
                maxTimeInCurBatch = 0;
                tempOut = addUpTime ;
            }

        }


        OtherUtils.charOutStream(carList,args);

    }

    //简单分批调度，每次发100辆车，每当有一辆车到终点，就发新的一辆车
    private static void easyDispatch2(String[] args) throws Exception {
        int BATCH = 300;  //每一批次
        int t = 1;  //这里作为时间片，向上增

        //这边设置一个优先队列，里面存的int数字从小到大排列，存的是前面一个批次出发的车子的到达时间
        Queue<Integer> carArriveTimeQueue = new PriorityQueue<>();

        //按出发时间从小到大排序
        List<Map.Entry<Integer, Car>> carList = OtherUtils.getCarListOrderByStartTime();


        //先同一时刻发 BATCH 辆车
        for(int i=0 ; i < BATCH ; i++){
            Car curCar = carList.get(i).getValue();
            curCar.dispatch();

            if(curCar.getInStartTime() > t)
            {
                curCar.setOutStartTime(curCar.getInStartTime());
                t = curCar.getInStartTime();
            }
            else {
                curCar.setOutStartTime(t);
            }

            carArriveTimeQueue.add(curCar.getOutStartTime()+curCar.getDispatchTime());

        }


        for(int i = BATCH ; i < InputData.carMap.size() ; i++){

            //此时有车子已经到达终点且最近一辆车的规定出发时间小于当前时间，发新车
            if(t >= carArriveTimeQueue.element() && t >= carList.get(i).getValue().getInStartTime() )
            {
                carArriveTimeQueue.remove();

                Car curCar = carList.get(i).getValue();
                curCar.dispatch();
                if(curCar.getInStartTime() > t)
                {
                    curCar.setOutStartTime(curCar.getInStartTime());
                    t = curCar.getInStartTime();
                }
                else {
                    curCar.setOutStartTime(t);
                }

                carArriveTimeQueue.add(curCar.getOutStartTime()+curCar.getDispatchTime());
            }
            //此时没车可以走，t++
            else
            {
                t++;
                i--;  //进入到这一层的时候，当前i相当于被忽略了，所以i减去一个值
            }

        }

        OtherUtils.charOutStream(carList,args);

    }


//    private static void monitor() {
//        int t = 1;
//        int waitCar = InputData.carMap.size(); //待出发车辆
//        int reachCar = 0; //到达车辆
//
//        List<Map.Entry<Integer, Car>> carList = OtherUtils.getCarListOrderByStartTime();
//
//        while (waitCar != 0 || reachCar != InputData.carMap.size()) {
//
//            //遍历所有车辆,遍历的顺序是从inStartTime小的值开始
//            for (Map.Entry<Integer, Car> m : carList) {
//
//                Car curCar = m.getValue();
//                //接下来根据不同的车辆情况，做不同的处理
//                switch (curCar.getCurState()) {
//                    //还没出发
//                    case -1:
//                        //对当前还未出发的汽车做一次调度算法，确定走哪条路,返回途径的crossIDs
//                        ArrayList<String> paths = AStar.easyDispatch(curCar);
//                        String roadID = OtherUtils.getLinkRoadID(paths.get(0),paths.get(1));
//
//                        //接着看这条road是否还有空间容纳车辆
//                        if (InputData.roadMap.get(roadID).isFree())
//                            dispatchCar(curCar, paths.get(0));
//                        else
//
//                        break;
//                    //在某条道路上,需要判断,这个比较简单
//                    case 0:
//                        if ()
//                            break;
//                    //
//                    case 1:
//                        break;
//                }
//
//            }
//
//
//        }
//
//    }


    public static void printGraph() {
        for (int i = 1; i <= InputData.crossMap.size(); ++i)
            for (int j = 1; j <= InputData.crossMap.size(); ++j) {
                if (Graph.road_length[i][j] != MAX_VALUE) {
                    System.out.println(i + " " + j + ":" + Graph.road_length[i][j] + ", " + Graph.limit_speed[i][j]);
                }
            }
    }

}

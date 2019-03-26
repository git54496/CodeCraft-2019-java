package com.huawei;


//车道
public class Lane {
	//这个数组存在该车道上的CarID，如一条车道长度为3，上面都有车，可以表示为
	//laneInfo[1]=50001,laneInfo[2]=50002,laneInfo[3]=50003
	//注意该数组从1开始计数
    //车道内容从小到大依次填满汽车（即Car先进到index小的，再慢慢往index大的位置移动）
    private int[] laneInfo;  
    private int length;      //lane长度和所在的road相同
    private boolean isFree;  //该车道上是否还有空间存放车辆，这个判断是看laneInfo[1]是不是等于-1，等于-1，即代表可以
    private int freeIndex;   //下一车辆进入这个车道的话，所能到的最远的下标位置



    public Lane(int roadLength) {
        laneInfo =new int[roadLength+1];
        this.length = roadLength;

        for(int i = 1;i<= length;i++)
            laneInfo[i] = -1;

    }

    public int[] getLaneInfo() {
        return laneInfo;
    }

    public void setLaneInfo(int[] laneInfo) {
        this.laneInfo = laneInfo;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isFree() {
        return laneInfo[1]==-1 ? true : false ;

    }

    public int getFreeIndex() {
        int index = -1;
        for(int i = 1;i<= length;i++)
            if(laneInfo[i] != -1 )
            {
                if(i==1)
                    return -1;
                else
                    return i-1;
            }


        return index;
    }
}

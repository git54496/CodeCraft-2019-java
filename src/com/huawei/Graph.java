package com.huawei;

import java.util.Map;

import static java.lang.Integer.MAX_VALUE;


/*
 * 根据读入信息构建图
 */
public class Graph {
	public static int cross_num = -1;
	public static int[][] road_length;		// 两个路口间的道路距离
	public static int[][] limit_speed;		// 两个路口间的道路限速



	public static void createGraph() {
		cross_num = InputData.crossMap.size();
		road_length = new int[cross_num + 1][cross_num + 1];
		limit_speed = new int[cross_num + 1][cross_num + 1];


		// 初始化,第一个记录的路口下标从 1 开始
		for(int i = 1; i <= InputData.crossMap.size(); ++i)
			for(int j = 1; j <= InputData.crossMap.size(); ++j) {
				road_length[i][j] = Integer.MAX_VALUE ;
				limit_speed[i][j] = Integer.MAX_VALUE ;
			}

		// 现在准备用road_length来处理cross的neighbor，road_length[i][j] ！= MAX_VALUE的就说明从Cross i到Cross j存在一条
		// 通路，即Cross j是Cross i的neighbor，但是这里读取的cross.txt文件中的cross的id刚好是从1开始的，数组下标和cross一一对应，
		// 但是如果后续cross.txt中的CrossID不再从1开始，那么我们这里就需要预先做好转换工作。即ID-->index,后续可以通过idToIndex.getValue(index)得到该下标对应的CrossID
		// 赋值读取
		for(Map.Entry<Integer, Road> entry : InputData.roadMap.entrySet()) {
			Road r = entry.getValue();
			int i = OtherUtils.crossIdToIndex(r.getStartID());
//			System.out.println("i: "+i+" start_id:" + r.start_id);

			int j = OtherUtils.crossIdToIndex(r.getEndID());
//			System.out.println("j: "+j+" end_id:" + r.end_id);

			road_length[i][j] = r.getLength();
			limit_speed[i][j] = r.getLimitSpeed();


			if(r.getIsDual() == 1) {
				road_length[j][i] = r.getLength();
				limit_speed[j][i] = r.getLimitSpeed();
			}
		}

		initCrossNeighbor();



	}



	//在一个Cross对象中存了与这个Cross相连的所有CrossID
	public static void initCrossNeighbor(){
		for(int i = 1; i <= InputData.crossMap.size(); ++i)
			for(int j = 1; j <= InputData.crossMap.size(); ++j) {
				if(Graph.road_length[i][j] != MAX_VALUE && i!=j ) {
					Cross curCross = InputData.crossMap.get(OtherUtils.indexToCrossId(i));
					curCross.addNeighborCrossId(OtherUtils.indexToCrossId(j));
				}
			}
		}

}


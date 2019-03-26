package com.huawei;

import java.util.ArrayList;
import java.util.HashSet;

/*
 * 单源最短路径算法
 */
public class Dijkstra {
	/*
	 * 
	 */
	public static int cross_num = InputData.crossMap.size();
	public static int[] dis = new int[cross_num + 1];	// 当前车辆起始路口到其他路口的路径长度，以耗时表示

	public static void getPath(int crossID,int carSpeed) {

		int start_place_id = crossID;
		int start_place_index = OtherUtils.crossIdToIndex(start_place_id);   //转化为下标
		int tempIndex = -1;


		int real_speed;		// 车辆在道路上行驶的实际速度（可以作为Car类的一个属性）

		String[] path = new String[cross_num + 1];	// 最短路径的路口记录,path[i]表示从当前确定的start_place到index为i的路口的最佳路径
		int[] hasMarkedCrossIndex = new int[cross_num + 1];	// 已找到的最短路径的Cross的ID集合
		int markedCrossNum = 0;
		ArrayList<Integer> neighborIDs;


		// 对数据、路径记录进行初始化
		for(int i = 1; i <= cross_num; ++i) {

			hasMarkedCrossIndex[i]=0;

			real_speed = Graph.limit_speed[start_place_index][i] > carSpeed ? carSpeed : Graph.limit_speed[start_place_index][i];
			dis[i] = (Graph.road_length[start_place_index][i] + real_speed - 1) / real_speed;	// 上取整

			if(Graph.road_length[start_place_index][i] != Integer.MAX_VALUE) {
				path[i] = start_place_id + "," + OtherUtils.indexToCrossId(i);
			}
			else {
				path[i] = "无法到达";
			}

		}

		//这里上面生成的dis[i]和hasMarkedCrossIndex[]完全没有问题


		hasMarkedCrossIndex[start_place_index]=1;
		markedCrossNum++;

		while(markedCrossNum < cross_num-1) {

			int min = Integer.MAX_VALUE;
			
			// 寻找离start_place路口最短的那个路口的index
			for(int i = 1; i <= cross_num; ++i) {
				if(hasMarkedCrossIndex[i]==0 && dis[i] < min) {
					tempIndex = i;
					min = dis[i];
				}
			}

			//如果这里等于-1,代表当前这个cross没有出边，以这个Cross为
			if(tempIndex == -1)
				break;


			// 将找到的路口ID放入集合，然后用这个路口对其他不在hasMarkedCrossIDs中的路口进行松弛
			hasMarkedCrossIndex[tempIndex]=1;
			markedCrossNum++;
			//这边获取当前找到的路口的所有出边ID
			neighborIDs = InputData.crossMap.get(OtherUtils.indexToCrossId(tempIndex)).getNeighborCrossIds();


			for(int i = 0 ; i< neighborIDs.size();i++)
			{
				int curId = neighborIDs.get(i);
				int curIdIndex = OtherUtils.crossIdToIndex(curId);

				real_speed = Graph.limit_speed[tempIndex][curIdIndex] > carSpeed ? carSpeed : Graph.limit_speed[tempIndex][curIdIndex];
				int dk = (Graph.road_length[tempIndex][curIdIndex] + real_speed - 1) / real_speed;

				if(hasMarkedCrossIndex[curIdIndex]==0 && dis[curIdIndex] > dis[tempIndex] + dk) {	// 更新
					dis[curIdIndex] = dis[tempIndex] + dk;
					path[curIdIndex] = path[tempIndex] + "," + curId;
				}
			}


		}

	}


	public static int[] getDis(){
		return dis;
	}
}

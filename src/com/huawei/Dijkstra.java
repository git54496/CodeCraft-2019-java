package com.huawei;

import java.util.ArrayList;

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

			real_speed = getRealSpeed(start_place_index,i,carSpeed);
			dis[i] = getRealDis(start_place_index,i,carSpeed,real_speed);

			if(Graph.road_length[start_place_index][i] != Integer.MAX_VALUE && isRoadUseable(start_place_index,i)) {
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
			neighborIDs = getRealNeighbor(tempIndex);


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

	private static ArrayList<Integer> getRealNeighbor(int tempIndex) {
		ArrayList<Integer> oldNeighbors = InputData.crossMap.get(OtherUtils.indexToCrossId(tempIndex)).getNeighborCrossIds();

		ArrayList<Integer> realNeighbors = (ArrayList<Integer>) oldNeighbors.clone();
		Cross startCross = InputData.crossMap.get(OtherUtils.indexToCrossId(tempIndex));
		Cross endCross;


		for(int i= 0;i<realNeighbors.size();i++)
		{
			endCross = InputData.crossMap.get(realNeighbors.get(i));
//			if(i==3&&realNeighbors.size()==3)
//				System.out.printf("sas");

			if(OtherUtils.getLinkRoadID(startCross.getCrossID(),endCross.getCrossID()).equals("0"))
			{
				realNeighbors.remove(i);
				i--;
			}
		}

		return realNeighbors;
	}

	private static boolean isRoadUseable(int start_place_index, int i) {
		String startCrossID = String.valueOf(OtherUtils.indexToCrossId(start_place_index));
		String endCrossID = String.valueOf(OtherUtils.indexToCrossId(i));
		String roadID = OtherUtils.getLinkRoadID(startCrossID,endCrossID);

		int roadIDint = Integer.parseInt(roadID);


		if(roadID.equals("0"))
			return false;

		Road road = InputData.roadMap.get(roadIDint);

		if(road.isUseable())
			return true;
		else
			return false;
	}

	private static int getRealDis(int start_place_index,int i,int carSpeed,int real_speed) {
		if(real_speed!=0)
			return (Graph.road_length[start_place_index][i] + real_speed - 1) / real_speed;	// 上取整
		else
			return Integer.MAX_VALUE;
	}

	private static int getRealSpeed(int start_place_index,int i,int carSpeed) {
		String startCrossID = String.valueOf(OtherUtils.indexToCrossId(start_place_index));
		String endCrossID = String.valueOf(OtherUtils.indexToCrossId(i));
		String roadID = OtherUtils.getLinkRoadID(startCrossID,endCrossID);
		int roadIDint = Integer.parseInt(roadID);

		if(roadID.equals("0"))
			return 0;

		Road road = InputData.roadMap.get(roadIDint);

		if(road.isUseable())
			return Graph.limit_speed[start_place_index][i] > carSpeed ? carSpeed : Graph.limit_speed[start_place_index][i];
		else
			return 0;//速度为0，即这条路不可行
	}


	public static int[] getDis(){
		return dis;
	}
}

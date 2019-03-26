package com.huawei;

import java.util.*;

//调度算法工具类
public class AStar {


    private ArrayList<Node> openList = new ArrayList<Node>();
    private ArrayList<Node> closeList = new ArrayList<Node>();

    //用于保存所有Node
    private HashMap<Integer, Node> nodes = new HashMap<>();

    public  Car curCar;


    public AStar(){

    }

	//A*算法入口方法1，这里是车辆从起点出发的情况，curstate = -1
    public  ArrayList<String> dispatch(Car car) {
        //获取基本信息
        curCar = car;

        //定点:起点终点
        Node startNode = new Node(InputData.crossMap.get(curCar.getStartPlace()));
        Node endNode = new Node(InputData.crossMap.get(curCar.getDestination()));

        nodes.put(Integer.valueOf(startNode.getNodeID()),startNode);
        nodes.put(Integer.valueOf(endNode.getNodeID()),endNode);


        //尝试寻找最短路径,找不到，返回空值
        Node destination = findPath(startNode, endNode);
        if(destination == null)
            return null;

        //生成栈
        Stack<String> stack = new Stack<>();
        stack.push(destination.getNodeID());

        while (destination.getParent()!=null) {
            stack.push(destination.getParent().getNodeID()) ;
            destination = destination.getParent() ;
        }

        //下标从0开始存数据
        ArrayList<String> path = new ArrayList<>();

        while (!stack.empty()) {
                String temp = stack.pop();
                path.add(temp);
        }

        ArrayList<String> roads =  crossToRoad(path);
        curCar.setRoads(roads);

        return path;
    }

    private ArrayList<String> crossToRoad( ArrayList<String> path) {
        ArrayList<String> roads=new ArrayList<>();

        for(int i = 0; i < path.size()-1;i++)
        {
            roads.add(OtherUtils.getLinkRoadID(path.get(i),path.get(i+1)));
        }

        return roads;

    }


    //生成路径，通过给parent赋值
    private  Node findPath(Node startNode, Node destinationNode) {

        //先对起点进行操作
        // 把起点加入 openList
        openList.add(startNode);

        //openList中是待处理的节点，closedList中是不需要考虑的节点，就是已经确定，不用考虑的节点
        while (openList.size() > 0) {
            // 遍历 openList ，查找F值最小的节点，把它作为当前要处理的节点
            Node minFNode = findMinFNode();
            // 从openList中移除
            openList.remove(minFNode);
            // 把这个节点移到 closeList
            closeList.add(minFNode);

            //获取当前找到的最小的F值节点的邻居,等待一会儿遍历，更新他们的F值，并加入openList中
            ArrayList<Integer> neighbors = minFNode.getNeighborNodes();

            for (Integer neighborId : neighbors) {

                //往nodes中添加node节点
                if(!exist(nodes,neighborId))
                {
                    Node node = new Node(InputData.crossMap.get(neighborId));
                    nodes.put(Integer.valueOf(node.getNodeID()),node);
                }

                //注意在closeList中的元素不要再更新F值
                if(! exist( closeList , neighborId )){
                    //当前节点的邻居节点已经在openlist中
                    if (exist( openList , neighborId )) {
                        //经由当前最小F的节点，更新这个邻居节点的F值(其中只需要重算G，H不变)
                        updateNodes(minFNode, neighborId);
                    } else {
                        //这里的更新和上面的稍微不一样,这边是第一次计算邻居节点的G、H值
                        updateNodes(minFNode, destinationNode, neighborId);
                    }
                }
            }

            //如果在开放链表中找到终点，就可以退出了
            if (exist(openList, destinationNode.getNodeID())) {
                return nodes.get(Integer.parseInt(destinationNode.getNodeID()));
            }
        }

        //openList都循环完了，还没找到destination,说明找不到
        return null;

    }

    public Node findMinFNode() {
        Node curNode = openList.get(0);
        for (Node node : openList) {
            if (node.getF() < curNode.getF()) {
                curNode = node;
            }
        }
        return curNode;
    }

    //经由当前最小F的节点，更新这个邻居节点的F值(其中只需要重算G，H不变)
    private void updateNodes(Node minFNode, int neighborId) {
        Node updateNode = nodes.get(neighborId);

        //计算从出发点开始经过minFNode之后updateNode的F值
        int G = calcG(minFNode, updateNode);
        //途径当前节点tempStart到达该节点Cross的距离G更小时，更新F
        if (G < updateNode.getG()) {
            updateNode.setParent(minFNode);
            updateNode.setG(G);
            updateNode.calcF();
        }


    }

    //这里的更新和上面的稍微不一样,这边是第一次计算邻居节点的G、H值
    private void updateNodes(Node minFNode, Node destinationNode, int neighborID) {
        Node neighbor = nodes.get(neighborID);

        neighbor.setParent(minFNode);
        neighbor.setG(calcG(minFNode, neighbor));

        //因为这里calH比较复杂，所以我懒得改了，这里传进去使用node对应的Cross
        int destinationCrossID = Integer.parseInt(destinationNode.getNodeID());
        neighbor.setH(calcH(InputData.crossMap.get(neighborID), InputData.crossMap.get(destinationCrossID)));

        neighbor.calcF();

        openList.add(neighbor);
    }


    //这个即从起点出发，经过minFNode，算出updateNode的G值,以实际行驶时间作为G值
    private int calcG(Node minFNode, Node updateNode) {

        int minFIndex = OtherUtils.crossIdToIndex(minFNode.getNodeID());
        int updateNodeIndex = OtherUtils.crossIdToIndex(updateNode.getNodeID());

        int real_speed = Graph.limit_speed[minFIndex][updateNodeIndex] > curCar.getMaxSpeed() ?
                curCar.getMaxSpeed() : Graph.limit_speed[minFIndex][updateNodeIndex];
        int real_time = (Graph.road_length[minFIndex][updateNodeIndex] + real_speed - 1) / real_speed;

        return real_time + minFNode.getG();

    }


    //测试正确
    public int calcH(Cross start, Cross end) {
        Dijkstra.getPath(Integer.parseInt(start.getCrossID()), 1);
        int[] dis = Dijkstra.getDis();
        int step;
        if (dis.length != 0) {
            step = dis[OtherUtils.crossIdToIndex(Integer.parseInt(end.getCrossID()))];
        } else
            step = 0;

        if (step == Integer.MAX_VALUE)
            return Integer.MAX_VALUE;

        return step;
    }


    public static boolean exist(List<Node> maps, int nodeID) {

        String nodeIdStr = String.valueOf(nodeID);

        for (Node n : maps) {
            if (n.getNodeID().equals(nodeIdStr)) {
                return true;
            }
        }
        return false;
    }

    public static boolean exist(List<Node> maps, String nodeID) {
        for (Node n : maps) {
            if (n.getNodeID().equals(nodeID)) {
                return true;
            }
        }
        return false;
    }

    public static boolean exist(HashMap<Integer,Node> nodes, int nodeID) {

        return nodes.get(nodeID) == null ? false : true ;

    }

    public static boolean exist(HashMap<Integer,Node> nodes, String nodeID) {

        return nodes.get(Integer.parseInt(nodeID)) == null ? false : true ;

    }




}

package com.huawei;

import java.util.ArrayList;


//该类用于A*算法使用
public class Node {

    private String nodeID = ""; //这个和相对应的CrossID保持一致

    private int F = 0;
    private int G = 0;
    private int H = 0;

    private Node parent;

    //与该Node通过road相连的
    private ArrayList<Integer> neighborNodes = new ArrayList<>();


    public Node(Cross cross) {
        neighborNodes = cross.getNeighborCrossIds();
        nodeID = cross.getCrossID();

    }

    public void calcF() {
        if(G==Integer.MAX_VALUE || H ==Integer.MAX_VALUE)
        {
            this.F = Integer.MAX_VALUE;
            return;
        }

        this.F = this.G + this.H;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public int getF() {
        return F;
    }

    public void setF(int f) {
        F = f;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getH() {
        return H;
    }

    public void setH(int h) {
        H = h;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Integer> getNeighborNodes() {
        return neighborNodes;
    }

    public void setNeighborNodes(ArrayList<Integer> neighborNodes) {
        this.neighborNodes = neighborNodes;
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import myEdge.MyEdgeWeight;
import java.util.ArrayList;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author Nam Do
 */
public class MenuOption {
    private String URL;
    private ArrayList<String> arr = null;
    private int type;
    private String vertexSourceSelected, vertexDestinationSelected;
    private ArrayList<DefaultWeightedEdge> arrEdge;
    private ArrayList<MyEdgeWeight> myWeight;
    
    
    public MenuOption (){}
    
    
    public String getURL(){
        return URL;
    }
    public void setURL(String URL){
        this.URL = URL;
    }
    public ArrayList getArr(){
        return arr;
    }
    public void setArr(ArrayList arr){
        this.arr = arr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public String getVertexSourceSelected() {
        return vertexSourceSelected;
    }

    public void setVertexSourceSelected(String vertexSelected) {
        this.vertexSourceSelected = vertexSelected;
    }

    public String getVertexDestinationSelected() {
        return vertexDestinationSelected;
    }

    public void setVertexDestinationSelected(String vertexDestinationSelected) {
        this.vertexDestinationSelected = vertexDestinationSelected;
    }

    public ArrayList<DefaultWeightedEdge> getArrEdge() {
        return arrEdge;
    }

    public void setArrEdge(ArrayList<DefaultWeightedEdge> arrEdge) {
        this.arrEdge = arrEdge;
    }

    public ArrayList<MyEdgeWeight> getMyWeight() {
        return myWeight;
    }

    public void setMyWeight(ArrayList<MyEdgeWeight> myWeight) {
        this.myWeight = myWeight;
    }
    
}

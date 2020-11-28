/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
/**
 *
 * @author ntloc
 */
public class MyEdgeWeight extends  DefaultWeightedEdge{

    @Override
    public String toString() {
        return String.valueOf(getWeight());
    }
    
}

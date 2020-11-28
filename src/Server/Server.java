/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools @ Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author Nam Do
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.WeightedMultigraph;

public final class Server {

    public static int buffsize = 512;
    public static int port = 1234;
    public static DatagramSocket socket;
    public static DatagramPacket dpreceive, dpsend;
    ArrayList<String> Edges = new ArrayList<>();
    ArrayList<String> Vertexs = new ArrayList<>();
    public Server() {
        try {
            
            acceptConnectToClient();
            while (true) {
                String tmp = receiveFromClient();
                

                if (tmp.contains("#")) {

                    if (checkSyntaxGraph(tmp)) {
                        Edges = getArrEdge(tmp);

                        for (String E : Edges) {
                            if (checkSyntaxEdge(E)) {
                                String[] temp = E.split(";");
                                Vertexs.add(temp[0]);
                                Vertexs.add(temp[1]);
                            }
                        }
                        Vertexs = arrCompactV(Vertexs);
                        sendToClient("1<"+String.valueOf(Vertexs));
                    } else {
                        sendToClient("1<false");

                    }

                }
                if (tmp.contains(">")&&!tmp.endsWith(">")&&!tmp.startsWith(">")) {
                    //chua xong
                    // timf dduongwf ddi nganws nhaats
                    String[] temp = tmp.trim().split(">");

                    if ("0".equals(temp[2])) {
                        WeightedMultigraph dg = createUnDirectedGraph(String.valueOf(Vertexs), String.valueOf(Edges));
                        
                        DijkstraShortestPath dijkstraShortestPath1 = new DijkstraShortestPath(dg, temp[0], temp[1]);
                        ArrayList shortestPath1 = (ArrayList) dijkstraShortestPath1.getPathEdgeList();
                        String length = String.valueOf(dijkstraShortestPath1.getPathLength());
                        System.out.println(length);
                        sendToClient("2<"+String.valueOf(shortestPath1) + "@" + length);
                    }
                    if ("1".equals(temp[2])) {
                        DirectedWeightedMultigraph udg = createDirectedGraph(String.valueOf(Vertexs), String.valueOf(Edges));
                        DijkstraShortestPath dijkstraShortestPath2 = new DijkstraShortestPath(udg, temp[0], temp[1]);
                        ArrayList shortestPath2 = (ArrayList) dijkstraShortestPath2.getPathEdgeList();
                        String length = String.valueOf(dijkstraShortestPath2.getPathLength());
                        sendToClient("2<"+String.valueOf(shortestPath2) + "@" + length);
                    }

                }

                if (tmp.equals("bye")) {
                    closeConnect();
                    break;
                }

                //sendToClient(String.valueOf(checkSyntaxGraph(tmp)));
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();

    }

    public void acceptConnectToClient() throws SocketException {
        socket = new DatagramSocket(1234);
        dpreceive = new DatagramPacket(new byte[buffsize], buffsize);

    }

    public String receiveFromClient() throws IOException {
        socket.receive(dpreceive);
        String tmp = new String(dpreceive.getData(), 0, dpreceive.getLength());
        System.out.println("Server received: " + tmp + " from "
                + dpreceive.getAddress().getHostAddress() + " at port "
                + socket.getLocalPort());
        return tmp;
    }

    public void closeConnect() {
        System.out.println("Server socket closed");
        socket.close();

    }

    public void sendToClient(String tmp) throws IOException {
        dpsend = new DatagramPacket(tmp.getBytes(), tmp.getBytes().length,
                dpreceive.getAddress(), dpreceive.getPort());
        System.out.println("Server sent back " + tmp + " to client");
        socket.send(dpsend);
    }

    // lay Edge tu chuoi from client
    public ArrayList getArrEdge(String s) {
        ArrayList arrE = new ArrayList();

        String[] temp = s.split("#");
        for (String tempE : temp) {
            arrE.add(tempE);
        }

        return arrE;
    }

    // lay dinh tu chuoi from client
//    public ArrayList getArrVertex(String s){
//        ArrayList arrV = new ArrayList();
//        String[] temp = s.split("#");
//        for (String tmpEdge : temp) {
//            String[] tempVertex = tmpEdge.split(";");
//            arrV.add(tempVertex[0]);
//            arrV.add(tempVertex[1]);
//        }
//
//        arrV = arrCompactV(arrV);
//
//        return arrV;
//    }
    public ArrayList arrCompactV(ArrayList a) {
        ArrayList<String> arrTemp = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            if (!arrTemp.contains(a.get(i))) {
                arrTemp.add((String) a.get(i));
            }
        }
        return arrTemp;
    }

    public Boolean checkSyntaxEdge(String s) {

        char a = 59;
        if (countCharInString(s, a) == 2) {
            if (!s.startsWith(";") && !s.endsWith(";")) {
                if (!s.contains(";;")) {
                    return true;
                }
            }

        }

        return false;

    }

    public Boolean checkSyntaxGraph(String s) {

        if (!s.startsWith("#") && !s.endsWith("#")) {
            if (!s.contains("##")) {
                return true;
            }
        }

        return false;
    }

    public static int countCharInString(String s, char c) {
        int count = 0;
        char[] temp = s.toCharArray();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == c) {
                count++;
            }
        }
        return count;
    }

    //======================= function to chang String to ArrayList======================
    public ArrayList changeStringToArr(String s) {
        ArrayList<String> arr = new ArrayList<>();
        char a = 91;    //[
        char b = 93;    //]
        char c = 32;    // khoang trong
        String[] temp = s.replace(a, c).replace(b, c).trim().split(",");
        for (String tmp : temp) {
            arr.add(tmp.trim());
        }
        return arr;
    }

    //============================function tao direct graph===================
    public DirectedWeightedMultigraph createDirectedGraph(String V, String E) {
        ArrayList<String> Vertexs = new ArrayList<>();
        ArrayList<String> Edges = new ArrayList<>();

        Vertexs = changeStringToArr(V);
        Edges = changeStringToArr(E);

        DirectedWeightedMultigraph dg = new DirectedWeightedMultigraph(DefaultWeightedEdge.class);

        for (String v : Vertexs) {
            dg.addVertex(v);
        }
        DefaultWeightedEdge[] de = new DefaultWeightedEdge[Edges.size()];
        for (int i = 0; i < Edges.size(); i++) {
            String e = Edges.get(i);
            String[] temp = e.split(";");

            de[i] = (DefaultWeightedEdge) dg.addEdge(temp[0], temp[1]);
            dg.setEdgeWeight(de[i], Integer.valueOf(temp[2]));
        }
        return dg;
    }
//============================function tao undirect graph===================

    public WeightedMultigraph createUnDirectedGraph(String V, String E) {
        ArrayList<String> Vertexs = new ArrayList<>();
        ArrayList<String> Edges = new ArrayList<>();

        Vertexs = changeStringToArr(V);
        Edges = changeStringToArr(E);

        WeightedMultigraph udg = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (String v : Vertexs) {
            udg.addVertex(v);
        }
        DefaultWeightedEdge[] de = new DefaultWeightedEdge[Edges.size()];
        for (int i = 0; i < Edges.size(); i++) {
            String e = Edges.get(i);
            String[] temp = e.split(";");

            de[i] = (DefaultWeightedEdge) udg.addEdge(temp[0], temp[1]);
            udg.setEdgeWeight(de[i], Integer.valueOf(temp[2]));
        }
        return udg;
    }
}

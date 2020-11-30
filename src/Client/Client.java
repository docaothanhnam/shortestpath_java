/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools @ Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author Nam Do
 */
import static Client.myFrame.cbbBeginPoint;
import static Client.myFrame.pContent;
import static Client.myFrame.radUndirected;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import myEdge.MyEdgeWeight;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.WeightedMultigraph;

public final class Client extends myFrame {

    public static int destPort = 1234;
    public static String hostname = "localhost";
    public static DatagramSocket socket;
    public static DatagramPacket dpsend, dpreceive;
    public static InetAddress add;
    public static Scanner stdIn;
//    public FileChooser fc = new FileChooser();
//    public radGraphType gt = new radGraphType();
    public static ArrayList<String> arrVertex = new ArrayList();
    public static ArrayList<String> arrCompactVertex = new ArrayList();
    public JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    public JGraphXAdapter<String, MyEdgeWeight> x;
    MenuOption mo = new MenuOption();
    static AES_Encryption aes = new AES_Encryption();
    static RSA_Encryption rsa = new RSA_Encryption();
    String keyClient = taoKey();
//    public static String temp;
    public int RADIUD = 300;

    public Client(String title) throws FileNotFoundException {

        super(title);
        try {
            connectToServer();
            
            String line = "";
           

            while (true) {
                FileChooser fc = new FileChooser(); // send to server data
                btnChooseFile.addActionListener(fc);

//                    btnChooseFile.
                line = receiveFromServer();
                System.out.println("res1:" + line);
                System.out.println("res456: " + mo.getURL());
                String[] tmpLine = line.split("<");
//                System.out.println("---" + tmpLine);
                for (String s : tmpLine) {
                    System.out.println("--" + s);

                }

                if (tmpLine[0].equals("1")) {
                    if (!tmpLine[1].equals("false")) {
                        radGraphType gt = new radGraphType();
                        radDirected.addActionListener(gt);
                        radUndirected.addActionListener(gt);

                        buildGraph bg = new buildGraph(tmpLine[1], String.valueOf(mo.getArr()));
                        btnBuild.addActionListener(bg);

                        ArrayList<String> arrV = changeStringToArr(tmpLine[1]);

                        for (String v : arrV) {
                            cbbBeginPoint.addItem(v);
                            cbbEndPoint.addItem(v);
                        }
                        cbbBeginPoint.repaint();
                        cbbEndPoint.repaint();

                        cbbSourceVertex csv = new cbbSourceVertex();
                        cbbBeginPoint.addActionListener(csv);
                        cbbDestinationVertex cdv = new cbbDestinationVertex();
                        cbbEndPoint.addActionListener(cdv);

                    }

                }

                findShortestPath fsp = new findShortestPath();
                btnShortestPath.addActionListener(fsp);

//                if(cbbBeginPoint.is){
//                        
//                }
                if (tmpLine[0].equals("2")) {

                    if (tmpLine[1].contains("@")) {

                        txtLength.removeAll();

                        System.out.println("ád:" + tmpLine[1]);
                        String[] templine = tmpLine[1].split("@");

                        for (String s : templine) {
                            System.out.println("--" + s);
                        }
                        System.out.println("tyu:" + templine[0]);
                        System.out.println("qưe:" + changeStringToArr(templine[0]));
//                        System.out.println("333:"+jgxAdapter);

                        paintEdge(changeStringToArr(templine[0]), x);
                        txtLength.setText(templine[1]);
                        txtLength.repaint();
                    }

                }
                
                exportPNG ex = new exportPNG();
                btnExportPng.addActionListener(ex);

                if (line == "bye") {
                    closeConnectToServer();
                    break;
                }

//                }
            }

//            buildGraph bg = new buildGraph();
//            btnBuild.addActionListener(bg);
        } catch (IOException e) {
            System.err.println(e);
        }

    }
//================================Main====================================

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Client client = new Client("shortestpath");
    }
//=================================ActionListener for btnChooseFile ===================

    public class FileChooser implements ActionListener {

        private String url;
        ArrayList<String> arrE = new ArrayList<>();

        public FileChooser() {

        }

        @Override
        public void actionPerformed(ActionEvent e) {

            fd.setVisible(true);
            fileName = fd.getFile();
            lUrl.setText("File : " + fileName);
            char c = 92;
            char d = 47;

            url = fd.getDirectory().replace(c, d) + fd.getFile();

            mo.setURL(url);

            try {
                if (url != null) {
                    FileInputStream inputStream = null;
                    BufferedReader bufferedReader = null;

                    inputStream = new FileInputStream(url);
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    String tempLine = bufferedReader.readLine();
                    arrE.add(tempLine);
                    while ((line = bufferedReader.readLine()) != null) {
                        tempLine += ("#" + line);
                        arrE.add(line);
                    }
                    sendToServer(tempLine);
                    mo.setArr(arrE);
//                System.out.println(arrE);
                    bufferedReader.close();
                    inputStream.close();
                }
                // doc file va chuyen ve server

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            lUrl.repaint();
        }

    }
//=======================Actionlistener for cbb source & destination============================

    public class cbbSourceVertex implements ActionListener {
//        String vertex;

        public cbbSourceVertex() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mo.setVertexSourceSelected(String.valueOf(cbbBeginPoint.getSelectedItem()));
            System.out.println(String.valueOf(cbbBeginPoint.getSelectedItem()));
            System.out.println("reS" + mo.getVertexSourceSelected());

        }
    }

    public class cbbDestinationVertex implements ActionListener {
//        String vertex;

        public cbbDestinationVertex() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mo.setVertexDestinationSelected(String.valueOf(cbbEndPoint.getSelectedItem()));
            System.out.println(String.valueOf(cbbEndPoint.getSelectedItem()));
            System.out.println("reD" + mo.getVertexDestinationSelected());
        }
    }
//========================Actionlistener for btn shortest path================================

    public class findShortestPath implements ActionListener {

//        String re = "";
        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("sd:"+mo.getVertexSourceSelected()+">"+mo.getVertexDestinationSelected()+">"+mo.getType());
            try {
                if (mo.getVertexSourceSelected().equals(mo.getVertexDestinationSelected())) {

                } else {
                    sendToServer(mo.getVertexSourceSelected() + ">" + mo.getVertexDestinationSelected() + ">" + mo.getType());
                }

//                re = receiveFromServer();
//                System.out.println("ress2:"+re);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//========================Actionlistener for radio btn Graph type===============================
    // xong
    public class radGraphType implements ActionListener {

        int type1 = 0;
        int type2 = 1;

        public radGraphType() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (radUndirected.isSelected()) {
                mo.setType(type1);
                System.out.println("type:" + mo.getType());
            }
            if (radDirected.isSelected()) {
                mo.setType(type2);
                System.out.println("type:" + mo.getType());
            }
        }
    }
//========================ActionListener for btnBuild =========================

    public class buildGraph implements ActionListener {

        public String vertex, edge;
//        public JGraphXAdapter x;
//        public int type = mo.getType();

        public buildGraph(String vertex, String edge) {
            this.edge = edge;
            this.vertex = vertex;

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            pContent.removeAll();
            pContent.setLayout(new BorderLayout());
            System.out.println("type" + mo.getType());
            if (mo.getType() == 1) {
                DirectedWeightedMultigraph dg = createDirectedGraph(vertex, edge);
                DirectedWeightedMultigraph dwm = createDirectedGraphWithMyWeigthEdge(vertex, edge);
                x = new JGraphXAdapter<>(dwm);
                jgxAdapter = new JGraphXAdapter<>(dg);
                createDirectedGraphVisualization(x, pContent);

            } else {
                WeightedMultigraph udg = createUnDirectedGraph(vertex, edge);
                WeightedMultigraph wm = createUnDirectedGraphWithMyWeigthEdge(vertex, edge);
                x = new JGraphXAdapter<>(wm);
                jgxAdapter = new JGraphXAdapter<>(udg);
                createUnDirectedGraphVisualization(x, pContent);

            }

            pContent.repaint();
        }
    }

    //====================function to build graph Visualization=========================
    public void createDirectedGraphVisualization(JGraphXAdapter x, JPanel p) {
//        setPreferredSize(DEFAULT_SIZE);
        mxGraphComponent component = new mxGraphComponent(x);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        p.add(component, BorderLayout.CENTER);
//        resize(DEFAULT_SIZE);

        mxCircleLayout layout = new mxCircleLayout(x);
//        mxIGraphLayout l =new 
        // center the circle
        int radius = RADIUD;
        layout.setX0((pContent.getWidth() / 2) - radius);
        layout.setY0((pContent.getHeight() / 2) - radius);
        layout.setRadius(radius);
        layout.setMoveCircle(false);
        layout.execute(x.getDefaultParent());

    }

    public void createUnDirectedGraphVisualization(JGraphXAdapter x, JPanel p) {
        mxGraphComponent component = new mxGraphComponent(x);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        mxGraphModel graphModel = (mxGraphModel) component.getGraph().getModel();
        Collection<Object> cells = graphModel.getCells().values();
        mxUtils.setCellStyles(component.getGraph().getModel(),
                cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);

        p.add(component, BorderLayout.CENTER);

        mxCircleLayout layout = new mxCircleLayout(x);
        
        // center the circle
        int radius = RADIUD;
        layout.setX0((pContent.getWidth() / 2.0) - radius);
        layout.setY0((pContent.getHeight() / 2.0) - radius);
        layout.setRadius(radius);
        
        layout.setMoveCircle(false);
        layout.execute(x.getDefaultParent());

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
        ArrayList<DefaultWeightedEdge> arrDefaultWeightedEdges = new ArrayList<>();

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
//            System.out.println(dg.getEdgeWeight(de[i]));
            arrDefaultWeightedEdges.add(de[i]);
        }
        mo.setArrEdge(arrDefaultWeightedEdges);
        return dg;
    }

    public DirectedWeightedMultigraph createDirectedGraphWithMyWeigthEdge(String V, String E) {
        ArrayList<String> Vertexs = new ArrayList<>();
        ArrayList<String> Edges = new ArrayList<>();
        ArrayList<MyEdgeWeight> arrWeightedEdge = new ArrayList<>();

        Vertexs = changeStringToArr(V);
        Edges = changeStringToArr(E);


        DirectedWeightedMultigraph dgtemp = new DirectedWeightedMultigraph(MyEdgeWeight.class);
        for (String v : Vertexs) {
            dgtemp.addVertex(v);
        }

        MyEdgeWeight[] we = new MyEdgeWeight[Edges.size()];
        for (int i = 0; i < Edges.size(); i++) {
            String e = Edges.get(i);
            String[] temp = e.split(";");

            we[i] = (MyEdgeWeight) dgtemp.addEdge(temp[0], temp[1]);

            dgtemp.setEdgeWeight(we[i], Integer.valueOf(temp[2]));

            arrWeightedEdge.add(we[i]);

        }
        mo.setMyWeight(arrWeightedEdge);

        return dgtemp;
    }
//============================function tao undirect graph===================

    public WeightedMultigraph createUnDirectedGraph(String V, String E) {
        ArrayList<String> Vertexs = new ArrayList<>();
        ArrayList<String> Edges = new ArrayList<>();
        ArrayList<DefaultWeightedEdge> arrDefaultWeightedEdges = new ArrayList<>();

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

            arrDefaultWeightedEdges.add(de[i]);

        }

        mo.setArrEdge(arrDefaultWeightedEdges);
        System.out.println("arrEdge :" + arrDefaultWeightedEdges);
        System.out.println("arrEdge get:" + mo.getArrEdge());
        return udg;
    }

    ///======================================asd
    public WeightedMultigraph createUnDirectedGraphWithMyWeigthEdge(String V, String E) {
        ArrayList<String> Vertexs = new ArrayList<>();
        ArrayList<String> Edges = new ArrayList<>();
        ArrayList<MyEdgeWeight> arrWeightedEdge = new ArrayList<>();

        Vertexs = changeStringToArr(V);
        Edges = changeStringToArr(E);


        WeightedMultigraph udgtemp = new WeightedMultigraph(MyEdgeWeight.class);
        for (String v : Vertexs) {
            udgtemp.addVertex(v);
        }
//        DefaultWeightedEdge[] de = new DefaultWeightedEdge[Edges.size()];
        MyEdgeWeight[] we = new MyEdgeWeight[Edges.size()];
        for (int i = 0; i < Edges.size(); i++) {
            String e = Edges.get(i);
            String[] temp = e.split(";");
            we[i] = (MyEdgeWeight) udgtemp.addEdge(temp[0], temp[1]);
            udgtemp.setEdgeWeight(we[i], Integer.valueOf(temp[2]));
            arrWeightedEdge.add(we[i]);
        }
        mo.setMyWeight(arrWeightedEdge);
        return udgtemp;
    }


//===========================painting Edge ============================================
    public void paintEdge(ArrayList Edges, JGraphXAdapter x) {
        System.out.println("nmo:" + Edges);
//        ArrayList <String> shortestPath1 = Edges;
//        System.out.println("ghi:"+shortestPath1);
        ArrayList<String> E = changeStringToArr(String.valueOf(mo.getArrEdge()));
        System.out.println("jkl:" + E);

        System.out.println("abc:" + mo.getArrEdge());


        ArrayList<MyEdgeWeight> Edge = mo.getMyWeight();
        /// chuwa lay duoc mang defaultEdge
        System.out.println("def:" + Edge);
        ArrayList<MyEdgeWeight> dweShortestPath = new ArrayList<>();

        for (int i = 0; i < Edges.size(); i++) {
            for (int j = 0; j < E.size(); j++) {
                if (Edges.get(i).equals(E.get(j))) {
                    dweShortestPath.add(Edge.get(j));
                }
            }

        }
        System.out.println("111:" + dweShortestPath);
        HashMap<MyEdgeWeight, com.mxgraph.model.mxICell> edgeToCellMap1 = x.getEdgeToCellMap();
        System.out.println("hashmap1 :" + edgeToCellMap1);
        for (int i = 0; i < Edge.size(); i++) {
            mxICell cell = (mxICell) edgeToCellMap1.get(Edge.get(i));
            if (cell.isEdge()) {
                x.setCellStyles(mxConstants.STYLE_STROKECOLOR, "#6482B9", new Object[]{cell});
            }

        }

        System.out.println("res Def shortest Edge:" + dweShortestPath);

        HashMap<MyEdgeWeight, com.mxgraph.model.mxICell> edgeToCellMap = x.getEdgeToCellMap();
        for (int i = 0; i < dweShortestPath.size(); i++) {
            mxICell cell = (mxICell) edgeToCellMap.get(dweShortestPath.get(i));
            if (cell.isEdge()) {
                x.setCellStyles(mxConstants.STYLE_STROKECOLOR, "red", new Object[]{cell});
            }

        }
    }
  //====================create  btn export PNG========================================
    public class exportPNG implements ActionListener{
        public exportPNG(){
            
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                givenAdaptedGraph_whenWriteBufferedImage_thenFileShouldExist(x);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//=========================== cac function connect ===================================

    public void connectToServer() throws UnknownHostException, SocketException, IOException {

        add = InetAddress.getByName(hostname);//UnknownHostException
        socket = new DatagramSocket();	//SocketException
        stdIn = new Scanner(System.in);
         String key = rsa.Encrpytion(keyClient);
                byte[] data = key.getBytes();
                
                dpsend = new DatagramPacket(data, data.length, add, destPort);

                socket.send(dpsend);
    }

    public void sendToServer(String line) throws IOException {
        line = aes.encrypt(line, keyClient); 
            
            

        System.out.print("Client input: ");
        byte[] data = line.getBytes();
        dpsend = new DatagramPacket(data, data.length, add, destPort);
        System.out.println("Client sent " + line + " to " + add.getHostAddress()
                + " from port " + socket.getLocalPort());
        socket.send(dpsend);

    }

    public void closeConnectToServer() {

        System.out.println("Client socket closed");
        stdIn.close();
        socket.close();

    }

    public String receiveFromServer() throws IOException {
        
        dpreceive = new DatagramPacket(new byte[512], 512);
        socket.receive(dpreceive);
        String tmp = new String(dpreceive.getData(), 0, dpreceive.getLength());
        tmp = aes.decrypt(tmp, keyClient);
        System.out.println("Client get: " + tmp + " from server");
        return tmp;
    }
    
    public void givenAdaptedGraph_whenWriteBufferedImage_thenFileShouldExist(JGraphXAdapter graphAdapter) throws IOException {

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer
                .createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        
        File imgFile = new File("src/graph.png");
        
        ImageIO.write(scale(image,pContent.getWidth(),pContent.getHeight()), "PNG", imgFile);
        imgFile.exists();
//        assertTrue(imgFile.exists());
    }
    
    public static BufferedImage scale(BufferedImage src, int w, int h) {
        BufferedImage img
                = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int x, y;
        int ww = src.getWidth();
        int hh = src.getHeight();
        int[] ys = new int[h];
        for (y = 0; y < h; y++) {
            ys[y] = y * hh / h;
        }
        for (x = 0; x < w; x++) {
            int newX = x * ww / w;
            for (y = 0; y < h; y++) {
                int col = src.getRGB(newX, ys[y]);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }
    public static String taoKey()
    {
        Random ran = new Random();
        int top = 6;
        char data = ' ';
        String dat = "";

        for (int i=0; i<=top; i++) {
          data = (char)(ran.nextInt(25)+97);
          dat = data + dat;
        }
        return dat;
    }
    public static int setport()
    {
        Random ran = new Random();

        int value = ran.nextInt((10000 - 1001) - 1);
        return value;
    }
}

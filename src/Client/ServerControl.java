/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author Nam Do
 */
import java.io.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerControl {

    private Connection con;
    private DatagramPacket receivePacket = null;
    private DatagramSocket myServer;
    private DatagramPacket dpreceive, dpsend;
    private int serverPort = 5555;

//    int ktKey = 0;
    int[] ktKey = new int[10000];
    String[] keyClient = new String[10000];
//    String keyClient = "";
    public static int buffsize = 10000;

    public ServerControl() {
        getCon("QLBH", "sa", "1234$");
        if (con != null) {
            System.out.println("OK");
        }
        open(serverPort);
        setstart();
        while (true) {
            listenning();
        }
    }

    private void getCon(String dbName, String user, String pass) {
        String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=" + dbName;
        String dbClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection(dbUrl, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void open(int port) {
        try {
            myServer = new DatagramSocket(port);
            dpreceive = new DatagramPacket(new byte[buffsize], buffsize);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenning() {
        Object o = receiveData();
        
        if(o instanceof String){
            String rev = (String) o;
//          ArrayList<sjfModel> laplichSjf = (ArrayList<sjfModel>) o;
            String sendsjf = getSjf(rev);
            sendData(sendsjf);
//        }else{
//            if(o instanceof MatHang){
//                MatHang mh = (MatHang) o;
//                updateMatHang(mh);
//            }
        }
    }

    public void sendData(String o) {
        try {
            o = aes.encrypt(o, keyClient[dpreceive.getPort()]);      //Mã Hóa Dữ Liệu

            byte[] sendData = o.getBytes();
            dpsend = new DatagramPacket(sendData, sendData.length, dpreceive.getAddress(), dpreceive.getPort());
    //                        System.out.println("Server sent back " + tmp + " to client");
           myServer.send(dpsend);   
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String receiveData() {
        String o = "";

        try {
            myServer.receive(dpreceive);
            o = new String(dpreceive.getData(), 0 , dpreceive.getLength());
            
            if(ktKey[dpreceive.getPort()] == 0) {

                            keyClient[dpreceive.getPort()] = rsa.Decrpytion(o);
                            System.out.println(o);
                            o = "Kết nối thành công";
                            dpsend = new DatagramPacket(o.getBytes(),o .getBytes().length, 
                                            dpreceive.getAddress(), dpreceive.getPort());
    //                        System.out.println("Server sent back " + tmp + " to client");
                           myServer.send(dpsend);
                           ktKey[dpreceive.getPort()] = 1;
                
            }else{
                o = aes.decrypt(o,keyClient[dpreceive.getPort()]);
            }
            
                        
                
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
    
    public String getSjf(String input){

        sjfModel sjfsend = new sjfModel();
        String[] words = input.split(";");
        String sjfSend="";
        if(words[0].equals("1")){
             sjfSend =sjfSend + ojSjf.schSjf(words);
        }
        if(words[0].equals("2")){
             sjfSend =sjfSend + ojPriority.schPriotity(words);
        }
        if(words[0].equals("3")){
             sjfSend =sjfSend + ojFCFS.schFcfs(words);
        }
        if(words[0].equals("4")){
             sjfSend =sjfSend + ojRR.schRR(words);
        }
       
        
//        try{
////            PreparedStatement ps = con.prepareStatement(sql);
////            ps.setString(1, "%"+ten+"%");
////            
////            ResultSet rs = ps.executeQuery()
//            Iterator iter = input.iterator();
//             while (iter.hasNext()) {
//                sjfsend.setId(Interger.valueOf.iter.getId("id"));
//                iter.next().getId();
//            }
//            while(rs.next()){
//                MatHang mh = new MatHang();
//                mh.setId(rs.getInt("id"));
//                mh.setLoai(rs.getString("loai"));
//                mh.setTen(rs.getString("ten"));
//                mh.setGia(rs.getFloat("gia"));
//                mh.setHandung(rs.getString("handung"));
//                mh.setNgaysx(rs.getString("ngaysx"));
//                mh.setSoluong(rs.getInt("soluong"));
//                
//                sjfsend.add(mh);
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        return sjfSend;
    }
    
    public void updateMatHang(MatHang mh){
        String sql = "UPDATE tblMatHang SET gia = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setFloat(1, mh.getGia());
            ps.setInt(2, mh.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void setstart(){
        for(int i = 0; i < 10000;i++){
            ktKey[i] = 0;
            keyClient[i] = "";
        }
    }
}

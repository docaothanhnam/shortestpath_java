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
import static Client.Client.fd;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

/**
 *
 * @author Nam Do
 */
public class myFrame extends JFrame implements ActionListener{
    //public static MenuOption moUrl = new MenuOption();
//    private Client client;
    public static JPanel pMenu, pContent;
    public static JRadioButton radUndirected, radDirected;
    public static JLabel lSource, lTarget, lUrl,lShortestPathLength;
    public static JButton btnChooseFile, btnBuild, btnExportPng, btnReset, btnShortestPath;
    public static FileDialog fd,fdsave;
    public static JTextField txtLength;
//    private JTextField txtUrl;
    public static JComboBox<String> cbbBeginPoint = new JComboBox<>();
    public static JComboBox<String> cbbEndPoint = new JComboBox<>();

    private int WIDTH = 1300, HEIGHT = 700;
    public static final Dimension DEFAULT_SIZE = new Dimension(1200, 1000);
    int WIDTH_SELECT, HEIGHT_SELECT;
    String fileName;
    public static String URL;
    
//    File f = null;
    
    
    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    public myFrame(String title) throws FileNotFoundException {
        //this.client = client;
        setTitle(title);
        inIt();

    }
    

    public void inIt() throws FileNotFoundException {

        setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        
        fd = new FileDialog(new Frame());
        fd.setLocationRelativeTo(null);
//        fd.setFilenameFilter(new FilenameFilter() {
//            public boolean accept(File dir, String name) {
//                return name.endsWith(".txt");
//            }
//        });
        fd.setTitle("Open File");
        fd.setFile("*.txt");
        
        
        fdsave = new FileDialog(new Frame(),"Save File",FileDialog.SAVE);
//        fdsave.setFilenameFilter(new FilenameFilter() {
//            public boolean accept(File dir, String name) {
//                return name.endsWith(".png");
//            }
//        });
        fdsave.setFile("*.png");
//        fdsave.setType(Type.);    
                
        lUrl= new JLabel("File:");

        // panel menu
        pMenu = new JPanel();
//        pMenu.setPreferredSize(new Dimension(450, MAXIMIZED_VERT));
        pMenu.setLayout(new GridLayout(1, 1, 50, 20));
        pMenu.setBackground(Color.WHITE);
        pMenu.add(createMenu());
        txtLength.setEditable(false);
        txtLength.setBorder(new LineBorder(Color.BLACK));
//        radUndirected.isSelected();
        // panel content
        
       
        pContent = new JPanel(new BorderLayout());
//        pContent.setBackground(Color.LIGHT_GRAY);
        pContent.setBorder(new LineBorder(Color.WHITE,2));
        
//        jgxAdapter = new JGraphXAdapter<>(dg);
//        createGraphVisualization(jgxAdapter,pContent);

        add(pMenu, BorderLayout.WEST);
        add(pContent, BorderLayout.CENTER);
        setVisible(true);
        setLocationRelativeTo(null);

        
    }
    
    

    private JPanel createMenu() {
        JPanel panel = new JPanel(new BorderLayout());
//        panel.setPreferredSize(new Dimension(450, MAXIMIZED_VERT));
        JPanel panelTop = new JPanel(new GridBagLayout());
        JPanel panelBottom = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

            // Panel for group graphtype
            JPanel panelGraphType = new JPanel(new BorderLayout());
            panelGraphType.setBorder(new TitledBorder("Graph Type"));
                
                JPanel panelGraphTypeTemp = new JPanel(new GridLayout(1, 2, 20, 20));
                panelGraphTypeTemp.setBorder(new EmptyBorder(0, 10, 0, 5));
                panelGraphTypeTemp.add(radUndirected = new JRadioButton("Undirected", true));
                panelGraphTypeTemp.add(radDirected = new JRadioButton("Directed", false));
                
                    ButtonGroup groupMapType = new ButtonGroup();
                    groupMapType.add(radUndirected);
                    groupMapType.add(radDirected);
                    
            panelGraphType.add(panelGraphTypeTemp);
            
            // panel for group btn choose file
            
            JPanel panelFileChooser = new JPanel(new BorderLayout());
            panelFileChooser.setBorder(new TitledBorder("Choose File"));
           
                JPanel panelFileChooserTemp = new JPanel(new BorderLayout(10, 10));
                panelFileChooserTemp.setBorder(new EmptyBorder(0, 10, 0, 5));
                panelFileChooserTemp.add(btnChooseFile = new JButton("Open File"),BorderLayout.WEST);
                panelFileChooserTemp.add(lUrl,BorderLayout.CENTER);               

            panelFileChooser.add(panelFileChooserTemp);
            
            // panel for group combobox
            
            JPanel panelCbb = new JPanel(new BorderLayout());
            panelCbb.setBorder(new TitledBorder("Source & Destination"));
            
                JPanel panelCbbTemp = new JPanel(new GridLayout(1, 4, 20, 5));
                panelCbbTemp.add(lSource = new JLabel("Source :"));
                panelCbbTemp.add(cbbBeginPoint);
                panelCbbTemp.add(lSource = new JLabel("Destination :"));
                panelCbbTemp.add(cbbEndPoint);
            
            panelCbb.add(panelCbbTemp);
        
            // panel for group btn option
            JPanel panelBtn = new JPanel(new BorderLayout());
            panelBtn.setBorder(new TitledBorder("Option"));
            
                JPanel panelBtnTemp = new JPanel(new GridLayout(3, 1, 20, 20));
                JPanel temp1 = new JPanel(new GridLayout(1,2,20,20));
                JPanel temp2 = new JPanel(new GridLayout(1,2,20,20));
                JPanel temp3 = new JPanel(new GridLayout(1, 2, 20, 20));
                temp1.add(btnBuild = new JButton("Build Graph"));
                temp1.add(btnShortestPath = new JButton("Shortest Path"));
                temp2.add(btnReset = new JButton("Reset"));
                temp2.add(btnExportPng = new JButton("Export PNG"));
                temp3.add(lShortestPathLength = new JLabel("ShortestPath Length: "),JLabel.CENTER);
                temp3.add(txtLength = new JTextField());
                panelBtnTemp.add(temp1);
                panelBtnTemp.add(temp2);
                panelBtnTemp.add(temp3);
            panelBtn.add(panelBtnTemp);
            
        // thiet lap vi tri cho cac thanh phan
        gbc.gridx = 0;
        gbc.gridy = 0;

        panelTop.add(panelFileChooser,gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;

        panelTop.add(panelGraphType,gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;

        panelTop.add(panelCbb,gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;

        panelTop.add(panelBtn,gbc);

        panel.add(panelTop,BorderLayout.PAGE_START);
        panel.add(panelBottom,BorderLayout.PAGE_END);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        
//		WIDTH_SELECT = (int) panel.getPreferredSize().getWidth();
//		HEIGHT_SELECT = (int) panel.getPreferredSize().getHeight();
        return panel;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //To change body of generated methods, choose Tools | Templates.
        
    }
    

    
}
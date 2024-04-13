package Main;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import Jdbc.Jdbc;
import MyPage.Membership;
import MyPage.UserManager;

public class MainTap extends JFrame {
   private static String loggedInUserImage;
    private static final long serialVersionUID = 1L;

    public MainTap() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);

        // �޴� �� ����
        JMenuBar menuBar = new JMenuBar();

        // �޴� ���� ũ�� ���� (���� ũ��)
        menuBar.setPreferredSize(new Dimension(1200, 80));

        // �޴� �׸� ����
        JMenuItem mainPageMenuItem = new JMenuItem("�쵿 ����Ʈ");
        JMenuItem courseMenuItem = new JMenuItem("�ڽ�");
        JMenuItem crewMenuItem = new JMenuItem("ũ��");
        JMenuItem boardMenuItem = new JMenuItem("�Խ���");

        // �޴� �׸��� ��Ʈ ����
        Font menuFont1 = new Font("Gong Gothic Bold", Font.PLAIN, 30);
        Font menuFont = new Font("Gong Gothic Bold", Font.PLAIN, 25);
        Color menuItemTextColor = Color.WHITE; // ������� ����
        mainPageMenuItem.setFont(menuFont1);
        courseMenuItem.setFont(menuFont);
        crewMenuItem.setFont(menuFont);
        boardMenuItem.setFont(menuFont);

        // �޴� �׸��� ��Ʈ ���� ����
        mainPageMenuItem.setForeground(menuItemTextColor);
        courseMenuItem.setForeground(menuItemTextColor);
        crewMenuItem.setForeground(menuItemTextColor);
        boardMenuItem.setForeground(menuItemTextColor);
        
        menuBar.add(Box.createHorizontalGlue());

        // �޴� �׸��� �ؽ�Ʈ ���� ������ ����� ����
        mainPageMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
        courseMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
        crewMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
        boardMenuItem.setHorizontalAlignment(SwingConstants.CENTER);

        // �޴� �׸� ActionListener �߰�
        mainPageMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // �ڽ� â �̵�
                new MainPage().setVisible(true);
                dispose(); // ���� â �ݱ�
            }
        });

        courseMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // �ڽ� â �̵�
                new Course().setVisible(true);
                dispose(); // ���� â �ݱ�
            }
        });

        crewMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ũ�� â �̵�
                new Crew().setVisible(true);
                dispose(); // ���� â �ݱ�
            }
        });

        boardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // �Խ��� â �̵�
                new Board().setVisible(true);
                dispose(); // ���� â �ݱ�
            }
        });
        
        


        // �޴� �ٿ� �޴� �׸� �߰�
        menuBar.add(mainPageMenuItem);
        menuBar.add(courseMenuItem);
        menuBar.add(crewMenuItem);
        menuBar.add(boardMenuItem);

        // WeatherAppPanel �߰�
        WeatherApp weatherApp = new WeatherApp();
        WeatherAppPanel weatherAppPanel = new WeatherAppPanel(weatherApp);
        weatherAppPanel.setPreferredSize(new Dimension(120, 600)); // WeatherAppPanel�� ũ�� ����
        menuBar.add(weatherAppPanel);

        // myPageMenuItem ��ư ���� �� ����
        JButton myPageButton = new JButton();
        myPageButton.setPreferredSize(new Dimension(80, 80)); // ���簢�� ũ��� ����

        // ImageIcon�� �����ϰ� �̹��� ũ�� ����
       
        
        ImageIcon myPageIcon = new ImageIcon(loggedInUserImage);
        Image img = myPageIcon.getImage();
        Image updateImg = img.getScaledInstance(myPageButton.getPreferredSize().width, myPageButton.getPreferredSize().height, Image.SCALE_SMOOTH);
        ImageIcon updateIcon = new ImageIcon(updateImg);
        myPageButton.setIcon(updateIcon);

        // �׵θ� ���ֱ�
        myPageButton.setBorderPainted(false);

        // ������ ������ ���� ���� �߰�
        menuBar.add(Box.createHorizontalGlue());

        // �޴� �ٿ� myPageButton �߰� (������ ����)
        menuBar.add(myPageButton);
        
        myPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // �ڽ� â �̵�
               
               new MyPage().setVisible(true);
                dispose(); // ���� â �ݱ�
               

            }
        });
        
        // UIManager�� ����Ͽ� �޴� ���� ���� ����
        UIManager.put("MenuBar.background", Color.BLACK);
        UIManager.put("Menu.background", Color.BLACK);
        UIManager.put("MenuItem.background", Color.BLACK);
        SwingUtilities.updateComponentTreeUI(menuBar);

        // �����ӿ� �޴� �� ����
        setJMenuBar(menuBar);
    }

    public static void setLoggedInUserImage(String userImage) {
        loggedInUserImage = userImage;
    }
    
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainTap frame = new MainTap();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
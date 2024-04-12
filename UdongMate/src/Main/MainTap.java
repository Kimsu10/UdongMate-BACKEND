package Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainTap extends JFrame {

    private static final long serialVersionUID = 1L;

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

    public MainTap() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);

        // �޴� �� ����
        JMenuBar menuBar = new JMenuBar();

        // �޴� ���� ũ�� ���� (���� ũ��)
        menuBar.setPreferredSize(new Dimension(1200, 80));

        // �޴� �׸� ����
        JMenuItem mainPageMenuItem = new JMenuItem("�쵿 ����Ʈ");
        JMenuItem courseMenuItem = new JMenuItem("�ڽ�");
        JMenuItem crewMenuItem = new JMenuItem("ũ��");
        JMenuItem boardMenuItem = new JMenuItem("�Խ���");
        JMenuItem weatherMenuItem = new JMenuItem("����");

        // �޴� �׸��� ��Ʈ ����
        Font menuFont = new Font("Gong Gothic Bold", Font.PLAIN, 25);
        Color menuItemTextColor = Color.WHITE; // ������� ����
        mainPageMenuItem.setFont(menuFont);
        courseMenuItem.setFont(menuFont);
        crewMenuItem.setFont(menuFont);
        boardMenuItem.setFont(menuFont);
        weatherMenuItem.setFont(menuFont);

        // �޴� �׸��� ��Ʈ ���� ����
        mainPageMenuItem.setForeground(menuItemTextColor);
        courseMenuItem.setForeground(menuItemTextColor);
        crewMenuItem.setForeground(menuItemTextColor);
        boardMenuItem.setForeground(menuItemTextColor);
        weatherMenuItem.setForeground(menuItemTextColor);

        // ��� ������ ���� ���� �߰�
        menuBar.add(Box.createHorizontalGlue());

        // �޴� �ٿ� �޴� �׸� �߰�
        menuBar.add(mainPageMenuItem);
        menuBar.add(courseMenuItem);
        menuBar.add(crewMenuItem);
        menuBar.add(boardMenuItem);
        menuBar.add(weatherMenuItem);

        // myPageMenuItem ��ư ���� �� ����
        JButton myPageButton = new JButton();
        myPageButton.setPreferredSize(new Dimension(80, 80)); // ���簢�� ũ��� ����

        // ImageIcon�� �����ϰ� �̹��� ũ�� ����
        ImageIcon myPageIcon = new ImageIcon("img/lim_seoyul.jpg");
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

        // UIManager�� ����Ͽ� �޴� ���� ���� ����
        UIManager.put("MenuBar.background", Color.BLACK);
        UIManager.put("Menu.background", Color.BLACK);
        UIManager.put("MenuItem.background", Color.BLACK);
        SwingUtilities.updateComponentTreeUI(menuBar);

        // �����ӿ� �޴� �� ����
        setJMenuBar(menuBar);
        
        // courseMenuItem�� ActionListener �߰�
        mainPageMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // �ڽ� â �̵�
                new MainPage().setVisible(true);
                dispose(); // ���� â �ݱ�
            }
        });
        // courseMenuItem�� ActionListener �߰�
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
                // �ڽ� â �̵�
                new Crew().setVisible(true);
                dispose(); // ���� â �ݱ�
            }
        });
        
        boardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // �ڽ� â �̵�
                new Board().setVisible(true);
                dispose(); // ���� â �ݱ�
            }
        });
        
        myPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // �ڽ� â �̵�
                new MyPage().setVisible(true);
                dispose(); // ���� â �ݱ�
            }
        });

    }
}
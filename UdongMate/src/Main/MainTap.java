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

        // 메뉴 바 생성
        JMenuBar menuBar = new JMenuBar();

        // 메뉴 바의 크기 설정 (고정 크기)
        menuBar.setPreferredSize(new Dimension(1200, 80));

        // 메뉴 항목 생성
        JMenuItem mainPageMenuItem = new JMenuItem("우동 메이트");
        JMenuItem courseMenuItem = new JMenuItem("코스");
        JMenuItem crewMenuItem = new JMenuItem("크루");
        JMenuItem boardMenuItem = new JMenuItem("게시판");
        JMenuItem weatherMenuItem = new JMenuItem("날씨");

        // 메뉴 항목의 폰트 설정
        Font menuFont = new Font("Gong Gothic Bold", Font.PLAIN, 25);
        Color menuItemTextColor = Color.WHITE; // 흰색으로 설정
        mainPageMenuItem.setFont(menuFont);
        courseMenuItem.setFont(menuFont);
        crewMenuItem.setFont(menuFont);
        boardMenuItem.setFont(menuFont);
        weatherMenuItem.setFont(menuFont);

        // 메뉴 항목의 폰트 색상 설정
        mainPageMenuItem.setForeground(menuItemTextColor);
        courseMenuItem.setForeground(menuItemTextColor);
        crewMenuItem.setForeground(menuItemTextColor);
        boardMenuItem.setForeground(menuItemTextColor);
        weatherMenuItem.setForeground(menuItemTextColor);

        // 가운데 정렬을 위한 공백 추가
        menuBar.add(Box.createHorizontalGlue());

        // 메뉴 바에 메뉴 항목 추가
        menuBar.add(mainPageMenuItem);
        menuBar.add(courseMenuItem);
        menuBar.add(crewMenuItem);
        menuBar.add(boardMenuItem);
        menuBar.add(weatherMenuItem);

        // myPageMenuItem 버튼 생성 및 설정
        JButton myPageButton = new JButton();
        myPageButton.setPreferredSize(new Dimension(80, 80)); // 정사각형 크기로 수정

        // ImageIcon을 생성하고 이미지 크기 조정
        ImageIcon myPageIcon = new ImageIcon("img/lim_seoyul.jpg");
        Image img = myPageIcon.getImage();
        Image updateImg = img.getScaledInstance(myPageButton.getPreferredSize().width, myPageButton.getPreferredSize().height, Image.SCALE_SMOOTH);
        ImageIcon updateIcon = new ImageIcon(updateImg);
        myPageButton.setIcon(updateIcon);

        // 테두리 없애기
        myPageButton.setBorderPainted(false);

        // 오른쪽 정렬을 위한 공백 추가
        menuBar.add(Box.createHorizontalGlue());

        // 메뉴 바에 myPageButton 추가 (오른쪽 정렬)
        menuBar.add(myPageButton);

        // UIManager를 사용하여 메뉴 바의 배경색 변경
        UIManager.put("MenuBar.background", Color.BLACK);
        UIManager.put("Menu.background", Color.BLACK);
        UIManager.put("MenuItem.background", Color.BLACK);
        SwingUtilities.updateComponentTreeUI(menuBar);

        // 프레임에 메뉴 바 설정
        setJMenuBar(menuBar);
        
        // courseMenuItem에 ActionListener 추가
        mainPageMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 코스 창 이동
                new MainPage().setVisible(true);
                dispose(); // 현재 창 닫기
            }
        });
        // courseMenuItem에 ActionListener 추가
        courseMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 코스 창 이동
                new Course().setVisible(true);
                dispose(); // 현재 창 닫기
            }
        });
        
        crewMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 코스 창 이동
                new Crew().setVisible(true);
                dispose(); // 현재 창 닫기
            }
        });
        
        boardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 코스 창 이동
                new Board().setVisible(true);
                dispose(); // 현재 창 닫기
            }
        });
        
        myPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 코스 창 이동
                new MyPage().setVisible(true);
                dispose(); // 현재 창 닫기
            }
        });

    }
}
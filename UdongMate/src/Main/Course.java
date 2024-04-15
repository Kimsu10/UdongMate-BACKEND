package Main;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Course extends MainTap {
    private static final long serialVersionUID = 1L;
    private JPanel contentPanel; // contentPanel을 멤버 변수로 변경

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Course frame = new Course();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Course() {
        super();
        setLocationRelativeTo(null);
        // contentPanel 초기화
        contentPanel = new JPanel(new BorderLayout());
        getContentPane().add(contentPanel, BorderLayout.CENTER); // contentPanel을 MainTap의 컨텐트 패널에 추가

        // 왼쪽 패널 생성
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(150, 150, 100, 100)); // 왼쪽 여백 설정
        contentPanel.add(leftPanel, BorderLayout.WEST);

        // 등산 버튼 생성
        JButton mountain = new JButton();
        mountain.setIcon(new ImageIcon("img/Mountain.png")); // 등산 아이콘 이미지 설정
        mountain.setPreferredSize(new Dimension(400, 400)); // 버튼 크기 조정
        mountain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 등산 탭으로 이동
                new Mountain().setVisible(true); // 등산 페이지로 이동하는 코드
                dispose(); // 현재 창 닫기
            }
        });
        
        Font menuFont1 = new Font("Gong Gothic Bold", Font.PLAIN, 30);
        JLabel mountainLabel = new JLabel("등산"); // 버튼 아래에 표시될 텍스트
        mountainLabel.setFont(menuFont1);
        mountainLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가운데 정렬

        // 등산 버튼과 라벨을 추가
        leftPanel.add(mountain, BorderLayout.NORTH);
        leftPanel.add(mountainLabel, BorderLayout.CENTER);

        // 오른쪽 패널 생성
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(150, 100, 100, 150)); // 오른쪽 여백 설정
        contentPanel.add(rightPanel, BorderLayout.EAST);

        // 산책 버튼 생성
        JButton ground = new JButton();
        ground.setIcon(new ImageIcon("img/Ground.png")); // 산책 아이콘 이미지 설정
        ground.setPreferredSize(new Dimension(400, 400)); // 버튼 크기 조정
        ground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 산책 탭으로 이동
                new Ground().setVisible(true); // 산책 페이지로 이동하는 코드
                dispose(); // 현재 창 닫기
            }
        });
        JLabel groundLabel = new JLabel("산책/러닝"); // 버튼 아래에 표시될 텍스트
        groundLabel.setFont(menuFont1);
        groundLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가운데 정렬

        // 산책 버튼과 라벨을 추가
        rightPanel.add(ground, BorderLayout.NORTH);
        rightPanel.add(groundLabel, BorderLayout.CENTER);
    }
}
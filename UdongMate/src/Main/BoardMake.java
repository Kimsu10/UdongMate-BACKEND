package Main;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import JDBC.Jdbc;

public class BoardMake extends JFrame {

   private static int loggedInUserNo;
   private JComboBox<String> boTypeDropdown;
   private JTextArea contentArea;

   public BoardMake() {
      super("새 글 작성");

      setSize(550, 450); // 창 크기
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(null); // 절대 레이아웃
      setLocationRelativeTo(null); // 창을 화면 중앙에 위치
      
      System.out.println(loggedInUserNo);

      Font font = new Font("Gong Gothic Bold", Font.PLAIN, 15); // 폰트 생성
      Iterator<Object> keysIterator = UIManager.getDefaults().keySet().iterator(); // 모든 컴포넌트에 폰트 적용
      while (keysIterator.hasNext()) {
         Object key = keysIterator.next();
         Object value = UIManager.get(key);
         if (value instanceof FontUIResource) {
            UIManager.put(key, font);
         }
      }

      // 라벨과 입력필드 생성
      JLabel nameLabel = new JLabel("제목: ");
      nameLabel.setBounds(10, 20, 80, 25);
      add(nameLabel);

      JTextField titleField = new JTextField();
      titleField.setBounds(69, 20, 380, 25);
      add(titleField);
      JLabel contentLabel = new JLabel("내용: ");
      contentLabel.setBounds(10, 50, 80, 25);
      add(contentLabel);

      contentArea = new JTextArea();
      contentArea.setText("새 글을 작성해 주세요!");
      contentArea.setForeground(Color.GRAY); // 힌트 텍스트의 색상을 회색으로 설정
      
      contentArea.addFocusListener(new FocusListener() {
         
      
       @Override
         public void focusGained(FocusEvent e) {
             if (contentArea.getText().equals("새 글을 작성해 주세요!")) {
                contentArea.setText("");
                contentArea.setForeground(Color.BLACK); // 힌트 텍스트의 색상을 검은색으로 변경
             }
         }

         @Override
         public void focusLost(FocusEvent e) {
             if (contentArea.getText().isEmpty()) {
                contentArea.setText("새 글을 작성해 주세요!");
                contentArea.setForeground(Color.GRAY); // 힌트 텍스트의 색상을 회색으로 변경
             }
         }
     });

      contentArea.setBounds(69, 50, 400, 250);
      add(contentArea);
      contentArea.setLineWrap(true); // 자동 줄 바꿈 설정

      JLabel boTypeLabel = new JLabel("글 종류: ");
      boTypeLabel.setBounds(10, 310, 80, 25);
      add(boTypeLabel);

      String[] boTypes = {"홍보글", "잡담"};
      boTypeDropdown = new JComboBox<>(boTypes);
      boTypeDropdown.setBounds(69, 310, 176, 25);
      add(boTypeDropdown);

      JButton registerButton = new JButton("게시글 등록하기");
      registerButton.setBounds(180, 350, 200, 25);
      registerButton.setBackground(Color.white);

      registerButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // 입력된 게시글 정보 가져오기
            String boTitle = titleField.getText();
            String boContent = contentArea.getText();
            String boType = (String) boTypeDropdown.getSelectedItem();
            int boTypeno = boType.equals("홍보글") ? 1 : 2;
            // 게시글 정보를 crew 테이블에 삽입하기 위한 INSERT 쿼리 작성
            String query = "CALL createBoard(?, ?, ?, ?);";
            try (
                  Connection con = Jdbc.get(); 
                  PreparedStatement psmt = con.prepareStatement(query)) {

               // 쿼리의 ?에 값 할당
               psmt.setString(1, boTitle);
               psmt.setString(2, boContent);
               psmt.setInt(3, boTypeno);
               psmt.setInt(4, loggedInUserNo);
               
               // 쿼리 실행
               int rowsInserted = psmt.executeUpdate();
               
               if (rowsInserted == 1) {
                  JOptionPane.showMessageDialog(BoardMake.this, "게시글이 등록되었습니다.");
                  // 크루 생성 완료 후 처리할 내용 추가
                  BoardMake.this.dispose();
               } else {
                  JOptionPane.showMessageDialog(BoardMake.this, "게시글 등록에 실패하였습니다. 다시 시도해주세요.");
               }
            } catch (SQLException ex) {
               ex.printStackTrace();
               JOptionPane.showMessageDialog(BoardMake.this, "게시글 등록 중 오류가 발생하였습니다. 다시 시도해주세요.");
            }
         }
      });
      add(registerButton);

      setVisible(true);
   }

   public static void setLoggedInUserNo(int userNo) {
      loggedInUserNo = userNo;
   }

   public static void main(String[] args) {
      // 크루를 생성한 사용자의 user_no를 인자로 전달하여 CrewMake 객체 생성
     
      new BoardMake();
   }
}
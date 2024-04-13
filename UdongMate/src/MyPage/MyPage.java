package MyPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import JDBC.Jdbc;

public class MyPage extends MainTap {
   private static int loggedInUserNo;
   private JPanel contentPanel; // contentPanel을 멤버 변수로 변경

   public static void main(String[] args) {
      // Create a new instance of MyPage
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               MyPage frame = new MyPage();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   public MyPage() {
      // Call the constructor of the parent class
      super();

      contentPanel = new JPanel(new BorderLayout());
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      contentPanel.setBorder(new LineBorder(Color.BLACK));

      JPanel leftPanel = new JPanel(new BorderLayout());
      leftPanel.setBorder(new EmptyBorder(30, 150, 100, 100)); // 왼쪽 여백 설정
      contentPanel.add(leftPanel, BorderLayout.WEST);

      JPanel rightPanel = new JPanel(new BorderLayout());
      rightPanel.setBorder(new EmptyBorder(10, 0, 0, 700)); // 오른쪽 여백 설정
      contentPanel.add(rightPanel, BorderLayout.EAST);



      try {
         Connection con = null;
         PreparedStatement psmt = null;
         ResultSet rs = null;

         String query = "SELECT * FROM user u, address a WHERE user_no = ?";
         con = Jdbc.get();
         psmt = con.prepareStatement(query);
         psmt.setInt(1, loggedInUserNo);
         rs = psmt.executeQuery();

         if (rs.next()) {
            // Retrieve user information
            String userName = rs.getString("user_name");
            String userNick = rs.getString("user_nick");
            String userPhone = rs.getString("user_phone");
            String userGender = rs.getString("user_gender");
            int userAge = rs.getInt("user_age");
            String addrName = rs.getString("addr_name");
            String userImg = rs.getString("user_img");

            // Create labels for each user information
            JLabel userNameLabel = new JLabel("이름 : " + userName);
            JLabel userNickLabel = new JLabel("닉네임 : " + userNick);
            JLabel userPhoneLabel = new JLabel("전화번호 : " + userPhone);
            JLabel userGenderLabel = new JLabel("성별 : " + userGender);
            JLabel userAgeLabel = new JLabel("나이 : " + userAge);
            JLabel addrNoLabel = new JLabel("주소 : " + addrName);
            JLabel userImgLabel = new JLabel();

            // Add labels to the panel
            ImageIcon imageIcon = new ImageIcon(userImg);
            Image image = imageIcon.getImage();
            Image circularImage = getCircularImage(image);
            userImgLabel.setIcon(new ImageIcon(circularImage)); // 등산 아이콘 이미지 설정
            leftPanel.add(userImgLabel, BorderLayout.NORTH);

            Font menuFont1 = new Font("Gong Gothic Bold", Font.PLAIN, 30);
            JLabel groundLabel = new JLabel("산책/러닝"); // 버튼 아래에 표시될 텍스트
            JLabel dLabel = new JLabel("d");
            groundLabel.setFont(menuFont1);
            groundLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가운데 정렬
            rightPanel.add(groundLabel, BorderLayout.CENTER);
            dLabel.setFont(menuFont1);
            // Align text to the center
            dLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Add labels to rightPanel
            rightPanel.add(dLabel);
            
            
            
            
            
         } else {
            System.out.println("No user found.");
         }

         // Close ResultSet, PreparedStatement, and Connection
         rs.close();
         psmt.close();
         con.close();

         // Add the panel to the center of the frame

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public static void setLoggedInUserNo(int userNo) {
      loggedInUserNo = userNo;
   }

   private Image getCircularImage(Image image) {
      int diameter = 290;
      BufferedImage circle = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = circle.createGraphics();
      int x = (diameter - image.getWidth(null)) / 2;
      int y = (diameter - image.getHeight(null)) / 2;
      graphics.setClip(new Ellipse2D.Float(1, 2, diameter, diameter));
      graphics.drawImage(image, x, y, null);
      return circle;
   }


   @Override
   public Dimension getPreferredSize() {
      return new Dimension(400, 300); // Set preferred size of the frame
   }
}


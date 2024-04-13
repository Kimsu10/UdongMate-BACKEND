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
import Main.MainTap;

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
      
      setLocationRelativeTo(null);
      
      System.out.println(loggedInUserNo);

      contentPanel = new JPanel(new BorderLayout());
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      

      JPanel leftPanel = new JPanel(new BorderLayout());
      leftPanel.setBorder(new EmptyBorder(130, 200, 100, 100)); // 왼쪽 여백 설정
      contentPanel.add(leftPanel, BorderLayout.WEST);

      JPanel rightPanel = new JPanel(new BorderLayout());
      rightPanel.setPreferredSize(new Dimension(1200, 1000)); // rightPanel 크기 설정
      contentPanel.add(rightPanel, BorderLayout.EAST);
      
      JPanel rPanel = new JPanel(new GridLayout(7, 1));
      rightPanel.add(rPanel, BorderLayout.NORTH);
      rightPanel.setBorder(new EmptyBorder(140, 450, 0, 200)); // 오른쪽 여백 설정
      

      Color menuItemTextColor = Color.GRAY;
      


      try {
         Connection con = null;
         PreparedStatement psmt = null;
         ResultSet rs = null;

         String query = "SELECT * FROM user u "
               + "JOIN user_crew uc ON u.user_no = uc.user_no "
               + "JOIN crew c ON uc.crew_no = c.crew_no "
               + "JOIN address a ON u.addr_no = a.addr_no "
               + "where u.user_no = ?";
         con = Jdbc.get();
         psmt = con.prepareStatement(query);
         psmt.setInt(1, loggedInUserNo);
         rs = psmt.executeQuery();

         if (rs.next()) {
            // Retrieve user information
            String userName = rs.getString("u.user_name");
            String userNick = rs.getString("u.user_nick");
            String userPhone = rs.getString("u.user_phone");
            String userGender = rs.getString("u.user_gender");
            int userAge = rs.getInt("u.user_age");
            String addrName = rs.getString("a.addr_name");
            String userImg = rs.getString("u.user_img");
            String crewName = rs.getString("c.crew_name");
            int crewCap = rs.getInt("uc.crew_cap");
            

            // Create labels for each user information
            JLabel userNameLabel = new JLabel("이름 :              " + userName);
            JLabel userNickLabel = new JLabel("닉네임 :          " + userNick);
            JLabel userPhoneLabel = new JLabel("전화번호 :       " + userPhone);
            JLabel userGenderLabel = new JLabel("성별 :              " + userGender);
            JLabel userAgeLabel = new JLabel("나이 :              " + userAge + "세");
            JLabel addrNoLabel = new JLabel("주소 :              " + addrName);
            JLabel userImgLabel = new JLabel();

            // Add labels to the panel
            ImageIcon imageIcon = new ImageIcon(userImg);
            Image image = imageIcon.getImage();
            Image circularImage = getCircularImage(image);
            userImgLabel.setIcon(new ImageIcon(circularImage)); 
            leftPanel.add(userImgLabel, BorderLayout.NORTH);

            Font menuFont1 = new Font("Gong Gothic Bold", Font.PLAIN, 30);
             

            
            userNickLabel.setFont(menuFont1);
            userNameLabel.setFont(menuFont1);
            userPhoneLabel.setFont(menuFont1);
            userGenderLabel.setFont(menuFont1);
            userAgeLabel.setFont(menuFont1);
            addrNoLabel.setFont(menuFont1);
            
            userNameLabel.setForeground(menuItemTextColor);
            userGenderLabel.setForeground(menuItemTextColor);
            addrNoLabel.setForeground(menuItemTextColor);
            
            rPanel.add(userNickLabel);
            rPanel.add(userNameLabel);
            rPanel.add(userPhoneLabel);
            rPanel.add(userGenderLabel);
            rPanel.add(userAgeLabel);
            rPanel.add(addrNoLabel);
            
            int verticalGap = 10;
            userNickLabel.setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
            userNameLabel.setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
            userPhoneLabel.setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
            userGenderLabel.setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
            userAgeLabel.setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
            addrNoLabel.setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
            
            
              
            if(crewCap == 1) {               
               JLabel crewNameLabel = new JLabel("크루 :              " + crewName + "(Leader)");
               crewNameLabel.setFont(menuFont1);
               rPanel.add(crewNameLabel);
               crewNameLabel.setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
            }else {
               JLabel crewNameLabel = new JLabel("크루 :              " + crewName);   
               crewNameLabel.setFont(menuFont1);
               rPanel.add(crewNameLabel);
               crewNameLabel.setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
            }
            
            
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
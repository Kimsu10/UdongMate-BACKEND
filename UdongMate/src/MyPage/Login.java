package MyPage;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

import Jdbc.Jdbc;
import Main.BoardMake;
import Main.CrewMake;
import Main.MainPage;
import Main.MainTap;

public class Login extends JFrame {

   private JTextField idField;
   private JPasswordField pwField;
   private int loggedInUserNo;

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               Login frame = new Login();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   public Login() {

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(500, 600);
      setResizable(false);
      setTitle("로그인");

      getContentPane().setLayout(null);

      Font font = new Font("gong gothic bold", Font.PLAIN, 14);

      // Adding JLabel to display image
      JLabel imgLabel = new JLabel();
      imgLabel.setBounds(100, 10, 300, 300);
//      imgLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); 

      ImageIcon imgIcon = new ImageIcon("img/loginImage.png"); 
      Image img = imgIcon.getImage().getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(), Image.SCALE_SMOOTH);
      imgIcon = new ImageIcon(img);
      imgLabel.setIcon(imgIcon);
      getContentPane().add(imgLabel);

      JLabel usernameLabel = new JLabel("아이디 :");
      usernameLabel.setFont(font);
//      usernameLabel.setBounds(96, 116, 80, 20);
      usernameLabel.setBounds(96, 340, 80, 20); 
      getContentPane().add(usernameLabel);

      idField = new JTextField();
      idField.setFont(font);
//      idField.setBounds(188, 112, 159, 30);
      idField.setBounds(188, 336, 159, 30); 
      getContentPane().add(idField);

      JLabel passwordLabel = new JLabel("비밀번호 :");
      passwordLabel.setFont(font);
//      passwordLabel.setBounds(96, 156, 80, 20);
      passwordLabel.setBounds(96, 380, 80, 20); 
      getContentPane().add(passwordLabel);

      pwField = new JPasswordField();
      pwField.setFont(font);
//      pwField.setBounds(188, 152, 159, 30);
      pwField.setBounds(188, 376, 159, 30);
      pwField.setEchoChar('*');
      getContentPane().add(pwField);

      JButton regButton = new JButton("회원가입");
      regButton.setBackground(Color.white);
      regButton.setFont(font);
//      regButton.setBounds(107, 239, 120, 30);
      regButton.setBounds(107, 459, 120, 30);
      getContentPane().add(regButton);

      JButton loginButton = new JButton("로그인");
      loginButton.setBackground(Color.white);
      loginButton.setFont(font);
//      loginButton.setBounds(252, 239, 120, 30);
      loginButton.setBounds(252, 459, 120, 30); 
      getContentPane().add(loginButton);

      setLocationRelativeTo(null);

      regButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Membership m = new Membership();
            m.setVisible(true);
         }
      });

      loginButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             // 로그인 성공 여부 확인
             if (loginSuccess() == 2) {

                String userImg = UserManager.getUserImage(loggedInUserNo);
                MainTap.setLoggedInUserImage(userImg);

                // 로그인 성공 시 메인 페이지 표시

                Login.this.dispose(); // 로그인 창 닫기
                MainPage mainPage = new MainPage();
                mainPage.setVisible(true);
             } else if (loginSuccess() == 1) {
                // 로그인 실패 시 메시지 표시
                JOptionPane.showMessageDialog(Login.this, "비밀번호가 올바르지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
             } else {
                JOptionPane.showMessageDialog(Login.this, "아이디가 존재하지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);

             }
          }
       });

    }

    // 로그인 성공 여부를 확인하는 메서드
    private int loginSuccess() {
       String id = idField.getText();
       String pw = new String(pwField.getPassword());
       int a = 0;

       Connection con = null;
       PreparedStatement psmt = null;
       ResultSet rs = null;

       try {
          con = Jdbc.get();
          String query = "call checkUser(?, ?, @result)";
          psmt = con.prepareStatement(query);
          psmt.setString(1, id);
          psmt.setString(2, pw);
          rs = psmt.executeQuery();
          if (rs.next()) {

             if (rs.getInt("result") == 2) {
                a = 2;
                loggedInUserNo = rs.getInt("user_no");
                String userImg = rs.getString("user_img");
                UserManager.setUserImage(loggedInUserNo, userImg); // 사용자 이미지 설정
                MainTap.setLoggedInUserImage(userImg); // MainTap 클래스에 사용자 이미지 설정
                MyPage.setLoggedInUserNo(loggedInUserNo); // MainTap 클래스에 사용자 설정
                CrewMake.setLoggedInUserNo(loggedInUserNo); // MainTap 클래스에 사용자 설정
                BoardMake.setLoggedInUserNo(loggedInUserNo); //BoardMake 클래스에 사용자 설정
             } else if (rs.getInt("result") == 1) {
                System.out.println("비번틀림");
                a = 1;

             }else {
                a = 0;
             }
          }

       } catch (SQLException e) {
          e.printStackTrace();
          // SQL 예외 발생 시 로그인 실패
       } finally {
          // 리소스 해제
          try {
             if (rs != null)
                rs.close();
             if (psmt != null)
                psmt.close();
             if (con != null)
                con.close();
          } catch (SQLException ex) {
             ex.printStackTrace();

          }
       }
       return a;
    }
 }
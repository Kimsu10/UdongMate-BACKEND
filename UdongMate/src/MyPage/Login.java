package MyPage;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import Jdbc.Jdbc;
import Main.MainPage;
import Main.MainTap;
import Main.MyPage;

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
      setSize(500, 300);
      setResizable(false); // â ũ�� ���� �Ұ����ϵ��� ����
      setTitle("�α���");

      getContentPane().setLayout(null); // AbsoluteLayout ���

      // "gong gothic bold" ��Ʈ ����
      Font font = new Font("gong gothic bold", Font.PLAIN, 14);

      JLabel usernameLabel = new JLabel("���̵� :");
      usernameLabel.setFont(font); // ��Ʈ ����
      usernameLabel.setBounds(96, 76, 80, 20);
      getContentPane().add(usernameLabel);

      idField = new JTextField();
      idField.setFont(font); // ��Ʈ ����
      idField.setBounds(188, 72, 159, 30);
      getContentPane().add(idField);

      JLabel passwordLabel = new JLabel("��й�ȣ :");
      passwordLabel.setFont(font); // ��Ʈ ����
      passwordLabel.setBounds(96, 116, 80, 20);
      getContentPane().add(passwordLabel);

      pwField = new JPasswordField();
      pwField.setFont(font); // ��Ʈ ����
      pwField.setBounds(188, 112, 159, 30);
      pwField.setEchoChar('*');
      getContentPane().add(pwField);

      JButton regButton = new JButton("ȸ������");
      regButton.setFont(font); // ��Ʈ ����
      regButton.setBounds(107, 199, 120, 30);
      getContentPane().add(regButton);

      JButton loginButton = new JButton("�α���");
      loginButton.setFont(font); // ��Ʈ ����
      loginButton.setBounds(252, 199, 120, 30);
      getContentPane().add(loginButton);

      setLocationRelativeTo(null); // ȭ�� �߾ӿ� ��ġ

      regButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Membership m = new Membership();
            m.setVisible(true);
         }
      });

      loginButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            // �α��� ���� ���� Ȯ��
            if (loginSuccess() == 2) {

               String userImg = UserManager.getUserImage(loggedInUserNo);
               MainTap.setLoggedInUserImage(userImg);

               // �α��� ���� �� ���� ������ ǥ��

               Login.this.dispose(); // �α��� â �ݱ�
               MainPage mainPage = new MainPage();
               mainPage.setVisible(true);
            } else if (loginSuccess() == 1) {
               // �α��� ���� �� �޽��� ǥ��
               JOptionPane.showMessageDialog(Login.this, "��й�ȣ�� �ùٸ��� �ʽ��ϴ�.", "�α��� ����", JOptionPane.ERROR_MESSAGE);
            } else {
               JOptionPane.showMessageDialog(Login.this, "���̵� �������� �ʽ��ϴ�.", "�α��� ����", JOptionPane.ERROR_MESSAGE);

            }
         }
      });

   }

   // �α��� ���� ���θ� Ȯ���ϴ� �޼���
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
               UserManager.setUserImage(loggedInUserNo, userImg); // ����� �̹��� ����
               MainTap.setLoggedInUserImage(userImg); // MainTap Ŭ������ ����� �̹��� ����
               MyPage.setLoggedInUserNo(loggedInUserNo); // MainTap Ŭ������ ����� ����

            } else if (rs.getInt("result") == 1) {
               System.out.println("���Ʋ��");
               a = 1;

            }else {
               a = 0;
            }
         }

      } catch (SQLException e) {
         e.printStackTrace();
         // SQL ���� �߻� �� �α��� ����
      } finally {
         // ���ҽ� ����
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

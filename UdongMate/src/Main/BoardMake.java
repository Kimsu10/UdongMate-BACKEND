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
      super("�� �� �ۼ�");

      setSize(550, 450); // â ũ��
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(null); // ���� ���̾ƿ�
      setLocationRelativeTo(null); // â�� ȭ�� �߾ӿ� ��ġ
      
      System.out.println(loggedInUserNo);

      Font font = new Font("Gong Gothic Bold", Font.PLAIN, 15); // ��Ʈ ����
      Iterator<Object> keysIterator = UIManager.getDefaults().keySet().iterator(); // ��� ������Ʈ�� ��Ʈ ����
      while (keysIterator.hasNext()) {
         Object key = keysIterator.next();
         Object value = UIManager.get(key);
         if (value instanceof FontUIResource) {
            UIManager.put(key, font);
         }
      }

      // �󺧰� �Է��ʵ� ����
      JLabel nameLabel = new JLabel("����: ");
      nameLabel.setBounds(10, 20, 80, 25);
      add(nameLabel);

      JTextField titleField = new JTextField();
      titleField.setBounds(69, 20, 380, 25);
      add(titleField);
      JLabel contentLabel = new JLabel("����: ");
      contentLabel.setBounds(10, 50, 80, 25);
      add(contentLabel);

      contentArea = new JTextArea();
      contentArea.setText("�� ���� �ۼ��� �ּ���!");
      contentArea.setForeground(Color.GRAY); // ��Ʈ �ؽ�Ʈ�� ������ ȸ������ ����
      
      contentArea.addFocusListener(new FocusListener() {
         
      
       @Override
         public void focusGained(FocusEvent e) {
             if (contentArea.getText().equals("�� ���� �ۼ��� �ּ���!")) {
                contentArea.setText("");
                contentArea.setForeground(Color.BLACK); // ��Ʈ �ؽ�Ʈ�� ������ ���������� ����
             }
         }

         @Override
         public void focusLost(FocusEvent e) {
             if (contentArea.getText().isEmpty()) {
                contentArea.setText("�� ���� �ۼ��� �ּ���!");
                contentArea.setForeground(Color.GRAY); // ��Ʈ �ؽ�Ʈ�� ������ ȸ������ ����
             }
         }
     });

      contentArea.setBounds(69, 50, 400, 250);
      add(contentArea);
      contentArea.setLineWrap(true); // �ڵ� �� �ٲ� ����

      JLabel boTypeLabel = new JLabel("�� ����: ");
      boTypeLabel.setBounds(10, 310, 80, 25);
      add(boTypeLabel);

      String[] boTypes = {"ȫ����", "���"};
      boTypeDropdown = new JComboBox<>(boTypes);
      boTypeDropdown.setBounds(69, 310, 176, 25);
      add(boTypeDropdown);

      JButton registerButton = new JButton("�Խñ� ����ϱ�");
      registerButton.setBounds(180, 350, 200, 25);
      registerButton.setBackground(Color.white);

      registerButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // �Էµ� �Խñ� ���� ��������
            String boTitle = titleField.getText();
            String boContent = contentArea.getText();
            String boType = (String) boTypeDropdown.getSelectedItem();
            int boTypeno = boType.equals("ȫ����") ? 1 : 2;
            // �Խñ� ������ crew ���̺� �����ϱ� ���� INSERT ���� �ۼ�
            String query = "CALL createBoard(?, ?, ?, ?);";
            try (
                  Connection con = Jdbc.get(); 
                  PreparedStatement psmt = con.prepareStatement(query)) {

               // ������ ?�� �� �Ҵ�
               psmt.setString(1, boTitle);
               psmt.setString(2, boContent);
               psmt.setInt(3, boTypeno);
               psmt.setInt(4, loggedInUserNo);
               
               // ���� ����
               int rowsInserted = psmt.executeUpdate();
               
               if (rowsInserted == 1) {
                  JOptionPane.showMessageDialog(BoardMake.this, "�Խñ��� ��ϵǾ����ϴ�.");
                  // ũ�� ���� �Ϸ� �� ó���� ���� �߰�
                  BoardMake.this.dispose();
               } else {
                  JOptionPane.showMessageDialog(BoardMake.this, "�Խñ� ��Ͽ� �����Ͽ����ϴ�. �ٽ� �õ����ּ���.");
               }
            } catch (SQLException ex) {
               ex.printStackTrace();
               JOptionPane.showMessageDialog(BoardMake.this, "�Խñ� ��� �� ������ �߻��Ͽ����ϴ�. �ٽ� �õ����ּ���.");
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
      // ũ�縦 ������ ������� user_no�� ���ڷ� �����Ͽ� CrewMake ��ü ����
     
      new BoardMake();
   }
}
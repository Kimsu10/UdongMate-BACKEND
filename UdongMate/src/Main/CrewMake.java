package Main;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import Jdbc.*;

public class CrewMake extends JFrame {

   private static int loggedInUserNo;
   private JComboBox<String> addressDropdown;
   private Map<String, Integer> addressMap; // �ּҸ�� �ּҹ�ȣ�� �����ϱ� ���� ��
   private JButton checkDuplicateButton;
   private boolean isIdChecked = false; // ���̵� �ߺ� Ȯ�� ���θ� ��Ÿ���� ���� �߰�
   private JTextArea introArea;

   public CrewMake() {
      super("ũ�� ����");

      setSize(310, 260); // â ũ��
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(null); // ���� ���̾ƿ�
      setLocationRelativeTo(null); // â�� ȭ�� �߾ӿ� ��ġ

      addressMap = new HashMap<>();
      populateAddressMap();
      
      System.out.println(loggedInUserNo);

      Font font = new Font("Gong Gothic bold", Font.PLAIN, 12); // ��Ʈ ����
      Iterator<Object> keysIterator = UIManager.getDefaults().keySet().iterator(); // ��� ������Ʈ�� ��Ʈ ����
      while (keysIterator.hasNext()) {
         Object key = keysIterator.next();
         Object value = UIManager.get(key);
         if (value instanceof FontUIResource) {
            UIManager.put(key, font);
         }
      }

      // �󺧰� �Է��ʵ� ����
      JLabel nameLabel = new JLabel("ũ���: ");
      nameLabel.setBounds(10, 20, 80, 25);
      add(nameLabel);

      JTextField nameField = new JTextField();
      nameField.setBounds(69, 20, 94, 25);
      add(nameField);

      checkDuplicateButton = new JButton("�ߺ�üũ"); // �ߺ�üũ ��ư ����
      checkDuplicateButton.setBounds(170, 21, 75, 25);
      Font buttonFont = checkDuplicateButton.getFont();
      checkDuplicateButton.setFont(buttonFont.deriveFont(Font.PLAIN, 10)); // ��Ʈ ũ�� ����
      add(checkDuplicateButton);

      checkDuplicateButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            String crewName = nameField.getText();
            if (crewName.isEmpty()) {
               JOptionPane.showMessageDialog(CrewMake.this, "ũ����� �Է��ϼ���.");
            } else if (isIdDuplicate(crewName)) {
               JOptionPane.showMessageDialog(CrewMake.this, "�̹� �����ϴ� ũ����Դϴ�.");
            } else {
               JOptionPane.showMessageDialog(CrewMake.this, "��� ������ ũ����Դϴ�.");
               isIdChecked = true; // ũ��� �ߺ� Ȯ�� �Ϸ�
            }
         }
      });

      JLabel introLabel = new JLabel("�Ұ���: ");
      introLabel.setBounds(10, 50, 80, 25);
      add(introLabel);

      introArea = new JTextArea();
      introArea.setText("ũ�縦 ������ �Ұ����ּ���!");
      introArea.setForeground(Color.GRAY); // ��Ʈ �ؽ�Ʈ�� ������ ȸ������ ����
      
      introArea.addFocusListener(new FocusListener() {
         
      
       @Override
         public void focusGained(FocusEvent e) {
             if (introArea.getText().equals("ũ�縦 ������ �Ұ����ּ���!")) {
                 introArea.setText("");
                 introArea.setForeground(Color.BLACK); // ��Ʈ �ؽ�Ʈ�� ������ ���������� ����
             }
         }

         @Override
         public void focusLost(FocusEvent e) {
             if (introArea.getText().isEmpty()) {
                 introArea.setText("ũ�縦 ������ �Ұ����ּ���!");
                 introArea.setForeground(Color.GRAY); // ��Ʈ �ؽ�Ʈ�� ������ ȸ������ ����
             }
         }
     });

      introArea.setBounds(69, 50, 174, 73);
      add(introArea);
      introArea.setLineWrap(true); // �ڵ� �� �ٲ� ����

      JLabel addressLabel = new JLabel("����: ");
      addressLabel.setBounds(10, 130, 80, 25);
      add(addressLabel);

      String[] addresses = { "���α�", "�߱�", "��걸", "������", "������", "���빮��", "�߶���", "���ϱ�", "���ϱ�", "������", "�����", "����",
            "���빮��", "������", "��õ��", "������", "���α�", "��õ��", "��������", "���۱�", "���Ǳ�", "���ʱ�", "������", "���ı�", "������" };
      addressDropdown = new JComboBox<>(addresses);
      addressDropdown.setBounds(69, 130, 176, 25);
      add(addressDropdown);

      JButton registerButton = new JButton("�����ϱ�");
      registerButton.setBounds(99, 171, 100, 25);

      registerButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // �Էµ� ũ�� ���� ��������
            String crewName = nameField.getText();
            String intro = introArea.getText();
            String addressName = (String) addressDropdown.getSelectedItem();
            Integer addressNo = addressMap.get(addressName);

            if (!isIdChecked) { // ũ��� �ߺ� Ȯ���� ���� �ʾ��� ���
               JOptionPane.showMessageDialog(CrewMake.this, "ũ��� �ߺ� ���θ� Ȯ�����ּ���!");
               return; // ũ�� ���� �ߴ�
            }

            // ũ�� ������ crew ���̺� �����ϱ� ���� INSERT ���� �ۼ�
            String query = "CALL CreateCrew(?, ?, ?, ?);";

            try (
                  Connection con = Jdbc.get(); 
                  PreparedStatement psmt = con.prepareStatement(query)) {

               // ������ ?�� �� �Ҵ�
               psmt.setInt(1, loggedInUserNo);
               psmt.setString(2, crewName);
               psmt.setString(3, intro);
               psmt.setInt(4, addressNo);

               // ���� ����
               int rowsInserted = psmt.executeUpdate();
               
               if (rowsInserted == 1) {
                  JOptionPane.showMessageDialog(CrewMake.this, "ũ�� ������ �Ϸ�Ǿ����ϴ�.");
                  // ũ�� ���� �Ϸ� �� ó���� ���� �߰�
                  CrewMake.this.dispose();
               } else {
                  JOptionPane.showMessageDialog(CrewMake.this, "ũ�� ������ �����Ͽ����ϴ�. �ٽ� �õ����ּ���.");
               }
            } catch (SQLException ex) {
               ex.printStackTrace();
               JOptionPane.showMessageDialog(CrewMake.this, "ũ�� ���� �� ������ �߻��Ͽ����ϴ�. �ٽ� �õ����ּ���.");
            }
         }
      });
      add(registerButton);

      setVisible(true);
   }

   private boolean isIdDuplicate(String crewName) {
      try (Connection con = Jdbc.get();
            PreparedStatement psmt = con
                  .prepareStatement("SELECT COUNT(*) AS count FROM crew WHERE crew_name = ?")) {
         psmt.setString(1, crewName);
         try (ResultSet rs = psmt.executeQuery()) {
            if (rs.next()) {
               int count = rs.getInt("count");
               return count > 0; // �ߺ��� ��� true ��ȯ
            }
         }
      } catch (SQLException ex) {
         ex.printStackTrace();
      }
      return false; // �ߺ����� ���� ��� false ��ȯ
   }

   private void populateAddressMap() {
      String query = "SELECT addr_no, addr_name FROM address";
      try (Connection con = Jdbc.get();
            PreparedStatement psmt = con.prepareStatement(query);
            ResultSet rs = psmt.executeQuery()) {
         while (rs.next()) {
            int addrNo = rs.getInt("addr_no");
            String addrName = rs.getString("addr_name");
            addressMap.put(addrName, addrNo);
         }
      } catch (SQLException e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(this, "�ּ� ������ �������� �� ������ �߻��Ͽ����ϴ�.");
      }
   }

   public static void setLoggedInUserNo(int userNo) {
      loggedInUserNo = userNo;
   }

   public static void main(String[] args) {
      // ũ�縦 ������ ������� user_no�� ���ڷ� �����Ͽ� CrewMake ��ü ����

      new CrewMake();
   }
}

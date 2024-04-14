package Main;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import JDBC.*;

public class Crew extends MainTap {
   private JPanel contentPane;

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               Crew frame = new Crew();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   public Crew() {

      super();
      
      

      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(100, 100, 100, 100)); // Add padding
      contentPane.setLayout(new BorderLayout(0, 10)); // Set vertical gap to 10

      // ��ư�� ������ �г� ����
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

      // ��ư ����
      JButton button = new JButton("ũ�� ����");
      button.setFont(new Font("Gong Gothic Light", Font.PLAIN, 20));
      buttonPanel.add(button); // ��ư�� �гο� �߰�

      button.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // �ڽ� â �̵�
            new CrewMake().setVisible(true);
            
         }
      });

      // ���̺� ���� ��ư �г��� �߰�
      contentPane.add(buttonPanel, BorderLayout.NORTH);


      // Define column names
      String[] columnNames = { "ũ���", "ũ�� �Ұ���", "����" };

      // Create table model
      DefaultTableModel model = new DefaultTableModel(columnNames, 0);

      try {
         Connection con = Jdbc.get();

         // SQL ����
         String sql = "SELECT crew.crew_name, crew.crew_intro, address.addr_name FROM crew JOIN address ON crew.addr_no = address.addr_no and crew_no<>5";
         PreparedStatement pstmt = con.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();

         // ����� ���̺� �𵨿� �߰�
         while (rs.next()) {
            String crName = rs.getString("crew_name");
            String crewIntro = rs.getString("crew_intro");
            String addrName = rs.getString("addr_name");

            // ���� �߰��Ͽ� �����ͺ��̽����� ������ ���� ���̺� �𵨿� �߰�
            model.addRow(new Object[] { crName, crewIntro, addrName });
         }

         rs.close();
         pstmt.close();
         con.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

      // ���̺� ����
      JTable table = new JTable(model);

      Font cellFont = new Font("Gong Gothic Light", Font.PLAIN, 20);
      table.setFont(cellFont);
      table.setRowHeight(30);

      TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
      table.getTableHeader().setFont(new Font("Gong Gothic Light", Font.PLAIN, 30));
      table.getTableHeader().setBackground(Color.gray);

      // ���̺��� ��輱�� ���� ��ũ�� �гο� �߰�
      JScrollPane scrollPane = new JScrollPane(table);
      scrollPane.setBorder(BorderFactory.createEmptyBorder()); // ��ũ�� �г��� ��輱 ����
      contentPane.add(scrollPane, BorderLayout.CENTER);

      setContentPane(contentPane);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);

      // ���̺� ���콺 ������ �߰�
      table.addMouseListener(new java.awt.event.MouseAdapter() {
         @Override
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = table.rowAtPoint(evt.getPoint());
            int col = table.columnAtPoint(evt.getPoint());
            if (row >= 0 && col >= 0) {
               // Ŭ�� �̺�Ʈ ó��
               String crewName = (String) table.getValueAt(row, 0);
               displayGroundDetails(crewName);
            }
         }
      });
   }

   private void displayGroundDetails(String crewName) {
        JFrame frame = new JFrame("Details - " + crewName);
        frame.setSize(600, 200);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE); // ���� ������� ����

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE); // ���� ������� ����

        try {
            Connection con = Jdbc.get();

            String sql = "SELECT crew_name, crew_intro, addr_name, user_nick, user_phone FROM user, user_crew, crew, address WHERE user.user_no=user_crew.user_no AND user_crew.crew_no=crew.crew_no AND address.addr_no=crew.addr_no AND crew_cap=1 AND crew.crew_name=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, crewName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String crName = rs.getString("crew_name");
                String crewIntro = rs.getString("crew_intro");
                String addrName = rs.getString("addr_name");
                String userNick = rs.getString("user_nick");
                String userPhone = rs.getString("user_phone");

                JPanel infoPanel = new JPanel(new BorderLayout());
                infoPanel.setBackground(Color.WHITE); // ���� ������� ����

                DefaultTableModel detailModel = new DefaultTableModel();
                detailModel.addColumn(crName);
                
                detailModel.addRow(new Object[]{"ũ�� �Ұ� : " + crewIntro});
                detailModel.addRow(new Object[]{"���� : " + addrName});
                detailModel.addRow(new Object[]{"ũ���� : " + userNick});
                detailModel.addRow(new Object[]{"ũ���� ��ȣ : " + userPhone});
                

                JTable detailTable = new JTable(detailModel);
                detailTable.setFont(new Font("Gong Gothic Light", Font.PLAIN, 20));
                detailTable.setRowHeight(30);
                detailTable.setShowGrid(false); // �׵θ� ���ֱ�
                
                TableCellRenderer headerRenderer = detailTable.getTableHeader().getDefaultRenderer();
                detailTable.getTableHeader().setFont(new Font("Gong Gothic Light", Font.PLAIN, 30));
                detailTable.getTableHeader().setBackground(Color.white);
                JScrollPane detailScrollPane = new JScrollPane(detailTable);
                detailScrollPane.setBorder(BorderFactory.createEmptyBorder());
                detailScrollPane.getViewport().setBackground(new Color(0, 0, 0, 0)); // ���� ���� ����
                infoPanel.add(detailScrollPane, BorderLayout.NORTH);

                detailsPanel.add(infoPanel, BorderLayout.CENTER);

             
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.add(detailsPanel);
        frame.setVisible(true);
    }
    
    
}
package Main;

import java.awt.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import Jdbc.*;

public class Mountain extends MainTap {
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Mountain frame = new Mountain();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Mountain() {
        super();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(100, 100, 100, 100)); // Add padding
        contentPane.setLayout(new BorderLayout(0, 10)); // Set vertical gap to 10


        // Define column names
        String[] columnNames = {"��", "�ڽ� ����", "����", "���̵�"};

        // Create table model
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            Connection con = Jdbc.get();

            // SQL ����
            String sql = "SELECT mountain.mt_name, mountain.mt_dist, address.addr_name, mountain.lev_no FROM mountain JOIN address ON mountain.addr_no = address.addr_no";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // ����� ���̺� �𵨿� �߰�
            while (rs.next()) {
                String mtName = rs.getString("mt_name");
                String addrName = rs.getString("addr_name");
                int levNo = rs.getInt("lev_no");
                int mtDist = rs.getInt("mt_dist");
                double Number = Math.round(mtDist * 10) / 10.0;
                // ���� ��ȣ�� ���� ���ڿ��� ����
                String level;
                switch (levNo) {
                    case 1:
                        level = "��";
                        break;
                    case 2:
                        level = "��";
                        break;
                    case 3:
                        level = "��";
                        break;
                    default:
                        level = "�� �� ����";
                }

                // ���� �߰��Ͽ� �����ͺ��̽����� ������ ���� ���̺� �𵨿� �߰�
                model.addRow(new Object[]{mtName, Number + "km", addrName, level});
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create table
        JTable table = new JTable(model);
        
        Font cellFont = new Font("Gong Gothic Light", Font.PLAIN, 20);
        table.setFont(cellFont);
        table.setRowHeight(30);
        
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        table.getTableHeader().setFont(new Font("Gong Gothic Light", Font.PLAIN, 30));
        table.getTableHeader().setBackground(Color.gray);

        // Add the table to a scroll pane with no border
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border from scroll pane
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
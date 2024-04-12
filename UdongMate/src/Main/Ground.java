package Main;

import java.awt.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import Jdbc.*;

public class Ground extends MainTap {
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                   Ground frame = new Ground();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Ground() {
        super();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(100, 100, 100, 100)); // Add padding
        contentPane.setLayout(new BorderLayout(0, 10)); // Set vertical gap to 10


        // Define column names
        String[] columnNames = {"��å/����", "�ڽ� ����", "����", "���̵�", "������", "�ְߵ���"};

        // Create table model
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            Connection con = Jdbc.get();

            // SQL ����
            String sql = "SELECT ground.gr_name, ground.gr_dist, address.addr_name, ground.lev_no, ground.gr_cycleok, ground.gr_dogok FROM ground JOIN address ON ground.addr_no = address.addr_no";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // ����� ���̺� �𵨿� �߰�
            while (rs.next()) {
                String grName = rs.getString("gr_name");
                String addrName = rs.getString("addr_name");
                int levNo = rs.getInt("lev_no");
                int grDist = rs.getInt("gr_dist");
                int cycleOK_num = rs.getInt("gr_cycleok");
                int dogOK_num = rs.getInt("gr_dogok");
                double Number = Math.round(grDist * 10) / 10.0;
                // ���� ��ȣ�� ���� ���ڿ��� ����
                String level;
                String cycleOk;
                String dogOk;
                switch (cycleOK_num) {
                    case 0:
                       cycleOk = "�Ұ���";
                        break;
                    case 1:
                       cycleOk = "����";
                        break;
                    case 2:
                       cycleOk = "�Ϻκ� ����";
                        break;
                    default:
                       cycleOk = "�� �� ����";
                }
                
                switch (dogOK_num) {
                case 0:
                   dogOk = "�Ұ���";
                    break;
                case 1:
                   dogOk = "����";
                    break;
                default:
                   dogOk = "�� �� ����";
                }
                
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
                model.addRow(new Object[]{grName, Number + "km", addrName, level, cycleOk, dogOk});
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
package Main;

import java.awt.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import JDBC.*;

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
        contentPane.setBorder(new EmptyBorder(100, 100, 100, 100)); // ���� �߰�
        contentPane.setLayout(new BorderLayout(0, 10)); // ���� ���� 10���� ����

        // �� �̸� ����
        String[] columnNames = {"��å/����", "�ڽ� ����", "����", "���̵�", "������", "�ݷ��� ����"};

        // ���̺� �� ����
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            Connection con = Jdbc.get();

            // SQL ����
            String sql = "SELECT ground.gr_name, ground.gr_dist, address.addr_name, ground.lev_no, ground.gr_cycleok, ground.gr_dogok, ground.gr_img, ground.gr_info FROM ground JOIN address ON ground.addr_no = address.addr_no ORDER BY gr_dist ASC";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // ����� ���̺� �𵨿� �߰�
            while (rs.next()) {
                String grName = rs.getString("gr_name");
                String addrName = rs.getString("addr_name");
                int levNo = rs.getInt("lev_no");
                double grDist = rs.getDouble("gr_dist");
                int cycleOK_num = rs.getInt("gr_cycleok");
                int dogOK_num = rs.getInt("gr_dogok");
                double Number = Math.round(grDist * 10) / 10.0;
                // ���� ��ȣ�� ���� ���ڿ� ����
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

                // �����ͺ��̽����� ������ ���� ���̺� �𵨿� �߰�
                model.addRow(new Object[]{grName, Number + "km", addrName, level, cycleOk, dogOk});
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ���̺� ����
        JTable table = new JTable(model) {
           @Override
           public boolean isCellEditable(int row, int column){
               return false;
            }
        };
        
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
                    String groundName = (String) table.getValueAt(row, 0);
                    displayGroundDetails(groundName);
                }
            }
        });
    }


    private void displayGroundDetails(String groundName) {
        JFrame frame = new JFrame("Details - " + groundName);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE); // ���� ������� ����

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE); // ���� ������� ����

        try {
            Connection con = Jdbc.get();

            String sql = "SELECT * FROM ground JOIN address ON ground.addr_no = address.addr_no WHERE ground.gr_name = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, groundName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String grName = rs.getString("gr_name");
                String addrName = rs.getString("addr_name");
                int levNo = rs.getInt("lev_no");
                int grDist = rs.getInt("gr_dist");
                int cycleOK_num = rs.getInt("gr_cycleok");
                int dogOK_num = rs.getInt("gr_dogok");
                String grImg = rs.getString("gr_img");
                String grInfo = rs.getString("gr_info");
                double distance = Math.round(grDist * 10) / 10.0;

                JPanel infoPanel = new JPanel(new BorderLayout());
                infoPanel.setBackground(Color.WHITE); // ���� ������� ����

                DefaultTableModel detailModel = new DefaultTableModel();
                detailModel.addColumn(grName);
                
                detailModel.addRow(new Object[]{"�ּ� : " + addrName});
                detailModel.addRow(new Object[]{"������ ���� ���� : " + (cycleOK_num == 1 ? "����" : "�Ұ���")});
                detailModel.addRow(new Object[]{"�ݷ����� ���� ���� ���� : " + (dogOK_num == 1 ? "����" : "�Ұ���")});
                detailModel.addRow(new Object[]{"�Ÿ� : " + (distance + " km")});
                detailModel.addRow(new Object[]{"�ڽ� �Ұ�"});

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

                JTextArea infoTextArea = new JTextArea(grInfo);
                infoTextArea.setOpaque(false); // ���� ���� ����
                infoTextArea.setLineWrap(true);
                infoTextArea.setWrapStyleWord(true);
                infoTextArea.setEditable(false);
                infoTextArea.setFont(new Font("Gong Gothic Light", Font.PLAIN, 18)); // ��Ʈ ����

                // infoTextArea�� detailTable ���� ���� ����
                detailScrollPane.setPreferredSize(new Dimension(detailScrollPane.getWidth(), 190));
                infoPanel.add(infoTextArea, BorderLayout.CENTER);

                detailsPanel.add(infoPanel, BorderLayout.CENTER);

                JLabel imgLabel = new JLabel();
                if (grImg != null && !grImg.isEmpty()) {
                    ImageIcon icon = new ImageIcon(grImg);
                    Image image = icon.getImage();
                    Image scaledImage = image.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    imgLabel.setIcon(scaledIcon);
                } else {
                    imgLabel.setText("�̹����� �������� �ʽ��ϴ�.");
                }
                imgLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                detailsPanel.add(imgLabel, BorderLayout.WEST); // �̹����� ���� ��ܿ� �߰�
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
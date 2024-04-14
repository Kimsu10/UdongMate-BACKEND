package Main;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import JDBC.*;

public class Mountain extends MainTap {
    private JPanel contentPane;
    private JComboBox<String> addrDropdown;
    private JComboBox<String> levDropdown;

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
        setLocationRelativeTo(null);
         contentPane = new JPanel();
         contentPane.setBorder(new EmptyBorder(100, 100, 100, 100)); // Add padding
         contentPane.setLayout(new BorderLayout(0, 10)); // Set vertical gap to 10


         // Define column names
         String[] columnNames = {"��", "�ڽ� ����", "����", "���̵�"};

         // Create table model
         DefaultTableModel model = new DefaultTableModel(columnNames, 0);

         try {
             Connection con = Jdbc.get();
             
             JPanel dropPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 210, 10)); // FlowLayout�� ���� ����� LEADING���� ����

             JPanel addrPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // FlowLayout�� ���� ����� LEADING���� ����
             JLabel addrLabel = new JLabel("����");
             addrPanel.add(addrLabel);
             String addr[] = {"���þ���", "���α�", "�߱�", "��걸", "������", "������", "���빮��", "�߶���", "���ϱ�",
                     "���ϱ�", "������", "�����", "����", "���빮��", "������", "��õ��", "������",
                     "���α�", "��õ��", "��������", "���۱�", "���Ǳ�", "���ʱ�", "������", "���ı�", "������"};
             addrDropdown = new JComboBox<>(addr);
             addrPanel.add(addrDropdown);

             JPanel levPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // FlowLayout�� ���� ����� LEADING���� ����
             JLabel levLabel = new JLabel("���̵�");
             levPanel.add(levLabel);
             String lev[] = {"���þ���", "��", "��", "��"};
             levDropdown = new JComboBox<>(lev);
             levPanel.add(levDropdown);

             JButton searchButton = new JButton("�˻�");
             searchButton.setPreferredSize(new Dimension(70, 30)); // ��ư ũ�� ����

             dropPanel.add(addrPanel);
             dropPanel.add(levPanel);
             dropPanel.add(searchButton); // ��ư �߰�
             
             searchButton.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent a) {
                     // ���õ� ���ǵ��� �����ɴϴ�.
                     String selectedAddr = (String) addrDropdown.getSelectedItem();
                     String selectedLev = (String) levDropdown.getSelectedItem(); // ���õ� ���̵� ��������
                     

                     try {
                         Connection con = Jdbc.get();

                         // SQL ����
                         String sql = "SELECT * FROM mountain JOIN address ON mountain.addr_no = address.addr_no ";
                         String s[][] = {
                               {"addr_name = ? "}, {"lev_no = ? "}
                         };
                         int c[]=  {0, 0};
                         
                         
                         
                         if(selectedAddr != "���þ���") c[0]++;
                         if(selectedLev != "���þ���") c[1]++;
                         
                         if(c[0] == 1 || c[1] == 1) {
                            sql += "WHERE ";
                         }
                         
                         int cnt = 0;
                        
                         for(int i = 0;i < 2;i++) {
                            if(c[i] == 1) cnt++;
                         }
                         
                         for(int i = 0;i < 2;i++) {
                            for(int j = 0;j < s[i].length;j++) {
                               if(c[i] == 1) {
                                  sql += s[i][j];
                                  
                                  if(cnt > 1) {
                                       sql += "AND ";
                                       cnt--;
                                    }
                                  
                               }else break;
                            }
            
                         }
                         
                         
                         sql += "ORDER BY mt_dist ASC";

                         
                         PreparedStatement pstmt = con.prepareStatement(sql);
                         
                         for(int i = 1;i <= 2;i++) {
                            if(c[0] == 1) {
                               pstmt.setString(i, selectedAddr);
                               c[0] = 0;
                               continue;
                            }
                            if(c[1] == 1) {
                               pstmt.setInt(i, getLevNo(selectedLev));
                               c[1] = 0;
                               continue;
                            }
                            
                         }

                         ResultSet rs = pstmt.executeQuery();

                         // ���� ���̺� �� �ʱ�ȭ
                         model.setRowCount(0);

                         // ����� ���̺� �𵨿� �߰�
                         while (rs.next()) {
                             // ������ �ڵ�� �����մϴ�.
                            String mtName = rs.getString("mt_name");
                             String addrName = rs.getString("addr_name");
                             int levNo = rs.getInt("lev_no");
                             int mtDist = rs.getInt("mt_dist");
                             double distance = Math.round(mtDist * 10) / 10.0;

                             // ���� ��ȣ�� ���� ���ڿ� ����
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

                             // �����ͺ��̽����� ������ ���� ���̺� �𵨿� �߰�
                             model.addRow(new Object[]{mtName, distance + "km", addrName, level});
                         }

                         rs.close();
                         pstmt.close();
                         con.close();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             });
             

             

             JScrollPane checkBoxScrollPane = new JScrollPane(dropPanel);
             contentPane.add(checkBoxScrollPane, BorderLayout.NORTH);

             
             // SQL ����
             String sql = "SELECT * FROM mountain JOIN address ON mountain.addr_no = address.addr_no";
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();

             // ����� ���̺� �𵨿� �߰�.
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
                    String mountainName = (String) table.getValueAt(row, 0);
                    displayGroundDetails(mountainName);
                }
            }
        });
    }


    private void displayGroundDetails(String mountainName) {
        JFrame frame = new JFrame("Details - " + mountainName);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE); // ���� ������� ����

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE); // ���� ������� ����

        try {
            Connection con = Jdbc.get();

            String sql = "SELECT * FROM mountain JOIN address ON mountain.addr_no = address.addr_no WHERE mountain.mt_name = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mountainName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String mtName = rs.getString("mt_name");
                String addrName = rs.getString("addr_name");
                int levNo = rs.getInt("lev_no");
                int mtDist = rs.getInt("mt_dist");
                String mtImg = rs.getString("mt_img");
                String mtInfo = rs.getString("mt_info");
                double distance = Math.round(mtDist * 10) / 10.0;

                JPanel infoPanel = new JPanel(new BorderLayout());
                infoPanel.setBackground(Color.WHITE); // ���� ������� ����

                DefaultTableModel detailModel = new DefaultTableModel();
                detailModel.addColumn(mtName);
                
                detailModel.addRow(new Object[]{"�ּ� : " + addrName});
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

                JTextArea infoTextArea = new JTextArea(mtInfo);
                infoTextArea.setOpaque(false); // ���� ���� ����
                infoTextArea.setLineWrap(true);
                infoTextArea.setWrapStyleWord(true);
                infoTextArea.setEditable(false);
                infoTextArea.setFont(new Font("Gong Gothic Light", Font.PLAIN, 18)); // ��Ʈ ����

                // infoTextArea�� detailTable ���� ���� ����
                detailScrollPane.setPreferredSize(new Dimension(detailScrollPane.getWidth(), 130));
                infoPanel.add(infoTextArea, BorderLayout.CENTER);

                detailsPanel.add(infoPanel, BorderLayout.CENTER);

                JLabel imgLabel = new JLabel();
                if (mtImg != null && !mtImg.isEmpty()) {
                    ImageIcon icon = new ImageIcon(mtImg);
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
    
    private int getLevNo(String lev) {
        switch (lev) {
            case "��":
                return 1;
            case "��":
                return 2;
            case "��":
                return 3;
            default:
                return 0;
        }
    }
}
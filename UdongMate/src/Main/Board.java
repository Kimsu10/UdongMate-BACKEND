package Main;
import java.awt.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import JDBC.*;

public class Board extends MainTap {
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                   Board frame = new Board();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Board() {
        super();
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(100, 100, 100, 100)); // ���� �߰�
        contentPane.setLayout(new BorderLayout(0, 10)); // ���� ���� 10���� ����

        // �� �̸� ����
        String[] columnNames = {"����", "����", "�ۼ�����", "�ۼ��ڸ�"};

        // ���̺� �� ����
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            Connection con = Jdbc.get();

            // SQL ����
            String sql = "SELECT board.board_title, board.board_type, board.board_date, user.user_nick FROM board JOIN user ON board.user_no = user.user_no ORDER BY board_date DESC";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // ����� ���̺� �𵨿� �߰�
            while (rs.next()) {
                String boTitle = rs.getString("board_title");
                int boTypeno = rs.getInt("board_type");
                Date boDate = rs.getDate("board_date");
                String nickName  = rs.getString("user_nick");
                
                String boType = "";
                switch (boTypeno) {
                    case 1:
                       boType = "ȫ����";
                        break;
                    case 2:
                       boType = "���";
                        break;
                    default:
                       boType = "�� �� ����";
                }

                // �����ͺ��̽����� ������ ���� ���̺� �𵨿� �߰�
                model.addRow(new Object[]{boTitle, boType, boDate, nickName});
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
                    String boardTitle = (String) table.getValueAt(row, 0);
                    displayGroundDetails(boardTitle);
                }
            }
        });
    }


    private void displayGroundDetails(String boardTitle) {
        JFrame frame = new JFrame(boardTitle);
        frame.setSize(550, 450);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE); // ���� ������� ����

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE); // ���� ������� ����

        try {
            Connection con = Jdbc.get();

            String sql = "SELECT * FROM board JOIN user ON board.user_no = user.user_no WHERE board.board_title = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, boardTitle);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String boTitle = rs.getString("board_title"); // �Խñ� ����
                String nickName  = rs.getString("user_nick"); // �ۼ��� �г���
                Date boDate = rs.getDate("board_date"); // �ۼ�����
                int boTypeno = rs.getInt("board_type"); // �� ����
                String boCont = rs.getString("board_cont"); // �� ����
                
                String boType = "";
                switch (boTypeno) {
                    case 1:
                       boType = "ȫ����";
                        break;
                    case 2:
                       boType = "���";
                        break;
                    default:
                       boType = "�� �� ����";
                }
                
                JPanel infoPanel = new JPanel(new BorderLayout());
                infoPanel.setBackground(Color.WHITE); // ���� ������� ����

                DefaultTableModel detailModel = new DefaultTableModel();
                detailModel.addColumn(boTitle); 
                
                detailModel.addRow(new Object[]{"�ۼ��� : " + nickName});
                detailModel.addRow(new Object[]{"�ۼ����� : " + boDate});
                detailModel.addRow(new Object[]{"�� ���� : " + boType});
                detailModel.addRow(new Object[]{"����"});

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

                JTextArea infoTextArea = new JTextArea(boCont);
                infoTextArea.setOpaque(false); // ���� ���� ����
                infoTextArea.setLineWrap(true);
                infoTextArea.setWrapStyleWord(true);
                infoTextArea.setEditable(false);
                infoTextArea.setFont(new Font("Gong Gothic Light", Font.PLAIN, 18)); // ��Ʈ ����

                // infoTextArea�� detailTable ���� ���� ����
                detailScrollPane.setPreferredSize(new Dimension(detailScrollPane.getWidth(), 160));
                infoPanel.add(infoTextArea, BorderLayout.CENTER);

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
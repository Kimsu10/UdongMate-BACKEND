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
        String[] columnNames = {"산책/러닝", "코스 길이", "지역", "난이도", "자전거", "애견동반"};

        // Create table model
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            Connection con = Jdbc.get();

            // SQL 쿼리
            String sql = "SELECT ground.gr_name, ground.gr_dist, address.addr_name, ground.lev_no, ground.gr_cycleok, ground.gr_dogok FROM ground JOIN address ON ground.addr_no = address.addr_no";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // 결과를 테이블 모델에 추가
            while (rs.next()) {
                String grName = rs.getString("gr_name");
                String addrName = rs.getString("addr_name");
                int levNo = rs.getInt("lev_no");
                int grDist = rs.getInt("gr_dist");
                int cycleOK_num = rs.getInt("gr_cycleok");
                int dogOK_num = rs.getInt("gr_dogok");
                double Number = Math.round(grDist * 10) / 10.0;
                // 레벨 번호에 따라 문자열을 설정
                String level;
                String cycleOk;
                String dogOk;
                switch (cycleOK_num) {
                    case 0:
                       cycleOk = "불가능";
                        break;
                    case 1:
                       cycleOk = "가능";
                        break;
                    case 2:
                       cycleOk = "일부분 가능";
                        break;
                    default:
                       cycleOk = "알 수 없음";
                }
                
                switch (dogOK_num) {
                case 0:
                   dogOk = "불가능";
                    break;
                case 1:
                   dogOk = "가능";
                    break;
                default:
                   dogOk = "알 수 없음";
                }
                
                switch (levNo) {
                case 1:
                    level = "하";
                    break;
                case 2:
                    level = "중";
                    break;
                case 3:
                    level = "상";
                    break;
                default:
                    level = "알 수 없음";
                }

                // 행을 추가하여 데이터베이스에서 가져온 값을 테이블 모델에 추가
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
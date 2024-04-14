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
         String[] columnNames = {"산", "코스 길이", "지역", "난이도"};

         // Create table model
         DefaultTableModel model = new DefaultTableModel(columnNames, 0);

         try {
             Connection con = Jdbc.get();
             
             JPanel dropPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 210, 10)); // FlowLayout의 정렬 방식을 LEADING으로 설정

             JPanel addrPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // FlowLayout의 정렬 방식을 LEADING으로 설정
             JLabel addrLabel = new JLabel("지역");
             addrPanel.add(addrLabel);
             String addr[] = {"선택안함", "종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구",
                     "강북구", "도봉구", "노원구", "은평구", "서대문구", "마포구", "양천구", "강서구",
                     "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", "강동구"};
             addrDropdown = new JComboBox<>(addr);
             addrPanel.add(addrDropdown);

             JPanel levPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // FlowLayout의 정렬 방식을 LEADING으로 설정
             JLabel levLabel = new JLabel("난이도");
             levPanel.add(levLabel);
             String lev[] = {"선택안함", "하", "중", "상"};
             levDropdown = new JComboBox<>(lev);
             levPanel.add(levDropdown);

             JButton searchButton = new JButton("검색");
             searchButton.setPreferredSize(new Dimension(70, 30)); // 버튼 크기 설정

             dropPanel.add(addrPanel);
             dropPanel.add(levPanel);
             dropPanel.add(searchButton); // 버튼 추가
             
             searchButton.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent a) {
                     // 선택된 조건들을 가져옵니다.
                     String selectedAddr = (String) addrDropdown.getSelectedItem();
                     String selectedLev = (String) levDropdown.getSelectedItem(); // 선택된 난이도 가져오기
                     

                     try {
                         Connection con = Jdbc.get();

                         // SQL 쿼리
                         String sql = "SELECT * FROM mountain JOIN address ON mountain.addr_no = address.addr_no ";
                         String s[][] = {
                               {"addr_name = ? "}, {"lev_no = ? "}
                         };
                         int c[]=  {0, 0};
                         
                         
                         
                         if(selectedAddr != "선택안함") c[0]++;
                         if(selectedLev != "선택안함") c[1]++;
                         
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

                         // 기존 테이블 모델 초기화
                         model.setRowCount(0);

                         // 결과를 테이블 모델에 추가
                         while (rs.next()) {
                             // 나머지 코드는 동일합니다.
                            String mtName = rs.getString("mt_name");
                             String addrName = rs.getString("addr_name");
                             int levNo = rs.getInt("lev_no");
                             int mtDist = rs.getInt("mt_dist");
                             double distance = Math.round(mtDist * 10) / 10.0;

                             // 레벨 번호에 따라 문자열 설정
                             String level;
                                                    

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

                             // 데이터베이스에서 가져온 값을 테이블 모델에 추가
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

             
             // SQL 쿼리
             String sql = "SELECT * FROM mountain JOIN address ON mountain.addr_no = address.addr_no";
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();

             // 결과를 테이블 모델에 추가.
             while (rs.next()) {
                 String mtName = rs.getString("mt_name");
                 String addrName = rs.getString("addr_name");
                 int levNo = rs.getInt("lev_no");
                 int mtDist = rs.getInt("mt_dist");
                 double Number = Math.round(mtDist * 10) / 10.0;
                 // 레벨 번호에 따라 문자열을 설정
                 String level;
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
                 model.addRow(new Object[]{mtName, Number + "km", addrName, level});
             }

             rs.close();
             pstmt.close();
             con.close();
         } catch (Exception e) {
             e.printStackTrace();
         }


        // 테이블 생성
        JTable table = new JTable(model);

        Font cellFont = new Font("Gong Gothic Light", Font.PLAIN, 20);
        table.setFont(cellFont);
        table.setRowHeight(30);

        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        table.getTableHeader().setFont(new Font("Gong Gothic Light", Font.PLAIN, 30));
        table.getTableHeader().setBackground(Color.gray);

        // 테이블을 경계선이 없는 스크롤 패널에 추가
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // 스크롤 패널의 경계선 제거
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 테이블에 마우스 리스너 추가
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    // 클릭 이벤트 처리
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
        frame.getContentPane().setBackground(Color.WHITE); // 배경색 흰색으로 설정

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정

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
                infoPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정

                DefaultTableModel detailModel = new DefaultTableModel();
                detailModel.addColumn(mtName);
                
                detailModel.addRow(new Object[]{"주소 : " + addrName});
                detailModel.addRow(new Object[]{"거리 : " + (distance + " km")});
                detailModel.addRow(new Object[]{"코스 소개"});

                JTable detailTable = new JTable(detailModel);
                detailTable.setFont(new Font("Gong Gothic Light", Font.PLAIN, 20));
                detailTable.setRowHeight(30);
                detailTable.setShowGrid(false); // 테두리 없애기
                
                TableCellRenderer headerRenderer = detailTable.getTableHeader().getDefaultRenderer();
                detailTable.getTableHeader().setFont(new Font("Gong Gothic Light", Font.PLAIN, 30));
                detailTable.getTableHeader().setBackground(Color.white);
                JScrollPane detailScrollPane = new JScrollPane(detailTable);
                detailScrollPane.setBorder(BorderFactory.createEmptyBorder());
                detailScrollPane.getViewport().setBackground(new Color(0, 0, 0, 0)); // 배경색 투명 설정
                infoPanel.add(detailScrollPane, BorderLayout.NORTH);

                JTextArea infoTextArea = new JTextArea(mtInfo);
                infoTextArea.setOpaque(false); // 배경색 투명 설정
                infoTextArea.setLineWrap(true);
                infoTextArea.setWrapStyleWord(true);
                infoTextArea.setEditable(false);
                infoTextArea.setFont(new Font("Gong Gothic Light", Font.PLAIN, 18)); // 폰트 변경

                // infoTextArea와 detailTable 사이 간격 조정
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
                    imgLabel.setText("이미지가 존재하지 않습니다.");
                }
                imgLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                detailsPanel.add(imgLabel, BorderLayout.WEST); // 이미지를 왼쪽 상단에 추가
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
            case "하":
                return 1;
            case "중":
                return 2;
            case "상":
                return 3;
            default:
                return 0;
        }
    }
}
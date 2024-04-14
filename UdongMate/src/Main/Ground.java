package Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.*;

import Jdbc.*;

public class Ground extends MainTap {
    private JPanel contentPane;
    private JComboBox<String> addrDropdown;
    private JComboBox<String> levDropdown;
    private JComboBox<String> cycleDropdown;
    private JComboBox<String> dogDropdown;

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
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(100, 100, 100, 100)); // 여백 추가
        contentPane.setLayout(new BorderLayout(0, 10)); // 수직 간격 10으로 설정

        // Create JPanel for checkboxes
        JPanel dropPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 80, 10)); // FlowLayout의 정렬 방식을 LEADING으로 설정

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

        JPanel cyclePanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // FlowLayout의 정렬 방식을 LEADING으로 설정
        JLabel cycleLabel = new JLabel("자전거 이용");
        cyclePanel.add(cycleLabel);
        String cycle[] = {"선택안함", "가능", "일부분 가능", "불가능"};
        cycleDropdown = new JComboBox<>(cycle);
        cyclePanel.add(cycleDropdown);

        JPanel dogPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // FlowLayout의 정렬 방식을 LEADING으로 설정
        JLabel dogLabel = new JLabel("반려동물 출입");
        dogPanel.add(dogLabel);
        String dog[] = {"선택안함", "가능", "불가능"};
        dogDropdown = new JComboBox<>(dog);
        dogPanel.add(dogDropdown);

        dropPanel.add(addrPanel);
        dropPanel.add(levPanel);
        dropPanel.add(cyclePanel);
        dropPanel.add(dogPanel);

        JButton searchButton = new JButton("검색");
        searchButton.setPreferredSize(new Dimension(70, 30)); // 버튼 크기 설정
        dropPanel.add(searchButton); // 버튼 추가

        JScrollPane checkBoxScrollPane = new JScrollPane(dropPanel);
        contentPane.add(checkBoxScrollPane, BorderLayout.NORTH);

        // 열 이름 정의
        String[] columnNames = {"산책/러닝", "코스 길이", "지역", "난이도", "자전거", "반려견 가능"};

        // 테이블 모델 생성
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                // 선택된 조건들을 가져옵니다.
                String selectedAddr = (String) addrDropdown.getSelectedItem();
                String selectedLev = (String) levDropdown.getSelectedItem(); // 선택된 난이도 가져오기
                String selectedCycle = (String) cycleDropdown.getSelectedItem();
                String selectedDog = (String) dogDropdown.getSelectedItem();

                try {
                    Connection con = Jdbc.get();

                    // SQL 쿼리
                    String sql = "SELECT * FROM ground JOIN address ON ground.addr_no = address.addr_no ";
                    String s[][] = {
                          {"addr_name = ? "}, {"lev_no = ? "}, {"gr_cycleok = ? "}, {"gr_dogok = ? "}
                    };
                    int c[]=  {0, 0, 0, 0};
                    
                    if(selectedAddr != "선택안함") c[0]++;
                    if(selectedLev != "선택안함") c[1]++;
                    if(selectedCycle != "선택안함") c[2]++;
                    if(selectedDog != "선택안함") c[3]++;

                    if(c[0] == 1 || c[1] == 1 || c[2] == 1 || c[3] == 1) {
                       sql += "WHERE ";
                    }
                    
                    int cnt = 0;
                   
                    for(int i = 0;i < 4;i++) {
                       if(c[i] == 1) cnt++;
                    }
                    
                    for(int i = 0;i < 4;i++) {
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
                    
                    
                    sql += "ORDER BY gr_dist ASC";

                    
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    
                    for(int i = 1;i <= 4;i++) {
                       if(c[0] == 1) {
                          pstmt.setString(i, selectedAddr);
                          c[0] = 0;
                          System.out.println("1");
                          continue;
                       }
                       if(c[1] == 1) {
                          pstmt.setInt(i, getLevNo(selectedLev));
                          c[1] = 0;
                          System.out.println("2");
                          continue;
                       }
                       if(c[2] == 1) {
                          pstmt.setInt(i, getCycleOkNum(selectedCycle));
                          c[2] = 0;
                          System.out.println("3");
                          continue;
                       }
                       if(c[3] == 1) {
                          pstmt.setInt(i, getDogOkNum(selectedDog));
                          c[3] = 0;
                          System.out.println("4");
                          continue;
                       }
                    }

                    ResultSet rs = pstmt.executeQuery();

                    // 기존 테이블 모델 초기화
                    model.setRowCount(0);

                    // 결과를 테이블 모델에 추가
                    while (rs.next()) {
                        // 나머지 코드는 동일합니다.
                        String grName = rs.getString("gr_name");
                        String addrName = rs.getString("addr_name");
                        int levNo = rs.getInt("lev_no");
                        double grDist = rs.getDouble("gr_dist");
                        int cycleOK_num = rs.getInt("gr_cycleok");
                        int dogOK_num = rs.getInt("gr_dogok");
                        double Number = Math.round(grDist * 10) / 10.0;
                        // 레벨 번호에 따라 문자열 설정
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

                        // 데이터베이스에서 가져온 값을 테이블 모델에 추가
                        model.addRow(new Object[]{grName, Number + "km", addrName, level, cycleOk, dogOk});
                    }

                    rs.close();
                    pstmt.close();
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Connection con = Jdbc.get();

            // SQL 쿼리
            String sql = "SELECT * FROM "
                    + "ground JOIN address ON ground.addr_no = address.addr_no "
                    + "ORDER BY gr_dist ASC";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // 결과를 테이블 모델에 추가
            while (rs.next()) {
                // 나머지 코드는 동일합니다.
                String grName = rs.getString("gr_name");
                String addrName = rs.getString("addr_name");
                int levNo = rs.getInt("lev_no");
                double grDist = rs.getDouble("gr_dist");
                int cycleOK_num = rs.getInt("gr_cycleok");
                int dogOK_num = rs.getInt("gr_dogok");
                double Number = Math.round(grDist * 10) / 10.0;
                // 레벨 번호에 따라 문자열 설정
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

                // 데이터베이스에서 가져온 값을 테이블 모델에 추가
                model.addRow(new Object[]{grName, Number + "km", addrName, level, cycleOk, dogOk});
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 테이블 생성
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

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
        frame.getContentPane().setBackground(Color.WHITE); // 배경색 흰색으로 설정

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정

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
                infoPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정

                DefaultTableModel detailModel = new DefaultTableModel();
                detailModel.addColumn(grName);
                
                detailModel.addRow(new Object[]{"주소 : " + addrName});
                detailModel.addRow(new Object[]{"자전거 가능 여부 : " + (cycleOK_num == 1 ? "가능" : "불가능")});
                detailModel.addRow(new Object[]{"반려동물 출입 가능 여부 : " + (dogOK_num == 1 ? "가능" : "불가능")});
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

                JTextArea infoTextArea = new JTextArea(grInfo);
                infoTextArea.setOpaque(false); // 배경색 투명 설정
                infoTextArea.setLineWrap(true);
                infoTextArea.setWrapStyleWord(true);
                infoTextArea.setEditable(false);
                infoTextArea.setFont(new Font("Gong Gothic Light", Font.PLAIN, 18)); // 폰트 변경

                // infoTextArea와 detailTable 사이 간격 조정
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

    private int getCycleOkNum(String cycle) {
        switch (cycle) {
            case "불가능":
                return 0;
            case "가능":
                return 1;
            case "일부분 가능":
                return 2;
            default:
                return 0;
        }
    }

    private int getDogOkNum(String dog) {
        switch (dog) {
            case "불가능":
                return 0;
            case "가능":
                return 1;
            default:
                return 0;
        }
    }
}

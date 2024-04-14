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
        contentPane.setBorder(new EmptyBorder(100, 100, 100, 100)); // 여백 추가
        contentPane.setLayout(new BorderLayout(0, 10)); // 수직 간격 10으로 설정

        // 열 이름 정의
        String[] columnNames = {"제목", "종류", "작성일자", "작성자명"};

        // 테이블 모델 생성
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            Connection con = Jdbc.get();

            // SQL 쿼리
            String sql = "SELECT board.board_title, board.board_type, board.board_date, user.user_nick FROM board JOIN user ON board.user_no = user.user_no ORDER BY board_date DESC";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // 결과를 테이블 모델에 추가
            while (rs.next()) {
                String boTitle = rs.getString("board_title");
                int boTypeno = rs.getInt("board_type");
                Date boDate = rs.getDate("board_date");
                String nickName  = rs.getString("user_nick");
                
                String boType = "";
                switch (boTypeno) {
                    case 1:
                       boType = "홍보글";
                        break;
                    case 2:
                       boType = "잡담";
                        break;
                    default:
                       boType = "알 수 없음";
                }

                // 데이터베이스에서 가져온 값을 테이블 모델에 추가
                model.addRow(new Object[]{boTitle, boType, boDate, nickName});
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
        frame.getContentPane().setBackground(Color.WHITE); // 배경색 흰색으로 설정

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정

        try {
            Connection con = Jdbc.get();

            String sql = "SELECT * FROM board JOIN user ON board.user_no = user.user_no WHERE board.board_title = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, boardTitle);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String boTitle = rs.getString("board_title"); // 게시글 제목
                String nickName  = rs.getString("user_nick"); // 작성자 닉네임
                Date boDate = rs.getDate("board_date"); // 작성일자
                int boTypeno = rs.getInt("board_type"); // 글 종류
                String boCont = rs.getString("board_cont"); // 글 내용
                
                String boType = "";
                switch (boTypeno) {
                    case 1:
                       boType = "홍보글";
                        break;
                    case 2:
                       boType = "잡담";
                        break;
                    default:
                       boType = "알 수 없음";
                }
                
                JPanel infoPanel = new JPanel(new BorderLayout());
                infoPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정

                DefaultTableModel detailModel = new DefaultTableModel();
                detailModel.addColumn(boTitle); 
                
                detailModel.addRow(new Object[]{"작성자 : " + nickName});
                detailModel.addRow(new Object[]{"작성일자 : " + boDate});
                detailModel.addRow(new Object[]{"글 종류 : " + boType});
                detailModel.addRow(new Object[]{"내용"});

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

                JTextArea infoTextArea = new JTextArea(boCont);
                infoTextArea.setOpaque(false); // 배경색 투명 설정
                infoTextArea.setLineWrap(true);
                infoTextArea.setWrapStyleWord(true);
                infoTextArea.setEditable(false);
                infoTextArea.setFont(new Font("Gong Gothic Light", Font.PLAIN, 18)); // 폰트 변경

                // infoTextArea와 detailTable 사이 간격 조정
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
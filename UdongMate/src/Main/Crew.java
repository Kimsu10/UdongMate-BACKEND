package Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import Jdbc.*;

public class Crew extends MainTap {
	   private JPanel contentPane;
	   private static int loggedInUserNo;
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
	      contentPane.setBorder(new EmptyBorder(50, 100, 100, 100));
	      contentPane.setLayout(new BorderLayout(0, 10));

	      // 버튼을 포함할 패널 생성
	      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

	      // 버튼 생성
	      JButton button = new JButton("크루 생성");
	      button.setFont(new Font("Gong Gothic Light", Font.PLAIN, 20));
	      button.setBackground(Color.WHITE);
	      buttonPanel.add(button); // 버튼을 패널에 추가

	      button.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            // 코스 창 이동
	            new CrewMake().setVisible(true);
	            
	         }
	      });

	      // 테이블 위에 버튼 패널을 추가
	      contentPane.add(buttonPanel, BorderLayout.NORTH);

	      String[] columnNames = { "크루명", "크루 소개글", "지역" };

	      DefaultTableModel model = new DefaultTableModel(columnNames, 0);

	      try {
	         Connection con = Jdbc.get();

	         // SQL 쿼리
	         String sql = "SELECT crew.crew_no, crew.crew_name, crew.crew_intro, address.addr_name FROM crew JOIN address ON crew.addr_no = address.addr_no and crew_no<>5";
	         PreparedStatement pstmt = con.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery();
	        
	         // 결과를 테이블 모델에 추가
	         while (rs.next()) {
	            String crName = rs.getString("crew_name");
	            String crewIntro = rs.getString("crew_intro");
	            String addrName = rs.getString("addr_name");

	            // 행을 추가하여 데이터베이스에서 가져온 값을 테이블 모델에 추가
	            model.addRow(new Object[] { crName, crewIntro, addrName });
	         }

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
	               String crewName = (String) table.getValueAt(row, 0);
	               displayGroundDetails(crewName);
	            }
	         }
	      });
	   }

	   private void displayGroundDetails(String crewName) { //크루정보 팝업창
		    JFrame frame = new JFrame(crewName);
		    frame.setSize(400, 250);
		    frame.setLocationRelativeTo(null);
		    frame.getContentPane().setBackground(Color.WHITE); // 배경색 흰색으로 설정

		    JPanel detailsPanel = new JPanel(new BorderLayout());
		    detailsPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정

		    try {
		        Connection con = Jdbc.get();
		        
		        String sql = "SELECT crew.crew_no, crew_name, crew_intro, addr_name, user_nick, user_phone FROM user, user_crew, crew, address WHERE user.user_no=user_crew.user_no AND user_crew.crew_no=crew.crew_no AND address.addr_no=crew.addr_no AND crew_cap=1 AND crew.crew_name=?";
	            PreparedStatement pstmt = con.prepareStatement(sql);
	            pstmt.setString(1, crewName);
	            ResultSet rs = pstmt.executeQuery();


		        if (rs.next()) {
		            int crewNo = rs.getInt("crew_no");
		            String crName = rs.getString("crew_name");
		            String crewIntro = rs.getString("crew_intro");
		            String addrName = rs.getString("addr_name");
		            String userNick = rs.getString("user_nick");
		            String userPhone = rs.getString("user_phone");
		            JPanel infoPanel = new JPanel(new BorderLayout());
		            infoPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정
		            
		            DefaultTableModel detailModel = new DefaultTableModel();
		            detailModel.addColumn(crName);

		            detailModel.addRow(new Object[]{"크루 소개 : " + crewIntro});
		            detailModel.addRow(new Object[]{"지역 : " + addrName});
		            detailModel.addRow(new Object[]{"크루장 : " + userNick});
		            detailModel.addRow(new Object[]{"크루장 번호 : " + userPhone});
		            
		            JButton joinButton = new JButton("가입하기");
		            joinButton.setFont(new Font("Gong Gothic Light", Font.PLAIN, 17));
		            joinButton.setPreferredSize(new Dimension(100, 30));
		            joinButton.setBackground(Color.WHITE);
		            // 이미 가입된 크루인지 확인
		            if (isAlreadyJoined(con, crewNo, loggedInUserNo)) {
		               // 이미 가입된 크루라면 메시지 표시 및 가입 버튼 비활성화
		               detailModel.addRow(new Object[]{"상태 : 이미 가입된 크루입니다."});
		               joinButton.setEnabled(false); // 가입 버튼 비활성화
		            } else {
		               // 가입되지 않은 크루라면 가입 버튼 활성화
		              
		               joinButton.addActionListener(new ActionListener() {
		                  @Override
		                   public void actionPerformed(ActionEvent e) {
		                       try {
		                    	   
		                           // 가입하기 버튼이 클릭되었을 때 수행할 동작
		                           String joinQuery = "CALL joinCrew(?, ?)";
		                           PreparedStatement joinStatement = con.prepareStatement(joinQuery);
		                           // 현재 로그인한 사용자의 user_no를 사용하고, 선택한 크루의 crew_no를 사용
		                           joinStatement.setInt(1, loggedInUserNo); // CURRENT_USER_ID에 현재 사용자의 ID를 할당
		                           joinStatement.setInt(2, crewNo);
		                           
		                           int rowsAffected = joinStatement.executeUpdate();
		                           if (rowsAffected > 0) {
		                               JOptionPane.showMessageDialog(frame, "크루에 가입되었습니다!");
		                           }
		                           joinStatement.close();
		                       } catch (SQLException ex) {
		                           ex.printStackTrace();
		                           JOptionPane.showMessageDialog(frame, "가입 중 오류가 발생했습니다.");
		                       }
		                   }
		               });
		            }

		            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		            buttonPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정
		            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // 위쪽과 아래쪽에 패딩 추가
		            buttonPanel.add(joinButton); // 버튼을 패널에 추가

		            frame.add(buttonPanel, BorderLayout.SOUTH); // 버튼 패널을 프레임의 하단에 추가
		            // 버튼 패널의 배경색을 흰색으로 설정
		            buttonPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정
		            
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

		            detailsPanel.add(infoPanel, BorderLayout.CENTER);
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

	        frame.add(detailsPanel);
	        frame.setVisible(true);
	    }
	    
	   private boolean isAlreadyJoined(Connection con, int crewNo, int userNo) throws SQLException {
	      String query = "SELECT * FROM user_crew WHERE crew_no = ? AND user_no = ?";
	      PreparedStatement pstmt = con.prepareStatement(query);
	      pstmt.setInt(1, crewNo);
	      pstmt.setInt(2, userNo);
	      ResultSet rs = pstmt.executeQuery();
	      return rs.next(); // 결과가 존재하면 이미 가입된 상태
	   }
	    
	   public static void setLoggedInUserNo(int userNo) {
	      loggedInUserNo = userNo;
	   }
	}

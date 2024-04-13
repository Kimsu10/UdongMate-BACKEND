package Main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import JDBC.Jdbc;

public class CrewMake extends JFrame {
    private JComboBox<String> addressDropdown;
    private Map<String, Integer> addressMap; // 주소명과 주소번호를 매핑하기 위한 맵
    private JTextField idField;
    private JButton checkDuplicateButton;
    private boolean isIdChecked = false; // 아이디 중복 확인 여부를 나타내는 변수 추가

    public CrewMake() {
        super("크루 생성");
        setSize(300, 300); // 창 크기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // 절대 레이아웃
        setLocationRelativeTo(null); // 창을 화면 중앙에 위치

        addressMap = new HashMap<>();
        populateAddressMap(); // 주소명, 주소번호를 매핑할 객체 생성, 메서드 호출

        Font font = new Font("Gong Gothic bold", Font.PLAIN, 12); // 폰트 생성
        Iterator<Object> keysIterator = UIManager.getDefaults().keySet().iterator(); // 모든 컴포넌트에 폰트 적용
        while (keysIterator.hasNext()) {
            Object key = keysIterator.next();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }

        // 라벨과 입력필드 생성
        JLabel nameLabel = new JLabel("크루명: ");
        nameLabel.setBounds(10, 20, 80, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(100, 20, 176, 25);
        add(nameField);
        
        checkDuplicateButton = new JButton("중복체크"); //중복체크 버튼 설정
        checkDuplicateButton.setBounds(210, 20, 80, 25);
        Font buttonFont = checkDuplicateButton.getFont();
        checkDuplicateButton.setFont(buttonFont.deriveFont(Font.PLAIN, 10)); //폰트 크기 조정
        add(checkDuplicateButton);

        checkDuplicateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String crewName = nameField.getText();
                if (crewName.isEmpty()) {
                    JOptionPane.showMessageDialog(CrewMake.this, "크루명을 입력하세요.");
                } else if (isIdDuplicate(crewName)) {
                    JOptionPane.showMessageDialog(CrewMake.this, "이미 존재하는 크루명입니다.");
                } else {
                    JOptionPane.showMessageDialog(CrewMake.this, "사용 가능한 크루명입니다.");
                    isIdChecked = true; // 크루명 중복 확인 완료
                }
            }
        });

        JLabel introLabel = new JLabel("소개글: ");
        introLabel.setBounds(10, 50, 80, 25);
        add(introLabel);

        JTextArea introArea = new JTextArea(); // JTextArea로 변경
        introArea.setLineWrap(true); // 자동 줄 바꿈 설정
        JScrollPane introScrollPane = new JScrollPane(introArea); // 스크롤 가능하도록 JScrollPane에 넣음
        introScrollPane.setBounds(100, 50, 176, 75); 
        add(introScrollPane);

        JLabel addressLabel = new JLabel("지역: ");
        addressLabel.setBounds(10, 130, 80, 25); 
        add(addressLabel);

        String[] addresses = { "종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구", "강북구", "도봉구", "노원구", "은평구",
                "서대문구", "마포구", "양천구", "강서구", "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", "강동구" };
        addressDropdown = new JComboBox<>(addresses);
        addressDropdown.setBounds(100, 130, 176, 25);
        add(addressDropdown);
        
        JButton registerButton = new JButton("생성하기");
        registerButton.setBounds(100, 200, 100, 25);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 입력된 크루 정보 가져오기
                String crewName = nameField.getText();
                String intro = introArea.getText();
                
                String addressName = (String) addressDropdown.getSelectedItem();
                Integer addressNo = addressMap.get(addressName);

                
               
                // 크루 정보를 crew 테이블에 삽입하기 위한 INSERT 쿼리 작성
                String query = "INSERT INTO crew (crew_name, crew_intro, addr_no) VALUES (?, ?, ?)";

                try (Connection con = Jdbc.get();
                     PreparedStatement psmt = con.prepareStatement(query)) {

                    // 쿼리의 ?에 값 할당
                    psmt.setString(1, crewName);
                    psmt.setString(2, intro);
                    psmt.setInt(3, addressNo);

                    // 쿼리 실행
                    int rowsInserted = psmt.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(CrewMake.this, "크루 생성이 완료되었습니다.");
                        // 크루 생성 완료 후 처리할 내용 추가
                        CrewMake.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(CrewMake.this, "크루 생성에 실패하였습니다. 다시 시도해주세요.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(CrewMake.this, "크루 생성 중 오류가 발생하였습니다. 다시 시도해주세요.");
                }
            }
        });
        add(registerButton);

        
        setVisible(true);
    }
    private boolean isIdDuplicate(String crewName) {
        try (Connection con = Jdbc.get();
             PreparedStatement psmt = con.prepareStatement("SELECT COUNT(*) AS count FROM crew WHERE crew_name = ?")) {
            psmt.setString(1, crewName);
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0; // 중복된 경우 true 반환
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; // 중복되지 않은 경우 false 반환
    }
    
    
    private void populateAddressMap() {
        String query = "SELECT addr_no, addr_name FROM address";
        try (Connection con = Jdbc.get();
             PreparedStatement psmt = con.prepareStatement(query);
             ResultSet rs = psmt.executeQuery()) {
            while (rs.next()) {
                int addrNo = rs.getInt("addr_no");
                String addrName = rs.getString("addr_name");
                addressMap.put(addrName, addrNo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "주소 정보를 가져오는 중 오류가 발생하였습니다.");
        }
    }

    public static void main(String[] args) {
        new CrewMake();
    }
}
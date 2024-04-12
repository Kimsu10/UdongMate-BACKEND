package MyPage;

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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import JDBC.Jdbc;

public class Membership extends JFrame {

    private JComboBox<String> addressDropdown;
    private Map<String, Integer> addressMap; // 주소명과 주소번호를 매핑하기 위한 맵
    private JTextField idField;
    private JButton checkDuplicateButton;
    private boolean isIdChecked = false; // 아이디 중복 확인 여부를 나타내는 변수 추가
   
    public Membership() {
        super("회원 가입");
        setSize(310, 400); //창 크기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); //절대 레이아웃
        setLocationRelativeTo(null); //창을 화면 중앙에 위치

        addressMap = new HashMap<>(); 
        populateAddressMap(); //주소명, 주소번호를 매핑할 객체 생성, 메서드 호출

        Font font = new Font("Gong Gothic bold", Font.PLAIN, 12); //폰트 생성
        
        Iterator<Object> keysIterator = UIManager.getDefaults().keySet().iterator(); // 모든 컴포넌트에 폰트 적용
        while (keysIterator.hasNext()) {
            Object key = keysIterator.next();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) { 
                UIManager.put(key, font);
            }
        }
        
        
        //라벨과 입력필드 생성
        JLabel nameLabel = new JLabel("이름: ");
        nameLabel.setBounds(10, 20, 80, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(100, 20, 176, 25);
        add(nameField);

        JLabel idLabel = new JLabel("아이디: ");
        idLabel.setBounds(10, 50, 80, 25);
        add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(100, 50, 100, 25);
        add(idField);

        checkDuplicateButton = new JButton("중복체크"); //중복체크 버튼 설정
        checkDuplicateButton.setBounds(200, 51, 75, 23);
        Font buttonFont = checkDuplicateButton.getFont();
        checkDuplicateButton.setFont(buttonFont.deriveFont(Font.PLAIN, 10)); //폰트 크기 조정
        add(checkDuplicateButton);

        checkDuplicateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(Membership.this, "아이디를 입력하세요.");
                } else if (isIdDuplicate(id)) {
                    JOptionPane.showMessageDialog(Membership.this, "이미 존재하는 아이디입니다.");
                } else {
                    JOptionPane.showMessageDialog(Membership.this, "사용 가능한 아이디입니다.");
                    isIdChecked = true; // 아이디 중복 확인 완료
                }
            }
        });
        
        JLabel passwordLabel = new JLabel("비밀번호: ");
        passwordLabel.setBounds(10, 80, 80, 25);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(100, 80, 176, 25);
        passwordField.setEchoChar('*');
        add(passwordField);

        JLabel nickNameLabel = new JLabel("닉네임: ");
        nickNameLabel.setBounds(10, 110, 80, 25);
        add(nickNameLabel);

        JTextField nickNameField = new JTextField();
        nickNameField.setBounds(100, 110, 176, 25);
        add(nickNameField);

        JLabel phoneNumberLabel = new JLabel("HP: ");
        phoneNumberLabel.setBounds(10, 140, 80, 25);
        add(phoneNumberLabel);

        JTextField phoneNumberField = new JTextField();
        phoneNumberField.setBounds(100, 140, 176, 25);
        add(phoneNumberField);

        JLabel genderLabel = new JLabel("성별: ");
        genderLabel.setBounds(10, 170, 80, 25);
        add(genderLabel);

        JRadioButton gen_m = new JRadioButton("남");
        gen_m.setBounds(100, 170, 50, 25);
        add(gen_m);

        JRadioButton gen_f = new JRadioButton("여");
        gen_f.setBounds(160, 170, 50, 25);
        add(gen_f);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(gen_m);
        genderGroup.add(gen_f);

        JLabel ageLabel = new JLabel("나이: ");
        ageLabel.setBounds(10, 200, 80, 25);
        add(ageLabel);

        JTextField ageField = new JTextField();
        ageField.setBounds(100, 200, 176, 25);
        add(ageField);

        JLabel addressLabel = new JLabel("주소: ");
        addressLabel.setBounds(10, 230, 80, 25);
        add(addressLabel);


        String[] addresses = {"종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구",
                "강북구", "도봉구", "노원구", "은평구", "서대문구", "마포구", "양천구", "강서구",
                "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", "강동구"};
        addressDropdown = new JComboBox<>(addresses);
        addressDropdown.setBounds(100, 230, 176, 25);
        add(addressDropdown);

        JButton registerButton = new JButton("가입하기");
        registerButton.setBounds(100, 290, 100, 25);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 입력된 회원 정보 가져오기
                String name = nameField.getText();
                String id = idField.getText();
                String password = new String(passwordField.getPassword());
                String nickname = nickNameField.getText();
                String phoneNumber = phoneNumberField.getText();
                String gender = gen_m.isSelected() ? "남자" : "여자";
                String age = ageField.getText();
                String addressName = (String) addressDropdown.getSelectedItem();
                Integer addressNo = addressMap.get(addressName);

                if (!isIdChecked) { // 아이디 중복 확인을 하지 않았을 경우
                    JOptionPane.showMessageDialog(Membership.this, "아이디 중복 여부를 확인해주세요!");
                    return; // 회원가입 중단
                }
                // 회원 정보를 user 테이블에 삽입하기 위한 INSERT 쿼리 작성
                String query = "INSERT INTO user (user_id, user_pw, user_name, user_nick, user_phone, user_gender, user_age, addr_no) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection con = Jdbc.get();
                     PreparedStatement psmt = con.prepareStatement(query)) {

                    // 쿼리의 ?에 값 할당
                    psmt.setString(1, id);
                    psmt.setString(2, password);
                    psmt.setString(3, name);
                    psmt.setString(4, nickname);
                    psmt.setString(5, phoneNumber);
                    psmt.setString(6, gender);
                    psmt.setString(7, age);
                    psmt.setInt(8, addressNo);

                    // 쿼리 실행
                    int rowsInserted = psmt.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(Membership.this, "회원가입이 완료되었습니다.");
                        // 회원가입 완료 후 처리할 내용 추가
                        Membership.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(Membership.this, "회원가입에 실패하였습니다. 다시 시도해주세요.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Membership.this, "회원가입 중 오류가 발생하였습니다. 다시 시도해주세요.");
                }
            }
        });
        add(registerButton);

        setVisible(true);
    }
    private boolean isIdDuplicate(String id) {
        try (Connection con = Jdbc.get();
             PreparedStatement psmt = con.prepareStatement("SELECT COUNT(*) AS count FROM user WHERE user_id = ?")) {
            psmt.setString(1, id);
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
        new Membership();
    }
}
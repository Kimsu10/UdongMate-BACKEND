package MyPage;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Membership extends JFrame {

    public Membership() {
        super("회원 가입");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); //절대 레이아웃
        
        // gong gothic bold 폰트 생성
        Font font = new Font("Gong Gothic bold", Font.PLAIN, 12);

        // 모든 컴포넌트의 폰트를 gong gothic bold로 변경
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }

        JLabel nameLabel = new JLabel("이름: ");
        nameLabel.setBounds(10, 20, 80, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(100, 20, 160, 25);
        add(nameField);

        JLabel idLabel = new JLabel("아이디: ");
        idLabel.setBounds(10, 50, 80, 25);
        add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(100, 50, 160, 25);
        add(idField);

        JLabel passwordLabel = new JLabel("비밀번호: ");
        passwordLabel.setBounds(10, 80, 80, 25);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(100, 80, 160, 25);
        add(passwordField);

        JLabel nickNameLabel = new JLabel("닉네임: ");
        nickNameLabel.setBounds(10, 110, 80, 25);
        add(nickNameLabel);

        JTextField nickNameField = new JTextField();
        nickNameField.setBounds(100, 110, 160, 25);
        add(nickNameField);

        JLabel phoneNumberLabel = new JLabel("HP: ");
        phoneNumberLabel.setBounds(10, 140, 80, 25);
        add(phoneNumberLabel);

        JTextField phoneNumberField = new JTextField();
        phoneNumberField.setBounds(100, 140, 160, 25);
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
        ageField.setBounds(100, 200, 160, 25);
        add(ageField);

        JLabel addressLabel = new JLabel("주소: ");
        addressLabel.setBounds(10, 230, 80, 25);
        add(addressLabel);
        

        String[] addresses = {"종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구", 
                         "강북구", "도봉구", "노원구", "은평구", "서대문구", "마포구", "양천구", "강서구", 
                         "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", "강동구"};
        JComboBox<String> addressDropdown = new JComboBox<>(addresses);
        addressDropdown.setBounds(100, 230, 160, 25);     
        add(addressDropdown);

        JButton registerButton = new JButton("가입하기");
        registerButton.setBounds(100, 290, 100, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().isEmpty() || idField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(Membership.this, "정보를 모두 기입해주세요!");
                } else {
                    Member member = new Member(nameField.getText(), idField.getText(), new String(passwordField.getPassword()));
                    saveMemberToFile(member);
                    JOptionPane.showMessageDialog(Membership.this, "회원가입을 완료했습니다!");
                }
            }
        });
        add(registerButton);

        setVisible(true);
    }

    private void saveMemberToFile(Member member) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("members.txt", true))) {
            bw.write(member.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Member {
        private String name;
        private String id;
        private String password;

        public Member(String name, String id, String password) {
            this.name = name;
            this.id = id;
            this.password = password;
        }

        @Override
        public String toString() {
            return "이름: " + name + ", 아이디: " + id + ", 비밀번호: " + password;
        }
    }

    public static void main(String[] args) {
        new Membership();
    }
}
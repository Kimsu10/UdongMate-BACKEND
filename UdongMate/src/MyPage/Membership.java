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
        super("ȸ�� ����");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); //���� ���̾ƿ�
        
        // gong gothic bold ��Ʈ ����
        Font font = new Font("Gong Gothic bold", Font.PLAIN, 12);

        // ��� ������Ʈ�� ��Ʈ�� gong gothic bold�� ����
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }

        JLabel nameLabel = new JLabel("�̸�: ");
        nameLabel.setBounds(10, 20, 80, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(100, 20, 160, 25);
        add(nameField);

        JLabel idLabel = new JLabel("���̵�: ");
        idLabel.setBounds(10, 50, 80, 25);
        add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(100, 50, 160, 25);
        add(idField);

        JLabel passwordLabel = new JLabel("��й�ȣ: ");
        passwordLabel.setBounds(10, 80, 80, 25);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(100, 80, 160, 25);
        add(passwordField);

        JLabel nickNameLabel = new JLabel("�г���: ");
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

        JLabel genderLabel = new JLabel("����: ");
        genderLabel.setBounds(10, 170, 80, 25);
        add(genderLabel);

        JRadioButton gen_m = new JRadioButton("��");
        gen_m.setBounds(100, 170, 50, 25);
        add(gen_m);

        JRadioButton gen_f = new JRadioButton("��");
        gen_f.setBounds(160, 170, 50, 25);
        add(gen_f);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(gen_m);
        genderGroup.add(gen_f);

        JLabel ageLabel = new JLabel("����: ");
        ageLabel.setBounds(10, 200, 80, 25);
        add(ageLabel);

        JTextField ageField = new JTextField();
        ageField.setBounds(100, 200, 160, 25);
        add(ageField);

        JLabel addressLabel = new JLabel("�ּ�: ");
        addressLabel.setBounds(10, 230, 80, 25);
        add(addressLabel);
        

        String[] addresses = {"���α�", "�߱�", "��걸", "������", "������", "���빮��", "�߶���", "���ϱ�", 
                         "���ϱ�", "������", "�����", "����", "���빮��", "������", "��õ��", "������", 
                         "���α�", "��õ��", "��������", "���۱�", "���Ǳ�", "���ʱ�", "������", "���ı�", "������"};
        JComboBox<String> addressDropdown = new JComboBox<>(addresses);
        addressDropdown.setBounds(100, 230, 160, 25);     
        add(addressDropdown);

        JButton registerButton = new JButton("�����ϱ�");
        registerButton.setBounds(100, 290, 100, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().isEmpty() || idField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(Membership.this, "������ ��� �������ּ���!");
                } else {
                    Member member = new Member(nameField.getText(), idField.getText(), new String(passwordField.getPassword()));
                    saveMemberToFile(member);
                    JOptionPane.showMessageDialog(Membership.this, "ȸ�������� �Ϸ��߽��ϴ�!");
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
            return "�̸�: " + name + ", ���̵�: " + id + ", ��й�ȣ: " + password;
        }
    }

    public static void main(String[] args) {
        new Membership();
    }
}
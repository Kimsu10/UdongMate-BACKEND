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
    private Map<String, Integer> addressMap; // �ּҸ�� �ּҹ�ȣ�� �����ϱ� ���� ��
    private JTextField idField;
    private JButton checkDuplicateButton;
    private boolean isIdChecked = false; // ���̵� �ߺ� Ȯ�� ���θ� ��Ÿ���� ���� �߰�
   
    public Membership() {
        super("ȸ�� ����");
        setSize(310, 400); //â ũ��
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); //���� ���̾ƿ�
        setLocationRelativeTo(null); //â�� ȭ�� �߾ӿ� ��ġ

        addressMap = new HashMap<>(); 
        populateAddressMap(); //�ּҸ�, �ּҹ�ȣ�� ������ ��ü ����, �޼��� ȣ��

        Font font = new Font("Gong Gothic bold", Font.PLAIN, 12); //��Ʈ ����
        
        Iterator<Object> keysIterator = UIManager.getDefaults().keySet().iterator(); // ��� ������Ʈ�� ��Ʈ ����
        while (keysIterator.hasNext()) {
            Object key = keysIterator.next();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) { 
                UIManager.put(key, font);
            }
        }
        
        
        //�󺧰� �Է��ʵ� ����
        JLabel nameLabel = new JLabel("�̸�: ");
        nameLabel.setBounds(10, 20, 80, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(100, 20, 176, 25);
        add(nameField);

        JLabel idLabel = new JLabel("���̵�: ");
        idLabel.setBounds(10, 50, 80, 25);
        add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(100, 50, 100, 25);
        add(idField);

        checkDuplicateButton = new JButton("�ߺ�üũ"); //�ߺ�üũ ��ư ����
        checkDuplicateButton.setBounds(200, 51, 75, 23);
        Font buttonFont = checkDuplicateButton.getFont();
        checkDuplicateButton.setFont(buttonFont.deriveFont(Font.PLAIN, 10)); //��Ʈ ũ�� ����
        add(checkDuplicateButton);

        checkDuplicateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(Membership.this, "���̵� �Է��ϼ���.");
                } else if (isIdDuplicate(id)) {
                    JOptionPane.showMessageDialog(Membership.this, "�̹� �����ϴ� ���̵��Դϴ�.");
                } else {
                    JOptionPane.showMessageDialog(Membership.this, "��� ������ ���̵��Դϴ�.");
                    isIdChecked = true; // ���̵� �ߺ� Ȯ�� �Ϸ�
                }
            }
        });
        
        JLabel passwordLabel = new JLabel("��й�ȣ: ");
        passwordLabel.setBounds(10, 80, 80, 25);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(100, 80, 176, 25);
        passwordField.setEchoChar('*');
        add(passwordField);

        JLabel nickNameLabel = new JLabel("�г���: ");
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
        ageField.setBounds(100, 200, 176, 25);
        add(ageField);

        JLabel addressLabel = new JLabel("�ּ�: ");
        addressLabel.setBounds(10, 230, 80, 25);
        add(addressLabel);


        String[] addresses = {"���α�", "�߱�", "��걸", "������", "������", "���빮��", "�߶���", "���ϱ�",
                "���ϱ�", "������", "�����", "����", "���빮��", "������", "��õ��", "������",
                "���α�", "��õ��", "��������", "���۱�", "���Ǳ�", "���ʱ�", "������", "���ı�", "������"};
        addressDropdown = new JComboBox<>(addresses);
        addressDropdown.setBounds(100, 230, 176, 25);
        add(addressDropdown);

        JButton registerButton = new JButton("�����ϱ�");
        registerButton.setBounds(100, 290, 100, 25);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // �Էµ� ȸ�� ���� ��������
                String name = nameField.getText();
                String id = idField.getText();
                String password = new String(passwordField.getPassword());
                String nickname = nickNameField.getText();
                String phoneNumber = phoneNumberField.getText();
                String gender = gen_m.isSelected() ? "����" : "����";
                String age = ageField.getText();
                String addressName = (String) addressDropdown.getSelectedItem();
                Integer addressNo = addressMap.get(addressName);

                if (!isIdChecked) { // ���̵� �ߺ� Ȯ���� ���� �ʾ��� ���
                    JOptionPane.showMessageDialog(Membership.this, "���̵� �ߺ� ���θ� Ȯ�����ּ���!");
                    return; // ȸ������ �ߴ�
                }
                // ȸ�� ������ user ���̺� �����ϱ� ���� INSERT ���� �ۼ�
                String query = "INSERT INTO user (user_id, user_pw, user_name, user_nick, user_phone, user_gender, user_age, addr_no) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection con = Jdbc.get();
                     PreparedStatement psmt = con.prepareStatement(query)) {

                    // ������ ?�� �� �Ҵ�
                    psmt.setString(1, id);
                    psmt.setString(2, password);
                    psmt.setString(3, name);
                    psmt.setString(4, nickname);
                    psmt.setString(5, phoneNumber);
                    psmt.setString(6, gender);
                    psmt.setString(7, age);
                    psmt.setInt(8, addressNo);

                    // ���� ����
                    int rowsInserted = psmt.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(Membership.this, "ȸ�������� �Ϸ�Ǿ����ϴ�.");
                        // ȸ������ �Ϸ� �� ó���� ���� �߰�
                        Membership.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(Membership.this, "ȸ�����Կ� �����Ͽ����ϴ�. �ٽ� �õ����ּ���.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Membership.this, "ȸ������ �� ������ �߻��Ͽ����ϴ�. �ٽ� �õ����ּ���.");
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
                    return count > 0; // �ߺ��� ��� true ��ȯ
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; // �ߺ����� ���� ��� false ��ȯ
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
            JOptionPane.showMessageDialog(this, "�ּ� ������ �������� �� ������ �߻��Ͽ����ϴ�.");
        }
    }
    public static void main(String[] args) {
        new Membership();
    }
}
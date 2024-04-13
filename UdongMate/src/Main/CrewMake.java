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
    private Map<String, Integer> addressMap; // �ּҸ�� �ּҹ�ȣ�� �����ϱ� ���� ��
    private JTextField idField;
    private JButton checkDuplicateButton;
    private boolean isIdChecked = false; // ���̵� �ߺ� Ȯ�� ���θ� ��Ÿ���� ���� �߰�

    public CrewMake() {
        super("ũ�� ����");
        setSize(300, 300); // â ũ��
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // ���� ���̾ƿ�
        setLocationRelativeTo(null); // â�� ȭ�� �߾ӿ� ��ġ

        addressMap = new HashMap<>();
        populateAddressMap(); // �ּҸ�, �ּҹ�ȣ�� ������ ��ü ����, �޼��� ȣ��

        Font font = new Font("Gong Gothic bold", Font.PLAIN, 12); // ��Ʈ ����
        Iterator<Object> keysIterator = UIManager.getDefaults().keySet().iterator(); // ��� ������Ʈ�� ��Ʈ ����
        while (keysIterator.hasNext()) {
            Object key = keysIterator.next();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }

        // �󺧰� �Է��ʵ� ����
        JLabel nameLabel = new JLabel("ũ���: ");
        nameLabel.setBounds(10, 20, 80, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(100, 20, 176, 25);
        add(nameField);
        
        checkDuplicateButton = new JButton("�ߺ�üũ"); //�ߺ�üũ ��ư ����
        checkDuplicateButton.setBounds(210, 20, 80, 25);
        Font buttonFont = checkDuplicateButton.getFont();
        checkDuplicateButton.setFont(buttonFont.deriveFont(Font.PLAIN, 10)); //��Ʈ ũ�� ����
        add(checkDuplicateButton);

        checkDuplicateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String crewName = nameField.getText();
                if (crewName.isEmpty()) {
                    JOptionPane.showMessageDialog(CrewMake.this, "ũ����� �Է��ϼ���.");
                } else if (isIdDuplicate(crewName)) {
                    JOptionPane.showMessageDialog(CrewMake.this, "�̹� �����ϴ� ũ����Դϴ�.");
                } else {
                    JOptionPane.showMessageDialog(CrewMake.this, "��� ������ ũ����Դϴ�.");
                    isIdChecked = true; // ũ��� �ߺ� Ȯ�� �Ϸ�
                }
            }
        });

        JLabel introLabel = new JLabel("�Ұ���: ");
        introLabel.setBounds(10, 50, 80, 25);
        add(introLabel);

        JTextArea introArea = new JTextArea(); // JTextArea�� ����
        introArea.setLineWrap(true); // �ڵ� �� �ٲ� ����
        JScrollPane introScrollPane = new JScrollPane(introArea); // ��ũ�� �����ϵ��� JScrollPane�� ����
        introScrollPane.setBounds(100, 50, 176, 75); 
        add(introScrollPane);

        JLabel addressLabel = new JLabel("����: ");
        addressLabel.setBounds(10, 130, 80, 25); 
        add(addressLabel);

        String[] addresses = { "���α�", "�߱�", "��걸", "������", "������", "���빮��", "�߶���", "���ϱ�", "���ϱ�", "������", "�����", "����",
                "���빮��", "������", "��õ��", "������", "���α�", "��õ��", "��������", "���۱�", "���Ǳ�", "���ʱ�", "������", "���ı�", "������" };
        addressDropdown = new JComboBox<>(addresses);
        addressDropdown.setBounds(100, 130, 176, 25);
        add(addressDropdown);
        
        JButton registerButton = new JButton("�����ϱ�");
        registerButton.setBounds(100, 200, 100, 25);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // �Էµ� ũ�� ���� ��������
                String crewName = nameField.getText();
                String intro = introArea.getText();
                
                String addressName = (String) addressDropdown.getSelectedItem();
                Integer addressNo = addressMap.get(addressName);

                
               
                // ũ�� ������ crew ���̺� �����ϱ� ���� INSERT ���� �ۼ�
                String query = "INSERT INTO crew (crew_name, crew_intro, addr_no) VALUES (?, ?, ?)";

                try (Connection con = Jdbc.get();
                     PreparedStatement psmt = con.prepareStatement(query)) {

                    // ������ ?�� �� �Ҵ�
                    psmt.setString(1, crewName);
                    psmt.setString(2, intro);
                    psmt.setInt(3, addressNo);

                    // ���� ����
                    int rowsInserted = psmt.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(CrewMake.this, "ũ�� ������ �Ϸ�Ǿ����ϴ�.");
                        // ũ�� ���� �Ϸ� �� ó���� ���� �߰�
                        CrewMake.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(CrewMake.this, "ũ�� ������ �����Ͽ����ϴ�. �ٽ� �õ����ּ���.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(CrewMake.this, "ũ�� ���� �� ������ �߻��Ͽ����ϴ�. �ٽ� �õ����ּ���.");
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
        new CrewMake();
    }
}
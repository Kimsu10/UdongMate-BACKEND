package Main;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Course extends MainTap {
    private static final long serialVersionUID = 1L;
    private JPanel contentPanel; // contentPanel�� ��� ������ ����

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Course frame = new Course();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Course() {
        super();
        setLocationRelativeTo(null);
        // contentPanel �ʱ�ȭ
        contentPanel = new JPanel(new BorderLayout());
        getContentPane().add(contentPanel, BorderLayout.CENTER); // contentPanel�� MainTap�� ����Ʈ �гο� �߰�

        // ���� �г� ����
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(150, 150, 100, 100)); // ���� ���� ����
        contentPanel.add(leftPanel, BorderLayout.WEST);

        // ��� ��ư ����
        JButton mountain = new JButton();
        mountain.setIcon(new ImageIcon("img/Mountain.png")); // ��� ������ �̹��� ����
        mountain.setPreferredSize(new Dimension(400, 400)); // ��ư ũ�� ����
        mountain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ��� ������ �̵�
                new Mountain().setVisible(true); // ��� �������� �̵��ϴ� �ڵ�
                dispose(); // ���� â �ݱ�
            }
        });
        
        Font menuFont1 = new Font("Gong Gothic Bold", Font.PLAIN, 30);
        JLabel mountainLabel = new JLabel("���"); // ��ư �Ʒ��� ǥ�õ� �ؽ�Ʈ
        mountainLabel.setFont(menuFont1);
        mountainLabel.setHorizontalAlignment(SwingConstants.CENTER); // �ؽ�Ʈ ��� ����

        // ��� ��ư�� ���� �߰�
        leftPanel.add(mountain, BorderLayout.NORTH);
        leftPanel.add(mountainLabel, BorderLayout.CENTER);

        // ������ �г� ����
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(150, 100, 100, 150)); // ������ ���� ����
        contentPanel.add(rightPanel, BorderLayout.EAST);

        // ��å ��ư ����
        JButton ground = new JButton();
        ground.setIcon(new ImageIcon("img/Ground.png")); // ��å ������ �̹��� ����
        ground.setPreferredSize(new Dimension(400, 400)); // ��ư ũ�� ����
        ground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ��å ������ �̵�
                new Ground().setVisible(true); // ��å �������� �̵��ϴ� �ڵ�
                dispose(); // ���� â �ݱ�
            }
        });
        JLabel groundLabel = new JLabel("��å/����"); // ��ư �Ʒ��� ǥ�õ� �ؽ�Ʈ
        groundLabel.setFont(menuFont1);
        groundLabel.setHorizontalAlignment(SwingConstants.CENTER); // �ؽ�Ʈ ��� ����

        // ��å ��ư�� ���� �߰�
        rightPanel.add(ground, BorderLayout.NORTH);
        rightPanel.add(groundLabel, BorderLayout.CENTER);
    }
}
package Main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class MainPage extends MainTap {
	   
	   
    private static final long serialVersionUID = 1L;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	MainPage frame = new MainPage();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainPage() {
    	super();
    	setLocationRelativeTo(null);
    	//�̹���
        JLabel BackImg = new JLabel();
//        BackImg.setBounds(200, 200, 200, 200); // ũ�� ������ ��
        BackImg.setHorizontalAlignment(JLabel.CENTER); //JLabel
        BackImg.setIcon(new ImageIcon("img/main44.png"));
      
        getContentPane().add(BackImg);           // frame �ȿ� �̹����߰�
    }
}
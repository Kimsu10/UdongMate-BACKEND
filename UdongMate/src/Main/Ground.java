package Main;

import java.awt.*;
import javax.swing.*;

public class Ground extends MainTap {

   private JPanel contentPane;

    public static void main(String[] args) {
        // Create a new instance of CustomFrame
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                   Ground frame = new Ground();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Ground() {
        // 부모 클래스의 생성자 호출
        super();


    }

}
package Main;

import java.awt.*;
import javax.swing.*;

public class WeatherAppPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public WeatherAppPanel(WeatherApp weatherApp) {
        setPreferredSize(new Dimension(70,  600)); // JPanel�� ũ�� ����
        setBackground(Color.BLACK); // ������ ���������� ����
        // WeatherApp�� JPanel�� �߰�
        add(weatherApp.getWeatherPanel());
    }
}
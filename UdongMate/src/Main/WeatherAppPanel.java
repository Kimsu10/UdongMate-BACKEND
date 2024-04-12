package Main;

import java.awt.*;
import javax.swing.*;

public class WeatherAppPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public WeatherAppPanel(WeatherApp weatherApp) {
        setPreferredSize(new Dimension(70,  600)); // JPanel의 크기 설정
        setBackground(Color.BLACK); // 배경색을 검은색으로 설정
        // WeatherApp을 JPanel에 추가
        add(weatherApp.getWeatherPanel());
    }
}
package Main;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import javax.swing.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class WeatherApp extends JFrame {

    private static final long serialVersionUID = 1L;

    private JScrollPane scrollPane;
    private JPanel weatherPanel;
    private List<JSONObject> weatherData;
    private int currentIndex = 0;
    
    private static final int DELAY = 3000; // 3초
    private long lastDisplayedTime = 0;

    private static final String API_KEY = "디스코드 확인후 붙여넣기";

    public WeatherApp() {
        setTitle("Weather App");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        weatherPanel = new JPanel();
        weatherPanel.setLayout(new FlowLayout());
        weatherPanel.setBackground(Color.BLACK); // 패널의 배경색을 검은색으로 설정

        scrollPane = new JScrollPane(weatherPanel);
        scrollPane.getViewport().setBackground(Color.BLACK); // 배경색을 검은색으로 설정

        //scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0)); // 스크롤 패널의 여백 설정

        panel.add(scrollPane);

        add(panel);

        weatherData = new ArrayList<>();

        fetchWeatherData(); // 날씨 데이터 가져오기
        startAnimation(); // 애니메이션 시작
    }

    private void fetchWeatherData() {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?lat=37.5665&lon=126.978&appid=" + API_KEY + "&units=metric&lang=kr");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();

            String jsonResponse = response.toString();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);

            JSONArray forecasts = (JSONArray) jsonObject.get("list");

            for (int i = 0; i < forecasts.size(); i++) {
                JSONObject forecast = (JSONObject) forecasts.get(i);
                String dateTime = (String) forecast.get("dt_txt");
                if (dateTime.contains("12:00:00")) { // 12시 데이터만 처리
                    weatherData.add(forecast);
                }
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatDate(String date) {
        String[] parts = date.split("-");
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return month + "월 " + day + "일";
    }

    private void displayNextWeather() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDisplayedTime >= DELAY) {
            lastDisplayedTime = currentTime;

            if (currentIndex >= weatherData.size()) {
                currentIndex = 0; // 날씨 데이터를 모두 표시한 후에는 처음부터 다시 시작
            }

            JSONObject forecast = weatherData.get(currentIndex);
            String date = ((String) forecast.get("dt_txt")).split(" ")[0];
            String formattedDate = formatDate(date);
            double temperature = (double) ((JSONObject) forecast.get("main")).get("temp");
            String iconCode = (String) ((JSONObject) ((JSONArray) forecast.get("weather")).get(0)).get("icon");

            JLabel dateLabel = new JLabel(formattedDate);
            JLabel tempLabel = new JLabel(String.format("%.1f°C", temperature));
            ImageIcon icon = getWeatherIcon(iconCode);
            JLabel iconLabel = new JLabel(icon);

            dateLabel.setForeground(Color.WHITE); // 폰트 색상을 흰색으로 설정
            tempLabel.setForeground(Color.WHITE); // 폰트 색상을 흰색으로 설정
            Font menuFont = new Font("Gong Gothic Light", Font.PLAIN, 18);
            dateLabel.setFont(menuFont);
            tempLabel.setFont(menuFont);
            JPanel entryPanel = new JPanel();
            entryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            entryPanel.setBackground(Color.BLACK); // 패널의 배경색을 검은색으로 설정
            entryPanel.add(dateLabel);
            entryPanel.add(iconLabel);
            entryPanel.add(tempLabel);

            weatherPanel.removeAll();
            weatherPanel.add(entryPanel);
            weatherPanel.revalidate();
            weatherPanel.repaint();

            currentIndex++;
        }
    }

    private ImageIcon getWeatherIcon(String iconCode) {
        try {
            URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconCode + ".png");
            ImageIcon icon = new ImageIcon(iconUrl);
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // 이미지 크기 조절
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void startAnimation() {
        if (weatherData.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No weather data available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        displayNextWeather();
                    }
                });
            }
        }, 0, DELAY, TimeUnit.MILLISECONDS);
    }

    public JPanel getWeatherPanel() {
        return weatherPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WeatherApp weatherApp = new WeatherApp();
                weatherApp.setVisible(true);
            }
        });
    }
}
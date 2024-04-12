package Main;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

import javax.swing.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class WeatherApp extends JFrame {
    private JScrollPane scrollPane;
    private JPanel weatherPanel;
    private List<JSONObject> weatherData;
    private int currentIndex = 0;
    private Timer timer;
    private static final int DELAY = 3000; // 3초

    private static final String API_KEY = "디스코드 확인후 변경하기";

    public WeatherApp() {
        setTitle("Weather App");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(weatherPanel);

        panel.add(scrollPane, BorderLayout.CENTER);

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

    private void startAnimation() {
        if (weatherData.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No weather data available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        displayNextWeather();
                    }
                });
            }
        }, 0, DELAY);
    }

    private void displayNextWeather() {
        if (currentIndex >= weatherData.size()) {
            currentIndex = 0; // 날씨 데이터를 모두 표시한 후에는 처음부터 다시 시작
        }

        JSONObject forecast = weatherData.get(currentIndex);
        String date = ((String) forecast.get("dt_txt")).split(" ")[0];
        String formattedDate = formatDate(date);
        double temperature = (double) ((JSONObject) forecast.get("main")).get("temp");
        // String description = (String) ((JSONObject) ((JSONArray) forecast.get("weather")).get(0)).get("description");
        String iconCode = (String) ((JSONObject) ((JSONArray) forecast.get("weather")).get(0)).get("icon");

        JLabel dateLabel = new JLabel(formattedDate);
        JLabel tempLabel = new JLabel(String.format("%.1f°C", temperature));
        // JLabel descLabel = new JLabel(description);
        ImageIcon icon = getWeatherIcon(iconCode);
        JLabel iconLabel = new JLabel(icon);

        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        entryPanel.add(dateLabel);
        entryPanel.add(iconLabel);
        entryPanel.add(tempLabel);
        // entryPanel.add(descLabel);

        weatherPanel.removeAll();
        weatherPanel.add(entryPanel);
        weatherPanel.revalidate();
        weatherPanel.repaint();

        currentIndex++;
    }

    private ImageIcon getWeatherIcon(String iconCode) {
        try {
            URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconCode + ".png");
            ImageIcon icon = new ImageIcon(iconUrl);
            return icon;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String formatDate(String date) {
        String[] parts = date.split("-");
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return month + "월 " + day + "일";
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
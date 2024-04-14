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
    
    private static final int DELAY = 3000; // 3��
    private long lastDisplayedTime = 0;

    private static final String API_KEY = "���ڵ� Ȯ���� �ٿ��ֱ�";

    public WeatherApp() {
        setTitle("Weather App");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        weatherPanel = new JPanel();
        weatherPanel.setLayout(new FlowLayout());
        weatherPanel.setBackground(Color.BLACK); // �г��� ������ ���������� ����

        scrollPane = new JScrollPane(weatherPanel);
        scrollPane.getViewport().setBackground(Color.BLACK); // ������ ���������� ����

        //scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0)); // ��ũ�� �г��� ���� ����

        panel.add(scrollPane);

        add(panel);

        weatherData = new ArrayList<>();

        fetchWeatherData(); // ���� ������ ��������
        startAnimation(); // �ִϸ��̼� ����
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
                if (dateTime.contains("12:00:00")) { // 12�� �����͸� ó��
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
        return month + "�� " + day + "��";
    }

    private void displayNextWeather() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDisplayedTime >= DELAY) {
            lastDisplayedTime = currentTime;

            if (currentIndex >= weatherData.size()) {
                currentIndex = 0; // ���� �����͸� ��� ǥ���� �Ŀ��� ó������ �ٽ� ����
            }

            JSONObject forecast = weatherData.get(currentIndex);
            String date = ((String) forecast.get("dt_txt")).split(" ")[0];
            String formattedDate = formatDate(date);
            double temperature = (double) ((JSONObject) forecast.get("main")).get("temp");
            String iconCode = (String) ((JSONObject) ((JSONArray) forecast.get("weather")).get(0)).get("icon");

            JLabel dateLabel = new JLabel(formattedDate);
            JLabel tempLabel = new JLabel(String.format("%.1f��C", temperature));
            ImageIcon icon = getWeatherIcon(iconCode);
            JLabel iconLabel = new JLabel(icon);

            dateLabel.setForeground(Color.WHITE); // ��Ʈ ������ ������� ����
            tempLabel.setForeground(Color.WHITE); // ��Ʈ ������ ������� ����
            Font menuFont = new Font("Gong Gothic Light", Font.PLAIN, 18);
            dateLabel.setFont(menuFont);
            tempLabel.setFont(menuFont);
            JPanel entryPanel = new JPanel();
            entryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            entryPanel.setBackground(Color.BLACK); // �г��� ������ ���������� ����
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
            Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // �̹��� ũ�� ����
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
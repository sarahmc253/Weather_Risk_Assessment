package main.java.risk.assessment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Display {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Risk Assessment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);


        JLabel experienceLabel = new JLabel("Select crew experience level:");
        Integer[] experienceOptions = {1, 2, 3};
        JComboBox<Integer> experienceField = new JComboBox<>(experienceOptions);

        JLabel boatSizeLabel = new JLabel("Select boat size:");
        Integer[] boatSizeOptions = {1, 2, 4, 8};
        JComboBox<Integer> boatSizeField = new JComboBox<>(boatSizeOptions);

        JLabel dateLabel = new JLabel("Select a date within the next week:");
        JLabel hourLabel = new JLabel("Select an hour:");

        // Scroll pane for dates
        DefaultListModel<String> dateModel = new DefaultListModel<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 3; i++) {
            dateModel.addElement(today.plusDays(i).format(dateFormatter));
        }
        JList<String> dateList = new JList<>(dateModel);
        dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane dateScrollPane = new JScrollPane(dateList);

        // Scroll pane for hours
        DefaultListModel<String> hourModel = new DefaultListModel<>();
        for (int i = 0; i < 24; i++) {
            hourModel.addElement(String.format("%02d:00", i)); // Format as "00:00", "01:00", etc.
        }
        JList<String> hourList = new JList<>(hourModel);
        hourList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane hourScrollPane = new JScrollPane(hourList);

        JButton calculateButton = new JButton("Calculate Risk Score");
        JTextArea resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);

        // Layout setup
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1, 10, 10));
        panel.add(experienceLabel);
        panel.add(experienceField);
        panel.add(boatSizeLabel);
        panel.add(boatSizeField);
        panel.add(dateLabel);
        panel.add(dateScrollPane);
        panel.add(hourLabel);
        panel.add(hourScrollPane);
        panel.add(calculateButton);


        frame.add(panel, BorderLayout.CENTER);
        frame.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        // Action listener for the button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer experience = (Integer) experienceField.getSelectedItem();
                    Integer boatSize = (Integer) boatSizeField.getSelectedItem();

                    String selectedDate = dateList.getSelectedValue();
                    String selectedHour = hourList.getSelectedValue();
                    if (selectedDate == null || selectedHour == null) {
                        JOptionPane.showMessageDialog(frame, "Please select both a date and an hour.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }


                    LocalDate date = LocalDate.parse(selectedDate, dateFormatter); // combining the date and hour
                    LocalTime time = LocalTime.parse(selectedHour);
                    LocalDateTime localDateTime = LocalDateTime.of(date, time);

                    // Convert to UTC ISO format
                    ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
                    DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    String targetTime = utcDateTime.format(isoFormatter);

                    Safety safety = new Safety();
                    double[] weatherDetails = safety.fetchingWeatherData(targetTime);
                    int windSpeed = (int) weatherDetails[0];
                    int windDirection = (int) weatherDetails[1];
                    int airTemp = (int) weatherDetails[2];

                    int initialRating = safety.initialRating(experience, boatSize);
                    int windRating = safety.windRating(windSpeed, windDirection);
                    int temperatureRating = safety.airTemp(airTemp);

                    int finalScore = initialRating + windRating + temperatureRating;

                    String decision = "";

                    if (finalScore <=9) {
                        decision = "Rowing allowed for all crews.";
                    }
                    else if (finalScore >= 10 && finalScore < 15) {
                        decision = "No rowing for crew types 1 or 2.";
                    }
                    else if (finalScore >= 15) {
                        decision = "No rowing.";
                    }

                    resultArea.append("Final Score is " + finalScore + ".\n" + decision + "\n");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numerical values.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }
}

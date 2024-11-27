package risk.assessment;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class Safety {

    public int initialRating(int experience, int boatSize) {
        int score = 0;

        // crew experience
        score += switch(experience) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            default -> 0;
        };


        // boat size
        score += switch(boatSize) {
            case 1 -> 4;
            case 2 -> 3;
            case 4 -> 2;
            case 8 -> 1;
            default -> 0;
        };

        return score;
    }

    public double[] fetchingWindData(){
        String url = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=52.668018&lon=-8.630498"; // Replace with your location's latitude/longitude
        String targetTime = "2024-11-27T17:00:00Z"; // Replace with your desired time (ISO 8601 format)
        double windSpeed = 0;
        double windDirection = 0;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Weather_Risk_Assessment (24403067@studentmail.ul.ie)") // Replace with your details
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Request failed: " + response.code());
            }

            // Parse the JSON response
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);

            // Navigate to the timeseries array
            JSONArray timeseries = json.getJSONObject("properties").getJSONArray("timeseries");

            // Search for the specific time
            boolean found = false;
            for (int i = 0; i < timeseries.length(); i++) {
                JSONObject dataPoint = timeseries.getJSONObject(i);
                String time = dataPoint.getString("time");

                if (time.equals(targetTime)) {
                    // Extract wind details
                    JSONObject details = dataPoint.getJSONObject("data").getJSONObject("instant").getJSONObject("details");
                    windSpeed = details.getDouble("wind_speed");
                    windDirection = details.getDouble("wind_from_direction");

                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException("No data available for specified time");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double[] windDetails = {windSpeed, windDirection};

        return windDetails;
    }

    public int windRating(int windSpeed, int windDirection) {
        int windScore = 0;

        if (windDirection >= 225 && windDirection <= 315) {
            windScore += switch (windSpeed) { // westerly winds
                case 0, 1, 2, 3 -> 1;
                case 4 -> 8;
                case 5 -> 10;
                case 6 -> 15;
                case 7 -> 20;
                default -> 25;
            };
        }
        else if (windDirection >= 45 && windDirection <= 135) { // easterly winds
            windScore += switch (windSpeed) {
                case 0, 1, 2, 3 -> 1;
                case 4 -> 8;
                case 5 -> 10;
                case 6 -> 12;
                case 7 -> 15;
                default -> 20;
            };
        }
        else if (windDirection >= 0 &&  windDirection < 360) { // other directions
            windScore += switch(windSpeed) {
                case 0, 1, 2, 3 -> 0;
                case 4 -> 2;
                case 5 -> 5;
                case 6 -> 6;
                case 7 -> 8;
                default -> 16;
            };
        }
        else {
            throw new IllegalArgumentException("Wind direction is invalid");
        }

        return windScore;
    }

    public int flowRating(int flowSpeed) {
        int flowScore = 0;

        if(flowSpeed >= 100 && flowSpeed < 200) {
            flowScore += 1;
        }
        else if(flowSpeed >= 200 && flowSpeed < 300) {
            flowScore += 2;
        }
        else if(flowSpeed >= 300 && flowSpeed < 400) {
            flowScore += 4;
        }
        else if(flowSpeed >= 400 && flowSpeed < 500) {
            flowScore += 5;
        }
        else if(flowSpeed >= 500) {
            flowScore += 12;
        }
        else {
            throw new IllegalArgumentException("Flow rate must be a positive number");
        }

        return flowScore;
    }

    public int airTemp(int temperature) {
        int airScore = 0;

        if (temperature <= 0) {
            airScore += 2;
        }
        else if (temperature > 0 && temperature <= 5) {
            airScore += 1;
        }

        return airScore;
    }
}
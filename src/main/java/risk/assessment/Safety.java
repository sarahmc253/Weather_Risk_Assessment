package risk.assessment;

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
            case 3 -> 2;
            case 4 -> 1;
            default -> 0;
        };

        return score;
    }

    public int windRating(int windDirection, int windSpeed) {
        int windScore = 0;

        if (windDirection == 1) {
            windScore += switch (windSpeed) { // westerly winds
                case 0, 1, 2, 3 -> 1;
                case 4 -> 8;
                case 5 -> 10;
                case 6 -> 15;
                case 7 -> 20;
                default -> 25;
            };
        }
        else if (windDirection == 2) { // easterly winds
            windScore += switch (windSpeed) {
                case 0, 1, 2, 3 -> 1;
                case 4 -> 8;
                case 5 -> 10;
                case 6 -> 12;
                case 7 -> 15;
                default -> 20;
            };
        }
        else if (windDirection == 3) { // other directions
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
            throw new IllegalArgumentException("Wind direction must be a value of 1, 2, 3");
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
        else {
        }

        return airScore;
    }
}
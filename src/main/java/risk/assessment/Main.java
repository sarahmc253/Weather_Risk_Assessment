package risk.assessment;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Safety safety = new Safety();

        System.out.print("Enter crew experience level: ");
        int experience = scanner.nextInt();

        System.out.print("Enter boat size: ");
        int boatSize = scanner.nextInt();

        double[] windDetails = safety.fetchingWindData();
        int windSpeed = (int) windDetails[0];
        int windDirection = (int) windDetails[1];

        System.out.print("Enter air temp: ");
        int airTemp = scanner.nextInt();

        int initialScore = safety.initialRating(experience, boatSize);
        int windScore = safety.windRating(windSpeed, windDirection);
        int airScore = safety.airTemp(airTemp);

        int finalScore = initialScore + windScore + airScore;

        System.out.println(finalScore);
    }
}
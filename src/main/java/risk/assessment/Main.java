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

        System.out.print("Enter wind direction: ");
        int windDirection = scanner.nextInt();

        System.out.print("Enter wind speed: ");
        int windSpeed = scanner.nextInt();

        System.out.print("Enter air temp: ");
        int airTemp = scanner.nextInt();

        int initialScore = safety.initialRating(experience, boatSize);
        int windScore = safety.windRating(windDirection, windSpeed);
        int airScore = safety.airTemp(airTemp);

        int finalScore = initialScore + windScore + airScore;

        System.out.println(finalScore);
    }
}
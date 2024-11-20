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

        safety.initialRating(experience, boatSize);
    }
}
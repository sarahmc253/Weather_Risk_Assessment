package risk.assessment;

public class Safety {
    public int score;

    public int initialRating(int a, int b) {
        if (a == 1) {
            score += 1;
        }
        else if (a == 2) {
            score += 2;
        }
        else if (a == 3) {
            score += 3;
        }
        else {
            throw new IllegalArgumentException("Must be between 1-3");
        }

        return score;
    }
}
import java.util.Random;

public class Die {
    public int DieNumber;
    public boolean MustBeRolled;
    public int Value;
    private static Random random = new Random();

    public Die(int dieNumber) {
        DieNumber = dieNumber;
        roll();
    }

    public int roll() {
        Value = random.nextInt(6) + 1;
        return Value;

    }
}

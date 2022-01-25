import java.util.*;

public class Yahtzee {
    private int upperSectionScore;
    private int round;
    private int currentScore;
    private boolean upperSectionBonusClaimed;
    private Die die1;
    private Die die2;
    private Die die3;
    private Die die4;
    private Die die5;
    private Box onesBox;
    private Box twosBox;
    private Box threesBox;
    private Box foursBox;
    private Box fivesBox;
    private Box sixesBox;
    private Box threeOfAKindBox;
    private Box fourOfAKindBox;
    private Box smallStraightBox;
    private Box bigStraightBox;
    private Box fullHouseBox;
    private Box chanceBox;
    private Box yahtzeeBox;
    List<Box> availableBoxes;
    Map<Integer, BoxType> optionToBoxTypeMap;
    ArrayList<Integer> Dice;

    public Yahtzee() {
        currentScore = 0;
        upperSectionScore = 0;
        upperSectionBonusClaimed = false;
        round = 0;
        die1 = new Die(1);
        die2 = new Die(2);
        die3 = new Die(3);
        die4 = new Die(4);
        die5 = new Die(5);
        onesBox = new Box(BoxType.Ones);
        twosBox = new Box(BoxType.Twos);
        threesBox = new Box(BoxType.Threes);
        foursBox = new Box(BoxType.Fours);
        fivesBox = new Box(BoxType.Fives);
        sixesBox = new Box(BoxType.Sixes);
        threeOfAKindBox = new Box(BoxType.ThreeOfAKind);
        fourOfAKindBox = new Box(BoxType.FourOfAKind);
        smallStraightBox = new Box(BoxType.SmallStraight);
        bigStraightBox = new Box(BoxType.BigStraight);
        fullHouseBox = new Box(BoxType.FullHouse);
        chanceBox = new Box(BoxType.Chance);
        yahtzeeBox = new Box(BoxType.Yahtzee);
    }

    public void StartGame() {
        boolean allBoxesAreFilled = false;
        while (true) {
            if (allBoxesAreFilled) {
                System.out.println("All boxes are filled. Game Over.");
                System.out.println("Total score: " + currentScore);
                System.exit(0);
            }

            round = 0;
            System.out.println("New turn started...");
            rollAll();
            PrintDieFaces(++round);
            while (round <= 3) {
                String rollOrSaveOption = GetRollOrSaveOptions();
                if (rollOrSaveOption.equals("reroll")) {
                    SetRerollOptions();
                    if (die1.MustBeRolled == true)
                        die1.roll();
                    if (die2.MustBeRolled == true)
                        die2.roll();
                    if (die3.MustBeRolled == true)
                        die3.roll();
                    if (die4.MustBeRolled == true)
                        die4.roll();
                    if (die5.MustBeRolled == true)
                        die5.roll();
                    PrintDieFaces(++round);
                    if (round == 3) {
                        allBoxesAreFilled = SaveTheThrow();
                        break;
                    }
                } else {
                    allBoxesAreFilled = SaveTheThrow();
                    break;
                }
            }
        }
    }

    private boolean SaveTheThrow() {
        PrintAvailableBoxes();
        int option = getSaveOption();
        BoxType boxType;
        if (optionToBoxTypeMap.containsKey(option)) {
            boxType = optionToBoxTypeMap.get(option);
            int score = SaveDiceInSelectedBox(boxType);
            if ((boxType == BoxType.Ones || boxType == BoxType.Twos || boxType == BoxType.Threes || boxType == BoxType.Fours || boxType == BoxType.Fives || boxType == BoxType.Sixes)) {
                upperSectionScore += score;
            }

            if (upperSectionScore >= 63 && upperSectionBonusClaimed == false) {
                currentScore += score + 35;
                upperSectionBonusClaimed = true;
                System.out.println("35 points bonus was claimed in upper section.");
            } else
                currentScore += score;

            System.out.println("Saved in the " + boxType.toString() + " totalling " + score + " points. Total score : " + currentScore);

            return allBoxesAreFilled();
        } else {
            System.err.println("Selected box not found");
            return true;
        }
    }

    private boolean allBoxesAreFilled() {
        if (onesBox.IsAvailable)
            return false;
        if (twosBox.IsAvailable)
            return false;
        if (threesBox.IsAvailable)
            return false;
        if (foursBox.IsAvailable)
            return false;
        if (fivesBox.IsAvailable)
            return false;
        if (sixesBox.IsAvailable)
            return false;
        if (threeOfAKindBox.IsAvailable)
            return false;
        if (fourOfAKindBox.IsAvailable)
            return false;
        if (smallStraightBox.IsAvailable)
            return false;
        if (bigStraightBox.IsAvailable)
            return false;
        if (fullHouseBox.IsAvailable)
            return false;
        if (yahtzeeBox.IsAvailable)
            return false;
        if (chanceBox.IsAvailable)
            return false;
        return true;
    }

    private int SaveDiceInSelectedBox(BoxType boxType) {
        switch (boxType) {
            case Ones:
                onesBox.SelectBox(Dice);
                return onesBox.calculateScore();
            case Twos:
                twosBox.SelectBox(Dice);
                return twosBox.calculateScore();
            case Threes:
                threesBox.SelectBox(Dice);
                return threesBox.calculateScore();
            case Fours:
                foursBox.SelectBox(Dice);
                return foursBox.calculateScore();
            case Fives:
                fivesBox.SelectBox(Dice);
                return fivesBox.calculateScore();
            case Sixes:
                sixesBox.SelectBox(Dice);
                return sixesBox.calculateScore();
            case ThreeOfAKind:
                threeOfAKindBox.SelectBox(Dice);
                return threeOfAKindBox.calculateScore();
            case FourOfAKind:
                fourOfAKindBox.SelectBox(Dice);
                return fourOfAKindBox.calculateScore();
            case SmallStraight:
                smallStraightBox.SelectBox(Dice);
                return smallStraightBox.calculateScore();
            case BigStraight:
                bigStraightBox.SelectBox(Dice);
                return bigStraightBox.calculateScore();
            case FullHouse:
                fullHouseBox.SelectBox(Dice);
                return fullHouseBox.calculateScore();
            case Yahtzee:
                yahtzeeBox.SelectBox(Dice);
                return yahtzeeBox.calculateScore();
            case Chance:
                chanceBox.SelectBox(Dice);
                return chanceBox.calculateScore();
            default:
                return 0;
        }
    }

    private int getSaveOption() {
        Scanner scanner = new Scanner(System.in);
        int option;
        while (true) {
            System.out.println("Where will you save your throw? (enter a number)");

            while (!scanner.hasNextInt()) {
                System.err.println("Invalid input. Please enter a valid option.");
                scanner.next();
            }
            option = scanner.nextInt();
            if (option < 1 || option > availableBoxes.size()) {
                System.err.println("Invalid input. Please enter a valid option.");
                continue;
            } else
                break;
        }
        return option;
    }

    private void PrintAvailableBoxes() {
        Dice = new ArrayList<Integer>();
        Dice.add(die1.Value);
        Dice.add(die2.Value);
        Dice.add(die3.Value);
        Dice.add(die4.Value);
        Dice.add(die5.Value);

        availableBoxes = new ArrayList<Box>();
        optionToBoxTypeMap = new HashMap<Integer, BoxType>();

        if (onesBox.IsMatch(BoxType.Ones, Dice) && onesBox.IsAvailable)
            availableBoxes.add(onesBox);

        if (twosBox.IsMatch(BoxType.Twos, Dice) && twosBox.IsAvailable)
            availableBoxes.add(twosBox);

        if (threesBox.IsMatch(BoxType.Threes, Dice) && threesBox.IsAvailable)
            availableBoxes.add(threesBox);

        if (foursBox.IsMatch(BoxType.Fours, Dice) && foursBox.IsAvailable)
            availableBoxes.add(foursBox);

        if (fivesBox.IsMatch(BoxType.Fives, Dice) && fivesBox.IsAvailable)
            availableBoxes.add(fivesBox);

        if (sixesBox.IsMatch(BoxType.Sixes, Dice) && sixesBox.IsAvailable)
            availableBoxes.add(sixesBox);

        if (threeOfAKindBox.IsMatch(BoxType.ThreeOfAKind, Dice) && threeOfAKindBox.IsAvailable)
            availableBoxes.add(threeOfAKindBox);

        if (fourOfAKindBox.IsMatch(BoxType.FourOfAKind, Dice) && fourOfAKindBox.IsAvailable)
            availableBoxes.add(fourOfAKindBox);

        if (fullHouseBox.IsMatch(BoxType.FullHouse, Dice) && fullHouseBox.IsAvailable)
            availableBoxes.add(fullHouseBox);

        if (smallStraightBox.IsMatch(BoxType.SmallStraight, Dice) && smallStraightBox.IsAvailable)
            availableBoxes.add(smallStraightBox);

        if (bigStraightBox.IsMatch(BoxType.BigStraight, Dice) && bigStraightBox.IsAvailable)
            availableBoxes.add(bigStraightBox);

        if (yahtzeeBox.IsMatch(BoxType.Yahtzee, Dice) && yahtzeeBox.IsAvailable)
            availableBoxes.add(yahtzeeBox);

        if (chanceBox.IsAvailable && chanceBox.IsAvailable)
            availableBoxes.add(chanceBox);

        int option = 0;
        if (availableBoxes.size() > 0) {
            System.out.println("You have the following options:");
            for (Box box : availableBoxes) {
                System.out.println(++option + ") " + box.BoxType.toString());
                optionToBoxTypeMap.put(option, box.BoxType);
            }
            return;
        } else {
            System.out.println("The throw does not match in any box. Select a box to discard it with 0 point.");
            if (onesBox.IsAvailable)
                availableBoxes.add(onesBox);

            if (twosBox.IsAvailable)
                availableBoxes.add(twosBox);

            if (threesBox.IsAvailable)
                availableBoxes.add(threesBox);

            if (foursBox.IsAvailable)
                availableBoxes.add(foursBox);

            if (fivesBox.IsAvailable)
                availableBoxes.add(fivesBox);

            if (sixesBox.IsAvailable)
                availableBoxes.add(sixesBox);

            if (threeOfAKindBox.IsAvailable)
                availableBoxes.add(threeOfAKindBox);

            if (fourOfAKindBox.IsAvailable)
                availableBoxes.add(fourOfAKindBox);

            if (fullHouseBox.IsAvailable)
                availableBoxes.add(fullHouseBox);

            if (smallStraightBox.IsAvailable)
                availableBoxes.add(smallStraightBox);

            if (bigStraightBox.IsAvailable)
                availableBoxes.add(bigStraightBox);

            if (yahtzeeBox.IsAvailable)
                availableBoxes.add(yahtzeeBox);

            if (chanceBox.IsAvailable)
                availableBoxes.add(chanceBox);

            System.out.println("You have the following options:");
            for (Box box : availableBoxes) {
                System.out.println(++option + ") " + box.BoxType.toString());
                optionToBoxTypeMap.put(option, box.BoxType);
            }
            return;
        }
    }

    private void PrintDieFaces(int round) {
        System.out.println("\n\nYour roll (" + round + " of 3):  " + die1.Value + " " + die2.Value + " " + die3.Value + " " + die4.Value + " " + die5.Value);
    }

    private void rollAll() {
        die1.roll();
        die2.roll();
        die3.roll();
        die4.roll();
        die5.roll();
    }

    private String GetRollOrSaveOptions() {
        Scanner scanner = new Scanner(System.in);
        String option;

        while (true) {
            System.out.println("Choose reroll or save: ");
            option = scanner.nextLine();

            if (!option.toLowerCase().equals("save") && !option.toLowerCase().equals("reroll")) {
                System.err.println("Invalid input. Please enter a valid option.");
                continue;
            } else
                break;
        }

        if (option.equals("save")) {
            return "save";
        } else {
            return "reroll";
        }
    }

    private void SetRerollOptions() {
        SetdieRollOption(die1);
        SetdieRollOption(die2);
        SetdieRollOption(die3);
        SetdieRollOption(die4);
        SetdieRollOption(die5);
    }

    private void SetdieRollOption(Die die) {
        Scanner scanner = new Scanner(System.in);
        String option;

        while (true) {
            System.out.println("Should die" + die.DieNumber + "(currently a " + die.Value + ")  be rerolled? (yes/no)");
            option = scanner.nextLine();

            if (!option.toLowerCase().equals("yes") && !option.toLowerCase().equals("no")) {
                System.err.println("Invalid input. Please enter a valid option.");
                continue;
            } else {
                if (option.equals("yes"))
                    die.MustBeRolled = true;
                else
                    die.MustBeRolled = false;
                break;
            }
        }
    }
}

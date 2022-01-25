import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;

public class Box {
    public boolean IsAvailable;
    public BoxType BoxType;
    public ArrayList<Integer> Dice; // values on dice will be send to this

    public Box(BoxType boxType) {
        IsAvailable = true;
        BoxType = boxType;
        Dice = new ArrayList<Integer>();
    }

    public void SelectBox(ArrayList<Integer> dice) {
        Dice = dice;
        IsAvailable = false;
    }

    public int calculateScore() {
        switch (BoxType) {
            case Ones:
                return CalculateCountCategory(1, Dice);
            case Twos:
                return CalculateCountCategory(2, Dice);
            case Threes:
                return CalculateCountCategory(3, Dice);
            case Fours:
                return CalculateCountCategory(4, Dice);
            case Fives:
                return CalculateCountCategory(5, Dice);
            case Sixes:
                return CalculateCountCategory(6, Dice);
            case Chance:
                return CalculateChanceScore(Dice);
            case FourOfAKind :
            case ThreeOfAKind:
                return CalculateThreeOrFourOfAKindScore(Dice);
            case SmallStraight:
                return CalculateSmallStraightScore(Dice);
            case BigStraight:
                return CalculateBigStraightScore(Dice);
            case FullHouse:
                return CalculateFullHouseScore(Dice);
            case Yahtzee:
                return CalculateYahtzeeScore(Dice);
            default:
                return 0;
        }
    }

    private int CalculateCountCategory(int dieFace, ArrayList<Integer> dice) {
        return (int) dice.stream().filter(x -> x == dieFace).count() * dieFace;
    }

    private int CalculateChanceScore(ArrayList<Integer> dice) {
        return (int) dice.stream().mapToLong(integer -> integer.longValue()).sum();
    }

    private int CalculateThreeOrFourOfAKindScore(ArrayList<Integer> dice) {
        if (MatchesThreeOfAKind(dice) || MatchesFourOfAKind(dice))
            return (int) dice.stream().mapToLong(Integer::longValue).sum();
        else
            return 0;
    }

    private int CalculateSmallStraightScore(ArrayList<Integer> dice) {
        Integer[] uniqueDieFaces = dice.stream()
                .collect(groupingBy(Integer::intValue)).keySet().toArray(Integer[]::new);

        if (uniqueDieFaces.length < 4)
            return 0;

        int count = 0;
        for (int i = 1; i < uniqueDieFaces.length; i++) {
            if (uniqueDieFaces[i] - uniqueDieFaces[i - 1] > 1)
                count++;
        }

        if (uniqueDieFaces.length < 5 && count >= 1)
            return 0;
        else
            return 30;
    }

    private int CalculateBigStraightScore(ArrayList<Integer> dice) {
        Integer[] uniqueDieFaces = dice.stream()
                .collect(groupingBy(Integer::intValue)).keySet().toArray(Integer[]::new);

        if (uniqueDieFaces.length < 5)
            return 0;

        int count = 0;
        for (int i = 1; i < uniqueDieFaces.length; i++) {
            if (uniqueDieFaces[i] - uniqueDieFaces[i - 1] > 1)
                count++;
        }

        if (count > 0)
            return 0;
        else
            return 30;
    }

    private int CalculateFullHouseScore(ArrayList<Integer> dice) {
        Map<Integer, List<Integer>> map = dice.stream()
                .collect(groupingBy(integer -> integer.intValue()));

        var countThrees = map.values().stream().filter(x -> x.size() == 3).count();
        var countTwos = map.values().stream().filter(x -> x.size() == 2).count();
        if (countThrees == 3 && countTwos == 2)
            return 25;
        else
            return 0;
    }

    private int CalculateYahtzeeScore(ArrayList<Integer> dice) {
        Map<Integer, List<Integer>> map = dice.stream()
                .collect(groupingBy(Integer::intValue));

        var count = map.values().stream().filter(x -> x.size() == 5).count();
        if (count == 5)
            return 50;
        else
            return 0;
    }

    public boolean IsMatch(BoxType boxType, ArrayList<Integer> Dice) {
        switch (BoxType) {
            case Ones:
                return MatchesNumber(1, Dice);
            case Twos:
                return MatchesNumber(2, Dice);
            case Threes:
                return MatchesNumber(3, Dice);
            case Fours:
                return MatchesNumber(4, Dice);
            case Fives:
                return MatchesNumber(5, Dice);
            case Sixes:
                return MatchesNumber(6, Dice);
            case ThreeOfAKind:
                return MatchesThreeOfAKind(Dice);
            case FourOfAKind:
                return MatchesFourOfAKind(Dice);
            case SmallStraight:
                return MatchesSmallStraight(Dice);
            case BigStraight:
                return MatchesBigStraight(Dice);
            case FullHouse:
                return MatchesFullHouse(Dice);
            case Yahtzee:
                return MatchesYahtzee(Dice);
            default:
                return false;
        }
    }

    private boolean MatchesNumber(int number, ArrayList<Integer> dice) {
        return dice.stream().filter(x -> x == number).count() > 0;
    }

    private boolean MatchesThreeOfAKind(ArrayList<Integer> dice) {
        Map<Integer, List<Integer>> map = dice.stream()
                .collect(groupingBy(Integer::intValue));

        var count = map.values().stream().filter(x -> x.size() == 3).count();

        return count != 0;
    }

    private boolean MatchesFourOfAKind(ArrayList<Integer> dice) {
        Map<Integer, List<Integer>> map = dice.stream()
                .collect(groupingBy(integer -> integer.intValue()));

        var count = map.values().stream().filter(x -> x.size() == 4).count();

        return count != 0;
    }

    private boolean MatchesSmallStraight(ArrayList<Integer> dice) {
        Integer[] uniqueDieFaces = dice.stream()
                .collect(groupingBy(Integer::intValue)).keySet().toArray(Integer[]::new);

        if (uniqueDieFaces.length < 4)
            return false;

        int count = 0;
        for (int i = 1; i < uniqueDieFaces.length; i++) {
            if (uniqueDieFaces[i] - uniqueDieFaces[i - 1] > 1)
                count++;
        }

        if ((uniqueDieFaces.length == 4 && count >= 1) || (uniqueDieFaces.length == 5 && count > 1))
            return false;
        else
            return true;
    }

    private boolean MatchesBigStraight(ArrayList<Integer> dice) {
        Integer[] uniqueDieFaces = dice.stream()
                .collect(groupingBy(x -> x.intValue())).keySet().toArray(value -> new Integer[value]);

        if (uniqueDieFaces.length < 5)
            return false;

        int count = 0;
        for (int i = 1; i < uniqueDieFaces.length; i++) {
            if (uniqueDieFaces[i] - uniqueDieFaces[i - 1] > 1)
                count++;
        }

        if (count >= 1)
            return false;
        else
            return true;
    } 

    private boolean MatchesFullHouse(ArrayList<Integer> dice) {
        Map<Integer, List<Integer>> map = dice.stream()
                .collect(groupingBy(Integer::intValue));

        var countThrees = map.values().stream().filter(x -> x.size() == 3).count();
        var countTwos = map.values().stream().filter(x -> x.size() == 2).count();
        if (countThrees == 1 && countTwos == 1)
            return true;
        else
            return false;
    }

    private boolean MatchesYahtzee(ArrayList<Integer> dice) {
        Map<Integer, List<Integer>> map = dice.stream()
                .collect(groupingBy(integer -> integer.intValue()));

        var count = map.values().stream().filter(x -> x.size() == 5).count();
        if (count == 1)
            return true;
        else
            return false;
    }

}

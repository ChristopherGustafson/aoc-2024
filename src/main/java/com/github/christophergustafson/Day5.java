package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.parseLinesOfIntegers;
import static com.github.christophergustafson.utils.ParseUtils.readInputFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("Duplicates")
public class Day5 {

    public static void main(String[] args) {

        String testInput = """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13
            
            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47""";
        String realInput = readInputFile("day5.txt");

        int part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        int part2Test = part2(parseInput(testInput));
        System.out.println("Part 2, test: " + part2Test);

        int part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static ParsedInput parseInput(String input) {
        String[] splitInput = input.split("\n\n");
        PageOrderingRule[] rules = Arrays.stream(splitInput[0].split("\n"))
            .map(line -> {
                String[] rule = line.split("\\|");
                return new PageOrderingRule(Integer.parseInt(rule[0]), Integer.parseInt(rule[1]));
            }).toArray(PageOrderingRule[]::new);
        int[][] updates = parseLinesOfIntegers(splitInput[1], ",");
        return new ParsedInput(rules, updates);
    }

    record ParsedInput(PageOrderingRule[] rules, int[][] updates) { }

    record PageOrderingRule(int left, int right) { }

    static int part1(ParsedInput input) {
        Map<Integer, Set<Integer>> ruleMap = createRuleMap(input.rules);

        int result = 0;
        for (int[] update : input.updates) {
            boolean rightOrder = true;
            for (int i = 0; i < update.length; i++) {
                for (int j = i - 1; j > -1; j--) {
                    if (ruleMap.containsKey(update[i]) && ruleMap.get(update[i]).contains(update[j])) {
                        rightOrder = false;
                        break;
                    }
                }
            }
            if (rightOrder) {
                result += update[(update.length - 1) / 2];
            }
        }
        return result;
    }

    static int part2(ParsedInput input) {
        Map<Integer, Set<Integer>> ruleMap = createRuleMap(input.rules);

        int result = 0;
        for (int[] update : input.updates) {
            List<Integer> updateList = new java.util.ArrayList<>(Arrays.stream(update).boxed().toList());
            boolean orderUpdated = false;
            for (int i = 0; i < updateList.size(); i++) {
                Integer element = updateList.get(i);
                int putElementAt = i;
                for (int j = i - 1; j > -1; j--) {
                    if (ruleMap.containsKey(element) && ruleMap.get(element).contains(updateList.get(j))) {
                        putElementAt = j;
                        orderUpdated = true;
                    }
                }
                if (putElementAt != i) {
                    updateList.remove(i);
                    updateList.add(putElementAt, element);
                }
            }
            if (orderUpdated) {
                result += updateList.get((updateList.size() - 1) / 2);
            }
        }
        return result;
    }

    private static Map<Integer, Set<Integer>> createRuleMap(PageOrderingRule[] rules) {
        Map<Integer, Set<Integer>> ruleMap = new HashMap<>();
        for (PageOrderingRule rule : rules) {
            if (ruleMap.containsKey(rule.left)) {
                ruleMap.get(rule.left).add(rule.right);
            } else {
                ruleMap.put(rule.left, new HashSet<>(List.of(rule.right)));
            }
        }
        return ruleMap;
    }
}

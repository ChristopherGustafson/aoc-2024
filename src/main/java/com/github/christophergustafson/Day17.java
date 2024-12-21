package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.findLongs;
import static com.github.christophergustafson.utils.ParseUtils.readInputFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class Day17 {
    public static void main(String[] args) {
        String testInput = """
            Register A: 729
            Register B: 0
            Register C: 0
            
            Program: 0,1,5,4,3,0""";
        String realInput = readInputFile("day17.txt");

        String part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        String part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        long part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static ParsedInput parseInput(String input) {
        String[] split = input.split("\n\n");
        List<Long> registers = findLongs(split[0]);
        List<Long> program = Arrays.stream(split[1].substring(9).split(",")).mapToLong(Long::parseLong).boxed()
            .toList();
        return new ParsedInput(registers.getFirst(), registers.get(1), registers.getLast(), program, split[1]);
    }

    record ParsedInput(long a, long b, long c, List<Long> program, String programString) {

    }

    static String part1(ParsedInput input) {
        Computer c = new Computer(input.a, input.b, input.c);
        return String.join(",", c.runProgram(input.program).stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    static Long part2(ParsedInput input) {

        List<Long> candidates = new ArrayList<>();
        // Add manually found first candidates
        candidates.add(0L);
        candidates.add(7L);
        for (int i = input.program.size() - 2; i >= 0; i--) {
            List<Long> nextCandidates = new ArrayList<>();
            for (int t = 0; t < 8; t++) {
                for (Long answer : candidates) {
                    long testAnswer = (answer << 3) + t;
                    Computer c = new Computer(testAnswer);
                    List<Long> subProgram = input.program.subList(i, input.program.size());
                    List<Long> output = c.runProgram(input.program);
                    if (output.equals(subProgram)) {
                        nextCandidates.add(testAnswer);
                    }
                }
            }
            candidates = nextCandidates;
        }

        return Collections.min(candidates);
    }
}

class Computer {

    long a;
    long b;
    long c;
    int pointer;

    public Computer(long a, long b, long c) {
        this.a = a;
        this.b = b;
        this.c = c;
        pointer = 0;
    }

    public Computer(long a) {
        this.a = a;
        this.b = 0;
        this.c = 0;
        pointer = 0;
    }

    public List<Long> runProgram(List<Long> program) {
        List<Long> result = new ArrayList<>();

        while (pointer < program.size()) {
            switch (program.get(pointer).intValue()) {
                // adv, advance
                case 0 -> {
                    adv(program.get(pointer+1));
                    pointer += 2;
                }
                // bxl, bitwise XOR
                case 1 -> {
                    bxl(program.get(pointer+1));
                    pointer += 2;
                }
                // bst
                case 2 -> {
                    bst(program.get(pointer+1));
                    pointer += 2;
                }
                // jnx,
                case 3 -> {
                    boolean jumped = jnz(program.get(pointer+1));
                    if(!jumped) {
                        pointer += 2;
                    }
                }
                // bxc
                case 4 -> {
                    bxc(program.get(pointer+1));
                    pointer += 2;
                }
                // out
                case 5 -> {
                    long output = out(program.get(pointer+1));
                    result.add(output);
                    pointer += 2;
                }
                // bdv
                case 6 -> {
                    bdv(program.get(pointer+1));
                    pointer += 2;
                }
                // cdv
                case 7 -> {
                    cdv(program.get(pointer+1));
                    pointer += 2;
                }
                default -> throw new IllegalStateException("Unexpected value: " + program.get(pointer));
            }
        }
        return result;
    }

    private long combo(long operand) {
        switch ((int) operand) {
            case 0, 1, 2, 3 -> {
                return operand;
            }
            case 4 -> {
                return a;
            }
            case 5 -> {
                return b;
            }
            case 6 -> {
                return c;
            }
            default -> throw new IllegalStateException("Unexpected value: " + operand);
        }
    }

    void adv(long operand) {
        a = a >> combo(operand);
    }

    void bxl(long operand) {
        b = b ^ operand;
    }

    void bst(long operand) {
        b = combo(operand) % 8;
    }

    boolean jnz(long operand) {
        if (a != 0) {
            pointer = (int) operand;
            return true;
        }
        return false;
    }

    void bxc(long ignored) {
        b = b ^ c;
    }

    long out(long operand) {
        return combo(operand) % 8;
    }

    void bdv(long operand) {
        b = a >> combo(operand);
    }

    void cdv(long operand) {
        c = a >> combo(operand);
    }
}

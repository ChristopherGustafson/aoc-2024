package com.github.christophergustafson;

import static com.github.christophergustafson.Utils.readInputFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("Duplicates")
public class Day9 {

    public static void main(String[] args) {

        String testInput = """
            2333133121414131402""";
        String realInput = readInputFile("day9.txt");

        long part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        long part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        long part2Test = part2(parseInput(testInput));
        System.out.println("Part 2, test: " + part2Test);

        long part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static int[] parseInput(String input) {
        return input.chars().map(Character::getNumericValue).toArray();

    }

    static long part1(int[] input) {
        List<Integer> disk = new ArrayList<>();
        int blockId = 0;
        boolean isBlock = true;

        for (int num: input) {
            if(isBlock) {
                int finalBlockId = blockId;
                IntStream.range(0, num).forEach($ -> disk.add(finalBlockId));
                blockId++;
                isBlock = false;
            } else {
                IntStream.range(0, num).forEach($ -> disk.add(-1));
                isBlock = true;
            }
        }

        long result = 0;
        int endIndex = disk.size() - 1;
        for (int startIndex = 0; startIndex <= endIndex; startIndex++) {
            if (disk.get(startIndex) == -1) {
                while(disk.get(endIndex) == -1) {
                    endIndex--;
                }
                result += (long) startIndex * disk.get(endIndex);
                endIndex--;
            } else {
                result += (long) startIndex * disk.get(startIndex);
            }
        }

        return result;
    }

    static long part2(int[] input) {
        List<MemoryRegion> disk = new ArrayList<>();
        int blockId = 0;
        boolean isBlock = true;

        for (int num: input) {
            if(isBlock) {
                int finalBlockId = blockId;
                disk.add(new Block(finalBlockId, num));
                blockId++;
                isBlock = false;
            } else {
                disk.add(new FreeSpace(num));
                isBlock = true;
            }
        }

        int i = 0;
        while (i < disk.size()) {
            if (disk.get(i) instanceof FreeSpace freeSpace) {
                for (MemoryRegion region: disk.subList(i, disk.size()).reversed()) {
                    if (region instanceof Block block && block.size <= freeSpace.size) {
                        int blockIndex = disk.indexOf(block);
                        disk.remove(block);
                        disk.add(blockIndex, new FreeSpace(block.size));
                        disk.add(i, block);
                        disk.remove(freeSpace);
                        disk.add(i+1, new FreeSpace(freeSpace.size - block.size));
                        break;
                    }
                }
            }
            i++;
        }

        long result = 0;
        int position = 0;
        for (MemoryRegion memoryRegion : disk) {
            if (memoryRegion instanceof Block block) {
                result += checkSum(position, block);
                position += block.size;
            } else if (memoryRegion instanceof FreeSpace space) {
                position += space.size;
            }
        }

        return result;
    }

    private static long checkSum(int position, Block block) {
        long result = 0;
        for(int i = position; i < position + block.size; i++) {
            result += (long) i * block.id;
        }
        return result;
    }

    interface MemoryRegion { }
    record Block(int id, int size) implements MemoryRegion { }
    record FreeSpace(int size) implements MemoryRegion { }
}
package com.kata.streams;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorkingWithStreams {

  public static void main(String[] args) throws IOException {
    String filePath = "/streams/words.txt";

    try (InputStream inputStream = WorkingWithStreams.class.getResourceAsStream(filePath)) {
      Stream<String> words = readFileFromResource(inputStream).parallel();

      // var largestGroup = selectLargestSetOfWordsThatAreAnagramsOfEachOther(words);
      // var wordsWithDistinctCharCount = getWordWithBiggestDistinctCharCount(words);

      System.out.println(getElems(150));
    }
  }

  static List<String> getElems(int n) {
    List<String> elems = List.of("apple", "pear", "lemon", "orange");

    return Stream.iterate(0, x -> x + 1).map(x -> elems.get(x % 4)).limit(n).toList();
  }

  static List<WordsCountWithSameFirstChar> groupWordsByFirstChar(Stream<String> words) {
    Objects.requireNonNull(words);

    return words
        .filter(w -> !w.isEmpty())
        .collect(Collectors.groupingBy(x -> x.charAt(0)))
        .entrySet()
        .stream()
        .map(entry -> new WordsCountWithSameFirstChar(entry.getKey(), entry.getValue().size()))
        .sorted(Comparator.comparingLong(WordsCountWithSameFirstChar::size).reversed())
        .toList();
  }

  static Optional<WordWithDistinctCharCount> getWordWithBiggestDistinctCharCount(
      Stream<String> words) {
    Objects.requireNonNull(words);
    return words
        .map(WorkingWithStreams::getDistinctCharCount)
        .max(Comparator.comparingLong(WordWithDistinctCharCount::distinctCharCount));
  }

  static WordWithDistinctCharCount getDistinctCharCount(String word) {
    Objects.requireNonNull(word);
    return new WordWithDistinctCharCount(
        word, word.chars().mapToObj(c -> (char) c).distinct().count());
  }

  static Optional<List<String>> getLargestSetOfWordsThatAreAnagramsOfEachOther(
      Stream<String> words) {
    Objects.requireNonNull(words);

    Stream<WordWithFrequencyCount> wordsWithFrequencyCount =
        words.filter(w -> !w.isEmpty()).map(WorkingWithStreams::getCharFrequencyCount);

    Map<String, List<String>> groupedWordsByCharFrequencyCount =
        wordsWithFrequencyCount.collect(
            Collectors.groupingBy(
                wordWithFrequencyCount ->
                    transformMapToString(wordWithFrequencyCount.frequencyCount()),
                HashMap::new,
                Collectors.mapping(WordWithFrequencyCount::word, Collectors.toList())));

    return groupedWordsByCharFrequencyCount.values().stream()
        .max(Comparator.comparingInt(List::size));
  }

  static String transformMapToString(Map<Character, Integer> map) {
    Objects.requireNonNull(map);

    StringBuilder sb = new StringBuilder();
    map.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> sb.append(entry.getKey()).append(entry.getValue()));

    return sb.toString();
  }

  static WordWithFrequencyCount getCharFrequencyCount(String word) {
    Objects.requireNonNull(word);

    Map<Character, Integer> frequencyCount = new HashMap<>();

    word.chars()
        .mapToObj(c -> (char) c)
        .forEach(c -> frequencyCount.compute(c, (_, v) -> (v == null) ? 1 : v + 1));

    return new WordWithFrequencyCount(word, frequencyCount);
  }

  static Stream<String> readFileFromResource(InputStream inputStream) throws IOException {
    try {
      Objects.requireNonNull(inputStream);

      return new BufferedReader(new InputStreamReader(inputStream)).lines();
    } catch (Exception e) {
      System.out.println("Error while reading file:");
      throw e;
    }
  }
}

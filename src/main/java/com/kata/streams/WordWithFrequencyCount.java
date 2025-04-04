package com.kata.streams;

import java.util.Map;

public record WordWithFrequencyCount(String word, Map<Character, Integer> frequencyCount) {}

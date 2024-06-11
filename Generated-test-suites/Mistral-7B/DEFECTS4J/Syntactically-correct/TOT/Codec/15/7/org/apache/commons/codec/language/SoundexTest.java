package org.apache.commons.codec.language;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class SoundexTest {

    @Test
    public void testSoundexUSEnglish() {
        Soundex soundex = Soundex.US_ENGLISH;
        String[] words = {"apple", "application", "approve", "April", "banana", "basement", "boat", "Bob", "bought", "Carl", "cat", "Charlie", "Chicago", "Christian", "code", "coding", "David", "december", "different", "doctor", "Dog", "eat", "elephant", "Elizabeth", "Elm", "Ellen", "Eric", "Eve", "example", "exercise", "father", "Five", "for", "friend", "garden", "George", "Gina", "Hannah", "happy", "hospital", "India", "Indian", "Jack", "January", "John", "June", "Kangaroo", "king", "kitten", "Lion", "Liz", "love", "Mike", "Mary", "Matthew", "mother", "Mumbai", "November", "Oliver", "one", "Ophelia", "October", "Oliver", "Paul", "pencil", "Peter", "Philip", "Philosophy", "Philosopher", "Rose", "Russell", "Sarah", "Saw", "Seattle", "seven", "Sharon", "shoulder", "Sue", "Sunday", "Susan", "Sydney", "ten", "Thomas", "Tooth", "Tommy", "Tuesday", "twenty", "University", "Valentine", "very", "Wednesday", "wet", "William", "wife", "Winston", "Wood", "yesterday", "you", "Your", "Zara", "zebra"};

        for (String word : words) {
            String soundexCode = soundex.encode(word);
            String expectedSoundexCode = getExpectedSoundexCode(word);
            assertEquals("Unexpected Soundex code for word: " + word, expectedSoundexCode, soundexCode);
        }
    }

    @Test
    public void testSoundexCustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Soundex soundex = new Soundex(customMapping);
        String[] words = {"apple", "application", "approve", "April", "banana", "basement", "boat", "Bob", "bought", "Carl", "cat", "Charlie", "Chicago", "Christian", "code", "coding", "David", "december", "different", "doctor", "Dog", "eat", "elephant", "Elizabeth", "Elm", "Ellen", "Eric", "Eve", "example", "exercise", "father", "Five", "for", "friend", "garden", "George", "Gina", "Hannah", "happy", "hospital", "India", "Indian", "Jack", "January", "John", "June", "Kangaroo", "king", "kitten", "Lion", "Liz", "love", "Mike", "Mary", "Matthew", "mother", "Mumbai", "November", "Oliver", "one", "Ophelia", "October", "Oliver", "Paul", "pencil", "Peter", "Philip", "Philosophy", "Philosopher", "Rose", "Russell", "Sarah", "Saw", "Seattle", "seven", "Sharon", "shoulder", "Sue", "Sunday", "Susan", "Sydney", "ten", "Thomas", "Tooth", "Tommy", "Tuesday", "twenty", "University", "Valentine", "very", "Wednesday", "wet", "William", "wife", "Winston", "Wood", "yesterday", "you", "Your", "Zara", "zebra"};

        for (String word : words) {
            String soundexCode = soundex.encode(word);
            String expectedSoundexCode = getExpectedSoundexCodeWithCustomMapping(word, customMapping);
            assertEquals("Unexpected Soundex code for word: " + word, expectedSoundexCode, soundexCode);
        }
    }

    private String getExpectedSoundexCode(String word) {
        return Arrays.asList(Soundex.US_ENGLISH_MAPPING_STRING.split("")).get(Arrays.asList(word.toUpperCase().split("")).stream().map(this::getMappingIndex).distinct().map(this::getSoundexCodeAtIndex).collect(Collectors.joining(""))).get(0);
    }

    private String getExpectedSoundexCodeWithCustomMapping(String word, char[] customMapping) {
        return Arrays.asList(word.toUpperCase().split("")).stream().map(c -> Character.toString(customMapping[Character.toUpperCase(c) - 'A'])).collect(Collectors.joining(""));
    }

    private int getMappingIndex(char character) {
        return Arrays.asList(Soundex.US_ENGLISH_MAPPING).indexOf(Character.toUpperCase(character));
    }

    private char getSoundexCodeAtIndex(int index) {
        return Soundex.US_ENGLISH_MAPPING[index];
    }
}
package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderWithReaderInput() {
        String testInput = "Hello World!";
        CharacterReader reader = new CharacterReader(new StringReader(testInput));

        // Test basic functionality
        Assert.assertEquals("Hello World!", reader.toString());
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());

        // Test current() and consume() methods
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('E', reader.current());
        reader.consume();
        // ... continue testing all characters in the input string

        // Test mark() and rewindToMark() methods
        reader.mark();
        reader.advance(5);
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());

        // Test isEmpty() method with an empty input
        CharacterReader emptyReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(emptyReader.isEmpty());

        // Test isEmpty() method with an input that has been fully consumed
        reader.consumeToEnd();
        Assert.assertTrue(reader.isEmpty());

        // Test nextIndexOf() method
        Assert.assertEquals(6, reader.nextIndexOf(' '));
        Assert.assertEquals(-1, reader.nextIndexOf('Z'));

        // Test consumeTo() method
        String expectedOutput = "World!";
        String output = reader.consumeTo('!');
        Assert.assertEquals(expectedOutput, output);

        // Test consumeToAny() method
        String expectedDelimiters = "!?.";
        String outputDelimiters = reader.consumeToAny(expectedDelimiters.toCharArray());
        Assert.assertEquals(expectedDelimiters, outputDelimiters);

        // Test consumeTagName() method
        String expectedTagName = "<html>";
        String outputTagName = reader.consumeTagName();
        Assert.assertEquals(expectedTagName, outputTagName);

        // Test consumeData() method
        String expectedData = "Hello ";
        String outputData = reader.consumeData();
        Assert.assertEquals(expectedData, outputData);

        // Test consumeLetterSequence() method
        String expectedLetterSequence = "Hello";
        String outputLetterSequence = reader.consumeLetterSequence();
        Assert.assertEquals(expectedLetterSequence, outputLetterSequence);

        // Test consumeLetterThenDigitSequence() method
        String expectedLetterThenDigitSequence = "Hello123";
        String outputLetterThenDigitSequence = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expectedLetterThenDigitSequence, outputLetterThenDigitSequence);

        // Test consumeHexSequence() method
        String expectedHexSequence = "Hello%20World!";
        String outputHexSequence = reader.consumeHexSequence();
        Assert.assertEquals(expectedHexSequence, outputHexSequence);

        // Test consumeDigitSequence() method
        String expectedDigitSequence = "12345";
        String outputDigitSequence = reader.consumeDigitSequence();
        Assert.assertEquals(expectedDigitSequence, outputDigitSequence);

        // Test matches() method
        Assert.assertTrue(reader.matches('H'));
        Assert.assertFalse(reader.matches('Z'));

        // Test matches() method with a String input
        Assert.assertTrue(reader.matches("Hello"));
        Assert.assertFalse(reader.matches("World"));

        // Test matchesIgnoreCase() method
        Assert.assertTrue(reader.matchesIgnoreCase("hello"));
        Assert.assertTrue(reader.matchesIgnoreCase("HELLO"));
        Assert.assertFalse(reader.matchesIgnoreCase("World"));

        // Test matchesAny() method
        char[] delimiters = {'H', ' ', '!'};
        Assert.assertTrue(reader.matchesAny(delimiters));
        Assert.assertFalse(reader.matchesAny(new char[]{'Z'}));

        // Test matchesAnySorted() method
        char[] sortedDelimiters = {'!', 'H'};
        Assert.assertTrue(reader.matchesAnySorted(sortedDelimiters));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'Z'}));

        // Test matchesLetter() and matchesDigit() methods
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesDigit());
        // ... continue testing all other characters in the input string

        // Test matchConsume() and matchConsumeIgnoreCase() methods
        String expectedMatchConsumeOutput = "World!";
        reader.matchConsume("World!");
        Assert.assertEquals(expectedMatchConsumeOutput, reader.toString());

        String expectedMatchConsumeIgnoreCaseOutput = "World!";
        reader.mark();
        reader.matchConsumeIgnoreCase("world!");
        reader.rewindToMark();
        Assert.assertEquals(expectedMatchConsumeOutput, reader.toString());

        // Test containsIgnoreCase() method
        Assert.assertTrue(reader.containsIgnoreCase("world"));
        Assert.assertTrue(reader.containsIgnoreCase("World"));
        Assert.assertFalse(reader.containsIgnoreCase("Hello"));
    }

    @Test
    public void testCharacterReaderWithStringInput() {
        String testInput = "Hello World!";
        CharacterReader reader = new CharacterReader(testInput);

        // Test basic functionality
        Assert.assertEquals("Hello World!", reader.toString());
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());

        // Test the same test cases as in testCharacterReaderWithReaderInput() method
        // ...
    }
}
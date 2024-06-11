package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        char expectedChar = 'H';
        char actualChar = characterReader.current();
        Assert.assertEquals("Current char should be the first char in input string", expectedChar, actualChar);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(input);
        char expectedChar = 'H';
        char actualChar = characterReader.current();
        Assert.assertEquals("Current char should be the first char in input string", expectedChar, actualChar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderConstructorWithNullReader() {
        CharacterReader characterReader = new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderPos() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        int expectedPos = 0;
        int actualPos = characterReader.pos();
        Assert.assertEquals("Position should be 0 at the beginning of the input", expectedPos, actualPos);
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        boolean expectedResult = true;
        boolean actualResult = characterReader.isEmpty();
        Assert.assertEquals("Should return true when the reader is empty", expectedResult, actualResult);
    }

    @Test
    public void testCharacterReaderIsEmptyNoBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        boolean expectedResult = true;
        boolean actualResult = characterReader.isEmptyNoBufferUp();
        Assert.assertEquals("Should return true when the reader is empty without bufferUp", expectedResult, actualResult);
    }

    @Test
    public void testCharacterReaderIsEmptyAfterConsumingAllChars() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.consumeToEnd();
        boolean expectedResult = true;
        boolean actualResult = characterReader.isEmpty();
        Assert.assertEquals("Should return true after consuming all chars", expectedResult, actualResult);
    }

    @Test
    public void testCharacterReaderCurrentChar() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        char expectedChar = 'H';
        char actualChar = characterReader.current();
        Assert.assertEquals("Current char should be 'H'", expectedChar, actualChar);
    }

    @Test
    public void testCharacterReaderConsume() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        char expectedChar = 'H';
        characterReader.consume();
        char actualChar = characterReader.current();
        Assert.assertEquals("Next char should be 'E'", 'E', actualChar);
    }

    @Test
    public void testCharacterReaderUnconsume() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.consume();
        characterReader.unconsume();
        char actualChar = characterReader.current();
        Assert.assertEquals("Char should be the same as before consume()", 'H', actualChar);
    }

    @Test
    public void testCharacterReaderAdvance() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.advance();
        char actualChar = characterReader.current();
        Assert.assertEquals("Next char should be 'E'", 'E', actualChar);
    }

    @Test
    public void testCharacterReaderMark() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.mark();
        int expectedPos = 0;
        int actualPos = characterReader.pos();
        Assert.assertEquals("Position should be the same before and after mark()", expectedPos, actualPos);
    }

    @Test
    public void testCharacterReaderRewindToMark() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        char actualChar = characterReader.current();
        Assert.assertEquals("Char should be the same before consume()", 'H', actualChar);
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderRewindToMarkInvalidMark() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.consume();
        characterReader.rewindToMark();
        characterReader.mark();
    }

    @Test
    public void testCharacterReaderNextIndexOfChar() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        int expectedIndex = 1;
        int actualIndex = characterReader.nextIndexOf('E');
        Assert.assertEquals("Index of 'E' should be 1", expectedIndex, actualIndex);
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        String input = "Hello <html>World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "<html>";
        int actualIndex = characterReader.nextIndexOf(expectedString);
        Assert.assertEquals("Index of the string should be 6", 6, actualIndex);
    }

    @Test
    public void testCharacterReaderConsumeToChar() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        char expectedChar = 'W';
        String actualString = characterReader.consumeTo(expectedChar).trim();
        Assert.assertEquals("Consumed string should contain only the 'W' character", "W", actualString);
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "World!";
        String actualString = characterReader.consumeTo(expectedString).trim();
        Assert.assertEquals("Consumed string should be 'World!'", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeToAnyChars() {
        String input = "Hello <html>World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        char[] expectedChars = new char[]{'<', 'h', 't', 'm', 'l'};
        String actualString = characterReader.consumeToAny(expectedChars).trim();
        Assert.assertArrayEquals("Consumed string should contain the specified characters", expectedChars, actualString.toCharArray());
    }

    @Test
    public void testCharacterReaderConsumeToAnySortedChars() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        char[] expectedChars = new char[]{'<', 'h', 'i', 'l'};
        String actualString = characterReader.consumeToAnySorted(expectedChars).trim();
        Assert.assertArrayEquals("Consumed string should contain the specified characters in the correct order", expectedChars, actualString.toCharArray());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        String input = "&lt;script src=\"script.js\"&gt;";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "&lt;script src=\"script.js\"";
        String actualString = characterReader.consumeData().trim();
        Assert.assertEquals("Consumed string should be '&lt;script src=\"script.js'" +
                " (without the final '>' character)", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        String input = "<html><head><title>Title</title></head><body>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "html";
        String actualString = characterReader.consumeTagName().trim();
        Assert.assertEquals("Consumed string should be 'html'", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        String input = "<html><head><title>Title</title></head><body>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "<html><head><title>Title</title></head><body>";
        String actualString = characterReader.consumeToEnd().trim();
        Assert.assertEquals("Consumed string should be the entire input", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        String input = "HeLlO wOrLd!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "HeLlO";
        String actualString = characterReader.consumeLetterSequence().trim();
        Assert.assertEquals("Consumed string should be 'HeLlO'", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        String input = "HeLlO12345";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "HeLlO";
        String actualString = characterReader.consumeLetterThenDigitSequence().trim();
        Assert.assertEquals("Consumed string should be 'HeLlO'", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        String input = "He&x68;&x69;&x6E;llO";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "He&x68;&x69;&x6E;llO";
        String actualString = characterReader.consumeHexSequence().trim();
        Assert.assertEquals("Consumed string should be the entire input", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        String input = "HeLlO12345";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "12345";
        String actualString = characterReader.consumeDigitSequence().trim();
        Assert.assertEquals("Consumed string should be '12345'", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderMatchesChar() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        char expectedChar = 'H';
        boolean actualResult = characterReader.matches(expectedChar);
        Assert.assertTrue("Should match 'H'", actualResult);
    }

    @Test
    public void testCharacterReaderMatchesString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "Hello";
        boolean actualResult = characterReader.matches(expectedString);
        Assert.assertTrue("Should match 'Hello'", actualResult);
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCaseString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "hello";
        boolean actualResult = characterReader.matchesIgnoreCase(expectedString);
        Assert.assertTrue("Should match 'hello' (case-insensitive)", actualResult);
    }

    @Test
    public void testCharacterReaderMatchesAnyChars() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        char[] expectedChars = new char[]{'<', 'h', 't', 'm'};
        boolean actualResult = characterReader.matchesAny(expectedChars);
        Assert.assertTrue("Should match any of the specified characters", actualResult);
    }

    @Test
    public void testCharacterReaderMatchesAnySortedChars() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        char[] expectedChars = new char[]{'<', 'h', 'i', 'l'};
        boolean actualResult = characterReader.matchesAnySorted(expectedChars);
        Assert.assertTrue("Should match any of the specified characters in the correct order", actualResult);
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        String input = "HeLlO wOrLd!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        boolean actualResult = characterReader.matchesLetter();
        Assert.assertTrue("Should match a letter", actualResult);
    }

    @Test
    public void testCharacterReaderMatchConsumeString() {
        String input = "<html><head><title>Title</title></head><body>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "<html>";
        characterReader.matchConsume(expectedString);
        String actualString = characterReader.consumeTagName().trim();
        Assert.assertEquals("Consumed string should be '<html>'", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCaseString() {
        String input = "<HTML><head><title>Title</title></head><body>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "<html>";
        characterReader.matchConsumeIgnoreCase(expectedString);
        String actualString = characterReader.consumeTagName().trim();
        Assert.assertEquals("Consumed string should be '<html>'", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderContainsIgnoreCaseString() {
        String input = "<html><head><title>Title</title></head><body>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "</title>";
        boolean actualResult = characterReader.containsIgnoreCase(expectedString);
        Assert.assertTrue("Should contain '/title'>', case-insensitive", actualResult);
    }

    @Test
    public void testCharacterReaderToString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String expectedString = "Hello World!";
        String actualString = characterReader.toString();
        Assert.assertEquals("ToString should return the entire input", expectedString, actualString);
    }
}
package org.jsoup.parser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CharacterReaderTest {

    private CharacterReader reader;
    private byte[] input = new byte[]{'t', 'e', 's', 't', ' ', 'c', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', 's', '\n'};

    @Before
    public void setUp() {
        reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input), "UTF-8"));
    }

    @After
    public void tearDown() throws IOException {
        reader.close();
    }

    @Test
    public void testCharacterReaderConstruction() {
        Assert.assertNotNull(reader);
    }

    @Test
    public void testIsEmptyFalse() {
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testIsEmptyTrue() {
        reader.consumeToEnd();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrentCharacter() {
        Assert.assertEquals('t', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testCurrentCharacterAfterConsumeToEnd() {
        reader.consumeToEnd();
        reader.current();
    }

    @Test
    public void testConsumeCharacter() {
        reader.consume();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeCharacters() {
        String expected = "tes";
        String actual = reader.consumeTo(expected.charAt(0));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeString() {
        String expected = " characters";
        String actual = reader.consumeTo(expected);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeStringIgnoreCase() {
        String expected = " Characters";
        String actual = reader.consumeToIgnoreCase(expected);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "testcharacters";
        String actual = reader.consumeToAny(expected.toCharArray());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = " test characters";
        String actual = reader.consumeToAnySorted(expected.toCharArray());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeData() {
        String expected = "testcharacters";
        String actual = reader.consumeData();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTagName() {
        String expected = "<title>";
        String actual = reader.consumeTagName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "test";
        String actual = reader.consumeLetterSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "test123";
        String actual = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "test#123";
        String actual = reader.consumeHexSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "123";
        String actual = reader.consumeDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(reader.matches(' '));
        reader.consume();
        Assert.assertFalse(reader.matches(' '));
    }

    @Test
    public void testMatchesString() {
        String expected = "test";
        Assert.assertTrue(reader.matches(expected));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String expected = "TEST";
        Assert.assertTrue(reader.matchesIgnoreCase(expected));
    }

    @Test
    public void testMatchesAny() {
        char[] expected = new char[]{'t', 'e', 's', ' '};
        Assert.assertTrue(reader.matchesAny(expected));
    }

    @Test
    public void testMatchesAnySorted() {
        char[] expected = new char[]{' ', 't', 'e', 's'};
        Assert.assertTrue(reader.matchesAnySorted(expected));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(reader.matchesLetter());
        reader.consumeLetterSequence();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertTrue(reader.matchesDigit());
        reader.consumeDigitSequence();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        String expected = "test";
        reader.matchConsume(expected);
        Assert.assertEquals(expected.length(), reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        String expected = "TEST";
        reader.matchConsumeIgnoreCase(expected);
        Assert.assertEquals(expected.length(), reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        String expected = "<title>";
        Assert.assertTrue(reader.containsIgnoreCase(expected));
        reader.consumeTagName();
        Assert.assertFalse(reader.containsIgnoreCase(expected));
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, reader.pos());
        reader.consumeToEnd();
        Assert.assertEquals(input.length, reader.pos());
    }

    @Test
    public void testEmptyNoBufferUp() {
        reader.consumeToEnd();
        Assert.assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testNotEmptyNoBufferUp() {
        reader.consumeLetterSequence();
        Assert.assertFalse(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testBufferUp() {
        reader.bufferUp();
        Assert.assertFalse(reader.isEmptyNoBufferUp());
    }

    @Test(expected = IllegalStateException.class)
    public void testBufferUpAfterConsumeToEnd() {
        reader.consumeToEnd();
        reader.bufferUp();
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consumeLetterSequence();
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
    }
}
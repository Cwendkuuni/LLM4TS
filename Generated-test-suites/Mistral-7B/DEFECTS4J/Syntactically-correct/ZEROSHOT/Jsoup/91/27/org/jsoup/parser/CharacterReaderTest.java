package org.jsoup.parser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * CharacterReaderTest class to test all methods in CharacterReader class.
 */
public class CharacterReaderTest {

    private CharacterReader characterReader;
    private final char[] testCharSequence = {'h', 'e', 'l', 'l', 'o'};
    private final char[] testCharSequence2 = {'H', 'e', 'l', 'l', 'o'};
    private final char[] testCharSequence3 = {'h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd'};

    @Before
    public void setUp() {
        characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(testCharSequence)));
    }

    @After
    public void tearDown() throws IOException {
        characterReader.reader.close();
    }

    @Test
    public void testCharacterReaderInstance() {
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(characterReader.isEmpty());
        characterReader.advance();
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals('h', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('h', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(1, characterReader.nextIndexOf('e'));
        Assert.assertEquals(5, characterReader.nextIndexOf('o'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        Assert.assertEquals(0, characterReader.nextIndexOf(testCharSequence));
        Assert.assertEquals(0, characterReader.nextIndexOf(testCharSequence2));
        Assert.assertEquals(-1, characterReader.nextIndexOf(testCharSequence3));
    }

    @Test
    public void testConsumeTo() {
        String expected = new String(testCharSequence, 0, 4);
        String result = characterReader.consumeTo('l');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeToAny() {
        String expected = new String(testCharSequence, 1, 3);
        String result = characterReader.consumeToAny('l', 'o');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = new String(testCharSequence, 1, 3);
        String result = characterReader.consumeToAnySorted('l', 'o');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeData() {
        String expected = new String(testCharSequence);
        String result = characterReader.consumeData();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeTagName() {
        String expected = new String(testCharSequence, 0, 4);
        String result = characterReader.consumeTagName();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = new String(testCharSequence);
        String result = characterReader.consumeToEnd();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = new String(testCharSequence, 0, 5);
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = new String(testCharSequence3, 0, 11);
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = new String(new char[]{'h', 'e', 'x', 'a', 'd', 'f', 'f', 'f'});
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = new String(new char[]{'1', '2', '3'});
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(characterReader.matches('e'));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesSequence() {
        Assert.assertTrue(characterReader.matches(testCharSequence));
        Assert.assertFalse(characterReader.matches(testCharSequence3));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(characterReader.matchesIgnoreCase(testCharSequence2));
        Assert.assertFalse(characterReader.matchesIgnoreCase(testCharSequence3));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(characterReader.matchesAny('h', 'e'));
        Assert.assertFalse(characterReader.matchesAny('z', 'e'));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(characterReader.matchesAnySorted('h', 'e'));
        Assert.assertFalse(characterReader.matchesAnySorted('z', 'e'));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String expected = new String(testCharSequence);
        characterReader.matchConsume(testCharSequence.toString());
        String result = characterReader.consumeToEnd();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String expected = new String(testCharSequence2);
        characterReader.matchConsumeIgnoreCase(testCharSequence2.toString());
        String result = characterReader.consumeToEnd();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/STYLE"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/body"));
    }
}
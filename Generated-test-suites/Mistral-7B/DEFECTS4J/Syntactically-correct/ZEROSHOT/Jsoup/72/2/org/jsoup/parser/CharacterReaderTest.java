package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertNotNull(characterReader);
        Assert.assertArrayEquals(new char[1024 * 32], characterReader.charBuf);
        Assert.assertNotNull(characterReader.reader);
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.consume();
        characterReader.consume();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals('T', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals('T', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.advance();
        characterReader.advance();
        Assert.assertEquals('s', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        characterReader.mark();
        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String Test"));
        Assert.assertEquals(5, characterReader.nextIndexOf(' '));
        characterReader.advance();
        Assert.assertEquals(6, characterReader.nextIndexOf(' '));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String Test"));
        String test = "Test";
        Assert.assertEquals(0, characterReader.nextIndexOf(test));
        characterReader.advance();
        characterReader.advance();
        Assert.assertEquals(4, characterReader.nextIndexOf(test));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String result = characterReader.consumeTo(' ');
        Assert.assertEquals("Test", result);
        characterReader.consume();
        result = characterReader.consumeTo(' ');
        Assert.assertEquals("Test ", result);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        String result = characterReader.consumeToEnd();
        Assert.assertEquals("Test String", result);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String Test"));
        String result = characterReader.consumeToAny(',');
        Assert.assertEquals("Test String", result);
        characterReader.advance();
        characterReader.advance();
        result = characterReader.consumeToAny(',');
        Assert.assertEquals("Test String Test", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String Test"));
        characterReader.advance();
        characterReader.advance();
        String result = characterReader.consumeToAnySorted(new char[]{'a', 'b', 'c'});
        Assert.assertEquals("Test String Test", result);
        characterReader.consume();
        result = characterReader.consumeToAnySorted(new char[]{'a', 'b', 'c'});
        Assert.assertEquals("Test String Test", result);
        characterReader.advance();
        result = characterReader.consumeToAnySorted(new char[]{'a', 'b', 'c'});
        Assert.assertEquals("Test String Test", result);
        characterReader.advance();
        result = characterReader.consumeToAnySorted(new char[]{'a', 'b'});
        Assert.assertEquals("Test String Test", result);
        characterReader.consume();
        result = characterReader.consumeToAnySorted(new char[]{'a', 'b'});
        Assert.assertEquals("Test String Test", result);
        characterReader.advance();
        result = characterReader.consumeToAnySorted(new char[]{'b', 'a'});
        Assert.assertEquals("Test String Test", result);
        characterReader.consume();
        result = characterReader.consumeToAnySorted(new char[]{'b', 'a'});
        Assert.assertEquals("Test String Test", result);
        characterReader.consume();
        result = characterReader.consumeToAnySorted(new char[]{'a'});
        Assert.assertEquals("Test String Test", result);
        characterReader.consume();
        result = characterReader.consumeToAnySorted(new char[]{'a'});
        Assert.assertEquals("Test String Test", result);
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test & < null"));
        String result = characterReader.consumeData();
        Assert.assertEquals("Test", result);

        characterReader.advance();
        characterReader.advance();
        result = characterReader.consumeData();
        Assert.assertEquals("& < null", result);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("/html/head/title"));
        String result = characterReader.consumeTagName();
        Assert.assertEquals("/html", result);
        characterReader.advance();
        characterReader.advance();
        result = characterReader.consumeTagName();
        Assert.assertEquals("/head", result);
        characterReader.advance();
        characterReader.advance();
        result = characterReader.consumeTagName();
        Assert.assertEquals("/title", result);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("/html/head/title"));
        String result = characterReader.consumeToEnd();
        Assert.assertEquals("/html/head/title", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test Letter Sequence"));
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123 Letters Digits"));
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Test123", result);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test #Hex #Sequence"));
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals("#Hex", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test 123 Digits"));
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals("123", result);
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertTrue(characterReader.matches('T'));
        Assert.assertTrue(characterReader.matches('s'));
        Assert.assertFalse(characterReader.matches('F'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertTrue(characterReader.matches("Te"));
        Assert.assertTrue(characterReader.matches("String"));
        Assert.assertFalse(characterReader.matches("TestF"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestAnyString"));
        Assert.assertTrue(characterReader.matchesAny('T'));
        Assert.assertTrue(characterReader.matchesAny('e'));
        Assert.assertTrue(characterReader.matchesAny('S'));
        Assert.assertFalse(characterReader.matchesAny('F'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestAnyString"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{ 'T', 'e', 'S' }));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{ 'e', 'T', 'S' }));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{ 'S', 'T', 'e' }));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{ 'F', 'T', 'e' }));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestLetterString"));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestDigitString"));
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestMatchConsumeString"));
        Assert.assertTrue(characterReader.matchConsume("Te"));
        Assert.assertTrue(characterReader.matchConsume("String"));
    }

    @Test
    public void testMatchConsumeSequenceIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestMatchConsumeString"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("te"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("String"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestContainIgnoreCaseString"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/html"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/head"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestToStringString"));
        Assert.assertEquals("TestToStringString", characterReader.toString());
    }

    @Test(expected = IOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
    }
}
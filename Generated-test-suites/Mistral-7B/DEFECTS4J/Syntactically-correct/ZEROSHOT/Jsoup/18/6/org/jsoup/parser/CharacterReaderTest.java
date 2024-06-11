package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        assertNotNull(reader);
    }

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testNotEmptyReader() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeChar() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testConsumeString() {
        CharacterReader reader = new CharacterReader("abc123def"); ;
        assertEquals("bc", reader.consumeAsString());
        assertEquals("123def", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcdeff"); ;
        assertEquals("bcde", reader.consumeTo('f'));
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdeff"); ;
        assertEquals("bcde", reader.consumeTo("f"));
    }

    @Test
    public void testConsumeToAnyChar() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz"); ;
        assertEquals("abc", reader.consumeToAny('b', 'c'));
        assertEquals("", reader.consumeToAny('z', 'x'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdeff"); ;
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz"); ;
        assertEquals("abcde", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1234567890abcdef"); ;
        assertEquals("1234", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890"); ;
        assertEquals("1234", reader.consumeDigitSequence());
    }

    @Test
    public void testMatchesChar() {
        CharacterReader reader = new CharacterReader("abc"); ;
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("abcabc"); ;
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
    }

    @Test
    public void testMatchesIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("ABCabc"); ;
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("CD"));
    }

    @Test
    public void testMatchesAnyChar() {
        CharacterReader reader = new CharacterReader("abcd"); ;
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('c', 'd'));
        assertFalse(reader.matchesAny('e', 'x'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc"); ;
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123"); ;
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader reader = new CharacterReader("abcdabcd"); ;
        assertTrue(reader.matchConsume("ab"));
        assertEquals("cd", reader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("ABCaBcD"); ;
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals("D", reader.consumeToEnd().charAt(0));
    }

    @Test
    public void testContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>Body</body></html>"); ;
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("TITLE"));
    }
}
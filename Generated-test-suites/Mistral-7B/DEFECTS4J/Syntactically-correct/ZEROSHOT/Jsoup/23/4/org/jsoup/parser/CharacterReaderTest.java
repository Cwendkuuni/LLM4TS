package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        reader.consume();
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        reader.consume();
        assertEquals('B', reader.current());
        reader.consume();
        assertEquals('C', reader.current());
        reader.consume();
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader reader = new CharacterReader("ABC");
        reader.mark();
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        reader.consume();
        reader.rewindToMark();
        assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String consumedString = reader.consumeAsString();
        assertEquals("AB", consumedString);
        reader.advance();
        consumedString = reader.consumeAsString();
        assertEquals("CD", consumedString);
        reader.consume();
        consumedString = reader.consumeAsString();
        assertEquals("EF", consumedString);
    }

    @Test
    public void testCharacterReaderConsumeToChar() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        String consumedString = reader.consumeTo('F');
        assertEquals("ABCDEF", reader.toString());
        assertEquals("DE", consumedString);

        reader.consume();
        consumedString = reader.consumeTo('G');
        assertEquals("EFG", reader.toString());
        assertEquals("F", reader.current());
        assertEquals("G", consumedString.charAt(0));
        assertEquals("", consumedString.substring(1));
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("ABCDEFG");
        String consumedString = reader.consumeTo("FG");
        assertEquals("ABCDEF", reader.toString());
        assertEquals("DE", consumedString);

        reader.consume();
        consumedString = reader.consumeTo("G");
        assertEquals("EFG", reader.toString());
        assertEquals("F", reader.current());
        assertEquals("G", consumedString.charAt(0));
        assertEquals("", consumedString.substring(1));
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("012345abcDEF");
        String consumedString = reader.consumeToAny('0', '5');
        assertEquals("01234", reader.toString());
        assertEquals("", consumedString);

        reader.consume();
        consumedString = reader.consumeToAny('a', 'F');
        assertEquals("abcDEF", reader.toString());
        assertEquals("", consumedString);

        reader.consume();
        consumedString = reader.consumeToAny('e', 'F');
        assertEquals("DEF", reader.toString());
        assertEquals("E", consumedString.charAt(0));
        assertEquals("F", consumedString.charAt(1));
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String consumedString = reader.consumeToEnd();
        assertEquals("ABCDEF", reader.toString());
        assertEquals("ABCDEF", consumedString);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdABCDEF");
        String consumedString = reader.consumeLetterSequence();
        assertEquals("abcd", reader.toString());
        assertEquals("", consumedString);

        reader.consume();
        consumedString = reader.consumeLetterSequence();
        assertEquals("ABCDEF", reader.toString());
        assertEquals("A", consumedString.charAt(0));
        assertEquals("B", consumedString.charAt(1));
        assertEquals("C", consumedString.charAt(2));
        assertEquals("D", consumedString.charAt(3));
        assertEquals("E", consumedString.charAt(4));
        assertEquals("F", consumedString.charAt(5));
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1a2bCD3eF");
        String consumedString = reader.consumeHexSequence();
        assertEquals("0x1a2bCD3e", reader.toString());
        assertEquals("CD3e", consumedString);

        reader.consume();
        consumedString = reader.consumeHexSequence();
        assertEquals("F", reader.toString());
        assertEquals("F", consumedString);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890123456");
        String consumedString = reader.consumeDigitSequence();
        assertEquals("123456789", reader.toString());
        assertEquals("012345", consumedString);

        reader.consume();
        consumedString = reader.consumeDigitSequence();
        assertEquals("6", reader.toString());
        assertEquals("56", consumedString);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcdABCDEF");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches('B'));
        assertTrue(reader.matches('C'));
        assertTrue(reader.matches('D'));
        assertTrue(reader.matches('E'));
        assertTrue(reader.matches('F'));
        assertFalse(reader.matches('G'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdABCDEF");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("C"));
        assertTrue(reader.matchesIgnoreCase("D"));
        assertTrue(reader.matchesIgnoreCase("E"));
        assertTrue(reader.matchesIgnoreCase("F"));
        assertFalse(reader.matchesIgnoreCase("G"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdABCDEF");
        assertTrue(reader.matchesAny('a', 'd'));
        assertTrue(reader.matchesAny('A', 'D'));
        assertTrue(reader.matchesAny('A', 'F'));
        assertTrue(reader.matchesAny('b', 'F'));
        assertTrue(reader.matchesAny('c', 'F'));
        assertFalse(reader.matchesAny('G', 'F'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdABCDEF");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234567890123456");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdABCDEF");
        assertTrue(reader.matchConsume("ab"));
        assertEquals("CDABCDEF", reader.toString());

        assertTrue(reader.matchConsume("AB"));
        assertEquals("CDEF", reader.toString());

        assertTrue(reader.matchConsume("CD"));
        assertEquals("EF", reader.toString());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdABCDEF");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals("CDABCDEF", reader.toString());

        assertTrue(reader.matchConsumeIgnoreCase("AB"));
        assertEquals("CDEF", reader.toString());

        assertTrue(reader.matchConsumeIgnoreCase("cd"));
        assertEquals("EF", reader.toString());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdABCDEF");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/style"));
    }
}
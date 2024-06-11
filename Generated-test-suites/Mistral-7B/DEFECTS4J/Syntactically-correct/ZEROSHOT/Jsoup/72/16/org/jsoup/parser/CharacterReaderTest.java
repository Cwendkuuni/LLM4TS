package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderCreation() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test String", characterReader.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderNullReader() {
        CharacterReader characterReader = new CharacterReader(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderUnsupportedReader() {
        byte[] bytes = new byte[10];
        Reader reader = new java.io.ByteArrayInputStream(bytes);
        CharacterReader characterReader = new CharacterReader(reader);
    }

    @Test
    public void testCharacterReaderMaxBufferLen() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader, 10);
        assertEquals("Test String", characterReader.toString());
    }

    @Test
    public void testCharacterReaderStringInput() {
        CharacterReader characterReader = new CharacterReader("Test String");
        assertEquals("Test String", characterReader.toString());
    }

    @Test
    public void testBufferUp() {
        Reader reader = new StringReader("Test Test Test Test");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.bufferUp();
        assertEquals(5, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Reader reader = new StringReader("");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader(new StringReader("Test"));
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Reader reader = new StringReader("Test Test Test Test");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        Reader reader = new StringReader("Test Test Test Test");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        Reader reader = new StringReader("Test Test Test Test");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        Reader reader = new StringReader("Test Test Test Test");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.advance();
        characterReader.advance();
        assertEquals('s', characterReader.current());
    }

    @Test
    public void testMark() {
        Reader reader = new StringReader("Test Test Test Test");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        Reader reader = new StringReader("TestTestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(3, characterReader.nextIndexOf('s'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        Reader reader = new StringReader("TestTestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(4, characterReader.nextIndexOf("est"));
        assertEquals(-1, characterReader.nextIndexOf("zzz"));
    }

    @Test
    public void testConsumeTo() {
        Reader reader = new StringReader("TestTestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("est", characterReader.consumeTo('s'));
        assertEquals("TestTest", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToSequence() {
        Reader reader = new StringReader("TestTestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("est", characterReader.consumeTo("est"));
        assertEquals("TestTest", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        Reader reader = new StringReader("TestTestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test", characterReader.consumeToAny(new char[]{'s', 't'}));
        assertEquals("TestTestTest", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnySorted() {
        Reader reader = new StringReader("TestTestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("TestTest", characterReader.consumeToAnySorted(new char[]{'s', 't'}));
        assertEquals("TestTestTest", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeData() {
        Reader reader = new StringReader("&lt;Test&gt;&amp;Test&amp;Test");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("&lt;Test&gt;&amp;Test", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        Reader reader = new StringReader("&lt;Test&gt;<tag name>&lt;/tag name&gt;");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("<tag name>", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeLetterSequence() {
        Reader reader = new StringReader("TestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("est", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        Reader reader = new StringReader("Test123Test456Test789");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        Reader reader = new StringReader("&amp;#x61;#x6C;#x65;#x74;");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("abc", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        Reader reader = new StringReader("12345678901234567890");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("1234567890", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        Reader reader = new StringReader("TestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matches('t'));
        assertTrue(characterReader.matches("es"));
        assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesSequence() {
        Reader reader = new StringReader("TestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matches("es"));
        assertFalse(characterReader.matches("zzz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Reader reader = new StringReader("TestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchesIgnoreCase("es"));
        assertTrue(characterReader.matchesIgnoreCase("EST"));
        assertFalse(characterReader.matchesIgnoreCase("ZZZ"));
    }

    @Test
    public void testMatchesAny() {
        Reader reader = new StringReader("TestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchesAny(new char[]{'s', 't'}));
        assertFalse(characterReader.matchesAny(new char[]{'z', 'z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        Reader reader = new StringReader("TestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchesAnySorted(new char[]{'s', 't'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'z', 'z'}));
    }

    @Test
    public void testMatchesLetter() {
        Reader reader = new StringReader("TestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        Reader reader = new StringReader("Test123Test456Test789");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        Reader reader = new StringReader("TestTestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("es", characterReader.consumeTo("es"));
        assertEquals("Test", characterReader.toString());
        assertTrue(characterReader.matchConsume("es"));
        assertEquals("Test", characterReader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        Reader reader = new StringReader("TestTestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("es", characterReader.consumeTo("es"));
        assertEquals("Test", characterReader.toString());
        assertTrue(characterReader.matchConsumeIgnoreCase("es"));
        assertEquals("Test", characterReader.toString());
    }

    @Test
    public void testToString() {
        Reader reader = new StringReader("TestTestTestTest");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("TestTestTestTest", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        char[] charBuf = new char[10];
        String[] stringCache = new String[512];
        int start = 0;
        int count = 5;
        String cached = CharacterReader.cacheString(charBuf, stringCache, start, count);
        assertEquals("TestTest", cached);
    }

    @Test
    public void testRangeEquals() {
        char[] charBuf = new char[10];
        String cached = new String(charBuf, 0, 5);
        assertTrue(CharacterReader.rangeEquals(charBuf, 0, 5, cached));
    }
}
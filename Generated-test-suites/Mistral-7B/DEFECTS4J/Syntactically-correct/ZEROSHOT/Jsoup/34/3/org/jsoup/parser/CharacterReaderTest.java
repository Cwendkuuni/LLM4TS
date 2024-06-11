package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.current());
        assertEquals("A", reader.consumeAsString());
        assertEquals(1, reader.pos());
        assertEquals(0, reader.nextIndexOf('A'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals('A', reader.current());
        assertEquals("A", reader.consumeAsString());
        assertEquals('B', reader.current());
        assertEquals("AB", reader.consumeTo('B').toString());
        assertEquals(3, reader.pos());
        assertEquals(1, reader.nextIndexOf('B'));
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkPosition() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('A', reader.current());
        assertEquals(2, reader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals(1, reader.nextIndexOf('B'));
        assertEquals(-1, reader.nextIndexOf('Z'));
        assertEquals(4, reader.nextIndexOf("DEF"));
        assertEquals(-1, reader.nextIndexOf("ABC"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("BCD", reader.consumeTo('D').toString());
        assertEquals("EF", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("BCDEF", reader.consumeTo("DEF").toString());
        assertEquals("", reader.consumeTo("G").toString());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("1aBc2D3eF");
        assertEquals("aBc", reader.consumeLetterSequence().toString());
        assertEquals("D3eF", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1234abcdE1F0");
        assertEquals("234abcd", reader.consumeHexSequence().toString());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234abcdE1F0");
        assertEquals("1234", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("ABCDEFABCDEF");
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches("ABC"));
        assertFalse(reader.matches('Z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEFABCDEF");
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertTrue(reader.matchesIgnoreCase("abC"));
        assertFalse(reader.matchesIgnoreCase("XYZ"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("ABCDEFABCDEF");
        assertTrue(reader.matchesAny('A', 'B'));
        assertTrue(reader.matchesAny('B', 'A'));
        assertFalse(reader.matchesAny('Z', 'X'));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("</title><style>");
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("<style>"));
        assertFalse(reader.containsIgnoreCase("<head>"));
    }
}
package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    private CharacterReader characterReader;
    private static final String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789&<>";

    @Before
    public void setUp() {
        try {
            characterReader = new CharacterReader(new StringReader(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        characterReader = null;
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(input);
        assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderConstructorWithReaderAndInt() {
        CharacterReader characterReader = new CharacterReader(new StringReader(input), input.length());
        assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertNotNull(characterReader);
    }

    @Test
    public void testBufferUp() {
        characterReader.bufferUp();
        assertEquals(input.length(), characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertEquals(false, characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('a', characterReader.current());
        characterReader.consume();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testConsume() {
        characterReader.consume();
        assertEquals('b', characterReader.current());
        characterReader.consume();
        assertEquals('c', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(0, characterReader.nextIndexOf('a'));
        assertEquals(1, characterReader.nextIndexOf('b'));
        assertEquals(27, characterReader.nextIndexOf('Z'));
        assertEquals(-1, characterReader.nextIndexOf('Z' + 1));
        assertEquals(26, characterReader.nextIndexOf("bc"));
        assertEquals(-1, characterReader.nextIndexOf("xy"));
    }

    @Test
    public void testConsumeTo() {
        String expected = "abc";
        assertEquals(expected, characterReader.consumeTo('c'));
        assertEquals("defghijklmnopqrstuvwxyz", characterReader.toString());
    }

    @Test
    public void testConsumeToAny() {
        String expected1 = "ab";
        String expected2 = "cd";
        assertEquals(expected1, characterReader.consumeToAny('b'));
        characterReader.consume();
        assertEquals(expected2, characterReader.consumeToAny('d'));
        assertEquals("efghijklmnopqrstuvwxyz", characterReader.toString());
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = "ac";
        assertEquals(expected, characterReader.consumeToAnySorted('a', 'c'));
        assertEquals("bdefghijklmnopqrstuvwxyz", characterReader.toString());
    }

    @Test
    public void testConsumeData() {
        String expected = "&<";
        assertEquals(expected, characterReader.consumeData());
        assertEquals("abcdefghijklmnopqrstuvwxyz", characterReader.toString());
    }

    @Test
    public void testConsumeTagName() {
        String expected = "</title>";
        assertEquals(expected, characterReader.consumeTagName());
        assertEquals("</style>", characterReader.toString());
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "abcdefghijklmnopqrstuvwxyz";
        assertEquals(expected, characterReader.consumeToEnd());
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "abcdefghijklmnopqrstuvwxyz";
        assertEquals(expected, characterReader.consumeLetterSequence());
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "abc123def456ghi789";
        assertEquals(expected, characterReader.consumeLetterThenDigitSequence());
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "abcdef";
        assertEquals(expected, characterReader.consumeHexSequence());
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "1234567890";
        assertEquals(expected, characterReader.consumeDigitSequence());
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('a'));
        assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("A"));
        assertFalse(characterReader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('a', 'b'));
        assertFalse(characterReader.matchesAny('z', 'c'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('a', 'b', 'c'));
        assertFalse(characterReader.matchesAnySorted('z', 'a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('9'));
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
    }
}
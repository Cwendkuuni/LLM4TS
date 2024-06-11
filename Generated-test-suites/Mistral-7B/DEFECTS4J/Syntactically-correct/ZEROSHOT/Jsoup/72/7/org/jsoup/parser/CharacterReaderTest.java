package org.jsoup.parser;

import org.jsoup.UncheckedIOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderBasic() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        assertEquals("a", reader.current());
        assertEquals("a", reader.consume());
        assertEquals("b", reader.current());
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(-1, reader.nextIndexOf("xyz"));
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeToEnd());

        // test empty
        reader.rewindToMark();
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(new byte[0]));
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderConstructors() {
        String input = "abcdefghijklmnopqrstuvwxyz";

        // test constructor with input stream and size
        CharacterReader reader1 = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())), input.length());
        assertEquals(input.length(), reader1.charBuf.length);
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader1.consumeToEnd());

        // test constructor with input stream
        CharacterReader reader2 = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        assertEquals(maxBufferLen, reader2.charBuf.length);
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader2.consumeToEnd());

        // test constructor with string
        CharacterReader reader3 = new CharacterReader(input);
        assertEquals(input.length(), reader3.charBuf.length);
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader3.consumeToEnd());
    }

    @Test
    public void testCharacterReaderBufferUp() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        // test buffer up when not necessary
        reader.bufferUp();
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeToEnd());

        // test buffer up when necessary
        reader.mark();
        reader.advance();
        reader.bufferUp();
        assertEquals("bcdefghijklmnopqrstuvwxyz", reader.consumeToEnd());
        reader.rewindToMark();
        assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReaderPos() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        // test current position
        assertEquals("a", reader.current());
        assertEquals("a", reader.consume());
        assertEquals("b", reader.current());

        // test position after consumption
        reader.consume();
        reader.consume();
        assertEquals("c", reader.current());

        // test position after reading to end
        reader.consumeToEnd();
        assertEquals(-1, reader.current());

        // test position after rewind
        reader.rewindToMark();
        assertEquals("a", reader.current());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        // test not empty
        assertFalse(reader.isEmpty());

        // test empty after consumption
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());

        // test not empty after rewind
        reader.rewindToMark();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumer() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        char consumed = reader.consume();
        assertEquals('a', consumed);

        // test consume multiple times
        consumed = reader.consume();
        assertEquals('b', consumed);
        consumed = reader.consume();
        assertEquals('c', consumed);

        // test consume to end
        String consumedString = reader.consumeToEnd();
        assertEquals("defghijklmnopqrstuvwxyz", consumedString);

        // test consume to character
        consumedString = reader.consumeTo('d');
        assertEquals("bc", consumedString);

        // test consume to string
        consumedString = reader.consumeTo("ef");
        assertEquals("ghijklmnopqrstuvwxyz", consumedString);

        // test consume to any single character
        consumedString = reader.consumeToAny(new char[]{'d'});
        assertEquals("bc", consumedString);

        // test consume to any sorted sequence of characters
        consumedString = reader.consumeToAnySorted(new char[]{'e', 'f'});
        assertEquals("ghijklmnopqrstuvwxyz", consumedString);
    }

    @Test
    public void testCharacterReaderStringMethods() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        // test consumeLetterSequence
        String letterSequence = reader.consumeLetterSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz", letterSequence);

        // test consumeLetterThenDigitSequence
        String letterThenDigitSequence = reader.consumeLetterThenDigitSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz0123456789", letterThenDigitSequence);

        // test consumeDigitSequence
        String digitSequence = reader.consumeDigitSequence();
        assertEquals("0123456789", digitSequence);

        // test consumeHexSequence
        String hexSequence = reader.consumeHexSequence();
        assertEquals("0123456789ABCDEF", hexSequence);

        // test consumeTagName
        String tagName = reader.consumeTagName();
        assertEquals("/tagName>", tagName);

        // test consumeData
        String data = reader.consumeData();
        assertEquals("&<null", data);
    }

    @Test
    public void testCharacterReaderMatching() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        // test matches
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'));

        // test matchesIgnoreCase
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("z"));

        // test matchesAny
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));

        // test matchesAnySorted
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));

        // test matchesLetter
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));

        // test matchesDigit
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('1'));
    }

    @Test
    public void testCharacterReaderToString() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        String stringResult = reader.toString();
        assertEquals(input, stringResult);
    }
}
package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Unit tests for CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader_ReaderInput() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // Test basic functionality
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('H', reader.consume());

        // Test reading characters up to a specific char
        String expected = reader.consumeTo('l');
        Assert.assertEquals("Hello, Wor", expected);
        Assert.assertEquals('d', reader.current());

        // Test reading characters up to a specific sequence
        expected = reader.consumeTo("ld!");
        Assert.assertEquals("Hello, World!", expected);
        Assert.assertEquals('\0', reader.current());

        // Test reading characters until a specific char is found
        expected = reader.consumeToAny('l');
        Assert.assertEquals("Hello, World!", expected);

        // Test reading characters until a specific char is found, sorted
        expected = reader.consumeToAnySorted(new char[]{'d', 'l'});
        Assert.assertEquals("Hello, World!", expected);

        // Test reading characters until a specific sequence is found
        expected = reader.consumeToAny(new String("ld!"));
        Assert.assertEquals("Hello, World!", expected);

        // Test reading characters until the end of the input
        expected = reader.consumeToEnd();
        Assert.assertEquals("Hello, World!", expected);

        // Test reading letters and letter-then-digit sequences
        Assert.assertTrue(reader.matchesLetterSequence());
        Assert.assertTrue(reader.matchesLetterThenDigitSequence());

        // Test reading hex sequences
        reader.mark();
        expected = reader.consumeHexSequence();
        reader.rewindToMark();
        Assert.assertEquals("0x68656c6c6f676e617264617373", expected);

        // Test reading digits sequences
        reader.mark();
        expected = reader.consumeDigitSequence();
        reader.rewindToMark();
        Assert.assertEquals("68656c6c6f7e746172646173", expected);

        // Test reading a specific character or sequence
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ld!"));

        // Test reading a specific character or sequence, case insensitive
        Assert.assertTrue(reader.matchesIgnoreCase("LD!"));
    }

    @Test
    public void testCharacterReader_StringInput() {
        String input = "Hello, World!";
        CharacterReader reader = new CharacterReader(input);

        // Test basic functionality
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('H', reader.consume());

        // Test reading characters up to a specific char
        String expected = reader.consumeTo('l');
        Assert.assertEquals("Hello, Wor", expected);
        Assert.assertEquals('d', reader.current());

        // Test reading characters up to a specific sequence
        expected = reader.consumeTo("ld!");
        Assert.assertEquals("Hello, World!", expected);
        Assert.assertEquals('\0', reader.current());

        // Test reading characters until a specific char is found
        expected = reader.consumeToAny('l');
        Assert.assertEquals("Hello, World!", expected);

        // Test reading characters until a specific char is found, sorted
        expected = reader.consumeToAnySorted(new char[]{'d', 'l'});
        Assert.assertEquals("Hello, World!", expected);

        // Test reading characters until a specific sequence is found
        expected = reader.consumeToAny(new String("ld!"));
        Assert.assertEquals("Hello, World!", expected);

        // Test reading characters until the end of the input
        expected = reader.consumeToEnd();
        Assert.assertEquals("Hello, World!", expected);

        // Test reading letters and letter-then-digit sequences
        Assert.assertTrue(reader.matchesLetterSequence());
        Assert.assertTrue(reader.matchesLetterThenDigitSequence());

        // Test reading hex sequences
        reader.mark();
        expected = reader.consumeHexSequence();
        reader.rewindToMark();
        Assert.assertEquals("0x68656c6c6f676e617264617373", expected);

        // Test reading digits sequences
        reader.mark();
        expected = reader.consumeDigitSequence();
        reader.rewindToMark();
        Assert.assertEquals("68656c6c6f7e746172646173", expected);

        // Test reading a specific character or sequence
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ld!"));

        // Test reading a specific character or sequence, case insensitive
        Assert.assertTrue(reader.matchesIgnoreCase("LD!"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReader_IOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Invalid Input"));
        reader.bufferUp();
        reader.consume(); // Read one character to trigger IOException
    }

    @Test
    public void testCharacterReader_ArrayInput() {
        char[] input = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', '\0'};
        CharacterReader reader = new CharacterReader(new String(new String(new String(new sun.misc.CharArrayDecoder().decode(input)))));

        // Test basic functionality
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('H', reader.consume());

        // Test reading characters up to a specific char
        String expected = reader.consumeTo('l');
        Assert.assertEquals(new String(input, 0, 5), expected);
        Assert.assertEquals('d', reader.current());

        // Test reading characters up to a specific sequence
        expected = reader.consumeTo("ld!");
        Assert.assertEquals(new String(input), expected);
        Assert.assertEquals('\0', reader.current());

        // Test reading characters until a specific char is found
        expected = reader.consumeToAny('l');
        Assert.assertEquals(new String(input), expected);

        // Test reading characters until a specific char is found, sorted
        expected = reader.consumeToAnySorted(new char[]{'d', 'l'});
        Assert.assertEquals(new String(input), expected);

        // Test reading characters until a specific sequence is found
        expected = reader.consumeToAny(new String("ld!"));
        Assert.assertEquals(new String(input), expected);

        // Test reading characters until the end of the input
        expected = reader.consumeToEnd();
        Assert.assertEquals(new String(input), expected);

        // Test reading letters and letter-then-digit sequences
        Assert.assertTrue(reader.matchesLetterSequence());
        Assert.assertTrue(reader.matchesLetterThenDigitSequence());

        // Test reading hex sequences
        reader.mark();
        expected = reader.consumeHexSequence();
        reader.rewindToMark();
        Assert.fail("Expected IllegalArgumentException for non-string input to consumeHexSequence() method.");

        // Test reading digits sequences
        reader.mark();
        expected = reader.consumeDigitSequence();
        reader.rewindToMark();
        Assert.fail("Expected IllegalArgumentException for non-string input to consumeDigitSequence() method.");

        // Test reading a specific character or sequence
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ld!"));

        // Test reading a specific character or sequence, case insensitive
        Assert.assertTrue(reader.matchesIgnoreCase("LD!"));
    }
}
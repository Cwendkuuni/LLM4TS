package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Tests for the CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "Hello World! <tag>Hello Again</tag>";

        // Test empty reader
        CharacterReader reader1 = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader1.isEmpty());
        Assert.assertEquals(-1, reader1.nextIndexOf('H'));
        Assert.assertEquals(-1, reader1.nextIndexOf("Hello"));
        Assert.assertEquals(-1, reader1.nextIndexOf('<'));

        // Test reader with input
        CharacterReader reader2 = new CharacterReader(new StringReader(input));
        Assert.assertFalse(reader2.isEmpty());
        Assert.assertEquals(0, reader2.pos());
        Assert.assertEquals('H', reader2.current());
        Assert.assertEquals('H', reader2.consume());
        Assert.assertEquals('e', reader2.current());
        Assert.assertEquals('l', reader2.consume());
        Assert.assertEquals('l', reader2.consume());
        Assert.assertEquals('o', reader2.consume());
        Assert.assertEquals(' ', reader2.consume());
        Assert.assertEquals('W', reader2.consume());
        Assert.assertEquals('r', reader2.consume());
        Assert.assertEquals('d', reader2.consume());
        Assert.assertEquals('!', reader2.consume());
        Assert.assertEquals(' ', reader2.consume());
        Assert.assertEquals('<', reader2.consume());
        Assert.assertEquals('t', reader2.consume());
        Assert.assertEquals('a', reader2.consume());
        Assert.assertEquals('g', reader2.consume());
        Assert.assertEquals('>', reader2.consume());
        Assert.assertEquals('H', reader2.consume());
        Assert.assertEquals('e', reader2.consume());
        Assert.assertEquals('l', reader2.consume());
        Assert.assertEquals('l', reader2.consume());
        Assert.assertEquals('o', reader2.consume());
        Assert.assertEquals(' ', reader2.consume());
        Assert.assertEquals('A', reader2.consume());
        Assert.assertEquals('g', reader2.consume());
        Assert.assertEquals('a', reader2.consume());
        Assert.assertEquals('i', reader2.consume());
        Assert.assertEquals('n', reader2.consume());
        Assert.assertEquals('>', reader2.consume());
        Assert.assertTrue(reader2.isEmpty());

        // Test reader with empty input
        CharacterReader reader3 = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader3.isEmpty());
        Assert.assertEquals(-1, reader3.nextIndexOf('H'));
        Assert.assertEquals(-1, reader3.nextIndexOf("Hello"));
        Assert.assertEquals(-1, reader3.nextIndexOf('<'));

        // Test reader with null input
        CharacterReader reader4 = new CharacterReader(null);
        Assert.assertNull(reader4);

        // Test reader with illegal size
        try {
            CharacterReader reader5 = new CharacterReader(new StringReader(""), -1);
            Assert.fail("Unexpectedly created CharacterReader with negative size.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test reader with zero size
        CharacterReader reader6 = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader6.isEmpty());
        Assert.assertEquals(-1, reader6.nextIndexOf('H'));
        Assert.assertEquals(-1, reader6.nextIndexOf("Hello"));
        Assert.assertEquals(-1, reader6.nextIndexOf('<'));
    }
}
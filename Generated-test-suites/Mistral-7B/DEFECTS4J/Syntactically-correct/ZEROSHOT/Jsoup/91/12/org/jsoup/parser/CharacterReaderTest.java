package org.jsoup.parser;

import org.jsoup.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        char[] input = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input)));

        // test initial state
        Assert.assertEquals(-1, reader.current());
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());

        // test basic consumption
        Assert.assertEquals('h', reader.consume());
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('e', reader.current());
        Assert.assertEquals(1, reader.pos());

        // test consumption beyond EOF
        Assert.assertEquals(-1, reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertTrue(reader.isEmpty());

        // test rewinding
        reader.mark();
        Assert.assertEquals('h', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('h', reader.current());
        Assert.assertEquals(0, reader.pos());

        // test nextIndexOf
        Assert.assertEquals(4, reader.nextIndexOf(' '));
        Assert.assertEquals(11, reader.nextIndexOf('!'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));

        // test consumeTo and consumeToEnd
        String str = reader.consumeTo(' ');
        Assert.assertEquals("hell", str);
        Assert.assertEquals(5, reader.pos());
        str = reader.consumeToEnd();
        Assert.assertEquals("hello world!", str);
        Assert.assertEquals(17, reader.pos());
        Assert.assertTrue(reader.isEmpty());

        // test consumeTo and consumeTo with CharSequence
        String seq = "world";
        str = reader.consumeTo(seq);
        Assert.assertEquals("hello world", str);
        Assert.assertEquals(12, reader.pos());

        // test consumeToAny
        char[] chars = {'h', 'e', 'l', 'l', 'o', 'w', 'r', 'l', 'd'};
        str = reader.consumeToAny(chars);
        Assert.assertEquals("hello world!", str);
        Assert.assertEquals(17, reader.pos());

        // test consumeToAnySorted
        char[] sortedChars = {'d', 'd', 'd', 'h', 'h', 'h', 'l', 'l', 'l', 'o', 'o', 'o', 'r', 'r', 'r', 'w'};
        str = reader.consumeToAnySorted(sortedChars);
        Assert.assertEquals("hello world!", str);
        Assert.assertEquals(17, reader.pos());

        // test consumeData
        str = reader.consumeData();
        Assert.assertEquals("&amp;&lt;null", str);
        Assert.assertEquals(12, reader.pos());

        // test consumeTagName
        str = reader.consumeTagName();
        Assert.assertEquals("world", str);
        Assert.assertEquals(12, reader.pos());

        // test consumeToEnd
        str = reader.consumeToEnd();
        Assert.assertEquals("", str);
        Assert.assertEquals(17, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithEmptyInput() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(new char[0])));
        Assert.assertEquals(-1, reader.current());
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithIoException() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(new char[0]))) {
            @Override
            public void bufferUp() {
                throw new IOException("Test IOException");
            }
        };
        reader.consume();
    }
}
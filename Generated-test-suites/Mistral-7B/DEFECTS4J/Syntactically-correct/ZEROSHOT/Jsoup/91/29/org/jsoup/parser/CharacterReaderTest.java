package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringReader;

/**
 * Test class for CharacterReader class
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderBasic() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());

        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        Assert.assertEquals('l', reader.current());
        reader.consume();
        Assert.assertEquals('l', reader.current());
        reader.consume();
        Assert.assertEquals('o', reader.current());
        reader.consume();
        Assert.assertEquals(' ', reader.current());
        reader.consume();
        Assert.assertEquals('W', reader.current());
        reader.consume();
        Assert.assertEquals('o', reader.current());
        reader.consume();
        Assert.assertEquals('r', reader.current());
        reader.consume();
        Assert.assertEquals('l', reader.current());
        reader.consume();
        Assert.assertEquals('d', reader.current());

        Assert.assertEquals(0, reader.pos());
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals(2, reader.pos());

        String str = reader.consumeTo('l');
        Assert.assertEquals("ll", str);
        Assert.assertEquals('o', reader.current());

        reader.unconsume();
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String str;

        str = reader.consumeTo('a');
        Assert.assertEquals("", str);
        Assert.assertEquals('a', reader.current());

        str = reader.consumeTo('z');
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", str);
        Assert.assertEquals(0, reader.pos());

        str = reader.consumeTo('x');
        Assert.assertEquals("abcdefghijklm", str);
        Assert.assertEquals('p', reader.current());

        str = reader.consumeTo(' ');
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", str);
        Assert.assertEquals(' ', reader.current());

        str = reader.consumeToAny(new char[]{'x', 'y', 'z'});
        Assert.assertEquals("xyz", str);
        Assert.assertEquals(' ', reader.current());

        str = reader.consumeToEnd();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", str);
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        String str;

        str = reader.consumeToAnySorted(new char[]{'9', '5', '1'});
        Assert.assertEquals("1", str);
        Assert.assertEquals('2', reader.current());

        str = reader.consumeToAnySorted(new char[]{'5', '3', '7'});
        Assert.assertEquals("35", str);
        Assert.assertEquals('7', reader.current());

        str = reader.consumeToAnySorted(new char[]{'0'});
        Assert.assertEquals("0", str);
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String str;

        str = reader.consumeData();
        Assert.assertEquals("&<", str);
        Assert.assertEquals('n', reader.current());

        str = reader.consumeData();
        Assert.assertEquals("null", str);
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("\t<title>Test</title>\n"));
        String str;

        str = reader.consumeTagName();
        Assert.assertEquals("title", str);
        Assert.assertEquals('<', reader.current());

        str = reader.consumeTagName();
        Assert.assertEquals("", str);
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(11, reader.pos() + reader.bufPos);

        reader.advance();
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals(12, reader.pos() + reader.bufPos);

        reader.mark();
        reader.advance();
        reader.advance();
        Assert.assertEquals(3, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals(1, reader.pos());

        reader.consumeTo('l');
        Assert.assertEquals(5, reader.pos());
        reader.consumeTo('o');
        Assert.assertEquals(7, reader.pos());

        reader.consumeToEnd();
        Assert.assertEquals(11, reader.pos());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(reader.isEmpty());

        reader.consumeTo('l');
        reader.consumeTo('o');
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertTrue(reader.matches("Hell"));
        Assert.assertFalse(reader.matches('x'));
        Assert.assertFalse(reader.matches("World"));

        reader.consumeTo('l');
        reader.consumeTo('o');
        Assert.assertFalse(reader.matches('l'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesIgnoreCase("HELLO"));
        Assert.assertTrue(reader.matchesIgnoreCase("World"));
        Assert.assertFalse(reader.matchesIgnoreCase("xxx"));
        Assert.assertTrue(reader.matchesIgnoreCase("HEllo"));

        reader.consumeTo('l');
        reader.consumeTo('o');
        Assert.assertFalse(reader.matchesIgnoreCase("LO"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesAny('l', 'o'));
        Assert.assertFalse(reader.matchesAny('x', 'o'));
        Assert.assertTrue(reader.matchesAny('l', 'o', 'h'));
        Assert.assertFalse(reader.matchesAny('x', 'x'));

        reader.consumeTo('l');
        reader.consumeTo('o');
        Assert.assertFalse(reader.matchesAny('l', 'o'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'o'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o', 'h'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'x'}));

        reader.consumeTo('l');
        reader.consumeTo('o');
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'l', 'o'}));
    }
}
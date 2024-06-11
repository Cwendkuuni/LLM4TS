package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        String input = "Hello World! <tag>Test</tag>";
        reader = new CharacterReader(new StringReader(input));
    }

    @Test
    public void testEmptyReader() {
        CharacterReader emptyReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(emptyReader.isEmpty());
        Assert.assertEquals(-1, emptyReader.nextIndexOf('a'));
    }

    @Test
    public void testRegularReader() {
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.unconsume();
        Assert.assertEquals('e', reader.current());

        reader.advance();
        Assert.assertEquals('H', reader.current());

        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals('e', reader.current());

        Assert.assertEquals(3, reader.nextIndexOf("ll"));
        Assert.assertEquals(8, reader.nextIndexOf("<tag>Test</tag>"));

        String data = reader.consumeTo('>');
        Assert.assertEquals("Test", data);
        reader.consume(); // consume tag end

        String tagName = reader.consumeTagName();
        Assert.assertEquals("tag", tagName);
    }

    @Test
    public void testScanTo() {
        Assert.assertEquals("Hello", reader.consumeTo(' '));
        Assert.assertEquals("World! <tag>", reader.toString());

        reader.reset(); // reset reader position
        Assert.assertEquals("World! <tag>", reader.toString());
        Assert.assertEquals("<tag>", reader.consumeToTagName());
        Assert.assertEquals("Test", reader.consumeTo("<tag>Test</tag>"));
    }

    @Test
    public void testScanToEnd() {
        Assert.assertEquals("Hello World! <tag>Test</tag>", reader.consumeToEnd());
    }

    @Test
    public void testScanToAny() {
        Assert.assertEquals("llo", reader.consumeToAny('/'));
        Assert.assertEquals("Hello World! <tag>Test</tag>", reader.toString());

        reader.reset(); // reset reader position
        Assert.assertEquals("Hello World! <tag>Test</tag>", reader.toString());
        Assert.assertEquals("Hello World! <tag>", reader.consumeToAnySorted(new char[]{'l', 't'}));
        Assert.assertEquals("Hello World! <tag>Test</tag>", reader.toString());
    }

    @Test
    public void testScanToAnySorted() {
        Assert.assertEquals("Hello World! <tag>Test</tag>", reader.toString());
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{}));

        reader.reset(); // reset reader position
        Assert.assertEquals("Hello World! <tag>Test</tag>", reader.toString());
        Assert.assertEquals("Hello", reader.consumeToAnySorted(new char[]{'l', 'o'}));
        Assert.assertEquals("Hello World! <tag>Test</tag>", reader.toString());
    }

    @Test
    public void testScanDigitOrLetter() {
        String letterSequence = reader.consumeLetterSequence();
        Assert.assertEquals("Hello", letterSequence);

        String letterThenDigitSequence = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello0", letterThenDigitSequence);

        reader.reset(); // reset reader position
        Assert.assertEquals("Hello World! <tag>Test</tag>", reader.toString());
        Assert.assertEquals("Hello", reader.consumeLetterSequence());
        Assert.assertEquals("World! <tag>Test</tag>", reader.toString());
        Assert.assertEquals("0", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("World! <tag>Test</tag>", reader.toString());
    }

    @Test
    public void testScanHexSequence() {
        String hexSequence = reader.consumeHexSequence();
        Assert.assertEquals("HelloWorld! <tag>Test</tag>", reader.toString());
        Assert.assertEquals("48656C6C6F20776F726C64213C61746573743E", hexSequence);

        reader.reset(); // reset reader position
        Assert.assertEquals("HelloWorld! <tag>Test</tag>", reader.toString());
        Assert.assertEquals("48656C", reader.consumeHexSequence());
        Assert.assertEquals("6C6F20776F726C64213C61746573743E", reader.toString());
    }

    @Test
    public void testScanDigitSequence() {
        String digitSequence = reader.consumeDigitSequence();
        Assert.assertEquals("HelloWorld! <tag>Test</tag>", reader.toString());
        Assert.assertEquals("1234567890", digitSequence);

        reader.reset(); // reset reader position
        Assert.assertEquals("HelloWorld! <tag>Test</tag>", reader.toString());
        Assert.assertEquals("123456", reader.consumeDigitSequence());
        Assert.assertEquals("7890", reader.toString());
    }

    @Test
    public void testMatch() {
        Assert.assertTrue(reader.matches('H'));
        Assert.assertTrue(reader.matches("He"));
        Assert.assertTrue(reader.matches("He"));
        Assert.assertFalse(reader.matches("ll"));

        reader.reset(); // reset reader position
        Assert.assertEquals("Hello World! <tag>Test</tag>", reader.toString());
        Assert.assertTrue(reader.matches("He"));
        Assert.assertFalse(reader.matches("ll"));
    }

    @Test
    public void testMatchIgnoreCase() {
        Assert.assertTrue(reader.matchesIgnoreCase("he"));
        Assert.assertTrue(reader.matchesIgnoreCase("He"));
        Assert.assertFalse(reader.matchesIgnoreCase("ll"));

        reader.reset(); // reset reader position
        Assert.assertEquals("Hello World! <tag>Test</tag>", reader.toString());
        Assert.assertTrue(reader.matchesIgnoreCase("He"));
        Assert.assertFalse(reader.matchesIgnoreCase("ll"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader ioReader = new CharacterReader(new ByteArrayReader(new ByteArrayInputStream(new byte[1])));
        ioReader.consume();
    }

    @Test
    public void testStringReader() {
        CharacterReader stringReader = new CharacterReader(new StringReader(""));
        String input = "Hello World! <tag>Test</tag>";
        CharacterReader inputReader = new CharacterReader(new StringReader(input));

        stringReader.mark(100);
        Assert.assertEquals("", stringReader.consumeToEnd());
        Assert.assertEquals(0, stringReader.pos());
        Assert.assertEquals(0, stringReader.readerPos);
        stringReader.rewindToMark();
        Assert.assertEquals("", stringReader.consumeToEnd());
        Assert.assertEquals(0, stringReader.pos());
        Assert.assertEquals(0, stringReader.readerPos);

        Assert.assertEquals("", stringReader.toString());
        Assert.assertEquals(input, inputReader.toString());
        Assert.assertEquals(0, stringReader.pos());
        Assert.assertEquals(0, inputReader.pos());
        Assert.assertEquals(input.length(), inputReader.bufLength);
        Assert.assertFalse(inputReader.isEmpty());
    }

    @Test
    public void testReader() {
        Reader readerInput = new ByteArrayInputStream(new byte[1]);
        CharacterReader reader = new CharacterReader(readerInput, 1);
        Assert.assertEquals(-1, reader.pos());
        Assert.assertEquals(-1, reader.readerPos);
        Assert.assertFalse(reader.isEmpty());

        reader.bufferUp();
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertEquals(0, reader.bufLength);
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(0, reader.bufSplitPoint);
        Assert.assertEquals(0, reader.bufMark);
        Assert.assertEquals(0, reader.nextIndexOf('a'));

        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(0, reader.bufSplitPoint);
        Assert.assertEquals(0, reader.bufMark);
        Assert.assertEquals(0, reader.nextIndexOf('a'));
    }

    @Test
    public void testString() {
        String input = "Hello World! <tag>Test</tag>";
        StringReader stringReader = new StringReader(input);
        CharacterReader reader = new CharacterReader(stringReader);

        Assert.assertEquals("", reader.consumeToEnd());
        Assert.assertEquals(input, reader.toString());
        Assert.assertEquals(input.length(), reader.pos());
        Assert.assertEquals(input.length(), reader.readerPos);
        Assert.assertEquals(input.length(), reader.bufLength);
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testArray() {
        char[] charArray = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', '<', 't', 'a', 'g', '>', 'T', 'e', 's', 't'};
        Reader readerInput = new ByteArrayReader(new ByteArrayInputStream(new byte[charArray.length]));
        CharacterReader reader = new CharacterReader(readerInput, charArray.length);

        Assert.assertEquals("", reader.consumeToEnd());
        Assert.assertEquals(new String(charArray), reader.toString());
        Assert.assertEquals(charArray.length, reader.pos());
        Assert.assertEquals(charArray.length, reader.readerPos);
        Assert.assertEquals(charArray.length, reader.bufLength);
        Assert.assertFalse(reader.isEmpty());
    }
}
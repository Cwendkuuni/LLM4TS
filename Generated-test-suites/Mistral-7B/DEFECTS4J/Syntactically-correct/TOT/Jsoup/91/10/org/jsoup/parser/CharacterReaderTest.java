package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(0, reader.consume());
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMultipleCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals('B', reader.current());
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('B', reader.consume());
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('C', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('B', reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testEmptyStringReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(0, reader.consume());
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("ABC", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("A") {
            @Override
            public int read(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.consume();
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCABC"));
        Assert.assertEquals(4, reader.nextIndexOf('B'));
        Assert.assertEquals(7, reader.pos());
        Assert.assertEquals(5, reader.nextIndexOf('B', 5));
        Assert.assertEquals(5, reader.pos());
        Assert.assertEquals(-1, reader.nextIndexOf('X'));
        Assert.assertEquals(10, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals(-1, reader.nextIndexOf(new String("AB")));
        Assert.assertEquals(3, reader.pos());
        reader.mark();
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals(1, reader.nextIndexOf('B'));
        Assert.assertEquals('B', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('B', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
        reader.consume();
        reader.consume();
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCABC"));
        Assert.assertEquals(4, reader.nextIndexOf("BA"));
        Assert.assertEquals(7, reader.pos());
        Assert.assertEquals(-1, reader.nextIndexOf(new String("CD")));
        Assert.assertEquals(10, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals(1, reader.nextIndexOf("AB"));
        Assert.assertEquals('B', reader.consume());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals('C', reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('C', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
        reader.consume();
        reader.consume();
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testUnicode() {
        CharacterReader reader = new CharacterReader(new StringReader("\u00E9\u00E8\u00E9\u00E8"));
        Assert.assertEquals("\u00E9\u00E8\u00E9\u00E8", reader.consumeToEnd());
        Assert.assertEquals(8, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC\u00E9"));
        String consumed = reader.consumeTo('\u00E9');
        Assert.assertEquals("ABC", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('\u00E9', reader.current());
        consumed = reader.consumeTo('\u00E9');
        Assert.assertEquals("", consumed);
        Assert.assertEquals(4, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEF"));
        String consumed = reader.consumeToAny('E');
        Assert.assertEquals("ABC", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('E', reader.current());
        consumed = reader.consumeToAny('A');
        Assert.assertEquals("", consumed);
        Assert.assertEquals(4, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEF"));
        String consumed = reader.consumeToAnySorted(new char[]{'E', 'B'});
        Assert.assertEquals("AB", consumed);
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('B', reader.current());
        consumed = reader.consumeToAnySorted(new char[]{'A', 'E'});
        Assert.assertEquals("", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }
}
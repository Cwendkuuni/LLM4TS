package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(0, reader.readerPos);
    }

    @Test
    public void testCharacterReaderSingleCharacterInput() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(1, reader.readerPos);
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(2, reader.readerPos);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacterInput() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(3, reader.readerPos);
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals('B', reader.current());
        Assert.assertEquals('B', reader.consume());
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals('C', reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("ABC") {
            @Override
            public int read(char[] c, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });
        reader.bufferUp();
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        reader.bufferUp();
        Assert.assertTrue(reader.isEmptyNoBufferUp());
        Assert.assertEquals(14, reader.bufLength);
        Assert.assertEquals(0, reader.bufMark);
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertEquals(14, reader.bufSplitPoint);
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        reader.bufferUp();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        String consumed = reader.consumeTo('E');
        Assert.assertEquals(5, consumed.length());
        Assert.assertEquals("ABCDE", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        String consumed = reader.consumeToAny(new char[]{'E', 'F'});
        Assert.assertEquals(2, consumed.length());
        Assert.assertEquals("EF", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        String consumed = reader.consumeToAnySorted(new char[]{'E', 'F', 'G'});
        Assert.assertEquals(3, consumed.length());
        Assert.assertEquals("EFG", consumed);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ&<null"));
        String consumed = reader.consumeData();
        Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", consumed);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title></html>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("html", consumed);
    }

    @Test
    public void testCharacterReaderConsumeTagNameWithAttributes() {
        CharacterReader reader = new CharacterReader(new StringReader("<html id='myId' class='myClass'><title>Test</title></html>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("html", consumed);
        String id = reader.consumeTo("id='");
        String cla = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("myId", id);
        Assert.assertEquals("myClass", cla);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("A1B2C3D4E5F6G7H8I9J10"));
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("A", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("A1B2C3D4E5F6G7H8I9J10"));
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("A1", consumed);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0x1A 0x2B 0x3C 0x4D 0x5E 0x6F"));
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("1A", consumed);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123 456 789 1011 1213 1314"));
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("123", consumed);
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        Assert.assertTrue(reader.matches('A'));
        Assert.assertTrue(reader.matchConsume("AB"));
        Assert.assertFalse(reader.matches('Z'));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("AbCd"));
        Assert.assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title></html>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertFalse(reader.containsIgnoreCase("/style"));
    }

    @Test
    public void testCharacterReaderConsumeTagNameWithQuotationMarks() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title></html>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("html", consumed);
        String title = reader.consumeTo('>');
        Assert.assertEquals("title", title);
        String quotes = reader.consumeTo('"');
        Assert.assertEquals("Test", quotes);
    }

    @Test
    public void testCharacterReaderConsumeTagNameWithAttribute() {
        CharacterReader reader = new CharacterReader(new StringReader("<html id='myId' class='myClass'><title>Test</title></html>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("html", consumed);
        String id = reader.consumeTo(" id='");
        String cla = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("myId", id);
        Assert.assertEquals("myClass", cla);
        String title = reader.consumeTo('>');
        Assert.assertEquals("title", title);
    }
}
package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        Reader reader = new StringReader("Test String");
        CharacterReader charReader = new CharacterReader(reader);
        Assert.assertNotNull(charReader.charBuf);
        Assert.assertEquals(maxBufferLen, charReader.charBuf.length);
    }

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader charReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(charReader.isEmpty());
        Assert.assertEquals(0, charReader.pos());
    }

    @Test
    public void testCharacterReaderSimpleInput() {
        CharacterReader charReader = new CharacterReader(new StringReader("Hello World"));
        char c;
        for (int i = 0; i < "Hello World".length(); i++) {
            c = charReader.consume();
            Assert.assertEquals('H' + i, c);
        }
        Assert.assertEquals(11, charReader.pos());
        Assert.assertFalse(charReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader charReader = new CharacterReader(new StringReader("Test String"));
        char c = charReader.consume();
        charReader.reader.mark(maxBufferLen);
        charReader.reader.skip(maxBufferLen + 1);
        charReader.consume();
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader charReader = new CharacterReader(new StringReader("Test String"));
        char c = charReader.consume();
        charReader.bufferUp();
        Assert.assertEquals(maxBufferLen, charReader.bufLength);
        Assert.assertEquals(maxBufferLen + 1, charReader.readerPos);
        Assert.assertEquals(0, charReader.bufPos);
        Assert.assertEquals(maxBufferLen, charReader.bufSplitPoint);
        Assert.assertEquals(EOF, charReader.current());
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader charReader = new CharacterReader(new StringReader("Test String"));
        char c = charReader.consume();
        charReader.mark();
        charReader.rewindToMark();
        Assert.assertEquals(c, charReader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader charReader = new CharacterReader(new StringReader("Test String"));
        char c;
        int pos = 0;
        for (int i = 0; i < "Test String".length(); i++) {
            c = charReader.consume();
            pos = charReader.nextIndexOf(c);
            Assert.assertEquals(i, pos);
        }
        c = 'Z';
        pos = charReader.nextIndexOf(c);
        Assert.assertEquals(-1, pos);
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader charReader = new CharacterReader(new StringReader("Hello World"));
        int pos = charReader.nextIndexOf("llo");
        Assert.assertEquals(3, pos);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader charReader = new CharacterReader(new StringReader("Test String"));
        String str;
        str = charReader.consumeTo('T');
        Assert.assertEquals("", str);
        Assert.assertEquals(0, charReader.pos());
        str = charReader.consumeTo('s');
        Assert.assertEquals("Test", str);
        Assert.assertEquals(4, charReader.pos());
        str = charReader.consumeTo('n');
        Assert.assertEquals("Test String", str);
        Assert.assertEquals(11, charReader.pos());
        str = charReader.consumeToEnd();
        Assert.assertEquals("Test String", str);
        Assert.assertEquals(11, charReader.pos());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader charReader = new CharacterReader(new StringReader("Test String"));
        String str;
        str = charReader.consumeToAny(new char[]{'s', 't'});
        Assert.assertEquals("Test", str);
        Assert.assertEquals(4, charReader.pos());
        str = charReader.consumeToAnySorted(new char[]{'s', 't'});
        Assert.assertEquals("Test", str);
        Assert.assertEquals(4, charReader.pos());
        str = charReader.consumeToAny(new char[]{});
        Assert.assertEquals("", str);
        Assert.assertEquals(4, charReader.pos());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader charReader = new CharacterReader(new StringReader("Test <data> String"));
        String str;
        str = charReader.consumeData();
        Assert.assertEquals("Test <data>", str);
        Assert.assertEquals(7, charReader.pos());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader charReader = new CharacterReader(new StringReader("<html><title>Test String</title>"));
        String str;
        str = charReader.consumeTagName();
        Assert.assertEquals("html", str);
        Assert.assertEquals(6, charReader.pos());
        str = charReader.consumeTagName();
        Assert.assertEquals("title", str);
        Assert.assertEquals(11, charReader.pos());
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader charReader = new CharacterReader(new StringReader("Test String"));
        String str = charReader.toString();
        Assert.assertEquals("Test String", str);
    }

    @Test
    public void testCharacterReaderCacheString() {
        CharacterReader charReader = new CharacterReader(new StringReader("Test String"));
        String str1 = charReader.consumeTo('S');
        String str2 = charReader.consumeTo('S');
        Assert.assertEquals(str1, str2);
    }
}
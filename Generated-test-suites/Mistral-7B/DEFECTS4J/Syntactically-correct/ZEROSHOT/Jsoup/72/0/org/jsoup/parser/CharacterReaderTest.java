package org.jsoup.parser;

import org.junit.Assert;import org.junit.Test;

import java.io.IOException;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertNotNull(reader.charBuf);
        Assert.assertNotNull(reader.reader);
        Assert.assertEquals(reader.bufLength, 1024 * 32);
    }

    @Test
    public void testCharacterReaderBufferUp() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertNotEquals(reader.bufPos, 0);
        reader.bufferUp();
        Assert.assertEquals(reader.bufPos, 1024 * 32);
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertEquals(reader.pos(), 0);
        reader.advance();
        Assert.assertEquals(reader.pos(), 1);
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(reader.isEmpty());
        reader.advance();
        reader.advance();
        Assert.assertFalse(reader.isEmpty());
        reader.consume();
        reader.consume();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertEquals(reader.current(), 'H');
        reader.consume();
        Assert.assertEquals(reader.current(), 'e');
        reader.consume();
        Assert.assertEquals(reader.current(), 'l');
        reader.consume();
        Assert.assertEquals(reader.current(), 'l');
        Assert.assertEquals(reader.current(), 'o');
        Assert.assertEquals(reader.current(), ' ');
        reader.consume();
        Assert.assertEquals(reader.current(), 'W');
        reader.consume();
        reader.consume();
        Assert.assertEquals(reader.current(), '\r');
        Assert.assertEquals(reader.current(), '\n');
        Assert.assertEquals(reader.current(), '\0');
        reader.consume();
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        reader.consume();
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        reader.consume();
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        reader.consume();
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        Assert.assertEquals(reader.current(), '\0');
        reader.consume();
        Assert.assertEquals(reader.current(), '\0');
        reader.consume();
        Assert.assertEquals(reader.current(), '\0');
        reader.consume();
        Assert.assertEquals(reader.current(), '\0');
        reader.consume();
        Assert.assertEquals(reader.current(), EOF);
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        reader.consume();
        Assert.assertEquals(reader.current(), 'e');
        reader.consume();
        Assert.assertEquals(reader.current(), 'l');
        reader.consume();
        Assert.assertEquals(reader.current(), 'l');
        reader.consume();
        Assert.assertEquals(reader.current(), 'o');
        reader.consume();
        Assert.assertEquals(reader.current(), ' ');
        reader.consume();
        Assert.assertEquals(reader.current(), 'W');
        reader.consume();
        reader.consume();
        Assert.assertEquals(reader.current(), '\r');
        Assert.assertEquals(reader.current(), '\n');
        Assert.assertEquals(reader.current(), '\0');
        reader.consume();
        reader.consume();
        reader.consume();
        Assert.assertEquals(reader.current(), EOF);
    }

    @Test
    public void testCharacterReaderUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        reader.consume();
        reader.consume();
        reader.unconsume();
        Assert.assertEquals(reader.current(), 'l');
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals(reader.current(), 'l');
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertEquals(reader.nextIndexOf('l'), 3);
        Assert.assertEquals(reader.nextIndexOf('z'), -1);
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertEquals(reader.nextIndexOf("ll"), 4);
        Assert.assertEquals(reader.nextIndexOf("World"), 6);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        String result = reader.consumeTo('l');
        Assert.assertEquals(result, "He");
        reader.consume();
        reader.consume();
        reader.consume();
        String result2 = reader.consumeTo('l');
        Assert.assertEquals(result2, "llo");
        reader.consume();
        reader.consume();
        reader.consume();
        String result3 = reader.consumeTo("World");
        Assert.assertEquals(result3, "World");
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        String result = reader.consumeToAny(',');
        Assert.assertEquals(result, "");
        reader.consume();
        reader.consume();
        reader.consume();
        String result2 = reader.consumeToAny('/');
        Assert.assertEquals(result2, "llo");
        reader.consume();
        reader.consume();
        String result3 = reader.consumeToAnySorted(new char[]{'/', 'l', 'o'});
        Assert.assertEquals(result3, "llo");
        reader.consume();
        reader.consume();
        String result4 = reader.consumeToAnySorted(new char[]{'l', 'o', '/'});
        Assert.assertEquals(result4, "llo");
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;html&gt;"));
        reader.bufferUp();
        String result = reader.consumeData();
        Assert.assertEquals(result, "&lt;html&gt;");
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("\t<title>Test</title>\n"));
        reader.bufferUp();
        String result = reader.consumeTagName();
        Assert.assertEquals(result, "<title>");
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        String result = reader.consumeToEnd();
        Assert.assertEquals(result, "Hello World\r\n");
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        String result = reader.consumeLetterSequence();
        Assert.assertEquals(result, "Hello");
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello12345 World"));
        reader.bufferUp();
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(result, "Hello12345");
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&xa0;&#x1F4;"));
        reader.bufferUp();
        String result = reader.consumeHexSequence();
        Assert.assertEquals(result, "&xa0;&#x1F4;");
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        reader.bufferUp();
        String result = reader.consumeDigitSequence();
        Assert.assertEquals(result, "12345");
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchesIgnoreCase("ll"));
        Assert.assertTrue(reader.matchesIgnoreCase("world"));
        Assert.assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchesAny('l'));
        Assert.assertTrue(reader.matchesAny('o'));
        Assert.assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'z', 'l'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello12345 World"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchConsume("ll"));
        Assert.assertEquals(reader.pos(), 6);
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchConsumeIgnoreCase("world"));
        Assert.assertEquals(reader.pos(), 6);
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("body"));
        Assert.assertFalse(reader.containsIgnoreCase("/head"));
    }
}
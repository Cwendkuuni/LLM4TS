package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader class
 */
public final class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        reader.advance();
        Assert.assertEquals(1, reader.pos());
        reader.advance();
        reader.advance();
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertFalse(reader.isEmpty());
        reader.consume();
        reader.consume();
        reader.consume();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals('T', reader.current());
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        char c = reader.consume();
        Assert.assertEquals('T', c);
        c = reader.consume();
        Assert.assertEquals('e', c);
        c = reader.consume();
        Assert.assertEquals('s', c);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Test String")) {
            @Override
            public int read(char[] cbuf, int offset, int length) throws IOException {
                throw new IOException();
            }
        };
        reader.consume();
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        reader.advance();
        Assert.assertEquals('e', reader.current());
        reader.advance();
        Assert.assertEquals('s', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals(2, reader.nextIndexOf('s'));
        reader.advance();
        Assert.assertEquals(3, reader.nextIndexOf('s'));
        reader.advance();
        Assert.assertEquals(-1, reader.nextIndexOf('s'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals(2, reader.nextIndexOf("es"));
        reader.advance();
        reader.advance();
        Assert.assertEquals(3, reader.nextIndexOf("es"));
        reader.advance();
        Assert.assertEquals(-1, reader.nextIndexOf("es"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        String result = reader.consumeTo('e');
        Assert.assertEquals("Te", result);
        reader.advance();
        reader.advance();
        result = reader.consumeTo('s');
        Assert.assertEquals("Test", result);
        reader.consume();
        Assert.assertEquals("Test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        String result = reader.consumeTo("es");
        Assert.assertEquals("Test", result);
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        result = reader.consumeTo("es");
        Assert.assertEquals("String", result);
        reader.consume();
        reader.consume();
        Assert.assertEquals("String", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        String result = reader.consumeToAny(',', ';');
        Assert.assertEquals("Test", result);
        reader.advance();
        reader.advance();
        result = reader.consumeToAny(',', ';');
        Assert.assertEquals("String", result);
        reader.consume();
        reader.consume();
        Assert.assertEquals("String", reader.consumeToAny(',', ';').trim());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        String result = reader.consumeToAnySorted(';', ',');
        Assert.assertEquals("Test", result);
        reader.advance();
        reader.advance();
        result = reader.consumeToAnySorted(';', ',');
        Assert.assertEquals("String", result);
        reader.consume();
        reader.consume();
        Assert.assertEquals("String", reader.consumeToAnySorted(new char[]{' ', ';'}, ',').trim());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&amp;&lt;&gt;&#55;&#65;"));
        String result = reader.consumeData();
        Assert.assertEquals("&amp;&lt;&gt;&#55;&#65;", result);
        reader.advance();
        reader.advance();
        result = reader.consumeData();
        Assert.assertEquals("", result);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title></title>"));
        String result = reader.consumeTagName();
        Assert.assertEquals("title", result);
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        result = reader.consumeTagName();
        Assert.assertEquals("</title>", result);
        reader.consume();
        reader.consume();
        Assert.assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("Test String", result);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        result = reader.consumeToEnd();
        Assert.assertEquals("", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("TestString"));
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("Test", result);
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        result = reader.consumeLetterSequence();
        Assert.assertEquals("String", result);
        reader.consume();
        reader.consume();
        result = reader.consumeLetterSequence();
        Assert.assertEquals("", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123String456"));
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Test123", result);
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("String456", result);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("", result);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(reader.matches('T'));
        Assert.assertTrue(reader.matches('e'));
        Assert.assertFalse(reader.matches('!'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(reader.matches("es"));
        reader.advance();
        reader.advance();
        Assert.assertTrue(reader.matches("es"));
        reader.advance();
        Assert.assertFalse(reader.matches("es"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(reader.matchesAny('e', 's'));
        Assert.assertTrue(reader.matchesAny('s', 'e'));
        Assert.assertFalse(reader.matchesAny('!', 's'));

        reader.advance();
        reader.advance();
        Assert.assertTrue(reader.matchesAny('s', 'e'));
        Assert.assertTrue(reader.matchesAny('e', 's'));
        Assert.assertFalse(reader.matchesAny('!', 's'));

        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        Assert.assertFalse(reader.matchesAny('!', 's'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'e', 's'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'s', 'e'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'!', 's'}));

        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'!', 's'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123String456"));
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        Assert.assertFalse(reader.matchesDigit());
    }
}
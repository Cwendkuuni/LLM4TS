package org.jsoup.parser;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.io.StringReader;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader(new StringReader("Hello World"));
    }

    @Test
    public void testConstructorWithInputAndSize() {
        Assert.assertEquals("Hello World", reader.toString());
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testConstructorWithInput() {
        CharacterReader reader2 = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(reader, reader2);
        Assert.assertEquals("Hello World", reader2.toString());
        Assert.assertEquals(11, reader2.pos());
        Assert.assertFalse(reader2.isEmpty());
    }

    @Test
    public void testConstructorWithString() {
        CharacterReader readerString = new CharacterReader(new StringReader("Hello World"), "Hello World");
        Assert.assertEquals(reader, readerString);
        Assert.assertEquals("Hello World", readerString.toString());
        Assert.assertEquals(11, readerString.pos());
        Assert.assertFalse(readerString.isEmpty());
    }

    @Test
    public void testBufferUp() {
        reader.bufferUp();
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());

        reader.bufferUp();
        Assert.assertEquals(12, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        Assert.assertEquals(11, reader.pos());

        reader.advance();
        Assert.assertEquals(12, reader.pos());

        reader.rewindToMark();
        Assert.assertEquals(11, reader.pos());

        reader.consumeTo('l');
        Assert.assertEquals(8, reader.pos());
        Assert.assertEquals("Hello", reader.consumeToEnd());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(reader.isEmpty());

        reader.consumeTo('l');
        Assert.assertEquals(8, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals('H', reader.current());
        reader.advance();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        Assert.assertEquals('l', reader.current());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        Assert.assertEquals('l', reader.current());
        reader.consume();
        Assert.assertEquals(' ', reader.current());
        reader.consume();
        Assert.assertEquals('W', reader.current());
        reader.consumeToEnd();
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.unconsume();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        Assert.assertEquals('H', reader.current());
        reader.advance();
        Assert.assertEquals('e', reader.current());
        reader.advance();
        Assert.assertEquals('l', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consumeTo('l');
        reader.rewindToMark();
        Assert.assertEquals(11, reader.pos());
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(5, reader.nextIndexOf('l'));
        Assert.assertEquals(6, reader.nextIndexOf('o'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));

        reader.consumeTo('l');
        Assert.assertEquals(1, reader.nextIndexOf('l'));
        Assert.assertEquals(-1, reader.nextIndexOf('o'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        Assert.assertEquals(6, reader.nextIndexOf("lo"));
        Assert.assertEquals(-1, reader.nextIndexOf("xl"));

        reader.consumeTo('l');
        Assert.assertEquals(1, reader.nextIndexOf("ll"));
        Assert.assertEquals(-1, reader.nextIndexOf("xl"));
    }

    @Test
    public void testConsumeTo() {
        Assert.assertEquals("Hello", reader.consumeTo('l'));
        Assert.assertEquals("World", reader.consumeToEnd());

        reader.consumeTo('l');
        Assert.assertEquals("He", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithCharSequence() {
        Assert.assertEquals("Hello", reader.consumeTo("ll"));
        Assert.assertEquals("World", reader.consumeToEnd());

        reader.consumeTo("ll");
        Assert.assertEquals("He", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("World", reader.consumeToAny(new char[]{'l', 'o'}));
        Assert.assertEquals("Hello World", reader.consumeToAny(new char[]{'H'}));
        Assert.assertEquals("", reader.consumeToAny(new char[]{'x'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        Assert.assertEquals("World", reader.consumeToAnySorted(new char[]{'l', 'o'}));
        Assert.assertEquals("Hello World", reader.consumeToAnySorted(new char[]{'H'}));
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{'x'}));
    }

    @Test
    public void testConsumeData() {
        Assert.assertEquals("Hello &lt;", reader.consumeData());

        reader.consumeTo('&');
        Assert.assertEquals("Hello ", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        Assert.assertEquals("<title", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        Assert.assertEquals("Hello World", reader.consumeToEnd());

        reader.consumeTo('l');
        Assert.assertEquals("He", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        Assert.assertEquals("Hello", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        Assert.assertEquals("Hello123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertFalse(reader.matches('x'));
        Assert.assertFalse(reader.matches("xx"));

        reader.consumeTo('l');
        Assert.assertTrue(reader.matches('l'));
        Assert.assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(reader.matchesIgnoreCase("lo"));
        Assert.assertTrue(reader.matchesIgnoreCase("HELLO"));
        Assert.assertFalse(reader.matchesIgnoreCase("Xx"));

        reader.consumeTo('l');
        Assert.assertTrue(reader.matchesIgnoreCase("ll"));
        Assert.assertFalse(reader.matchesIgnoreCase("Xx"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(reader.matchesAny('l', 'o'));
        Assert.assertTrue(reader.matchesAny('H', 'o'));
        Assert.assertFalse(reader.matchesAny('x', 'x'));

        reader.consumeTo('l');
        Assert.assertTrue(reader.matchesAny('l', 'l'));
        Assert.assertFalse(reader.matchesAny('x', 'x'));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'H'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'x'}));

        reader.consumeTo('l');
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'l'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'x'}));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());

        reader.consumeTo('l');
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());

        reader.consumeTo('l');
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        Assert.assertEquals("ll", reader.matchConsume("ll"));
        Assert.assertEquals("Hel", reader.matchConsume("He"));
        Assert.assertEquals("", reader.matchConsume("xx"));

        reader.consumeTo('l');
        Assert.assertEquals("ll", reader.matchConsume("ll"));
        Assert.assertEquals("", reader.matchConsume("xx"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        Assert.assertEquals("ll", reader.matchConsumeIgnoreCase("ll"));
        Assert.assertEquals("He", reader.matchConsumeIgnoreCase("He"));
        Assert.assertEquals("", reader.matchConsumeIgnoreCase("XX"));

        reader.consumeTo('l');
        Assert.assertEquals("ll", reader.matchConsumeIgnoreCase("ll"));
        Assert.assertEquals("", reader.matchConsumeIgnoreCase("XX"));
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("STYLE"));
        Assert.assertFalse(reader.containsIgnoreCase("XX"));

        reader.consumeTo('l');
        Assert.assertFalse(reader.containsIgnoreCase("title"));
        Assert.assertFalse(reader.containsIgnoreCase("XX"));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCacheString() {
        String str = reader.consumeTo('l').trim();
        Assert.assertEquals("Hello", str);
        Assert.assertEquals("Hello", reader.cacheString(reader.charBuf, reader.stringCache, 0, str.length()));
    }
}
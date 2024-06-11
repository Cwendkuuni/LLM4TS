package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String testData = "This is a test for CharacterReader class.\n" +
                "It should be able to read characters from different sources.\n" +
                "Let's test it with some strings.";
        reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));
    }

    @Test
    public void testCharacterReaderEmpty() {
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        Assert.assertEquals('T', reader.current());
        reader.consume();
        Assert.assertEquals('h', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        Reader input = new InputStreamReader(new ByteArrayInputStream(new byte[0]));
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
    }

    @Test
    public void testCharacterReaderConsume() {
        char c = reader.consume();
        Assert.assertEquals('i', c);
        c = reader.consume();
        Assert.assertEquals('s', c);
        // test empty after consumption
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderAdvance() {
        reader.advance();
        Assert.assertEquals('h', reader.current());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('h', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        Assert.assertEquals(16, reader.nextIndexOf(' '));
        Assert.assertEquals(11, reader.nextIndexOf("is"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        String data = reader.consumeTo('.');
        Assert.assertEquals("a test for CharacterReader class.", data);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        String data = reader.consumeToAny(new char[]{'i', 's'});
        Assert.assertEquals("It should be able to read characters from different sources.", data);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        String data = reader.consumeLetterSequence();
        Assert.assertNotNull(data);
        Assert.assertEquals("h", data.charAt(0));
        Assert.assertEquals("a", data.charAt(1));
        Assert.assertEquals("t", data.charAt(2));
    }

    @Test
    public void testCharacterReaderMatches() {
        Assert.assertTrue(reader.matches('h'));
        Assert.assertTrue(reader.matches("is"));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        Assert.assertTrue(reader.matchesIgnoreCase("is"));
        Assert.assertTrue(reader.matchesIgnoreCase("IS"));
        Assert.assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        Assert.assertTrue(reader.matchesAny('i', 's'));
        Assert.assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        Assert.assertTrue(reader.matchesAnySorted('s', 'i'));
        Assert.assertFalse(reader.matchesAnySorted('z'));
    }

    @Test
    public void testCharacterReaderToString() {
        String data = reader.toString();
        Assert.assertEquals("This is a test for CharacterReader class.\n" +
                "It should be able to read characters from different sources.\n" +
                "Let's test it with some strings.", data);
    }
}
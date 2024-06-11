package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertNotNull(reader);
        Assert.assertEquals("Test String", reader.toString());
    }

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertNotNull(reader);
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderConsumeSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        Assert.assertEquals('T', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('s', reader.consume());
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new IOException("Test Error").getReader());
        reader.consume();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToChar() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTestTest"));
        String result = reader.consumeTo('T');
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTestTest"));
        String result = reader.consumeTo("est");
        Assert.assertEquals("Testest", result);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123Test"));
        String result = reader.consumeToAny(new char[]{'1', '2'});
        Assert.assertEquals("123", result);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123Test"));
        String result = reader.consumeToAnySorted(new char[]{'2', '3', '1'});
        Assert.assertEquals("123", result);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String result = reader.consumeData();
        Assert.assertEquals("&<null", result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag>"));
        String result = reader.consumeTagName();
        Assert.assertEquals("tag", result);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("TestLetterSequence"));
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("TestLetterSequence", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123"));
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Test123", result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test#x11#x2A#x2F"));
        String result = reader.consumeHexSequence();
        Assert.assertEquals("Test#x11#x2A#x2F", result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test12345"));
        String result = reader.consumeDigitSequence();
        Assert.assertEquals("Test12345", result);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        Assert.assertTrue(reader.matches('T'));
        Assert.assertTrue(reader.matches("Te"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        Assert.assertTrue(reader.matchesIgnoreCase("te"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123"));
        Assert.assertTrue(reader.matchesAny(new char[]{'1', '2'}));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'2', '3'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("TestLetter"));
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("Test1234"));
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        reader.matchConsume("Te");
        Assert.assertEquals("est", reader.toString());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        reader.matchConsumeIgnoreCase("te");
        Assert.assertEquals("est", reader.toString());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test Title</title></head><body>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/body"));
    }
}
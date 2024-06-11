package org.jsoup.parser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    private CharacterReader reader;
    private final String input = "Hello <world>! This is a test for CharacterReader.";

    @Before
    public void setUp() {
        reader = new CharacterReader(new StringReader(input));
    }

    @After
    public void tearDown() {
        reader = null;
    }

    @Test
    public void testNewInstance() {
        CharacterReader reader1 = new CharacterReader(new StringReader("test"));
        CharacterReader reader2 = new CharacterReader(new StringReader("test"));
        CharacterReader reader3 = new CharacterReader(new ByteArrayReader(new byte[0]));
        CharacterReader reader4 = new CharacterReader(new ByteArrayInputStream(new byte[0]));

        Assert.assertThat(reader1, is(instanceOf(CharacterReader.class)));
        Assert.assertThat(reader2, is(equalTo(reader1)));
        Assert.assertThat(reader3, is(not(equalTo(reader1))));
        Assert.assertThat(reader4, is(not(equalTo(reader1))));
    }

    @Test
    public void testNewInstanceWithInputSize() {
        CharacterReader reader = new CharacterReader(new StringReader("test"), 10);
        Assert.assertThat(reader.charBuf.length, is(equalTo(10)));
    }

    @Test
    public void testNewInstanceWithInput() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertThat(reader.reader.getClass(), is(equalTo(StringReader.class)));
        Assert.assertThat(reader.charBuf.length, is(equalTo(CharacterReader.maxBufferLen)));
    }

    @Test
    public void testPos() {
        Assert.assertThat(reader.pos(), is(equalTo(0)));
    }

    @Test
    public void testIsEmpty() {
        Assert.assertThat(reader.isEmpty(), is(equalTo(false)));
    }

    @Test
    public void testIsEmptyNoBufferUp() {
        reader.mark();
        reader.rewindToMark();
        Assert.assertThat(reader.isEmptyNoBufferUp(), is(equalTo(true)));
    }

    @Test
    public void testCurrent() {
        Assert.assertThat(reader.current(), is(equalTo((char) 0)));

        reader.advance();
        Assert.assertThat(reader.current(), is(equalTo(input.charAt(0))));
    }

    @Test
    public void testConsume() {
        char c = reader.consume();
        Assert.assertThat(c, is(equalTo(input.charAt(0))));
        Assert.assertThat(reader.current(), is(equalTo(input.charAt(1))));
        Assert.assertThat(reader.pos(), is(equalTo(2)));
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        Assert.assertThat(reader.current(), is(equalTo(input.charAt(0))));
    }

    @Test
    public void testAdvance() {
        reader.advance();
        Assert.assertThat(reader.current(), is(equalTo(input.charAt(1))));
        Assert.assertThat(reader.pos(), is(equalTo(2)));
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.rewindToMark();
        Assert.assertThat(reader.current(), is(equalTo(input.charAt(0))));
        Assert.assertThat(reader.pos(), is(equalTo(0)));
    }

    @Test
    public void testRewindToMark() {
        reader.mark();
        reader.rewindToMark();
        Assert.assertThat(reader.current(), is(equalTo(input.charAt(0))));
        Assert.assertThat(reader.pos(), is(equalTo(0)));
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertThat(reader.nextIndexOf('l'), is(equalTo(1)));
        Assert.assertThat(reader.nextIndexOf('z'), is(equalTo(-1)));
        reader.mark();
        reader.advance();
        Assert.assertThat(reader.nextIndexOf('l'), is(equalTo(2)));
        reader.rewindToMark();
        Assert.assertThat(reader.nextIndexOf('l'), is(equalTo(1)));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        Assert.assertThat(reader.nextIndexOf("ll"), is(equalTo(2)));
        Assert.assertThat(reader.nextIndexOf("world"), is(equalTo(5)));
        reader.mark();
        reader.advance();
        Assert.assertThat(reader.nextIndexOf("ll"), is(equalTo(3)));
        reader.rewindToMark();
        Assert.assertThat(reader.nextIndexOf("ll"), is(equalTo(2)));
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpThrowsIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"), 1) {
            @Override
            void bufferUp() throws IOException {
                throw new IOException("Test Exception");
            }
        };
        reader.consume();
    }

    @Test
    public void testConsumeTo() {
        String expected = "Hello ";
        String actual = reader.consumeTo(' ');
        Assert.assertThat(actual, is(equalTo(expected)));
        Assert.assertThat(reader.current(), is(equalTo(' ')));
        Assert.assertThat(reader.pos(), is(equalTo(3)));
    }

    @Test
    public void testConsumeToAny() {
        String expected = "test";;
        String actual = reader.consumeToAny('t', 'e', 's');
        Assert.assertThat(actual, is(equalTo(expected)));
        Assert.assertThat(reader.pos(), is(equalTo(5)));
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = "test";
        String actual = reader.consumeToAnySorted('s', 'e', 't');
        Assert.assertThat(actual, is(equalTo(expected)));
        Assert.assertThat(reader.pos(), is(equalTo(5)));
    }

    @Test
    public void testConsumeData() {
        String expected = "Hello <world>! This is a test for CharacterReader.";
        String actual = reader.consumeData();
        Assert.assertThat(actual, is(equalTo(expected)));
        Assert.assertThat(reader.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void testConsumeTagName() {
        String expected = "<world>";
        String actual = reader.consumeTagName();
        Assert.assertThat(actual, is(equalTo(expected)));
        Assert.assertThat(reader.isEmpty(), is(equalTo(false)));
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "This is a test for CharacterReader.";
        String actual = reader.consumeToEnd();
        Assert.assertThat(actual, is(equalTo(expected)));
        Assert.assertThat(reader.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "Hello";
        String actual = reader.consumeLetterSequence();
        Assert.assertThat(actual, is(equalTo(expected)));
        Assert.assertThat(reader.isEmpty(), is(equalTo(false)));
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "Hello123";
        String actual = reader.consumeLetterThenDigitSequence();
        Assert.assertThat(actual, is(equalTo(expected)));
        Assert.assertThat(reader.isEmpty(), is(equalTo(false)));
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "test#abcdef";
        String actual = reader.consumeHexSequence();
        Assert.assertThat(actual, is(equalTo(expected)));
        Assert.assertThat(reader.isEmpty(), is(equalTo(false)));
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "test12345";
        String actual = reader.consumeDigitSequence();
        Assert.assertThat(actual, is(equalTo(expected)));
        Assert.assertThat(reader.isEmpty(), is(equalTo(false)));
    }

    @Test
    public void testMatches() {
        Assert.assertThat(reader.matches('l'), is(equalTo(true)));
        Assert.assertThat(reader.matches('z'), is(equalTo(false)));
        reader.mark();
        reader.advance();
        Assert.assertThat(reader.matches('l'), is(equalTo(true)));
        reader.rewindToMark();
        Assert.assertThat(reader.matches('l'), is(equalTo(true)));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertThat(reader.matchesIgnoreCase("world"), is(equalTo(true)));
        Assert.assertThat(reader.matchesIgnoreCase("test"), is(equalTo(false)));
        reader.mark();
        reader.advance();
        Assert.assertThat(reader.matchesIgnoreCase("world"), is(equalTo(true)));
        reader.rewindToMark();
        Assert.assertThat(reader.matchesIgnoreCase("world"), is(equalTo(true)));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertThat(reader.matchesAny('l'), is(equalTo(true)));
        Assert.assertThat(reader.matchesAny('z'), is(equalTo(false)));
        Assert.assertThat(reader.matchesAny(' '), is(equalTo(true)));
        Assert.assertThat(reader.matchesAny('>'), is(equalTo(true)));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertThat(reader.matchesAnySorted('>'), is(equalTo(true)));
        Assert.assertThat(reader.matchesAnySorted(' '), is(equalTo(true)));
        Assert.assertThat(reader.matchesAnySorted('l'), is(equalTo(true)));
        Assert.assertThat(reader.matchesAnySorted('z'), is(equalTo(false)));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertThat(reader.matchesLetter(), is(equalTo(true)));
        reader.mark();
        reader.advance();
        Assert.assertThat(reader.matchesLetter(), is(equalTo(true)));
        reader.rewindToMark();
        Assert.assertThat(reader.matchesLetter(), is(equalTo(true)));
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertThat(reader.matchesDigit(), is(equalTo(false)));
        reader.mark();
        reader.advance();
        Assert.assertThat(reader.matchesDigit(), is(equalTo(true)));
        reader.rewindToMark();
        Assert.assertThat(reader.matchesDigit(), is(equalTo(false)));
    }
}
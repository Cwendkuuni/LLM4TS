package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    static final String TEST_STRING = "This is a test string with <html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>";
    static final char EOF = (char) -1;

    CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader(new StringReader(TEST_STRING));
    }

    @After
    public void tearDown() {
        reader = null;
    }

    @Test
    public void testConstructor() {
        reader = new CharacterReader(new StringReader(TEST_STRING));
        Validate.notNull(reader);
        Validate.isTrue(reader.reader.markSupported());
        assertEquals(TEST_STRING.length(), reader.charBuf.length);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgument() {
        reader = new CharacterReader(null, maxBufferLen);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeBufferSize() {
        reader = new CharacterReader(new StringReader("test"), -1);
    }

    @Test
    public void testCurrent() {
        assertEquals(TEST_STRING.charAt(0), reader.current());
        reader.consume();
        assertEquals(TEST_STRING.charAt(1), reader.current());
        reader.advance();
        assertEquals(TEST_STRING.charAt(2), reader.current());
        reader.unconsume();
        assertEquals(TEST_STRING.charAt(2), reader.current());
        reader.consume();
        assertEquals(TEST_STRING.charAt(3), reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals(TEST_STRING.charAt(0), reader.current());
        reader.consume();
        assertEquals(TEST_STRING.charAt(1), reader.current());
        reader.consume();
        assertEquals(TEST_STRING.charAt(2), reader.current());
    }

    @Test
    public void testConsumeTo() {
        String expected = TEST_STRING.substring(0, 7);
        String actual = reader.consumeTo(' ');
        assertEquals(expected, actual);
        assertEquals(TEST_STRING.charAt(7), reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        String actual = reader.consumeToEnd();
        assertEquals(TEST_STRING, actual);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        String expected = "<html>";
        String actual = reader.consumeToAny(Arrays.asList('<', 'h', 't', 'm', 'l').toArray(new char[0]));
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = "<html>";
        String actual = reader.consumeToAnySorted(new char[]{"<", "h", "t", "m", "l"});
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeData() {
        String expected = "This is a test string with ";
        String actual = reader.consumeData();
        assertEquals(expected, actual);
        assertEquals(TEST_STRING.charAt(expected.length()), reader.current());
    }

    @Test
    public void testConsumeTagName() {
        String expected = "html";
        String actual = reader.consumeTagName();
        assertEquals(expected, actual);
        assertEquals(TEST_STRING.charAt(expected.length()), reader.current());
    }

    @Test
    public void testIsEmpty() {
        assertEquals(false, reader.isEmpty());
        reader.consumeToEnd();
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertTrue(reader.matches("est"));
        assertFalse(reader.matches('<'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("is"));
        assertTrue(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesIgnoreCase("<"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T'));
        assertTrue(reader.matchesAny("est"));
        assertFalse(reader.matchesAny('<'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(new char[]{'T', 'e', 's', 't'}));
        assertFalse(reader.matchesAnySorted(new char[]{'<'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.consume();
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String expected = "html";
        reader.consumeTagName();
        reader.matchConsume(expected);
        assertEquals(TEST_STRING.charAt(expected.length()), reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String expected = "html";
        reader.consumeTagName();
        reader.matchConsumeIgnoreCase(expected);
        assertEquals(TEST_STRING.charAt(expected.length()), reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/style"));
        assertFalse(reader.containsIgnoreCase("/script"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        characterReader.reader.close();
        characterReader.consume();
    }

    @Test
    public void testBufferUp() throws IOException {
        ByteArrayReader reader = new ByteArrayReader(TEST_STRING.getBytes());
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.bufferUp();
        characterReader.consume();
        assertEquals(TEST_STRING.charAt(3), reader.read());
        assertEquals(TEST_STRING.charAt(4), reader.read());
        assertEquals(TEST_STRING.charAt(5), reader.read());
        assertEquals(TEST_STRING.charAt(6), reader.read());
        assertEquals(CharacterReader.EOF, reader.read());
        assertEquals(TEST_STRING.charAt(3), characterReader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consumeTo(' ');
        reader.rewindToMark();
        assertEquals(TEST_STRING.charAt(0), reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testMarkInvalid() throws IOException {
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        reader.consume();
    }
}
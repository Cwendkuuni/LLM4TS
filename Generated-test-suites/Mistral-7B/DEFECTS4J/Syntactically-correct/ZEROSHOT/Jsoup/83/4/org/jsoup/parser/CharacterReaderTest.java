package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.parser.CharacterReader.EOF;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    private CharacterReader characterReader;
    private static final String TEST_STRING = "Hello World! <test>123</test>";

    @Before
    public void setUp() {
        characterReader = new CharacterReader(new StringReader(TEST_STRING));
    }

    @After
    public void tearDown() throws IOException {
        characterReader.close();
    }

    @Test
    public void testInitialization() {
        assertThat(characterReader.reader, is(instanceOf(StringReader.class)));
        assertThat(characterReader.charBuf, is(notNullValue()));
        assertThat(characterReader.bufLength, is(CharacterReader.maxBufferLen));
        assertThat(characterReader.bufPos, is(0));
        assertThat(characterReader.readerPos, is(0));
        assertThat(characterReader.bufMark, is(0));
        assertThat(characterReader.bufSplitPoint, is(CharacterReader.readAheadLimit));
    }

    @Test
    public void testPos() {
        assertThat(characterReader.pos(), is(0));
        characterReader.advance();
        characterReader.consume();
        assertThat(characterReader.pos(), is(2));
    }

    @Test
    public void testIsEmpty() {
        assertThat(characterReader.isEmpty(), is(true));
        characterReader.bufferUp();
        characterReader.consume();
        assertThat(characterReader.isEmpty(), is(false));
    }

    @Test
    public void testCurrent() {
        assertThat(characterReader.current(), is(EOF));
        characterReader.bufferUp();
        characterReader.consume();
        assertThat(characterReader.current(), is('H'));
    }

    @Test
    public void testConsume() {
        characterReader.bufferUp();
        characterReader.consume();
        assertThat(characterReader.current(), is('H'));
        characterReader.unconsume();
        characterReader.consume();
        assertThat(characterReader.current(), is('H'));
        characterReader.advance();
        characterReader.consume();
        assertThat(characterReader.current(), is('e'));
    }

    @Test
    public void testAdvance() {
        characterReader.bufferUp();
        characterReader.advance();
        characterReader.consume();
        assertThat(characterReader.current(), is('e'));
        characterReader.advance();
        characterReader.consume();
        assertThat(characterReader.current(), is('l'));
    }

    @Test
    public void testMark() {
        characterReader.bufferUp();
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        characterReader.consume();
        assertThat(characterReader.current(), is('H'));
    }

    @Test
    public void testNextIndexOf() {
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();

        int index = characterReader.nextIndexOf('l');
        assertThat(index, is(8));

        index = characterReader.nextIndexOf('z');
        assertThat(index, is(-1));
    }

    @Test
    public void testNextIndexOfWithSeq() {
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();

        int index = characterReader.nextIndexOf("elo");
        assertThat(index, is(6));

        index = characterReader.nextIndexOf("zoo");
        assertThat(index, is(-1));
    }

    @Test
    public void testConsumeTo() {
        String expected = "llo";
        characterReader.bufferUp();
        characterReader.consumeTo('l');
        String result = characterReader.toString();
        assertThat(result, is(expected));

        expected = "World! <test>123</test>";
        characterReader.consumeTo(' ');
        result = characterReader.toString();
        assertThat(result, is(expected.substring(0, 11)));
    }

    @Test
    public void testConsumeToAny() {
        String expected = "ld! <test>123</test>";
        characterReader.bufferUp();
        characterReader.consumeToAny(Character.forDigit(0, 10));
        String result = characterReader.toString();
        assertThat(result, is(expected));
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = "Hello World! <test>123</test>";
        characterReader.bufferUp();
        characterReader.consumeToAnySorted(Character.forDigit(0, 10), '>');
        String result = characterReader.toString();
        assertThat(result, is(expected.substring(0, 21)));
    }

    @Test
    public void testConsumeData() {
        String expected = "&amp;<test>123</test>";
        characterReader.bufferUp();
        characterReader.consumeData();
        String result = characterReader.toString();
        assertThat(result, is(expected));
    }

    @Test
    public void testConsumeTagName() {
        String expected = "<test>";
        characterReader.bufferUp();
        characterReader.consumeTagName();
        String result = characterReader.toString();
        assertThat(result, is(expected));
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "Hello World! <test>123</test>";
        characterReader.consumeToEnd();
        String result = characterReader.toString();
        assertThat(result, is(expected));
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "Hello";
        characterReader.bufferUp();
        characterReader.consumeLetterSequence();
        String result = characterReader.toString();
        assertThat(result, is(expected));
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "He1lo12World! 1<test>123</test>";
        characterReader.bufferUp();
        characterReader.consumeLetterThenDigitSequence();
        String result = characterReader.toString();
        assertThat(result, is(expected));
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "He&amp;lt;test&gt;123";
        characterReader.bufferUp();
        characterReader.consumeHexSequence();
        String result = characterReader.toString();
        assertThat(result, is(expected));
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "He0001lo00012World! 1<test>123000</test>";
        characterReader.bufferUp();
        characterReader.consumeDigitSequence();
        String result = characterReader.toString();
        assertThat(result, is(expected));
    }

    @Test
    public void testMatches() {
        characterReader.bufferUp();
        characterReader.consume();
        boolean result = characterReader.matches('l');
        assertThat(result, is(true));

        result = characterReader.matches('z');
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesSequence() {
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();

        boolean result = characterReader.matches("elo");
        assertThat(result, is(true));

        result = characterReader.matches("zoo");
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesIgnoreCase() {
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();

        boolean result = characterReader.matchesIgnoreCase("elo");
        assertThat(result, is(true));

        result = characterReader.matchesIgnoreCase("zoo");
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesAny() {
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();

        boolean result = characterReader.matchesAny('l', 'w');
        assertThat(result, is(true));

        result = characterReader.matchesAny('z', 'w');
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesAnySorted() {
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();

        boolean result = characterReader.matchesAnySorted('w', 'l');
        assertThat(result, is(true));

        result = characterReader.matchesAnySorted('z', 'w');
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesLetter() {
        characterReader.bufferUp();
        boolean result = characterReader.matchesLetter();
        assertThat(result, is(true));

        characterReader.advance();
        result = characterReader.matchesLetter();
        assertThat(result, is(true));

        characterReader.consume();
        result = characterReader.matchesLetter();
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesDigit() {
        characterReader.bufferUp();
        boolean result = characterReader.matchesDigit();
        assertThat(result, is(false));

        characterReader.consume();
        result = characterReader.matchesDigit();
        assertThat(result, is(true));
    }

    @Test
    public void testMatchConsumeSequence() {
        String expected = "llo";
        characterReader.bufferUp();
        characterReader.matchConsume("llo");
        String result = characterReader.toString();
        assertThat(result, is(expected));
    }

    @Test
    public void testMatchConsumeSequenceIgnoreCase() {
        String expected = "Hello";
        characterReader.bufferUp();
        characterReader.matchConsumeIgnoreCase("hello");
        String result = characterReader.toString();
        assertThat(result, is(expected));
    }

    @Test
    public void testContainsIgnoreCase() {
        characterReader.bufferUp();
        boolean result = characterReader.containsIgnoreCase("/test");
        assertThat(result, is(true));

        characterReader.consume();
        result = characterReader.containsIgnoreCase("/test");
        assertThat(result, is(false));
    }

}
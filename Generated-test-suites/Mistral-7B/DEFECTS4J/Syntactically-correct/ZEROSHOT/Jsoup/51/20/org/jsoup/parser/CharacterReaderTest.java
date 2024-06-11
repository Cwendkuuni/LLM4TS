package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(1, reader.length());
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals(3, reader.length());
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('C', reader.current());
        assertEquals('C', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("ABC");
        reader.mark();
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        reader.consume();
        reader.rewindToMark();
        assertEquals('A', reader.current());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals("", reader.consumeAsString());
        assertEquals("A", reader.consumeAsString());
        assertEquals("AB", reader.consumeTo(CharacterReader.EOF));
        assertEquals("BC", reader.consumeTo(CharacterReader.EOF));
        assertEquals("ABC", reader.consumeToEnd());
        assertEquals("A", reader.consumeLetterSequence().trim());
        assertEquals("AB", reader.consumeLetterThenDigitSequence().trim());
        assertEquals("123", reader.consumeDigitSequence().trim());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("ABCDEFabcdef");
        assertEquals(1, reader.nextIndexOf('B'));
        assertEquals(-1, reader.nextIndexOf('X'));
        assertEquals(11, reader.nextIndexOf("def"));
        assertEquals(1, reader.nextIndexOf("de"));
        assertEquals(1, reader.nextIndexOf("ef"));
        assertEquals(11, reader.nextIndexOf("def", 4));
        assertEquals(13, reader.nextIndexOf("ef", 8));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("ABCDEFabcdef");
        assertEquals("B", reader.consumeTo('B').trim());
        assertEquals("BC", reader.consumeTo('C').trim());
        assertEquals("DEF", reader.consumeTo("DEF").trim());
        assertEquals("abcdef", reader.consumeToEnd().trim());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("ABC123!@#$%&*()_+-=[]{}|;':\",.<>/?");
        assertEquals("1", reader.consumeToAny(Character.forDigit(1, 10)).trim());
        assertEquals("23", reader.consumeToAny(Character.forDigit(2, 10), Character.forDigit(3, 10)).trim());
        assertEquals("!@#$%&", reader.consumeToAnySorted(Character.forDigit(1, 10), Character.forDigit(2, 10), Character.forDigit(3, 10), '&', '*', '(', ')' ).trim());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader("&lt;tag&gt; some text &amp; &lt;attribute=&quot;value&quot;&gt;");
        assertEquals("<tag>", reader.consumeData().trim());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("<tag attr=\"value\"> text </tag>");
        assertEquals("tag", reader.consumeTagName().trim());
    }
}
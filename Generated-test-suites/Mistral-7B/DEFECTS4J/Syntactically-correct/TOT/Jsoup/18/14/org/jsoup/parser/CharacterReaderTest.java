package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testEmptyCharacterReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testCharacterReaderWithSingleCharacter() {
 CharacterReader reader = new CharacterReader("A");
 assertEquals('A', reader.consume());
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderWithMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals('H', reader.consume());
 assertEquals('e', reader.consume());
 assertEquals('l', reader.consume());
 assertEquals('l', reader.consume());
 assertEquals('o', reader.consume());
 assertEquals(' ', reader.consume());
 assertEquals('W', reader.consume());
 assertEquals('o', reader.consume());
 assertEquals('r', reader.consume());
 assertEquals('l', reader.consume());
 assertEquals('d', reader.consume());
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('H', reader.current());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("123abcdefGhiJklMnopQstUVwxyz");
 String sequence = reader.consumeLetterSequence();
 assertEquals("abcdef", sequence);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("1234abcdEf56Ghij5KlMnOpQrStUvWxYz");
 String sequence = reader.consumeHexSequence();
 assertEquals("234abcdEf", sequence);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String sequence = reader.consumeDigitSequence();
 assertEquals("0123456789", sequence);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMarkBeforeStart() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 reader.mark();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndEmpty() {
 CharacterReader reader = new CharacterReader("");
 reader.consumeToEnd();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndPastEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consumeToEnd();
 reader.consume();
 reader.consumeToEnd();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentConsumeToEnd() {
 CharacterReader reader = new CharacterReader("");
 reader.consumeTo('a');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentConsumeToEndNull() {
 CharacterReader reader = new CharacterReader("");
 reader.consumeTo(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentConsumeToAnyNull() {
 CharacterReader reader = new CharacterReader("");
 reader.consumeToAny((char) 0);
 }

 @Test
 public void testConsumeToAnyMultipleMatches() {
 CharacterReader reader = new CharacterReader("1222abbaa333");
 String sequence = reader.consumeToAny('2', 'a');
 assertEquals("222a", sequence);
 }

 @Test
 public void testConsumeToAnySingleMatch() {
 CharacterReader reader = new CharacterReader("1222abbaa333");
 String sequence = reader.consumeToAny('3');
 assertEquals("333", sequence);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("1234abcdEf56Ghij5KlMnOpQrStUvWxYz");
 assertTrue(reader.matches('e'));
 assertFalse(reader.matches('x'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("1234abcdEf56Ghij5KlMnOpQrStUvWxYz");
 assertTrue(reader.matchesIgnoreCase("e"));
 assertFalse(reader.matchesIgnoreCase("x"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("1234abcdEf56Ghij5KlMnOpQrStUvWxYz");
 assertTrue(reader.matchesAny('e', 'a'));
 assertFalse(reader.matchesAny('x', 'a'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("1234abcdEf56Ghij5KlMnOpQrStUvWxYz");
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234abcdEf56Ghij5KlMnOpQrStUvWxYz");
 assertFalse(reader.matchesLetter());
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("1234abcdEf56Ghij5KlMnOpQrStUvWxYz");
 String sequence = reader.matchConsume("ef");
 assertEquals("ef", sequence);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("1234abcdEf56Ghij5KlMnOpQrStUvWxYz");
 String sequence = reader.matchConsumeIgnoreCase("EF");
 assertEquals("EF", sequence);
 }
}
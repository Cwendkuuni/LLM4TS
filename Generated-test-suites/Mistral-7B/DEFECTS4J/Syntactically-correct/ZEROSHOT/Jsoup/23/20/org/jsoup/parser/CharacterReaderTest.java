package org.jsoup.parser.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private String input;
 private CharacterReader reader;

 @Before
 public void setUp() {
 input = "This is a test for CharacterReader class.";
 reader = new CharacterReader(input);
 }

 @Test
 public void testCharacterReaderInstance() {
 CharacterReader reader = new CharacterReader("Test String");
 assertNotNull(reader);
 }

 @Test
 public void testCharacterReaderPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testCharacterReaderIsEmpty() {
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderCurrent() {
 assertEquals('T', reader.current());
 }

 @Test
 public void testCharacterReaderConsume() {
 assertEquals('T', reader.consume());
 assertEquals('h', reader.current());
 }

 @Test
 public void testCharacterReaderUnconsume() {
 reader.unconsume();
 assertEquals('T', reader.current());
 }

 @Test
 public void testCharacterReaderAdvance() {
 reader.advance();
 assertEquals('h', reader.current());
 }

 @Test
 public void testCharacterReaderMark() {
 reader.mark();
 }

 @Test(expected = IllegalStateException.class)
 public void testCharacterReaderRewindToMarkBeforeMark() {
 reader.rewindToMark();
 }

 @Test
 public void testCharacterReaderConsumeAsString() {
 assertEquals("is a test for CharacterReader class.", reader.consumeAsString());
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 assertEquals("a test for CharacterReader class.", reader.consumeTo(' '));
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 assertEquals("for CharacterReader class.", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 assertEquals("Test String", reader.consumeToAny('S', 'r', 't', 'i', 'n', 'g'));
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 assertEquals("Test", reader.consumeLetterSequence());
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 assertEquals("for", reader.consumeHexSequence());
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 assertEquals("4", reader.consumeDigitSequence());
 }

 @Test
 public void testCharacterReaderMatches() {
 assertTrue(reader.matches('h'));
 }

 @Test
 public void testCharacterReaderMatchesString() {
 assertTrue(reader.matches("CharacterReader"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("characterreader"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 assertTrue(reader.matchesAny('r', 'a'));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testCharacterReaderMatchConsume() {
 assertEquals("CharacterReader", reader.matchConsume("CharacterReader"));
 }

 @Test
 public void testCharacterReaderMatchConsumeIgnoreCase() {
 assertEquals("characterreader", reader.matchConsumeIgnoreCase("CharacterReader"));
 }

 @Test
 public void testCharacterReaderContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("test"));
 }

 @Test
 public void testCharacterReaderToString() {
 assertEquals("for CharacterReader class.", reader.toString());
 }

 @After
 public void tearDown() {
 reader = null;
 input = null;
 }
}
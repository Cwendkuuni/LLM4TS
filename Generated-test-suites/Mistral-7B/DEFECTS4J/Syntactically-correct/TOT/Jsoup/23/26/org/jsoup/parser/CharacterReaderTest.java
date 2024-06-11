package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

 // Test constructor
 @Test
 public void testCharacterReaderConstructor() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("This is a test string", reader.input);
 Assert.assertEquals(14, reader.length);
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 reader = new CharacterReader("This is a test string");
 Assert.assertFalse(reader.isEmpty());
 }

 // Test current method with valid input
 @Test
 public void testCurrentWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals('T', reader.current());
 reader.advance();
 Assert.assertEquals('h', reader.current());
 }

 // Test current method with empty input
 @Test(expected = IllegalStateException.class)
 public void testCurrentWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 reader.current();
 }

 // Test consume method with valid input
 @Test
 public void testConsumeWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals('T', reader.consume());
 Assert.assertEquals('h', reader.current());
 }

 // Test consume method with empty input
 @Test(expected = IllegalStateException.class)
 public void testConsumeWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 reader.consume();
 }

 // Test unconsume method with valid input
 @Test
 public void testUnconsumeWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('T', reader.current());
 }

 // Test unconsume method with empty input
 @Test(expected = IllegalStateException.class)
 public void testUnconsumeWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 reader.unconsume();
 }

 // Test advance method with valid input
 @Test
 public void testAdvanceWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 reader.advance();
 Assert.assertEquals('h', reader.current());
 }

 // Test advance method with empty input
 @Test(expected = IllegalStateException.class)
 public void testAdvanceWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 reader.advance();
 }

 // Test mark method with valid input
 @Test
 public void testMarkWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 reader.mark();
 }

 // Test rewindToMark method with valid input
 @Test
 public void testRewindToMarkWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('T', reader.current());
 }

 // Test rewindToMark method with empty input
 @Test(expected = IllegalStateException.class)
 public void testRewindToMarkWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 reader.rewindToMark();
 }

 // Test consumeAsString method with valid input
 @Test
 public void testConsumeAsStringWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 String consumed = reader.consumeAsString();
 Assert.assertEquals("is a test", consumed);
 }

 // Test consumeTo method with valid input
 @Test
 public void testConsumeToWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 String consumed = reader.consumeTo(' ');
 Assert.assertEquals("is a te", consumed);
 }

 // Test consumeTo method with empty input
 @Test
 public void testConsumeToWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 String consumed = reader.consumeTo(' ');
 Assert.assertEquals("", consumed);
 }

 // Test consumeToEnd method
 @Test
 public void testConsumeToEnd() { // Test consumeToEnd method with valid input
 CharacterReader reader = new CharacterReader("This is a test string");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("string", consumed);
 }

 // Test consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("This is a test string");
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("is a test", consumed);
 }

 // Test consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x12345678");
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("1234", consumed);
 }

 // Test consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("12345678");
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("12345678", consumed);
 }

 // Test matches method with valid input
 @Test
 public void testMatchesWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matches('t'));
 Assert.assertTrue(reader.matches("est string"));
 }

 // Test matches method with invalid input
 @Test
 public void testMatchesWithInvalidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertFalse(reader.matches('x'));
 Assert.assertFalse(reader.matches("test string"));
 }

 // Test matchesIgnoreCase method with valid input
 @Test
 public void testMatchesIgnoreCaseWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchesIgnoreCase("is"));
 Assert.assertTrue(reader.matchesIgnoreCase("Test String"));
 }

 // Test matchesIgnoreCase method with invalid input
 @Test
 public void testMatchesIgnoreCaseWithInvalidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertFalse(reader.matchesIgnoreCase("Is"));
 Assert.assertFalse(reader.matchesIgnoreCase("test string"));
 }

 // Test matchesAny method with valid input
 @Test
 public void testMatchesAnyWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchesAny('t', 's'));
 Assert.assertTrue(reader.matchesAny('t', 'T'));
 }

 // Test matchesAny method with invalid input
 @Test
 public void testMatchesAnyWithInvalidInput() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertFalse(reader.matchesAny('x', 's'));
 Assert.assertFalse(reader.matchesAny('t', 'X'));
 }

 // Test containsIgnoreCase method with valid input
 @Test
 public void testContainsIgnoreCaseWithValidInput() {
 CharacterReader reader = new CharacterReader("This is a <title>test</title> string");
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertTrue(reader.containsIgnoreCase("Test"));
 }

 // Test containsIgnoreCase method with invalid input
 @Test
 public void testContainsIgnoreCaseWithInvalidInput() {
 CharacterReader reader = new CharacterReader("This is a <title>test</title> string");
 Assert.assertFalse(reader.containsIgnoreCase("Title"));
 Assert.assertFalse(reader.containsIgnoreCase("test string"));
 }

 // Test toString method
 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("This is a test string");
 String expected = "test string";
 Assert.assertEquals(expected, reader.toString());
 }
}
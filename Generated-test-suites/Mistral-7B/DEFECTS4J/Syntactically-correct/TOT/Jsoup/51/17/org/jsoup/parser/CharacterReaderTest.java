package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest class to test the functionality of the CharacterReader class.
 */
public class CharacterReaderTest {

 @Test
 public void testEmptyCharacterReader() {
 CharacterReader reader = new CharacterReader("");
 // Test basic methods
 Assert.assertEquals(0, reader.pos());
 Assert.assertTrue(reader.isEmpty());
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 // Test consumption and rewinding
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testBasicCharacterReader() {
 CharacterReader reader = new CharacterReader("Hello World!");
 // Test basic methods
 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('H', reader.current());
 // Test consumption
 Assert.assertEquals('H', reader.consume());
 Assert.assertEquals('e', reader.current());
 // Test unconsuming and marking
 reader.unconsume();
 reader.mark();
 Assert.assertEquals('H', reader.current());
 reader.rewindToMark();
 Assert.assertEquals('H', reader.current());
 // Test advance, rewind and consumeToEnd
 reader.advance();
 Assert.assertEquals('e', reader.current());
 reader.rewindToMark();
 reader.consumeToEnd();
 Assert.assertEquals(12, reader.pos());
 }

 @Test
 public void testSpecialCharactersCharacterReader() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head></html>");
 // Test consumeTo and consumeToAny with special characters
 Assert.assertEquals("<html><head><title>Test", reader.consumeTo("<html>"));
 Assert.assertTrue(reader.matches("/"));
 Assert.assertTrue(reader.matchesAny(new char[]{'<', '/'}));
 Assert.assertEquals("Test</title></head></html>", reader.consumeToEnd());
 }

 @Test(expected = NullPointerException.class)
 public void testNullInputCharacterReader() {
 CharacterReader reader = new CharacterReader(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEndCharacterReader() {
 CharacterReader reader = new CharacterReader("Hello World!");
 reader.consumeToEnd();
 reader.consume(); // consume last character
 reader.consume(); // consume past end
 }
}
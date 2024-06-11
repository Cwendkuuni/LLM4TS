package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Test
 public void testCharacterReaderConstruction() {
 char[] expectedInput = {'t', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(expectedInput));
 Assert.assertEquals(expectedInput.length, reader.length());
 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderIsEmpty() {
 char[] expectedInput = {};
 reader = new CharacterReader(new String(expectedInput));
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderCurrent() {
 char[] expectedInput = {'t', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(expectedInput));
 Assert.assertEquals('t', reader.current());
 }

 @Test
 public void testCharacterReaderConsume() {
 char[] expectedInput = {'t', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(expectedInput));
 Assert.assertEquals('t', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testCharacterReaderAdvance() {
 char[] expectedInput = {'t', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(expectedInput));
 reader.advance();
 reader.advance();
 reader.advance();
 Assert.assertEquals('s', reader.current());
 }

 @Test
 public void testCharacterReaderMark() {
 char[] expectedInput = {'t', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(expectedInput));
 reader.mark();
 reader.consume();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('t', reader.current());
 }

 @Test
 public void testCharacterReaderConsumeAsString() {
 char[] expectedInput = {'t', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(expectedInput));
 String consumed = reader.consumeAsString();
 Assert.assertEquals("st\n", consumed);
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 char[] expectedInput = {'t', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(expectedInput));
 String consumed = reader.consumeTo('s');
 Assert.assertEquals("es\n", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToString() {
 char[] expectedInput = {'t', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(expectedInput));
 String consumed = reader.consumeTo("st");
 Assert.assertEquals("es\n", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 char[] expectedInput = {'t', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(expectedInput));
 String consumed = reader.consumeToAny('s', 't');
 Assert.assertEquals("es\n", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 char[] expectedInput = {'t', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(expectedInput));
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("tes\n", consumed);
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 char[] expectedInput = {"tes\n", "test\n"};
 for (char[] input : expectedInput) {
 reader = new CharacterReader(new String(input));
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("tes", consumed);
 }
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 char[] expectedInput = {"tes\n", "test\n"};
 for (char[] input : expectedInput) {
 reader = new CharacterReader(new String(input));
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("tes", consumed);
 }
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 char[] expectedInput = {"tes\n", "test\n"};
 for (char[] input : expectedInput) {
 reader = new CharacterReader(new String(input));
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("tes", consumed);
 }
 }

 @Test
 public void testCharacterReaderMatches() {
 char[] expectedInput1 = {"tes", "st"};
 char[] expectedInput2 = {"test", "st"};
 reader = new CharacterReader(new String(expectedInput1[0]));
 Assert.assertTrue(reader.matches('t'));
 Assert.assertFalse(reader.matches('s'));
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertTrue(reader.matches('s'));
 reader = new CharacterReader(new String(expectedInput2[0]));
 Assert.assertFalse(reader.matches('s'));
 Assert.assertTrue(reader.matches('t'));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 char[] expectedInput = {"Test", "test"};
 reader = new CharacterReader(new String(expectedInput[0]));
 Assert.assertTrue(reader.matchesIgnoreCase("test"));
 reader = new CharacterReader(new String(expectedInput[1]));
 Assert.assertTrue(reader.matchesIgnoreCase("Test"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 char[] expectedInput = {"test", "test"};
 reader = new CharacterReader(new String(expectedInput[0]));
 Assert.assertFalse(reader.matchesAny('x'));
 reader.consume();
 Assert.assertTrue(reader.matchesAny('t'));
 reader = new CharacterReader(new String(expectedInput[1]));
 Assert.assertTrue(reader.matchesAny('t'));
 Assert.assertTrue(reader.matchesAny('e'));
 }

 @Test
 public void testCharacterReaderContainsIgnoreCase() {
 char[] expectedInput = {"<title>Test</title>", "<title>test</title>"};
 reader = new CharacterReader(new String(expectedInput[0]));
 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 reader = new CharacterReader(new String(expectedInput[1]));
 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 }

 @Test
 public void testCharacterReaderToString() {
 char[] expectedInput = {"tes\n", "test\n"};
 for (char[] input : expectedInput) {
 reader = new CharacterReader(new String(input));
 Assert.assertEquals("tes\n", reader.toString());
 }
 }
}
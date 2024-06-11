package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private static final String TEST_INPUT = "This is a test string with <html><head></head><body><h1>Test Title</h1></body></html>";
 private String input;
 private CharacterReader reader;

 @Before
 public void setup() {
 input = TEST_INPUT;
 reader = new CharacterReader(input);
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('T', reader.current());
 reader.consume();
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testConsume() {
 char[] expected = new char[]{'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 's', 't', 'r', 'i', 'n', 'g', '<', 'h', '1', '>', ' ', 'T', 'e', 's', 't', ' ', 'T', 'i', 't', 'l', 'e', '</h1>', '</h', '1', '>', '</b', 'o', 'd', 'y>', '</html>'};
 char[] actual = new char[input.length()];
 int pos = 0;
 while (!reader.isEmpty()) {
 actual[pos++] = reader.consume();
 }
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('i', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 Assert.assertEquals(reader.pos(), reader.mark());
 }

 @Test
 public void testRewindToMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('T', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "<html><head></head><body>";
 String actual = reader.consumeAsString();
 Assert.assertEquals(expected, actual);
 reader.consume(); // consume the space
 Assert.assertEquals("body", reader.consumeTo("body"));
 }

 @Test
 public void testConsumeTo() {
 String expected = "Test Title";
 String actual = reader.consumeTo("</h1>");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "</html>";
 String actual = reader.consumeToEnd();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "TestTitle";
 String actual = reader.consumeLetterSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "html";
 String actual = reader.consumeHexSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "1";
 String actual = reader.consumeDigitSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('T'));
 Assert.assertFalse(reader.matches(' '));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("th"));
 Assert.assertFalse(reader.matches("is"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("h1"));
 Assert.assertFalse(reader.matchesIgnoreCase("body"));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = new char[]{'h', '1'};
 Assert.assertTrue(reader.matchesAny(seq));
 Assert.assertFalse(reader.matchesAny(new char[]{'t', 'i'}));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 String expected = "body";
 Assert.assertTrue(reader.matchConsume("body"));
 Assert.assertEquals(expected.length(), reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "body";
 Assert.assertTrue(reader.matchConsumeIgnoreCase("Body"));
 Assert.assertEquals(expected.length(), reader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("<html>"));
 Assert.assertTrue(reader.containsIgnoreCase("BODY"));
 }

 @Test
 public void testEmptyInput() {
 input = "";
 reader = new CharacterReader(input);
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testInputWithHtmlTags() {
 input = "<html><head></head><body><h1>Test Title</h1></body></html>";
 reader = new CharacterReader(input);
 String title = reader.consumeLetterSequence();
 Assert.assertEquals("TestTitle", title);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidInput() {
 input = "This is an invalid input with <script>alert('Error')</script>";
 reader = new CharacterReader(input);
 reader.consumeLetterSequence();
 }
}
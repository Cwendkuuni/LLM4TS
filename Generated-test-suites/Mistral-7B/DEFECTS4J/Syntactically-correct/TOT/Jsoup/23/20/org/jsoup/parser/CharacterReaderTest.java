package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private String input;
 private CharacterReader reader;

 @Before
 public void setup() {
 input = "This is a test string with <html>\n<head>\n<title>Title</title>\n<body>\n<h1>Heading</h1>\n<p> Para 1 </p>\n<p> Para 2 </p>\n</body>\n</html>";
 reader = new CharacterReader(input);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(reader.isEmpty());
 reader.consume();
 Assert.assertFalse(reader.isEmpty());
 reader.consumeToEnd();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrentAndConsume() {
 char expected = 'T';
 char actual = reader.current();
 Assert.assertEquals(expected, actual);
 actual = reader.consume();
 expected = 'h';
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeAsString() {
 String expected = "<html>\n";
 String actual = reader.consumeAsString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWhenAtEnd() {
 String actual = reader.consumeToEnd();
 }

 @Test
 public void testConsumeTo() {
 String expected = "<title>\n";
 String actual = reader.consumeTo('\n');
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToWhenCharNotPresent() {
 String actual = reader.consumeTo('Z');
 }

 @Test
 public void testConsumeToStringSequence() {
 String expected = "<body>\n<p> Para 1 </p>\n<p> Para 2 </p>\n";
 String actual = reader.consumeTo("<body>");
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToStringSequenceNotPresent() {
 String actual = reader.consumeTo("<html>");
 }

 @Test
 public void testConsumeToAny() {
 String expected = "<p> Para 1 </p>\n<p> Para 2 </p>\n";
 char[] seq = new char[]{'<', 'p'};
 String actual = reader.consumeToAny(seq);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testConsumeToAnySequenceTooLong() {
 char[] seq = new char[]{'<', 'p', 'a'};
 reader.consumeToAny(seq);
 }

 @Test
 public void testMatches() {
 char c = 'h';
 Assert.assertTrue(reader.matches(c));
 reader.consume();
 Assert.assertFalse(reader.matches(c));
 }

 @Test
 public void testMatchesString() {
 String seq = "<html>";
 Assert.assertTrue(reader.matches(seq));
 reader.consumeTo(seq);
 Assert.assertFalse(reader.matches(seq));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "<HTML>";
 Assert.assertTrue(reader.matchesIgnoreCase(seq));
 reader.consumeTo(seq.charAt(0));
 Assert.assertTrue(reader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = new char[]{'<', 'p'};
 Assert.assertTrue(reader.matchesAny(seq));
 reader.consumeToAny(seq);
 Assert.assertFalse(reader.matchesAny(seq));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 reader.consume();
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 reader.consume();
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String seq = "<html>";
 Assert.assertTrue(reader.matchConsume(seq));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String seq = "<HTML>";
 Assert.assertTrue(reader.matchConsumeIgnoreCase(seq));
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "<title>";
 Assert.assertTrue(reader.containsIgnoreCase(seq));
 }

 @Test
 public void testToString() {
 String expected = "Title";
 String actual = reader.toString();
 Assert.assertEquals(expected, actual);
 }
}
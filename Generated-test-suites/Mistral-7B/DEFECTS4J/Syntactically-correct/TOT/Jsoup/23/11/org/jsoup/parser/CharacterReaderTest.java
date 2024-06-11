package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Test
 public void testCreateCharacterReader() {
 char[] input = {'h', 'e', 'l', 'l', 'o', '\n'};
 reader = new CharacterReader(new String(input));
 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testIsEmpty() {
 char[] input = {};
 reader = new CharacterReader(new String(input));
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testConsume() {
 char[] input = {'h', 'e', 'l', 'l', 'o', '\n'};
 reader = new CharacterReader(new String(input));
 Assert.assertEquals('h', reader.consume());
 Assert.assertEquals('e', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 char[] input = {'h', 'e', 'l', 'l', 'o', '\n'};
 reader = new CharacterReader(new String(input));
 reader.consume();
 reader.consume(); // consume past end
 }

 @Test
 public void testMarkAndRewind() {
 char[] input = {'h', 'e', 'l', 'l', 'o', '\n'};
 reader = new CharacterReader(new String(input));
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "<title>Test</title>";
 Assert.assertEquals(expected, reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "<title>Test</title>";
 reader.mark();
 reader.consumeTo(expected.charAt(0));
 Assert.assertEquals(expected, reader.consumeTo(expected));
 }

 @Test
 public void testConsumeToAny() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 char[] expected = {'<', 't', 'i', 't', 'l'};
 String consumed = reader.consumeToAny(expected);
 Assert.assertEquals(new String(expected), consumed);
 }

 @Test
 public void testConsumeToEnd() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "</body>\n</html>";
 Assert.assertEquals(expected, reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "Test";
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals(expected, consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 char[] input = {"<html>\n<head>\n<meta charset='UTF-8'>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "UTF-8";
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals(expected, consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 char[] input = {"<html>\n<head>\n<meta charset='1234567890'>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "1234567890";
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals(expected, consumed);
 }

 @Test
 public void testMatches() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 Assert.assertTrue(reader.matches('T'));
 Assert.assertFalse(reader.matches('@'));
 }

 @Test
 public void testMatchesString() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "<title>Test</title>";
 Assert.assertTrue(reader.matches(expected));
 Assert.assertFalse(reader.matches("<body>"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "<title>Test</title>";
 Assert.assertTrue(reader.matchesIgnoreCase(expected));
 Assert.assertTrue(reader.matchesIgnoreCase("<Title>Test</Title>"));
 Assert.assertFalse(reader.matchesIgnoreCase("BODY"));
 }

 @Test
 public void testMatchesAny() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 char[] expected = {'<', 't', 'i', 't', 'l'};
 Assert.assertTrue(reader.matchesAny(expected));
 }

 @Test
 public void testMatchesLetter() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 Assert.assertFalse(reader.matchesLetter());
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "<title>Test</title>";
 Assert.assertTrue(reader.matchConsume(expected));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "<title>Test</title>";
 Assert.assertTrue(reader.matchConsumeIgnoreCase(expected));
 }

 @Test
 public void testContainsIgnoreCase() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n<meta name='description' content='This is a test'>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "description";
 Assert.assertTrue(reader.containsIgnoreCase(expected));
 }

 @Test
 public void testToString() {
 char[] input = {"<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"};
 reader = new CharacterReader(new String(input));
 String expected = "</body>\n</html>";
 String actual = reader.toString();
 Assert.assertEquals(expected, actual);
 }
}
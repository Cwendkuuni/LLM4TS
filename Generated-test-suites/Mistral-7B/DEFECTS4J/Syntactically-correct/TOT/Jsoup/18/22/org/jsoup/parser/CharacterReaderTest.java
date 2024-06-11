package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
 String testInputString = new String(testInput);
 reader = new CharacterReader(testInputString);
 }

 @After
 public void tearDown() {
 reader = null;
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.consume());
 assertEquals(1, reader.pos());
 }

 @Test
 public void testConsumeString() {
 String consumed = reader.consumeAsString();
 assertEquals("ab", consumed);
 reader.advance();
 reader.advance();
 consumed = reader.consumeAsString();
 assertEquals("cd", consumed);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume(); // should throw an exception
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = reader.consumeToEnd();
 assertEquals("i", consumed.charAt(consumed.length() - 1));
 assertEquals("", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 String consumed = reader.consumeTo('c');
 assertEquals("a b ", consumed);
 }

 @Test
 public void testConsumeToIgnoreCase() {
 String consumed = reader.consumeToIgnoreCase("d e");
 assertEquals("ab cd e", consumed);
 }

 @Test
 public void testConsumeToAny() {
 String consumed = reader.consumeToAny('a', 'b');
 assertEquals("a", consumed);
 consumed = reader.consumeToAny('x', 'z');
 assertEquals("", consumed);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('b'));
 assertFalse(reader.matches('x'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("ab"));
 assertFalse(reader.matches("xy"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("AB"));
 assertTrue(reader.matchesIgnoreCase("cd e"));
 assertFalse(reader.matchesIgnoreCase("xy"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('a', 'b'));
 assertFalse(reader.matchesAny('x', 'z'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 assertTrue(reader.matchesDigit('1'));
 }

 @Test
 public void testMatchConsume() {
 String consumed = reader.matchConsume("ab");
 assertEquals("ab", consumed);
 assertEquals("cd e", reader.consumeAsString());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = reader.matchConsumeIgnoreCase("cd e");
 assertEquals("cd e", consumed);
 assertEquals("", reader.consumeAsString());
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("cd e"));
 assertFalse(reader.containsIgnoreCase("xy"));
 }
}
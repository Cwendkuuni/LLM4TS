package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
 String testInputString = new String(testInput);
 reader = new CharacterReader(testInputString);
 }

 @Test
 public void testConstructor() {
 assertNotNull(reader);
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(reader.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEndOfInput() {
 reader.consume();
 reader.consume();
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
 public void testCurrent() {
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.consume());
 assertEquals('b', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 reader.advance();
 assertEquals('c', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("ab", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("bc", reader.consumeTo('c'));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("defg", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("abcd", reader.consumeToAny('d', 'c', 'b', 'a'));
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('a'));
 assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("ab"));
 assertFalse(reader.matches("cd"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("ab"));
 assertFalse(reader.matchesIgnoreCase("CD"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('a', 'b'));
 assertFalse(reader.matchesAny('z'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 assertTrue(reader.matchesDigit('3'));
 }

 @Test
 public void testMatchConsume() {
 String expected = "bc";
 assertTrue(reader.matchConsume("bc"));
 assertEquals(expected, reader.toString());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "bc";
 assertTrue(reader.matchConsumeIgnoreCase("BC"));
 assertEquals(expected, reader.toString());
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("ef"));
 assertFalse(reader.containsIgnoreCase("xy"));
 }

 @Test
 public void testToString() {
 assertEquals("defg", reader.toString());
 }
}
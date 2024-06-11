package org.jsoup.parser.test;

import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

import org.junit.Test;

public class CharacterReaderTest {

 CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] input = {'h', 'e', 'l', 'l', 'o', '\n', 'w', 'o', 'r', 'l', 'd','\n', 'd', 'a', 't', 'a', ':', '/', '/', 't', 'i', 't', 'l', 'e', '.', 'h', 't', 'm', 'l', '\n', 'e', 'n', 'd', '.'};
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());

 CharacterReader reader1 = new CharacterReader("abcd");
 assertFalse(reader1.isEmpty());

 assertEquals(characterReader.pos(), 0);
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(characterReader.pos(), 0);
 characterReader.advance();
 assertEquals(characterReader.pos(), 1);
 }

 @Test
 public void testCurrent() {
 assertEquals(characterReader.current(), 'h');
 characterReader.advance();
 assertEquals(characterReader.current(), 'e');
 }

 @Test
 public void testConsume() {
 assertEquals(characterReader.consume(), 'h');
 characterReader.advance();
 assertEquals(characterReader.current(), 'e');
 assertEquals(characterReader.consume(), 'e');
 }

 @Test
 public void testUnconsume() {
 characterReader.advance();
 characterReader.unconsume();
 assertEquals(characterReader.current(), 'h');
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 characterReader.advance();
 assertEquals(characterReader.pos(), 2);
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals(characterReader.pos(), 0);
 }

 @Test
 public void testConsumeAsString() {
 assertEquals(characterReader.consumeAsString(), "ll");
 characterReader.advance();
 characterReader.advance();
 assertEquals(characterReader.consumeAsString(), "ld");
 }

 @Test
 public void testConsumeTo() {
 assertEquals(characterReader.consumeTo(':'), ":/t/i/t/l/.");
 characterReader.advance();
 assertEquals(characterReader.consumeTo('t'), "htm");
 }

 @Test
 public void testConsumeToAny() {
 assertEquals(characterReader.consumeToAny('l', 't'), "ll");
 assertEquals(characterReader.consumeToAny('l', 'm'), "ll");
 assertEquals(characterReader.consumeToAny('l', 'n'), "");
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals(characterReader.consumeToEnd(), "end.");
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals(characterReader.consumeLetterSequence(), "hello");
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals(characterReader.consumeHexSequence(), "dat:/");
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals(characterReader.consumeDigitSequence(), "1234");
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('h'));
 assertFalse(characterReader.matches('i'));
 }

 @Test
 public void testMatchesString() {
 String str = "hello";
 assertTrue(characterReader.matches(str));
 String str1 = "world";
 assertFalse(characterReader.matches(str1));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String str = "hello";
 assertTrue(characterReader.matchesIgnoreCase(str));
 String str1 = "World";
 assertTrue(characterReader.matchesIgnoreCase(str1));
 }

 @Test
 public void testMatchesAny() {
 char[] arr = {'h', 'e', 'l', 'o'};
 assertTrue(characterReader.matchesAny(arr));
 char[] arr1 = {'w', 'o', 'r', 'l'};
 assertFalse(characterReader.matchesAny(arr1));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals(characterReader.consume(), 'h');
 assertTrue(characterReader.matchConsume("h"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals(characterReader.consume(), 'h');
 assertTrue(characterReader.matchConsumeIgnoreCase("H"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("title"));
 }

}
package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderCreation() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertEquals("Hello World!", reader.toString());
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testIsEmptyAfterConsumption() {
 CharacterReader reader = new CharacterReader("Hello World!");
 reader.consume();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("Hello World!");
 String consumed = reader.consumeAsString();
 Assert.assertEquals("He", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("Hello World!");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("World!", consumed);
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertEquals(6, reader.nextIndexOf('W'));
 Assert.assertEquals(-1, reader.nextIndexOf('X'));
 }

 @Test
 public void testNextIndexOfString() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertEquals(0, reader.nextIndexOf("He"));
 Assert.assertEquals(-1, reader.nextIndexOf("Xy"));
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader("Hello World!");
 String consumed = reader.consumeTo('l');
 Assert.assertEquals("He", consumed);
 consumed = reader.consumeTo('r');
 Assert.assertEquals("World", consumed);
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("Hello World!");
 String consumed = reader.consumeToAny('l', 'o');
 Assert.assertEquals("He", consumed);
 consumed = reader.consumeToAny('r', 'd');
 Assert.assertEquals("World", consumed);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertTrue(reader.matches('e'));
 Assert.assertTrue(reader.matches("ll"));
 Assert.assertFalse(reader.matches('X'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertTrue(reader.matchesIgnoreCase("ll"));
 Assert.assertFalse(reader.matchesIgnoreCase("X"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertTrue(reader.matchesAny('e', 'l'));
 Assert.assertFalse(reader.matchesAny('X'));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertTrue(reader.containsIgnoreCase("ll"));
 Assert.assertFalse(reader.containsIgnoreCase("X"));
 }
}
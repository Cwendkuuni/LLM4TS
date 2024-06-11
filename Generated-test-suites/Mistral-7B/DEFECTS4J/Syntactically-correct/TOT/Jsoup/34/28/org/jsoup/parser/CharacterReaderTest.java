import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 assertEquals("", reader.toString());
 }

 @Test
 public void testReaderWithSingleCharacter() {
 CharacterReader reader = new CharacterReader("A");
 assertFalse(reader.isEmpty());
 assertEquals("A", reader.toString());
 assertEquals('A', reader.current());
 }

 @Test
 public void testReaderWithMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals(11, reader.length);
 assertEquals("Hello World", reader.toString());
 assertEquals('H', reader.current());
 reader.consume();
 assertEquals('H', reader.current());
 reader.unconsume();
 assertEquals('H', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 reader.consume();
 reader.consume();
 assertEquals('l', reader.current());
 reader.advance();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals("World", reader.consumeToEnd());
 assertEquals(0, reader.pos());
 }

 @Test
 public void testNextIndexOfChar() {
 CharacterReader reader = new CharacterReader("ababcba");
 assertEquals(1, reader.nextIndexOf('a'));
 assertEquals(4, reader.nextIndexOf('b'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOfString() {
 CharacterReader reader = new CharacterReader("ababcbaab");
 assertEquals(4, reader.nextIndexOf("ba"));
 assertEquals(-1, reader.nextIndexOf("xyz"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInput() {
 CharacterReader reader = new CharacterReader("");
 reader.consume();
 reader.consume(-1); // should throw an exception
 }

 @Test
 public void testConsumeEndOfString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("World", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("Hello", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("Hello1234World");
 assertEquals("Hello123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x1a2b3c");
 assertEquals("1a2b3c", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 assertEquals("1234567890", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matches('l'));
 assertTrue(reader.matches("ld"));
 assertFalse(reader.matches('z'));
 assertFalse(reader.matches("xyz"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesIgnoreCase("ld"));
 assertFalse(reader.matchesIgnoreCase("xyz"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesAny('l', 'd'));
 assertFalse(reader.matchesAny('z', 'x'));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>My Page</title></head>");
 assertTrue(reader.containsIgnoreCase("title"));
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("ababcba");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }
}
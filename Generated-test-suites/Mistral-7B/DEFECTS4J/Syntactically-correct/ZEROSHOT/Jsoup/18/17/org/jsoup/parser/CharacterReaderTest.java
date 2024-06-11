import org.jsoup.parser.CharacterReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '.', '!'};
 reader = new CharacterReader(new String(testInput));
 }

 @After
 public void tearDown() {
 reader = null;
 }

 @Test
 public void testConstructor() {
 assertNotNull(reader);
 assertEquals(6, reader.pos());
 assertEquals("hello world.", reader.toString());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(reader.isEmpty());
 reader.consumeToEnd();
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('h', reader.current());
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('h', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 assertEquals('h', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('h', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("hello", reader.consumeAsString());
 reader.advance();
 assertEquals("world.", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("ello", reader.consumeTo('l'));
 reader.advance();
 assertEquals("world.", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("world.", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("hello", reader.consumeLetterSequence());
 reader.consumeLetterSequence();
 assertNull(reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("world.", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("12345", reader.consumeDigitSequence());
 reader.consumeDigitSequence();
 assertNull(reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('h'));
 assertFalse(reader.matches('!'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("ello"));
 assertFalse(reader.matches("world"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("ello"));
 assertTrue(reader.matchesIgnoreCase("World"));
 }

 @Test
 public void testMatchesAny() {
 assertFalse(reader.matchesAny('$'));
 assertTrue(reader.matchesAny('l', 'o'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 assertTrue(reader.matchesDigit('5'));
 }

 @Test
 public void testMatchConsume() {
 assertEquals("ello", reader.matchConsume("ello"));
 assertEquals("world.", reader.matchConsume("world."));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("ello", reader.matchConsumeIgnoreCase("ello"));
 assertEquals("world.", reader.matchConsumeIgnoreCase("World."));
 }

 @Test
 public void testContainIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("ello"));
 assertTrue(reader.containsIgnoreCase("World"));
 }
}
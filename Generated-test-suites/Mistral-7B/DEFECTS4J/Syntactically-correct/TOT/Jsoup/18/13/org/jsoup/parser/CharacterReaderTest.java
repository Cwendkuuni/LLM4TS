import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 reader = new CharacterReader("This is a test string for CharacterReader.");
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 reader.consume();
 assertEquals(1, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('T', reader.current());
 reader.consume();
 assertEquals('h', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('T', reader.consume());
 assertEquals('h', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 assertEquals('T', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('h', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('T', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("is a test ", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("test ", reader.consumeTo(' '));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("string for CharacterReader.", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("is a test ", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("for", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches(' '));
 assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("is"));
 assertFalse(reader.matches("it"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("is"));
 assertFalse(reader.matchesIgnoreCase("it"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny(' ', 't'));
 assertFalse(reader.matchesAny('z'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesLetter());
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals("test ", reader.consume(3).toString());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("test ", reader.matchConsumeIgnoreCase("test"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("test"));
 assertFalse(reader.containsIgnoreCase("example"));
 }
}
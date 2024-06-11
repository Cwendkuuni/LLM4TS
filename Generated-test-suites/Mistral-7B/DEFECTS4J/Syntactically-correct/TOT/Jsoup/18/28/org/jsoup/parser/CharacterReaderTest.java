import org.junit.Before;
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
 char[] testInput = {'t', 'e', 's', 't', ' ', 'a', ' ', 'n', ' ', 'e', 'x', 'a', 'm', 'p', 'l', 'e', ' ', 'o', 'r', ' ', 'c', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', 'e', 'r', '.', ' '};
 String testInputString = new String(testInput);
 reader = new CharacterReader(testInputString);
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.consume());
 assertEquals('e', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.unconsume();
 assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 }

 @Test
 public void testRewindToMark() {
 reader.rewindToMark();
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("test ", reader.consumeAsString());
 reader.advance();
 assertEquals("an example", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("an example", reader.consumeTo(' '));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("or character reader.", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("or character reader.", reader.consumeToAny(' r', 'd'));
 }

 @Test
 public void testMatches() {
 assertFalse(reader.matches('@'));
 assertTrue(reader.matches(' '));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches(" test "));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase(" test "));
 assertTrue(reader.matchesIgnoreCase(" An Example"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny(' ', 'r'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals("character reader.", reader.matchConsume(" or character reader."));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("character reader.", reader.matchConsumeIgnoreCase(" Or Character Reader."));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("example"));
 }

 @Test
 public void testToString() {
 assertNotNull(reader.toString());
 }
}
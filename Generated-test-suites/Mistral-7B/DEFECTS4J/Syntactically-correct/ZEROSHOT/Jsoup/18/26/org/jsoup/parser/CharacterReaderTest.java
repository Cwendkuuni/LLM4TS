import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] testInput = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
 characterReader = new CharacterReader(new String(testInput));
 }

 @Test
 public void testConstructor() {
 assertNotNull(characterReader);
 assertEquals(10, characterReader.length());
 }

 @Test
 public void testPos() {
 assertEquals(0, characterReader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', characterReader.consume());
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 characterReader.unconsume();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("ab", characterReader.consumeAsString());
 assertEquals(8, characterReader.pos());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("bc", characterReader.consumeTo('c'));
 assertEquals('d', characterReader.current());
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("defgih", characterReader.consumeToEnd());
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("abcdefg", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("12345abc", characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("12345", characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('b'));
 assertFalse(characterReader.matches('x'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(characterReader.matches("ab"));
 assertFalse(characterReader.matches("xyz"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("ab"));
 assertFalse(characterReader.matchesIgnoreCase("xyz"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('b', 'c'));
 assertFalse(characterReader.matchesAny('x'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(characterReader.matchesLetter());
 assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals("bc", characterReader.matchConsume("bc"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("bc", characterReader.matchConsumeIgnoreCase("BC"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("ef"));
 assertFalse(characterReader.containsIgnoreCase("xyz"));
 }

 @Test
 public void testToString() {
 assertEquals("defgih", characterReader.toString());
 }

 @After
 public void tearDown() {
 characterReader = null;
 }
}
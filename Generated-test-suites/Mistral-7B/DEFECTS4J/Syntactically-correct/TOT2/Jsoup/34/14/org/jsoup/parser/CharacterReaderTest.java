import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @Before
 public void setUp() {
 char[] input = new char[]{'a', 'b', 'c', 'd', 'e'};
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.consume());
 assertEquals('b', reader.current());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeBeyondEnd() {
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume(); // should throw an IndexOutOfBoundsException
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
 assertEquals('c', reader.current());
 }

 @Test
 public void testMark() {
 reader.consume();
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("a", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOfChar() {
 assertEquals(1, reader.nextIndexOf('b'));
 assertEquals(-1, reader.nextIndexOf('f'));
 }

 @Test
 public void testNextIndexOfCharSequence() {
 char[] seq = new char[]{'b', 'c'};
 assertEquals(2, reader.nextIndexOf(seq));
 }

 @Test
 public void testConsumeToChar() {
 assertEquals("ab", reader.consumeTo('b'));
 }

 @Test
 public void testConsumeToCharSequence() {
 assertEquals("ab", reader.consumeTo(new String(new char[]{'b', 'b'})));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("cd", reader.consumeToAny('c', 'd'));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("de", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("abcd", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("ab12cd", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("abcd12ef", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("12345", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('b'));
 assertFalse(reader.matches('f'));
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
 assertTrue(reader.matchesAny('b', 'c'));
 assertFalse(reader.matchesAny('a', 'z'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals("ab", reader.matchConsume("ab"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("AB", reader.matchConsumeIgnoreCase("Ab"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("bc"));
 assertFalse(reader.containsIgnoreCase("ef"));
 }

 @Test
 public void testToString() {
 assertEquals("de", reader.toString());
 }

}
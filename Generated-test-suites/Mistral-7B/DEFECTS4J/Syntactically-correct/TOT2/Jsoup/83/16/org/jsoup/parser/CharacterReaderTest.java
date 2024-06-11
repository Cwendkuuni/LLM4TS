import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 reader = new CharacterReader(new StringReader("test string"));
 }

 @Test
 public void testConstructorWithString() {
 CharacterReader reader = new CharacterReader("test string");
 Validate.notNull(reader);
 }

 @Test
 public void testConstructorWithReaderAndSz() {
 CharacterReader reader = new CharacterReader(new StringReader("test string"), 10);
 Validate.notNull(reader);
 }

 @Test
 public void testConstructorWithReader() {
 CharacterReader reader = new CharacterReader(new StringReader("test string"));
 Validate.notNull(reader);
 }

 @Test
 public void testBufferUp() {
 reader.bufferUp();
 assertEquals(reader.pos(), 0);
 reader.bufferUp();
 assertEquals(reader.pos(), 11); // 10 characters read + initial position
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume('t');
 assertFalse(reader.isEmpty());
 reader.unconsume();
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 reader.bufferUp();
 assertEquals(reader.current(), 't');
 reader.consume();
 assertEquals(reader.current(), 'e');
 reader.unconsume();
 assertEquals(reader.current(), 't');
 }

 @Test
 public void testConsume() {
 reader.bufferUp();
 assertEquals(reader.consume(), 't');
 assertEquals(reader.pos(), 10);
 reader.bufferUp();
 reader.consume();
 assertEquals(reader.consume(), 'e');
 reader.unconsume();
 assertEquals(reader.consume(), 't');
 }

 @Test
 public void testUnconsume() {
 reader.bufferUp();
 reader.consume();
 reader.unconsume();
 assertEquals(reader.current(), 't');
 }

 @Test
 public void testAdvance() {
 reader.bufferUp();
 reader.advance();
 assertEquals(reader.pos(), 11);
 }

 @Test
 public void testMark() {
 reader.bufferUp();
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals(reader.current(), 't');
 }

 @Test
 public void testNextIndexOf() {
 reader.bufferUp();
 assertEquals(reader.nextIndexOf('t'), 0);
 reader.consume();
 assertEquals(reader.nextIndexOf('t'), 1);
 reader.consume();
 assertEquals(reader.nextIndexOf('t'), -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextIndexOfWithNull() {
 reader.bufferUp();
 reader.nextIndexOf(null);
 }

 @Test
 public void testNextIndexOfWithCharSequence() {
 reader.bufferUp();
 assertEquals(reader.nextIndexOf("st"), 1);
 assertEquals(reader.nextIndexOf("test"), 4);
 assertEquals(reader.nextIndexOf("tes"), -1);
 }

 @Test
 public void testConsumeTo() {
 reader.bufferUp();
 assertEquals(reader.consumeTo('s'), "test");
 assertEquals(reader.pos(), 10);
 }

 @Test
 public void testConsumeToWithEmptyString() {
 reader.bufferUp();
 assertEquals(reader.consumeTo(""), "");
 assertEquals(reader.pos(), 11);
 }

 @Test
 public void testConsumeToWithEmptyTarget() {
 reader.bufferUp();
 assertEquals(reader.consumeTo(""), "");
 assertEquals(reader.pos(), 11);
 }

 @Test
 public void testConsumeToAny() {
 reader.bufferUp();
 assertEquals(reader.consumeToAny(new char[]{'s', 't'}), "test");
 }

 @Test
 public void testConsumeToAnySorted() {
 reader.bufferUp();
 assertEquals(reader.consumeToAnySorted(new char[]{'s', 't'}), "test");
 }

 @Test
 public void testConsumeData() {
 reader.bufferUp();
 assertEquals(reader.consumeData(), "test");
 }

 @Test
 public void testConsumeTagName() {
 reader.bufferUp();
 assertEquals(reader.consumeTagName(), "test");
 }

 @Test
 public void testConsumeToEnd() {
 reader.bufferUp();
 assertEquals(reader.consumeToEnd(), "test");
 }

 @Test
 public void testConsumeLetterSequence() {
 reader.bufferUp();
 assertEquals(reader.consumeLetterSequence(), "test");
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 reader.bufferUp();
 assertEquals(reader.consumeLetterThenDigitSequence(), "test123");
 }

 @Test
 public void testConsumeHexSequence() {
 reader.bufferUp();
 assertEquals(reader.consumeHexSequence(), "test");
 reader.consume();
 assertEquals(reader.consumeHexSequence(), "est");
 }

 @Test
 public void testConsumeDigitSequence() {
 reader.bufferUp();
 assertEquals(reader.consumeDigitSequence(), "123");
 }

 @Test
 public void testMatches() {
 reader.bufferUp();
 assertTrue(reader.matches('t'));
 assertTrue(reader.matches("est"));
 assertFalse(reader.matches('s'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 reader.bufferUp();
 assertTrue(reader.matchesIgnoreCase("Test"));
 assertTrue(reader.matchesIgnoreCase("est"));
 assertFalse(reader.matchesIgnoreCase("s"));
 }

 @Test
 public void testMatchesAny() {
 reader.bufferUp();
 assertTrue(reader.matchesAny('t', 's'));
 assertTrue(reader.matchesAny('t', 'e'));
 assertFalse(reader.matchesAny('s', 'e'));
 }

 @Test
 public void testMatchesAnySorted() {
 reader.bufferUp();
 assertTrue(reader.matchesAnySorted(new char[]{'s', 't'}));
 assertTrue(reader.matchesAnySorted(new char[]{'t', 's'}));
 assertFalse(reader.matchesAnySorted(new char[]{'s', 'e'}));
 }

 @Test
 public void testMatchesLetter() {
 reader.bufferUp();
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('1'));
 }

 @Test
 public void testMatchesDigit() {
 reader.bufferUp();
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit('t'));
 }

 @Test
 public void testMatchConsume() {
 reader.bufferUp();
 assertTrue(reader.matchConsume("est"));
 assertEquals(reader.pos(), 4);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 reader.bufferUp();
 assertTrue(reader.matchConsumeIgnoreCase("Test"));
 assertEquals(reader.pos(), 4);
 }

 @Test
 public void testContainsIgnoreCase() {
 reader.bufferUp();
 assertTrue(reader.containsIgnoreCase("test"));
 assertTrue(reader.containsIgnoreCase("Test"));
 assertFalse(reader.containsIgnoreCase("hello"));
 }
}
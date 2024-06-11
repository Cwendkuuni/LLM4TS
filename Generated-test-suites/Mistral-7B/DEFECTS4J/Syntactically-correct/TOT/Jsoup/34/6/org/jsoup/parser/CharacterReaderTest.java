import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testSingleCharacterReader() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals('a', reader.consume());
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testConsumeEndOfReader() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consumeAll();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeString() {
 CharacterReader reader = new CharacterReader("abcabc");
 String consumed = reader.consumeTo("bc");
 Assert.assertEquals("bc", consumed);
 Assert.assertEquals('a', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEndOfReader() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcd123efg");
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("abcd", consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd123efg");
 String consumed = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("abcd123", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("abcd%#FEEFg");
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("%#FE", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd123efg");
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("123", consumed);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcd123efg");
 Assert.assertTrue(reader.matches('d'));
 Assert.assertFalse(reader.matches('g'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCD123EFG");
 Assert.assertTrue(reader.matchesIgnoreCase("d"));
 Assert.assertTrue(reader.matchesIgnoreCase("G"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcd123efg");
 Assert.assertTrue(reader.matchesAny('d', 'e'));
 Assert.assertFalse(reader.matchesAny('i', 'e'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcd123efg");
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("abcd123efg");
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.matchConsume("ef");
 Assert.assertEquals("ef", consumed);
 Assert.assertEquals('d', reader.current());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 String consumed = reader.matchConsumeIgnoreCase("ef");
 Assert.assertEquals("ef", consumed);
 Assert.assertEquals('D', reader.current());
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>");
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertTrue(reader.containsIgnoreCase("HEAD"));
 }
}
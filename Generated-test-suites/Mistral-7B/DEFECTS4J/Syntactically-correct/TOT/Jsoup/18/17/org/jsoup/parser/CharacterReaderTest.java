import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testSimpleConsumption() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.consume());
 }

 @Test
 public void testConsumptionToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("def", consumed);
 }

 @Test
 public void testConsumptionToCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeTo('f');
 Assert.assertEquals("abcde", consumed);
 }

 @Test
 public void testConsumptionToString() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeTo("def");
 Assert.assertEquals("abcde", consumed);
 }

 @Test
 public void testConsumptionToAny() {
 CharacterReader reader = new CharacterReader("abcdababcdabcdabcd");
 String consumed = reader.consumeToAny('a', 'b', 'c');
 Assert.assertEquals("abcdababcd", consumed);
 }

 @Test
 public void testConsumptionLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("abcdef", consumed);
 }

 @Test
 public void testConsumptionHexSequence() {
 CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("0123", consumed);
 }

 @Test
 public void testConsumptionDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890123456789");
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("123456", consumed);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
 Assert.assertTrue(reader.matches('a'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
 Assert.assertTrue(reader.matches("ab"));
 Assert.assertFalse(reader.matches("xyz"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("AbCdAbCdAbCdAbCd");
 Assert.assertTrue(reader.matchesIgnoreCase("ab"));
 Assert.assertFalse(reader.matchesIgnoreCase("XYZ"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
 Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
 Assert.assertFalse(reader.matchesAny('x', 'y', 'z'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234567890123456789");
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
 String consumed = reader.matchConsume("ab");
 Assert.assertEquals("ab", consumed);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("AbCdAbCdAbCdAbCd");
 String consumed = reader.matchConsumeIgnoreCase("Ab");
 Assert.assertEquals("Ab", consumed);
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>Test</body></html>");
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertTrue(reader.containsIgnoreCase("head"));
 Assert.assertFalse(reader.containsIgnoreCase("foot"));
 }

 @Test
 public void testPositionModification() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 Assert.assertEquals('a', reader.current());
 reader.advance();
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }
}
import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstruction() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertNotNull(reader);
 }

 @Test
 public void testCharacterReaderBufferUp() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertTrue(reader.bufPos > 0);
 }

 @Test
 public void testCharacterReaderPos() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertEquals(0, reader.pos());
 reader.consume();
 reader.bufferUp();
 Assert.assertEquals(1, reader.pos());
 }

 @Test
 public void testCharacterReaderIsEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 Assert.assertTrue(reader.isEmpty());
 reader.consume();
 reader.bufferUp();
 reader.consume();
 Assert.assertFalse(reader.isEmpty());
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 try {
 reader.bufferUp();
 } catch (IOException e) {
 throw new UncheckedIOException(e);
 }
 }

 @Test
 public void testCharacterReaderCurrent() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testCharacterReaderConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 reader.consume();
 reader.bufferUp();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testCharacterReaderUnconsume() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 reader.consume();
 reader.unconsume();
 reader.bufferUp();
 Assert.assertEquals('H', reader.current());
 }

 @Test
 public void testCharacterReaderAdvance() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 reader.advance();
 reader.bufferUp();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testCharacterReaderMark() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 reader.mark();
 reader.consume();
 reader.bufferUp();
 reader.rewindToMark();
 Assert.assertEquals('H', reader.current());
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertEquals(8, reader.nextIndexOf('W'));
 Assert.assertEquals(-1, reader.nextIndexOf('X'));
 }

 @Test
 public void testCharacterReaderNextIndexOfCharSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertEquals(6, reader.nextIndexOf("llo"));
 Assert.assertEquals(-1, reader.nextIndexOf("xxxx"));
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 String result = reader.consumeTo('l');
 Assert.assertEquals("He", result);
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 String result = reader.consumeToEnd();
 Assert.assertEquals("Hello World", result);
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 String result = reader.consumeToAny(new char[]{'l', 'o'});
 Assert.assertEquals("llo", result);
 }

 @Test
 public void testCharacterReaderConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 String result = reader.consumeToAnySorted(new char[]{'l', 'o'});
 Assert.assertEquals("llo", result);
 }

 @Test
 public void testCharacterReaderConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("&<null"));
 reader.bufferUp();
 String result = reader.consumeData();
 Assert.assertEquals("&<null", result);
 }

 @Test
 public void testCharacterReaderConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<title>Test</title>"));
 reader.bufferUp();
 String result = reader.consumeTagName();
 Assert.assertEquals("title", result);
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 String result = reader.consumeLetterSequence();
 Assert.assertEquals("Hello", result);
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello123 World"));
 reader.bufferUp();
 String result = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Hello123", result);
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("&amp;#x61;#x6c;#x65;#x65;"));
 reader.bufferUp();
 String result = reader.consumeHexSequence();
 Assert.assertEquals("ampple", result);
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("123"));
 reader.bufferUp();
 String result = reader.consumeDigitSequence();
 Assert.assertEquals("123", result);
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertTrue(reader.matches('l'));
 Assert.assertFalse(reader.matches('x'));
 }

 @Test
 public void testCharacterReaderMatchesString() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertTrue(reader.matches("llo"));
 Assert.assertFalse(reader.matches("xxxx"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCaseString() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesIgnoreCase("llo"));
 Assert.assertTrue(reader.matchesIgnoreCase("hello"));
 Assert.assertFalse(reader.matchesIgnoreCase("xxxx"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesAny('l', 'o'));
 Assert.assertFalse(reader.matchesAny('x', 'x'));
 }

 @Test
 public void testCharacterReaderMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'x'}));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('x'));
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('x'));
 }

 @Test
 public void testCharacterReaderMatchConsumeString() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 reader.matchConsume("llo");
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testCharacterReaderMatchConsumeIgnoreCaseString() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 reader.matchConsumeIgnoreCase("llo");
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testCharacterReaderContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body></body></html>"));
 reader.bufferUp();
 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 Assert.assertTrue(reader.containsIgnoreCase("/TITLE"));
 }
}
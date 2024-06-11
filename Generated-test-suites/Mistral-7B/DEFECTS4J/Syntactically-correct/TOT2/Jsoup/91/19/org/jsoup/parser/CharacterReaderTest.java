import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReader.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructorWithReader() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("Hello World", reader.toString());
 }

 @Test
 public void testCharacterReaderConstructorWithString() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello World", reader.toString());
 }

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderNotEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderCurrent() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.reader.mark(1);
 reader.reader.close();
 reader.reader.mark(1); // mark failed since the reader is closed
 reader.consume();
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 String result = reader.consumeTo('l');
 Assert.assertEquals("Hello Wor", result);
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 String result = reader.consumeToAny(new char[]{'l', 'd'});
 Assert.assertEquals("Hello World", result);
 }

 @Test
 public void testCharacterReaderConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 String result = reader.consumeToAnySorted(new char[]{'l', 'd', 'r'});
 Assert.assertEquals("Hello World", result);
 }

 @Test
 public void testCharacterReaderConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World & <  null"));
 String result = reader.consumeData();
 Assert.assertEquals("Hello World & <  null", result);
 }

 @Test
 public void testCharacterReaderConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head></html>"));
 String result = reader.consumeTagName();
 Assert.assertEquals("html", result);
 reader.advance();
 result = reader.consumeTagName();
 Assert.assertEquals("head", result);
 reader.advance();
 result = reader.consumeTagName();
 Assert.assertEquals("title", result);
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("AlphaBetaGammaDelta"));
 String result = reader.consumeLetterSequence();
 Assert.assertEquals("Alpha", result);
 reader.advance();
 result = reader.consumeLetterSequence();
 Assert.assertEquals("Beta", result);
 reader.advance();
 result = reader.consumeLetterSequence();
 Assert.assertEquals("Gamma", result);
 reader.advance();
 result = reader.consumeLetterSequence();
 Assert.assertEquals("Delta", result);
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Alpha12Beta34Gamma56Delta78"));
 String result = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Alpha1", result);
 reader.advance();
 result = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Beta3", result);
 reader.advance();
 result = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Gamma5", result);
 reader.advance();
 result = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Delta7", result);
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("0x1a 0x2b 0x3c 0x4d"));
 String result = reader.consumeHexSequence();
 Assert.assertEquals("1a", result);
 reader.advance();
 result = reader.consumeHexSequence();
 Assert.assertEquals("2b", result);
 reader.advance();
 result = reader.consumeHexSequence();
 Assert.assertEquals("3c", result);
 reader.advance();
 result = reader.consumeHexSequence();
 Assert.assertEquals("4d", result);
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("123 456 789"));
 String result = reader.consumeDigitSequence();
 Assert.assertEquals("123", result);
 reader.advance();
 result = reader.consumeDigitSequence();
 Assert.assertEquals("456", result);
 reader.advance();
 result = reader.consumeDigitSequence();
 Assert.assertEquals("789", result);
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matches('l'));
 Assert.assertTrue(reader.matches("World"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesIgnoreCase("wORld"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesAny(new char[]{'l', 'd'}));
 Assert.assertTrue(reader.matchesAny(new char[]{'l', 'd', 'w'}));
 }

 @Test
 public void testCharacterReaderMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'd'}));
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'d', 'l'}));
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'd', 'w'}));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesLetter());
 reader.consume();
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesDigit());
 reader.consume();
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testCharacterReaderMark() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('H', reader.current());
 }

 @Test
 public void testCharacterReaderRewindToMarkInvalid() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertFalse(reader.matches('H'));
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(9, reader.nextIndexOf('l'));
 Assert.assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testCharacterReaderNextIndexOfCharSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(6, reader.nextIndexOf("World"));
 Assert.assertEquals(-1, reader.nextIndexOf("Zulu"));
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 String result = reader.consumeToEnd();
 Assert.assertEquals("Hello World", result);
 }
}
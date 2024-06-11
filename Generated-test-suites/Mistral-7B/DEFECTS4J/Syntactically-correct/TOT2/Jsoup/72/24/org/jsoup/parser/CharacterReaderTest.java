import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Locale;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 Validate.noException("Reader must not throw an exception in the constructor.", () -> reader = new CharacterReader(new StringReader("Hello World!")));
 }

 @Test
 public void testConstructorWithReaderAndSize() {
 CharacterReader reader1 = new CharacterReader(new StringReader("Test"), 10);
 Validate.noException("Reader must not throw an exception in the constructor.", () -> reader1);
 }

 @Test
 public void testConstructorWithString() {
 CharacterReader reader1 = new CharacterReader(new StringReader("Test"), "TestString".length());
 Validate.noException("Reader must not throw an exception in the constructor.", () -> reader1);
 }

 @Test
 public void testConstructorWithStringOnly() {
 CharacterReader reader1 = new CharacterReader(new StringReader("Test"), "TestString");
 Validate.noException("Reader must not throw an exception in the constructor.", () -> reader1);
 }

 @Test
 public void testBufferUp() {
 reader.bufferUp();
 Assert.assertEquals(0, reader.bufPos);
 Assert.assertEquals(0, reader.bufLength);

 reader = new CharacterReader(new StringReader("Test"), "TestString".length());
 reader.bufferUp();
 Assert.assertEquals(0, reader.bufPos);
 Assert.assertEquals(11, reader.bufLength);
 }

 @Test
 public void testPos() {
 reader = new CharacterReader(new StringReader("Test"), "TestString".length());
 reader.mark();
 reader.advance();
 reader.advance();
 Assert.assertEquals(2, reader.pos());
 reader.rewindToMark();
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 reader = new CharacterReader(new StringReader("Test"), "TestString".length());
 reader.advance();
 Assert.assertFalse(reader.isEmpty());
 reader.rewindToMark();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 reader = new CharacterReader(new StringReader("Test"), "TestString".length());
 reader.advance();
 Assert.assertEquals('T', reader.current());
 reader.advance();
 Assert.assertEquals('e', reader.current());
 reader.unconsume();
 Assert.assertEquals('e', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEndOfReader() {
 reader = new CharacterReader(new StringReader("Test"), "TestString".length());
 reader.advance();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 }

 @Test
 public void testConsumeLetterSequence() {
 reader = new CharacterReader(new StringReader("TestLetterSequence"), "TestLetterSequence".length());
 String result = reader.consumeLetterSequence();
 Assert.assertEquals("Test", result);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 reader = new CharacterReader(new StringReader("TestLetterThenDigitSequence123"), "TestLetterThenDigitSequence123".length());
 String result = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Test123", result);
 }

 @Test
 public void testConsumeHexSequence() {
 reader = new CharacterReader(new StringReader("TestHexSequence#F123"), "TestHexSequence#F123".length());
 String result = reader.consumeHexSequence();
 Assert.assertEquals("Test#F123", result);
 }

 @Test
 public void testConsumeDigitSequence() {
 reader = new CharacterReader(new StringReader("TestDigitSequence123"), "TestDigitSequence123".length());
 String result = reader.consumeDigitSequence();
 Assert.assertEquals("123", result);
 }

 @Test
 public void testMatches() {
 reader = new CharacterReader(new StringReader("TestMatchesTests"), "TestMatchesTests".length());
 Assert.assertTrue(reader.matches('s'));
 Assert.assertTrue(reader.matches("est"));
 Assert.assertFalse(reader.matches('x'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 reader = new CharacterReader(new StringReader("TestMatchesTests"), "TestMatchesTests".length());
 Assert.assertTrue(reader.matchesIgnoreCase("ests"));
 Assert.assertTrue(reader.matchesIgnoreCase("est"));
 Assert.assertFalse(reader.matchesIgnoreCase("x"));
 }

 @Test
 public void testMatchesAny() {
 reader = new CharacterReader(new StringReader("TestMatchesAnyTests"), "TestMatchesAnyTests".length());
 Assert.assertTrue(reader.matchesAny('s', 'e'));
 Assert.assertTrue(reader.matchesAny("est", 't'));
 Assert.assertFalse(reader.matchesAny('x', 'e'));
 }

 @Test
 public void testMatchesAnySorted() {
 reader = new CharacterReader(new StringReader("TestMatchesAnySortedTests"), "TestMatchesAnySortedTests".length());
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'s', 'e'}));
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'e'}));
 }

 @Test
 public void testConsumeTo() {
 reader = new CharacterReader(new StringReader("TestConsumeToTests"), "TestConsumeToTests".length());
 String result = reader.consumeTo('s');
 Assert.assertEquals("Test", result);
 String result2 = reader.consumeTo('t');
 Assert.assertEquals("Test", result2);
 String result3 = reader.consumeTo('e');
 Assert.assertEquals("Test", result3);
 String result4 = reader.consumeTo('s');
 Assert.assertEquals("Test", result4);
 String result5 = reader.consumeTo('s');
 Assert.assertEquals("Test", result5);
 String result6 = reader.consumeTo('n');
 Assert.assertEquals("Test", result6);
 String result7 = reader.consumeTo('s');
 Assert.assertEquals("Tests", result7);
 String result8 = reader.consumeTo('e');
 Assert.assertEquals("Tests", result8);
 String result9 = reader.consumeTo('s');
 Assert.assertEquals("Tests", result9);
 String result10 = reader.consumeTo('s');
 Assert.assertEquals("Tests", result10);
 String result11 = reader.consumeToEnd();
 Assert.assertEquals("Tests", result11);
 }

 @Test
 public void testConsumeToAny() {
 reader = new CharacterReader(new StringReader("TestConsumeToAnyTests"), "TestConsumeToAnyTests".length());
 String result = reader.consumeToAny(new char[]{'s', 't'});
 Assert.assertEquals("Test", result);
 String result2 = reader.consumeToAny(new char[]{'s', 'e'});
 Assert.assertEquals("Test", result2);
 String result3 = reader.consumeToAny(new char[]{'t', 'e'});
 Assert.assertEquals("Test", result3);
 String result4 = reader.consumeToAny(new char[]{'s', 'e', 's'});
 Assert.assertEquals("Tests", result4);
 String result5 = reader.consumeToAnySorted(new char[]{'t', 's', 'e'});
 Assert.assertEquals("Tests", result5);
 String result6 = reader.consumeData();
 Assert.assertEquals("TestData", result6);
 String result7 = reader.consumeTagName();
 Assert.assertEquals("TestTagName", result7);
 }

 @Test
 public void testConsumeTagName() {
 reader = new CharacterReader(new StringReader("<TestTagNameTest>"), "<TestTagNameTest>.length()");
 String result = reader.consumeTagName();
 Assert.assertEquals("TestTagName", result);
 }

 @Test
 public void testConsumeToEnd() {
 reader = new CharacterReader(new StringReader("TestConsumeToEndTests"), "TestConsumeToEndTests".length());
 String result = reader.consumeToEnd();
 Assert.assertEquals("Tests", result);
 }
}
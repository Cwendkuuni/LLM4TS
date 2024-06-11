import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CharacterReaderTest {

 private static final String TEST_STRING = "This is a test string for CharacterReader.";

 @Test
 public void testCharacterReaderConstruction() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertNotNull(reader);
 assertEquals(TEST_STRING, reader.toString());
 }

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testReaderPosition() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(0, reader.pos());
 }

 @Test
 public void testReaderConsume() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(TEST_STRING.charAt(0), reader.consume());
 assertEquals(1, reader.pos());
 }

 @Test
 public void testReaderConsumeString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(TEST_STRING.substring(0, 1), reader.consumeAsString());
 assertEquals(1, reader.pos());
 }

 @Test
 public void testReaderConsumeEnd() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeToEnd();
 assertEquals(TEST_STRING, consumed);
 assertEquals(TEST_STRING.length(), reader.pos());
 }

 @Test
 public void testReaderNextIndexOfChar() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(1, reader.nextIndexOf('t'));
 }

 @Test
 public void testReaderNextIndexOfCharNotFound() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testReaderNextIndexOfString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String substring = "is a test";
 assertEquals(TEST_STRING.indexOf(substring), reader.nextIndexOf(substring));
 }

 @Test
 public void testReaderNextIndexOfStringNotFound() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(-1, reader.nextIndexOf("not in the string"));
 }

 @Test
 public void testReaderConsumeTo() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String expected = TEST_STRING.substring(0, 5);
 assertEquals(expected, reader.consumeTo(' '));
 assertEquals(6, reader.pos());
 }

 @Test
 public void testReaderConsumeToEndWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 assertEquals("", reader.consumeToEnd());
 assertEquals(0, reader.pos());
 }

 @Test
 public void testReaderConsumeToEndWithStringInput() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(TEST_STRING, reader.consumeToEnd());
 assertEquals(TEST_STRING.length(), reader.pos());
 }

 @Test
 public void testReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String expected = TEST_STRING.substring(0, 4);
 assertEquals(expected, reader.consumeToAny(' '));
 }

 @Test
 public void testReaderConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String expected = TEST_STRING.substring(0, 3);
 assertEquals(expected, reader.consumeToAnySorted(new char[]{'t', 's'}));
 }

 @Test
 public void testReaderConsumeData() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String expected = TEST_STRING.substring(0, TEST_STRING.indexOf('&'));
 assertEquals(expected, reader.consumeData());
 }

 @Test
 public void testReaderConsumeTagName() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String expected = TEST_STRING.substring(0, TEST_STRING.indexOf("/"))
 .substring(TEST_STRING.indexOf("/")+1);
 assertEquals(expected, reader.consumeTagName());
 }

 @Test(expected = AssertionError.class)
 public void testReaderConsumeBeyondEnd() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consumeToEnd();
 reader.consume(); // should throw an AssertionError
 }

 @Test
 public void testReaderMark() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderCacheString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String cached = reader.cacheString(0, 10);
 assertNotNull(cached);
 assertEquals(TEST_STRING.substring(0, 10), cached);
 }

 @Test
 public void testCharacterReaderRangeEquals() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String substring1 = TEST_STRING.substring(0, 10);
 String substring2 = TEST_STRING.substring(5, 15);
 assertTrue(reader.rangeEquals(0, 10, substring1));
 assertFalse(reader.rangeEquals(0, 10, substring2));
 }
}
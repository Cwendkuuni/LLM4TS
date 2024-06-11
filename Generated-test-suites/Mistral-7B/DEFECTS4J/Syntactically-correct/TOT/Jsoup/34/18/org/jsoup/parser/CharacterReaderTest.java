import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmptyString() {
 CharacterReader reader = new CharacterReader("");
 assertEquals(0, reader.pos());
 assertFalse(reader.matches('a'));
 }

 @Test
 public void testCharacterReaderSimpleConsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertEquals('a', reader.current());
 assertEquals('a', reader.consume());
 assertEquals('b', reader.current());
 }

 @Test
 public void testCharacterReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 reader.advance();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test (expected = NullPointerException.class)
 public void testCharacterReaderNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }

 @Test
 public void testCharacterReaderConsumeAsString() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeAsString();
 assertEquals("abc", consumed);
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader("abcabcabc");
 int index = reader.nextIndexOf('a');
 assertEquals(0, index);
 index = reader.nextIndexOf('b');
 assertEquals(3, index);
 index = reader.nextIndexOf('x');
 assertEquals(-1, index);
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader("abcabcabc");
 String consumed = reader.consumeTo('a');
 assertEquals("a", consumed);
 consumed = reader.consumeTo('b');
 assertEquals("abc", consumed);
 consumed = reader.consumeToEnd();
 assertEquals("abcabcabc", consumed);
 consumed = reader.consumeTo("ab");
 assertEquals("abc", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcd1234efg");
 String consumed = reader.consumeToAny('1', '2');
 assertEquals("12", consumed);
 consumed = reader.consumeToAny('x', 'y');
 assertEquals("", consumed);
 }

 @Test
 public void testCharacterReaderToString() {
 CharacterReader reader = new CharacterReader("abcdef");
 String input = reader.toString();
 assertEquals("abcdef", input);
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertTrue(reader.matches('a'));
 assertFalse(reader.matches('z'));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcDEF");
 assertTrue(reader.matchesIgnoreCase("a"));
 assertFalse(reader.matchesIgnoreCase("z"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader("abcd1234efg");
 assertTrue(reader.matchesAny('a', 'b'));
 assertFalse(reader.matchesAny('z', 'x'));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader("abc123def");
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testCharacterReaderMatchConsume() {
 CharacterReader reader = new CharacterReader("abcabcabc");
 String consumed = reader.matchConsume("bc");
 assertEquals("bc", consumed);
 }

 @Test
 public void testCharacterReaderMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("AbCaBcAbC");
 String consumed = reader.matchConsumeIgnoreCase("AbC");
 assertEquals("AbC", consumed);
 }

 @Test
 public void testCharacterReaderContainIgnoreCase() {
 CharacterReader reader = new CharacterReader(" <html><head><title>Test</title></head> <body>");
 assertTrue(reader.containsIgnoreCase("<title>"));
 assertFalse(reader.containsIgnoreCase("<Head>"));
 }
}
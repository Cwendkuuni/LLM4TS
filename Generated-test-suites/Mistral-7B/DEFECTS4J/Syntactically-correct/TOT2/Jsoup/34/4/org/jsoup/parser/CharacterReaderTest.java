import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testConstructor() {
 CharacterReader characterReader = new CharacterReader("TestInput");
 Assert.assertNotNull(characterReader);
 }

 @Test
 public void testisEmpty() {
 CharacterReader characterReader = new CharacterReader("TestInput");
 Assert.assertFalse(characterReader.isEmpty());
 characterReader.consume();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 CharacterReader characterReader = new CharacterReader("TestInput");
 Assert.assertEquals(0, characterReader.pos());
 characterReader.consume();
 Assert.assertEquals(1, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 CharacterReader characterReader = new CharacterReader("TestInput");
 Assert.assertEquals('T', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testConsume() {
 CharacterReader characterReader = new CharacterReader("TestInput");
 String input = characterReader.consumeTo('t').toString();
 Assert.assertEquals("Test", input);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 CharacterReader characterReader = new CharacterReader("TestInput");
 characterReader.consumeToEnd();
 characterReader.consume();
 }

 @Test
 public void testUnconsume() {
 CharacterReader characterReader = new CharacterReader("TestInput");
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('T', characterReader.current());
 }

 @Test
 public void testMark() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 Assert.assertEquals('T', characterReader.current());
 }

 @Test
 public void testNextIndexOfChar() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 Assert.assertEquals(5, characterReader.nextIndexOf('p'));
 Assert.assertEquals(-1, characterReader.nextIndexOf('x'));
 }

 @Test
 public void testNextIndexOfSequence() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 Assert.assertEquals(5, characterReader.nextIndexOf("pt"));
 Assert.assertEquals(-1, characterReader.nextIndexOf("xx"));
 }

 @Test
 public void testConsumeToChar() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 String input = characterReader.consumeTo('p').toString();
 Assert.assertEquals("TestInput", input);
 }

 @Test
 public void testConsumeToSequence() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 String input = characterReader.consumeTo("pt").toString();
 Assert.assertEquals("TestInputTest", input);
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 String input = characterReader.consumeToEnd().toString();
 Assert.assertEquals("TestInputTestInput", input);
 }

 @Test
 public void testMatching() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 Assert.assertTrue(characterReader.matches('t'));
 Assert.assertTrue(characterReader.matches("pt"));
 Assert.assertFalse(characterReader.matches('x'));
 Assert.assertFalse(characterReader.matches("xx"));
 }

 @Test
 public void testMatchingIgnoreCase() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 Assert.assertTrue(characterReader.matchesIgnoreCase("Pt"));
 Assert.assertTrue(characterReader.matchesIgnoreCase("pt"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("Xx"));
 }

 @Test
 public void testMatchingAny() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 Assert.assertTrue(characterReader.matchesAny('p', 't'));
 Assert.assertTrue(characterReader.matchesAny("pt", 't'));
 Assert.assertFalse(characterReader.matchesAny('x', 'x'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 Assert.assertTrue(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 Assert.assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 String input = characterReader.matchConsume("pt").toString();
 Assert.assertEquals("TestInputTest", input);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader characterReader = new CharacterReader("TestInputTestInput");
 String input = characterReader.matchConsumeIgnoreCase("PT").toString();
 Assert.assertEquals("TestInputTest", input);
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader characterReader = new CharacterReader("Test<title>Input<title>Test<style>");
 Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
 Assert.assertTrue(characterReader.containsIgnoreCase("/STYLE"));
 }
}
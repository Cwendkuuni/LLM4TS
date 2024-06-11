import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import static org.junit.Assert.*;

public class TokenQueueTest {

 private TokenQueue queue;

 @Before
 public void setup() {
 queue = new TokenQueue("abcdef<tag>123<tag class='test' id='test'> <tag2>hello world</tag2>");
 }

 /**
 * Test isEmpty method
 */
 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());
 queue.consume('a');
 assertFalse(queue.isEmpty());
 }

 /**
 * Test peek method
 */
 @Test
 public void testPeek() {
 assertEquals('a', queue.peek());
 queue.advance();
 assertEquals('b', queue.peek());
 }

 /**
 * Test addFirst method
 */
 @Test
 public void testAddFirst() {
 queue.addFirst('z');
 assertEquals('z', queue.peek());
 }

 /**
 * Test matches method with case sensitive string
 */
 @Test
 public void testMatchesCS() {
 assertTrue(queue.matchesCS("ab"));
 assertFalse(queue.matchesCS("AB"));
 }

 /**
 * Test matches method with case insensitive string
 */
 @Test
 public void testMatches() {
 assertTrue(queue.matches("ab"));
 assertTrue(queue.matches("Ab"));
 }

 /**
 * Test matchesAny method with case sensitive strings
 */
 @Test
 public void testMatchesAnyCS() {
 assertFalse(queue.matchesAnyCS("CD"));
 assertTrue(queue.matchesAnyCS("AB", "CD"));
 }

 /**
 * Test matchesAny method with case insensitive strings
 */
 @Test
 public void testMatchesAny() {
 assertFalse(queue.matchesAny("cd"));
 assertTrue(queue.matchesAny("ab", "cd"));
 }

 /**
 * Test matchesStartTag method
 */
 @Test
 public void testMatchesStartTag() {
 assertTrue(queue.matchesStartTag());
 assertFalse(queue.matchesStartTag()); // consume a character
 }

 /**
 * Test matchChomp method
 */
 @Test
 public void testMatchChomp() {
 queue.matchChomp("tag");
 assertEquals("123<tag class='test' id='test'>", queue.toString());
 }

 /**
 * Test matchesWhitespace method
 */
 @Test
 public void testMatchesWhitespace() {
 assertTrue(queue.matchesWhitespace());
 queue.advance();
 assertFalse(queue.matchesWhitespace());
 }

 /**
 * Test matchesWord method
 */
 @Test
 public void testMatchesWord() {
 assertTrue(queue.matchesWord());
 assertFalse(queue.matchesWord()); // consume a character
 }

 /**
 * Test advance method
 */
 @Test
 public void testAdvance() {
 queue.advance();
 assertEquals('b', queue.peek());
 }

 /**
 * Test consume method
 */
 @Test
 public void testConsume() {
 assertEquals('a', queue.consume());
 }

 /**
 * Test consume method with string
 */
 @Test
 public void testConsumeString() {
 queue.consume("tag");
 assertEquals("123<tag class='test' id='test'> <tag2>hello world</tag2>", queue.toString());
 }

 /**
 * Test consumeTo method with case sensitive string
 */
 @Test
 public void testConsumeToCS() {
 String consumed = queue.consumeTo("tag");
 assertEquals("123<tag class='test' id='test'> ", queue.toString());
 assertEquals("tag", consumed);
 }

 /**
 * Test consumeTo method with case insensitive string
 */
 @Test
 public void testConsumeTo() {
 String consumed = queue.consumeTo("Tag");
 assertEquals("123<tag class='test' id='test'> ", queue.toString());
 assertEquals("tag", consumed);
 }

 /**
 * Test consumeToAny method with case sensitive strings
 */
 @Test
 public void testConsumeToAnyCS() {
 String consumed = queue.consumeToAny("CD");
 assertEquals("123<tag class='test' id='test'> <tag2>hello world</tag2>", queue.toString());
 assertEquals("", consumed);
 }

 /**
 * Test consumeToAny method with case insensitive strings
 */
 @Test
 public void testConsumeToAny() {
 String consumed = queue.consumeToAny("AB", "CD");
 assertEquals("123<tag class='test' id='test'> <tag2>hello world</tag2>", queue.toString());
 assertEquals("tag2", consumed);
 }

 /**
 * Test chompBalanced method with balanced parentheses
 */
 @Test
 public void testChompBalanced() {
 String consumed = queue.chompBalanced('(', ')');
 assertEquals("abcdef<tag>123<tag class='test' id='test'> ", queue.toString());
 assertEquals("(one (two) three)", consumed);
 }

 /**
 * Test chompBalanced method with unbalanced openers and closers
 */
 @Test(expected = IllegalStateException.class)
 public void testChompBalancedUnbalanced() {
 queue.chompBalanced('(', '}');
 }

 /**
 * Test chompBalanced method with escaped characters
 */
 @Test
 public void testChompBalancedEscaped() {
 String consumed = queue.chompBalanced('(', '\\)');
 assertEquals("abcdef<tag>123<tag class='test' id='test'> ", queue.toString());
 assertEquals("(one (two) three)", consumed);
 }

 /**
 * Test unescape method
 */
 @Test
 public void testUnescape() {
 String escaped = "\\(one \\(two\\) three\\)";
 String unescaped = TokenQueue.unescape(escaped);
 assertEquals("(one (two) three)", unescaped);
 }

 /**
 * Test consumeWhitespace method
 */
 @Test
 public void testConsumeWhitespace() {
 assertTrue(queue.consumeWhitespace());
 }

 /**
 * Test consumeWord method
 */
 @Test
 public void testConsumeWord() {
 String word = queue.consumeWord();
 assertEquals("tag", word);
 }

 /**
 * Test consumeTagName method
 */
 @Test
 public void testConsumeTagName() {
 String tagName = queue.consumeTagName();
 assertEquals("tag", tagName);
 }

 /**
 * Test consumeElementSelector method
 */
 @Test
 public void testConsumeElementSelector() {
 String elementSelector = queue.consumeElementSelector();
 assertEquals("tag", elementSelector);
 }

 /**
 * Test consumeCssIdentifier method
 */
 @Test
 public void testConsumeCssIdentifier() {
 String cssIdentifier = queue.consumeCssIdentifier();
 assertEquals("test", cssIdentifier);
 }

 /**
 * Test consumeAttributeKey method
 */
 @Test
 public void testConsumeAttributeKey() {
 String attributeKey = queue.consumeAttributeKey();
 assertEquals("class", attributeKey);
 }

 /**
 * Test remainder method
 */
 @Test
 public void testRemainder() {
 String remainder = queue.remainder();
 assertEquals("", remainder);
 queue.consume("tag");
 remainder = queue.remainder();
 assertEquals("", remainder);
 }

 /**
 * Test toString method
 */
 @Test
 public void testToString() {
 assertEquals("", queue.toString());
 queue.consume('a');
 assertEquals("b", queue.toString());
 }
}
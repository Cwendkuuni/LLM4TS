import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 tokenQueue = new TokenQueue("test");
 }

 @After
 public void tearDown() {
 tokenQueue = null;
 }

 /**
 * Test constructor.
 */
 @Test
 public void testConstructor() {
 assertNotNull(tokenQueue);
 assertEquals("test", tokenQueue.queue);
 assertEquals(0, tokenQueue.pos);
 }

 /**
 * Test isEmpty.
 */
 @Test
 public void testIsEmpty() {
 assertTrue(tokenQueue.isEmpty());
 tokenQueue.addFirst('a');
 assertFalse(tokenQueue.isEmpty());
 tokenQueue.consume();
 assertTrue(tokenQueue.isEmpty());
 }

 /**
 * Test peek.
 */
 @Test
 public void testPeek() {
 tokenQueue.addFirst('a');
 assertEquals('a', tokenQueue.peek());
 tokenQueue.consume();
 assertEquals(0, tokenQueue.peek());
 }

 /**
 * Test addFirst.
 */
 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 assertEquals("ab", tokenQueue.toString());
 }

 /**
 * Test matches.
 */
 @Test
 public void testMatches() {
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 assertEquals(true, tokenQueue.matches("test"));
 }

 /**
 * Test matchesCS.
 */
 @Test
 public void testMatchesCS() {
 tokenQueue.addFirst('T');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 assertEquals(true, tokenQueue.matchesCS("test"));
 }

 /**
 * Test matchesAny.
 */
 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 assertEquals(true, tokenQueue.matchesAny("ab"));
 }

 /**
 * Test matchesAny with array.
 */
 @Test
 public void testMatchesAnyArray() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 assertArrayEquals(new char[]{'a', 'b'}, tokenQueue.matchesAny(new char[]{'a', 'b'}));
 tokenQueue.addFirst('c');
 assertFalse(tokenQueue.matchesAny(new char[]{'a', 'b'}));
 }

 /**
 * Test matchesStartTag.
 */
 @Test
 public void testMatchesStartTag() {
 tokenQueue.addFirst('<');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst(' ');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('>');
 assertTrue(tokenQueue.matchesStartTag());
 }

 /**
 * Test matchChomp.
 */
 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 assertEquals("test", tokenQueue.matchChomp("test"));
 }

 /**
 * Test matchesWhitespace.
 */
 @Test
 public void testMatchesWhitespace() {
 tokenQueue.addFirst(' ');
 tokenQueue.addFirst(' ');
 tokenQueue.addFirst('\t');
 assertTrue(tokenQueue.matchesWhitespace());
 }

 /**
 * Test matchesWord.
 */
 @Test
 public void testMatchesWord() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 tokenQueue.addFirst('d');
 assertTrue(tokenQueue.matchesWord());
 tokenQueue.consume();
 tokenQueue.consume();
 tokenQueue.consume();
 assertTrue(tokenQueue.matchesWord());
 }

 /**
 * Test advance.
 */
 @Test
 public void testAdvance() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 tokenQueue.advance();
 assertEquals('b', tokenQueue.consume());
 tokenQueue.advance();
 assertEquals('c', tokenQueue.consume());
 }

 /**
 * Test consume.
 */
 @Test
 public void testConsume() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 assertEquals('a', tokenQueue.consume());
 assertEquals('b', tokenQueue.consume());
 assertEquals(0, tokenQueue.consume());
 }

 /**
 * Test consume.
 */
 @Test(expected = IllegalStateException.class)
 public void testConsumeEmptyQueue() {
 tokenQueue.consume();
 }

 /**
 * Test consume.
 */
 @Test(expected = IllegalStateException.class)
 public void testConsumeWithNullSequence() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.consume("null");
 }

 /**
 * Test consume.
 */
 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceShorterThanQueue() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 tokenQueue.consume("ab");
 }

 /**
 * Test consumeTo.
 */
 @Test
 public void testConsumeTo() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 tokenQueue.addFirst('d');
 tokenQueue.addFirst('e');
 assertEquals("abc", tokenQueue.consumeTo("abc"));
 assertEquals("defe", tokenQueue.toString());
 }

 /**
 * Test consumeToIgnoreCase.
 */
 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue.addFirst('A');
 tokenQueue.addFirst('B');
 tokenQueue.addFirst('C');
 tokenQueue.addFirst('D');
 tokenQueue.addFirst('E');
 assertEquals("ABC", tokenQueue.consumeToIgnoreCase("AbC"));
 assertEquals("DEF", tokenQueue.toString());
 }

 /**
 * Test consumeToAny.
 */
 @Test
 public void testConsumeToAny() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 tokenQueue.addFirst('d');
 tokenQueue.addFirst('e');
 assertEquals("abc", tokenQueue.consumeToAny(new String[]{"a", "b", "c"}));
 assertEquals("defe", tokenQueue.toString());
 }

 /**
 * Test chompBalanced.
 */
 @Test
 public void testChompBalanced() {
 tokenQueue.addFirst('(');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('(');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst(')');
 assertEquals("test", tokenQueue.chompBalanced('(', ')').toString());
 }

 /**
 * Test unescape.
 */
 @Test
 public void testUnescape() {
 assertEquals("abc\\def\\ghi\\jkl\\mno\\pqr\\stu\\vwxyz", TokenQueue.unescape("abc\\def\\ghi\\\\jkl\\mno\\pqr\\stu\\vwxyz"));
 }

 /**
 * Test consumeWhitespace.
 */
 @Test
 public void testConsumeWhitespace() {
 tokenQueue.addFirst(' ');
 tokenQueue.addFirst('\t');
 tokenQueue.addFirst('\r');
 tokenQueue.addFirst('\n');
 tokenQueue.consumeWhitespace();
 tokenQueue.consumeWhitespace();
 assertTrue(tokenQueue.isEmpty());
 }

 /**
 * Test consumeWord.
 */
 @Test
 public void testConsumeWord() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 tokenQueue.addFirst('d');
 assertEquals("abc", tokenQueue.consumeWord());
 assertEquals("d", tokenQueue.consumeWord());
 tokenQueue.consumeWord();
 assertEquals(0, tokenQueue.consumeWord().length());
 }

 /**
 * Test consumeTagName.
 */
 @Test
 public void testConsumeTagName() {
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 assertEquals("test", tokenQueue.consumeTagName());
 tokenQueue.consumeTagName();
 assertEquals(0, tokenQueue.consumeTagName().length());
 }

 /**
 * Test consumeElementSelector.
 */
 @Test
 public void testConsumeElementSelector() {
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('|');
 tokenQueue.addFirst('i');
 tokenQueue.addFirst('d');
 tokenQueue.addFirst(':');
 tokenQueue.addFirst('p');
 tokenQueue.addFirst(' ');
 tokenQueue.addFirst('a');
 tokenQueue.addFirst(' ');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('t');
 assertEquals("test|id:p", tokenQueue.consumeElementSelector());
 }

 /**
 * Test consumeCssIdentifier.
 */
 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue.addFirst('i');
 tokenQueue.addFirst('d');
 tokenQueue.addFirst(':');
 tokenQueue.addFirst('m');
 tokenQueue.addFirst('y');
 tokenQueue.addFirst('-');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('y');
 tokenQueue.addFirst('l');
 tokenQueue.addFirst('e');
 assertEquals("my-style", tokenQueue.consumeCssIdentifier());
 }

 /**
 * Test consumeAttributeKey.
 */
 @Test
 public void testConsumeAttributeKey() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('r');
 tokenQueue.addFirst('i');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('u');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst(':');
 tokenQueue.addFirst('c');
 tokenQueue.addFirst('l');
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('s');
 assertEquals("attribute:class", tokenQueue.consumeAttributeKey());
 }

 /**
 * Test remainder.
 */
 @Test
 public void testRemainder() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 tokenQueue.addFirst('d');
 tokenQueue.addFirst('e');
 assertEquals("e", tokenQueue.remainder().toString());
 }
}
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 // Test for isEmpty() method
 @Test
 public void testIsEmpty() {
 TokenQueue tokenQueue = new TokenQueue("");
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 // Test for isEmpty() method with non-empty queue
 @Test
 public void testNotEmpty() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 // Test for peek() method with empty queue
 @Test(expected = IllegalStateException.class)
 public void testPeekEmptyQueue() {
 TokenQueue tokenQueue = new TokenQueue("");
 tokenQueue.peek();
 }

 // Test for peek() method with non-empty queue
 @Test
 public void testPeekNonEmptyQueue() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertEquals('a', tokenQueue.peek());
 }

 // Test for addFirst() method with null string
 @Test(expected = NullPointerException.class)
 public void testAddFirstNullString() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 tokenQueue.addFirst(null);
 }

 // Test for addFirst() method with empty string
 @Test
 public void testAddFirstEmptyString() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 tokenQueue.addFirst("");
 Assert.assertEquals("abc", tokenQueue.toString());
 }

 // Test for addFirst() method with non-empty string
 @Test
 public void testAddFirstNonEmptyString() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 tokenQueue.addFirst("def");
 Assert.assertEquals("defabc", tokenQueue.toString());
 }

 // Test for matches() method with null sequence
 @Test(expected = NullPointerException.class)
 public void testMatchesNullSeq() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 tokenQueue.matches(null);
 }

 // Test for matches() method with empty sequence
 @Test
 public void testMatchesEmptySeq() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertFalse(tokenQueue.matches(""));
 }

 // Test for matches() method with non-matching sequence
 @Test
 public void testMatchesNonMatchingSeq() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertFalse(tokenQueue.matches("def"));
 }

 // Test for matches() method with matching sequence
 @Test
 public void testMatchesMatchingSeq() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertTrue(tokenQueue.matches("abc"));
 }

 // Test for matchesCS() method with null sequence
 @Test(expected = NullPointerException.class)
 public void testMatchesCSNullSeq() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 tokenQueue.matchesCS(null);
 }

 // Test for matchesCS() method with empty sequence
 @Test
 public void testMatchesCSEmptySeq() {
 TokenQueue tokenQueue = new TokenQueue("abcABC");
 Assert.assertFalse(tokenQueue.matchesCS(""));
 }

 // Test for matchesCS() method with non-matching sequence
 @Test
 public void testMatchesCSNonMatchingSeq() {
 TokenQueue tokenQueue = new TokenQueue("abcABC");
 Assert.assertFalse(tokenQueue.matchesCS("def"));
 }

 // Test for matchesCS() method with matching sequence
 @Test
 public void testMatchesCSMatchingSeq() {
 TokenQueue tokenQueue = new TokenQueue("abcABC");
 Assert.assertTrue(tokenQueue.matchesCS("abc"));
 }

 // Test for matchesAny() method with null array
 @Test(expected = NullPointerException.class)
 public void testMatchesAnyNullArray() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 tokenQueue.matchesAny(null);
 }

 // Test for matchesAny() method with empty array
 @Test
 public void testMatchesAnyEmptyArray() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertFalse(tokenQueue.matchesAny(new String[]{}));
 }

 // Test for matchesAny() method with non-matching arrays
 @Test
 public void testMatchesAnyNonMatchingArr() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertFalse(tokenQueue.matchesAny(new String[]{"def"}));
 }

 // Test for matchesAny() method with matching arrays
 @Test
 public void testMatchesAnyMatchingArr() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertTrue(tokenQueue.matchesAny(new String[]{"a", "b", "c"}));
 }

 // Test for matchesAny() method with matching array and non-matching string
 @Test
 public void testMatchesAnyMatchingArrNonMatchingString() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertFalse(tokenQueue.matchesAny(new String[]{"a", "b", "c"}, "def"));
 }

 // Test for matchesStartTag() method with empty queue
 @Test(expected = IllegalStateException.class)
 public void testMatchesStartTagEmptyQueue() {
 TokenQueue tokenQueue = new TokenQueue("");
 tokenQueue.matchesStartTag();
 }

 // Test for matchesStartTag() method with non-matching sequence
 @Test
 public void testMatchesStartTagNonMatchingSeq() {
 TokenQueue tokenQueue = new TokenQueue("</p>");
 Assert.assertFalse(tokenQueue.matchesStartTag());
 }

 // Test for matchesStartTag() method with matching sequence
 @Test
 public void testMatchesStartTagMatchingSeq() {
 TokenQueue tokenQueue = new TokenQueue(" <p");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 // Test for matchChomp() method with empty queue
 @Test(expected = IllegalStateException.class)
 public void testMatchChompEmptyQueue() {
 TokenQueue tokenQueue = new TokenQueue("");
 tokenQueue.matchChomp("def");
 }

 // Test for matchChomp() method with non-matching sequence
 @Test
 public void testMatchChompNonMatchingSeq() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 tokenQueue.matchChomp("def");
 Assert.assertEquals("abc", tokenQueue.toString());
 }

 // Test for matchChomp() method with matching sequence
 @Test
 public void testMatchChompMatchingSeq() {
 TokenQueue tokenQueue = new TokenQueue("abcd");
 tokenQueue.matchChomp("ab");
 Assert.assertEquals("cd", tokenQueue.toString());
 }

 // Test for matchesWhitespace() method with empty queue
 @Test
 public void testMatchesWhitespaceEmptyQueue() {
 TokenQueue tokenQueue = new TokenQueue("");
 Assert.assertFalse(tokenQueue.matchesWhitespace());
 }

 // Test for matchesWhitespace() method with non-whitespace character
 @Test
 public void testMatchesWhitespaceNonWhitespaceChar() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertFalse(tokenQueue.matchesWhitespace());
 }

 // Test for matchesWhitespace() method with whitespace character
 @Test
 public void testMatchesWhitespaceWhitespaceChar() {
 TokenQueue tokenQueue = new TokenQueue(" abc");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 // Test for matchesWord() method with empty queue
 @Test
 public void testMatchesWordEmptyQueue() {
 TokenQueue tokenQueue = new TokenQueue("");
 Assert.assertFalse(tokenQueue.matchesWord());
 }

 // Test for matchesWord() method with non-word character
 @Test
 public void testMatchesWordNonWordChar() {
 TokenQueue tokenQueue = new TokenQueue("abc");
 Assert.assertFalse(tokenQueue.matchesWord());
 }

 // Test for matchesWord() method with word character
 @Test
 public void testMatchesWordWordChar() {
 TokenQueue tokenQueue = new TokenQueue("abcd");
 Assert.assertTrue(tokenQueue.matchesWord());
 }

}
import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

 @Test
 public void testIndexedStringCreation() {
 char[] values = new char[] {'a', 'b', 'c', 'a', 'b'};
 IndexedString indexedString = new IndexedString(new String(values));
 Assert.assertArrayEquals(values, indexedString.values);

 int[][] expectedIndex = new int[256][];
 for (char c = '\u0000'; c <= '\u0060'; ++c) {
 expectedIndex[c] = new int[] {0, 2, 3, 4, 4};
 }
 Assert.assertArrayEquals(expectedIndex, indexedString.index);
 }

 @Test
 public void testIndexOfChar() {
 IndexedString indexedString = new IndexedString("applepie");
 Assert.assertEquals(0, indexedString.indexOf(new char[] {'p', 'p'}));
 Assert.assertEquals(1, indexedString.indexOf(new char[] {'a', 'p'}));
 Assert.assertEquals(3, indexedString.indexOf(new char[] {'p', 'i', 'e'}));
 Assert.assertEquals(-1, indexedString.indexOf(new char[] {'z'}));
 }

 @Test
 public void testIndexOfEmptyArray() {
 IndexedString indexedString = new IndexedString("applepie");
 Assert.assertEquals(-1, indexedString.indexOf(new char[] {}));
 }

 @Test
 public void testIndexOfNullArray() {
 IndexedString indexedString = new IndexedString("applepie");
 Assert.assertNull(indexedString.indexOf(null));
 }

 @Test
 public void testIndexOfLongerTargetThanSource() {
 IndexedString indexedString = new IndexedString("applepie");
 Assert.assertEquals(-1, indexedString.indexOf(new char[] {'p', 'i', 'e', 'p'}));
 }

 @Test
 public void testIndexOfMismatchedTarget() {
 IndexedString indexedString = new IndexedString("applepie");
 Assert.assertEquals(-1, indexedString.indexOf(new char[] {'p', 'e'}));
 }

 @Test
 public void testIndexOfTargetContainsNonMatchingChar() {
 IndexedString indexedString = new IndexedString("applepie");
 Assert.assertEquals(-1, indexedString.indexOf(new char[] {'p', 'e', 'z'}));
 }
}
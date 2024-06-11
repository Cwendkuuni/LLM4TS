import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNegative;
import static org.junit.Assert.assertNull;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 @Test
 public void testCountChar() {
 char[] testValues = {'a', 'b', 'a', 'a', 'b'};
 IndexedString is = new IndexedString(new String(testValues));
 assertEquals(3, is.count('a'));
 }

 @Test
 public void testIndexOfEmptySubstring() {
 char[] target = {};
 IndexedString is = new IndexedString("abcdef");
 assertNegative(is.indexOf(target));
 }

 @Test
 public void testIndexOfSingleChar() {
 char[] target = {'a'};
 IndexedString is = new IndexedString("abcdefaa");
 assertEquals(5, is.indexOf(target));
 }

 @Test
 public void testIndexOfSubstringInBeginning() {
 char[] target = {'a', 'b'};
 IndexedString is = new IndexedString("ababcdefaa");
 assertEquals(0, is.indexOf(target));
 }

 @Test
 public void testIndexOfSubstringInMiddle() {
 char[] target = {'b', 'a', 'b'};
 IndexedString is = new IndexedString("ababcdefaa");
 assertEquals(5, is.indexOf(target));
 }

 @Test
 public void testIndexOfSubstringInEnd() {
 char[] target = {'a', 'a', 'a'};
 IndexedString is = new IndexedString("ababcdefaaa");
 assertEquals(12, is.indexOf(target));
 }

 @Test
 public void testIndexOfSubstringNotPresent() {
 char[] target = {'c', 'd'};
 IndexedString is = new IndexedString("ababcdefaa");
 assertNegative(is.indexOf(target));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 IndexedString is = new IndexedString(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOutOfBound() {
 char[] target = {'a'};
 IndexedString is = new IndexedString("ababcdefa");
 }

 @Test
 public void testIndexOfSubstringWithDifferentCases() {
 char[] target = {'A', 'b'};
 IndexedString is = new IndexedString("abABCdefaa");
 assertNull(is.indexOf(target));
 }
}
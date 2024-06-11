import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNegative;
import static org.junit.Assert.assertNull;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 @Test
 public void testConstructor() {
 char[] values = new char[]{'h', 'e', 'l', 'l', 'o'};
 IndexedString is = new IndexedString(new String(values));
 assertEquals(values.length, is.values.length);
 assertArrayEquals(values, is.values);
 }

 @Test
 public void testIndexOfChar() {
 char[] target = new char[]{'h', 'e'};
 IndexedString is = new IndexedString("hello");
 int expected = 0;
 int actual = is.indexOf(target);
 assertEquals(expected, actual);

 target = new char[]{'l', 'o'};
 expected = 4;
 actual = is.indexOf(target);
 assertEquals(expected, actual);
 }

 @Test
 public void testIndexOfEmptyArray() {
 IndexedString is = new IndexedString("hello");
 int actual = is.indexOf(new char[0][0]);
 assertNegative(actual);
 }

 @Test
 public void testIndexOfNullArray() {
 IndexedString is = new IndexedString("hello");
 int actual = is.indexOf(null);
 assertNull(actual);
 }

 @Test
 public void testIndexOfCharOutOfRange() {
 char[] target = new char[]{'z'};
 IndexedString is = new IndexedString("hello");
 int actual = is.indexOf(target);
 assertNegative(actual);
 }

 @Test
 public void testIndexOfSubArray() {
 char[] target = new char[]{'e', 'l'};
 IndexedString is = new IndexedString("hello");
 int actual = is.indexOf(target);
 assertEquals(3, actual);
 }

 @Test
 public void testIndexOfSubArrayDifferentLength() {
 char[] target = new char[]{'e', 'l'};
 IndexedString is = new IndexedString("hello");
 int actual = is.indexOf(new char[]{'h', 'e'});
 assertEquals(0, actual);
 }

 @Test
 public void testIndexOfSubArrayDifferentOrder() {
 char[] target = new char[]{'l', 'e'};
 IndexedString is = new IndexedString("hello");
 int actual = is.indexOf(new char[]{'e', 'l'});
 assertEquals(3, actual);
 }
}
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.beiri22.stringincrementor.helper.IndexedString;

class IndexedStringTest {

 @Test
 void testIndexedStringCreation() {
 char[] input = new char[]{'a', 'b', 'a', 'c', 'a', 'a'};
 IndexedString is = new IndexedString(new String(input));
 Assertions.assertArrayEquals(input, is.values);
 }

 @Test
 void testIndexCharacter() {
 IndexedString is = new IndexedString("hello");
 int count = is.index('l').length;
 Assertions.assertEquals(3, count);
 }

 @Test
 void testIndexEmptyString() {
 IndexedString is = new IndexedString("");
 int count = is.index('a').length;
 Assertions.assertEquals(0, count);
 }

 @Test
 void testIndexCharNotPresent() {
 IndexedString is = new IndexedString("hello");
 int index = is.indexOf(new char[]{'z'});
 Assertions.assertEquals(-1, index);
 }

 @Test
 void testIndexSubstring() {
 IndexedString is = new IndexedString("hello world");
 int index = is.indexOf(new char[]{'e', 'l', 'l', 'o'});
 Assertions.assertEquals(3, index);
 }

 @Test
 void testIndexStringWithLeadingAndTrailingSpaces() {
 IndexedString is = new IndexedString(" hello world ");
 int index = is.indexOf(new char[]{'e', 'l', 'l', 'o'});
 Assertions.assertEquals(3, index);
 }

 @Test
 void testIndexStringWithTrailingZeroLengthSubstring() {
 IndexedString is = new IndexedString("hello");
 int index = is.indexOf(new char[0]);
 Assertions.assertEquals(-1, index);
 }

 @Test
 void testIndexStringWithNullInput() {
 Assertions.assertThrows(NullPointerException.class, () -> new IndexedString(null));
 }
}
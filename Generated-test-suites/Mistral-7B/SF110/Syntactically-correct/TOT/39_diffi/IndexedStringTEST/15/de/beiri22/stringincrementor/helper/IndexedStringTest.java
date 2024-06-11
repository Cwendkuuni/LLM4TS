import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexedStringTest {

 private static final String TEST_STRING_1 = "abcabc";
 private static final String TEST_STRING_2 = "abcaabca";
 private static final String TEST_STRING_3 = "abcdefghijklmnopqrstuvwxyz";
 private static final char[] TEST_CHAR_ARRAY_1 = {'a', 'b', 'c'};
 private static final char[] TEST_CHAR_ARRAY_2 = {'a', 'b', 'c', 'a'};
 private static final char[] TEST_CHAR_ARRAY_3 = {'z', 'z', 'z'};
 private static final char TEST_CHAR = 'a';
 private static final char TEST_INVALID_CHAR = '\u0101'; // a character outside of the ASCII table

 @Test
 public void testConstructor() {
 IndexedString is1 = new IndexedString(TEST_STRING_1);
 IndexedString is2 = new IndexedString(TEST_STRING_2);
 IndexedString is3 = new IndexedString(TEST_STRING_3);
 }

 @Test
 public void testCount() {
 IndexedString is = new IndexedString(TEST_STRING_1);
 assertEquals(3, is.count(TEST_CHAR));
 }

 @Test
 public void testIndexOf_EmptySubstring() {
 IndexedString is = new IndexedString(TEST_STRING_1);
 assertEquals(-1, is.indexOf(new char[0]));
 }

 @Test
 public void testIndexOf_SingleCharacter() {
 IndexedString is = new IndexedString(TEST_STRING_1);
 assertEquals(0, is.indexOf(TEST_CHAR_ARRAY_1));
 }

 @Test
 public void testIndexOf_SubstringPresent() {
 IndexedString is = new IndexedString(TEST_STRING_1);
 assertEquals(1, is.indexOf(TEST_CHAR_ARRAY_2));
 }

 @Test
 public void testIndexOf_SubstringNotPresent() {
 IndexedString is = new IndexedString(TEST_STRING_1);
 assertEquals(-1, is.indexOf(TEST_CHAR_ARRAY_3));
 }

 @Test
 public void testIndexOf_InvalidCharacter() {
 IndexedString is = new IndexedString(TEST_STRING_1);
 assertEquals(-1, is.indexOf(new char[] { TEST_INVALID_CHAR }));
 }

 @Test
 public void testIndexOf_SubstringPartiallyPresent() {
 IndexedString is = new IndexedString(TEST_STRING_2);
 assertEquals(1, is.indexOf(TEST_CHAR_ARRAY_2));
 }

 @Test
 public void testIndexOf_SubstringOverlapping() {
 IndexedString is = new IndexedString(TEST_STRING_2);
 assertEquals(2, is.indexOf(TEST_CHAR_ARRAY_2));
 }
}
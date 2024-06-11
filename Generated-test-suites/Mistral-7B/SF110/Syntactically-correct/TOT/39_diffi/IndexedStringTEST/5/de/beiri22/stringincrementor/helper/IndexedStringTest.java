import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

 private IndexedString indexedString;
 private char[] targetCharArray1, targetCharArray2, targetCharArray3;

 @Before
 public void setUp() {
 char[] values = new char[]{'a', 'b', 'a', 'b', 'c', 'c', 'a'};
 indexedString = new IndexedString(new String(values));
 targetCharArray1 = new char[]{'a', 'b'};
 targetCharArray2 = new char[]{'c', 'c'};
 targetCharArray3 = new char[]{'a', 'b', 'c'};
 }

 @Test
 public void testCount() {
 assertEquals(3, indexedString.count('a'));
 assertEquals(2, indexedString.count('b'));
 assertEquals(2, indexedString.count('c'));
 assertEquals(0, indexedString.count('d'));
 }

 @Test
 public void testGetIndexedString() {
 char[] expectedIndexedValues = new char[]{'a', 'b', 'a', 'b'};
 assertArrayEquals(expectedIndexedValues, indexedString.values);
 }

 @Test
 public void testIndexOf_Success() {
 int expectedIndex = 0;
 int actualIndex = indexedString.indexOf(targetCharArray1);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOf_Failure() {
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(targetCharArray2);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexOf_MiddleOfString() {
 int expectedIndex = 2;
 indexedString = new IndexedString("abcabccab");
 int actualIndex = indexedString.indexOf(targetCharArray3);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOf_NullTarget() {
 indexedString = new IndexedString("abc");
 try {
 indexedString.indexOf(null);
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOf_TargetBeyondEndOfString() {
 indexedString = new IndexedString("abc");
 try {
 indexedString.indexOf(new char[]{'d', 'e'});
 } catch (ArrayIndexOutOfBoundsException e) {
 // pass
 }
 }
}
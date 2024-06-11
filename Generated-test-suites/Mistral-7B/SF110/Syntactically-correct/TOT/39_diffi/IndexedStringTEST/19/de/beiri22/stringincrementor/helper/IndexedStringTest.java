import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexedStringTest {

 private static final String TEST_STRING = "abracadabra";
 private IndexedString indexedString;

 @Test
 public void testIndexedStringInitialization() {
 indexedString = new IndexedString(TEST_STRING);
 assertEquals(TEST_STRING.length(), indexedString.values.length);
 }

 @Test
 public void testIndexChar() {
 char testChar = 'a';
 int expectedIndex = 5;
 int actualIndex = indexedString.indexOf(new char[]{testChar});
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexEmptyCharArray() {
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(new char[0][]);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexCharNotInString() {
 char testChar = 'z';
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(new char[]{testChar});
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexSubstring() {
 char[] target = {'a', 'b', 'r', 'a'};
 int expectedIndex = 2;
 int actualIndex = indexedString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexSubstringNotInString() {
 char[] target = {'c', 'd', 'e', 'f'};
 int expectedIndex = -1;
 int actualIndex = indexedString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexSubstringEndOfString() {
 char[] target = {'a', 'b', 'r', 'a', 'c', 'a', 'd', 'a', 'b', 'r'};
 int expectedIndex = indexedString.values.length - target.length;
 int actualIndex = indexedString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexSubstringMiddleOfString() {
 char[] target = {'a', 'b', 'r', 'a', 'c', 'a'};
 int expectedIndex = 5;
 int actualIndex = indexedString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testIndexSubstringWithRepeatedChars() {
 char[] target = {'a', 'a', 'b', 'a', 'a'};
 int expectedIndex = 2;
 int actualIndex = indexedString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }
}
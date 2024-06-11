import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 private IndexedString indexedString;

 @Before
 public void setup() {
     indexedString = new IndexedString("teststring12345teststring12345");
 }

 @Test
 public void testIndexedStringConstructor() {
     assertEquals("teststring12345teststring12345".length(), indexedString.values.length);
 }

 @Test
 public void testCount() {
     assertEquals(2, indexedString.count('t'));
     assertEquals(5, indexedString.count('s'));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testIndexedStringConstructorWithNullString() {
     new IndexedString(null);
 }

 @Test
 public void testIndexOfChar() {
     assertEquals(0, indexedString.indexOf('t'));
     assertEquals(12, indexedString.indexOf('n')); // character not present in the string
 }

 @Test
 public void testIndexOfArray() {
     char[] target = {'t', 'e', 's', 't', 's'};
     assertEquals(0, indexedString.indexOf(target));

     char[] target2 = {'s', 't', 'r', 'i', 'n', 'g'};
     assertEquals(-1, indexedString.indexOf(target2));
 }

 @Test
 public void testIndexOfEmptyArray() {
     assertEquals(-1, indexedString.indexOf(new char[0]));
 }

 @Test
 public void testIndexOfArrayWithNullElement() {
     char[] target = {'t', 'e', 's', 't', 's', null};
     assertEquals(-1, indexedString.indexOf(target));
 }

 @Test
 public void testIndexOfArrayWithLongerSubstring() {
     char[] target = {'t', 'e', 's', 't', 's', 'g'};
     assertEquals(-1, indexedString.indexOf(target));
 }
}
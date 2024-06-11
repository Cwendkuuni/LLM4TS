import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import de.beiri22.stringincrementor.helper.IndexedString;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IndexedStringTest {

 @BeforeAll
 public void setup() {
 // Empty string test
 IndexedString emptyString = new IndexedString("");
 // Single character test
 IndexedString singleCharacter = new IndexedString("A");
 // Multi-character test
 IndexedString multiCharacter = new IndexedString("ABCABC");
 }

 @Test
 public void testCountChar() {
 char testChar = 'A';
 assertEquals(1, new IndexedString("A").count(testChar));
 assertEquals(3, new IndexedString("ABCABC").count(testChar));
 }

 @Test
 public void testIndexOfEmptyString() {
 char[] emptyArray = {};
 int result = new IndexedString("").indexOf(emptyArray);
 assertEquals(-1, result);
 }

 @Test
 public void testIndexOfSingleCharacter() {
 char[] singleCharacterArray = {'A'};
 int result = new IndexedString("A").indexOf(singleCharacterArray);
 assertEquals(0, result);
 }

 @Test
 public void testIndexOfMultiCharacter() {
 char[] multiCharacterArray = {'A', 'B', 'C'};
 int result = new IndexedString("ABCABC").indexOf(multiCharacterArray);
 assertEquals(1, result);
 }

 @Test
 public void testIndexOfSubstring() {
 char[] substring = {'A', 'B', 'C'};
 int result = new IndexedString("ABCABC").indexOf(substring);
 assertEquals(2, result);
 }

 @Test
 public void testIndexOfSubstringNotPresent() {
 char[] substring = {'D', 'E', 'F'};
 int result = new IndexedString("ABCABC").indexOf(substring);
 assertEquals(-1, result);
 }

 @Test
 public void testIndexOfEmptyArray() {
 int result = new IndexedString("ABCABC").indexOf(new char[0][]);
 assertEquals(-1, result);
 }

 @Test
 public void testIndexOfNullArray() {
 int result = new IndexedString("ABCABC").indexOf(null);
 assertThrows(NullPointerException.class, () -> result);
 }

 @Test
 public void testIndexOfNullCharacterArray() {
 int result = new IndexedString("ABCABC").indexOf(null);
 assertThrows(NullPointerException.class, () -> result);
 }

 @Test
 public void testIndexOfNullCharacter() {
 char[] array = {'A', 'B', 'C'};
 int result = new IndexedString("ABCABC").indexOf(null);
 assertThrows(NullPointerException.class, () -> result);
 int result2 = new IndexedString("ABCABC").indexOf(array);
 assertEquals(2, result2);
 }
}
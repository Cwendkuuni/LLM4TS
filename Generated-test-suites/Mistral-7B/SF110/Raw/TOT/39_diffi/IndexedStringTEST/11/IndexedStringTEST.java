import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

 private char[] testArray1 = {'a', 'b', 'a', 'c', 'a'};
 private char[] testArray2 = {'a', 'b', 'c', 'a'};
 private char[] testArray3 = {'a', 'b'};
 private char[] testArray4 = {'a'};
 private char[] testArray5 = {};
 private char[] testArray6 = {'\u00E1'};
 private char[] testArray7 = {'\u0100'};
 private char[] testArray8 = {'\u0100', '\u0100'};
 private char[] testArray9 = {'\u00E1', '\u0100'};

 @Test
 public void testIndexedStringConstructor() {
 IndexedString testString1 = new IndexedString("abaaac");
 IndexedString testString2 = new IndexedString("abca");
 IndexedString testString3 = new IndexedString("ab");
 IndexedString testString4 = new IndexedString("a");
 IndexedString testString5 = new IndexedString("");
 IndexedString testString6 = new IndexedString("á");
 IndexedString testString7 = new IndexedString("Ö");
 IndexedString testString8 = new IndexedString("ÖÖ");
 IndexedString testString9 = new IndexedString("áÖ");

 assertEquals(6, testString1.values.length);
 assertEquals(5, testString1.index[92].length);
 }

 @Test
 public void testCountMethod() {
 IndexedString testString = new IndexedString("abaaac");
 assertEquals(3, testString.count('a'));
 assertEquals(1, testString.count('b'));
 assertEquals(1, testString.count('c'));
 assertEquals(0, testString.count('d'));
 }

 @Test
 public void testIndexOfMethod_Success() {
 IndexedString testString = new IndexedString("abaaac");
 char[] targetArray1 = {'a', 'b'};
 char[] targetArray2 = {'c', 'a'};
 char[] targetArray3 = {'a'};

 int result1 = testString.indexOf(targetArray1);
 int result2 = testString.indexOf(targetArray2);
 int result3 = testString.indexOf(targetArray3);

 assertEquals(1, result1);
 assertEquals(2, result2);
 assertEquals(0, result3);
 }

 @Test
 public void testIndexOfMethod_Failure() {
 IndexedString testString = new IndexedString("abaaac");
 char[] targetArray1 = {'c', 'b'};
 char[] targetArray2 = {'d', 'a'};
 char[] targetArray3 = {'e'};

 int result1 = testString.indexOf(targetArray1);
 int result2 = testString.indexOf(targetArray2);
 int result3 = testString.indexOf(targetArray3);

 assertEquals(-1, result1);
 assertEquals(-1, result2);
 assertEquals(-1, result3);
 }
}
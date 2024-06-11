import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndexedStringTest {

 private IndexedString string1;
 private IndexedString string2;

 @Test
 public void testConstructor() {
 String testString = "TestString123";
 string1 = new IndexedString(testString);
 assertEquals("TestString123".length(), string1.values.length);
 }

 @Test
 public void testIndexOfSingleCharacter() {
 char testChar = 'T';
 int index = string1.indexOf(new char[]{testChar});
 assertEquals(0, index);
 }

 @Test
 public void testIndexOfMultipleCharactersInSequence() {
 char testChar1 = 'T';
 char testChar2 = 'S';
 int index = string1.indexOf(new char[]{testChar1, testChar2});
 assertEquals(1, index);
 }

 @Test
 public void testIndexOfSubString() {
 String testSubString = "TestString";
 int index = string1.indexOf(testSubString.toCharArray());
 assertEquals(0, index);
 }

 @Test
 public void testIndexOfSubStringNotPresentInString() {
 String testSubString = "TestSubString";
 int index = string1.indexOf(testSubString.toCharArray());
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfEmptyString() {
 int index = string1.indexOf(new char[0]);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfNullString() {
 String testString = null;
 try {
 string2 = new IndexedString(testString);
 String testNullString = null;
 int index = string2.indexOf(testNullString.toCharArray());
 assertEquals(-1, index);
 } catch (NullPointerException e) {
 Assert.assertNotNull(e);
 }
 }

 @Test
 public void testIndexOfSubStringWithDifferentCase() {
 String testSubString = "testSubString";
 int index = string1.indexOf(testSubString.toCharArray());
 assertEquals(-1, index);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testIndexOfSubStringBeyondEndOfString() {
 String testSubString = "TestString";
 int index = string1.indexOf(testSubString.toCharArray());
 int endIndex = index + testSubString.length();
 int lastIndex = string1.values.length - 1;
 assertEquals(index + testSubString.length(), lastIndex);
 int testIndex = string1.indexOf(testSubString.toCharArray());
 String testSubString2 = new String(string1.values, index, testSubString.length());
 assertEquals(testSubString, testSubString2);
 String testSubString3 = new String(string1.values, endIndex, testSubString.length());
 assertEquals(testSubString, testSubString3);
 int testIndex2 = string1.indexOf(testSubString.toCharArray());
 assertEquals(testIndex, testIndex2);
 int testIndex3 = string1.indexOf(new char[0]);
 assertEquals(-1, testIndex3);

 String testString3 = "TestString123TestString123";
 IndexedString testStringIndexed = new IndexedString(testString3);
 int testIndex4 = testStringIndexed.indexOf(testSubString.toCharArray());
 assertEquals(index, testIndex4);
 int testIndex5 = testStringIndexed.indexOf(new char[0]);
 assertEquals(-1, testIndex5);

 String testString4 = "TestString123TestString123TestString123";
 IndexedString testStringIndexed2 = new IndexedString(testString4);
 int testIndex6 = testStringIndexed2.indexOf(testSubString.toCharArray());
 assertEquals(-1, testIndex6);

 String testString5 = "TestString123TestString123TestString123TestString123";
 IndexedString testStringIndexed3 = new IndexedString(testString5);
 int testIndex7 = testStringIndexed3.indexOf(testSubString.toCharArray());
 assertEquals(-1, testIndex7);

 String testString6 = "TestString123TestString123TestString123TestString123TestString123";
 IndexedString testStringIndexed4 = new IndexedString(testString6);
 int testIndex8 = testStringIndexed4.indexOf(testSubString.toCharArray());
 assertEquals(-1, testIndex8);
 }

 @Test
 public void testIndexOfSubStringAfterLastCharacterInString() {
 String testSubString = "TestString";
 int index = string1.indexOf(testSubString.toCharArray());
 int endIndex = index + testSubString.length();
 int lastIndex = string1.values.length - 1;
 assertEquals(lastIndex, endIndex - 1);
 int testIndex = string1.indexOf(testSubString.toCharArray());
 String testSubString2 = new String(string1.values, index, testSubString.length());
 assertEquals(testSubString, testSubString2);
 String testSubString3 = new String(string1.values, endIndex, testSubString.length());
 assertEquals(testSubString, testSubString3);
 int testIndex2 = string1.indexOf(new char[0]);
 assertEquals(-1, testIndex2);

 String testString3 = "TestString123TestString123";
 IndexedString testStringIndexed = new IndexedString(testString3);
 int testIndex4 = testStringIndexed.indexOf(testSubString.toCharArray());
 assertEquals(-1, testIndex4);

 String testString4 = "TestString123TestString123TestString123";
 IndexedString testStringIndexed2 = new IndexedString(testString4);
 int testIndex5 = testStringIndexed2.indexOf(testSubString.toCharArray());
 assertEquals(-1, testIndex5);

 String testString5 = "TestString123TestString123TestString123TestString123";
 IndexedString testStringIndexed3 = new IndexedString(testString5);
 int testIndex6 = testStringIndexed3.indexOf(testSubString.toCharArray());
 assertEquals(-1, testIndex6);
 }

 @Test
 public void testIndexOfSubStringAtBeginningOfString() {
 String testSubString = "TestString";
 int index = string1.indexOf(testSubString.toCharArray());
 int testIndex = string1.indexOf(testSubString.toCharArray());
 assertEquals(0, index);
 assertEquals(0, testIndex);
 }

 @Test
 public void testIndexOfSubStringAtEndOfString() {
 String testSubString = "TestString";
 int index = string1.indexOf(testSubString.toCharArray());
 int endIndex = index + testSubString.length();
 int lastIndex = string1.values.length - 1;
 assertEquals(lastIndex, endIndex - testSubString.length());
 }

 @Test
 public void testIndexOfSubStringInMiddleOfString() {
 String testSubString = "TestString";
 int index = string1.indexOf(testSubString.toCharArray());
 int startIndex = index - testSubString.length();
 int endIndex = index + testSubString.length();
 int testIndex = string1.values[startIndex];
 assertEquals(testSubString.charAt(0), testIndex);
 int testIndex2 = string1.values[endIndex - 1];
 assertEquals(testSubString.charAt(testSubString.length() - 1), testIndex2);
 }
}
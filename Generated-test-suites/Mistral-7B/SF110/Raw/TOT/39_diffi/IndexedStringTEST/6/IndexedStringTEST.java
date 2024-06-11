import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*; // for assertions
import static de.beiri22.stringincrementor.helper.IndexedString.*; // for class import

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // for class level setup and teardown

public class IndexedStringTest {

 private IndexedString testString;

 @BeforeAll // for setup before all tests in the class
 public void setupClass() {
 testString = new IndexedString("testString");
 }

 @Test // Test case for constructor
 public void testConstructor() {
 assertEquals("testString".length(), testString.values.length);
 }

 @Test // Test case for count method
 public void testCount() {
 assertEquals(5, testString.count('t'));
 assertEquals(1, testString.count('z'));
 }

 @Test // Test case for indexOf method with full match
 public void testIndexOfFullMatch() {
 char[] target = {'t', 'e', 's', 't', 'S', 't', 'r', 'i', 'n', 'g'};
 int expectedIndex = 0;
 int actualIndex = testString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test // Test case for indexOf method with partial match - no match
 public void testIndexOfPartialMatchNoMatch() {
 char[] target = {'t', 'e', 's', 't', 'S', 't', 'r', 'i', 'n', 'n'};
 int expectedIndex = -1;
 int actualIndex = testString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test // Test case for indexOf method with partial match - match in the middle
 public void testIndexOfPartialMatchMiddle() {
 char[] target = {'t', 'e', 's', 's', 't', 'r', 'i', 'n', 'g'};
 int expectedIndex = 5;
 int actualIndex = testString.indexOf(target);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test // Test case for indexOf method with empty array
 public void testIndexOfEmptyArray() {
 int expectedIndex = -1;
 int actualIndex = testString.indexOf(new char[0][]);
 assertEquals(expectedIndex, actualIndex);
 }

 @AfterAll // for teardown after all tests in the class
 public void teardownClass() {
 testString = null;
 }

}
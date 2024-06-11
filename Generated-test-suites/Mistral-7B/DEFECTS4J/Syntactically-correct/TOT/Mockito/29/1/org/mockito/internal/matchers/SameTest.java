import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;
import org.mockito. ArgumentMatchers;

public class SameTest {

 // Test case for the constructor and matches method with null object
 @Test
 public void testSameWithNullObject() {
 Same same = new Same(null);
 assertFalse(same.matches(new Object()));
 }

 // Test case for the matches method with equal objects
 @Test
 public void testSameWithEqualObjects() {
 Same same = new Same("hello");
 assertTrue(same.matches("hello"));
 }

 // Test case for the matches method with non-equal objects of different classes
 @Test
 public void testSameWithNonEqualObjects() {
 Same same = new Same(5);
 assertFalse(same.matches("five"));
 }

 // Test case for the matches method with non-equal objects of the same class
 @Test
 public void testSameWithNonEqualObjectsSameClass() {
 Same same = new Same(new Integer(5));
 assertFalse(same.matches(new Integer(6)));
 }

 // Test case for the describeTo method with String object
 @Test
 public void testDescribeToWithStringObject() {
 Description description = new Description();
 Same same = new Same("hello");
 same.describeTo(description);
 String expectedDescription = "same(\"hello\")";
 assertEquals(expectedDescription, description.toString());
 }

 // Test case for the describeTo method with Character object
 @Test
 public void testDescribeToWithCharacterObject() {
 Description description = new Description();
 Same same = new Same('h');
 same.describeTo(description);
 String expectedDescription = "same('h')";
 assertEquals(expectedDescription, description.toString());
 }
}
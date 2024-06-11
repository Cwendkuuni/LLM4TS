import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 // Test constructor with empty string
 @Test
 public void testEmptyJsonPointer() {
 JsonPointer jsonPointer = new JsonPointer();
 Assert.assertNotNull(jsonPointer);
 Assert.assertEquals("", jsonPointer.toString());
 Assert.assertTrue(jsonPointer.matches());
 Assert.assertNull(jsonPointer.getMatchingProperty());
 Assert.assertFalse(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 Assert.assertNull(jsonPointer.tail());
 }

 // Test constructor with valid JSON Pointer
 @Test
 public void testValidJsonPointer() throws IllegalArgumentException {
 String jsonPointerString = "/test/property";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 Assert.assertEquals(jsonPointerString, jsonPointer.toString());
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("property", jsonPointer.getMatchingProperty());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 Assert.assertNull(jsonPointer.tail());
 }

 // Test constructor with invalid JSON Pointer
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonPointer() {
 String jsonPointerString = "testProperty";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 }

 // Test factory method with empty array
 @Test
 public void testJsonPointerFromEmptyArray() {
 String[] segments = new String[0];
 JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
 Assert.assertEquals("", jsonPointer.toString());
 Assert.assertTrue(jsonPointer.matches());
 Assert.assertNull(jsonPointer.getMatchingProperty());
 Assert.assertFalse(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 Assert.assertNull(jsonPointer.tail());
 }

 // Test factory method with single segment
 @Test
 public void testJsonPointerFromSingleSegment() {
 String[] segments = {"test"};
 JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
 Assert.assertEquals("/test", jsonPointer.toString());
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("test", jsonPointer.getMatchingProperty());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 Assert.assertNull(jsonPointer.tail());
 }

 // Test factory method with multiple segments
 @Test
 public void testJsonPointerFromMultipleSegments() {
 String[] segments = {"test", "property"};
 JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
 Assert.assertEquals("/test/property", jsonPointer.toString());
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("property", jsonPointer.getMatchingProperty());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 Assert.assertNotNull(jsonPointer.tail());
 }

 // Test matching property
 @Test
 public void testMatchingProperty() {
 String jsonPointerString = "/test/property";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 String input = "{\"test\": {\"property\": \"value\"}}";
 Assert.assertEquals(jsonPointer.getMatchingProperty(), "property");
 Assert.assertEquals("value", JsonPointer.compile(jsonPointer.getMatchingProperty()).matchProperty(input).getMatchingProperty());
 }

 // Test matching element
 @Test
 public void testMatchingElement() {
 String jsonPointerString = "/test/0";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 String input = "[\"test1\", \"test2\", \"test3\"]";
 Assert.assertEquals(jsonPointer.getMatchingIndex(), 0);
 Assert.assertEquals("test1", JsonPointer.compile(jsonPointer.getMatchingProperty()).matchElement(input).getMatchingProperty());
 }

 // Test tail
 @Test
 public void testTail() {
 String jsonPointerString = "/test/property/subproperty";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 Assert.assertEquals("/property/subproperty", jsonPointer.tail().toString());
 }

 // Test toString
 @Test
 public void testToString() {
 String jsonPointerString = "/test/property/subproperty";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 Assert.assertEquals(jsonPointerString, jsonPointer.toString());
 }

 // Test hashCode and equals
 @Test
 public void testHashCodeAndEquals() {
 String jsonPointerString1 = "/test/property/subproperty";
 String jsonPointerString2 = "/test/property/subproperty";
 JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerString1);
 JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerString2);
 Assert.assertEquals(jsonPointer1, jsonPointer2);
 Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }

 // Test hashCode and not equals
 @Test
 public void testHashCodeAndNotEquals() {
 String jsonPointerString1 = "/test/property/subproperty";
 String jsonPointerString2 = "/test/property/subproperty/subsubproperty";
 JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerString1);
 JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerString2);
 Assert.assertNotEquals(jsonPointer1, jsonPointer2);
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }
}
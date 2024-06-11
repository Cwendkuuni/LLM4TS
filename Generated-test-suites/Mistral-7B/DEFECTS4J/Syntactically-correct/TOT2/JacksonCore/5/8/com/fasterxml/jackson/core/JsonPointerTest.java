import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 // Test for empty constructor
 @Test
 public void testEmptyJsonPointer() {
 JsonPointer jsonPointer = new JsonPointer();
 assertTrue(jsonPointer.matches());
 assertEquals("", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 }

 // Test for constructor with valid input
 @Test
 public void testJsonPointerWithValidInput() throws IllegalArgumentException {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertFalse(jsonPointer.matches());
 assertEquals("foo", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 // Test for factory method with empty segments
 @Test
 public void testJsonPointerWithEmptySegments() {
 String[] segments = {};
 JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
 assertEquals(JsonPointer.EMPTY, jsonPointer);
 }

 // Test for factory method with valid segments
 @Test
 public void testJsonPointerWithValidSegments() {
 String[] segments = {"foo", "bar", "1"};
 JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
 assertFalse(jsonPointer.matches());
 assertEquals("foo", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 // Test for matches method with empty JsonPointer
 @Test
 public void testMatchesWithEmptyJsonPointer() {
 JsonPointer jsonPointer1 = new JsonPointer();
 JsonPointer jsonPointer2 = new JsonPointer();
 assertTrue(jsonPointer1.matches());
 assertTrue(jsonPointer2.matches());
 assertTrue(jsonPointer1.matches(jsonPointer2));
 }

 // Test for matches method with non-empty JsonPointer
 @Test
 public void testMatchesWithNonEmptyJsonPointer() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer1 = JsonPointer.compile(input);
 String input2 = "/foo/bar/2";
 JsonPointer jsonPointer2 = JsonPointer.compile(input2);
 assertFalse(jsonPointer1.matches());
 assertFalse(jsonPointer2.matches());
 assertFalse(jsonPointer1.matches(jsonPointer2));
 }

 // Test for matches method with overlapping JsonPointer
 @Test
 public void testMatchesWithOverlappingJsonPointer() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer1 = JsonPointer.compile(input);
 String input2 = "/foo/bar/1/baz";
 JsonPointer jsonPointer2 = JsonPointer.compile(input2);
 assertFalse(jsonPointer1.matches());
 assertTrue(jsonPointer1.matches(jsonPointer2));
 }

 // Test for getMatchingProperty method with empty JsonPointer
 @Test
 public void testGetMatchingPropertyWithEmptyJsonPointer() {
 JsonPointer jsonPointer = new JsonPointer();
 assertEquals("", jsonPointer.getMatchingProperty());
 }

 // Test for getMatchingProperty method with non-empty JsonPointer
 @Test
 public void testGetMatchingPropertyWithNonEmptyJsonPointer() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals("bar", jsonPointer.getMatchingProperty());
 }

 // Test for getMatchingIndex method with empty JsonPointer
 @Test
 public void testGetMatchingIndexWithEmptyJsonPointer() {
 JsonPointer jsonPointer = new JsonPointer();
 assertEquals(-1, jsonPointer.getMatchingIndex());
 }

 // Test for getMatchingIndex method with non-empty JsonPointer
 @Test
 public void testGetMatchingIndexWithNonEmptyJsonPointer() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 // Test for matchProperty method with matching property
 @Test
 public void testMatchPropertyWithMatchingProperty() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 String matchingProperty = "bar";
 JsonPointer expectedJsonPointer = new JsonPointer("/foo/bar");
 assertTrue(jsonPointer.mayMatchProperty());
 assertEquals(expectedJsonPointer, jsonPointer.matchProperty(matchingProperty));
 }

 // Test for matchProperty method with non-matching property
 @Test
 public void testMatchPropertyWithNonMatchingProperty() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 String nonMatchingProperty = "baz";
 assertNull(jsonPointer.matchProperty(nonMatchingProperty));
 }

 // Test for matchElement method with matching index
 @Test
 public void testMatchElementWithMatchingIndex() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 int matchingIndex = 0;
 JsonPointer expectedJsonPointer = new JsonPointer("/foo/bar");
 assertTrue(jsonPointer.mayMatchElement());
 assertEquals(expectedJsonPointer, jsonPointer.matchElement(matchingIndex));
 }

 // Test for matchElement method with non-matching index
 @Test
 public void testMatchElementWithNonMatchingIndex() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 int nonMatchingIndex = 1;
 assertNull(jsonPointer.matchElement(nonMatchingIndex));
 }

 // Test for tail method with empty JsonPointer
 @Test
 public void testTailWithEmptyJsonPointer() {
 JsonPointer jsonPointer = new JsonPointer();
 assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
 }

 // Test for tail method with non-empty JsonPointer
 @Test
 public void testTailWithNonEmptyJsonPointer() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertFalse(jsonPointer.matches());
 assertEquals("/bar/1", jsonPointer.tail().toString());
 }

 // Test for toString method with empty JsonPointer
 @Test
 public void testToStringWithEmptyJsonPointer() {
 JsonPointer jsonPointer = new JsonPointer();
 assertEquals("", jsonPointer.toString());
 }

 // Test for toString method with non-empty JsonPointer
 @Test
 public void testToStringWithNonEmptyJsonPointer() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals(input, jsonPointer.toString());
 }

 // Test for equals method with same JsonPointer
 @Test
 public void testEqualsWithSameJsonPointer() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer1 = JsonPointer.compile(input);
 JsonPointer jsonPointer2 = JsonPointer.compile(input);
 assertTrue(jsonPointer1.equals(jsonPointer2));
 }

 // Test for equals method with different JsonPointer
 @Test
 public void testEqualsWithDifferentJsonPointer() {
 String input1 = "/foo/bar/1";
 String input2 = "/foo/bar/2";
 JsonPointer jsonPointer1 = JsonPointer.compile(input1);
 JsonPointer jsonPointer2 = JsonPointer.compile(input2);
 assertFalse(jsonPointer1.equals(jsonPointer2));
 }

 // Test for equals method with null input
 @Test
 public void testEqualsWithNullInput() {
 JsonPointer jsonPointer = new JsonPointer();
 assertFalse(jsonPointer.equals(null));
 }

 // Test for equals method with different object type
 @Test
 public void testEqualsWithDifferentObjectType() {
 JsonPointer jsonPointer = new JsonPointer();
 assertFalse(jsonPointer.equals(new Object()));
 }
}
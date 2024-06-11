import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @org.junit.Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 assertEquals("", emptyPointer.toString());
 }

 @org.junit.Test
 public void testConstructWithEmptyString() {
 JsonPointer pointer = new JsonPointer("");
 assertTrue(pointer.matches());
 assertEquals("", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @org.junit.Test
 public void testConstructWithSingleSegment() {
 JsonPointer pointer = JsonPointer.compile("/test");
 assertFalse(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/test", pointer.toString());
 }

 @org.junit.Test
 public void testConstructWithMultipleSegments() {
 JsonPointer pointer = JsonPointer.compile("/test/property");
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/test/property", pointer.toString());
 }

 @org.junit.Test
 public void testConstructWithMultipleSegmentsAndIndex() {
 JsonPointer pointer = JsonPointer.compile("/test/0/property");
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/test/0/property", pointer.toString());
 }

 @org.junit.Test
 public void testConstructWithInvalidInput() {
 try {
 JsonPointer pointer = JsonPointer.compile("invalid");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid input: JSON Pointer expression must start with '/': \"invalid\"", e.getMessage());
 }
 }

 @org.junit.Test
 public void testMatches() {
 JsonPointer pointer1 = JsonPointer.compile("/test");
 JsonPointer pointer2 = JsonPointer.compile("/test");
 JsonPointer pointer3 = JsonPointer.compile("/test/property");
 assertTrue(pointer1.matches());
 assertTrue(pointer2.matches());
 assertFalse(pointer3.matches());
 }

 @org.junit.Test
 public void testGetMatchingProperty() {
 JsonPointer pointer1 = JsonPointer.compile("/test");
 JsonPointer pointer2 = JsonPointer.compile("/test/property");
 assertEquals("test", pointer1.getMatchingProperty());
 assertEquals("property", pointer2.getMatchingProperty());
 }

 @org.junit.Test
 public void testGetMatchingIndex() {
 JsonPointer pointer1 = JsonPointer.compile("/test");
 JsonPointer pointer2 = JsonPointer.compile("/test/0/property");
 assertEquals(-1, pointer1.getMatchingIndex());
 assertEquals(0, pointer2.getMatchingIndex());
 }

 @org.junit.Test
 public void testMayMatchProperty() {
 JsonPointer pointer1 = JsonPointer.compile("/test");
 JsonPointer pointer2 = JsonPointer.compile("/test/property");
 assertTrue(pointer1.mayMatchProperty());
 assertTrue(pointer2.mayMatchProperty());
 }

 @org.junit.Test
 public void testMayMatchElement() {
 JsonPointer pointer1 = JsonPointer.compile("/test");
 JsonPointer pointer2 = JsonPointer.compile("/test/0/property");
 assertFalse(pointer1.mayMatchElement());
 assertTrue(pointer2.mayMatchElement());
 }

 @org.junit.Test
 public void testMatchProperty() {
 JsonPointer pointer1 = JsonPointer.compile("/test");
 JsonPointer pointer2 = JsonPointer.compile("/test/property");
 JsonPointer matchedPointer = pointer1.matchProperty("test");
 assertEquals(pointer1, matchedPointer);
 JsonPointer matchedPointer2 = pointer2.matchProperty("property");
 assertEquals(pointer2, matchedPointer2);
 JsonPointer nullPointer = JsonPointer.EMPTY;
 assertNull(nullPointer.matchProperty("test"));
 }

 @org.junit.Test
 public void testMatchElement() {
 JsonPointer pointer1 = JsonPointer.compile("/test/0/property");
 JsonPointer matchedPointer = pointer1.matchElement(0);
 assertEquals(pointer1, matchedPointer);
 JsonPointer nullPointer = JsonPointer.EMPTY;
 assertNull(nullPointer.matchElement(0));
 }

 @org.junit.Test
 public void testTail() {
 JsonPointer pointer1 = JsonPointer.compile("/test");
 JsonPointer pointer2 = JsonPointer.compile("/test/property");
 assertEquals(pointer1, pointer1.tail());
 assertEquals(pointer2, pointer2.tail());
 }

 @org.junit.Test
 public void testToString() {
 JsonPointer pointer1 = JsonPointer.compile("/test");
 JsonPointer pointer2 = JsonPointer.compile("/test/property");
 assertEquals("/test", pointer1.toString());
 assertEquals("/test/property", pointer2.toString());
 }

 @org.junit.Test
 public void testHashCodeAndEquals() {
 JsonPointer pointer1 = JsonPointer.compile("/test");
 JsonPointer pointer2 = JsonPointer.compile("/test");
 JsonPointer pointer3 = JsonPointer.compile("/test/property");
 assertEquals(pointer1.hashCode(), pointer2.hashCode());
 assertTrue(pointer1.equals(pointer2));
 assertFalse(pointer1.equals(pointer3));
 }
}
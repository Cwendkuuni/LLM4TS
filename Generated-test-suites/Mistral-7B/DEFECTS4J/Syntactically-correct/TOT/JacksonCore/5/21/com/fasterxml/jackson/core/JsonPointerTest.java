import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer empty = JsonPointer.EMPTY;
 assertTrue(empty.matches());
 assertNull(empty.getMatchingProperty());
 assertFalse(empty.mayMatchProperty());
 assertFalse(empty.mayMatchElement());
 assertNull(empty.tail());
 assertEquals("", empty.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/test/property");
 assertTrue(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/test/property", pointer.toString());
 }

 @Test
 public void testNestedPointer() {
 JsonPointer pointer = JsonPointer.compile("/test/array/1/nested/property");
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(1, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertEquals("/nested", tail.getMatchingProperty());
 assertNull(tail.getMatchingIndex());
 assertFalse(tail.matches());
 assertEquals("/test/array/1/nested", tail.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer pointer = JsonPointer.compile("invalid");
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInputPointer() {
 JsonPointer pointer = JsonPointer.compile("");
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInputLastSegmentPointer() {
 JsonPointer pointer = JsonPointer.compile("/test/");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testFromSegment() {
 String[] segments = {"test", "array", "1", "nested"};
 JsonPointer pointer = JsonPointer.fromSegment(segments);
 assertEquals("/test/array/1/nested", pointer.toString());
 }
}
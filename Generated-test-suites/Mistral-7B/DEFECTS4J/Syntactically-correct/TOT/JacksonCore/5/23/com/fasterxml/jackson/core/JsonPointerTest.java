import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/a/b/c");
 assertTrue(pointer.matches());
 assertEquals("c", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/a/b/c", pointer.toString());
 }

 @Test
 public void testPropertyPointer() {
 JsonPointer pointer = JsonPointer.compile("/a/b");
 assertTrue(pointer.matches());
 assertEquals("b", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/a/b", pointer.toString());
 }

 @Test
 public void testElementPointer() {
 JsonPointer pointer = JsonPointer.compile("/a/0");
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchElement());
 assertEqualTo(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("/a/0", pointer.toString());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer pointer = JsonPointer.compile("a/b"); // no leading slash
 throw new AssertionError("Should have thrown IllegalArgumentException");
 }

 @Test (expected = IllegalArgumentException.class)
 public void testInvalidElementPointer() {
 JsonPointer pointer = JsonPointer.compile("/a/x"); // invalid index
 throw new AssertionError("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testMatchingProperty() {
 JsonPointer pointer1 = JsonPointer.compile("/a/b");
 JsonPointer pointer2 = JsonPointer.compile("/a/b/c");
 JsonPointer matchingPointer = pointer1.matchProperty("b");
 assertEquals(pointer2, matchingPointer);
 }

 @Test
 public void testMatchingElement() {
 JsonPointer pointer1 = JsonPointer.compile("/a/0");
 JsonPointer pointer2 = JsonPointer.compile("/a/1");
 JsonPointer matchingPointer = pointer1.matchElement(0);
 assertEquals(pointer1, matchingPointer);
 }

 @Test
 public void testTail() {
 JsonPointer pointer1 = JsonPointer.compile("/a/b/c");
 JsonPointer tailPointer = pointer1.tail();
 assertEquals(pointer1.tail(), tailPointer);
 }
}
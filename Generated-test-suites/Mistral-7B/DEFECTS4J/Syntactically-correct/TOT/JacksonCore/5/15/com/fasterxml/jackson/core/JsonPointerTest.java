import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertEquals("", pointer.toString());
 assertEquals(0, pointer.hashCode());
 assertNotSame(pointer, new Object());
 assertNull(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/pointers/0");
 assertEquals("/pointers/0", pointer.toString());
 assertNull(pointer.matches());
 assertEquals("pointers", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNotNull(pointer.tail());
 }

 @Test
 public void testMultiLevelPointer() {
 JsonPointer pointer = JsonPointer.compile("/pointers/0/elements/1");
 assertEquals("/pointers/0/elements/1", pointer.toString());
 assertNull(pointer.matches());
 assertEquals("pointers", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNotNull(pointer.tail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerNoSlash() {
 JsonPointer pointer = JsonPointer.compile("pointers0");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerDottedSegment() {
 JsonPointer pointer = JsonPointer.compile("/pointers/0.1");
 }

 @Test
 public void testSimpleMatch() {
 JsonPointer pointer1 = JsonPointer.compile("/pointers/0");
 JsonPointer pointer2 = JsonPointer.compile("/pointers/0");
 JsonPointer pointer3 = JsonPointer.compile("/pointers/1");
 assertEquals(pointer1, pointer2);
 assertNotSame(pointer1, pointer3);
 }

 @Test
 public void testMultiLevelMatch() {
 JsonPointer pointer1 = JsonPointer.compile("/pointers/0/elements/1");
 JsonPointer pointer2 = JsonPointer.compile("/pointers/0/elements/1");
 JsonPointer pointer3 = JsonPointer.compile("/pointers/0/elements/2");
 assertEquals(pointer1, pointer2);
 assertNotSame(pointer1, pointer3);
 }

 @Test
 public void testMatches() {
 JsonPointer pointer = JsonPointer.compile("/pointers/0/elements/1");
 assertTrue(pointer.matches());
 assertTrue(pointer.matches());
 assertFalse(JsonPointer.EMPTY.matches());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer = JsonPointer.compile("/pointers/0/elements/1");
 assertEquals("pointers", pointer.matchProperty("pointers"));
 assertNull(pointer.matchProperty("elements"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer = JsonPointer.compile("/pointers/0/elements/1");
 assertEquals(1, pointer.matchElement(1));
 assertNull(pointer.matchElement(2));
 }

 @Test
 public void testTail() {
 JsonPointer pointer = JsonPointer.compile("/pointers/0/elements/1");
 assertEquals("/pointers/0/elements", pointer.tail().toString());
 }

 @Test
 public void testToString() {
 JsonPointer pointer = JsonPointer.compile("/pointers/0/elements/1");
 assertEquals("/pointers/0/elements/1", pointer.toString());
 }

 @Test
 public void testHashCode() {
 JsonPointer pointer1 = JsonPointer.compile("/pointers/0/elements/1");
 JsonPointer pointer2 = JsonPointer.compile("/pointers/0/elements/1");
 assertEquals(pointer1.hashCode(), pointer2.hashCode());
 JsonPointer pointer3 = JsonPointer.compile("/pointers/0/elements/2");
 assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
 }

 @Test
 public void testEquals() {
 JsonPointer pointer1 = JsonPointer.compile("/pointers/0/elements/1");
 JsonPointer pointer2 = JsonPointer.compile("/pointers/0/elements/1");
 JsonPointer pointer3 = JsonPointer.compile("/pointers/0/elements/2");
 assertEquals(pointer1, pointer2);
 assertNotEquals(pointer1, pointer3);
 assertNotEquals(pointer1, new Object());
 }

}
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 }

 @Test
 public void testBasicPointer() {
 String pointerString = "/a/0";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertTrue(pointer.matches());
 assertEquals("a", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 }

 @Test
 public void testNestedPointer() {
 String pointerString = "/a/0/b/1";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer tail = pointer.tail();
 assertTrue(tail.matches());
 assertEquals("b", tail.getMatchingProperty());
 assertEquals(1, tail.getMatchingIndex());
 assertNull(tail.tail());
 assertEquals("/a/0", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String invalidPointerString = "~a";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(invalidPointerString);
 } catch (IllegalArgumentException e) {
 pointer = e.getCause();
 }
 assertEquals("Invalid input: JSON Pointer expression must start with '/': \"~a\"", pointer.getMessage());
 }

 @Test
 public void testQuotedPointer() {
 String pointerString = "/a/~1/b/~0";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer tail = pointer.tail();
 JsonPointer tail2 = tail.tail();
 assertTrue(tail.matches());
 assertEquals("b", tail.getMatchingProperty());
 assertEquals(0, tail.getMatchingIndex());
 assertTrue(tail2.matches());
 assertEquals("~1", tail2.getMatchingProperty());
 assertEquals(1, tail2.getMatchingIndex());
 assertNull(tail2.tail());
 assertEquals("/a/~1/b", pointer.toString());
 }

 @Test
 public void testPointerWithProperty() {
 String pointerString = "/a/b";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer tail = pointer.tail();
 assertTrue(tail.matches());
 assertEquals("b", tail.getMatchingProperty());
 assertEquals(-1, tail.getMatchingIndex());
 assertNull(tail.tail());
 assertEquals("/a/b", pointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 String pointerString = "/a/1";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer tail = pointer.tail();
 assertTrue(tail.matches());
 assertEquals("", tail.getMatchingProperty());
 assertEquals(1, tail.getMatchingIndex());
 assertNull(tail.tail());
 assertEquals("/a/1", pointer.toString());
 }

 @Test
 public void testPointerWithEmptySegment() {
 String pointerString = "/a//b";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer tail = pointer.tail();
 assertTrue(tail.matches());
 assertEquals("b", tail.getMatchingProperty());
 assertEquals(-1, tail.getMatchingIndex());
 assertNull(tail.tail());
 assertEquals("/a/b", pointer.toString());
 }

}
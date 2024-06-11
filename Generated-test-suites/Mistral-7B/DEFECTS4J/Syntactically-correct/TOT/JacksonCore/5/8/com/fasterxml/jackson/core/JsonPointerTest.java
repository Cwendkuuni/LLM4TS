import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testBasicPointer() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/foo/bar", pointer.toString());
 }

 @Test
 public void testElementPointer() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(0, pointer.getMatchingIndex());
 assertEquals("/foo/0", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer pointer = JsonPointer.compile("foo");
 fail("Expected IllegalArgumentException but got: " + pointer);
 }

 @Test
 public void testTailPointer() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar/");
 JsonPointer tailPointer = pointer.tail();
 assertNotNull(tailPointer);
 assertEquals("/bar", tailPointer.toString());
 }

 @Test
 public void testMatchesProperty() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 JsonPointer matchingPointer = new JsonPointer("/foo/bar");
 assertTrue(pointer.matchesProperty(matchingPointer.getMatchingProperty()));
 assertEquals(matchingPointer, pointer.tail());
 }

 @Test
 public void testMatchesElement() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 JsonPointer matchingPointer = new JsonPointer("/foo/1");
 assertNull(pointer.matchElement(matchingPointer.getMatchingIndex()));
 assertNull(matchingPointer.tail());
 }
}
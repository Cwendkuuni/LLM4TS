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
 public void testValidPointer() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar/0");
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals("/foo/bar/0", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer pointer = JsonPointer.compile("baz");
 fail("Expected an IllegalArgumentException");
 }

 @Test
 public void testMatchesProperty() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 JsonPointer matchProperty = pointer.matchProperty("bar");
 assertEquals(pointer, matchProperty);
 }

 @Test
 public void testMatchesElementWithIndex() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 JsonPointer matchElement = pointer.matchElement(0);
 assertEquals(pointer, matchElement);
 }

 @Test
 public void testMatchesElementWithWrongIndex() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 JsonPointer matchElement = pointer.matchElement(1);
 assertNull(matchElement);
 }

 @Test
 public void testMatchesElementWithNegativeIndex() {
 JsonPointer pointer = JsonPointer.compile("/foo/1/-1");
 JsonPointer matchElement = pointer.matchElement(-1);
 assertEquals(pointer, matchElement);
 }

 @Test
 public void testMatchesElementWithOutOfBoundsIndex() {
 JsonPointer pointer = JsonPointer.compile("/foo/1/2");
 JsonPointer matchElement = pointer.matchElement(2);
 assertNull(matchElement);
 }

 @Test
 public void testMatchesNullPointer() {
 JsonPointer pointer = JsonPointer.compile("/foo");
 assertFalse(pointer.matches());
 }

 @Test
 public void testEquals() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/bar/0");
 JsonPointer pointer2 = JsonPointer.compile("/foo/bar/0");
 JsonPointer pointer3 = JsonPointer.compile("/foo/qux/0");
 JsonPointer pointer4 = JsonPointer.EMPTY;
 assertTrue(pointer1.equals(pointer1));
 assertTrue(pointer1.equals(pointer2));
 assertFalse(pointer1.equals(pointer3));
 assertFalse(pointer1.equals(pointer4));
 }
}
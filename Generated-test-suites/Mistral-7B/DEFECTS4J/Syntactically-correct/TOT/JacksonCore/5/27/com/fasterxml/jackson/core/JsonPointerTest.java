import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/foo");
 assertTrue(pointer.matches());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo", pointer.toString());
 }

 @Test
 public void testPointerWithProperty() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 assertFalse(pointer.matches());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals("/foo", pointer.tail().toString());
 }

 @Test
 public void testPointerWithIndex() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 assertFalse(pointer.matches());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(0, pointer.getMatchingIndex());
 assertEquals("/foo", pointer.tail().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer.compile("baz");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerWithSlash() {
 JsonPointer.compile("//foo");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerWithTwoSlashes() {
 JsonPointer.compile("/foo//bar");
 }

 @Test
 public void testComposePointer() {
 JsonPointer pointer1 = JsonPointer.compile("/foo");
 JsonPointer pointer2 = JsonPointer.compile("/bar");
 JsonPointer pointer3 = JsonPointer.compile(pointer1.toString() + "/" + pointer2.toString());
 JsonPointer pointer4 = JsonPointer.compile(pointer1.toString() + pointer2.toString());
 assertEquals(pointer3.toString(), pointer4.toString());
 }

 @Test
 public void testTailPointer() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/bar/baz");
 JsonPointer pointer2 = pointer1.tail();
 assertEquals("/foo/bar", pointer2.toString());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
 String property = "bar";
 JsonPointer matchedPointer = pointer1.matchProperty(property);
 assertEquals(pointer1.toString(), matchedPointer.toString());
 }

 @Test
 public void testMatchPropertyNotFound() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
 String property = "qux";
 assertNull(pointer1.matchProperty(property));
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/0");
 int index = 0;
 JsonPointer matchedPointer = pointer1.matchElement(index);
 assertEquals(pointer1.toString(), matchedPointer.toString());
 }

 @Test
 public void testMatchElementNotFound() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/0");
 int index = 1;
 assertNull(pointer1.matchElement(index));
 }

 @Test
 public void testMatchElementNegativeIndex() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/1");
 int index = -1;
 assertNull(pointer1.matchElement(index));
 }
}
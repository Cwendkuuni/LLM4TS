import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertEquals("", emptyPointer.toString());
 assertNull(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertNull(emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 }

 @Test
 public void testSimplePointer() {
 String pointerString = "/foo";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals("/foo", pointer.toString());
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertNull(pointer.tail());
 }

 @Test
 public void testPropertyPointer() {
 String pointerString = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals("/foo/bar", pointer.toString());
 assertNull(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertEquals("/foo", tail.toString());
 }

 @Test
 public void testIndexPointer() {
 String pointerString = "/foo/0";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals("/foo/0", pointer.toString());
 assertNull(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertEquals("/foo", tail.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String pointerString = "~foo";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInputPointer() {
 String pointerString = "";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFirstCharacterPointer() {
 String pointerString = "bar/";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 }

 @Test
 public void testCompositePointer() {
 String pointerString = "/foo/bar/0";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals("/foo/bar/0", pointer.toString());
 assertNull(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertEquals("/foo/bar", tail.toString());
 JsonPointer tailTail = tail.tail();
 assertEquals("/foo", tailTail.toString());
 }

}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertEquals("", emptyPointer.toString());
 assertEquals(emptyPointer, emptyPointer.tail());
 assertNull(emptyPointer.matchesProperty(""));
 assertNull(emptyPointer.matchesElement(-1));
 }

 @Test
 public void testBasicPointer() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 assertEquals("/foo/0", pointer.toString());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.matches());
 }

 @Test
 public void testSimplePropertyMatch() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
 JsonPointer pointer2 = JsonPointer.compile("/foo");
 JsonPointer matched = pointer1.matchProperty("bar");
 assertEquals(pointer2, matched);
 }

 @Test
 public void testSimpleElementMatch() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/0");
 JsonPointer pointer2 = JsonPointer.compile("/foo");
 JsonPointer matched = pointer1.matchElement(0);
 assertEquals(pointer2, matched);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerStartsWithSlash() {
 JsonPointer.compile("//foo");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerNoSlash() {
 JsonPointer.compile("foo");
 }

 @Test
 public void testQuotedPointer() {
 JsonPointer pointer = JsonPointer.compile("/foo/~0/bar");
 assertEquals("/foo/0/bar", pointer.toString());
 assertEquals("0", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 }

 @Test
 public void testTailPointer() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/bar/baz");
 JsonPointer pointer2 = pointer1.tail();
 assertEquals("/bar/baz", pointer2.toString());
 }

 @Test
 public void testMatches() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 assertTrue(pointer.matches());
 pointer = JsonPointer.compile("/foo/1");
 assertFalse(pointer.matches());
 }

 @Test
 public void testPropertyMatches() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 JsonPointer matched = pointer.matchProperty("bar");
 assertEquals(pointer, matched);
 }

 @Test
 public void testElementMatches() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 JsonPointer matched = pointer.matchElement(0);
 assertEquals(pointer, matched);
 }

 @Test
 public void testNullPointer() {
 JsonPointer pointer = null;
 assertNull(pointer);
 }
}
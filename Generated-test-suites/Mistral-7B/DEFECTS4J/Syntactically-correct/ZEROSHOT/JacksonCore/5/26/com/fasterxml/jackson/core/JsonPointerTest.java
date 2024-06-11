import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertEquals("", emptyPointer.toString());
 assertNull(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertNull(emptyPointer.getMatchingIndex());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 }

 @Test
 public void testBasicConstruction() {
 String fullString = "/a/b/c";
 String segment = "b";
 JsonPointer pointer = new JsonPointer(fullString, segment, null);
 assertEquals(fullString, pointer.toString());
 assertEquals(segment, pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 }

 @Test
 public void testMatches() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/c", "c", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/d", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b/c", "b", pointer2);
 JsonPointer pointer5 = new JsonPointer("/a", "a", null);
 JsonPointer pointer6 = JsonPointer.EMPTY;

 assertEquals(false, pointer1.matches());
 assertEquals(true, pointer2.matches());
 assertEquals(true, pointer4.matches());
 assertEquals(true, pointer5.matches());
 assertEquals(true, pointer6.matches());
 }

 @Test
 public void testGetMatchingProperty() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/c", "c", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/d", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b/c", "b", pointer2);
 JsonPointer pointer5 = new JsonPointer("/a", "a", null);
 JsonPointer pointer6 = JsonPointer.EMPTY;

 assertEquals("b", pointer1.getMatchingProperty());
 assertEquals(null, pointer2.getMatchingProperty());
 assertEquals(null, pointer3.getMatchingProperty());
 assertEquals("b", pointer4.getMatchingProperty());
 assertEquals("a", pointer5.getMatchingProperty());
 assertEquals(null, pointer6.getMatchingProperty());
 }

 @Test
 public void testGetMatchingIndex() {
 JsonPointer pointer1 = new JsonPointer("/a/b/0", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/1", "b", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/-1", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b", "b", null);
 JsonPointer pointer5 = new JsonPointer("/a/c", "b", null);
 JsonPointer pointer6 = new JsonPointer("/a", "b", null);
 JsonPointer pointer7 = JsonPointer.EMPTY;

 assertEquals(0, pointer1.getMatchingIndex());
 assertEquals(1, pointer2.getMatchingIndex());
 assertEquals(-1, pointer3.getMatchingIndex());
 assertEquals(null, pointer4.getMatchingIndex());
 assertEquals(null, pointer5.getMatchingIndex());
 assertEquals(-1, pointer6.getMatchingIndex());
 assertEquals(-1, pointer7.getMatchingIndex());
 }

 @Test
 public void testMayMatchProperty() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/c", "c", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/d", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b/c", "b", pointer2);
 JsonPointer pointer5 = new JsonPointer("/a", "a", null);
 JsonPointer pointer6 = JsonPointer.EMPTY;

 assertEquals(true, pointer1.mayMatchProperty());
 assertEquals(false, pointer2.mayMatchProperty());
 assertEquals(true, pointer4.mayMatchProperty());
 assertEquals(false, pointer5.mayMatchProperty());
 assertEquals(false, pointer6.mayMatchProperty());
 }

 @Test
 public void testMayMatchElement() {
 JsonPointer pointer1 = new JsonPointer("/a/b/0", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/1", "b", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/-1", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b", "b", null);
 JsonPointer pointer5 = new JsonPointer("/a/c", "b", null);
 JsonPointer pointer6 = new JsonPointer("/a", "b", null);
 JsonPointer pointer7 = JsonPointer.EMPTY;

 assertEquals(false, pointer1.mayMatchElement());
 assertEquals(true, pointer2.mayMatchElement());
 assertEquals(false, pointer3.mayMatchElement());
 assertEquals(null, pointer4.getMatchingIndex());
 assertEquals(null, pointer5.mayMatchElement());
 assertEquals(false, pointer6.mayMatchElement());
 assertEquals(false, pointer7.mayMatchElement());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/c", "c", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/d", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b/c", "b", pointer2);
 JsonPointer pointer5 = new JsonPointer("/a", "a", null);
 JsonPointer pointer6 = JsonPointer.EMPTY;

 assertEquals(pointer2, pointer1.matchProperty("b"));
 assertEquals(null, pointer1.matchProperty("c"));
 assertEquals(pointer3, pointer1.matchProperty("b"));
 assertEquals(null, pointer1.matchProperty("d"));
 assertEquals(null, pointer1.matchProperty("a"));
 assertEquals(null, pointer1.matchProperty("e"));
 assertEquals(pointer2, pointer4.matchProperty("b"));
 assertEquals(null, pointer4.matchProperty("c"));
 assertEquals(null, pointer5.matchProperty("a"));
 assertEquals(null, pointer5.matchProperty("b"));
 assertEquals(null, pointer6.matchProperty("a"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer1 = new JsonPointer("/a/b/0", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/1", "b", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/-1", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b", "b", null);
 JsonPointer pointer5 = new JsonPointer("/a/c", "b", null);
 JsonPointer pointer6 = new JsonPointer("/a", "b", null);
 JsonPointer pointer7 = JsonPointer.EMPTY;

 assertEquals(pointer2, pointer1.matchElement(0));
 assertEquals(null, pointer1.matchElement(1));
 assertEquals(null, pointer1.matchElement(-1));
 assertEquals(null, pointer1.matchElement(2));
 assertEquals(null, pointer4.matchElement(0));
 assertEquals(null, pointer5.matchElement(0));
 assertEquals(null, pointer6.matchElement(0));
 assertEquals(null, pointer7.matchElement(0));
 }

 @Test
 public void testTail() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/c", "c", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/d", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b/c", "b", pointer2);
 JsonPointer pointer5 = new JsonPointer("/a", "a", null);
 JsonPointer pointer6 = JsonPointer.EMPTY;

 assertEquals(pointer2, pointer1.tail());
 assertEquals(null, pointer2.tail());
 assertEquals(pointer3, pointer3.tail());
 assertEquals(pointer2, pointer4.tail());
 assertEquals(null, pointer5.tail());
 assertEquals(null, pointer6.tail());
 }

 @Test
 public void testToString() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/c", "c", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/d", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b/c", "b", pointer2);
 JsonPointer pointer5 = new JsonPointer("/a", "a", null);
 JsonPointer pointer6 = JsonPointer.EMPTY;

 assertEquals("/a/b/c", pointer1.toString());
 assertEquals("/a/b/c", pointer2.toString());
 assertEquals("/a/b/d", pointer3.toString());
 assertEquals("/a/b/c", pointer4.toString());
 assertEquals("/", pointer5.toString());
 assertEquals("", pointer6.toString());
 }

 @Test
 public void testHashCode() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/c", "c", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/d", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b/c", "b", pointer2);
 JsonPointer pointer5 = new JsonPointer("/a", "a", null);
 JsonPointer pointer6 = JsonPointer.EMPTY;

 assertEquals(pointer1.hashCode(), pointer1.hashCode());
 assertEquals(pointer1.hashCode(), pointer1.toString().hashCode());
 assertEquals(pointer2.hashCode(), pointer2.hashCode());
 assertEquals(pointer2.hashCode(), pointer2.toString().hashCode());
 assertEquals(pointer3.hashCode(), pointer3.hashCode());
 assertEquals(pointer3.hashCode(), pointer3.toString().hashCode());
 assertEquals(pointer4.hashCode(), pointer4.hashCode());
 assertEquals(pointer4.hashCode(), pointer4.toString().hashCode());
 assertEquals(pointer5.hashCode(), pointer5.hashCode());
 assertEquals(pointer5.hashCode(), pointer5.toString().hashCode());
 assertEquals(pointer6.hashCode(), pointer6.hashCode());
 assertEquals(pointer6.hashCode(), pointer6.toString().hashCode());
 }

 @Test
 public void testEquals() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c", "b", null);
 JsonPointer pointer2 = new JsonPointer("/a/b/c", "c", null);
 JsonPointer pointer3 = new JsonPointer("/a/b/d", "b", null);
 JsonPointer pointer4 = new JsonPointer("/a/b/c", "b", pointer2);
 JsonPointer pointer5 = new JsonPointer("/a", "a", null);
 JsonPointer pointer6 = JsonPointer.EMPTY;
 JsonPointer pointer7 = new JsonPointer("/a/b/c", "b", null);

 assertEquals(pointer1, pointer1);
 assertEquals(pointer1, pointer1.toString());
 assertEquals(pointer1, pointer1.toString());
 assertFalse(pointer1.equals(pointer2));
 assertFalse(pointer1.equals(pointer3));
 assertFalse(pointer1.equals(pointer4));
 assertFalse(pointer1.equals(pointer5));
 assertFalse(pointer1.equals(pointer6));
 assertFalse(pointer1.equals(pointer7));
 assertFalse(pointer2.equals(pointer3));
 assertFalse(pointer2.equals(pointer4));
 assertFalse(pointer2.equals(pointer5));
 assertFalse(pointer2.equals(pointer6));
 assertFalse(pointer2.equals(pointer7));
 assertFalse(pointer3.equals(pointer4));
 assertFalse(pointer3.equals(pointer5));
 assertFalse(pointer3.equals(pointer6));
 assertFalse(pointer3.equals(pointer7));
 assertFalse(pointer4.equals(pointer5));
 assertFalse(pointer4.equals(pointer6));
 assertFalse(pointer4.equals(pointer7));
 assertFalse(pointer5.equals(pointer6));
 assertFalse(pointer5.equals(pointer7));
 assertFalse(pointer6.equals(pointer7));
 }
}
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.EMPTY;
 assertTrue(jsonPointer.matches());
 assertNull(jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testJsonPointerConstructionWithEmptyString() {
 JsonPointer jsonPointer = new JsonPointer("");
 assertEquals("", jsonPointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidInput() {
 JsonPointer.compile("invalidInput");
 }

 @Test
 public void testJsonPointerCompilationWithValidInput() {
 String input = "/foo/bar/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals(input, jsonPointer.toString());
 assertFalse(jsonPointer.matches());
 assertEquals("foo", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 }

 @Test(expected = NullPointerException.class)
 public void testJsonPointerMatchPropertyWithNullPropertyName() {
 JsonPointer jsonPointer = new JsonPointer("/foo/bar", "nullPropertyName", JsonPointer.EMPTY);
 jsonPointer.matchProperty("nullPropertyName");
 }

 @Test
 public void testJsonPointerMatchPropertyWithValidPropertyName() {
 JsonPointer jsonPointer = new JsonPointer("/foo/bar/0", "bar", JsonPointer.EMPTY);
 JsonPointer expectedJsonPointer = new JsonPointer("/foo");
 assertTrue(jsonPointer.matches());
 assertEquals(expectedJsonPointer.toString(), jsonPointer.tail().toString());
 }

 @Test
 public void testJsonPointerMatchElementWithValidIndex() {
 JsonPointer jsonPointer = new JsonPointer("/foo/bar/0", "", JsonPointer.EMPTY);
 JsonPointer expectedJsonPointer = new JsonPointer("/foo/bar");
 assertTrue(jsonPointer.matches());
 assertEquals(expectedJsonPointer.toString(), jsonPointer.tail().toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJsonPointerMatchElementWithInvalidIndex() {
 JsonPointer jsonPointer = new JsonPointer("/foo/bar/0", "", JsonPointer.EMPTY);
 jsonPointer.matchElement(-1);
 }

 @Test
 public void testJsonPointerToString() {
 String input = "/foo/bar/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals(input, jsonPointer.toString());
 }

 @Test
 public void testJsonPointerHashCodeAndEquals() {
 JsonPointer jsonPointer1 = new JsonPointer("/foo/bar", "bar", JsonPointer.EMPTY);
 JsonPointer jsonPointer2 = new JsonPointer("/foo/bar", "bar", JsonPointer.EMPTY);
 JsonPointer jsonPointer3 = new JsonPointer("/foo", "bar", JsonPointer.EMPTY);
 JsonPointer jsonPointer4 = new JsonPointer("/foo/qux", "bar", JsonPointer.EMPTY);
 assertEquals(jsonPointer1.hashCode(), jsonPointer1.hashCode());
 assertEquals(jsonPointer1, jsonPointer1);
 assertEquals(jsonPointer1, jsonPointer2);
 assertNotEquals(jsonPointer1, jsonPointer3);
 assertNotEquals(jsonPointer1, jsonPointer4);
 }

}
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 @Test
 void testConstructorWithEmptyString() {
 char[] emptyValues = new char[0];
 IndexedString emptyIndexedString = new IndexedString("");
 assertEquals(0, emptyIndexedString.values.length);
 }

 @Test
 void testConstructorWithNullString() {
 assertThrows(NullPointerException.class, () -> new IndexedString(null));
 }

 @Test
 void testConstructorWithStringContainingAllAsciiChars() {
 String asciiString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
 IndexedString indexedString = new IndexedString(asciiString);
 assertEquals(asciiString.length(), indexedString.values.length);
 for (char c = '\0'; c < '\u0100'; ++c) {
 int count = indexedString.count(c);
 assertNotEquals(0, count);
 for (int i = 0; i < count; ++i) {
 int index = indexedString.index[c][i];
 assertTrue(index < indexedString.values.length);
 assertSame(c, indexedString.values[index]);
 }
 }
 }

 @Test
 void testIndexOfWithEmptyTargetArray() {
 char[] emptyTarget = new char[0];
 IndexedString indexedString = new IndexedString("abcdef");
 int result = indexedString.indexOf(emptyTarget);
 assertEquals(-1, result);
 }

 @Test
 void testIndexOfWithSingleCharacterTarget() {
 IndexedString indexedString = new IndexedString("abcdef");
 int result = indexedString.indexOf(new char[]{'e'});
 assertEquals(4, result);
 }

 @Test
 void testIndexOfWithMultipleCharacterTarget() {
 IndexedString indexedString = new IndexedString("abcdef");
 int result = indexedString.indexOf(new char[]{'a', 'b'});
 assertEquals(-1, result);
 }

 @Test
 void testIndexOfWithPartialMatch() {
 IndexedString indexedString = new IndexedString("abcdef");
 int result = indexedString.indexOf(new char[]{'a', 'b', 'c'});
 assertEquals(-1, result);
 }

 @Test
 void testIndexOfWithMismatchingCharacters() {
 IndexedString indexedString = new IndexedString("abcdef");
 int result = indexedString.indexOf(new char[]{'g', 'h'});
 assertEquals(-1, result);
 }

 @Test
 void testIndexOfWithTargetBeyondEndOfString() {
 IndexedString indexedString = new IndexedString("abcdef");
 int result = indexedString.indexOf(new char[]{'f', 'g'});
 assertEquals(-1, result);
 }
}
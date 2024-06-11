import com.google.common.base.Utf8;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 char[] sequence = {'a', 'b', 'c', '\u0393', '\u0410'}; // Greek and Cyrillic characters
 int expected = Utf8.encodedLength(sequence);
 int actual = Utf8.encodedLength(new String(sequence));
 assertEquals(expected, actual);
 }

 @Test
 public void testEncodedLengthEdgeCases() {
 char[] sequenceEmpty = {};
 int expected = 0;
 int actual = Utf8.encodedLength(sequenceEmpty);
 assertEquals(expected, actual);

 char[] sequenceAllBelow7F = {'a', '\u0001', '\u0002', '\u0003'};
 int expectedBelow7F = 3;
 int actualBelow7F = Utf8.encodedLength(sequenceAllBelow7F);
 assertEquals(expectedBelow7F, actualBelow7F);

 char[] sequenceSurrogate = {'\uD800', '\uDC00', '\uD800', '\uDC01'}; // Surrogate pair
 int expectedSurrogate = 12;
 int actualSurrogate = Utf8.encodedLength(sequenceSurrogate);
 assertEquals(expectedSurrogate, actualSurrogate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthIllegalArgument() {
 char[] sequenceIllegal = {'\uFDD0', '\uD800'}; // Invalid UTF-8 sequence
 Utf8.encodedLength(sequenceIllegal);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytesValid = {97, 98, 99, 353, 454, 455}; // Valid UTF-8 sequence
 boolean expectedValid = true;
 boolean actualValid = Utf8.isWellFormed(bytesValid);
 assertEquals(expectedValid, actualValid);

 byte[] bytesInvalid1 = {97, 98, 99, -1, 454, 455}; // Invalid UTF-8 sequence
 boolean expectedInvalid1 = false;
 boolean actualInvalid1 = Utf8.isWellFormed(bytesInvalid1);
 assertEquals(expectedInvalid1, actualInvalid1);

 byte[] bytesInvalid2 = {97, 98, 99, 226, 160, 128, 153}; // Invalid UTF-8 sequence
 boolean expectedInvalid2 = false;
 boolean actualInvalid2 = Utf8.isWellFormed(bytesInvalid2);
 assertEquals(expectedInvalid2, actualInvalid2);
 }

 @Test
 public void testIsWellFormedEdgeCases() {
 byte[] bytesEmpty = {}; // Empty array
 boolean expectedEmpty = true;
 boolean actualEmpty = Utf8.isWellFormed(bytesEmpty);
 assertEquals(expectedEmpty, actualEmpty);

 byte[] bytesSingleByte = {97}; // Single byte
 boolean expectedSingleByte = true;
 boolean actualSingleByte = Utf8.isWellFormed(bytesSingleByte);
 assertEquals(expectedSingleByte, actualSingleByte);

 byte[] bytesAllBytesBelow7F = {97, 1, 2, 3}; // All bytes below 128
 boolean expectedAllBytesBelow7F = true;
 boolean actualAllBytesBelow7F = Utf8.isWellFormed(bytesAllBytesBelow7F);
 assertEquals(expectedAllBytesBelow7F, actualAllBytesBelow7F);

 byte[] bytesSurrogate = {0xD8, 0x00, 0xDC, 0x01}; // Surrogate pair
 boolean expectedSurrogate = true;
 boolean actualSurrogate = Utf8.isWellFormed(bytesSurrogate);
 assertEquals(expectedSurrogate, actualSurrogate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedIllegalArgument() {
 byte[] bytesIllegal = {-1}; // Negative byte
 Utf8.isWellFormed(bytesIllegal);
 }
}
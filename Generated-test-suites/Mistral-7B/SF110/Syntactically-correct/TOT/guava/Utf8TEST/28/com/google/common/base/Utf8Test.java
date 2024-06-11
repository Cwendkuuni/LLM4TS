package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 charSequence cs = " abcDEFGHIJklmNOPQRSTuvwXYZabcdefghijklmNOPQRSTuvwXYZ"; // ASCII characters
 int length = Utf8.encodedLength(cs);
 assertEquals(cs.length(), length);

 cs = "\u00A0\u0391\u0392\u0393\u0394"; // Characters with code points less than 0x80
 length = Utf8.encodedLength(cs);
 assertEquals(cs.length(), length);

 cs = "\u0410\u0411\u0412\u0413\u0414\uD800\uDC00"; // Characters with code points greater than 0xFFFF
 int utf16Length = cs.length();
 int utf8Length = Utf8.encodedLength(cs);
 assertEquals(utf16Length, utf8Length);

 cs = "\uD800\uDC01"; // Surrogate pair
 try {
 utf8Length = Utf8.encodedLength(cs);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 charSequence cs = " abcDEFGHIJklmNOPQRSTuvwXYZabcdefghijklmNOPQRSTuvwXYZ"; // ASCII characters
 int length = Utf8.encodedLengthGeneral(cs, 0);
 assertEquals(cs.length(), length);

 cs = "\u0391\u0392\u0393\u0394"; // Characters with code points less than 0x80
 length = Utf8.encodedLengthGeneral(cs, 0);
 assertEquals(cs.length(), length);

 cs = "\u0410\u0411\u0412\u0413\u0414\uD800\uDC00"; // Characters with code points greater than 0xFFFF
 int start = cs.indexOf("\uD800");
 int utf8Length = Utf8.encodedLengthGeneral(cs, start);
 assertEquals(cs.length() - start, utf8Length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthGeneralInvalidInput() {
 charSequence cs = "\uD800"; // Surrogate character
 int utf8Length = Utf8.encodedLengthGeneral(cs, 0);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = { -128, 63, 63, 63, 63, -128, -62, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63 }; // Invalid byte sequence
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[] { 72, 199, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191, 191 }; // Valid UTF-8 sequence, all bytes represent single characters
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedInvalidInput() {
 byte[] bytes = new byte[] { -128 }; // Invalid byte
 assertFalse(Utf8.isWellFormed(bytes));
 }
}
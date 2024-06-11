package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        String input = "";
        int expectedLength = 0;
        int actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u0041";
        expectedLength = 1;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u0410";
        expectedLength = 2;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\uD801\uDC00";
        expectedLength = 3;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\uD800\uDFFF";
        expectedLength = 3; // Surrogate pair not considered as a single character
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength <= actualLength);

        input = "foo";
        expectedLength = 3;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u0394\u03C3\u03B1\u03C1\u039F";
        expectedLength = 10;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u2122\u2122\u2122"; // Three consecutive copyright symbols
        expectedLength = 9;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "👋👋👋"; // Three emoji faces
        expectedLength = 12;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\uFFFF"; // Maximum Unicode character
        expectedLength = 4;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u10FFFF"; // Maximum Unicode code point
        expectedLength = 4;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "moon\uD83C\uDD18sun\uD83C\uDD26";
        expectedLength = 13;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "moon🌙sun🌞";
        expectedLength = 11;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "moon\uD83C\uDD18sun";
        expectedLength = 7; // Surrogate pair not considered as a single character
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength <= actualLength);

        input = "\uFFFE"; // Replacement character
        expectedLength = 2;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u0000"; // Null character
        expectedLength = 1;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u007F"; // Del character
        expectedLength = 1;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u0080"; // Start of ASCII range
        expectedLength = 1;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u00FF"; // End of ASCII range
        expectedLength = 1;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u0080\u00FF"; // Single byte outside ASCII range
        expectedLength = 2;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\u0410\u0410"; // Two bytes outside ASCII range
        expectedLength = 4;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength <= actualLength);

        input = "\uD800\uD801"; // Two surrogate pairs
        expectedLength = 6;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "\uD800\uDFFF"; // All surrogate pairs
        expectedLength = 2 * 0x10000;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);

        input = "moon\uD83C\uDD18sun\uD83C\uDD26"; // Emojis with surrogate pairs
        expectedLength = 16;
        actualLength = Utf8.encodedLength(input);
        assertTrue(expectedLength == actualLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionEncoding() {
        String input = "\u10FFFF\u10FFFF";
        Utf8.encodedLength(input);
    }

    @Test
    public void testIsWellFormed() {
        byte[] validUtf8 = {0x41, 0x42, 0x43, 0x44};
        assertTrue(Utf8.isWellFormed(validUtf8));

        byte[] validUtf8WithFirstByteMinusOne = new byte[4];
        System.arraycopy(validUtf8, 0, validUtf8WithFirstByteMinusOne, 0, 4);
        validUtf8WithFirstByteMinusOne[0] = (byte) (validUtf8[0] - 1);
        assertTrue(Utf8.isWellFormed(validUtf8WithFirstByteMinusOne));

        byte[] validUtf8WithFirstByteMinusTwo = new byte[4];
        System.arraycopy(validUtf8, 0, validUtf8WithFirstByteMinusTwo, 0, 4);
        validUtf8WithFirstByteMinusTwo[0] = (byte) (validUtf8[0] - 2);
        assertFalse(Utf8.isWellFormed(validUtf8WithFirstByteMinusTwo));

        byte[] invalidUtf8LeadingZero = new byte[1];
        invalidUtf8LeadingZero[0] = 0;
        assertFalse(Utf8.isWellFormed(invalidUtf8LeadingZero));

        byte[] invalidUtf8TrailingZero = new byte[1];
        invalidUtf8TrailingZero[0] = (byte) 0x7F;
        assertFalse(Utf8.isWellFormed(invalidUtf8TrailingZero));

        byte[] invalidUtf8IllegalByte = new byte[1];
        invalidUtf8IllegalByte[0] = 0xC2; // Illegal byte outside the BMP
        assertFalse(Utf8.isWellFormed(invalidUtf8IllegalByte));

        byte[] invalidUtf8Sequence = new byte[2];
        invalidUtf8Sequence[0] = 0xE0; // Start of a multi-byte sequence
        invalidUtf8Sequence[1] = 0x9F; // Illegal byte for this sequence
        assertFalse(Utf8.isWellFormed(invalidUtf8Sequence));

        byte[] invalidUtf8LoneSurrogate = new byte[1];
        invalidUtf8LoneSurrogate[0] = 0xD800; // Lone surrogate is not a valid UTF-8 sequence
        assertFalse(Utf8.isWellFormed(invalidUtf8LoneSurrogate));

        byte[] invalidUtf8SurrogatePair = new byte[3];
        invalidUtf8SurrogatePair[0] = 0xD800; // First surrogate
        invalidUtf8SurrogatePair[1] = 0xD801; // Second surrogate
        assertFalse(Utf8.isWellFormed(invalidUtf8SurrogatePair));

        byte[] invalidUtf8SurrogatePairMissingSecond = new byte[2];
        invalidUtf8SurrogatePairMissingSecond[0] = 0xD800; // First surrogate
        assertFalse(Utf8.isWellFormed(invalidUtf8SurrogatePairMissingSecond));

        byte[] invalidUtf8SurrogatePairInvalidSecond = new byte[3];
        invalidUtf8SurrogatePairInvalidSecond[0] = 0xD800; // First surrogate
        invalidUtf8SurrogatePairInvalidSecond[1] = (byte) 0x9F; // Invalid second surrogate
        assertFalse(Utf8.isWellFormed(invalidUtf8SurrogatePairInvalidSecond));

        byte[] invalidUtf8InvalidSequence = new byte[3];
        invalidUtf8InvalidSequence[0] = 0xE0; // Start of invalid sequence
        invalidUtf8InvalidSequence[1] = 0x9F; // Invalid continuation byte
        assertFalse(Utf8.isWellFormed(invalidUtf8InvalidSequence));

        byte[] validUtf8WithBom = {(byte) 0xEF, 0xBB, 0xBF, 0x41, 0x42, 0x43, 0x44};
        assertTrue(Utf8.isWellFormed(validUtf8WithBom));

        byte[] validUtf8WithBomMissingFirstByte = new byte[7];
        System.arraycopy(validUtf8WithBom, 1, validUtf8WithBomMissingFirstByte, 0, 7);
        assertFalse(Utf8.isWellFormed(validUtf8WithBomMissingFirstByte));

        byte[] validUtf8WithBomMissingLastByte = new byte[7];
        validUtf8WithBomMissingLastByte[0] = 0xEF;
        validUtf8WithBomMissingLastByte[1] = 0xBB;
        validUtf8WithBomMissingLastByte[2] = 0xBF;
        System.arraycopy(validUtf8, 0, validUtf8WithBomMissingLastByte, 3, 4);
        assertFalse(Utf8.isWellFormed(validUtf8WithBomMissingLastByte));
    }
}
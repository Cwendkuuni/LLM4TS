package org.apache.commons.codec.net;

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import org.junit.Test;

/**
 * QuotedPrintableCodecTest.java
 */
public class QuotedPrintableCodecTest {

    @Test
    public void testEncodeDecode() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] original = {0x2A, 0x69, 0x64, 0x65, 0x6E, 0x74, 0x6C, 0x69, 0x6F, 0x6E};
        byte[] encoded = codec.encode(original);
        byte[] decoded = codec.decode(encoded);

        assertArrayEquals(original, decoded);

        String originalString = new String(original, "UTF-8");
        String encodedString = Hex.encodeHexString(encoded);
        String decodedString = new String(decoded, "UTF-8");

        assertEquals(originalString, decodedString);
        assertEquals(encodedString, codec.encode(originalString, "UTF-8"));
    }

    @Test
    public void testEncodeWhitespace() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] original = {0x20, 0x20, 0x20, 0x20, 0x20};
        byte[] encoded = codec.encode(original);

        String encodedString = Hex.encodeHexString(encoded);
        String decodedString = new String(codec.decode(encoded), "UTF-8");

        assertEquals(encodedString, codec.encode(new String(original), "UTF-8"));
        assertEquals(new String(original, "UTF-8"), decodedString);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidChar() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] original = {'\u0080'};
        codec.encode(original);
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidData() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encoded = {0x3D, 0x30, 0x31, 0x3D, 0x30, 0x32};
        codec.decode(encoded);
    }

    @Test
    public void testEncodeCR() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] original = {0x0D};
        byte[] encoded = codec.encode(original);

        String encodedString = Hex.encodeHexString(encoded);
        String decodedString = new String(codec.decode(encoded), "UTF-8");

        assertEquals(encodedString, codec.encode(new String(original, "UTF-8"), "UTF-8"));
        assertEquals(new String(original, "UTF-8"), decodedString);
    }

    @Test
    public void testEncodeLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] original = {0x0A};
        byte[] encoded = codec.encode(original);

        String encodedString = Hex.encodeHexString(encoded);
        String decodedString = new String(codec.decode(encoded), "UTF-8");

        assertEquals(encodedString, codec.encode(new String(original, "UTF-8"), "UTF-8"));
        assertEquals(new String(original, "UTF-8"), decodedString);
    }

    @Test
    public void testEncodeCRLF() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] original = {0x0D, 0x0A};
        byte[] encoded = codec.encode(original);

        String encodedString = Hex.encodeHexString(encoded);
        String decodedString = new String(codec.decode(encoded), "UTF-8");

        assertEquals(encodedString, codec.encode(new String(original, "UTF-8"), "UTF-8"));
        assertEquals(new String(original, "UTF-8"), decodedString);
    }
}
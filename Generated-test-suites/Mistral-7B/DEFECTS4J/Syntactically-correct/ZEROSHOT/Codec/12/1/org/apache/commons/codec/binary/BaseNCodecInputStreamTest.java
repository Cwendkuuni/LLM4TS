package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private InputStream inputStream;
    private BaseNCodec codec;
    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        codec = new BaseNCodec();
        inputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x2B, 0x2C, 0x2D, 0x2E }); // Base32 encoded
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, codec, false);
    }

    @Test
    public void testConstructor() {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(null, codec, false);
        assertNull(baseNCodecInputStream);
    }

    @Test
    public void testConstructorWithNullCodec() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x2B, 0x2C, 0x2D, 0x2E });
        assertThrows(NullPointerException.class, () -> new BaseNCodecInputStream(inputStream, null, false));
    }

    @Test
    public void testConstructorWithNullInputStream() {
        assertThrows(NullPointerException.class, () -> new BaseNCodecInputStream(null, codec, false));
    }

    @Test
    public void testConstructorWithNullCodecButValidInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x2B, 0x2C, 0x2D, 0x2E });
        assertThrows(NullPointerException.class, () -> new BaseNCodecInputStream(inputStream, null, false));
    }

    @Test
    public void testConstructorWithValidArguments() {
        assertNotNull(baseNCodecInputStream);
        assertEquals(codecs(), baseNCodecInputStream.getCodec());
    }

    @Test
    public void testDoEncodeIsFalse() throws IOException {
        byte[] expectedOutput = new byte[] { 0x60, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E }; // Base64 decoded
        byte[] buffer = new byte[6];
        int actualLength = baseNCodecInputStream.read(buffer, 0, 6);
        assertArrayEquals(expectedOutput, buffer);
        assertEquals(5, actualLength);
    }

    @Test
    public void testDoEncodeIsTrue() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodecOutputStream baseNCodecOutputStream = new BaseNCodecOutputStream((OutputStream) outputStream, codec);
        byte[] input = new byte[] { 0x60, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E };
        baseNCodecOutputStream.write(input, 0, input.length);
        baseNCodecOutputStream.flush();
        byte[] expectedOutput = new byte[] { 0x2A, 0x2B, 0x2C, 0x2D, 0x2E };
        byte[] buffer = new byte[5];
        int actualLength = baseNCodecInputStream.read(buffer, 0, 5);
        assertArrayEquals(expectedOutput, buffer);
        assertEquals(5, actualLength);
    }

    @Test
    public void testHasAvailableData() throws IOException {
        assertTrue(baseNCodecInputStream.hasAvailableData());
        byte[] buffer = new byte[1];
        while (baseNCodecInputStream.read(buffer, 0, 1) >= 0) {}
        assertFalse(baseNCodecInputStream.hasAvailableData());
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testRead() throws IOException {
        byte[] expectedOutput = new byte[] { 0x2A, 0x2B, 0x2C, 0x2D, 0x2E };
        byte[] buffer = new byte[5];
        int actualLength = baseNCodecInputStream.read(buffer, 0, 5);
        assertArrayEquals(expectedOutput, buffer);
        assertEquals(5, actualLength);
    }

    @Test
    public void testReadByte() throws IOException {
        byte[] expectedOutput = new byte[] { 0x2A, 0x2B, 0x2C, 0x2D, 0x2E };
        byte[] buffer = new byte[5];
        int actualLength = baseNCodecInputStream.read(buffer, 0, 5);
        assertEquals(expectedOutput[0], buffer[0]);
        assertEquals(expectedOutput[1], buffer[1]);
        assertEquals(expectedOutput[2], buffer[2]);
        assertEquals(expectedOutput[3], buffer[3]);
        assertEquals(expectedOutput[4], buffer[4]);
        assertEquals(5, actualLength);
    }

    @Test
    public void testReadNullByteArray() {
        assertThrows(NullPointerException.class, () -> baseNCodecInputStream.read(null, 0, 1));
    }

    @Test
    public void testReadNegativeLength() {
        assertThrows(IndexOutOfBoundsException.class, () -> baseNCodecInputStream.read(new byte[] { 1 }, 6, 1));
    }

    @Test
    public void testReadZeroLength() throws IOException {
        byte[] buffer = new byte[0];
        int actualLength = baseNCodecInputStream.read(buffer, 0, 0);
        assertEquals(0, actualLength);
    }

    @Test
    public void testReadZeroBytes() throws IOException {
        byte[] buffer = new byte[5];
        int actualLength = baseNCodecInputStream.read(buffer, 0, 0);
        assertEquals(0, actualLength);
    }

    @Test
    public void testSkipsData() throws IOException {
        byte[] expectedOutput = new byte[] { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37 };
        byte[] buffer = new byte[8];
        int actualLength = baseNCodecInputStream.read(buffer, 0, 8);
        assertEquals(actualLength, 8);
        int skippedLength = baseNCodecInputStream.skip(8);
        assertEquals(8, skippedLength);
        byte[] bufferToCheck = new byte[4];
        actualLength = baseNCodecInputStream.read(bufferToCheck, 0, 4);
        assertEquals(expectedOutput[4], bufferToCheck[0]);
        assertEquals(expectedOutput[5], bufferToCheck[1]);
        assertEquals(expectedOutput[6], bufferToCheck[2]);
        assertEquals(expectedOutput[7], bufferToCheck[3]);
        assertEquals(4, actualLength);
    }

    @Test
    public void testSkipsDataMoreThanAvailable() throws IOException {
        byte[] expectedOutput = new byte[] { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37 };
        byte[] buffer = new byte[8];
        int actualLength = baseNCodecInputStream.read(buffer, 0, 8);
        assertEquals(actualLength, 8);
        int skippedLength = baseNCodecInputStream.skip(16);
        assertEquals(8, baseNCodecInputStream.available());
    }

    @Test
    public void testSkipsDataZeroLength() throws IOException {
        byte[] buffer = new byte[0];
        int skippedLength = baseNCodecInputStream.skip(0);
        assertEquals(0, skippedLength);
    }

    private BaseNCodec getCodecs() {
        return new BaseNCodec();
    }

    private static final BaseNCodec codecs() {
        return new BaseNCodec();
    }

}
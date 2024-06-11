package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private InputStream inputStream;
    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setup() {
        baseNCodec = new BaseNCodec();
        inputStream = new ByteArrayInputStream(new byte[] { 0x61, 0x61, 0x61 });
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(0x61, baseNCodecInputStream.read());
        assertEquals(0x61, baseNCodecInputStream.read());
        assertEquals(0x61, baseNCodecInputStream.read());
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = new byte[] { 0x61, 0x61, 0x61 };
        byte[] actual = new byte[3];
        int read = baseNCodecInputStream.read(actual, 0, 3);
        assertEquals(3, read);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        int byteToSkip = 1;
        baseNCodecInputStream.skip(byteToSkip);
        byte[] readBuffer = new byte[1];
        int bytesRead = baseNCodecInputStream.read(readBuffer);
        assertEquals(1, bytesRead);
        assertArrayEquals(new byte[] { 0x61 }, readBuffer);

        byteToSkip = 2;
        baseNCodecInputStream.skip(byteToSkip);
        bytesRead = baseNCodecInputStream.read(readBuffer);
        assertEquals(1, bytesRead);
        assertArrayEquals(new byte[] { 0x61 }, readBuffer);

        baseNCodecInputStream.mark(0); // mark
        baseNCodecInputStream.skip(3); // skip more than marked
        int bytesReadAfterSkip = baseNCodecInputStream.read(readBuffer);
        assertEquals(BaseNCodecInputStream.EOF, bytesReadAfterSkip);

        baseNCodecInputStream.reset(); // reset
        bytesRead = baseNCodecInputStream.read(readBuffer);
        assertEquals(1, bytesRead);
        assertArrayEquals(new byte[] { 0x61 }, readBuffer);
    }

    @Test
    public void testEncodeDecodeExample() throws IOException {
        // given
        String inputString = "aaabbbbccccccddddeeeee";
        byte[] encoded = baseNCodec.encode(inputString.getBytes());

        // when
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encoded);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] output = new byte[BaseNCodec.MAX_ENCODED_LENGTH];
        int readBytes;

        // then
        while ((readBytes = baseNCodecInputStream.read(output)) > 0) {
            outputStream.write(output, 0, readBytes);
        }
        byte[] outputBytes = outputStream.toByteArray();
        String decodedString = new String(baseNCodec.decode(outputBytes));

        // assert
        assertEquals(inputString, decodedString);
    }

    @Test
    public void testEncodeDecodeNullInput() {
        // given
        byte[] nullInput = null;

        // when
        try {
            baseNCodec.encode(nullInput);
        } catch (NullPointerException e) {
            // then
            assertNull(baseNCodecInputStream);
        }

        try {
            baseNCodecInputStream = new BaseNCodecInputStream(null, baseNCodec, true);
        } catch (NullPointerException e) {
            // then
            assertNull(baseNCodecInputStream);
        }
    }
}
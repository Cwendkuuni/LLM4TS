package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} constructor.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamConstructor() throws Exception {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes(StandardCharsets.UTF_8));
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(input, baseNCodec, false);
        assertEquals(input, testInstance.in);
        assertEquals(baseNCodec, testInstance.baseNCodec);
        assertFalse(testInstance.doEncode);
    }

    /**
     * Test for {@link BaseNCodecInputStream#read()} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testRead() throws Exception {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes(StandardCharsets.UTF_8));
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] result = new byte[1];
        for (int i = 0; i < 5; i++) {
            int len = testInstance.read(result, 0, 1);
            assertEquals(1, len);
            assertEquals((byte) 'B', result[0]);
        }

        int len = testInstance.read(result, 0, 1);
        assertEquals(BaseNCodecInputStream.EOF, len);
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testReadArray() throws Exception {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes(StandardCharsets.UTF_8));
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] result = new byte[5];
        int len = testInstance.read(result, 0, 5);
        assertEquals(5, len);
        for (int i = 0; i < len; i++) {
            assertEquals((byte) 'B', result[i]);
        }

        len = testInstance.read(result, 0, 2);
        assertEquals(2, len);
        assertEquals((byte) 'A', result[0]);
        assertEquals((byte) 'B', result[1]);

        len = testInstance.read(result, 2, 3);
        assertEquals(3, len);
        assertEquals((byte) 'A', result[2]);
        assertEquals((byte) 'B', result[3]);
        assertEquals((byte) 'A', result[4]);

        len = testInstance.read(result, 5, 1);
        assertEquals(BaseNCodecInputStream.EOF, len);
    }

    /**
     * Test for {@link BaseNCodecInputStream#markSupported()} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testMarkSupported() throws Exception {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes(StandardCharsets.UTF_8));
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(input, baseNCodec, false);
        assertFalse(testInstance.markSupported());
    }

    /**
     * Test for {@link BaseNCodecInputStream#skip(long)} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testSkip() throws Exception {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes(StandardCharsets.UTF_8));
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(input, baseNCodec, false);

        assertThrows(IOException.class, () -> testInstance.skip(Long.MAX_VALUE));
        testInstance.skip(1);
        testInstance.skip(1);
        testInstance.skip(1);
        testInstance.skip(1);
        testInstance.skip(1);

        int len = testInstance.read(new byte[1][], 0, 1);
        assertEquals(BaseNCodecInputStream.EOF, len);
    }

    /**
     * Test for {@link BaseNCodecInputStream#available()} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testAvailable() throws Exception {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes(StandardCharsets.UTF_8));
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(input, baseNCodec, false);

        assertEquals(5, testInstance.available());
        testInstance.read(new byte[1][], 0, 1);
        testInstance.read(new byte[1][], 0, 1);
        testInstance.read(new byte[1][], 0, 1);
        testInstance.read(new byte[1][], 0, 1);
        testInstance.read(new byte[1][], 0, 1);

        assertEquals(0, testInstance.available());
    }
}
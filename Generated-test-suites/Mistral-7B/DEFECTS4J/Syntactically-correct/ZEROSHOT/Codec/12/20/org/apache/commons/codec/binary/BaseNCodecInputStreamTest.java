package org.apache.commons.codec.binary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private InputStream inputStream;

    @Before
    public void setUp() throws IOException {
        baseNCodec = new BaseNCodec();
        inputStream = new ByteArrayInputStream(new byte[]{"ABCDEFG".getBytes()});
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
    }

    /**
     * Test the {@code read()} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testRead() throws IOException {
        BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] result = new byte[3];

        int readByte = testee.read();
        assertThat(readByte, is(0));

        int readLength = testee.read(result, 0, 3);
        assertThat(readLength, is(3));
        assertThat(new String(result), is("ABC"));
    }

    /**
     * Test the {@code read(byte[], int, int)} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadByteArray() throws IOException {
        BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[6];
        int readLength = testee.read(result, 0, 6);
        assertThat(readLength, is(6));
        assertThat(new String(result), is("ABCDEFG"));
    }

    /**
     * Test the {@code markSupported()} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        testee.markSupported();
    }

    /**
     * Test the {@code skip(long)} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testSkip() throws IOException {
        BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        long skipLength = 5;
        int skippedBytes = 0;
        while (skippedBytes < skipLength) {
            int readLength = testee.read(new byte[1], 0, 1);
            if (readLength < 0) {
                break;
            }
            skippedBytes++;
        }

        int readLength = testee.read(new byte[1], 0, 1);
        assertThat(readLength, is(-1));
    }
}
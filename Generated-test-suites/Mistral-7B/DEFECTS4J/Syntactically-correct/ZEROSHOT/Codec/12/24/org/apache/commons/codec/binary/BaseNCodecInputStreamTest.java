package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testConstructor() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("BaseNData".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertNull(baseNCodecInputStream.getMark);
        assertEquals(inputStream, baseNCodecInputStream.in);
        assertEquals(false, baseNCodecInputStream.doEncode);
        assertEquals(baseNCodec, baseNCodecInputStream.baseNCodec);
    }

    /**
     * Test read byte.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testReadByte() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // read 'A'
        final int read = baseNCodecInputStream.read();
        assertTrue(read >= 0 && read <= 255);
        assertEquals((byte) 'A', read);

        // read 'B'
        final int readB = baseNCodecInputStream.read();
        assertTrue(readB >= 0 && readB <= 255);
        assertEquals((byte) 'B', readB);

        // read EOF
        final int readEOF = baseNCodecInputStream.read();
        assertEquals(EOF, readEOF);

        // verify that EOF has been reached
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test read byte array.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testReadByteArray() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // read 'ABCDEFGH'
        final byte[] buffer = new byte[8];
        final int length = baseNCodecInputStream.read(buffer);
        assertEquals(8, length);
        for (int i = 0; i < length; i++) {
            assertEquals((byte) (i + 'A'), buffer[i]);
        }

        // read EOF
        final int lengthEOF = baseNCodecInputStream.read(buffer, 0, 8);
        assertEquals(0, lengthEOF);

        // verify that EOF has been reached
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test skip byte.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testSkipByte() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // skip 'A'
        final int skipped = baseNCodecInputStream.skip(1);
        assertEquals(1, skipped);

        // verify that 'B' can be read
        final int read = baseNCodecInputStream.read();
        assertTrue(read >= 0 && read <= 255);
        assertEquals((byte) 'B', read);

        // verify that EOF has been reached
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test skip byte array.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testSkipByteArray() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // skip 'AB'
        final int skipped = baseNCodecInputStream.skip(2);
        assertEquals(2, skipped);

        // verify that 'C' can be read
        final int read = baseNCodecInputStream.read();
        assertTrue(read >= 0 && read <= 255);
        assertEquals((byte) 'C', read);

        // verify that EOF has been reached
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test encodeInputStream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testEncodeInputStream() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // copy encoded data to output stream
        final byte[] buffer = new byte[8];
        int length;
        while ((length = baseNCodecInputStream.read(buffer, 0, buffer.length)) != EOF) {
            outputStream.write(buffer, 0, length);
        }

        // compare encoded data with expected data
        final byte[] expected = "U2FSDGVWMV198BQJ6WXUCWFWQDK1GQO1FJ8VESTW9DK2NBQJ84".getBytes();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], outputStream.toByteArray()[i]);
        }

        // verify that EOF has been reached
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test decodeInputStream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testDecodeInputStream() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("U2FSDGVWMV198BQJ6WXUCWFWQDK1GQO1FJ8VESTW9DK2NBQJ84".getBytes());
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // copy decoded data to output stream
        final byte[] buffer = new byte[8];
        int length;
        while ((length = baseNCodecInputStream.read(buffer, 0, buffer.length)) != EOF) {
            outputStream.write(buffer, 0, length);
        }

        // compare decoded data with expected data
        final byte[] expected = "ABCDEFGH".getBytes();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], outputStream.toByteArray()[i]);
        }

        // verify that EOF has been reached
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test hasData.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testHasData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // check if there is data available
        assertTrue(baseNCodecInputStream.hasData());

        // read data
        final byte[] buffer = new byte[8];
        baseNCodecInputStream.read(buffer, 0, buffer.length);

        // check if there is data available
        assertFalse(baseNCodecInputStream.hasData());

        // verify that EOF has been reached
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test close.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testClose() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // verify that close does not throw an exception
        baseNCodecInputStream.close();

        // verify that mark is null
        assertNull(baseNCodecInputStream.getMark());

        // verify that read throws an exception
        try {
            baseNCodecInputStream.read();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test close.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testCloseTwice() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // verify that close does not throw an exception
        baseNCodecInputStream.close();

        // verify that close does not throw an exception again
        baseNCodecInputStream.close();

        // verify that mark is null
        assertNull(baseNCodecInputStream.getMark());

        // verify that read throws an exception
        try {
            baseNCodecInputStream.read();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test mark supported.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // verify that markSupported throws an exception
        baseNCodecInputStream.markSupported();
    }

    /**
     * Test mark.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testMark() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // mark position
        baseNCodecInputStream.mark(10);

        // verify that getMark returns the mark
        assertEquals(10, baseNCodecInputStream.getMark().intValue());

        // reset to current position
        baseNCodecInputStream.reset();

        // verify that getMark returns null
        assertNull(baseNCodecInputStream.getMark());

        // verify that EOF has been reached
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test reset.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testReset() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream("ABCDEFGH".getBytes());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // mark position
        baseNCodecInputStream.mark(10);
        baseNCodecInputStream.reset();

        // verify that getMark returns null
        assertNull(baseNCodecInputStream.getMark());

        // verify that EOF has been reached
        assertFalse(baseNCodecInputStream.markSupported());
    }

}
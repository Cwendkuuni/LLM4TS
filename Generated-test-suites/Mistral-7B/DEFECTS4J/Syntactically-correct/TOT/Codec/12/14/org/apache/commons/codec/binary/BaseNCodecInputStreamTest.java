package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Tests for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamConstruction() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base32();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);
        assertEquals(input, inputStream.in);
        assertEquals(baseNCodec, inputStream.baseNCodec);
        assertEquals(doEncode, inputStream.doEncode);
    }

    /**
     * Test for {@link BaseNCodecInputStream#read()}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamRead() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = mock(BaseNCodec.class);
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(1);

        assertEquals((byte) 'A', inputStream.read());
        assertEquals(EOF, inputStream.read());
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamReadByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = mock(BaseNCodec.class);
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(1);

        final byte[] expectedData = new byte[] { 'A' };
        final byte[] buffer = new byte[10];
        final int offset = 0;
        final int length = 1;

        inputStream.read(buffer, offset, length);

        assertEquals(expectedData, buffer, 0);
        assertEquals(EOF, inputStream.read(buffer, offset, 1));
    }

    /**
     * Test for {@link BaseNCodecInputStream#markSupported()}.
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamMarkSupported() {
        final InputStream input = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertFalse(inputStream.markSupported());
    }

    /**
     * Test for {@link BaseNCodecInputStream#skip(long)}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamSkip() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base32();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenAnswer(invocation -> 1);

        final long lengthToSkip = 512L;
        inputStream.skip(lengthToSkip);

        verify(baseNCodec).decode(anyByteArray(), anyInt(), eq(lengthToSkip));
    }

    /**
     * Test for {@link BaseNCodecInputStream#close()}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamClose() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base32();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);

        inputStream.close();
        inputStream.read();

        assertThrows(IOException.class, () -> inputStream.read());
    }

    /**
     * Test for {@link BaseNCodecInputStream#read()}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamReadEmptyStream() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final BaseNCodec baseNCodec = new Base32();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);

        assertEquals(EOF, inputStream.read());
        assertEquals(EOF, inputStream.read());
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamReadEmptyStreamByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final BaseNCodec baseNCodec = new Base32();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);

        final byte[] buffer = new byte[1];
        final int offset = 0;
        final int length = 1;

        assertEquals(EOF, inputStream.read(buffer, offset, length));
        assertEquals(EOF, inputStream.read(buffer, offset, length));
    }

    /**
     * Test for {@link BaseNCodecInputStream#read()}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamReadWithData() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 'A' });
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new Base32();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);

        final byte[] expectedData = new byte[] { 'A' };
        final byte[] buffer = new byte[10];
        final int offset = 0;
        final int length = 1;

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(1);

        inputStream.read(buffer, offset, length);
        assertEquals(expectedData, buffer, 0);

        final byte[] decodedData = output.toByteArray();
        assertEquals(expectedData, decodedData, 0);
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamReadWithDataByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 'A' });
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new Base32();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);

        final byte[] expectedData = new byte[] { 'A' };
        final byte[] buffer = new byte[10];
        final int offset = 0;
        final int length = 1;

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(1);

        inputStream.read(buffer, offset, length);
        assertEquals(expectedData, buffer, 0);

        final byte[] decodedData = output.toByteArray();
        assertEquals(expectedData, decodedData, 0);
    }

    /**
     * Test for {@link BaseNCodecInputStream#read()}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamReadWithNoData() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final BaseNCodec baseNCodec = new Base32();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);

        assertEquals(EOF, inputStream.read());
        assertEquals(EOF, inputStream.read());
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamReadWithNoDataByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final BaseNCodec baseNCodec = new Base32();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, doEncode);

        final byte[] buffer = new byte[10];
        final int offset = 0;
        final int length = 1;

        assertEquals(EOF, inputStream.read(buffer, offset, length));
        assertEquals(EOF, inputStream.read(buffer, offset, length));
    }
}
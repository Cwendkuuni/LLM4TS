package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void test_BaseNCodecInputStream_Initialize() {
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15 });
        boolean doEncode = false;
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);
        assertNotNull(testInstance);
        assertEquals(inputStream, testInstance.in);
        assertEquals(baseNCodec, testInstance.baseNCodec);
        assertEquals(doEncode, testInstance.doEncode);
        assertEquals(BaseNCodecInputStream.class, testInstance.getClass());
    }

    @Test
    public void test_read_ShouldReturnValidByte() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int result = testInstance.read();
        assertEquals(0x15, result);
    }

    @Test
    public void test_read_ShouldReturnEOF() throws IOException {
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        when(baseNCodec.hasData()).thenReturn(false);
        InputStream inputStream = mock(InputStream.class);
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int result = testInstance.read();
        assertEquals(BaseNCodecInputStream.EOF, result);
    }

    @Test
    public void test_read_ShouldDecodData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15, 0x16 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int result = testInstance.read();
        assertEquals(0x15, result);
        result = testInstance.read();
        assertEquals(0x16, result);
    }

    @Test
    public void test_read_ShouldEncodeData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15, 0x16 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int result;
        byte[] expected = new byte[] { 0x2D, 0x31, 0x33, 0x31 };
        while ((result = testInstance.read()) != BaseNCodecInputStream.EOF) {
            outputStream.write(result);
        }
        byte[] actual = outputStream.toByteArray();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void test_read_ShouldReturnEOFWhenNoMoreData() throws IOException {
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        InputStream inputStream = mock(InputStream.class);
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        when(baseNCodec.hasData()).thenReturn(false);
        when(inputStream.read()).thenReturn(-1);
        int result = testInstance.read();
        assertEquals(BaseNCodecInputStream.EOF, result);
    }

    @Test
    public void test_read_ShouldReadMultipleByte() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15, 0x16, 0x17, 0x18 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] expected = new byte[] { 0x15, 0x16, 0x17, 0x18 };
        byte[] buffer = new byte[4];
        int result = testInstance.read(buffer, 0, buffer.length);
        assertEquals(expected.length, result);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], buffer[i]);
        }
    }

    @Test
    public void test_markSupported_ShouldReturnFalse() {
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] {}), new BaseNCodec(), false);
        assertFalse(testInstance.markSupported());
    }

    @Test
    public void test_skip_ShouldSkipData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15, 0x16, 0x17, 0x18, 0x19 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int skipLength = 2;
        long currentPosition = testInstance.skip(skipLength);
        byte[] expected = new byte[] { 0x17, 0x18, 0x19 };
        byte[] buffer = new byte[skipLength];
        for (int i = 0; i < skipLength; i++) {
            assertEquals(expected[i], buffer[i]);
        }
        assertEquals(currentPosition + skipLength, testInstance.getChannel().position());
    }

    @Test
    public void test_skip_ShouldReturnNumberOfSkippedByte() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15, 0x16, 0x17, 0x18, 0x19 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int skipLength = 2;
        int result = testInstance.skip(skipLength);
        assertEquals(skipLength, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_read_ShouldThrowIndexOutOfBoundsExceptionWhenInvalidOffset() {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15, 0x16, 0x17, 0x18 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[5];
        int offset = buffer.length + 1;
        testInstance.read(buffer, offset, buffer.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_read_ShouldThrowIndexOutOfBoundsExceptionWhenInvalidLength() {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15, 0x16, 0x17, 0x18 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[5];
        int length = buffer.length * 2;
        testInstance.read(buffer, 0, length);
    }

    @Test(expected = NullPointerException.class)
    public void test_read_ShouldThrowNullPointerExceptionWhenNullBuffer() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15, 0x16, 0x17, 0x18 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        testInstance.read(null, 0, 4);
    }

    @Test
    public void test_close_ShouldCloseBaseNCodecInputStream() throws IOException {
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        InputStream inputStream = mock(InputStream.class);
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        testInstance.close();
        verify(baseNCodec).close();
        verify(inputStream).close();
    }

    @Test
    public void test_close_ShouldNotCloseBaseNCodecInputStreamWhenDoEncode() throws IOException {
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        InputStream inputStream = mock(InputStream.class);
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        testInstance.close();
        verify(baseNCodec, never()).close();
        verify(inputStream, never()).close();
    }

    @Test
    public void test_hasNext_ShouldReturnTrueWhenHasData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15, 0x16, 0x17, 0x18 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        when(baseNCodec.hasData()).thenReturn(true);
        byte[] buffer = new byte[4];
        int read = testInstance.read(buffer, 0, buffer.length);
        assertTrue(read > 0);
        assertTrue(testInstance.hasNext());
    }

    @Test
    public void test_hasNext_ShouldReturnFalseWhenNoMoreData() throws IOException {
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        InputStream inputStream = mock(InputStream.class);
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        when(baseNCodec.hasData()).thenReturn(false);
        assertFalse(testInstance.hasNext());
    }

    @Test
    public void test_hasNext_ShouldReturnFalseWhenClosed() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x15, 0x16, 0x17, 0x18 });
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        testInstance.close();
        assertFalse(testInstance.hasNext());
    }

}
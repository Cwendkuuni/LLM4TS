
package org.junit.tests;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {
    @Test
    public void testReadMethod() throws IOException {
        // Test reading valid bytes
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("ABCDEFG".getBytes());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        assertEquals(65, inputStream.read()); // A
        assertEquals(66, inputStream.read()); // B
        assertEquals(67, inputStream.read()); // C
        assertEquals(68, inputStream.read()); // D
        assertEquals(69, inputStream.read()); // E
        assertEquals(70, inputStream.read()); // F
        assertEquals(-1, inputStream.read()); // EOF

        // Test reading invalid byte
        InputStream invalidInput = new ByteArrayInputStream("ABCDEFG\u00FF".getBytes());
        BaseNCodecInputStream invalidInputStream = new BaseNCodecInputStream(invalidInput, baseNCodec, false);
        assertEquals(-1, invalidInputStream.read()); // EOF

        // Test reading empty stream
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream emptyInputStream = new BaseNCodecInputStream(emptyInput, baseNCodec, false);
        assertEquals(-1, emptyInputStream.read()); // EOF
    }

    @Test
    public void testReadBytesMethod() throws IOException {
        // Test reading valid bytes
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("ABCDEFG".getBytes());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] buffer = new byte[5];

        // Test reading valid bytes
        int bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"A", "B", "C", "D", "E"}, buffer);

        // Test reading partial data
        buffer = new byte[3];
        bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"A", "B", "C"}, buffer);

        // Test reading empty stream
        buffer = new byte[5];
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream emptyInputStream = new BaseNCodecInputStream(emptyInput, baseNCodec, false);
        bytesRead = emptyInputStream.read(buffer, 0, buffer.length);
        assertEquals(0, bytesRead);

        // Test reading invalid byte array
        byte[] invalidBuffer = null;
        InputStreamStream invalidInput = new ByteArrayInputStream("ABCDEFG".getBytes());
        BaseNCodecInputStream invalidInputStream = new BaseNCodecInputStream(invalidInput, baseNCodec, false);
        try {
            bytesRead = invalidInputStream.read(invalidBuffer, 0, buffer.length);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMarkSupportedMethod() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()), new BaseNCodec(), false);
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testSkipMethod() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("ABCDEFG".getBytes());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        long bytesToSkip = 3;
        long skippedBytes = inputStream.skip(bytesToSkip);

        assertEquals(3, skippedBytes);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"D", "E", "F", "G", "A"}, buffer);
    }
}
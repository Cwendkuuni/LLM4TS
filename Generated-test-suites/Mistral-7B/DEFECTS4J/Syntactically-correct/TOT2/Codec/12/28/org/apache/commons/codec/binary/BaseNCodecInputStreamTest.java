package org.apache.commons.codec.binary;

import static org.junit.Assert.*;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    @org.junit.Test
    public void testReadMethod() throws IOException {
        // Given
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-1, 25, 66, 72, 83});
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // When
        int result = testInstance.read();

        // Then
        assertEquals(25, result);
    }

    @org.junit.Test
    public void testReadMethodWithArray() throws IOException {
        // Given
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-1, 25, 66, 72, 83});
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] expectedOutput = {25};

        // When
        int bytesRead = testInstance.read(new byte[1][], 0, 1);
        byte[] output = new byte[bytesRead];
        System.arraycopy(new byte[] {25}, 0, output, 0, bytesRead);

        // Then
        assertArrayEquals(expectedOutput, output);
    }

    @org.junit.Test
    public void testMarkSupportedMethod() {
        // Given
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(null, null, false);

        // When
        boolean result = testInstance.markSupported();

        // Then
        assertFalse(result);
    }

    @org.junit.Test
    public void testReadMethodWithEOF() throws IOException {
        // Given
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-1});
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // When
        int result = testInstance.read();

        // Then
        assertEquals(-1, result);
    }

    @org.junit.Test
    public void testReadMethodWithArrayAndEOF() throws IOException {
        // Given
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-1});
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] expectedOutput = {-1};

        // When
        int bytesRead = testInstance.read(new byte[1][], 0, 1);
        byte[] output = new byte[bytesRead];
        System.arraycopy(new byte[] {-1}, 0, output, 0, bytesRead);

        // Then
        assertArrayEquals(expectedOutput, output);
    }
}
package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;

    @BeforeEach
    public void setup() throws IOException {
        input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        output = new ByteArrayOutputStream();
    }

    @Test
    public void testCopy() throws IOException {
        long copiedBytes = IOUtils.copy(input, output);
        byte[] expectedOutput = new byte[]{1, 2, 3, 4, 5};
        byte[] actualOutput = output.toByteArray();
        Assertions.assertEquals(expectedOutput.length, copiedBytes);
        Assertions.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] buffer = new byte[1024];
        long copiedBytes = IOUtils.copy(input, output, 1024);
        byte[] expectedOutput = new byte[5];
        System.arraycopy(new byte[]{1, 2, 3, 4, 5}, 0, expectedOutput, 0, 5);
        byte[] actualOutput = output.toByteArray();
        Assertions.assertEquals(expectedOutput.length, copiedBytes);
        Assertions.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSkip() throws IOException {
        long numToSkip = 2;
        long skippedBytes = IOUtils.skip(input, numToSkip);
        byte[] expectedInput = new byte[]{3, 4, 5};
        long expectedSkipped = 2;
        Assertions.assertArrayEquals(expectedInput, IOUtils.toByteArray(input));
        Assertions.assertEquals(expectedSkipped, skippedBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] expectedOutput = new byte[]{1, 2, 3, 4, 5};
        byte[] actualOutput = new byte[5];
        IOUtils.readFully(input, actualOutput);
        Assertions.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testReadFullyWithBufferSize() throws IOException {
        byte[] buffer = new byte[10];
        IOUtils.readFully(input, buffer, 0, 5);
        byte[] expectedOutput = new byte[]{1, 2, 3, 4, 5};
        byte[] actualOutput = new byte[5];
        System.arraycopy(buffer, 0, actualOutput, 0, 5);
        Assertions.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] expectedOutput = new byte[]{1, 2, 3, 4, 5};
        byte[] actualOutput = IOUtils.toByteArray(input);
        Assertions.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        try (ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5})){
            IOUtils.closeQuietly(input);
        }
        Assertions.assertDoesNotThrow(() -> IOUtils.closeQuietly(input));
    }
}
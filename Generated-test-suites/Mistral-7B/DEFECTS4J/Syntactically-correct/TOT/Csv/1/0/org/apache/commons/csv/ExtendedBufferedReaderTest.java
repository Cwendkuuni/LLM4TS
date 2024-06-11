package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for ExtendedBufferedReader.
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String input = "Line 1\nLine 2\nLine 3\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        // Typical use case
        Assert.assertEquals('L', reader.read()); // First character in the first line
        Assert.assertEquals('\n', reader.read()); // Line terminators are read and discarded
        Assert.assertEquals('i', reader.read()); // First character in the second line
        Assert.assertEquals(' ', reader.readAgain()); // Read again to check last character
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read()); // Reach EOF

        // Edge case: Empty input
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        // Edge case: Input with only line terminators
        input = "\n\n";
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        // Error case: InputStream closed
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())) {
            @Override
            public void close() throws IOException {
                throw new IOException("Closed");
            }
        });
        try {
            reader.read();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "Line 1\nLine 2\nLine 3\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        // Typical use case
        Assert.assertEquals("Line 1", reader.readLine());
        Assert.assertEquals("Line 2", reader.readLine());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

        // Edge case: Empty line
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("\n".getBytes())));
        Assert.assertEquals(null, reader.readLine());

        // Edge case: Input with only line terminators
        input = "\n\n";
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertEquals(null, reader.readLine());
        Assert.assertEquals(null, reader.readLine());

        // Error case: InputStream closed
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())) {
            @Override
            public void close() throws IOException {
                throw new IOException("Closed");
            }
        });
        try {
            reader.readLine();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "Line 1\nLine 2\nLine 3\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        // Typical use case
        Assert.assertEquals('L', reader.lookAhead()); // Look at the first character in the first line
        reader.read(); // Consume it
        Assert.assertEquals('i', reader.lookAhead()); // Look at the first character in the second line

        // Edge case: EOF
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());

        // Error case: InputStream closed
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())) {
            @Override
            public void close() throws IOException {
                throw new IOException("Closed");
            }
        });
        try {
            reader.lookAhead();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "Line 1\nLine 2\nLine 3\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        // Typical use case
        Assert.assertEquals(1, reader.getLineNumber());
        Assert.assertEquals(2, reader.getLineNumber());
        Assert.assertEquals(3, reader.getLineNumber());
        Assert.assertEquals(3, reader.getLineNumber()); // After reaching EOF

        // Edge case: Empty input
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        Assert.assertEquals(0, reader.getLineNumber());

        // Error case: InputStream closed
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())) {
            @Override
            public void close() throws IOException {
                throw new IOException("Closed");
            }
        });
        try {
            reader.getLineNumber();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }
}
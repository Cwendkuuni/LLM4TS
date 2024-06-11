package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ExtendedBufferedReaderTest.java
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testReadMethod() throws IOException {
        String testData = "Hello World\nThis is a test\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('T', reader.read());
        Assert.assertEquals('h', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('s', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('s', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('a', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('t', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('s', reader.read());
        Assert.assertEquals('t', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadLineMethod() throws IOException {
        String testData = "This is a test\nThis is another test\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        String line1 = reader.readLine();
        Assert.assertEquals("This is a test", line1);

        String line2 = reader.readLine();
        Assert.assertEquals("This is another test", line2);

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
    }

    @Test
    public void testPeekMethod() throws IOException {
        String testData = "This is a test\nThis is another test\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        int peekedChar = reader.lookAhead();
        Assert.assertEquals('\n', peekedChar);

        int peekedCharAfterFirstLine = reader.lookAhead();
        Assert.assertEquals('T', peekedCharAfterFirstLine);

        int peekedCharAtEndOfStream = reader.lookAhead();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, peekedCharAtEndOfStream);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenReadPastEndOfStream() throws IOException {
        String testData = "This is a test\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();

        reader.read(); // This should throw an IOException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenNegativeBufferSize() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        try {
            ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream, -1));
        } catch (IOException e) {
            // IOException is expected, but not the test case failure
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Negative size: -1", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testGetLineNumberMethod() throws IOException {
        String testData = "This is the first line\nThis is the second line\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        Assert.assertEquals(0, reader.getLineNumber());

        reader.readLine();
        Assert.assertEquals(1, reader.getLineNumber());

        reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.getLineNumber());
    }
}
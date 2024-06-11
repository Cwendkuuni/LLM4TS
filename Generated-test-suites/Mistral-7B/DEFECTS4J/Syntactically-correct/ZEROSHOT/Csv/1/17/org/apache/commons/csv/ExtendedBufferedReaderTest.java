package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest.java
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReader() throws Exception {
        String input = "Line 1\rLine 2\rLine 3\rLine 4\rLine 5\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        // Test read method
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('\r', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('\r', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('g', reader.read());
        Assert.assertEquals('\r', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        // Test readAgain method
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals('L', reader.readAgain());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('i', reader.readAgain());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.readAgain());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('n', reader.readAgain());
        Assert.assertEquals('\r', reader.read());
        Assert.assertEquals('\r', reader.readAgain());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.readAgain());
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals('L', reader.readAgain());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.readAgain());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('i', reader.readAgain());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.readAgain());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('n', reader.readAgain());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.readAgain());
        Assert.assertEquals('g', reader.read());
        Assert.assertEquals('\r', reader.read());
        Assert.assertEquals('\r', reader.readAgain());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.readAgain());
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals('L', reader.readAgain());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.readAgain());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('i', reader.readAgain());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.readAgain());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('n', reader.readAgain());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());

        // Test readLine method
        Assert.assertEquals("Line 1\r", reader.readLine());
        Assert.assertEquals("Line 2\r", reader.readLine());
        Assert.assertEquals("Line 3\r", reader.readLine());
        Assert.assertEquals("Line 4\r", reader.readLine());
        Assert.assertEquals("Line 5\n", reader.readLine());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

        // Test lookAhead method
        Assert.assertEquals('L', reader.lookAhead());
        Assert.assertEquals('L', reader.lookAhead());
        Assert.assertEquals('i', reader.lookAhead());
        Assert.assertEquals('i', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals('n', reader.lookAhead());
        Assert.assertEquals('n', reader.lookAhead());
        Assert.assertEquals('\r', reader.lookAhead());
        Assert.assertEquals('\r', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals('L', reader.lookAhead());
        Assert.assertEquals('L', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals('i', reader.lookAhead());
        Assert.assertEquals('i', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals('n', reader.lookAhead());
        Assert.assertEquals('n', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals('g', reader.lookAhead());
        Assert.assertEquals('\r', reader.lookAhead());
        Assert.assertEquals('\r', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals('L', reader.lookAhead());
        Assert.assertEquals('L', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals('i', reader.lookAhead());
        Assert.assertEquals('i', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals('n', reader.lookAhead());
        Assert.assertEquals('n', reader.lookAhead());
        Assert.assertEquals(' ', reader.lookAhead());
        Assert.assertEquals('\n', reader.lookAhead());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());

        // Test getLineNumber method
        Assert.assertEquals(0, reader.getLineNumber());
        Assert.assertEquals(1, reader.readLine() == null ? 0 : reader.getLineNumber());
        Assert.assertEquals(2, reader.readLine() == null ? 0 : reader.getLineNumber());
        Assert.assertEquals(3, reader.readLine() == null ? 0 : reader.getLineNumber());
        Assert.assertEquals(4, reader.readLine() == null ? 0 : reader.getLineNumber());
        Assert.assertEquals(5, reader.getLineNumber());
        Assert.assertEquals(-1, reader.getLineNumber());
    }
}
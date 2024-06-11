package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReader() throws IOException {
        String input = "Line 1\rLine 2\rLine 3\rLine 4\rLine 5";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int expectedLineNumber = 0;
        int lastChar;

        // Test read method
        char[] buffer = new char[1];
        int ch;
        while ((ch = extendedBufferedReader.read()) != ExtendedBufferedReader.END_OF_STREAM) {
            Assert.assertNotEquals(ExtendedBufferedReader.END_OF_STREAM, ch);
            buffer[0] = (char) ch;
            Assert.assertEquals(String.valueOf(buffer[0]), Character.toString(buffer[0]));
            lastChar = ch;
            expectedLineNumber++;
        }
        Assert.assertEquals(5, expectedLineNumber);
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);

        // Test readAgain method
        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals(ExtendedBufferedReader.UNDEFINED, lastChar);

        // Test readLine method
        String line = extendedBufferedReader.readLine();
        Assert.assertNotNull(line);
        Assert.assertEquals("Line 1\r", line);
        expectedLineNumber++;
        lastChar = extendedBufferedReader.readChar();
        Assert.assertNotEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
        Assert.assertEquals('\r', lastChar);

        line = extendedBufferedReader.readLine();
        Assert.assertNotNull(line);
        Assert.assertEquals("Line 2\r", line);
        expectedLineNumber++;
        lastChar = extendedBufferedReader.readChar();
        Assert.assertNotEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
        Assert.assertEquals('\r', lastChar);

        line = extendedBufferedReader.readLine();
        Assert.assertNotNull(line);
        Assert.assertEquals("Line 3\r", line);
        expectedLineNumber++;
        lastChar = extendedBufferedReader.readChar();
        Assert.assertNotEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
        Assert.assertEquals('\r', lastChar);

        line = extendedBufferedReader.readLine();
        Assert.assertNotNull(line);
        Assert.assertEquals("Line 4\r", line);
        expectedLineNumber++;
        lastChar = extendedBufferedReader.readChar();
        Assert.assertNotEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
        Assert.assertEquals('\r', lastChar);

        line = extendedBufferedReader.readLine();
        Assert.assertNull(line);
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);

        // Test lookAhead method
        int lookAheadChar = extendedBufferedReader.lookAhead();
        Assert.assertNotEquals(ExtendedBufferedReader.END_OF_STREAM, lookAheadChar);
        Assert.assertEquals(expectedLineNumber == 5 ? '\n' : '\r', lookAheadChar);

        // Test getLineNumber method
        Assert.assertEquals(5, extendedBufferedReader.getLineNumber());
    }
}
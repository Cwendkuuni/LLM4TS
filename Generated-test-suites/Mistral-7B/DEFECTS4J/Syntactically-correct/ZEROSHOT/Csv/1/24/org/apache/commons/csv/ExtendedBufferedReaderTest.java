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
        String input = "Line 1\rLine 2\rLine 3\rLine 4\rLine 5\r";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        // Test read method
        char[] buffer = new char[1];
        int readChar;
        for (int i = 0; i < input.length(); i++) {
            readChar = reader.read();
            Assert.assertEquals(input.charAt(i), (char) readChar);
            if (readChar == '\r' || readChar == '\n') {
                Assert.assertEquals(reader.getLineNumber(), i / (readChar == '\r' ? 2 : 1) + 1);
            }
            buffer[0] = (char) readChar;
            Assert.assertEquals(reader.readLine(), new String(buffer));
        }
        readChar = reader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, readChar);

        // Test readAgain method
        readChar = reader.read();
        Assert.assertEquals(input.charAt(0), (char) reader.readAgain());
        Assert.assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());

        // Test lookAhead method
        reader.mark(0);
        readChar = reader.lookAhead();
        Assert.assertEquals(input.charAt(0), (char) readChar);
        reader.reset();

        // Test getLineNumber method
        Assert.assertEquals(-1, reader.getLineNumber());
        for (int i = 0; i < input.length(); i++) {
            char c = (char) reader.read();
            Assert.assertNotEquals(-1, reader.getLineNumber());
        }
        Assert.assertEquals(-1, reader.getLineNumber());
    }
}
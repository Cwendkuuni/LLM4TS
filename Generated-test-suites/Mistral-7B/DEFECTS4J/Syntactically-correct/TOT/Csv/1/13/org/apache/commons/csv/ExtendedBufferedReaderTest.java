package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String input = "Hello World\n123\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('H', reader.readAgain()); // Should return the last read character
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('W', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('r', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('d', reader.read());
        Assert.assertEquals('\n', reader.read()); // line counter should be incremented
        Assert.assertEquals(-1, reader.read()); // End of stream reached

        // Test reading an empty line
        String emptyLine = "\n";
        reader = new ExtendedBufferedReader(new StringReader(emptyLine));
        Assert.assertEquals(-1, reader.read()); // End of stream reached as there's no character to read
        Assert.assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadArray() throws IOException {
        String input = "Hello World\n123\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        char[] buffer = new char[input.length()];
        int length = reader.read(buffer, 0, buffer.length);
        Assert.assertEquals(input.length(), length);

        for (int i = 0; i < input.length(); i++) {
            Assert.assertEquals(input.charAt(i), buffer[i]);
        }

        Assert.assertEquals(input.length(), reader.getLineNumber());
        Assert.assertEquals(-1, reader.read()); // End of stream reached
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "Hello World\n123\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        String line1 = reader.readLine();
        Assert.assertEquals("Hello World", line1);
        Assert.assertEquals('W', reader.read()); // Should return the last character on the line

        String line2 = reader.readLine();
        Assert.assertEquals("123", line2);
        Assert.assertEquals(-1, reader.read()); // End of stream reached

        Assert.assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "Hello World\n123\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        Assert.assertEquals('H', reader.lookAhead());
        Assert.assertEquals('H', reader.read());

        super.mark(1);
        Assert.assertEquals('e', reader.lookAhead());
        Assert.assertEquals('e', reader.read());

        super.mark(1);
        Assert.assertEquals('l', reader.lookAhead());
        Assert.assertEquals('l', reader.read());

        Assert.assertEquals(-1, reader.lookAhead()); // End of stream reached

        // Test lookahead with an empty reader
        ExtendedBufferedReader emptyReader = new ExtendedBufferedReader(new ByteArrayInputStream(new byte[0]));
        Assert.assertEquals(-1, reader.lookAhead());
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeLookAhead() throws IOException {
        String input = "Hello World\n123\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        reader.lookAhead(-1); // This should throw an IllegalStateException as lookahead can't be negative
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "Hello World\n123\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        Assert.assertEquals(0, reader.getLineNumber());

        String line1 = reader.readLine();
        Assert.assertEquals("Hello World", line1);
        Assert.assertEquals(1, reader.getLineNumber());

        String line2 = reader.readLine();
        Assert.assertEquals("123", line2);
        Assert.assertEquals(2, reader.getLineNumber());

        Assert.assertEquals(-1, reader.read()); // End of stream reached
    }
}
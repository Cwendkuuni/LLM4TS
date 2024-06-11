package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testReadInt() throws IOException {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        // Test read with empty buffer
        int result = reader.read();
        Assert.assertEquals(-1, result);
        Assert.assertEquals(-1, reader.getLineNumber());

        // Test read with filled buffer
        char[] buffer = new char[1];
        result = reader.read(buffer, 0, 1);
        Assert.assertEquals('H', buffer[0]);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('e', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('l', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('l', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('\n', result);
        Assert.assertEquals(1, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('W', result);
        Assert.assertEquals(1, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('o', result);
        Assert.assertEquals(1, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('r', result);
        Assert.assertEquals(1, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('l', result);
        Assert.assertEquals(1, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('\n', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals(-1, result);
        Assert.assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testLookAheadInt() throws IOException {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        // Test lookAhead with empty buffer
        int result = reader.lookAhead();
        Assert.assertEquals(-1, result);

        // Test lookAhead with filled buffer
        char[] buffer = new char[1];
        result = reader.read(buffer, 0, 1);
        Assert.assertEquals('H', buffer[0]);
        reader.mark(1);
        result = reader.lookAhead();
        Assert.assertEquals('e', result);
        reader.reset();

        result = reader.lookAhead();
        Assert.assertEquals('H', result);

        result = reader.read();
        Assert.assertEquals('e', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals('l', result);

        result = reader.read();
        Assert.assertEquals('l', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals('l', result);

        result = reader.read();
        Assert.assertEquals('\n', result);
        Assert.assertEquals(1, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals('W', result);

        result = reader.readLine();
        Assert.assertEquals("World\n", reader.readLine());
        Assert.assertEquals(-1, reader.read());
        Assert.assertEquals(2, reader.getLineNumber());
    }
}
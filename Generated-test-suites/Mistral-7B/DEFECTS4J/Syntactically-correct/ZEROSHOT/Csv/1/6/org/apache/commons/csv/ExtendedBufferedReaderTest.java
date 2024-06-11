package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        String input = "Hello\nWorld!";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        int result;

        result = reader.read();
        Assert.assertEquals('H', result);
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
        Assert.assertEquals('o', result);
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
        Assert.assertEquals('d', result);
        Assert.assertEquals(1, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('!', result);
        Assert.assertEquals(1, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        Assert.assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testPeek() throws Exception {
        String input = "Hello\nWorld!";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        int result;

        result = reader.lookAhead();
        Assert.assertEquals('H', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('H', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals('e', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('e', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals('l', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.readLine();
        Assert.assertEquals("World!\n", reader.readLine());
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals('W', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('W', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals('o', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('o', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals('r', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('r', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals('l', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('d', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals('!', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('!', result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.lookAhead();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        Assert.assertEquals(2, reader.getLineNumber());
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeLookAhead() throws Exception {
        String input = "Hello\nWorld!";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        reader.lookAhead(-1);
    }
}
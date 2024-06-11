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
    public void testRead() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("line1\nline2\nline3".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('1', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('2', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('3', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        Assert.assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("abcd\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        Assert.assertEquals('a', reader.lookAhead(), "Look ahead should return the next character without consuming it");
        Assert.assertEquals('a', reader.read(), "The next character after lookAhead() should be the same as the one looked up");

        reader.mark(1);
        Assert.assertEquals('b', reader.lookAhead(), "Mark and lookAhead() should not change current position");
        reader.reset();
        Assert.assertEquals('a', reader.read());
        Assert.assertEquals('b', reader.read());
    }

    @Test(expected = IOException.class)
    public void testExceptionWhenMarkAfterEndOfStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("abcd\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.mark(1);
        reader.read();
    }

    @Test
    public void testPeek() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("abcd\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        Assert.assertEquals('a', reader.peek(), "Peek should not change current position and return next character");
        Assert.assertEquals('a', reader.read(), "The next character after peek() should be the same as the one peeked at");
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("abcd\n".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        char[] buffer = new char[5];
        int length = reader.read(buffer, 0, buffer.length);
        Assert.assertEquals(4, length, "Read should have read 4 characters");
        Assert.assertEquals("abcd", new String(buffer), "Buffer should contain the read characters");
        Assert.assertEquals('d', reader.read(), "Next character should be 'd'");
        Assert.assertEquals(5, reader.getLineNumber(), "5 lines should be read in total");
    }

    @Test
    public void testReadEmptyLine() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        String line = reader.readLine();
        Assert.assertNull(line, "readLine should return null for empty line");
        Assert.assertEquals(0, reader.getLineNumber(), "No line should be read");
    }
}
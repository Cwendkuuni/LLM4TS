package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        assertEquals('H', reader.read());
        assertEquals('e', reader.read());
        assertEquals('l', reader.read());
        assertEquals('l', reader.read());
        assertEquals('o', reader.read());
        assertEquals(' ', reader.read());
        assertEquals('W', reader.read());
        assertEquals('o', reader.read());
        assertEquals('r', reader.read());
        assertEquals('l', reader.read());
        assertEquals('d', reader.read());
        assertEquals('\n', reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        String line = reader.readLine();
        assertEquals("Hello World", line);
        assertEquals('\n', reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testPeek() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        assertEquals('H', reader.lookAhead());
        assertEquals('e', reader.read());
        assertEquals('l', reader.read());
        // assert read doesn't change the lookahead char
        assertEquals('l', reader.lookAhead());
        assertEquals('o', reader.read());
        // the lookahead char is now the next char
        assertEquals('W', reader.lookAhead());
        assertEquals('o', reader.read());
        assertEquals('r', reader.read());
        assertEquals('l', reader.read());
        assertEquals('d', reader.read());
        assertEquals('\n', reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test(expected = IOException.class)
    public void testNegativeBufferSize() throws Exception {
        new ExtendedBufferedReader(new StringReader(""), -1);
    }

    @Test(expected = IOException.class)
    public void testMarkAfterRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        reader.read();
        reader.mark(1);
        reader.read();
        reader.reset();
        assertEquals('H', reader.read());
    }

    @Test(expected = IOException.class)
    public void testMarkAfterPeek() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        reader.mark(1);
        reader.lookAhead();
        reader.reset();
        assertEquals('H', reader.read());
    }

    @Test(expected = IOException.class)
    public void testMarkAfterReadPastEnd() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        reader.read();
        reader.mark(2);
        reader.reset();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testLineCounter() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\nHello Again\n"));
        assertEquals(2, reader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }
}
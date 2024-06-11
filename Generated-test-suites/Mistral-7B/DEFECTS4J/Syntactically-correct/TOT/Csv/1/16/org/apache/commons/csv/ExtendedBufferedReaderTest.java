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
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
        // Test reading individual characters
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('W', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('r', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        // Test reading multiple characters
        char[] buffer = new char[2];
        Assert.assertEquals(2, reader.read(buffer, 0, buffer.length));
        Assert.assertEquals('He', new String(buffer));

        // Test reading at EOF
        buffer = new char[1];
        Assert.assertEquals(0, reader.read(buffer, 0, buffer.length));
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        // Test reading with malformed input (incomplete line)
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        buffer = new char[2];
        Assert.assertEquals(1, reader.read(buffer, 0, buffer.length));
        Assert.assertEquals('H', buffer[0]);
        reader.read(); // read '\n'
        Assert.assertEquals(1, reader.read(buffer, 0, buffer.length));
        Assert.assertEquals('o', buffer[0]);
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));

        // Test reading single line
        String line = reader.readLine();
        Assert.assertEquals("Hello", line);
        Assert.assertEquals(1, reader.getLineNumber());

        line = reader.readLine();
        Assert.assertEquals("World", line);
        Assert.assertEquals(2, reader.getLineNumber());

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

        // Test reading multiple lines
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\nHello again\n"));
        line = reader.readLine();
        Assert.assertEquals("Hello", line);
        Assert.assertEquals(1, reader.getLineNumber());

        line = reader.readLine();
        Assert.assertEquals("World", line);
        Assert.assertEquals(2, reader.getLineNumber());

        line = reader.readLine();
        Assert.assertEquals("Hello again", line);
        Assert.assertEquals(3, reader.getLineNumber());

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

        // Test reading empty line
        reader = new ExtendedBufferedReader(new StringReader(""));
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

        // Test reading malformed input (incomplete line)
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        line = reader.readLine();
        Assert.assertEquals("Hello", line);
        Assert.assertEquals(1, reader.getLineNumber());

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());

    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("HelloWorld"));

        // Test looking ahead to next character
        Assert.assertEquals('H', reader.lookAhead());
        Assert.assertEquals('e', reader.read());

        // Test looking ahead to multiple characters
        char[] buffer = new char[2];
        reader.mark(1);
        reader.read();
        reader.read(buffer, 0, buffer.length);
        Assert.assertEquals("He", new String(buffer));
        Assert.assertEquals(2, reader.getLineNumber());

        // Test looking ahead at EOF
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());

        // Test looking ahead with malformed input (incomplete line)
        reader = new ExtendedBufferedReader(new StringReader("Hello"));
        Assert.assertEquals('H', reader.lookAhead());
        reader.read(); // read 'H'
        Assert.assertEquals('\n', reader.lookAhead());
        reader.read(); // read '\n'
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }
}
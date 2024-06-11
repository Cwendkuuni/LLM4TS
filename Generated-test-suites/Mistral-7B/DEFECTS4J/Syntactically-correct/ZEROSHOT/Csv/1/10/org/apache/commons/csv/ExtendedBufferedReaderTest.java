package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for ExtendedBufferedReader
 */
public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        String input = "Hello\rWorld\n1\r2\r3\n";
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
    }

    @Test
    public void testRead() throws IOException {
        Assert.assertEquals('H', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('\r', reader.read());
        Assert.assertEquals('W', reader.read());
        Assert.assertEquals('o', reader.read());
        Assert.assertEquals('r', reader.read());
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('d', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals('1', reader.read());
        Assert.assertEquals('\r', reader.read());
        Assert.assertEquals('2', reader.read());
        Assert.assertEquals('\r', reader.read());
        Assert.assertEquals('3', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(-1, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
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

        Assert.assertEquals('o', reader.readAgain());
        Assert.assertEquals('r', reader.readAgain());
        Assert.assertEquals('l', reader.readAgain());
        Assert.assertEquals('d', reader.readAgain());
        Assert.assertEquals('\n', reader.readAgain());
        Assert.assertEquals('1', reader.readAgain());
        Assert.assertEquals('\r', reader.readAgain());
        Assert.assertEquals('2', reader.readAgain());
        Assert.assertEquals('\r', reader.readAgain());
        Assert.assertEquals('3', reader.readAgain());
        Assert.assertEquals('\n', reader.readAgain());
        Assert.assertEquals(-1, reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        Assert.assertEquals("Hello\r", reader.readLine());
        Assert.assertEquals("World\n", reader.readLine());
        Assert.assertEquals("1\r", reader.readLine());
        Assert.assertEquals("2\r", reader.readLine());
        Assert.assertEquals("3\n", reader.readLine());
        Assert.assertEquals(null, reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        Assert.assertEquals('H', reader.lookAhead());
        reader.read();
        Assert.assertEquals('e', reader.lookAhead());
        reader.read();
        Assert.assertEquals('l', reader.lookAhead());
        reader.read();
        Assert.assertEquals('l', reader.lookAhead());
        reader.read();
        Assert.assertEquals('o', reader.lookAhead());
        reader.read();
        Assert.assertEquals('\r', reader.lookAhead());
        reader.readLine();
        Assert.assertEquals('W', reader.lookAhead());
        Assert.assertEquals('o', reader.lookAhead());
        Assert.assertEquals('r', reader.lookAhead());
        Assert.assertEquals('l', reader.lookAhead());
        Assert.assertEquals('d', reader.lookAhead());
        Assert.assertEquals('\n', reader.lookAhead());
        Assert.assertEquals('1', reader.lookAhead());
        Assert.assertEquals('\r', reader.lookAhead());
        Assert.assertEquals('2', reader.lookAhead());
        Assert.assertEquals('\r', reader.lookAhead());
        Assert.assertEquals('3', reader.lookAhead());
        Assert.assertEquals('\n', reader.lookAhead());
        Assert.assertEquals(-1, reader.lookAhead());
    }

    @Test
    public void testLineNumber() throws IOException {
        Assert.assertEquals(0, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(1, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(3, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(3, reader.getLineNumber()); // Should not increment on empty line
        Assert.assertEquals(null, reader.readLine());
        Assert.assertEquals(3, reader.getLineNumber());
    }
}
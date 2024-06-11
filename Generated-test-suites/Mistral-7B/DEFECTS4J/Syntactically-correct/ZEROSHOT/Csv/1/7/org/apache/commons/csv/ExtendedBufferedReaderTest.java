package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReader() throws IOException {
        String input = "Line 1\rLine 2\rLine 3\rLine 4\rLine 5";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        testRead(extReader);
        testReadAgain(extReader);
        testReadLine(extReader);
        testLookAhead(extReader);
        testGetLineNumber(extReader);

        extReader.close();
    }

    private void testRead(ExtendedBufferedReader extReader) throws IOException {
        int ch;

        ch = extReader.read();
        Assert.assertNotEquals(ExtendedBufferedReader.END_OF_STREAM, ch);
        Assert.assertEquals('L', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('i', (char) ch);
        ch = extReader.read();
        Assert.assertEquals(' ', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('\r', (char) ch);

        ch = extReader.read();
        Assert.assertEquals('L', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('i', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('n', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('e', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('\r', (char) ch);

        ch = extReader.read();
        Assert.assertEquals('L', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('i', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('n', (char) ch);
        ch = extReader.read();
        Assert.assertEquals(' ', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('\r', (char) ch);

        ch = extReader.read();
        Assert.assertEquals('L', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('i', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('n', (char) ch);
        ch = extReader.read();
        Assert.assertEquals(' ', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('\r', (char) ch);

        ch = extReader.read();
        Assert.assertEquals('L', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('i', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('n', (char) ch);
        ch = extReader.read();
        Assert.assertEquals(' ', (char) ch);
        ch = extReader.read();
        Assert.assertEquals('\r', (char) ch);

        ch = extReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, ch);

        Assert.assertEquals(5, extReader.getLineNumber());
    }

    private void testReadAgain(ExtendedBufferedReader extReader) throws IOException {
        int ch;

        ch = extReader.read();
        Assert.assertNotEquals(ExtendedBufferedReader.END_OF_STREAM, ch);
        Assert.assertEquals('L', (char) ch);

        ch = extReader.readAgain();
        Assert.assertEquals('L', (char) ch);

        ch = extReader.read();
        Assert.assertEquals('i', (char) ch);

        ch = extReader.readAgain();
        Assert.assertEquals('i', (char) ch);

        ch = extReader.read();
        Assert.assertEquals('n', (char) ch);

        ch = extReader.readAgain();
        Assert.assertEquals('n', (char) ch);

        ch = extReader.read();
        Assert.assertEquals(' ', (char) ch);

        ch = extReader.readAgain();
        Assert.assertEquals(' ', (char) ch);

        ch = extReader.read();
        Assert.assertEquals('\r', (char) ch);

        ch = extReader.readAgain();
        Assert.assertEquals('\r', (char) ch);

        ch = extReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, ch);

        Assert.assertEquals(5, extReader.getLineNumber());
    }

    private void testReadLine(ExtendedBufferedReader extReader) throws IOException {
        String line;

        line = extReader.readLine();
        Assert.assertNotNull(line);
        Assert.assertEquals("Line 1\r", line);
        Assert.assertEquals(1, extReader.getLineNumber());

        line = extReader.readLine();
        Assert.assertNotNull(line);
        Assert.assertEquals("Line 2\r", line);
        Assert.assertEquals(2, extReader.getLineNumber());

        line = extReader.readLine();
        Assert.assertNotNull(line);
        Assert.assertEquals("Line 3\r", line);
        Assert.assertEquals(3, extReader.getLineNumber());

        line = extReader.readLine();
        Assert.assertNotNull(line);
        Assert.assertEquals("Line 4\r", line);
        Assert.assertEquals(4, extReader.getLineNumber());

        line = extReader.readLine();
        Assert.assertNull(line);
        Assert.assertEquals(5, extReader.getLineNumber());
    }

    private void testLookAhead(ExtendedBufferedReader extReader) throws IOException {
        int ch;
        int lookAhead;

        ch = extReader.read();
        Assert.assertNotEquals(ExtendedBufferedReader.END_OF_STREAM, ch);

        lookAhead = extReader.lookAhead();
        Assert.assertEquals('i', (char) lookAhead);
        Assert.assertEquals(1, extReader.getLineNumber());

        extReader.mark(1);
        ch = extReader.read();
        Assert.assertEquals('i', (char) ch);
        extReader.reset();

        lookAhead = extReader.lookAhead();
        Assert.assertEquals('n', (char) lookAhead);
        Assert.assertEquals(1, extReader.getLineNumber());

        extReader.mark(1);
        ch = extReader.read();
        Assert.assertEquals('n', (char) ch);
        extReader.reset();

        lookAhead = extReader.lookAhead();
        Assert.assertEquals(' ', (char) lookAhead);
        Assert.assertEquals(1, extReader.getLineNumber());

        extReader.mark(1);
        ch = extReader.read();
        Assert.assertEquals(' ', (char) ch);
        extReader.reset();

        lookAhead = extReader.lookAhead();
        Assert.assertEquals('\r', (char) lookAhead);
        Assert.assertEquals(1, extReader.getLineNumber());

        extReader.mark(1);
        ch = extReader.read();
        Assert.assertEquals('\r', (char) ch);
        extReader.reset();

        lookAhead = extReader.lookAhead();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, lookAhead);
        Assert.assertEquals(5, extReader.getLineNumber());
    }

    private void testGetLineNumber(ExtendedBufferedReader extReader) throws IOException {
        int lineNumber;

        lineNumber = extReader.getLineNumber();
        Assert.assertEquals(-1, lineNumber);

        String line = extReader.readLine();
        Assert.assertNotNull(line);
        lineNumber = extReader.getLineNumber();
        Assert.assertEquals(1, lineNumber);

        line = extReader.readLine();
        Assert.assertNotNull(line);
        lineNumber = extReader.getLineNumber();
        Assert.assertEquals(2, lineNumber);

        line = extReader.readLine();
        Assert.assertNotNull(line);
        lineNumber = extReader.getLineNumber();
        Assert.assertEquals(3, lineNumber);

        line = extReader.readLine();
        Assert.assertNotNull(line);
        lineNumber = extReader.getLineNumber();
        Assert.assertEquals(4, lineNumber);

        line = extReader.readLine();
        Assert.assertNull(line);
        lineNumber = extReader.getLineNumber();
        Assert.assertEquals(5, lineNumber);
    }
}
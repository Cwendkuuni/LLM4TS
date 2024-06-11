package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVFormat;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReader() throws IOException {
        String input = "Line 1\nLine 2\nLine 3\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        // Test read method
        int c1 = extReader.read();
        Assert.assertEquals('L', c1);
        int c2 = extReader.read();
        Assert.assertEquals('i', c2);
        int c3 = extReader.read();
        Assert.assertEquals(' ', c3);
        int eof = extReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, eof);

        // Test readLine method
        String line1 = extReader.readLine();
        Assert.assertEquals("Line 1", line1);
        int lineCounterBeforeReadLine2 = extReader.getLineNumber();
        String line2 = extReader.readLine();
        Assert.assertEquals("Line 2", line2);
        Assert.assertEquals(lineCounterBeforeReadLine2 + 1, extReader.getLineNumber());
        String line3 = extReader.readLine();
        Assert.assertEquals("Line 3", line3);
        int lineCounterAfterReadLine3 = extReader.getLineNumber();
        Assert.assertEquals(lineCounterAfterReadLine3 + 1, extReader.getLineNumber()); // Reading EOF should increment line counter
        String eofLine = extReader.readLine();
        Assert.assertNull(eofLine);

        // Test readAgain method
        int c4 = extReader.read();
        Assert.assertEquals(ExtendedBufferedReader.UNDEFINED, c4);
        int c5 = extReader.readAgain();
        Assert.assertEquals(' ', c5); // Should return last read character

        // Test lookAhead method
        int lookAheadChar = extReader.lookAhead();
        Assert.assertEquals('L', lookAheadChar); // Should return next character without consuming it

        // Test read method with char array
        char[] buffer = new char[5];
        int readLength = extReader.read(buffer, 0, buffer.length);
        Assert.assertEquals(3, readLength);
        Assert.assertEquals("Line 1", new String(buffer, 0, readLength));
    }
}
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
    public void testConstruct() {
        Reader reader = new StringReader("line1\nline2\nline3");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        Assert.assertNotNull(ebr);
        Assert.assertEquals(reader, ebr.getUnderlyingReader());
    }

    @Test
    public void testRead() throws Exception {
        Reader reader = new StringReader("line1\nline2\nline3");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int expectedChar = -1;
        int actualChar;

        for (int i = 0; i < 3; i++) {
            expectedChar = i == 2 ? ExtendedBufferedReader.END_OF_STREAM : 'l' + i;
            actualChar = ebr.read();
            Assert.assertEquals(expectedChar, actualChar);
            Assert.assertEquals("line" + (i + 1), ebr.readLine());
            Assert.assertEquals(expectedChar == ExtendedBufferedReader.END_OF_STREAM ? ExtendedBufferedReader.END_OF_STREAM : (char) expectedChar, ebr.readAgain());
        }

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
    }

    @Test
    public void testLookAhead() throws Exception {
        Reader reader = new StringReader("line1\nline2\nline3");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int expectedChar = -1;
        int actualChar;

        for (int i = 0; i < 3; i++) {
            expectedChar = i == 2 ? ExtendedBufferedReader.END_OF_STREAM : 'l' + i;
            actualChar = ebr.lookAhead();
            Assert.assertEquals(expectedChar, actualChar);
            Assert.assertEquals("line" + (i + 1), ebr.readLine());
            Assert.assertEquals(expectedChar == ExtendedBufferedReader.END_OF_STREAM ? ExtendedBufferedReader.END_OF_STREAM : (char) expectedChar, ebr.readAgain());
        }

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.lookAhead());
    }

    @Test
    public void testReadLine() throws Exception {
        Reader reader = new StringReader("line1\nline2\nline3");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        String expectedLine;
        String actualLine;

        for (int i = 0; i < 3; i++) {
            expectedLine = "line" + (i + 1);
            actualLine = ebr.readLine();
            Assert.assertEquals(expectedLine, actualLine);
            Assert.assertEquals((i == 2 ? ExtendedBufferedReader.END_OF_STREAM : 'l' + i), ebr.readAgain());
        }

        Assert.assertEquals(null, ebr.readLine());
    }

    @Test(expected = IllegalStateException.class)
    public void testReadLineAfterEOF() throws Exception {
        Reader reader = new StringReader("line1\nline2\nline3");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        ebr.readLine();
        ebr.readLine();
        ebr.readLine();

        ebr.readLine();
    }
}
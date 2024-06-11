package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for ExtendedBufferedReader
 */
public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() throws IOException {
        String input = "Line 1\nLine 2\rLine 3\nLine 4\rLine 5\rLine 6\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes());
        reader = new ExtendedBufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    @Test
    public void testRead() throws IOException {
        int expected = '\n';
        int actual = reader.read();

        Assert.assertEquals(expected, actual);

        expected = '3';
        actual = reader.read();

        Assert.assertEquals(expected, actual);

        expected = reader.END_OF_STREAM;
        actual = reader.read();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadAgain() throws IOException {
        int expected = '\n';
        int actual = reader.read();
        int actual2 = reader.readAgain();

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, actual2);

        expected = '3';
        actual = reader.read();
        actual2 = reader.readAgain();

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, actual2);

        expected = reader.END_OF_STREAM;
        actual = reader.read();
        actual2 = reader.readAgain();

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, actual2);
    }

    @Test
    public void testReadLine() throws IOException {
        String expected = "Line 1";
        String actual = reader.readLine();

        Assert.assertEquals(expected, actual);

        expected = "Line 2";
        actual = reader.readLine();

        Assert.assertEquals(expected, actual);

        expected = "Line 3";
        actual = reader.readLine();

        Assert.assertEquals(expected, actual);

        expected = "Line 4";
        actual = reader.readLine();

        Assert.assertEquals(expected, actual);

        expected = null;
        actual = reader.readLine();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLookAhead() throws IOException {
        int expected = 'L';
        int actual = reader.lookAhead();

        super.mark(1);
        String expectedLine = reader.readLine();
        int actualLine = reader.read();

        reader.reset();

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expectedLine.charAt(0), actualLine);
    }

    @Test(expected = IllegalStateException.class)
    public void testLookAheadWithNoChar() throws IOException {
        int actual = reader.lookAhead();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        int expected = 6;
        int actual = reader.getLineNumber();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCSVParsing() throws IOException {
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        CSVRecord record;

        record = parser.getHeaderMap();
        Assert.assertEquals(0, record.size());

        record = parser.nextRecord();
        Assert.assertEquals("Line 1", record.get(0));
        Assert.assertEquals("Line 2", record.get(1));
        Assert.assertEquals("Line 3", record.get(2));

        record = parser.nextRecord();
        Assert.assertEquals("Line 4", record.get(0));
        Assert.assertEquals("Line 5", record.get(1));
        Assert.assertEquals("Line 6", record.get(2));

        record = parser.nextRecord();
        Assert.assertNull(record);

        record = parser.nextRecord();
        Assert.assertNull(record);
    }
}
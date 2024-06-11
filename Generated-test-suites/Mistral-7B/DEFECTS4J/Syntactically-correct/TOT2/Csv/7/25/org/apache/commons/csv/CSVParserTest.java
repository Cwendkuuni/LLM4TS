package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

import org.junit.Test;

/**
 * CSVParser test cases.
 */
public class CSVParserTest {

    /**
     * Test basic parsing of CSV file.
     */
    @Test
    public void testParseBasic() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc,d"), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"a", "b"}, record.getValues());
        assertNull(record.getComment());
        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test parsing of CSV file with header.
     */
    @Test
    public void testParseWithHeader() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("Name,Age\nJohn,30\nMike,25"), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age"}, record.getHeader());
        assertArrayEquals(new String[]{"John", "30"}, record.getValues());
        assertNull(record.getComment());
        assertEquals(1, record.getRecordNumber());

        record = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record.getHeader());
        assertArrayEquals(new String[]{"Mike", "25"}, record.getValues());
        assertNull(record.getComment());
        assertEquals(2, record.getRecordNumber());
    }

    /**
     * Test parsing of CSV file with empty line.
     */
    @Test
    public void testParseWithEmptyLine() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc\nd,e"), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"a", "b"}, record1.getValues());
        assertNull(record1.getComment());
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{}, record2.getValues());
        assertNull(record2.getComment());
        assertEquals(2, record2.getRecordNumber());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"d", "e"}, record3.getValues());
        assertNull(record3.getComment());
        assertEquals(3, record3.getRecordNumber());
    }

    /**
     * Test parsing of CSV file with comments.
     */
    @Test
    public void testParseWithComments() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b# This is a comment\nc,d"), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"a", "b"}, record1.getValues());
        assertEquals("This is a comment", record1.getComment());
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"c", "d"}, record2.getValues());
        assertNull(record2.getComment());
        assertEquals(2, record2.getRecordNumber());
    }

    /**
     * Test parsing of CSV file with multiple records and null values.
     */
    @Test
    public void testParseWithNullValues() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("Name,Age\nJohn,30\nMike,null"), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getHeader());
        assertArrayEquals(new String[]{"John", "30"}, record1.getValues());
        assertNull(record1.getComment());
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getHeader());
        assertArrayEquals(new String[]{"Mike", null}, record2.getValues());
        assertNull(record2.getComment());
        assertEquals(2, record2.getRecordNumber());
    }

    /**
     * Test parsing of CSV file with empty header.
     */
    @Test
    public void testParseWithEmptyHeader() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader(""), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        assertTrue(records.isEmpty());
    }

    /**
     * Test parsing of CSV file with different formats.
     */
    @Test
    public void testParseWithDifferentFormats() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc,d\n"), CSVFormat.EXCEL);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"a", "b"}, record1.getValues());
        assertNull(record1.getComment());
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"c", "d"}, record2.getValues());
        assertNull(record2.getComment());
        assertEquals(2, record2.getRecordNumber());

        parser = CSVParser.parse(new StringReader("a,b\nc,d\n"), CSVFormat.RFC_DELIMITED_CSV);

        records.clear();
        parser.getRecords(records);

        record1 = records.get(0);
        assertArrayEquals(new String[]{"a", "c"}, record1.getValues());
        assertNull(record1.getComment());
        assertEquals(1, record1.getRecordNumber());

        record2 = records.get(1);
        assertArrayEquals(new String[]{"b", "d"}, record2.getValues());
        assertNull(record2.getComment());
        assertEquals(2, record2.getRecordNumber());
    }

    /**
     * Test parsing of CSV file with missing records.
     */
    @Test
    public void testParseWithMissingRecords() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\n"), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"a", "b"}, record1.getValues());
        assertNull(record1.getComment());
        assertEquals(1, record1.getRecordNumber());

        assertTrue(records.isEmpty());
    }

    /**
     * Test closing and reopening the parser.
     */
    @Test
    public void testParseCloseAndReopen() throws IOException {
        StringReader reader = new StringReader("a,b\nc,d");
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        parser.close();

        parser = CSVParser.parse(reader, CSVFormat.DEFAULT);

        records.clear();
        parser.getRecords(records);

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"a", "b"}, record1.getValues());
        assertNull(record1.getComment());
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"c", "d"}, record2.getValues());
        assertNull(record2.getComment());
        assertEquals(2, record2.getRecordNumber());

        parser.close();
    }

    /**
     * Test parsing of CSV file with EOF exception.
     */
    @Test(expected = IOException.class)
    public void testParseWithEOF() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b"), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        parser.nextRecord();

        records.clear();
        parser.getRecords(records);

        throw new AssertionError("Should have thrown IOException");
    }

    /**
     * Test parsing of CSV file with INVALID token exception.
     */
    @Test(expected = IOException.class)
    public void testParseWithInvalidToken() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nX"), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        throw new AssertionError("Should have thrown IOException");
    }

    // Add more test cases as needed

    /**
     * Test parsing of CSV file with EORECORD token exception.
     */
    @Test(expected = NoSuchElementException.class)
    public void testParseWithEORecord() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b"), CSVFormat.DEFAULT);

        parser.nextRecord();
        parser.nextRecord();

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        throw new AssertionError("Should have thrown NoSuchElementException");
    }

    /**
     * Test parsing of CSV file with EOF and EORECORD token exceptions.
     */
    @Test(expected = NoSuchElementException.class)
    public void testParseWithEOFAndEORecord() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b"), CSVFormat.DEFAULT);

        parser.nextRecord();

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        parser.getRecords(records);

        parser.nextRecord();

        throw new AssertionError("Should have thrown NoSuchElementException");
    }

}
package org.apache.commons.csv;

import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFactory;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.ext.ExtendedBufferedReader;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test file for the CSVParser class.
 */
public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        String testData = "a;b\nc;d\ne:f\nf:g";
        parser = parse(new StringReader(testData), EXCEL);
    }

    /**
     * Test getRecords() method with empty list as argument.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testGetRecordsEmptyList() throws IOException {
        List<CSVRecord> records = parser.getRecords(Collections.emptyList());
        assertEquals(2, records.size());
        assertEquals("a;b", records.get(0).get(0));
        assertEquals("c;d", records.get(0).get(1));
        assertEquals("e:f", records.get(1).get(0));
        assertEquals("f:g", records.get(1).get(1));
    }

    /**
     * Test getRecords() method with ArrayList as argument.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testGetRecordsArrayList() throws IOException {
        List<CSVRecord> records = new ArrayList<>();
        parser.getRecords(records);
        assertEquals(2, records.size());
        assertEquals("a;b", records.get(0).get(0));
        assertEquals("c;d", records.get(0).get(1));
        assertEquals("e:f", records.get(1).get(0));
        assertEquals("f:g", records.get(1).get(1));
    }

    /**
     * Test getRecords() method with LinkedHashMap as argument.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testGetRecordsLinkedHashMap() throws IOException {
        List<CSVRecord> records = new ArrayList<>();
        Map<String, Integer> headerMap = new LinkedHashMap<>();
        headerMap.put("ColumnA", 0);
        headerMap.put("ColumnB", 1);

        parser.getRecords(records, headerMap);
        assertEquals(2, records.size());
        assertEquals("a;b", records.get(0).get(headerMap.get("ColumnA")));
        assertEquals("c;d", records.get(0).get(headerMap.get("ColumnB")));
        assertEquals("e:f", records.get(1).get(headerMap.get("ColumnA")));
        assertEquals("f:g", records.get(1).get(headerMap.get("ColumnB")));
    }

    /**
     * Test getRecordsIterator() method.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testGetRecordsIterator() throws IOException {
        CSVRecordIterator it = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (it.hasNext()) {
            records.add(it.next());
        }
        assertEquals(2, records.size());
        assertEquals("a;b", records.get(0).get(0));
        assertEquals("c;d", records.get(0).get(1));
        assertEquals("e:f", records.get(1).get(0));
        assertEquals("f:g", records.get(1).get(1));
    }

    /**
     * Test getHeaderMap() method.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testGetHeaderMap() throws IOException {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(2, headerMap.size());
        assertEquals("ColumnA", headerMap.get("ColumnA").toString());
        assertEquals("ColumnB", headerMap.get("ColumnB").toString());
    }

    /**
     * Test getRecordNumber() method.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testGetRecordNumber() throws IOException {
        assertEquals(0, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(1, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(2, parser.getRecordNumber());
    }

    /**
     * Test getCurrentLineNumber() method.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testGetCurrentLineNumber() throws IOException {
        assertEquals(1, parser.getCurrentLineNumber());
        parser.nextRecord();
        assertEquals(2, parser.getCurrentLineNumber());
    }

    /**
     * Test isClosed() method.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testIsClosed() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    /**
     * Test nextRecord() method with empty record.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testNextRecordEmptyRecord() throws IOException {
        CSVRecord record = parser.nextRecord();
        assertNull(record);
        assertEquals(1, parser.getCurrentLineNumber());
    }

    /**
     * Test nextRecord() method with single record.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testNextRecordSingleRecord() throws IOException {
        CSVRecord record = parser.nextRecord();
        assertEquals("a;b", record.get(0).toString());
        assertEquals("c;d", record.get(1).toString());
        assertEquals(2, parser.getCurrentLineNumber());

        record = parser.nextRecord();
        assertEquals("e:f", record.get(0).toString());
        assertEquals("f:g", record.get(1).toString());
        assertEquals(3, parser.getCurrentLineNumber());
    }

    /**
     * Test nextRecord() method with multiple records.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testNextRecordMultipleRecords() throws IOException {
        CSVRecord record1 = parser.nextRecord();
        assertEquals("a;b", record1.get(0).toString());
        assertEquals("c;d", record1.get(1).toString());
        assertEquals(2, parser.getCurrentLineNumber());

        CSVRecord record2 = parser.nextRecord();
        assertEquals("e:f", record2.get(0).toString());
        assertEquals("f:g", record2.get(1).toString());
        assertEquals(3, parser.getCurrentLineNumber());

        record1 = parser.nextRecord();
        assertEquals("", record1.get(0), "Record 3 should be empty");
        assertEquals(4, parser.getCurrentLineNumber());

        record2 = parser.nextRecord();
        assertNull(record2);
        assertEquals(4, parser.getCurrentLineNumber());
    }

    /**
     * Test nextRecord() method with trailing comment.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testNextRecordWithComment() throws IOException {
        String testData = "a;b\nc;d# This is a comment\ne:f\nf:g# Another comment";
        CSVParser parser = parse(new StringReader(testData), EXCEL);

        CSVRecord record1 = parser.nextRecord();
        assertEquals("a;b", record1.get(0).toString());
        assertEquals("c;d", record1.get(1).toString());
        assertEquals(2, parser.getCurrentLineNumber());

        CSVRecord record2 = parser.nextRecord();
        assertEquals("e:f", record2.get(0).toString());
        assertEquals("f:g", record2.get(1).toString());
        assertEquals(4, parser.getCurrentLineNumber());

        record1 = parser.nextRecord();
        assertEquals("", record1.get(0), "Record 3 should be empty");
        assertEquals(5, parser.getCurrentLineNumber());

        assertNull(parser.nextRecord());
        assertEquals(5, parser.getCurrentLineNumber());
    }

    /**
     * Test nextRecord() method with multi-line record.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testNextRecordWithMultiLineRecord() throws IOException {
        String testData = "a;b\nc:d\n e:f\nf:g";
        CSVParser parser = parse(new StringReader(testData), EXCEL);

        CSVRecord record1 = parser.nextRecord();
        assertEquals("a;b", record1.get(0).toString());
        assertEquals("c:d\ne:f", record1.get(1).toString());
        assertEquals(2, parser.getCurrentLineNumber());

        CSVRecord record2 = parser.nextRecord();
        assertEquals("", record2.get(0), "Record 2 should be empty");
        assertEquals(3, parser.getCurrentLineNumber());

        assertNull(parser.nextRecord());
        assertEquals(3, parser.getCurrentLineNumber());
    }

    /**
     * Test parse() method with File.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseFile() throws IOException {
        File file = new File("target/test-classes/CSVParserTest_CSVParserTest_parseFile.csv");
        String testData = "a;b\nc;d\ne:f\nf:g";
        Files.write(Paths.get(file.getAbsolutePath()), testData.getBytes());

        CSVParser parser = parse(file, EXCEL);

        CSVRecord record1 = parser.nextRecord();
        assertEquals("a;b", record1.get(0).toString());
        assertEquals("c;d", record1.get(1).toString());
        assertEquals(1, parser.getCurrentLineNumber());

        CSVRecord record2 = parser.nextRecord();
        assertEquals("e:f", record2.get(0).toString());
        assertEquals("f:g", record2.get(1).toString());
        assertEquals(2, parser.getCurrentLineNumber());

        record1 = parser.nextRecord();
        assertEquals("", record1.get(0), "Record 3 should be empty");
        assertEquals(3, parser.getCurrentLineNumber());

        assertNull(parser.nextRecord());
        assertEquals(3, parser.getCurrentLineNumber());

        parser.close();
        Files.delete(Paths.get(file.getAbsolutePath()));
    }

    /**
     * Test parse() method with String.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseString() throws IOException {
        String testData = "a;b\nc;d\ne:f\nf:g";
        CSVParser parser = parse(new StringReader(testData), EXCEL);

        CSVRecord record1 = parser.nextRecord();
        assertEquals("a;b", record1.get(0).toString());
        assertEquals("c;d", record1.get(1).toString());
        assertEquals(1, parser.getCurrentLineNumber());

        CSVRecord record2 = parser.nextRecord();
        assertEquals("e:f", record2.get(0).toString());
        assertEquals("f:g", record2.get(1).toString());
        assertEquals(2, parser.getCurrentLineNumber());

        record1 = parser.nextRecord();
        assertEquals("", record1.get(0), "Record 3 should be empty");
        assertEquals(3, parser.getCurrentLineNumber());

        assertNull(parser.nextRecord());
        assertEquals(3, parser.getCurrentLineNumber());
    }
}
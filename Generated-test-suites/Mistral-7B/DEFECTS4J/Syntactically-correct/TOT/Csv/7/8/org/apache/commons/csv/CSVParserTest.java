package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.ext.CSVFormatEXCEL;

/**
 * Test for {@link CSVParser} class.
 */
public class CSVParserTest {

    /** Test data for parser. */
    private static final String TEST_DATA = "name,age\nJohn,30\nMary,35\n,33\n";

    /** Test data for parser with custom format. */
    private static final String TEST_DATA_EXCEL = "name,age\nJohn,30\nMary,35\n,33\n# This is a comment\n";

    /** Test data for parser with empty CSV file. */
    private static final String TEST_DATA_EMPTY = "";

    /** Test data for parser with invalid CSV file. */
    private static final String TEST_DATA_INVALID = "name,age\nJohn,30\nMary,35#This is a comment\n";

    /** Test data for parser with malformed CSV file. */
    private static final String TEST_DATA_MALFORMED = "name,age\nJohn,30\nMary,age";

    /** Test data for parser with quote in value. */
    private static final String TEST_DATA_QUOTE = "name,age\nJohn,\"O'Reilly\",30\nMary,\"Miller, Jr.\",35\n";

    /** Test data for parser with empty record. */
    private static final String TEST_DATA_EMPTY_RECORD = "name,age\nJohn,30,\nMary,35\n";

    /** Test data for parser with header record. */
    private static final String TEST_DATA_HEADER = "name,age\nName,Age\nJohn,30\nMary,35\n";

    @org.junit.Test
    public void testParse() throws IOException {
        // Test parsing with default format
        CSVParser parser = CSVParser.parse(new ByteArrayInputStream(TEST_DATA.getBytes()), null);
        List<CSVRecord> records = parser.getRecords();
        assertSame(3, records.size());
        CSVRecord record = records.get(0);
        assertEquals("name", record.get(0));
        assertEquals("John", record.get(1));
        record = records.get(1);
        assertEquals("name", record.get(0));
        assertEquals("Mary", record.get(1));
        assertNull(records.get(2).get(1));
        parser.close();

        // Test parsing with custom format
        CSVFormat format = new CSVFormatEXCEL();
        parser = new CSVParser(new StringReader(TEST_DATA_EXCEL), format);
        records = parser.getRecords();
        assertSame(3, records.size());
        record = records.get(0);
        assertEquals("name", record.getColumnName(0));
        assertEquals("John", record.get(0));
        assertEquals("age", record.getColumnName(1));
        assertEquals("30", record.get(1));
        record = records.get(1);
        assertEquals("name", record.getColumnName(0));
        assertEquals("Mary", record.get(0));
        assertEquals("age", record.getColumnName(1));
        assertEquals("35", record.get(1));
        assertNull(records.get(2).get(1));
        assertEquals("This is a comment", records.get(2).getComment());
        parser.close();

        // Test parsing with empty CSV file
        parser = CSVParser.parse(new StringReader(TEST_DATA_EMPTY), null);
        assertSame(0, parser.getRecords().size());
        parser.close();

        // Test parsing with invalid CSV file
        parser = CSVParser.parse(new StringReader(TEST_DATA_INVALID), null);
        assertSame(1, parser.getRecords().size());
        CSVRecord invalidRecord = parser.getRecords().get(0);
        assertEquals(0, invalidRecord.size());
        parser.close();

        // Test parsing with malformed CSV file
        parser = CSVParser.parse(new StringReader(TEST_DATA_MALFORMED), null);
        assertSame(1, parser.getRecords().size());
        CSVRecord malformedRecord = parser.getRecords().get(0);
        assertEquals("name,age", malformedRecord.getHeaderNames());
        assertEquals(2, malformedRecord.size());
        assertEquals("name", malformedRecord.get(0));
        assertEquals("age", malformedRecord.get(1));
        assertNull(malformedRecord.get(1));
        assertEquals("age", malformedRecord.getColumnName(1));
        parser.close();

        // Test parsing with quote in value
        parser = CSVParser.parse(new StringReader(TEST_DATA_QUOTE), null);
        records = parser.getRecords();
        assertSame(3, records.size());
        record = records.get(0);
        assertEquals("name", record.get(0));
        assertEquals("O'Reilly", record.get(1));
        record = records.get(1);
        assertEquals("name", record.get(0));
        assertEquals("Miller, Jr.", record.get(1));
        parser.close();

        // Test parsing with empty record
        parser = CSVParser.parse(new StringReader(TEST_DATA_EMPTY_RECORD), null);
        records = parser.getRecords();
        assertSame(3, records.size());
        record = records.get(2);
        assertSame(0, record.size());
        parser.close();

        // Test parsing with header record
        parser = CSVParser.parse(new StringReader(TEST_DATA_HEADER), null);
        records = parser.getRecords();
        assertSame(3, records.size());
        record = records.get(0);
        assertEquals("Name", record.getColumnName(0));
        assertEquals("Age", record.getColumnName(1));
        record = records.get(1);
        assertEquals("John", record.get(0));
        assertEquals("30", record.get(1));
        record = records.get(2);
        assertEquals("Mary", record.get(0));
        assertEquals("35", record.get(1));
        parser.close();

        // Test parsing with custom format and iterator
        CSVParser parserWithIterator = CSVParser.parse(new StringReader(TEST_DATA), CSVFormat.DEFAULT);
        CSVRecordIterator iterator = parserWithIterator.iterator();
        CSVRecord recordIterator = iterator.next();
        assertEquals("name", recordIterator.get(0));
        assertEquals("John", recordIterator.get(1));
        assertEquals(null, recordIterator.getComment());
        recordIterator = iterator.next();
        assertEquals("name", recordIterator.get(0));
        assertEquals("Mary", recordIterator.get(1));
        assertEquals(null, recordIterator.getComment());
        assertNull(iterator.next());
        parserWithIterator.close();
    }
}
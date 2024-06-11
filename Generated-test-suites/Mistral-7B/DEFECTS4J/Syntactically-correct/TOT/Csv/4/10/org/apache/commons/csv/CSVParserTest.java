package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;

/**
 * Tests for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Tests parsing a simple CSV file with no header.
     */
    @org.junit.Test
    public void testSimpleCsv() throws IOException {
        final String csv = "a,b,c\n1,2,3\n4,5,6";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream);
        final CSVFormat format = CSVFormat.DEFAULT.withHeader();
        final CSVParser parser = CSVParser.parse(reader, format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(3, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", record.get(2));
        assertEquals(1, record.getRecordNumber());

        CSVRecordIterator iterator = parser.iterator();
        assertEquals(record, iterator.next());
        assertNull(iterator.next());
    }

    /**
     * Tests parsing a CSV file with a header.
     */
    @org.junit.Test
    public void testCsvWithHeader() throws IOException {
        final String csv = "Name,Age,City\nAlice,25,New York\nBob,30,Chicago\n";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream);
        final CSVFormat format = CSVFormat.DEFAULT.withHeader();
        final CSVParser parser = CSVParser.parse(reader, format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(3, record1.size());
        assertEquals("Name", record1.getHeaderName(0));
        assertEquals("Alice", record1.get(0));
        assertEquals("Age", record1.getHeaderName(1));
        assertEquals(25, record1.get(1).intValue());
        assertEquals("City", record1.getHeaderName(2));
        assertEquals("New York", record1.get(2));
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertEquals(3, record2.size());
        assertEquals("Name", record2.getHeaderName(0));
        assertEquals("Bob", record2.get(0));
        assertEquals("Age", record2.getHeaderName(1));
        assertEquals(30, record2.get(1).intValue());
        assertEquals("City", record2.getHeaderName(2));
        assertEquals("Chicago", record2.get(2));
        assertEquals(2, record2.getRecordNumber());

        CSVRecordIterator iterator = parser.iterator();
        assertEquals(record1, iterator.next());
        assertEquals(record2, iterator.next());
        assertNull(iterator.next());
    }

    /**
     * Tests parsing an empty CSV file.
     */
    @org.junit.Test
    public void testEmptyCsv() throws IOException {
        final String csv = "";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream);
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVParser parser = CSVParser.parse(reader, format);
        assertThrows(NoSuchElementException.class, () -> parser.nextRecord());
        parser.close();
    }

    /**
     * Tests parsing an invalid CSV file.
     */
    @org.junit.Test
    public void testInvalidCsv() throws IOException {
        final String csv = "Name,Age,City\nAlice,25\nBob,30,Chicago\nName,Age,City\n";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream);
        final CSVFormat format = CSVFormat.DEFAULT.withHeader();
        final CSVParser parser = CSVParser.parse(reader, format);
        assertThrows(IOException.class, () -> parser.nextRecord());
        parser.close();
    }

    /**
     * Tests parsing a CSV file with a missing header.
     */
    @org.junit.Test
    public void testMissingHeaderCsv() throws IOException {
        final String csv = "a,b,c\n1,2,3\n4,5,6";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream);
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVParser parser = CSVParser.parse(reader, format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(3, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", record.get(2));
        assertEquals(1, record.getRecordNumber());

        CSVRecordIterator iterator = parser.iterator();
        assertEquals(record, iterator.next());
        assertNull(iterator.next());
    }

    /**
     * Tests parsing a CSV file with a header and a missing value.
     */
    @org.junit.Test
    public void testMissingValueCsv() throws IOException {
        final String csv = "Name,Age,City\nAlice,,New York\nBob,30,Chicago\n";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream);
        final CSVFormat format = CSVFormat.DEFAULT.withHeader();
        final CSVParser parser = CSVParser.parse(reader, format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(3, record1.size());
        assertEquals("Name", record1.getHeaderName(0));
        assertEquals("Alice", record1.get(0));
        assertEquals("Age", record1.getHeaderName(1));
        assertNull(record1.get(1));
        assertEquals("City", record1.getHeaderName(2));
        assertEquals("New York", record1.get(2));
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertEquals(3, record2.size());
        assertEquals("Name", record2.getHeaderName(0));
        assertEquals("Bob", record2.get(0));
        assertEquals("Age", record2.getHeaderName(1));
        assertEquals(30, record2.get(1).intValue());
        assertEquals("City", record2.getHeaderName(2));
        assertEquals("Chicago", record2.get(2));
        assertEquals(2, record2.getRecordNumber());

        CSVRecordIterator iterator = parser.iterator();
        assertEquals(record1, iterator.next());
        assertEquals(record2, iterator.next());
        assertNull(iterator.next());
    }
}
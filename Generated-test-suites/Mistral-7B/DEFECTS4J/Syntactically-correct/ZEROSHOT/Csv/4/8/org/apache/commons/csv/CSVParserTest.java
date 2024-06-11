package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test method for {@link CSVParser#parse(File, CSVFormat)}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testParseFile() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";
        final File file = new File("test.csv");
        file.getParentFile().mkdirs();
        try (final FileWriter fileWriter = new FileWriter(file);) {
            fileWriter.write(csvContent);
        }

        final CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Test record content
        final CSVRecord firstRecord = records.get(0);
        assertSame(TYPE, firstRecord.getRecordType());
        assertEquals("name", firstRecord.getHeaderMap().get("name"));
        assertEquals("Alice", firstRecord.get(0));
        assertEquals(23, firstRecord.get(1).intValue());

        // Test record count
        assertEquals(3, records.size());

        // Test closing the parser closes the file
        parser.close();
        assertFalse(file.exists());

        // Test parsing an empty file
        parser = parse(file, CSVFormat.DEFAULT);
        records = parser.getRecords();
        assertSame(List.class, records.getClass());
        assertEmpty(records);
        parser.close();
        assertFalse(file.exists());
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testParseString() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";
        final String csv = "name,age\nAlice,23\nBob,31\nCharlie,29";

        final CSVParser parser = parse(csv, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Test record content
        final CSVRecord firstRecord = records.get(0);
        assertSame(TYPE, firstRecord.getRecordType());
        assertEquals("name", firstRecord.getHeaderMap().get("name"));
        assertEquals("Alice", firstRecord.get(0));
        assertEquals(23, firstRecord.get(1).intValue());

        // Test record count
        assertEquals(3, records.size());

        // Test iterating through records
        int i = 0;
        for (CSVRecord record : parser) {
            i++;
            assertEquals(i, record.getRecordNumber());
            assertEquals(i % 2 == 0 ? "Alice" : "Bob", record.get(0));
            assertEquals(i % 2 == 0 ? 23 : 31, record.get(1).intValue());
        }

        // Test parsing an empty string
        parser = parse("", CSVFormat.DEFAULT);
        List<CSVRecord> emptyRecords = parser.getRecords();
        assertSame(List.class, emptyRecords.getClass());
        assertEmpty(emptyRecords);
    }

    /**
     * Test method for {@link CSVParser#parse(InputStreamReader, CSVFormat)}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testParseInputStreamReader() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        try (final InputStreamReader reader = new InputStreamReader(inputStream)) {
            final CSVParser parser = parse(reader, CSVFormat.DEFAULT);
            List<CSVRecord> records = parser.getRecords();

            // Test record content
            final CSVRecord firstRecord = records.get(0);
            assertSame(TYPE, firstRecord.getRecordType());
            assertEquals("name", firstRecord.getHeaderMap().get("name"));
            assertEquals("Alice", firstRecord.get(0));
            assertEquals(23, firstRecord.get(1).intValue());

            // Test record count
            assertEquals(3, records.size());

            // Test iterating through records
            int i = 0;
            for (CSVRecord record : parser) {
                i++;
                assertEquals(i, record.getRecordNumber());
                assertEquals(i % 2 == 0 ? "Alice" : "Bob", record.get(0));
                assertEquals(i % 2 == 0 ? 23 : 31, record.get(1).intValue());
            }
        }
    }

    /**
     * Test method for {@link CSVParser#parse(StringReader, CSVFormat)}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testParseStringReader() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";
        final StringReader stringReader = new StringReader(csvContent);

        final CSVParser parser = parse(stringReader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Test record content
        final CSVRecord firstRecord = records.get(0);
        assertSame(TYPE, firstRecord.getRecordType());
        assertEquals("name", firstRecord.getHeaderMap().get("name"));
        assertEquals("Alice", firstRecord.get(0));
        assertEquals(23, firstRecord.get(1).intValue());

        // Test record count
        assertEquals(3, records.size());

        // Test iterating through records
        int i = 0;
        for (CSVRecord record : parser) {
            i++;
            assertEquals(i, record.getRecordNumber());
            assertEquals(i % 2 == 0 ? "Alice" : "Bob", record.get(0));
            assertEquals(i % 2 == 0 ? 23 : 31, record.get(1).intValue());
        }
    }

    /**
     * Test method for {@link CSVParser#getHeaderMap()}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testGetHeaderMap() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";

        final CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(2, headerMap.size());
        assertEquals("name", headerMap.get("name"));
        assertEquals(1, headerMap.get("age").intValue());
    }

    /**
     * Test method for {@link CSVParser#getRecordNumber()}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testGetRecordNumber() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";

        final CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertEquals(1, parser.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Alice", record.get(0));
        assertEquals(23, record.get(1).intValue());

        record = parser.nextRecord();
        assertEquals(2, parser.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Bob", record.get(0));
        assertEquals(31, record.get(1).intValue());

        record = parser.nextRecord();
        assertNull(parser.nextRecord());
        assertEquals(3, parser.getRecordNumber());
    }

    /**
     * Test method for {@link CSVParser#getRecords()}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testGetRecords() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";

        final CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (CSVRecord record : records) {
            assertEquals(CSVRecord.TYPE, record.getRecordType());
            assertEquals(3, record.getRecordNumber());
            assertEquals("name", record.getHeaderMap().get("name"));
            assertEquals("Alice", record.get(0));
            assertEquals(23, record.get(1).intValue());
        }
    }

    /**
     * Test method for {@link CSVParser#hasNext()}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testHasNext() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";

        final CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        assertTrue(parser.hasNext());
        CSVRecord record = parser.nextRecord();
        assertEquals("Alice", record.get(0));
        assertEquals(23, record.get(1).intValue());

        assertTrue(parser.hasNext());
        record = parser.nextRecord();
        assertEquals("Bob", record.get(0));
        assertEquals(31, record.get(1).intValue());

        assertTrue(parser.hasNext());
        record = parser.nextRecord();
        assertEquals("Charlie", record.get(0));
        assertEquals(29, record.get(1).intValue());

        assertFalse(parser.hasNext());
    }

    /**
     * Test method for {@link CSVParser#nextRecord()}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testNextRecord() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";

        final CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertEquals("Alice", record.get(0));
        assertEquals(23, record.get(1).intValue());

        record = parser.nextRecord();
        assertEquals("Bob", record.get(0));
        assertEquals(31, record.get(1).intValue());

        record = parser.nextRecord();
        assertEquals("Charlie", record.get(0));
        assertEquals(29, record.get(1).intValue());

        assertNull(parser.nextRecord());
    }

    /**
     * Test method for {@link CSVParser#iterator()}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testIterator() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";

        final CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<CSVRecord>();
        for (CSVRecord record : parser) {
            records.add(record);
        }

        assertEquals(3, records.size());
        assertEquals("Alice", records.get(0).get(0));
        assertEquals(23, records.get(0).get(1).intValue());
        assertEquals("Bob", records.get(1).get(0));
        assertEquals(31, records.get(1).get(1).intValue());
        assertEquals("Charlie", records.get(2).get(0));
        assertEquals(29, records.get(2).get(1).intValue());
    }

    /**
     * Test method for {@link CSVParser#iterator()}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testClose() throws IOException {
        final String csvContent = "name,age\nAlice,23\nBob,31\nCharlie,29";

        final CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<CSVRecord>();
        for (CSVRecord record : parser) {
            records.add(record);
        }

        // Test closing the parser closes the input reader
        parser.close();
        assertFalse(parser.hasNext());

        // Test reusing the parser throws an exception
        try {
            while (parser.hasNext()) {
                parser.nextRecord();
            }
        } catch (final NoSuchElementException e) {
            // expected
        }
    }

    /**
     * Test method for {@link CSVParser#iterator()}.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testParserWithEmptyLine() throws IOException {
        final String csvContent = "name,age\nAlice,23\n\nBob,31\nCharlie,29";

        final CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<CSVRecord>();
        for (CSVRecord record : parser) {
            records.add(record);
        }

        assertEquals(3, records.size());
        assertEquals("Alice", records.get(0).get(0));
        assertEquals(23, records.get(0).get(1).intValue());
        assertEquals("Bob", records.get(1).get(0));
        assertEquals(31, records.get(1).get(1).intValue());
        assertEquals("Charlie", records.get(2).get(0));
        assertEquals(29, records.get(2).get(1).intValue());

        // Test closing the parser closes the input reader
        parser.close();
        assertFalse(parser.hasNext());
    }
}
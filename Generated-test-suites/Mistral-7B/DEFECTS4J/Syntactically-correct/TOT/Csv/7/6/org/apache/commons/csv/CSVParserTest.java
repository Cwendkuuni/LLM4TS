package org.apache.commons.csv;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVRecord.TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.ext.ExtendedBufferedReader;

/**
 * Test case for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file with no header.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws IOException {
        final String csvData = "a,b\nc,d\ne,f";
        final CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes()),
                DEFAULT.getCharset()));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            assertEquals("a", record.get(0));
            assertEquals("b", record.get(1));
            if (i == 1) {
                assertEquals("c", record.get(0));
                assertEquals("d", record.get(1));
            }
        }
    }

    /**
     * Test parsing a CSV file with a header.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testParseCSVWithHeader() throws IOException {
        final String csvData = "Name,Age\nAlice,25\nBob,30\nCharlie,35";
        final CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes()),
                DEFAULT.getCharset()));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            if (i == 0) {
                assertEquals("Name", record.getHeaderName(0));
                assertEquals("Age", record.getHeaderName(1));
            }
            if (i == 1) {
                assertEquals("Alice", record.get(0));
                assertEquals("25", record.get(1));
            }
            if (i == 2) {
                assertEquals("Bob", record.get(0));
                assertEquals("30", record.get(1));
            }
        }
    }

    /**
     * Test parsing a CSV file with empty values.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testParseCSVWithEmptyValues() throws IOException {
        final String csvData = "Name,Age\nAlice,,25\nBob,,30\nCharlie, ,35";
        final CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes()),
                DEFAULT.getCharset()));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            if (i == 1) {
                assertNull(record.get(0));
                assertEquals("25", record.get(1));
            }
            if (i == 2) {
                assertNull(record.get(0));
                assertEquals("30", record.get(1));
            }
            if (i == 0 || i == 2) {
                assertNull(record.getHeaderName(1));
            }
            if (i == 1) {
                assertEquals("Alice", record.get(0));
                assertEquals(TYPE.STRING, record.get(1).getType());
            }
            if (i == 2) {
                assertEquals("Charlie", record.get(0));
                assertEquals(TYPE.STRING, record.get(1).getType());
            }
        }
    }

    /**
     * Test handling of comments.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testParseCSVWithComments() throws IOException {
        final String csvData = "Name,Age#This is a comment\nAlice,25\nBob,30\nCharlie,35";
        final CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes()),
                DEFAULT.getCharset()));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            if (i == 1) {
                assertEquals("Alice", record.get(0));
                assertEquals("25", record.get(1));
                assertEquals("This is a comment\n", record.getComment());
            }
            if (i == 2) {
                assertEquals("Charlie", record.get(0));
                assertEquals("35", record.get(1));
                assertNull(record.getComment());
            }
        }
    }

    /**
     * Test parsing a CSV file with an empty line.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testParseCSVWithEmptyLine() throws IOException {
        final String csvData = "Name,Age\nAlice,25\n\nBob,30\nCharlie,35";
        final CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes()),
                DEFAULT.getCharset()));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            if (i == 1) {
                assertNull(record.get(0));
                assertEquals("", record.get(1));
            }
            if (i == 2) {
                assertEquals("Bob", record.get(0));
                assertEquals("30", record.get(1));
            }
            if (i == 0 || i == 2) {
                assertNull(record.getHeaderName(1));
            }
            if (i == 1) {
                assertNull(record.getHeaderName(0));
            }
            if (i == 2) {
                assertEquals("Charlie", record.get(0));
                assertEquals("35", record.get(1));
            }
        }
    }

    /**
     * Test parsing a CSV file with an invalid record.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testParseCSVWithInvalidRecord() throws IOException {
        final String csvData = "Name,Age\nAlice,25\nBob,30\nCharlie,35\nName,Age";
        final CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes()),
                DEFAULT.getCharset()));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            if (i == 3) {
                assertThrows(IOException.class, () -> record.get(0));
                assertThrows(IOException.class, () -> record.get(1));
            }
        }
    }

    /**
     * Test iterating over a CSV file.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testParseCSVWithIterator() throws IOException {
        final String csvData = "Name,Age\nAlice,25\nBob,30\nCharlie,35";
        final CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes()),
                DEFAULT.getCharset()));
        CSVRecordIterator iterator = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : iterator) {
            records.add(record);
        }
        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            if (i == 1) {
                assertEquals("Alice", record.get(0));
                assertEquals("25", record.get(1));
            }
            if (i == 2) {
                assertEquals("Bob", record.get(0));
                assertEquals("30", record.get(1));
            }
            if (i == 0 || i == 2) {
                assertNull(record.getHeaderName(1));
            }
            if (i == 1) {
                assertNull(record.getHeaderName(0));
            }
            if (i == 2) {
                assertEquals("Charlie", record.get(0));
                assertEquals("35", record.get(1));
            }
        }
        assertNull(iterator.next());
    }

    /**
     * Test parsing a CSV file with a custom format.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testParseCSVWithCustomFormat() throws IOException {
        final String csvData = "Name,Age\nAlice,25\nBob,30\nCharlie,35";
        final CSVFormat format = new CSVFormatBuilder().setHeaderNames(Collections.singletonList("Name"))
                .setColumnNames(Arrays.asList("firstName", "lastName"))
                .build();
        final CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes()),
                format.getCharset()));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            if (i == 1) {
                assertEquals("Alice", record.get(0));
                assertEquals("Alice", record.get("firstName"));
                assertEquals("", record.get("lastName"));
                assertEquals("25", record.get(1));
            }
            if (i == 2) {
                assertEquals("Bob", record.get(0));
                assertEquals("Bob", record.get("firstName"));
                assertEquals("", record.get("lastName"));
                assertEquals("30", record.get(1));
            }
            if (i == 0 || i == 2) {
                assertNull(record.getHeaderName(1));
            }
            if (i == 1) {
                assertEquals("Name", record.getHeaderName(0));
            }
            if (i == 2) {
                assertEquals("Name", record.getHeaderName(0));
            }
            if (i == 1) {
                assertEquals("Alice", record.get(0));
                assertEquals("Alice", record.get("firstName"));
                assertEquals("", record.get("lastName"));
                assertEquals("25", record.get(1));
            }
            if (i == 2) {
                assertEquals("Charlie", record.get(0));
                assertEquals("Charlie", record.get("firstName"));
                assertEquals("", record.get("lastName"));
                assertEquals("35", record.get(1));
            }
        }
    }

}
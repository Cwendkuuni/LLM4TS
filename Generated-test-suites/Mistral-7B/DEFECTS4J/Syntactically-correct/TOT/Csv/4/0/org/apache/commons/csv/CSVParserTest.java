package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.commons.csv.CSVFormat.RFC4180;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

/**
 * Tests for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Tests parsing a simple CSV file with the default format.
     */
    @org.junit.Test
    public void testDefaultFormat() throws IOException {
        final String csvContent = "name,age\nAlice,30\nBob,25\n";
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("name", record1.get(0));
        assertEquals("Alice", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals("name", record2.get(0));
        assertEquals("Bob", record2.get(1));
    }

    /**
     * Tests parsing a CSV file with the RFC4180 format.
     */
    @org.junit.Test
    public void testRFC4180Format() throws IOException {
        final String csvContent = "name,age\nAlice,30\nBob,25\n";
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("name", record1.get(0));
        assertEquals("Alice", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals("name", record2.get(0));
        assertEquals("Bob", record2.get(1));
    }

    /**
     * Tests parsing a CSV file with the EXCEL format.
     */
    @org.junit.Test
    public void testEXCELFormat() throws IOException {
        final String csvContent = "name,age\nAlice,30\nBob,25\n";
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), EXCEL);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("name", record1.get(0));
        assertEquals("Alice", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals("name", record2.get(0));
        assertEquals("Bob", record2.get(1));
    }

    /**
     * Tests parsing a CSV file with empty lines.
     */
    @org.junit.Test
    public void testEmptyLines() throws IOException {
        final String csvContent = "name,age\nAlice,30\n\nBob,25\n";
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("name", record1.get(0));
        assertEquals("Alice", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals("name", record2.get(0));
        assertEquals("Bob", record2.get(1));
    }

    /**
     * Tests parsing a CSV file with a comment.
     */
    @org.junit.Test
     public void testComment() throws IOException {
        final String csvContent = "name,age\nAlice,30# This is a comment\nBob,25";
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertEquals("name", record.get(0));
        assertEquals("Alice", record.get(1));
        assertNull(record.getComment());
    }

    /**
     * Tests parsing a CSV file with an invalid format.
     */
    @org.junit.Test
    public void testInvalidFormat() {
        final String csvContent = "name,ageAlice,30# This is a commentBob,25";

        assertThrows(IllegalArgumentException.class, () -> {
            parse(new StringReader(csvContent), DEFAULT);
        });
    }
}